docker rm -f ooad-gateway-1 && docker rmi -f project-helper-gateway
docker build -t project-helper-gateway .
docker run -d -p 8080:8080 --name ooad-gateway-1 project-helper-gateway