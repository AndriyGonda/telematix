FROM gradle:7.4.2-jdk17
COPY . /usr/src/app
WORKDIR /usr/src/app
ENV CORS_ORIGIN http://localhost:8080
ENV IMAGES_FOLDER images
RUN /usr/src/app/gradlew build
