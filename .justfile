[default]
_list_available:
    @just --list --unsorted

# Build project
[group('Build')]
build:
    ./gradlew build

# Run project locally
[group('Dev')]
run:
    ./gradlew bootRun --args='--spring.profiles.active=local'

# Create postgresql container
[group('Dev')]
_create_postgres_service:
    docker-compose create

# Start dev postgresql instance
[group('Dev')]
postgres-start: _create_postgres_service
    docker-compose start

# Stop dev postgresql instance
[group('Dev')]
postgres-stop:
    docker-compose stop