package com.kgc.sauw.core.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.intbyte.bdb.DataBuffer;
import com.intbyte.bdb.ExtraData;
import com.kgc.sauw.config.Settings;
import com.kgc.sauw.core.Container;
import com.kgc.sauw.core.item.Item;
import com.kgc.sauw.core.graphic.Animator;
import com.kgc.sauw.core.input.PlayerController;
import com.kgc.sauw.core.math.Maths;

import static com.kgc.sauw.game.environment.Environment.ITEMS;
import static com.kgc.sauw.core.graphic.Graphic.BATCH;
import static com.kgc.sauw.core.graphic.Graphic.TEXTURES;
import static com.kgc.sauw.gui.Interfaces.DEAD_INTERFACE;
import static com.kgc.sauw.gui.Interfaces.HUD;
import static com.kgc.sauw.core.map.World.WORLD;


public class Player extends Entity implements ExtraData {
    @Override
    public byte[] getBytes() {
        DataBuffer buffer = new DataBuffer();
        buffer.put("health", health);
        buffer.put("hunger", hunger);
        buffer.put("X", getPosition().x);
        buffer.put("Y", getPosition().y);
        buffer.put("InvLength", inventory.containers.size());
        for (int i = 0; i < inventory.containers.size(); i++) {
            buffer.put("Inv_" + i, inventory.containers.get(i));
        }
        return buffer.toBytes();
    }

    @Override
    public void readBytes(byte[] bytes, int begin, int end) {
        DataBuffer buffer = new DataBuffer();
        buffer.readBytes(bytes);
        setPosition(buffer.getFloat("X"), buffer.getFloat("Y"));
        health = buffer.getInt("health");
        hunger = buffer.getInt("hunger");
        inventory = new Inventory(buffer.getInt("InvLength"));
        for (int i = 0; i < buffer.getInt("InvLength"); i++) {
            inventory.containers.add(i, new Container());
            inventory.containers.get(i).readBytes(buffer.getByteArray("Inv_" + i), begin, end);
        }
    }

    @Override
    public Vector2 getBodySize() {
        return new Vector2(entityBodyW, entityBodyH / 4f);
    }

    public int carriedSlot = 0;
    public int[] hotbar = new int[8];

    public Item getCarriedItem() {
        return getItemFromHotbar(carriedSlot);
    }

    public Item getItemFromHotbar(int index) {
        if (hotbar[index] != -1 && hotbar[index] < inventory.containers.size() && ITEMS.getItemById(inventory.containers.get(hotbar[index]).id) != null) {
            return ITEMS.getItemById(inventory.containers.get(hotbar[index]).id);
        } else {
            hotbar[index] = -1;
            return ITEMS.getItemById(0);
        }
    }

    @Override
    public void onDead() {
        DEAD_INTERFACE.open();
    }

    public Player() {
        entityBodyW = 10 / 26f;
        entityBodyH = 1f;
        setSize(new Vector2(entityBodyW, entityBodyH));

        for (int i = 0; i < hotbar.length; i++) {
            this.hotbar[i] = -1;
        }

        animator = new Animator();
        animator.addAnimationRegion("animation_region:player", TEXTURES.player, 4, 3);
        animator.addAnimation("animation:player_walk_left", "animation_region:player", 0.2f, 0, 1);
        animator.addAnimation("animation:player_walk_right", "animation_region:player", 0.2f, 2, 3);
        animator.addAnimation("animation:player_walk_down", "animation_region:player", 0.2f, 4, 5);
        animator.addAnimation("animation:player_walk_up", "animation_region:player", 0.2f, 6, 7);

        currentFrame = animator.getFrames("animation_region:player")[4];
    }

    @Override
    public void render() {
        if (!isDead) {
            float AL = 0.75f - (Maths.module(720 - WORLD.WorldTime.getTime()) / (720 / 0.3f));
            BATCH.setColor(1f, 1f, 1f, AL);
            //BATCH.draw(TEXTURES.shadow, body.getPosition().x - entityBodyW / 2f, body.getPosition().y - entityBodyH / 2f, entityBodyW, entityBodyH);
            BATCH.setColor(1f, 1f, 1f, 1f);
            BATCH.draw(currentFrame, body.getPosition().x - entityBodyW / 2f, body.getPosition().y - entityBodyH / 8f, entityBodyW, entityBodyH);
        }
    }

    @Override
    public void animationTick() {
        int angle = Maths.angleBetweenVectors(0, 0, velocity.x, velocity.y);
        if (isEntityMoving()) {
            if (angle < 315 && angle > 225) {
                rotation = 0;
            } else if (angle < 225 && angle > 135) {
                rotation = 1;
            } else if (angle > 45 && angle < 135) {
                rotation = 2;
            } else if (angle < 45 || angle > 315) {
                rotation = 3;
            }
        }
        if (rotation == 0) {
            currentFrame = animator.getFrame("animation:player_walk_up");
        } else if (rotation == 1) {
            currentFrame = animator.getFrame("animation:player_walk_right");
        } else if (rotation == 2) {
            currentFrame = animator.getFrame("animation:player_walk_down");
        } else if (rotation == 3) {
            currentFrame = animator.getFrame("animation:player_walk_left");
        }

        if (!isEntityMoving()) {
            if (rotation == 1) {
                currentFrame = animator.getFrames("animation_region:player")[3];
            } else if (rotation == 3) {
                currentFrame = animator.getFrames("animation_region:player")[1];
            } else if (rotation == 0) {
                currentFrame = animator.getFrames("animation_region:player")[9];
            } else if (rotation == 2) {
                currentFrame = animator.getFrames("animation_region:player")[8];
            }
        }
    }

    @Override
    public void tick() {
        if (!isDead) {
            PlayerController.update();
            if (Settings.autopickup || (HUD.interactionButton.isTouched() || Gdx.input.isKeyPressed(Input.Keys.E))) {
                Entity entity = EntityManager.findEntity(this, 0.6f);
                if (entity != null) {
                    Drop item = (Drop) entity;
                    inventory.addItem((int) item.getExtraData("itemId"), (int) item.getExtraData("itemCount"));
                    EntityManager.delete(item);
                }
            }
        }
    }

    public static class PlayerFactory extends EntityFactory {
        public PlayerFactory() {
            super("player", 0);
        }

        @Override
        protected Entity createEntity() {
            return new Player();
        }
    }
}