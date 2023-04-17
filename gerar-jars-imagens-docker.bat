cd service-anotacoes
mvn clean package -DskipTests
docker build -t efigueredo/ms-anotacoes:1.2 .

cd ..
cd service-gateway
mvn clean package -DskipTests
docker build -t efigueredo/gateway:1.0 .

cd ..
cd service-identidade
mvn clean package -DskipTests
docker build -t efigueredo/ms-identidade:1.0 .

cd ..
cd service-registry
mvn clean package -DskipTests
docker build -t efigueredo/service-registry:1.0 .

echo FINALIZADO