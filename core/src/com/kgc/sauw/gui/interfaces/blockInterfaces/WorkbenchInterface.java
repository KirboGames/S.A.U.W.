package com.kgc.sauw.gui.interfaces.blockInterfaces;

import com.badlogic.gdx.Gdx;
import com.kgc.sauw.game.blocks.Workbench;
import com.kgc.sauw.core.gui.Interface;
import com.kgc.sauw.core.gui.elements.Image;
import com.kgc.sauw.core.map.Tile;

import static com.kgc.sauw.core.graphic.Graphic.TEXTURES;

public class WorkbenchInterface extends Interface {
    public final Image craftHandsawNotAvailableImage;
    public final Image craftHammerNotAvailableImage;

    public WorkbenchInterface() {
        super("WORKBENCH_TABLE_INTERFACE");

        createFromXml(Gdx.files.internal("xml/WorkbenchInterface.xml"));

        ((Image) getElement("CRAFT_HANDSAW_ARROW")).setImg(TEXTURES.icon_right);
        ((Image) getElement("CRAFT_HAMMER_ARROW")).setImg(TEXTURES.icon_right);
        ((Image) getElement("HANDSAW")).setImg(TEXTURES.handsaw);
        ((Image) getElement("HAMMER")).setImg(TEXTURES.hammer);
        ((Image) getElement("HAMMER")).setImg(TEXTURES.hammer);

        craftHandsawNotAvailableImage = (Image) getElement("CRAFT_HANDSAW_NOT_AVAILABLE_ICON");
        craftHammerNotAvailableImage = (Image) getElement("CRAFT_HAMMER_NOT_AVAILABLE_ICON");

        craftHandsawNotAvailableImage.setImg(TEXTURES.closeButton);
        craftHammerNotAvailableImage.setImg(TEXTURES.closeButton);
    }

    @Override
    public void tick(Tile tile) {
        craftHandsawNotAvailableImage.hide(Workbench.findToolWall(tile).getContainer("HandsawSlot").id == com.kgc.sauw.core.utils.ID.get("item:handsaw"));
        craftHammerNotAvailableImage.hide(Workbench.findToolWall(tile).getContainer("HammerSlot").id == com.kgc.sauw.core.utils.ID.get("item:hammer"));
    }
}