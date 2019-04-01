FROM tomcat:8
VOLUME /logs
ADD product.war /usr/local/tomcat/webapps/orders.war
RUN bash -c 'touch /usr/local/tomcat/webapps/orders.war'
ENV spring.profiles.active=prod
