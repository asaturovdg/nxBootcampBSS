FROM openjdk:17-oracle
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} bss.jar

ADD ./files ./files

ENTRYPOINT ["java", "-jar", "bss.jar"]