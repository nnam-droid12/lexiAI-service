FROM openjdk:17-slim

# Install required system libraries for Speech SDK
RUN apt-get update && apt-get install -y \
    libstdc++6 \
    libasound2 \
    libssl-dev \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy the JAR file directly
COPY target/lexiAI-0.0.1-SNAPSHOT.jar app.jar

# Set the temporary directory environment variable
ENV TMPDIR=/tmp

# Expose the port your app runs on (adjust if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]