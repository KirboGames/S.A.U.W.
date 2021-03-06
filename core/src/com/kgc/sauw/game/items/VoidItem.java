package com.kgc.sauw.game.items;

import com.kgc.sauw.core.item.Item;
import com.kgc.sauw.core.utils.ID;

public class VoidItem extends Item {
    public VoidItem() {
        super(ID.registeredId("item:void"));

        itemConfiguration.name = null;
        itemConfiguration.weight = 0f;
        itemConfiguration.type = -1;
        itemConfiguration.maxCount = -1;
        itemConfiguration.maxDamage = -1;
    }
}
