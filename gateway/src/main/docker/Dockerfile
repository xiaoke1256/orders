FROM openjdk:8

MAINTAINER xiaoke_1256

VOLUME /tmp

#VOLUME /logs
ADD orders-gateway*.war /orders-gateway.war
#ADD context.xml /usr/local/tomcat/conf/context.xml
EXPOSE 8761 8762
ENV JAVA_OPTS='-server -Xms64m -Xmx512m $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
ENTRYPOINT java -jar /orders-gateway.war
