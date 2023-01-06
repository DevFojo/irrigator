FROM maven:3.8.6-amazoncorretto-19 as BUILD

COPY . /usr/src/app

WORKDIR /usr/src/app
# Package the project
RUN mvn -s /usr/share/maven/ref/settings-docker.xml package -DskipTests

FROM amazoncorretto:19
# Optional, expose the container port
EXPOSE 8080
# From fist step, copy build jar file to target directory
COPY --from=BUILD /usr/src/app/target/*.jar /opt/target/app.jar
# Set the working directory to /opt/target
WORKDIR /opt/target

# Run the application
ENTRYPOINT ["java","-jar", "app.jar"]