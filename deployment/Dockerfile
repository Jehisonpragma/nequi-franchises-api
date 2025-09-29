FROM openjdk:17-alpine@sha256:4b6abae565492dbe9e7a894137c966a7485154238902f2f25e9dbd9784383d81
EXPOSE 8081

COPY applications/app-service/build/libs/nequi-franchises-api.jar app.jar
CMD ["/bin/sh"]
ENTRYPOINT ["java", "-jar","-Duser.timezone=America/Bogota", "/app.jar"]
