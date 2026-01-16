k8s-master:
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

k8s-master02和k8s-master03:
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
配置haproxy
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

k8s-master 节点上生成 kubeadm-config.yml 文件
```shell
[root@k8s-master ~]# kubeadm  config print init-defaults >kubeadm-config.yml
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
k8s-master 节点上执行初始化集群命令：
```shell
[root@k8s-master ~]# kubeadm  init --config kubeadm-config.yml 
```

配置API所需的配置文件

```shell
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```
安装集群网络组件flannel
```shell
kubectl apply -f kube-flannel.yml
```

master02 master03加入集群
```shell
[root@k8s-master03 kubernetes]# mkdir  -p /etc/kubernetes/pki/etc
# 主节点上把证书文件拷贝到从节点上
[root@k8s-master ~]# vi cpkey.sh

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

[root@k8s-master ~]# ./cpkey.sh

# 加入集群
[root@k8s-master03 kubernetes]#   kubeadm join 192.168.249.10:8443 --token s6a40e.4fyob3lury5mydby \
        --discovery-token-ca-cert-hash sha256:0afe17cd028666174d3b9a5c48ba9878d5bc2fe1022a08d1145e12fcdde0cd2b \
        --control-plane
        
 # 配置API所需的配置文件
 
[root@k8s-master03 kubernetes]# mkdir -p $HOME/.kube
[root@k8s-master03 kubernetes]# sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
[root@k8s-master03 kubernetes]# sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

最后查看集群状态
```shell
[root@k8s-master ~]# kubectl get nodes
```