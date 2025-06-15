# Purpose
Headless game server running the same ECS logic as the client and exposing network services.

# Key tasks
- Launch via `./gradlew :server:run`.
- Broadcast state updates to connected clients.

# Testing notes
- Follow the root module steps before running tests.
- See [`../docs/developer_workflow.md`](../docs/developer_workflow.md) for build, style and coverage commands.
