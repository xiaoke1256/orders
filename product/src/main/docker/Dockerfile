FROM tomcat:8
#VOLUME /logs
ADD product.war /usr/local/tomcat/webapps/product.war
ADD context.xml /usr/local/tomcat/conf/context.xml
ENV -Dspring.profiles.active=prod
