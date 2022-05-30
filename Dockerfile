FROM gradle:7.4.2-jdk17
COPY . /usr/src/app
WORKDIR /usr/src/app
#ENV TIMEZONE "Europe/Kiev"
#ENV CORS_ORIGIN "http://localhost:8081"
RUN /usr/src/app/gradlew build
