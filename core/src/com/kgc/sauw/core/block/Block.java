package com.kgc.sauw.core.block;

import com.badlogic.gdx.graphics.Texture;
import com.kgc.sauw.core.gui.Interface;
import com.kgc.sauw.core.map.Tile;

import static com.kgc.sauw.game.environment.Environment.BLOCKS;
import static com.kgc.sauw.core.graphic.Graphic.BATCH;

public class Block {
    public int id;
    public Texture t0;
    public Interface GUI = null;
    protected BlockConfiguration blockConfiguration;

    public Block(int id, Texture t0) {
        blockConfiguration = new BlockConfiguration();
        this.id = id;
        this.t0 = t0;
    }

    public BlockConfiguration getBlockConfiguration() {
        return blockConfiguration;
    }


    public void onPlace(Tile tile) {
    }

    public void tick(Tile tile) {
    }

    public void tick() {
    }

    public void renderTick(Tile tile) {
        float w = BLOCKS.getBlockById(tile.id).getBlockConfiguration().getSize().x;
        float h = BLOCKS.getBlockById(tile.id).getBlockConfiguration().getSize().y;
        BATCH.draw(tile.t, tile.x, tile.y, w, h);
    }

    public void randomTick(Tile tile) {
    }

    public void onInteractionButtonPressed(Tile tile) {
    }

    public void click(Tile tile) {
    }
}
