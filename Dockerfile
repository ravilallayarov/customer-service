FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY build/libs/customer-service-0.0.1-SNAPSHOT.jar /app/customer-service.jar

# 8080 (nTLS), 8443 (TLS), 5005 (debug)
EXPOSE 8080 8443 5005

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "/app/customer-service.jar"]