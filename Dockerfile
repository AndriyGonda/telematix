FROM gradle:7.4.2-jdk17
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN /usr/src/app/gradlew build
