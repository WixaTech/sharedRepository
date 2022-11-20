FROM amazoncorretto:17-alpine-jdk
MAINTAINER pl.wixatech
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY build/libs/*.jar backend-1.0.0.jar
ENTRYPOINT ["java","-jar","backend-1.0.0.jar"]