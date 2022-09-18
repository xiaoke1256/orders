FROM tomcat:9

MAINTAINER xiaoke_1256

VOLUME /tmp

#VOLUME /logs
ADD discovery-eureka*.war /usr/local/tomcat/webapps/discovery.war
#ADD context.xml /usr/local/tomcat/conf/context.xml
EXPOSE 8080
ENV JAVA_OPTS='-server -Xms64m -Xmx512m -Dspring.profiles.active=prod $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
