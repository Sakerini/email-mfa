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
You can download the JAR file from the here:
[Download email JAR](https://github.com/Sakerini/email-mfa/packages/2332145)
At the assets block

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


