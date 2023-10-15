export BUILD_DIR="/home/ponchick/datapool/build"
nohup java -Dspring.config.additional-location=/datapool-app.yaml -jar datapool-app-1.0-SNAPSHOT.jar&
nohup java -Dspring.config.additional-location=/cache-manager-app.yaml -jar cache-manager-app-1.0-SNAPSHOT.jar&
nohup java -Dspring.config.additional-location=/api-controller-app.yaml -jar api-controller-app.jar&