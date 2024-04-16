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
- [ ] animations
- [ ] particles
- [ ] frustum culling
- [ ] scene loading/saving/parsing
- [ ] preload models and textures?
- [ ] double buffering
- [ ] material texture loading
- [ ] compute shaders (if school supports it)
- [ ] batch rendering with just model data and modelMatrices (no cpu calculations)
### Game
- [ ] space invaders demo

- [ ] Lobby
  - [ ] Item-Stash
  - [ ] Character Selection
- [ ] Map
  - [ ] Graphics
    - [ ] fantasy forest
    - [ ] layered
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
      - [ ] dash
      - [ ] projectile salve (getting bigger with each bullet/hit?)
      - [ ] slow big projectiles
      - [ ] fast small projectiles
      - [ ] shield
      - [ ] piercing
      - [x] homing
      - [ ] shotgun/spread
    - [ ] simulation distance
      
- [x] …

## Resources
Tutorials: <br>
<sub> text </sub>
[LWJGL - Get Started](https://www.lwjgl.org/guide) <br>
[Open Gl Doc](https://docs.gl) <br>
[learnopengl.com | ausführliches e-Buch](https://learnopengl.com/) <br>
[MDN Web Docs](https://developer.mozilla.org/en-US/docs/Games/Techniques/2D_collision_detection) <br>
[LWJGL3-demos](https://github.com/LWJGL/lwjgl3-demos) <br>
<sub> videos </sub>
[The Cherno - OpenGl series on YouTube](https://www.youtube.com/watch?v=W3gAzLwfIP0&list=PLlrATfBNZ98foTJPJ_Ev03o2oq3-GGOS2) <br>
[GamesWithGabe - Making a 2D game in java](https://www.youtube.com/watch?v=VyKE7vz65rY&list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE) <br>
[Acerola](https://www.youtube.com/@Acerola_t) <br>
[Vercidium](https://www.youtube.com/@Vercidium) <br>
[Brendan Galea - The Math behind (most) 3D games - Perspective Projection](https://www.youtube.com/watch?v=Do_vEjd6gF0) <br>
[pikuma - Math for Game Developers: Why do we use 4x4 Matrices in 3D Graphics?](https://www.youtube.com/watch?v=Do_vEjd6gF0) <br>
[3Blue1Brown: Linear Algebra](https://www.youtube.com/playlist?list=PL0-GT3co4r2y2YErbmuJw2L5tW4Ew2O5B) <br>
[Coding Adventure: Simulating Fluids](https://www.youtube.com/watch?v=rSKMYc1CQHE) <br>


Assets: <br>
[Font](https://opengameart.org/content/ascii-bitmap-font-oldschool#comment-105057) <br>
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
