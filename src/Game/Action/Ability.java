package Game.Action;

import Audio.AudioClip;
import Audio.AudioSource;
import Game.Entities.Living;
import Game.Entities.Player;
import Game.Entities.Projectiles.Projectile;
import Render.Entity.Entity2D;
import Tests.Test;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static Game.Entities.Dungeon.Dungeon.EFFECT_VOLUME;

public class Ability {
    private Test scene;
    private Projectile[] projectileTypes;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    /**
     * Cooldown in seconds
     */
    private float cooldown;
    private float currentCooldown;

    private String name = "NO NAME";
    private String description;
    private UUID uuid;

    private ArrayList<Upgrade> upgrades = new ArrayList<>();
    public HashMap<String, Float> stats = new HashMap<>();
    private int level;

    private AudioSource audioSource = new AudioSource();
    private AudioClip sound = null;

    public HexConsumer<Ability, Float, Vector2f, Vector2f, Entity2D, Test> onTrigger;
    public Consumer<Ability> onUpgradeGen = (a) -> {a.setUpgrades(Upgrades.getDefaults());};
    public BiConsumer<Ability, Living> onApplyToAble = (a, b) -> {};

    public Ability(Projectile[] projectileTypes, float cooldown) {
        this.projectileTypes = projectileTypes;
        this.cooldown = cooldown;
        this.currentCooldown = .5f;
        audioSource.setVolume(EFFECT_VOLUME);
        uuid = UUID.randomUUID();
    }

    public Ability() {
        this.projectileTypes = new Projectile[]{new Projectile()};
        this.cooldown = 1f;
        this.currentCooldown = .5f;
        audioSource.setVolume(EFFECT_VOLUME);
        uuid = UUID.randomUUID();
    }

    public void update(float dt, Vector2f mousePos, Entity2D trigger) {
        update(dt, mousePos, Test.renderer.screenToWorldCoords(mousePos), trigger);
    }

    public void update(float dt, Vector2f mousePos, Vector2f target, Entity2D trigger) {
        if (trigger instanceof Player p && p.getAutoshooting() || !(trigger instanceof Player)) {
            if (currentCooldown > 0) {
                currentCooldown -= dt;
            } else {
                currentCooldown = cooldown * 2;
                onTrigger.accept(this, dt, mousePos, target, trigger, scene);
                playSound();
            }
        }
        projectiles.forEach(projectile -> projectile.update(dt));
        projectiles.removeIf(projectile -> projectile.getPierce() <= 0);
    }

    public <T extends Living> void collide(ArrayList<T> entities) {
        projectiles.forEach(projectile -> projectile.collide(entities));
    }
    public void collide(Player player) {
        projectiles.forEach(projectile -> projectile.collide(player));
    }

    public Ability clone(Ability into) {
        Projectile[] pT = new Projectile[this.projectileTypes.length];
        for (int i = 0; i < this.projectileTypes.length; i++) {
            pT[i] = this.projectileTypes[i].clone();
        }
        into.setProjectileTypes(pT);

        into.setCooldown(cooldown);
        into.setOnTrigger(onTrigger);
        into.setScene(scene);
        into.setDescription(description);
        into.setLevel(level);
        into.setName(name);
        into.setUUID();
        into.setUpgrades(new ArrayList<>(upgrades));
        into.stats = new HashMap<>(stats);
        into.audioSource = new AudioSource();
        into.setSound(sound);
        into.setOnApplyToAble(onApplyToAble);

        return into;
    }


    public Ability clone() {
        Ability a = new Ability();
        return clone(a);
    }


    public float getCooldown() {
        return cooldown;
    }

    public float getCurrentCooldown() {
        return currentCooldown;
    }

    public Projectile[] getProjectileTypes() {
        return projectileTypes;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Test getScene() {
        return scene;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ArrayList<Upgrade> getUpgrades() {
        return upgrades;
    }

    public Upgrade getRndmUpgrade() {
        upgrades.clear();
        onUpgradeGen.accept(this);

        float[] probabilityDistribution = new float[upgrades.size()];
        float sum = 0;
        for (int i = 0; i < upgrades.size(); i++) {
            sum += upgrades.get(i).getRarity();
            probabilityDistribution[i] = sum;
        }
        float rand = (float) Math.random() * sum;
        int index = 0;
        for (int i = 0; i < probabilityDistribution.length; i++) {
            if (rand < probabilityDistribution[i]) {
                index = i;
                break;
            }
        }
        return upgrades.get(index);
    }
    public AudioSource getAudioSource() {
        return audioSource;
    }
    public AudioClip getSound() {
        return sound;
    }
    public void playSound() {
        if(sound != null)
            audioSource.playSound(sound);
    }

    public Ability setCooldown(float cooldown) {
        this.cooldown = cooldown;
        return this;
    }
    public void setOnTrigger(HexConsumer<Ability, Float, Vector2f, Vector2f, Entity2D, Test> onTrigger) {
        this.onTrigger = onTrigger;
    }
    public void setOnApplyToAble(BiConsumer<Ability, Living> onApplyToAble) {
        this.onApplyToAble = onApplyToAble;
    }
    public void setOnUpgradeGen(Consumer<Ability> onUpgradeGen) {
        this.onUpgradeGen = onUpgradeGen;
    }
    public void setProjectileTypes(Projectile[] projectileTypes) {
        this.projectileTypes = projectileTypes;
    }
    public Ability setScene(Test scene) {
        this.scene = scene;
        return this;
    }
    public String setDescription(String description) {
        return this.description = description;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Ability setCurrentCooldown(float currentCooldown) {
        this.currentCooldown = currentCooldown;
        return this;
    }
    public void setProjectiles(ArrayList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }
    public void setUUID() {
        this.uuid = UUID.randomUUID();
    }
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
    public void addUpgrade(Upgrade upgrade) {
        upgrades.add(upgrade);
    }
    public void addUpgrades(Upgrade... upgrades) {
        for (Upgrade upgrade : upgrades) {
            addUpgrade(upgrade);
        }
    }
    public void setUpgrades(Upgrade... upgrades) {
        this.upgrades.clear();
        this.addUpgrades(upgrades);
    }
    private void setUpgrades(ArrayList<Upgrade> ul) {
        this.upgrades = ul;
    }
    public void setSound(AudioClip sound) {
        this.sound = sound;
    }

    @FunctionalInterface
    public interface HexConsumer<T, U, V, W, X, Y> {
        void accept(T t, U u, V v, W w, X x, Y y);
    }

}
