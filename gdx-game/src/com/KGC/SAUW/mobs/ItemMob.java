package com.KGC.SAUW.mobs;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.KGC.SAUW.Items;
import com.badlogic.gdx.Gdx;

public class ItemMob extends Mob {
	public int itemID;
	public int itemCount;
	public int itemData;
	public Items items;
	public ItemMob(int x, int y, int iI, int iC, int iD, Items items){
		itemID = iI;
		itemCount = iC;
		itemData = iD;
		posX = x;
		posY = y;
		this.items = items;
		plW = Gdx.graphics.getWidth() / 32;
		plH = Gdx.graphics.getWidth() / 32;
		collisions = false;
	}
	@Override
	public void update() {
		super.update();
	}

	@Override
	public void render(SpriteBatch b) {
		super.render(b);
		b.draw(items.getTextureById(itemID), posX, posY, plW, plH);
	}
}