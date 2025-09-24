#
# Build stage
#
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# First copy just the pom to leverage Docker layer cache for dependencies
COPY pom.xml .
RUN mvn -B -q -DskipTests dependency:go-offline

# Now copy the source and build
COPY src ./src
RUN mvn -B -DskipTests clean package

#
# Runtime stage
#
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT:-8080} -jar app.jar"]