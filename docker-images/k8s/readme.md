kubectl describe pod nginx-pod 发现以下日志：

failed to load flannel 'subnet.env' file: open /run/flannel/subnet.env: no s

创建 /run/flannel/subnet.env 文件。
内容为：
FLANNEL_NETWORK=10.244.0.0/16
FLANNEL_SUBNET=10.244.1.1/24
FLANNEL_MTU=1450
FLANNEL_IPMASQ=true