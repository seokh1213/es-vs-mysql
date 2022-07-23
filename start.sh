docker-compose -f docker/es-docker-compose.yml down
docker volume rm -f docker_certs docker_esdata01 docker_esdata02 docker_esdata03 docker_kibanadata
docker-compose -f docker/es-docker-compose.yml up -d
docker-compose -f docker/mysql-docker-compose.yml down
docker volume rm -f docker_mysql8
docker-compose -f docker/mysql-docker-compose.yml up -d
docker cp es01:/usr/share/elasticsearch/config/certs/es01/es01.crt ./http_ca.crt
keytool -delete -noprompt -cacerts -alias escert -storepass changeit
keytool -importcert -noprompt -alias escert -cacerts -file http_ca.crt -storepass changeit # default password changeit