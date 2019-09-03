FROM repo.orders.com:5000/orders/base_web:latest
#VOLUME /logs
ADD 3rdpay*.war /usr/local/tomcat/webapps/3rdpay.war
ADD context.xml /usr/local/tomcat/conf/context.xml
ENV JAVA_OPTS='-server -Xms64m -Xmx512m -Dspring.profiles.active=prod $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
