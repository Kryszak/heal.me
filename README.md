![Build Status](https://github.com/Kryszak/heal.me/actions/workflows/build.yml/badge.svg)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/5f435e91f78544f5a7634174aab97a5f)](https://app.codacy.com/gh/Kryszak/heal.me/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Codacy Badge](https://app.codacy.com/project/badge/Coverage/5f435e91f78544f5a7634174aab97a5f)](https://app.codacy.com/gh/Kryszak/heal.me/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_coverage)

# heal.me
Example recruitment task with purpose of "showing off how I code" for Kotlin + Springboot stack

## Project description
Check [requirements](./requirements.md)

## Development
For development purposes there is docker compose setup for local PostgreSQL db setup.

Before running server with either `./gradlew bootRun` or provided Intellij IDEA run configuration
run 
```
docker-compose up
``` 
in project directory.

For convenience you can also use `just` command runner with provided configuration.
