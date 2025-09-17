### Maven build
```bash
./mvnw clean package -DskipTests
```

### Docker + DockerHub
```bash
Docker

for Windows

docker build -t products-service:latest . -f .\products-service\Dockerfile
...
docker images

for Linux
docker build -t products-service:latest . -f ./products-service/Dockerfile

docker scout cves <IMAGE_ID>
docker rmi <IMAGE_ID>
docker image prune -a
docker system prune -a --volumes

Docker compose

Orden
1. docker compose up -d discovery-server
2. docker compose up -d keycloak
3. Follow KEYCLOAK_CONFIGURATION.md
4. docker compose up -d api-gateway
5. docker compose up -d inventory-service notification-service orders-service products-service kafka
6. docker logs -f discovery-server

docker compose up -f compose.yaml -d
docker compose down -f compose.yaml -v

DockerHub

docker login -u {your-dockerhub}
Token: dckr_pat_x ...
docker logout

docker tag products-service <your-dockerhub>/products-service
docker push <your-dockerhub>/products-service
```

### Kubernetes
```bash
kubectl apply -f discovery-server.yaml ...
kubectl apply -f .
```

### Jaeger
```bash
minikube service jaeger --url
# minikube service jaeger-query
```