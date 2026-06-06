# IoT Smart-Plug Hub & Simulator

[![CI](https://github.com/nhp-atel/nhp-atel.github.io/actions/workflows/ci.yml/badge.svg)](https://github.com/nhp-atel/nhp-atel.github.io/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-8-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-1.5.x-brightgreen)
![MQTT](https://img.shields.io/badge/MQTT-Eclipse%20Paho-purple)
![Build](https://img.shields.io/badge/build-Gradle-blue)

A distributed **Internet-of-Things control system** that manages a fleet of simulated smart plugs. A **Spring Boot** hub exposes a REST API and a web dashboard, communicating with an independent device **simulator** over an **MQTT** message broker. Built as an academic systems project and extended with containerization and a CI/CD pipeline to mirror a production engineering workflow.

---

## Architecture

The system is split into two independently deployable services that are decoupled through an MQTT broker — a publish/subscribe pattern that lets the hub and any number of devices scale and fail independently.

```
            ┌──────────────────────┐         REST / JSON        ┌──────────────────┐
            │     Web Dashboard    │ ◄────────────────────────► │     IoT Hub      │
            │ (HTML/JS + Bootstrap)│   /api/plugs  /api/groups  │  (Spring Boot)   │
            └──────────────────────┘                            └────────┬─────────┘
                                                                         │
                                                            MQTT publish │ subscribe
                                                       action/* ▼        │ ▲ update/*
                                                          ┌──────────────┴───────────┐
                                                          │       MQTT Broker         │
                                                          │    (Eclipse Mosquitto)    │
                                                          └──────────────┬───────────┘
                                                            subscribe ▲  │ ▼ publish
                                                                       │  │
                                                          ┌────────────┴──┴───────────┐
                                                          │      IoT Simulator        │
                                                          │  Smart-plug devices +     │
                                                          │  power-measurement loop   │
                                                          └───────────────────────────┘
```

- **IoT Hub** (`ece448.iot_hub`) — Spring Boot service. Exposes a REST API for querying/controlling plugs and managing plug *groups*, serves the web dashboard, and translates HTTP requests into MQTT control messages. Maintains in-memory state synchronized from device `update/*` messages.
- **IoT Simulator** (`ece448.iot_sim`) — Simulates physical smart plugs. Subscribes to `action/*` commands (on/off/toggle), runs a periodic power-measurement loop, and publishes state/power changes back as `update/*` messages. Uses an **Observer pattern** to push device changes to MQTT.
- **MQTT Broker** — Decouples the hub from devices. Topics follow `iot_ece448/action/<plug>/<command>` and `iot_ece448/update/<plug>/<key>`.

---

## Tech Stack

| Layer            | Technologies |
|------------------|--------------|
| Backend          | Java 8, Spring Boot (`spring-boot-starter-web`), Spring DI/IoC |
| Messaging        | MQTT via Eclipse Paho client; Eclipse Mosquitto broker |
| Frontend         | HTML, CSS, vanilla JavaScript, Bootstrap, jQuery |
| Build & Test     | Gradle, JUnit 4, JaCoCo (code coverage) |
| DevOps           | Docker, Docker Compose, GitHub Actions (CI/CD) |

---

## REST API

| Method | Endpoint                              | Description |
|--------|---------------------------------------|-------------|
| `GET`  | `/api/plugs`                          | List all plugs with current state and power draw |
| `GET`  | `/api/plugs/{plug}`                   | Get a single plug's state and power |
| `GET`  | `/api/plugs/{plug}?action=on\|off\|toggle` | Send a control command to a plug (published over MQTT) |
| `GET`  | `/api/groups`                         | List all plug groups and their aggregate state |
| `GET`  | `/api/groups/{group}`                 | Get a single group |
| `POST` | `/api/groups/{group}`                 | Create/update a group |
| `DELETE`| `/api/groups/{group}`                | Delete a group |

---

## Running the project

### Option A — Docker Compose (recommended)

Brings up the broker, simulator, and hub together. No local Java/Gradle install needed.

```bash
docker compose up --build
```

Then open **http://localhost:8088/index.html**.

### Option B — Run locally with Gradle

Requires JDK 8 and a running MQTT broker on `tcp://127.0.0.1:1883`
(e.g. `docker run -p 1883:1883 eclipse-mosquitto:2`).

```bash
# Terminal 1 — start the device simulator
gradle iot_sim

# Terminal 2 — start the hub (Spring Boot)
gradle iot_hub
```

Then open **http://localhost:8088/index.html** and interact with the simulated plugs.

---

## Testing & Coverage

The project is tested with **JUnit** and measures coverage with **JaCoCo**. Tests run automatically before each `iot_sim` / `iot_hub` task and on every CI run.

```bash
gradle clean test jacocoTestReport
# HTML coverage report: build/reports/jacoco/test/html/index.html
```

Test suites cover the plug simulator logic (`PlugSimTests`), the HTTP command layer (`HTTPCommandsTests`), and the hub's REST resources (`PlugsResourceTest`, `GroupsResourceTest`).

---

## CI/CD

Every push and pull request to `main` triggers the [GitHub Actions pipeline](.github/workflows/ci.yml), which:

1. Builds the project on JDK 8 with Gradle
2. Runs the full JUnit test suite
3. Generates and uploads the JaCoCo coverage report as a build artifact
4. Builds the Docker image to verify the container packaging

---

## Engineering highlights

- **Distributed, event-driven design** — services communicate asynchronously over MQTT pub/sub rather than tight coupling, allowing devices to be added/removed without changing the hub.
- **Spring dependency injection** — the MQTT controller and REST resources are wired as Spring beans with managed lifecycles (`@Bean(destroyMethod = "close")`).
- **Design patterns** — Observer pattern for device-to-broker state propagation; configuration externalized to JSON for environment-specific deployment.
- **Production-style workflow** — containerized with Docker Compose, gated by an automated CI pipeline with test coverage reporting.
