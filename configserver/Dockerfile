FROM openjdk:22-jdk-oracle
LABEL "org.opencontainers.image.authors"="eazybytes.com"
COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","configserver-0.0.1-SNAPSHOT.jar"]
