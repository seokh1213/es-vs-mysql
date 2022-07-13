docker-compose -f docker/docker-compose.yml down
docker-compose -f docker/docker-compose.yml up -d
docker cp es01:/usr/share/elasticsearch/config/certs/es01/es01.crt ./src/main/resources/http_ca.crt