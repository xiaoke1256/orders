如何安装Kubernetes集群
=======

#### 一、环境准备

3台服务器，安装Linux7.9系统，2核CPU/2G内存/20G硬盘。

1. 安装网络工具：

```shell
sudo yum install net-tools
```

2. 配置每个节点的网络，然后能互相ping通（每个节点上都要执行）

```shell
vi /etc/hosts
```
&emsp;&emsp;添加以下内容,实际情况请填写自己的ip
```
192.168.249.131  master
192.168.249.132  node1
192.168.249.133  node2
```
&emsp;&emsp;看看各个节点是否可以ping通

2. 安装时间插件，保证每个节点时间一致（每个节点上都要执行）
```shell
yum install -y chrony
systemctl start chronyd
systemctl enable chronyd
```
4. 开启端口，或直接关闭防火墙（每个节点上都要执行）

&emsp;&emsp;以下端口要开放：

&emsp;&emsp;<b>控制面:</b>

|协议|方向|端口范围|目的|使用者|
|--|--|--|--|--|
|TCP|入站|6443|Kubernetes API server|所有|
|TCP|入站|2379-2380|etcd server client API|所有|
|TCP|入站|10250|Kubelet API|自身，控制面|
|TCP|入站|10259|kube-scheduer|自身|
|TCP|入站|10257|kube-controller-manager|自身|

&emsp;&emsp;<b>工作节点:</b>

|协议|方向| 端口范围        | 目的                | 使用者    |
|--|--|-------------|-------------------|--------|
|TCP|入站| 10250       | Kubelet API       | 自身，控制面 |
|TCP|入站| 30000-32767 | NodePort Services | 所有     |

&emsp;&emsp;关闭防火墙命令：
```shell
systemctl stop firewalld && systemctl disable firewalld
```

5. 禁用SELINUX（每个节点上都要执行）
```shell
sed -i 's/^SELINUX=.*/SELINUX=disabled/' /etc/selinux/config  && setenforce 0
```
&emsp;&emsp;或者手动修改SELINUX=disabled
```shell
vi /etc/selinux/config
```

6. 禁用swap分区（每个节点上都要执行）
```shell
# 临时
swapoff -a
#永久
sed -i '/ swap / s/^\(.*\)$/#\1/g' /etc/fstab
```
&emsp;&emsp;或者执行以下命令注释掉`/dev/mapper/centos-swap swap`  行。

```shell
vi /etc/fstab
```

7. 允许 iptables 检查桥接流量和地址转发（每个节点上都要执行）
```shell
cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF
 
#重新加载
sudo sysctl --system
#加载br_netfilter模块
modprobe br_netfilter
#查看网桥过滤模块是否加载成功
lsmod  | grep br_netfilter
```

8. 配置ipvs功能（每个节点上都要执行）
```shell
#安装
yum install ipset ipvsadm -y
#配置
cat <<EOF > /etc/sysconfig/modules/ipvs.modules
#!/bin/bash
modprobe -- ip_vs
modprobe -- ip_vs_rr
modprobe -- ip_vs_wrr
modprobe -- ip_vs_sh
modprobe -- nf_conntrack_ipv4
EOF
#授权
chmod +x /etc/sysconfig/modules/ipvs.modules
#执行
sh +x /etc/sysconfig/modules/ipvs.modules
#查看
lsmod | grep -e ip_vs -e nf_conntrack_ipv4
```
9. 重启服务器让SELINUX生效（每个节点上都要执行）
```shell
reboot -h now
```
#### 二、docker安装
1. docker安装（每个节点上都要执行）
```shell
#更新yum源
yum -y update
 
#安装软件包
yum install -y yum-utils device-mapper-persistent-data lvm2
 
#设置镜像仓库
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
 
#查看版本列表
yum list docker-ce-cli --showduplicates|sort -r
 
#指定安装版本
yum install --setopt=obsoletes=0 docker-ce-20.10.9-3.el7 -y 
 
#启动docker
systemctl start docker
 
#设置开机启动
systemctl enable docker
 
#检测安装是否成功
docker version
```

2. 配置加速镜像（每个节点上都要执行）
```shell
#创建文件
sudo mkdir -p /etc/docker
#配置
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "exec-opts": ["native.cgroupdriver=systemd"],
  "registry-mirrors": ["https://3kcpregv.mirror.aliyuncs.com"]
}
EOF
#重启docker
 systemctl restart docker
```

#### 三、Kubernetes集群安装

1. 配置阿里云yum源（每个节点上都要执行）
```shell
cat > /etc/yum.repos.d/kubernetes.repo << EOF
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg         
       https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
```

2. 安装kubeadm、kubelet和kubectl（每个节点上都要执行）
```shell
yum install --setopt=obsoletes=0 kubelet-1.23.6 kubeadm-1.23.6 kubectl-1.23.6 -y
```

3. 修改kubelet驱动和docker驱动一样（每个节点上都要执行）
```shell

#编辑文件
vi /etc/sysconfig/kubelet
#添加下面的配置
KUBELET_CGROUP_ARGS="--cgroup-driver=systemd"
KUBE_PROXY_MODE="ipvs"
```

4. 设置开机启动（每个节点上都要执行）
```shell
systemctl enable kubelet
```

5. kubeadm config images list --kubernetes-version 1.23.6
```shell
kubeadm config images list --kubernetes-version 1.23.6
```
得到如下信息：
```
k8s.gcr.io/kube-apiserver:v1.23.6
k8s.gcr.io/kube-controller-manager:v1.23.6
k8s.gcr.io/kube-scheduler:v1.23.6
k8s.gcr.io/kube-proxy:v1.23.6
k8s.gcr.io/pause:3.7
k8s.gcr.io/etcd:3.5.3-0
k8s.gcr.io/coredns/coredns:v1.8.6
```

6. 下载镜像，由于网络原因，从阿里云仓库地址下载，然后修改名称（每个节点上都要执行）
```shell
#创建脚本文件
vi pull.sh
```
脚本内容 
```
images=(
kube-apiserver:v1.23.6
kube-controller-manager:v1.23.6
kube-scheduler:v1.23.6
kube-proxy:v1.23.6
pause:3.7
etcd:3.5.3-0 
coredns:1.8.6
)
coreDnsName=coredns:1.8.6
for imageName in ${images[@]} ; do
docker pull registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName
if [ $imageName = $coreDnsName ]
then echo 'dns-------'
docker tag registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName k8s.gcr.io/coredns/coredns:v1.8.6
docker rmi registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName
else echo 'other-------'
docker tag registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName k8s.gcr.io/$imageName
docker rmi registry.cn-hangzhou.aliyuncs.com/google_containers/$imageName
fi
done
```

执行:

```shell
chmod +x pull.sh
#执行
./pull.sh
```

7. 初始化（仅在master节点执行即可）

```shell
kubeadm init \
--kubernetes-version=v1.23.6 \
--pod-network-cidr=10.244.0.0/16 \
--image-repository registry.aliyuncs.com/google_containers \
--service-cidr=10.96.0.0/12 \
--apiserver-advertise-address=192.168.249.131 #ip对应master的ip
```
执行结果包含如下信息：
```
Your Kubernetes control-plane has initialized successfully!

To start using your cluster, you need to run the following as a regular user:

  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config

Alternatively, if you are the root user, you can run:

  export KUBECONFIG=/etc/kubernetes/admin.conf

You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 192.168.249.131:6443 --token jqy5ed.rpz3kr1zc9agloin \
        --discovery-token-ca-cert-hash sha256:1910df97ff2343d96313033bf8b88d7a87ae431b4835a7356813febd64d6d4cd
```

8. 创建文件（仅在master节点执行即可）
```shell
  mkdir -p $HOME/.kube
  sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
  sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

9. 在其他两台从节点上执行如下命令
```shell
kubeadm join 192.168.249.131:6443 --token jqy5ed.rpz3kr1zc9agloin \
        --discovery-token-ca-cert-hash sha256:1910df97ff2343d96313033bf8b88d7a87ae431b4835a7356813febd64d6d4cd
```

&emsp;&emsp;注意：token过期时间为24小时，如果过期执行以下命令创建token
```shell
#创建token
kubeadm token create
#查看token
kubeadm token list
```
&emsp;&emsp;获取证书hash值
```shell
openssl x509 -pubkey -in /etc/kubernetes/pki/ca.crt | openssl rsa -pubin -outform der 2>/dev/null | openssl dgst -sha256 -hex | sed 's/^.* //'
```

10. 在master节点上查看节点信息
```shell
kubectl get nodes
```

11. 安装网络插件

&emsp;&emsp;下载 kube-flannel.yml 插件

&emsp;&emsp; 下载地址：[https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml](https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml)

&emsp;&emsp;或者：[kube-flannel.yml](docker-images/k8s/kube-flannel.yml)

将该文件上传到master服务器 执行如下命令：
```shell
kubectl apply -f kube-flannel.yml
```
注意上传完文件之后 使用如下命令检查一下文件:
```shell
vi -b kube-flannel.yml
```

如果显示^M等样式的文件 执行以下命令

```shell
sed -i 's/\r//g' kube-flannel.yml 
```

查看插件是否安装成功:
```shell
kubectl get pods -n kube-system |grep flannel
```

12. 查看节点状态（等待1分钟 左右）
```shell
kubectl get nodes
```