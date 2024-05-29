package Game;

import Game.Action.Abilities;
import Game.Action.Ability;
import Game.Action.Upgrade;
import Game.Action.Waves.EnemySpawner;
import Game.Action.Waves.Wave;
import Game.Entities.Dungeon.Door;
import Game.Entities.Player;
import Render.Entity.Interactable.Button;
import Render.Entity.Interactable.Interactable;
import Render.Window;
import Tests.Test;
import Tests.TestGame;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static Game.Action.Waves.EnemySpawner.Result.NOTHING;
import static Render.Entity.Interactable.Interactable.States.HOVER;

public class UI {
    private static Button[] upgradeButtons;

    public static void onLvlUp(Player player, TestGame scene, int bcnt) {
        Vector2f diff = Window.getDifferP();
        float bw = (float) Window.dim.x / bcnt / diff.x / diff.x;
        float bh = Window.dim.y / (bcnt+1f)    / diff.y / diff.y;



        if(upgradeButtons == null) {
            upgradeButtons = new Button[bcnt];
            scene.setShouldSimulate(false);

            for (int i = 0; i < bcnt; i++) {
                upgradeButtons[i] = new Button(scene, new Vector2f(0, bh * i - bh * (bcnt/2f - 0.5f) + 5*i - 5*(bcnt/2f)));
                upgradeButtons[i].scale(bw/2f, bh/2f);
                upgradeButtons[i].setColor(0.6f, 0.6f, 0.6f, 0.25f);
                upgradeButtons[i].setHitTime(1500);

                // rndm ability that has upgrades
                if(player.getAbilities().isEmpty())
                    return;

                int l = player.getAbilities().size();
                Ability rA = player.getAbilities().get((int) (Math.random() * l));
                rA.onUpgradeGen.accept(rA);
                while(rA.getUpgrades().isEmpty())
                    rA = player.getAbilities().get((int) (Math.random() * l));

                Upgrade rU = rA.getRndmUpgrade();
                String d = rU.genDescription(rA);
                upgradeButtons[i].setLabel(rA.getName() + "\n" + rU.getName() + ": " + d);
                //upgradeButtons[i].setTooltip(rA.getName() + "\n" + rU.getName() + ": " + d);

                Ability finalRA = rA;
                upgradeButtons[i].setReleasedCallback((button) -> {
                    rU.applyTo(finalRA);

                    player.setJustLeveledUp(false);
                    scene.setShouldSimulate(true);

                    for(Button ub : upgradeButtons) {
                        ub.setReleasedCallback((interactable) -> {});
                    }

                    upgradeButtons = null;
                });
            }
        }
        if (upgradeButtons == null)
            return;

        Vector2f cp = Test.renderer.getCamera().getPosition();
        for (int i = 0; i < upgradeButtons.length; i++) {
            if (abilityButtons != null) // prevent buttons from stacking on top of each other if active simultaneously
                pos.set(100000, 1000000);
            else
                pos.set(-cp.x, bh * i - bh * (bcnt / 2f - 0.5f) + 5 * i - 5 * (bcnt / 2f) + -cp.y);
            upgradeButtons[i].setPosition(pos);
        }

    }

    private static Button[] abilityButtons;
    public static void onRoomCompletion(Player player, TestGame scene, int bcnt) {
        Vector2f diff = Window.getDifferP();
        float bw = (float) Window.dim.x / bcnt / diff.x / diff.x;
        float bh = Window.dim.y / (bcnt+1f)    / diff.y / diff.y;

        if(abilityButtons == null) {
            abilityButtons = new Button[bcnt];
            for (int i = 0; i < bcnt; i++) {
                abilityButtons[i] = new Button(scene, new Vector2f(0, bh * i - bh * (bcnt/2f - 0.5f) + 5*i - 5*(bcnt/2f)));
                abilityButtons[i].scale(bw/2f, bh/2f);
                abilityButtons[i].setColor(0.6f, 0.6f, 0.6f, 0.25f);
                abilityButtons[i].setHitTime(100);

                int l = Abilities.abilities.length;
                Ability rA = Abilities.abilities[(int) (Math.random() * l)];

                abilityButtons[i].setLabel(rA.getName());
                abilityButtons[i].setTooltip(rA.getDescription());

                abilityButtons[i].setReleasedCallback((button) -> {
                    player.addAbility(rA);

                    EnemySpawner sp = scene.getSpawner();
                    sp.setCurrentWave(Wave.getEmptyWave());
                    sp.setLastResult(NOTHING);
                    for(Door d : scene.getRoom().getDoors()) {
                        d.open();
                    }

                    for(Button ab : abilityButtons) {
                        ab.setReleasedCallback((interactable) -> {});
                    }

                    abilityButtons = null;
                });
            }
        }
        if(abilityButtons == null)
            return;
        for (int i = 0; i < abilityButtons.length; i++) {
            Vector2f cp = Test.renderer.getCamera().getPosition();
            pos.set(-cp.x, bh * i - bh * (bcnt/2f - 0.5f) + 5*i - 5*(bcnt/2f) + -cp.y);
            abilityButtons[i].setPosition(pos);
        }
    }

    public static Button[] getAbilityButtons() {
        return abilityButtons;
    }


    private static final Vector2f pos = new Vector2f(0, 0);
    private static final Vector2f scale = new Vector2f(0, 0);
    public static void draw() {
        if(upgradeButtons != null) {
            for (int i = 0; i < upgradeButtons.length; i++) {
                Test.renderer.draw(upgradeButtons[i]);

                Vector4f color;
                if (upgradeButtons[i].getState() == HOVER)
                    color = new Vector4f(.1f, .7f, .1f, 1);
                else if (upgradeButtons[i].getState() == Interactable.States.DRAGGED)
                    color = new Vector4f(.5f, 1f, .5f, 1);
                else
                    color = new Vector4f(.6f, .6f, .6f, 1);

                Test.renderer.drawRect(upgradeButtons[i].getPosition().sub(upgradeButtons[i].getScale(), pos), upgradeButtons[i].getScale().mul(2, scale), color);

                upgradeButtons[i].onUpdate(-1, Test.mousePos);
                if(upgradeButtons == null)
                    break;
            }
        }
        if(abilityButtons != null) {
            for (int i = 0; i < abilityButtons.length; i++) {
                Test.renderer.draw(abilityButtons[i]);

                Vector4f color;
                if (abilityButtons[i].getState() == HOVER)
                    color = new Vector4f(.1f, .7f, .1f, 1);
                else if (abilityButtons[i].getState() == Interactable.States.DRAGGED)
                    color = new Vector4f(.5f, 1f, .5f, 1);
                else
                    color = new Vector4f(.6f, .6f, .6f, 1);

                Test.renderer.drawRect(abilityButtons[i].getPosition().sub(abilityButtons[i].getScale(), pos), abilityButtons[i].getScale().mul(2, scale), color);

                abilityButtons[i].onUpdate(-1, Test.mousePos);
                if(abilityButtons == null)
                    break;
            }
        }
    }

}
