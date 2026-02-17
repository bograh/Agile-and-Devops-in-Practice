# Property Management API - Docker Instructions

## Build the Docker image
```bash
docker build -t property-management-api:latest .
```

## Run the container
```bash
docker run -p 8080:8080 property-management-api:latest
```

## Access the application
- API: http://localhost:8080/api
- H2 Console: http://localhost:8080/h2-console
- Health Check: http://localhost:8080/actuator/health
