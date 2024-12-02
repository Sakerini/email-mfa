# Email MFA Application

A microservice that issues Multi-Factor Authentication (MFA) emails and allows users to verify their
MFA code at an endpoint provided by the microservice.

## Requirements

Application requires:

- Gradle
- Java 21
- Docker && Docker Compose

## Installation

Clone the repository and build the project

Use script:

```sh
chmod +x deploy.sh

./start_app.sh
```

Or

```sh
./gradlew build
```

If you don't have gradle installed on your machine, you can use pre-build jar.
You can download the JAR file from the following link:
[Download email JAR](https://github-registry-files.githubusercontent.com/896576988/480bd680-b0d6-11ef-96b9-29c836503b9c?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20241202%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20241202T150028Z&X-Amz-Expires=300&X-Amz-Signature=d07d6bf004bd634b891c95b0ac9b17865acfa7affbeb0dc22901f396ebc12283&X-Amz-SignedHeaders=host&response-content-disposition=filename%3Demail-0.0.1-20241202.145256-1-plain.jar&response-content-type=application%2Foctet-stream)

After build use docker-compose.yml file to deploy containers to Docker

```sh
docker-compose up
```

This command will create 3 containers:

- redis-server
- mail-server
- mfa-application

## Usage

You can access swagger UI, and read the application interface.

```sh
http://localhost:8080/swagger-ui/index.html
```

Send email with code

```sh
curl --location 'http://localhost:8080/api/v1/mfa/email-code' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "user@email.com"
}'
```

This curl will send an email to the mail-server.
Mail server UI can be accessed at address http://localhost:8081

to verify the code

```sh
curl --location 'http://localhost:8080/api/v1/mfa/email-code/verify/user@mail.com/689781'
```

For more detailed information use swagger documentation.


