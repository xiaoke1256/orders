FROM openjdk:8

MAINTAINER xiaoke_1256

# 修改源
RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list

# 增加常用软件
RUN apt-get update; \
    apt-get install -y --no-install-recommends \
        net-tools \
    ; \
    rm -rf /var/lib/apt/lists/*

# 设置时区
RUN echo "Asia/Shanghai" > /etc/timezone \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

VOLUME /tmp

#VOLUME /logs
ADD discovery-eureka*.war /discovery-eureka.war
#ADD context.xml /usr/local/tomcat/conf/context.xml
EXPOSE 8761 8762
ENV JAVA_OPTS='-server -Xms64m -Xmx512m $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /discovery-eureka.war > log.out
