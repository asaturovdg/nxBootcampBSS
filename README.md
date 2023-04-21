# nxBootcampBSS

## Запуск приложения с помощью docker compose

```cmd
.\mvnw clean package -DskipTests
cp .\target\nxBootcampBSS-1.jar .\src\main\docker\
docker compose -f .\src\main\docker\docker-compose.yml up -d --build
```
