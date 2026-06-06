# ---------------------------------------------------------------------------
# Build image for the IoT Hub / Simulator (Spring Boot, Java 8, Gradle).
# Tests + JaCoCo coverage run at build time, so the image only builds if the
# test suite is green — the same gate the CI pipeline enforces.
# ---------------------------------------------------------------------------
# Pin the platform: the gradle:5.6.4-jdk8 base image only ships for linux/amd64,
# so this keeps the build reproducible on ARM hosts (e.g. Apple Silicon) too.
FROM --platform=linux/amd64 gradle:5.6.4-jdk8 AS build

WORKDIR /app
COPY --chown=gradle:gradle . .

# Compile and run the full test + coverage suite. Build fails fast if tests fail.
RUN gradle clean test jacocoTestReport classes --no-daemon

# Hub REST API (8088) and Simulator HTTP (8080)
EXPOSE 8088 8080

# Default command runs the Hub; docker-compose overrides this per service.
CMD ["gradle", "iot_hub", "-Pconfig=hubConfig.docker.json", "-x", "test", "-x", "jacocoTestReport", "--no-daemon"]
