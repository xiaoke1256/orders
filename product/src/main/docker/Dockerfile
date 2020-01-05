FROM repo.orders.com:5000/orders/base_web:latest

MAINTAINER xiaoke_1256

#VOLUME /logs
#RUN sed -i /port=8080/port=8081/g /usr/local/tomcat/conifg/service.xml
ADD product*.war /usr/local/tomcat/webapps/product.war
ADD context.xml /usr/local/tomcat/conf/context.xml
ENV JAVA_OPTS='-server -Xms64m -Xmx512m -Dspring.profiles.active=prod $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
