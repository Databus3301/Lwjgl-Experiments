package Game.Action.Waves;

import Game.Entities.Enemies;
import Game.Entities.Enemy;
import Render.Entity.Entity2D;
import Render.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;

public class EnemySpawner {
    private Wave currentWave;
    private float[] probabilityDistribution = new float[Enemies.enemies.size()]; // probability distribution
    private Result lastResult = Result.NOTHING;

    private Entity2D tracker = new Entity2D();

    public EnemySpawner(Wave currentWave) {
        this.currentWave = currentWave;
    }
    public EnemySpawner() {
        this(Wave.getEmptyWave());
    }

    public Enemy[] update(float dt) {
        int enemiesLeft = currentWave.getEnemiesLeft();
        currentWave.update(dt);

        Enemy[] spawned = null;
        if (currentWave.getEnemiesLeft() < enemiesLeft) {
            spawned = new Enemy[enemiesLeft - currentWave.getEnemiesLeft()];
            for(int i = 0; i < spawned.length; i++) {
                spawned[i] = spawn();
            }
        }

        return spawned;
    }
    public Result update(float dt, ArrayList<Enemy> enemyCollection, int floor) {
        int enemiesLeft = currentWave.getEnemiesLeft();
        currentWave.update(dt);

        if (currentWave.getEnemiesLeft() < enemiesLeft) {
            for(int i = 0; i < enemiesLeft - currentWave.getEnemiesLeft(); i++) {
                Enemy e = spawn();
                // scale to floor
                e.setMaxLP((int) (e.getMaxLP() * (1 + 0.5f * floor-1)));
                e.setLP(e.getMaxLP());
                e.setSpeed(e.getSpeed() * (1 + 0.5f * floor-1));

                enemyCollection.add(e);
            }
            return lastResult = Result.SPAWNED;
        }

        if (currentWave.isFinishedSpawning()) {
            if(enemyCollection.isEmpty()) // if there are no enemies left alive
                return lastResult = Result.WAVE_OVER;
            return lastResult = Result.FINISHED_SPAWNING;
        }

        return lastResult = Result.NOTHING;
    }

    public Enemy spawn() { // CDF function -> https://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
        float rand = (float) Math.random();
        float sum = 0;
        int index = 0;
        for (int i = 0; i < probabilityDistribution.length; i++) {
            sum += probabilityDistribution[i];
            if (rand < sum) {
                index = i;
                break;
            }
        }
        index = Math.min(index, Enemies.enemies.size() - 1); // prevent out of bounds
        Enemy enemy = Enemies.enemies.get(index).clone();
        // gen pos
        Vector2f pos = new Vector2f((float) (Math.random() * Window.dim.x - Window.dim.x / 2f +  tracker.getPosition().x), (float) (Math.random() * Window.dim.y - Window.dim.y / 2f + tracker.getPosition().y));
        // check if pos is colliding with tracker/player
        while(tracker.collideRect(new Vector4f(pos.x-enemy.getScale().x*2, pos.y-enemy.getScale().y*2, enemy.getScale().x*4, enemy.getScale().y*4))) {
            // if so, regenerate pos
            pos.set((float) Math.random() * Window.dim.x - Window.dim.x / 2f + tracker.getPosition().x, (float) Math.random() * Window.dim.y - Window.dim.y / 2f + tracker.getPosition().y);
        }
        enemy.setPosition(pos);
        return enemy;
    }

    public void setCurrentWave(Wave currentWave) {
        this.currentWave = currentWave;
    }
    public void setProbabilityDistribution(float[] probabilityDistribution) {
        // normalize P's to 0-1 for the CDF
        float sum = 0;
        for (float p : probabilityDistribution) {
            sum += p;
        }
        for (int i = 0; i < probabilityDistribution.length; i++) {
            probabilityDistribution[i] /= sum;
        }
        // set P's
        this.probabilityDistribution = probabilityDistribution;
    }

    public void setTracker(Entity2D tracker) {
        this.tracker = tracker;
    }

    public Wave getCurrentWave() {
        return currentWave;
    }
    public float[] getProbabilityDistribution() {
        return probabilityDistribution;
    }

    public Entity2D getTracker() {
        return tracker;
    }

    public Result getLastResult() {
        return lastResult;
    }

    /**
     * Only use if you know what you are doing! <br>
     * It's the spawners job to set results via < update >
     */
    public void setLastResult(Result r) {
        this.lastResult = r;
    }

    public enum Result {
        SPAWNED, NOTHING, WAVE_OVER, FINISHED_SPAWNING
    }
}
