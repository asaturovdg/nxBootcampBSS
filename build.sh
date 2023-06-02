./mvnw clean package
docker build -t asaturovdg/bss .
docker push asaturovdg/bss
docker compose up --build