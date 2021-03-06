package com.kgc.sauw.game.blocks;

import com.kgc.sauw.core.Container;
import com.kgc.sauw.core.block.Block;
import com.kgc.sauw.core.environment.Crafting;
import com.kgc.sauw.core.gui.elements.Slot;
import com.kgc.sauw.core.map.Tile;
import com.kgc.sauw.core.utils.ID;

import java.util.ArrayList;

import static com.kgc.sauw.core.entity.EntityManager.PLAYER;
import static com.kgc.sauw.core.graphic.Graphic.TEXTURES;
import static com.kgc.sauw.core.map.World.MAPS;
import static com.kgc.sauw.game.environment.Environment.ITEMS;
import static com.kgc.sauw.gui.Interfaces.WORKBENCH_INTERFACE;

public class Workbench extends Block {
    private final ArrayList<Crafting.Craft> crafts;

    public Workbench() {
        super(ID.registeredId("block:workbench"), TEXTURES.table);

        blockConfiguration.setTransparent(true);
        blockConfiguration.setCollisionsRectangleByPixels(1, 2, 30, 7, 32);
        GUI = WORKBENCH_INTERFACE;

        crafts = new ArrayList<>();

        crafts.add(new Crafting.Craft(
                new int[]{ID.get("item:planks"), 2},
                new int[][]{{ID.get("item:handsaw"), 1}, {ID.get("item:log"), 1}}));
        crafts.add(new Crafting.Craft(
                new int[]{ID.get("item:iron_plate"), 1},
                new int[][]{{ID.get("item:hammer"), 1}, {ID.get("item:iron_ingot"), 1}}));
        ((Slot) GUI.getElement("CRAFT_HANDSAW_RESULT")).setSF(new Slot.SlotFunctions() {
            @Override
            public boolean isValid(int id, int count, int data, String FromSlotWithId) {
                return false;
            }

            @Override
            public void onClick() {
                craft(0);
            }

            @Override
            public boolean possibleToDrag() {
                return false;
            }
        });
        ((Slot) GUI.getElement("CRAFT_HAMMER_RESULT")).setSF(new Slot.SlotFunctions() {
            @Override
            public boolean isValid(int id, int count, int data, String FromSlotWithId) {
                return false;
            }

            @Override
            public void onClick() {
                craft(1);
            }

            @Override
            public boolean possibleToDrag() {
                return false;
            }
        });
    }

    @Override
    public void tick(Tile tile) {
        Tile toolWall = findToolWall(tile);
        tile.getContainer("CRAFT_HANDSAW_RESULT").setItem(0, 0, 0);
        tile.getContainer("CRAFT_HAMMER_RESULT").setItem(0, 0, 0);
        for (Crafting.Craft craft : crafts) {
            if (craft.ingredients[0][0] == ID.get("item:handsaw") && toolWall != null) {
                if (toolWall.getContainer("HandsawSlot").getId() == ID.get("item:handsaw") &&
                        tile.getContainer("CRAFT_HANDSAW_INGREDIENT").getId() == craft.ingredients[1][0]) {
                    tile.getContainer("CRAFT_HANDSAW_RESULT").setItem(craft.result[0], craft.result[1] * tile.getContainer("CRAFT_HANDSAW_INGREDIENT").getCount(), 0);
                }
            } else if (craft.ingredients[0][0] == ID.get("item:hammer") && toolWall != null) {
                if (toolWall.getContainer("HammerSlot").getId() == ID.get("item:hammer") &&
                        tile.getContainer("CRAFT_HAMMER_INGREDIENT").getId() == craft.ingredients[1][0]) {
                    tile.getContainer("CRAFT_HAMMER_RESULT").setItem(craft.result[0], craft.result[1] * tile.getContainer("CRAFT_HAMMER_INGREDIENT").getCount(), 0);
                }
            }
        }
    }

    private void craft(int craftId) {
        Tile tile = MAPS.getTile(GUI.currX, GUI.currY, GUI.currZ);
        if (craftId == 0) {
            Container toolWallContainer = findToolWall(tile).getContainer("HandsawSlot");
            Container craftContainer = tile.getContainer("CRAFT_HANDSAW_INGREDIENT");
            Crafting.Craft craft = getCraft(craftContainer.id);
            if (craft != null) {
                int resultCount = Math.min(craftContainer.count, ITEMS.getItemById(ID.get("item:handsaw")).getItemConfiguration().maxDamage - toolWallContainer.damage);
                for (int i = 0; i < resultCount; i++) {
                    PLAYER.inventory.addItem(tile.getContainer("CRAFT_HANDSAW_RESULT").id, craft.result[1]);
                    toolWallContainer.setItem(toolWallContainer.id, toolWallContainer.count, toolWallContainer.damage + 1);
                    craftContainer.setItem(craftContainer.id, craftContainer.count - 1, 0);
                }
            }
        } else if (craftId == 1) {
            Container toolWallContainer = findToolWall(tile).getContainer("HammerSlot");
            Container craftContainer = tile.getContainer("CRAFT_HAMMER_INGREDIENT");
            Crafting.Craft craft = getCraft(craftContainer.id);
            if (craft != null) {
                int resultCount = Math.min(craftContainer.count, ITEMS.getItemById(ID.get("item:hammer")).getItemConfiguration().maxDamage - toolWallContainer.damage);
                for (int i = 0; i < resultCount; i++) {
                    PLAYER.inventory.addItem(tile.getContainer("CRAFT_HAMMER_RESULT").id, craft.result[1]);
                    toolWallContainer.setItem(toolWallContainer.id, toolWallContainer.count, toolWallContainer.damage + 1);
                    craftContainer.setItem(craftContainer.id, craftContainer.count - 1, 0);
                }
            }
        }
    }

    private Crafting.Craft getCraft(int id) {
        for (Crafting.Craft craft : crafts) {
            if (craft.ingredients[1][0] == id) return craft;
        }
        return null;
    }

    public static Tile findToolWall(Tile tile) {
        Tile toolWall = null;
        if (MAPS.getTile(tile.x - 1, tile.y, tile.z).id == ID.get("block:tool_wall")) {
            toolWall = MAPS.getTile(tile.x - 1, tile.y, tile.z);
        }
        if (MAPS.getTile(tile.x + 1, tile.y, tile.z).id == ID.get("block:tool_wall")) {
            toolWall = MAPS.getTile(tile.x + 1, tile.y, tile.z);
        }
        return toolWall;
    }
}