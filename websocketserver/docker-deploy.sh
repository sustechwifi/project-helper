docker rm -f ooad-websocketserver-1 && docker rmi -f project-helper-websocketserver
docker build -t project-helper-websocketserver .
docker run -d -p 8000:8000 --name ooad-websocketserver-1 project-helper-websocketserver