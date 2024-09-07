FROM openjdk:22-jdk-oracle
LABEL "org.opencontainers.image.authors"="eazybytes.com"
COPY target/gatewayserver-0.0.1-SNAPSHOT.jar gatewayserver-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","gatewayserver-0.0.1-SNAPSHOT.jar"]
