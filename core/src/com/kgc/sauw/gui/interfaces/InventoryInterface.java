package com.kgc.sauw.gui.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kgc.sauw.core.gui.Interface;
import com.kgc.sauw.core.gui.elements.Image;
import com.kgc.sauw.core.gui.elements.Slot;
import com.kgc.sauw.core.gui.elements.Text;

import java.text.DecimalFormat;

import static com.kgc.sauw.core.entity.EntityManager.PLAYER;
import static com.kgc.sauw.core.graphic.Graphic.TEXTURES;

public class InventoryInterface extends Interface {
    Slot[] hotbarslots = new Slot[8];

    DecimalFormat DF = new DecimalFormat("#.##");

    private TextureRegion[] playerAnimFrames;
    private TextureRegion currentFrame;

    private float stateTime = 0.0f;
    private float timer = 0.0f;

    Animation<TextureRegion> playerAnim;
    Animation<TextureRegion> tiredPlayerAnim;

    Image playerImg;
    Text playerWeight;

    public InventoryInterface() {
        super("INVENTORY_INTERFACE");
        createFromXml(Gdx.files.internal("xml/InventoryInterface.xml"));

        playerWeight = (Text) getElement("playerWeight");
        playerImg = (Image) getElement("playerImg");

        TextureRegion[][] tmp = TextureRegion.split(TEXTURES.player_inv, TEXTURES.player_inv.getWidth() / 3, TEXTURES.player_inv.getHeight());
        playerAnimFrames = new TextureRegion[3];
        int index = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 3; j++) {
                playerAnimFrames[index++] = tmp[i][j];
            }
        }

        playerAnim = new Animation<>(0.2f, playerAnimFrames[0], playerAnimFrames[1], playerAnimFrames[2], playerAnimFrames[1], playerAnimFrames[0]);
        tiredPlayerAnim = new Animation<>(0.2f, playerAnimFrames[1], playerAnimFrames[2], playerAnimFrames[2], playerAnimFrames[2], playerAnimFrames[1]);

        currentFrame = playerAnimFrames[0];

        for (int i = 0; i < 8; i++) {
            final int ii = i;
            final Slot s = (Slot) getElement("hotbarslot_" + i);
            s.setSF(new Slot.SlotFunctions() {
                @Override
                public void onClick() {
                    PLAYER.hotbar[ii] = -1;
                }

                @Override
                public boolean possibleToDrag() {
                    return true;
                }

                @Override
                public boolean isValid(int id, int count, int data, String FromSlotWithId) {
                    if (FromSlotWithId.contains("InventorySlot_")) {
                        for (int i = 0; i < PLAYER.hotbar.length; i++) {
                            if (PLAYER.hotbar[i] == currentTabInv * 30 + Integer.parseInt(FromSlotWithId.substring(14))) {
                                PLAYER.hotbar[i] = -1;
                            }
                        }
                        PLAYER.hotbar[ii] = currentTabInv * 30 + Integer.parseInt(FromSlotWithId.substring(14));
                    }

                    return false;
                }
            });
            hotbarslots[i] = s;
        }
        updateElementsList();
    }

    @Override
    public void tick() {
        for (int i = 0; i < hotbarslots.length; i++) {
            if (PLAYER.hotbar[i] != -1) {
                hotbarslots[i].id = PLAYER.getItemFromHotbar(i).id;
                hotbarslots[i].count = PLAYER.inventory.containers.get(PLAYER.hotbar[i]).count;
            }
        }
        timer += Gdx.graphics.getDeltaTime();
        if (timer >= 6) {
            stateTime += Gdx.graphics.getDeltaTime();
            if (PLAYER.itemsWeight < PLAYER.maxWeight) {
                currentFrame = playerAnim.getKeyFrame(stateTime, true);
                if (playerAnim.getKeyFrameIndex(stateTime) == 4) {
                    timer = 0;
                    stateTime = 0;
                    currentFrame = playerAnimFrames[0];
                }
            } else {
                currentFrame = tiredPlayerAnim.getKeyFrame(stateTime, true);
                if (tiredPlayerAnim.getKeyFrameIndex(stateTime) == 4) {
                    timer = 0;
                    stateTime = 0;
                    currentFrame = playerAnimFrames[1];
                }
            }

        }
        playerImg.setImg(currentFrame);
        playerWeight.setText(DF.format(PLAYER.itemsWeight) + " | " + DF.format(PLAYER.maxWeight) + " Kg");
    }
}