FROM tomcat:8
# hosts 问题暂时无解，望k8s能解决.
#ADD hosts.txt /usr/local/hosts.txt
#RUN touch /etc/profile.d/hosts.sh 
#RUN echo "cat /usr/local/hosts.txt >> /etc/hosts" > /etc/profile.d/hosts.sh & chmod u+x /etc/profile.d/hosts.sh
#RUN cat /usr/local/hosts.txt >> /etc/hosts
#RUN rm -f /usr/local/hosts.txt
#解决时区问题
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
