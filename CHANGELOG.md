# Change Log

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

# [5.6.0](https://gitlab.com/getbrett/colony/compare/v5.5.0...v5.6.0) (2019-08-06)


### Features

* implement (slightly broken) chunks ([c90ef44](https://gitlab.com/getbrett/colony/commit/c90ef44))
* implement Console window ([834b4bb](https://gitlab.com/getbrett/colony/commit/834b4bb))
* implement GodControl and GodCamera systems ([20fb4a3](https://gitlab.com/getbrett/colony/commit/20fb4a3))



# [5.5.0](https://gitlab.com/getbrett/colony/compare/v5.4.0...v5.5.0) (2019-06-28)


### Features

* implement GdxTestRunner and FileLocation tests ([e166dfd](https://gitlab.com/getbrett/colony/commit/e166dfd))


# [5.4.0](https://gitlab.com/getbrett/colony/compare/v5.3.0...v5.4.0) (2019-05-07)



### Features

* **build:** implement unit tests ([939d979](https://gitlab.com/getbrett/colony/commit/939d979))



# [5.3.0](https://gitlab.com/getbrett/colony/compare/v5.2.1...v5.3.0) (2019-05-04)


### Features

* **CI:** add gitlab CI integration ([b95bf6d](https://gitlab.com/getbrett/colony/commit/b95bf6d))



## [5.2.1](https://gitlab.com/getbrett/colony/compare/v5.2.0...v5.2.1) (2019-04-05)



# [5.2.0](https://gitlab.com/getbrett/colony/compare/v5.1.0...v5.2.0) (2019-04-05)


### Bug Fixes

* blah ([83090ad](https://gitlab.com/getbrett/colony/commit/83090ad))
* run npm i ([adc8d4f](https://gitlab.com/getbrett/colony/commit/adc8d4f))
* this stuff ([b89a10c](https://gitlab.com/getbrett/colony/commit/b89a10c))


### Features

* add standard-version ([b8088f9](https://gitlab.com/getbrett/colony/commit/b8088f9))



<a name="5.1.0"></a>
# [5.1.0](https://gitlab.com/getbrett/colony/compare/v5.0.0...v5.1.0) (2019-01-26)


### Features

* **engine:** implement variable season and time of day colours ([32d6a67](https://gitlab.com/getbrett/colony/commit/32d6a67))
* **gameplay:** player's flashlight is only enabled during night, dawn, evening and dusk ([962ad52](https://gitlab.com/getbrett/colony/commit/962ad52))
* **gui:** give seasons to GUI ([67320ff](https://gitlab.com/getbrett/colony/commit/67320ff))
* **logic:** implement TimeSystem ([5242e78](https://gitlab.com/getbrett/colony/commit/5242e78))



<a name="5.0.0"></a>
# [5.0.0](https://gitlab.com/getbrett/colony/compare/v4.3.0...v5.0.0) (2019-01-26)


### Bug Fixes

* **engine:** camera orientation ([e3cd7bc](https://gitlab.com/getbrett/colony/commit/e3cd7bc))
* **engine:** Fix width of rendered sprites ([253fdd5](https://gitlab.com/getbrett/colony/commit/253fdd5))


### Features

* **engine:** allow placing buildings in world ([41dcc8d](https://gitlab.com/getbrett/colony/commit/41dcc8d))
* **engine:** async asset loading system ([bfdd748](https://gitlab.com/getbrett/colony/commit/bfdd748))
* **engine:** better asset logging ([9a91d0d](https://gitlab.com/getbrett/colony/commit/9a91d0d))
* **engine:** center PlayerCamera on player origin ([93155a7](https://gitlab.com/getbrett/colony/commit/93155a7))
* **engine:** don't render tiles outside viewport ([34346c7](https://gitlab.com/getbrett/colony/commit/34346c7))
* **engine:** give hovered flag to TileComponent ([0006dd2](https://gitlab.com/getbrett/colony/commit/0006dd2))
* **engine:** give player a pointlight ([f827c62](https://gitlab.com/getbrett/colony/commit/f827c62))
* **engine:** implement abstract sortable component batching ([72b4e62](https://gitlab.com/getbrett/colony/commit/72b4e62))
* **engine:** implement conelights ([241d812](https://gitlab.com/getbrett/colony/commit/241d812))
* **engine:** implement EntityFactorySystem ([811e478](https://gitlab.com/getbrett/colony/commit/811e478))
* **engine:** implement GuiRenderSystem FPS counter ([63a50e4](https://gitlab.com/getbrett/colony/commit/63a50e4))
* **engine:** implement limiting player to map bounds ([cbfde36](https://gitlab.com/getbrett/colony/commit/cbfde36))
* **engine:** implement PlayerComponent and hook to MapPhysicsSystem ([a4f0d14](https://gitlab.com/getbrett/colony/commit/a4f0d14))
* **engine:** let's go back to basics ([d8b7db2](https://gitlab.com/getbrett/colony/commit/d8b7db2))
* **engine:** player rotation ([16f78b6](https://gitlab.com/getbrett/colony/commit/16f78b6))
* **engine:** properly center on player character ([b0d87c5](https://gitlab.com/getbrett/colony/commit/b0d87c5))
* **engine:** reimplement player control ([3f70dfb](https://gitlab.com/getbrett/colony/commit/3f70dfb))
* **engine:** update legacy events to extend AbstractEvent ([01e6351](https://gitlab.com/getbrett/colony/commit/01e6351))


### Performance Improvements

* **engine:** remove unnecessary calls to .set() in CameraSystem ([6327e62](https://gitlab.com/getbrett/colony/commit/6327e62))



<a name="4.3.0"></a>
# [4.3.0](https://gitlab.com/getbrett/colony/compare/v4.2.0...v4.3.0) (2019-01-19)


### Bug Fixes

* **engine:** make buildings static ([5211cfe](https://gitlab.com/getbrett/colony/commit/5211cfe))
* **engine:** redraw tile selection when player is moving ([af6f7ff](https://gitlab.com/getbrett/colony/commit/af6f7ff))


### Features

* **engine:** implement box2dlights ([2013d7c](https://gitlab.com/getbrett/colony/commit/2013d7c))
* **engine:** Implement lighting system ([aad3f49](https://gitlab.com/getbrett/colony/commit/aad3f49))



<a name="4.2.0"></a>
# [4.2.0](https://gitlab.com/getbrett/colony/compare/v4.1.0...v4.2.0) (2019-01-19)


### Bug Fixes

* **engine:** cell rendering ([2a06ae2](https://gitlab.com/getbrett/colony/commit/2a06ae2))
* **engine:** Dependency ordering ([3bca179](https://gitlab.com/getbrett/colony/commit/3bca179))


### Features

* **engine:** convert GuiRenderingSystem to EntityProcessingSystem ([3752c57](https://gitlab.com/getbrett/colony/commit/3752c57))
* **engine:** Give player VelocityComponent ([f823bf2](https://gitlab.com/getbrett/colony/commit/f823bf2))
* **engine:** Implement player reach ([bb8b093](https://gitlab.com/getbrett/colony/commit/bb8b093))
* **engine:** Reimplement controls ([241fa01](https://gitlab.com/getbrett/colony/commit/241fa01))
* **engine:** Switch to TiledMapRenderer ([1a0e047](https://gitlab.com/getbrett/colony/commit/1a0e047))
* **gameplay:** Increase player friction ([0b1eb6e](https://gitlab.com/getbrett/colony/commit/0b1eb6e))



<a name="4.1.0"></a>
# [4.1.0](https://gitlab.com/getbrett/colony/compare/v4.0.0...v4.1.0) (2019-01-18)


### Features

* **engine:** Give map bounds ([37d06ec](https://gitlab.com/getbrett/colony/commit/37d06ec))
* **engine:** Remove docs and desktop project ([2011863](https://gitlab.com/getbrett/colony/commit/2011863))
* **engine:** Split MapRenderingSystem and GuiRenderingSystem ([aac4c12](https://gitlab.com/getbrett/colony/commit/aac4c12))
* **engine:** Split out MapGenerationSystem ([565abc9](https://gitlab.com/getbrett/colony/commit/565abc9))


### Performance Improvements

* **engine:** Optimise for large map sizes ([ad6ee96](https://gitlab.com/getbrett/colony/commit/ad6ee96))



<a name="4.0.0"></a>
# [4.0.0](https://gitlab.com/getbrett/colony/compare/v3.0.0...v4.0.0) (2019-01-13)


### Features

* **engine:** Move to artemis-odb ([893f4aa](https://gitlab.com/getbrett/colony/commit/893f4aa))



<a name="3.0.0"></a>
# [3.0.0](https://gitlab.com/getbrett/colony/compare/v2.1.0...v3.0.0) (2019-01-06)


### Features

* **engine:** Add fbx-conv script ([83cee47](https://gitlab.com/getbrett/colony/commit/83cee47))
* **engine:** Implement loading g3db model files ([78ea40a](https://gitlab.com/getbrett/colony/commit/78ea40a))
* **engine:** Implement loading g3db model files ([45fd2bc](https://gitlab.com/getbrett/colony/commit/45fd2bc))
* **engine:** Pull postprocessing into core namespace ([7b8adb8](https://gitlab.com/getbrett/colony/commit/7b8adb8))
* **engine:** Split rendering of terrain and units ([6403bd6](https://gitlab.com/getbrett/colony/commit/6403bd6))



<a name="2.1.0"></a>
# [2.1.0](https://gitlab.com/getbrett/colony/compare/v2.0.0...v2.1.0) (2018-12-30)


### Bug Fixes

* **engine:** Remove Camera from common ([7079e01](https://gitlab.com/getbrett/colony/commit/7079e01))


### Features

* **engine:** Implement InputManager ([a0d4ecf](https://gitlab.com/getbrett/colony/commit/a0d4ecf))
* **engine:** Implement shader system ([67cee83](https://gitlab.com/getbrett/colony/commit/67cee83))
* **engine:** Switch to square grid map ([1e856ef](https://gitlab.com/getbrett/colony/commit/1e856ef))



<a name="2.0.0"></a>
# [2.0.0](https://gitlab.com/getbrett/colony/compare/v1.2.0...v2.0.0) (2018-11-05)


### Features

* **engine:** Implement BitmapFont and Skin loading ([d8f8419](https://gitlab.com/getbrett/colony/commit/d8f8419))



<a name="1.2.0"></a>
# [1.2.0](https://gitlab.com/getbrett/colony/compare/v1.1.0...v1.2.0) (2018-10-28)


### Bug Fixes

* **engine:** Race condition when loading resources before game engine elements ([dbf33d8](https://gitlab.com/getbrett/colony/commit/dbf33d8))
* Merge conflict ([28121e2](https://gitlab.com/getbrett/colony/commit/28121e2))


### Features

* **engine:** Implement EventQueue ([5a26bc9](https://gitlab.com/getbrett/colony/commit/5a26bc9))
* **engine:** Implement PlayerSystem ([d2fb128](https://gitlab.com/getbrett/colony/commit/d2fb128))
* **engine:** WIP SunShader ([1484cef](https://gitlab.com/getbrett/colony/commit/1484cef))



<a name="1.1.0"></a>
# [1.1.0](https://gitlab.com/getbrett/colony/compare/v1.0.1...v1.1.0) (2018-10-19)


### Features

* **docs:** Implement javadocs ([a45304c](https://gitlab.com/getbrett/colony/commit/a45304c))
* **engine:** Implement model rendering pipeline ([bdd4867](https://gitlab.com/getbrett/colony/commit/bdd4867))
* **engine:** Implement support for loading shaders via resourceLoader ([97a3d25](https://gitlab.com/getbrett/colony/commit/97a3d25))
* **engine:** Upgrade backend to lwjgl3 ([67d73d7](https://gitlab.com/getbrett/colony/commit/67d73d7))



<a name="1.0.1"></a>
## [1.0.1](https://gitlab.com/getbrett/colony/compare/1.0.0...v1.0.1) (2018-10-18)


### Features

* **build:** Implement conventional-changelog and add basic README.md ([6545d41](https://gitlab.com/getbrett/colony/commit/6545d41))
* **engine:** Implement ModelBatch to Core ([ee2767a](https://gitlab.com/getbrett/colony/commit/ee2767a))



<a name="1.0.0"></a>
# [1.0.0](https://gitlab.com/getbrett/colony/compare/5944382...1.0.0) (2018-10-18)


### Features

* **engine:** Implement 3D hex grid engine ([5944382](https://gitlab.com/getbrett/colony/commit/5944382))
