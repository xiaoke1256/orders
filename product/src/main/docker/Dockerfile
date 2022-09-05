FROM tomcat:9

MAINTAINER xiaoke_1256

VOLUME /tmp
#RUN sed -i /port=8080/port=8081/g /usr/local/tomcat/conifg/service.xml
ADD product-1.2.1.war /usr/local/tomcat/webapps/product.war
#RUN bash -c 'touch /usr/local/tomcat/webapps/product.war'
ADD context.xml /usr/local/tomcat/conf/context.xml
EXPOSE 8080
ENV JAVA_OPTS='-server -Xms64m -Xmx512m -Dspring.profiles.active=prod $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
