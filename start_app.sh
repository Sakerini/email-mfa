# Build the project using Gradle
echo "Building the project with Gradle..."
./gradlew build

# Deploy containers with Docker Compose
echo "Starting Docker containers..."
docker-compose up