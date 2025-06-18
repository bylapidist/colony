# Controls

This page lists the default keyboard bindings and how to customize them in-game.

## Default key bindings

| Action | Key | Description |
|-------|----|-------------|
| Move Up | **W** | Pan the camera upward |
| Move Down | **S** | Pan the camera downward |
| Move Left | **A** | Pan the camera to the left |
| Move Right | **D** | Pan the camera to the right |
| Gather | **H** | Order selected tiles to gather resources |
| Build | **B** | Toggle building placement mode |
| Chat | **T** | Open the chat input box |
| Toggle Minimap | **M** | Show or hide the minimap |
| Toggle Camera | **F** | Switch between map overview and player camera |

The defaults are defined in [`KeyBindings.java`](../core/src/main/java/net/lapidist/colony/settings/KeyBindings.java).

Placing a building consumes resources. Houses cost **1 wood**, markets cost **5 wood and 2 stone**, and factories require **10 wood and 5 stone**. If you don't have enough resources the build request is ignored.

## Changing bindings

1. Launch the game and open the **Settings** menu from the main screen.
2. Click **Keybinds** to open the remapping screen.
3. Select an action, then press the new key you want to use.
4. Press **Back** to save your changes. Use **Reset** to restore the defaults.

The **Load Game** screen includes a search box at the top for quickly filtering
saved games by name.
