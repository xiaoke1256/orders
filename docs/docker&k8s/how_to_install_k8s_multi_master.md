如何安装Kubernetes高可用集群
=======

### 部署规划

| IP | 主机名 |
|----|----|
| 192.168.249.131 | k8s-master |
| 192.168.249.132 | k8s-master02 |
| 192.168.249.133 | k8s-master03 |

### 一. 环境准备

修改三台机器上的主机名
```shell
hostnamectl set-hostname k8s-master
```
```shell
hostnamectl set-hostname k8s-master02
```
```shell
hostnamectl set-hostname k8s-master03
```

在三台机器上修改hosts文件
```
192.168.249.10  k8svip
192.168.249.131 k8s-master
192.168.249.132 k8s-master02
192.168.249.133 k8s-master03
```

### 二. 安装 docker 和 Kubernetes

请参考[这里](./how_to_install_k8s.md).


### 三. 安装keepalived和haproxy（所有节点上执行）

1. 安装软件包
```shell
yum install -y keepalived haproxy
```
2. 配置keepalived

k8s-master 节点上如下配置:
```shell
cat <<EOF > /etc/keepalived/keepalived.conf
! /etc/keepalived/keepalived.conf
! Configuration File for keepalived
global_defs {
    router_id LVS_DEVEL
}
vrrp_script check_apiserver {
    script "/etc/keepalived/check_apiserver.sh"
    interval 3
    weight -2
    fall 10
    rise 2
}

vrrp_instance VI_1 {
    state  MASTER
    interface ens33
    virtual_router_id  51
    priority 100
    authentication {
        auth_type PASS
        auth_pass 123456
    }
    virtual_ipaddress {
        192.168.249.10
    }
    track_script {
        check_apiserver
    }
}

EOF
```
其中 ens33 是网卡;“priority 100”是优先级；192.168.249.10 是 vip

k8s-master02和k8s-master03 节点上如下配置:
```shell
cat <<EOF > /etc/keepalived/keepalived.conf
! /etc/keepalived/keepalived.conf
! Configuration File for keepalived
global_defs {
    router_id LVS_DEVEL
}
vrrp_script check_apiserver {
    script "/etc/keepalived/check_apiserver.sh"
    interval 3
    weight -2
    fall 10
    rise 2
}

vrrp_instance VI_1 {
    state  BACKUP
    interface ens33
    virtual_router_id  51
    priority 50
    authentication {
        auth_type PASS
        auth_pass 123456
    }
    virtual_ipaddress {
        192.168.249.10
    }
    track_script {
        check_apiserver
    }
}

EOF
```

其中 ens33 是网卡;“priority 100”是优先级；192.168.249.10 是 vip

创建健康检查脚本
```shell
vi /etc/keepalived/check_apiserver.sh
### 添加内容
#!/bin/sh
 
errorExit() {
    echo "*** $*" 1>&2
    exit 1
}
 
curl --silent --max-time 2 --insecure https://localhost:8443/ -o /dev/null || errorExit "Error GET https://localhost:8443/"
if ip addr | grep -q 192.168.249.10; then
    curl --silent --max-time 2 --insecure https://192.168.249.10:8443/ -o /dev/null || errorExit "Error GET https://192.168.249.10:8443/"
fi
```

3. 配置haproxy
```
# /etc/haproxy/haproxy.cfg
#---------------------------------------------------------------------
# Global settings
#---------------------------------------------------------------------
global
    log /dev/log local0
    log /dev/log local1 notice
    daemon
 
#---------------------------------------------------------------------
# common defaults that all the 'listen' and 'backend' sections will
# use if not designated in their block
#---------------------------------------------------------------------
defaults
    mode                    http
    log                     global
    option                  httplog
    option                  dontlognull
    option http-server-close
    option forwardfor       except 127.0.0.0/8
    option                  redispatch
    retries                 1
    timeout http-request    10s
    timeout queue           20s
    timeout connect         5s
    timeout client          20s
    timeout server          20s
    timeout http-keep-alive 10s
    timeout check           10s
 
#---------------------------------------------------------------------
# apiserver frontend which proxys to the masters
#---------------------------------------------------------------------
frontend apiserver
    bind *:8443   
    mode tcp
    option tcplog
    default_backend apiserver
 
#---------------------------------------------------------------------
# round robin balancing for apiserver
#---------------------------------------------------------------------
backend apiserver
    option httpchk GET /healthz
    http-check expect status 200
    mode tcp
    option ssl-hello-chk
    balance     roundrobin
        server k8s-master 192.168.249.131:6443 check
        server k8s-master02 192.168.249.132:6443 check
        server k8s-master03 192.168.249.133:6443 check
```
启动服务并设置开机自启
```shell
systemctl enable haproxy --now
systemctl enable keepalived --now
```
### 四. 启动kubernetes集群
在 k8s-master 节点上执行，生成 kubeadm-config.yml 文件
```shell
kubeadm  config print init-defaults >kubeadm-config.yml
```

修改 kubeadm-config.yml 文件

```
apiVersion: kubeadm.k8s.io/v1beta3
bootstrapTokens:
- groups:
  - system:bootstrappers:kubeadm:default-node-token
  token: abcdef.0123456789abcdef
  ttl: 24h0m0s
  usages:
  - signing
  - authentication
kind: InitConfiguration
localAPIEndpoint:
  advertiseAddress: 192.168.249.131 #改成k8s-master节点的ip
  bindPort: 6443
nodeRegistration:
  criSocket: /var/run/dockershim.sock
  imagePullPolicy: IfNotPresent
  name: k8s-master #改成k8s-master节点的主机名
  taints: null
---
apiServer:
  timeoutForControlPlane: 4m0s
apiVersion: kubeadm.k8s.io/v1beta3
certificatesDir: /etc/kubernetes/pki
clusterName: kubernetes
controlPlaneEndpoint: "192.168.249.10:8443" #改成haproxy的vip和端口
controllerManager: {}
dns: {}
etcd:
  local:
    dataDir: /var/lib/etcd
imageRepository: k8s.gcr.io
kind: ClusterConfiguration
kubernetesVersion: 1.23.6
networking:
  dnsDomain: cluster.local
  serviceSubnet: 10.96.0.0/12
scheduler: {}

```
k8s-master 节点上执行，初始化集群命令：
```shell
kubeadm  init --config kubeadm-config.yml 
```

然后配置API所需的配置文件

```shell
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
安装集群网络组件calico（flannel不行，请直接用calico）
```shell
kubectl apply -f calico.yaml
```

在 master02 和 master03上执行，创建证书目录
```shell
mkdir  -p /etc/kubernetes/pki/etcd
```
在master点上执行， 把master节点上把证书文件拷贝到master02 和 master03节点上
```shell
vi cpkey.sh

USER=root # 账号
CONTROL_PLANE_IPS="192.168.249.132 192.168.249.133" #节点IP
dir=/etc/kubernetes/pki/
for host in ${CONTROL_PLANE_IPS}; do
    scp /etc/kubernetes/pki/ca.crt "${USER}"@$host:${dir}
    scp /etc/kubernetes/pki/ca.key "${USER}"@$host:${dir}
    scp /etc/kubernetes/pki/sa.key "${USER}"@$host:${dir}
    scp /etc/kubernetes/pki/sa.pub "${USER}"@$host:${dir}
    scp /etc/kubernetes/pki/front-proxy-ca.crt "${USER}"@$host:${dir}
    scp /etc/kubernetes/pki/front-proxy-ca.key "${USER}"@$host:${dir}
    scp /etc/kubernetes/pki/etcd/ca.crt "${USER}"@$host:${dir}etcd      
    #如果您使用的是外部etcd，请引用此行
    scp /etc/kubernetes/pki/etcd/ca.key "${USER}"@$host:${dir}etcd
done

./cpkey.sh
```
master02 和 master03节点 加入集群
```shell
# 加入集群
kubeadm join 192.168.249.10:8443 --token s6a40e.4fyob3lury5mydby \
        --discovery-token-ca-cert-hash sha256:0afe17cd028666174d3b9a5c48ba9878d5bc2fe1022a08d1145e12fcdde0cd2b \
        --control-plane
        
 # 配置API所需的配置文件
 mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

最后查看集群状态
```shell
kubectl get nodes
```

### 遇到的问题
1. kubeadm join 时在“Checking that the etcd cluster is healthy”卡住

请参考[这里](https://blog.csdn.net/hunheidaode/article/details/118341102)

或[这里](https://blog.csdn.net/weixin_44946147/article/details/124944268)

2. coredns-xxxxxxxxx-xxxxx 一直处于 ContainerCreating 状态

   需要在对应的work节点创建/run/flannel/subnet.env文件，内容如下：
```
FLANNEL_NETWORK=10.244.0.0/16
FLANNEL_SUBNET=10.244.1.1/24
FLANNEL_MTU=1450
FLANNEL_IPMASQ=true
```

3. calico-kube-controllers-xxxxxxxxx-xxxxx pod 一直处于 CrashLoopBackOff 状态
    
 往往是 calico-node pod 没有创建成功。请想办法解决 calico-node 的创建问题。

4. calico-node-xxxxx 拉取镜像失败,报 ImagePullBackOff 错误

在容器外执行一下拉取镜像操作：
```shell
docker pull docker.io/calico/kube-controllers:v3.25.0
```