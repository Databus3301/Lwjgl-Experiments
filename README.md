# Lwjgl-Experiments

Documenting my learning process of Open GL and LWJGL for a school project

## Contents (/Tests)
- Pong
- Camera
- Simple Textures
- Clear colour
- .Obj Model Parser 
  - .Obj Model normalisation
- .Obj Model Renderer 2D
- .Obj 3D spinning model
- Batch Rendering
- 2D Collision Detection
- Primitives
- Mouse Input
- Keyboard Input
- Text Rendering
- Interactables
  - Buttons
- Sound
- Ability System
- Enemy System
- normalised invincibility frames
- texture atlassing

## WOP
### Engine
- [ ] OBJ/3D texture loading
- [ ] shading / lighting / dithering shader
- [ ] 3D camera
- [x] animations
- [ ] particles
- [ ] frustum culling
- [ ] scene loading/saving/parsing
- [ ] preload models and textures?
- [x] double buffering
- [ ] material texture loading
- [ ] compute shaders (if school supports it)
- [ ] batch rendering with just model data and modelMatrices (no cpu calculations)
- [x] post processing shaders
### Game
- [ ] first launch
  - spawn in a room
    - post processing shader
      - wobbly fading out
      - pixelated
      - color channel swizzleing

- [ ] Lobby
  - [ ] Item-Stash
  - [ ] Character Selection
- [ ] Map
  - [ ] Gameplay
    - [ ] enemy waves
      - [ ] possible interlacing if cleared to slow
      - [ ] items/abilities to delay wave starts?
    - [ ] on map events
      - [ ] boss altars
      - [ ] shrines
        - [ ] ability / healing / quest
      - [ ] chests
        - [ ] items / healing 
    - Abilities
      - [x] dash
      - [x] projectile salve (getting bigger with each bullet/hit?)
      - [ ] slow big projectiles
      - [ ] fast small projectiles
      - [x] shield
      - [x] shooting in all directions
      - [ ] piercing
      - [x] homing
      - [ ] shotgun/spread
    - Enemies
      - [ ] basic
          - takes the direct route to the player
          - has no special abilities
          - basic HP
      - [ ] fast
      - [ ] tanky
      - [ ] ranged
      - [ ] flying
      - [ ] boss
    - [ ] simulation distance
    - Rooms
      - [x] drawing
      - [x] generation
      - [ ] room types
        - [x] normal
        - [ ] boss
        - [ ] shop
        - [ ] event
        - [ ] start
        - [ ] end
      
  - get ability on room completion
  - get upgrade on lvl up

## Resources
Tutorials: <br>
<sub> text </sub>
[LWJGL - Get Started](https://www.lwjgl.org/guide) <br>
[Open Gl Doc](https://docs.gl) <br>
[learnopengl.com | ausführliches e-Buch](https://learnopengl.com/) <br>
[MDN Web Docs](https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection) <br>
[LWJGL3-demos](https://github.com/LWJGL/lwjgl3-demos) <br>
[Signed Distance Functions for Shaders](https://iquilezles.org/articles/distfunctions2d/) <br>
[GLFW - documentation](https://www.glfw.org/docs/latest/) <br>
<sub> video </sub>
[The Cherno - OpenGl series on YouTube](https://www.youtube.com/watch?v=W3gAzLwfIP0&list=PLlrATfBNZ98foTJPJ_Ev03o2oq3-GGOS2) <br>
[GamesWithGabe - Making a 2D game in java](https://www.youtube.com/watch?v=VyKE7vz65rY&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE) <br>
[Acerola](https://www.youtube.com/@Acerola_t) <br>
[Vercidium](https://www.youtube.com/@Vercidium) <br>
[Brendan Galea - The Math behind (most) 3D games - Perspective Projection](https://www.youtube.com/watch?v=Do_vEjd6gF0) <br>
[pikuma - Math for Game Developers: Why do we use 4x4 Matrices in 3D Graphics?](https://www.youtube.com/watch?v=Do_vEjd6gF0) <br>
[3Blue1Brown: Linear Algebra](https://www.youtube.com/playlist?list=PL0-GT3co4r2y2YErbmuJw2L5tW4Ew2O5B) <br>
[Coding Adventure: Simulating Fluids](https://www.youtube.com/watch?v=rSKMYc1CQHE) <br>
[Introduction to shaders: Learn the basics! - Barney Codes](https://www.youtube.com/watch?v=3mfvZ-mdtZQ) <br>
[An introduction to Shader Art Coding - kishimisu](https://www.youtube.com/watch?v=f4s1h2YETNY&t=1187s) <br>
[I Added Realistic Physics to Minecraft -- 3D Rigid Body Physics Engine Datapack […]](https://www.youtube.com/watch?v=DhCBCudKJTs)<br>
[How 2D Game Collision Works (Separating Axis Theorem)](https://youtu.be/dn0hUgsok9M?si=RC3326ZOYTs-CUk3)


Assets: <br>
[Font](https://opengameart.org/content/ascii-bitmap-font-oldschool#comment-105057) <br>
[Sound](https://sfxr.me/) <br>
[Normal Music](https://www.fesliyanstudios.com/royalty-free-music/download/retro-platforming/454) <br>
[Boss Music](https://www.fesliyanstudios.com/royalty-free-music/download/boss-time/2340) <br>
[Skull](https://www.pinterest.com/pin/496029346460251614/) <br>
<sub>tmp:</sub> <br>
[Camera Model](https://rigmodels.com/model.php?view=Camera-3d-model__7WSLWPG7ZGVUXV18PUHLN2G4N) <br>
[Link Animtion](https://forum.unity.com/attachments/linkedit-png.80767/)

## intelij-IDEA "install" (hopefully)
- git clone / new project from VCS
- project-structure
     - select newest sdk
- import lib folder as lib (right-click)
- import lwjgl2 folder as lwjgl lib
- set run configuration to new application and set program arguments to "-" "TEST_NAME" "dimX" "dimY" or provide program args via some other way
- potentially run IDEA-repair tool

- TEST_NAMEs can be found jn the window class
