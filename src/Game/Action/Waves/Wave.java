package Game.Action.Waves;

public class Wave {
    private int waveNumber;
    private int enemies;
    private int enemiesLeft;
    private int enemiesSpawned;
    private int enemiesKilled;
    private float spawnRate; // enemies per second
    private float spawnTimer;
    private boolean finishedSpawning;

    public Wave(int waveNumber, int enemies, float spawnRate) {
        this.waveNumber = waveNumber;
        this.enemies = enemies;
        this.enemiesLeft = enemies;
        this.enemiesSpawned = 0;
        this.enemiesKilled = 0;
        this.spawnRate = spawnRate <= 0.01 ? 0.1f : spawnRate;
        this.spawnTimer = 0;
        this.finishedSpawning = false;
    }

    public void update(float dt) {
        if (enemiesLeft > 0) {
            spawnTimer += dt;
            if (spawnTimer >= spawnRate) {
                enemiesLeft    -= (int)(spawnTimer / spawnRate); // spawn multiple enemies if a lot of time elapsed since last update
                enemiesSpawned += (int)(spawnTimer / spawnRate); // spawn multiple enemies if a lot of time elapsed since last update
                spawnTimer = 0;
            }
            if(enemiesLeft < 0) {
                enemiesLeft = 0;
            }
        } else {
            finishedSpawning = true;
        }
    }

    public static Wave getEmptyWave() {
        return new Wave(-1, 1, Float.MAX_VALUE);
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    public int getEnemies() {
        return enemies;
    }

    public int getEnemiesLeft() {
        return enemiesLeft;
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public float getSpawnRate() {
        return spawnRate;
    }

    public float getSpawnTimer() {
        return spawnTimer;
    }

    public boolean isFinishedSpawning() {
        return finishedSpawning;
    }

    public void setFinishedSpawning(boolean b) {
        finishedSpawning = b;
    }
    public void addEnemiesKilled(int i) {
        enemiesKilled += i;
    }
}
