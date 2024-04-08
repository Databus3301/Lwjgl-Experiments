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

 
 public class Player extends Living {
     private Test scene;
     private Entity2D entity;
     private final ArrayList<Ability> abilities = new ArrayList<>();
     private final Map<String, Animation> animations = new HashMap<>();
     private Animation currentAnimation;
 
     public Player(Entity2D player, int maxLivePoints, Map<String, Animation> animations) {
         this.entity = player;
         this.maxLP = maxLivePoints;
         this.LP = maxLivePoints;
         this.animations.putAll(animations);
     }
     public <T extends Test> Player(T scene, Entity2D player, int maxLivePoints) {
         scene.addUpdateListener(this::update);
         this.scene = scene;
         this.entity = player;
         this.maxLP = maxLivePoints;
         this.LP = maxLivePoints;

         // add shooting ability
         abilities.add(Abilities.SHOOT.setScene(scene));
     }
 
     public void update(float dt, Vector2f mousePos) {
        if(currentAnimation != null)
            currentAnimation.update(dt);

        for (Ability ability : abilities) {
            ability.update(dt, mousePos, entity);
            ability.getProjectiles().removeIf(projectile -> projectile.getPosition().x < -Window.dim.x / 2f + entity.getPosition().x - 100 || projectile.getPosition().x > Window.dim.x / 2f + entity.getPosition().x + 100 || projectile.getPosition().y < -Window.dim.y / 2f + entity.getPosition().y - 100 || projectile.getPosition().y > Window.dim.y / 2f + entity.getPosition().y + 100);
        }
     }

     public <T extends Living> void collide(ArrayList<T> entities) {
         for (Living collider : entities) {
             // push away from player
             if (collider.collideCircle(this.entity)) {
                 collider.translate(
                         new Vector2f(entity.getPosition()).sub(collider.getPosition())
                        .sub( (float) Math.random()*20f-1f, (float) Math.random()*20f-1f) // randomize a bit (to avoid getting stuck in a loop of pushing each other back and forth
                        .normalize().mul(-1)
                 );
                 damage();
             }
         }
         // collide projectiles / custom ability colliders
         abilities.forEach(ability -> ability.collide(entities));
     }

     public void addAbility(Ability ability) {
         abilities.add(ability);
     }
     public void removeAbility(Ability ability) {
         abilities.remove(ability);
     }
     public void addAnimation(String name, Animation animation) {
         animations.put(name, animation);
     }
     public void removeAnimation(String name) {
         animations.remove(name);
     }
     public void switchAnimation(String name) {
         currentAnimation = animations.get(name);
         entity.setAnimation(currentAnimation);
     }
 
 
     public Entity2D getEntity() {
         return entity;
     }
     public ArrayList<Ability> getAbilities() {
         return abilities;
     }
     public Map<String, Animation> getAnimations() {
          return animations;
     }

 }