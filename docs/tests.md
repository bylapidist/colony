# Scenario Test Harness

`GameSimulation` boots a LibGDX application in headless mode so gameplay systems can be
verified without a graphical window. The helper loads a `MapState` through
`MapLoadSystem` and attaches the client networking systems that react to server
messages.

`tests/scenario/GameSimulationTileUpdateTest.java` shows a typical flow. A
`GameServer` and two `GameClient` instances are started. Once the receiver
obtains the initial map state it constructs `GameSimulation` with that state and
the client. The test sends a tile selection request via the sender, waits for the
server broadcast and then calls `GameSimulation.step()`. Assertions inspect the
ECS world to confirm the tile update was applied just like during normal play.
