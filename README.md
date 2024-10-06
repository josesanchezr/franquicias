## Descripción
API para manejar una lista de franquicias. Una franquicia se 
compone por un nombre y un listado de sucursales y, a su vez, 
una sucursal está compuesta por un nombre y un listado de 
productos ofertados en la sucursal. Un producto se componente de un nombre y una cantidad de stock.

Aplicación desarrollada en Java 17 con Spring Boot 3.3.4, que usa programación reactiva, base de datos MySQL 8.0

## Prerequisitos
1. JDK 17
2. Configurar variable de entorno JAVA_HOME con la ruta de instalación de JDK 17
3. MySQL 3.8.*
4. Docker https://docs.docker.com/get-started/get-docker/
5. Terraform https://developer.hashicorp.com/terraform/install
6. AWS CLI https://docs.aws.amazon.com/es_es/cli/latest/userguide/getting-started-install.html

## Para ejecutar aplicación
1. Compilar aplicación con el comando: `./gradlew bootJar`
2. Crear imagen docker de la aplicación con el comando: `docker build -t franquicias-app .`
3. Ejecutar imagen docker de la aplicación en local con el comando: 
   `docker run -e SERVER_PORT=8080 -e DB_CONNECTION=r2dbc:mysql://host.docker.internal:3306/franquiciasdb -e DB_USERNAME=usuario_db -e DB_PASSWORD=password_db -p 8080:8080 franquicias-app:latest`

## Para crear la instancia de AWS RDS con MySQL 8.0
Dentro de la carpeta terraform-project se encuentra configurado con terraform la opción de crear la infraestructura
de la instancia AWS RDS con MySQL 8.0.
1. Ir a la carpeta terraform-project
2. Ejecutar el comando: `terraform init`
3. Ejecutar el comando: `terraform plan`
4. Ejecutar el comando: `terraform apply`

## Para ejecutar los unit tests
Para ejecutar los tests y generar reporte de la cobertura:
1. Ejecutar el comando `./gradlew test jacocoTestReport`