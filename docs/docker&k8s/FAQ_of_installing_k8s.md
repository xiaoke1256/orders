
具体安装过程请参考： [这里](https://blog.csdn.net/weixin_39555954/article/details/124783111)

ip变掉后重置请看 [这里](https://blog.csdn.net/shuxinghe/article/details/115370189)

0.初始化命令：
```
sudo kubeadm init --apiserver-advertise-address=192.168.1.7 --image-repository=registry.aliyuncs.com/google_containers --kubernetes-version=v1.16.4 --pod-network-cidr=10.244.0.0/16 --service-cidr=10.96.0.0/12 --ignore-preflight-errors=Swap
```
（注意:apiserver-advertise-address 要根据需求改的）

1. sudo kubeadm init 命令时发现 /proc/sys/net/bridge/bridge-nf-call-iptables contents are not set to 1

具体提示信息如下：
```
[ERROR FileContent--proc-sys-net-bridge-bridge-nf-call-iptables]: /proc/sys/net/bridge/bridge-nf-call-iptables contents are not set to 1 
```

先赋予`/etc/sysctl.conf`文件写权限，然后执行以下命令：
```
$ echo "net.bridge.bridge-nf-call-iptables=1" >> /etc/sysctl.conf
$ echo "net.bridge.bridge-nf-call-ip6tables=1" >> /etc/sysctl.conf
 
$ sysctl -p /etc/sysctl.conf
```

2. Master（或 Worker） 节点NotReady 的问题

刚刚执行完`kubeadm init`命令时发现此问题。用`journalctl -u kubelet | tail -n 300`命令查日志，发现如下提示：
```
Unable to update cni config: No networks found in /etc/cni/net.d
```
解决方法：新增文件 /etc/cni/net.d/10-flannel.conflist，内容如下：

```
{
  "cniVersion": "0.3.1",
  "name": "cbr0",
  "plugins": [
    {
      "type": "flannel",
      "delegate": {
        "hairpinMode": true,
        "isDefaultGateway": true
      }
    },
    {
      "type": "portmap",
      "capabilities": {
        "portMappings": true
      }
    }
  ]
}
```

`systemctl restart kubelet`命令重启kubelet。

如果在work节点，用`journalctl -u kubelet | tail -n 300`命令，发现了如下提示：
```
failed to find plugin “flannel” in path [/opt/cni/bin]
```
则需要安装 cni 插件。具体查看[这里](https://blog.csdn.net/qq_29385297/article/details/127682552)

[下载cni插件](https://github.com/containernetworking/plugins/releases/tag/v0.8.6)并解压缩后复制 flannel 目录到 /opt/cni/bin/。

如果用`journalctl -u kubelet | tail -n 300`命令，发现了如下提示：
```
unknown container “/system.slice/docker.service“
```
请看[这里](https://blog.csdn.net/qq_26545503/article/details/124635672)。

如果用`journalctl -u kubelet | tail -n 300`命令，发现了如下提示：
```
failed to find plugin \"portmap\" in path [/opt/cni/bin]
```
那就是因为相关组件未安装。执行以下语句安装组件：
```
yum clean all
yum install kubernetes-cni -y
```

3. 如果忘记工作节点加入集群的命令，可用如下命令获取
```
kubeadm token create --print-join-command
```

4.  swap 永久关闭

查看是否关闭：
```
free -h
```
然后输入 `vi /etc/fstab` 将swap那行注释掉。

5. /proc/sys/net/ipv4/ip_forward 无权写入

修改 /etc/sysctl.conf 文件，加入以下一行：
```
net.ipv4.ip_forward=1
```
然后执行`/sbin/sysctl -p `刷新。

6. LoadBalancer service 不生效

部署 LoadBalancer 有时会出现 External-IP 一直处于 pending , 指定 External-IP 后任然没法从外网访问. 原因是自建的 Kubernetes 集群（minikube、kubeadm等）,没有集成 LoadBalancer.

[此处](https://makeoptim.com/service-mesh/kubeadm-kubernetes-istio-setup#metallb) 用 metallb 解决此问题.

7. Pod 一直处于状态ContainerCreating状态

`kubectl describe pod <pod-name>` 命令发现以下日志：`failed to load flannel 'subnet.env' file: open /run/flannel/subnet.env: no s....`

需要在对应的work节点创建`/run/flannel/subnet.env`文件，内容如下：
```
FLANNEL_NETWORK=10.244.0.0/16
FLANNEL_SUBNET=10.244.1.1/24
FLANNEL_MTU=1450
FLANNEL_IPMASQ=true
```