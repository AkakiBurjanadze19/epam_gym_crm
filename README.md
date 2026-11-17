# Gym CRM - Docker Deployment Guide

### 1. Build and Start All Services
```bash
docker compose up -d --build
```

### 2. Verify Services Are Running
```bash
docker compose ps
```

## Service Endpoints

| Service | URL |
|---------|-----|
| Application | http://localhost:8085 |
| Health Check | http://localhost:8085/actuator/health |
| Liveness Probe | http://localhost:8085/actuator/health/liveness |
| Readiness Probe | http://localhost:8085/actuator/health/readiness |
| Swagger UI | http://localhost:8085/swagger-ui.html |
| OpenAPI Spec | http://localhost:8085/api-docs |
| Training Types API | http://localhost:8085/api/training_types |

## Common Commands

### View Logs
```bash
# All services
docker compose logs -f

# App only (docker compose)
docker compose logs -f app

# App only (docker directly)
docker logs -f gym-crm-app
```

### Diagnostic Shell Access
```bash
# Execute commands inside app container
docker exec gym-crm-app sh -c "ls -la /app && java -version"
```

### Stop and Restart Application (Verify DB Reconnection)
```bash
# Stop app only 
docker compose stop app

# Start app again
docker compose start app
```

### Stop All Services
```bash
# Stop and remove containers
docker compose down

# Stop and remove containers AND volumes
docker compose down -v
```