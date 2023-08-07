docker run --restart=always --name elasticsearch \
-p 9200:9200  -p 9300:9300 \
-e "discovery.type=single-node" \
-e ES_JAVA_OPTS="-Xms256m -Xmx256m" \
-v /opt/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
-v /opt/elasticsearch/data:/usr/share/elasticsearch/data \
-v /opt/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d --restart=always --network=host elasticsearch:7.11.1

docker run -p 9100:9100 -d --restart=always mobz/elasticsearch-head:5-alpine


docker run -d -it --restart=always  --privileged=true  --name=logstash -p 5047:5047 -p 9600:9600 -v /home/elk/logstash/pipeline/:/usr/share/logstash/pipeline/ -v /home/elk/logstash/config/logstash.yml:/usr/share/logstash/config/logstash.yml  docker.elastic.co/logstash/logstash:7.7.1

docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.1.101:9200 -p 5601:5601 --network=host --restart=always -d kibana:7.2.0