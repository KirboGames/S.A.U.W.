package com.kgc.sauw.environment.blocks;

import com.kgc.sauw.config.BlockConfiguration;
import com.kgc.sauw.utils.ID;

import static com.kgc.sauw.graphic.Graphic.TEXTURES;

public class StoneLump extends Block {
    public StoneLump() {
        super(ID.registeredId("block:stone_lump", 9), TEXTURES.stone_1);

        BlockConfiguration.setMaxDamage(5);
        BlockConfiguration.setTransparent(true);
        BlockConfiguration.setDrop(new int[][]{{12, 5}});
        BlockConfiguration.setInstrumentType(1);
        BlockConfiguration.setCollisionsRectangleByPixels(0, 0, 32, 8, 32);
    }
}
