Подключение к базе данных
bash
````
docker run --name auth-service -e POSTGRES_USER=alexa -e POSTGRES_PASSWORD=auth-service -e POSTGRES_DB=auth-service -p 6000:5432 -d postgres:17
````