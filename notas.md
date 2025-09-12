### Maven build

      ./mvnw clean package -DskipTests

### Docker (Cmd)

      docker login -u {docker-registry-username}

      Token: dckr_pat_x ...

      docker logout

      docker images

      for Linux

      docker build -t products-service:latest . -f ./products-service/Dockerfile

      for Windows

      docker build -t products-service:latest . -f .\products-service\Dockerfile

      docker scout cves <IMAGE_ID>

      docker tag products-service <dockerhub_username>/products-service

      docker push <dockerhub_username>/products-service

      docker rmi <IMAGE_ID>

      docker image prune -a

      docker system prune -a --volumes

      Docker compose

      docker compose up -f compose.yaml -d

      docker compose down -f compose.yaml -v

### Orden

      docker compose up -d discovery-server

      docker compose up -d keycloak

      Follow KEYCLOAK_CONFIGURATION.md

      docker compose up -d api-gateway

      docker compose up -d inventory-service notification-service orders-service products-service kafka

      docker logs -f discovery-server