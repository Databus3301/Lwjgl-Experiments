 package Game.Entities;
        
 import Game.Action.Abilities;
 import Game.Action.Ability;
 import Render.Entity.Entity2D;
 import Render.MeshData.Texturing.Animation;

 import Render.Window;
 import Tests.Test;
 import org.joml.Vector2f;

 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.Map;

 
 public class Player extends Living implements Able {
     private Test scene;
     private final ArrayList<Ability> abilities = new ArrayList<>();
     private final Map<String, Animation> animations = new HashMap<>();

     public Player(Entity2D player, int maxLivePoints, Map<String, Animation> animations) {
         player.clone(this);
         this.maxLP = maxLivePoints;
         this.LP = maxLivePoints;
         this.animations.putAll(animations);
     }
     public <T extends Test> Player(T scene, Entity2D player, int maxLivePoints) {
         scene.addUpdateListener(this::update);
         player.clone(this);
         this.scene = scene;
         this.maxLP = maxLivePoints;
         this.LP = maxLivePoints;

         // --TEMP-- add shooting ability as a default
         addAbility(Abilities.getHOMING());

     }
 
     public void update(float dt, Vector2f mousePos) {
        if(animation != null)
            animation.update(dt);

        for (Ability ability : abilities) {
            ability.update(dt, mousePos, this);
            // remove out-of-view projectiles
            ability.getProjectiles().removeIf(projectile -> projectile.getPosition().x < -Window.dim.x / 2f + position.x - 100 || projectile.getPosition().x > Window.dim.x / 2f + position.x + 100 || projectile.getPosition().y < -Window.dim.y / 2f + position.y - 100 || projectile.getPosition().y > Window.dim.y / 2f + position.y + 100);
        }
     }

     public <T extends Living> void collide(ArrayList<T> entities) {
         for (Living collider : entities) {
             // push away from player
             if (collider.collideRect(this)) {
                 collider.translate(
                         new Vector2f(position).sub(collider.getPosition())
                        .sub( (float) Math.random()*20f-1f, (float) Math.random()*20f-1f) // randomize a bit (to avoid getting stuck in a loop of pushing each other back and forth
                        .normalize().mul(-5)
                 );
                 damage();
             }
         }
         // collide projectiles / custom ability colliders
         abilities.forEach(ability -> ability.collide(entities));
     }

     public void addAnimation(String name, Animation animation) {
         animations.put(name, animation);
     }
     public void removeAnimation(String name) {
         animations.remove(name);
     }
     public void switchAnimation(String name) {
        animation = animations.get(name);
     }

     public ArrayList<Ability> getAbilities() {
         return abilities;
     }
     public Map<String, Animation> getAnimations() {
          return animations;
     }

 }
