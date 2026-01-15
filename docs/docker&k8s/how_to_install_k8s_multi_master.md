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