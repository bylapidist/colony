# Purpose
Headless game server running the same ECS logic as the client and exposing network services.

# Key tasks
- Launch via `./gradlew :server:run`.
- Broadcast state updates to connected clients.

# Testing notes
- Follow the root module steps before running tests: `tests:copyAssets`, `clean test`, and `check`.
