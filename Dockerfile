FROM eclipse-temurin:21.0.7_6-jre

RUN mkdir /opt/app
COPY application.conf /opt/app/
COPY build/libs/main.jar /opt/app/
CMD ["java", "-jar", "/opt/app/main.jar", "/opt/app/application.conf"]
