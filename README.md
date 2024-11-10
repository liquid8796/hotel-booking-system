
# Hotel Booking System

This project is a **Hotel Booking System** built with Spring Boot and MySQL. The application uses **Docker Compose** for easy deployment of services, including the backend application and the MySQL database.

## Project Structure

```
project-root/
├── deployment/
│	└── db-init
│   └── docker-compose.yml
├── Dockerfile
├── src/
│   └── ... (your Spring Boot source code)
├── pom.xml
└── README.md
```

## Prerequisites

Make sure you have the following installed on your system:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/liquid8796/hotel-booking-system.git
cd hotel-booking-system
```

### Step 2: Build and Run the Docker Containers

Navigate to the `deployment/` directory and use Docker Compose to build and run the containers:

```bash
cd deployment
docker-compose up -d
```

This command will:
- Build the Spring Boot application using the provided `Dockerfile`.
- Start the MySQL database and wait until it is fully ready before starting the Spring Boot application.

### Step 3: Access the Application

- The Spring Boot application will be available at: [http://localhost:8080](http://localhost:8080)
- MySQL will be exposed on port `3306` and could be accessible from within Docker containers or your local machine.

## Docker Compose Configuration

Here's a quick overview of the `docker-compose.yml`:

```yaml
version: "3.8"

services:
  mysql:
    image: mysql:8
    restart: unless-stopped
    ports:
      - "3306:3306"
    environment:
      MYSQL_DATABASE: hotel_booking_system
      MYSQL_USER: user
      MYSQL_PASSWORD: dummypassword
      MYSQL_ROOT_PASSWORD: dummypassword
    volumes:
      - ./db-init/mysql/schema.sql:/docker-entrypoint-initdb.d/1.sql
      - ./db-init/mysql/data.sql:/docker-entrypoint-initdb.d/2.sql
    networks:
      - app-network
    healthcheck:
      test: [ "CMD", "mysqladmin", "ping", "-h", "localhost", "--silent" ]
      interval: 10s
      retries: 5
      start_period: 10s

  reservations:
    build:
      context: ..
      dockerfile: Dockerfile
      target: production
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/hotel_booking_system
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: dummypassword
    ports:
      - '8080:8080'
    volumes:
      - ../:/usr/src/app
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
```

### Step 4: Stopping the Containers

To stop the Docker containers, press `Ctrl+C` in the terminal where `docker-compose up` is running or use:

```bash
docker-compose down
```

### Step 5: Clean Up

If you want to remove all Docker containers, networks, and volumes created by Docker Compose, use:

```bash
docker-compose down -v
```

## Troubleshooting

1. **MySQL Connection Error**: If the Spring Boot application fails to connect to MySQL, ensure that:
   - The MySQL container is running and healthy.
   - The correct environment variables (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`) are set.

2. **Build Issues**:
   - If there are errors during the build, try clearing Docker’s cache:
     ```bash
     docker-compose build --no-cache
     ```

## Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MySQL Documentation](https://dev.mysql.com/doc/)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
