docker rm -f ooad-server-1 && docker rmi -f project-helper-mainservice
docker build -t project-helper-mainservice .
docker run -d -p 8070:8070 -e SERVER_IP=139.199.172.27 --name ooad-server-1 project-helper-mainservice
docker logs -f ooad-server-1