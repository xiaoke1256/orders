docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -v esdata:/usr/share/elasticsearch/data -v /home/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -d --restart=always --network=host docker.elastic.co/elasticsearch/elasticsearch:7.2.0

docker run -p 9100:9100 -d --restart=always mobz/elasticsearch-head:5-alpine


docker run -d -it --restart=always  --privileged=true  --name=logstash -p 5047:5047 -p 9600:9600 -v /home/elk/logstash/pipeline/:/usr/share/logstash/pipeline/ -v /home/elk/logstash/config/logstash.yml:/usr/share/logstash/config/logstash.yml  docker.elastic.co/logstash/logstash:7.7.1
