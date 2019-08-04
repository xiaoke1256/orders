FROM tomcat:8
ADD hosts.txt /usr/local/hosts.txt
RUN cat /usr/local/hosts.txt >> /etc/hosts
RUN rm -f /usr/local/hosts.txt