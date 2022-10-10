FROM openjdk:8

MAINTAINER xiaoke_1256

VOLUME /tmp

#VOLUME /logs
ADD discovery-eureka*.war /discovery-eureka.war
#ADD context.xml /usr/local/tomcat/conf/context.xml
EXPOSE 8761 8762
ENV JAVA_OPTS='-server -Xms64m -Xmx512m $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /discovery-eureka.war > log.out
