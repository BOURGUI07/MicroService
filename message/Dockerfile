FROM openjdk:22-jdk-oracle
LABEL "org.opencontainers.image.authors"="eazybytes.com"
COPY target/message-0.0.1-SNAPSHOT.jar message-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","message-0.0.1-SNAPSHOT.jar"]
