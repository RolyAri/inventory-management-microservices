# Maven build

./mvnw clean package -DskipTests

### Docker (Cmd)

      docker login -u {docker-registry-username}

      Token: dckr_pat_x ...

      docker logout

      docker images

      docker build -t products-service:latest . -f ./products-service/Dockerfile

      docker build -t products-service:latest . -f .\products-service\Dockerfile

      docker scout cves <IMAGE_ID>

      docker tag products-service <dockerhub_username>/products-service

      docker push <dockerhub_username>/products-service

      docker rmi <IMAGE_ID>

      docker image prune -a

      docker system prune -a --volumes

`Docker compose`

      docker compose up compose.yaml -d

      docker compose down compose.yaml -v

docker compose -f compose.yaml up -d config-server eureka-server zipkin msvc-products msvc-items

