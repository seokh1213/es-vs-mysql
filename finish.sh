docker-compose -f docker/es-docker-compose.yml down
docker volume rm -f docker_certs docker_esdata01 docker_esdata02 docker_esdata03 docker_kibanadata
docker-compose -f docker/mysql-docker-compose.yml down
docker volume rm -f docker_mysql8
keytool -delete -noprompt -cacerts -alias escert -storepass changeit