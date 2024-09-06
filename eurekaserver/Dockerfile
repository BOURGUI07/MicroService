FROM openjdk:22-jdk-oracle
LABEL "org.opencontainers.image.authors"="eazybytes.com"
COPY target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","eurekaserver-0.0.1-SNAPSHOT.jar"]
