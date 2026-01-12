# 原先镜像名为“openjdk:8” 现在被墙了
FROM openanolis/openjdk:8-8.6

MAINTAINER xiaoke_1256

# ubuntu 系统有效，现在是Anolis OS系统
# # 修改源
# RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list
#
# # 增加常用软件
# RUN apt-get update; \
#     apt-get install -y --no-install-recommends \
#         net-tools \
#     ; \
#     apt-get install iputils-ping; \
#     rm -rf /var/lib/apt/lists/*

# 设置时区
RUN echo "Asia/Shanghai" > /etc/timezone \
&& ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

VOLUME /tmp

#VOLUME /logs
ADD product*.jar /product.jar
#ADD context.xml /usr/local/tomcat/conf/context.xml
EXPOSE 8082
ENV JAVA_OPTS='-server -Xms64m -Xmx512m $EXT_JAVA_OPTS'
ENV EXT_JAVA_OPTS=''
ENTRYPOINT java -jar /product.jar
