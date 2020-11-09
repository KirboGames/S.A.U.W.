package com.KGC.SAUW;
import android.os.Environment;
import box2dLight.RayHandler;
import com.KGC.SAUW.Camera2D;
import com.KGC.SAUW.Textures;
import com.KGC.SAUW.Items;
import com.KGC.SAUW.Maps;
import com.KGC.SAUW.mobs.Mobs;
import com.KGC.SAUW.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.intbyte.bdb.DataBuffer;
import java.io.File;
import java.util.Random;

public class World {
	private int WIDTH = Gdx.graphics.getWidth();
	private int HEIGHT = Gdx.graphics.getHeight();
	//private player pl;
	private Items items;
	private Camera2D cam;
	private SpriteBatch b;
	public Maps maps;
	private Blocks blocks;
	private Textures Textures;
	private GameInterface GI;
	private boolean interfaceTouched;
    private boolean isTouched;
    private Random r = new Random();
	private boolean worldTouched;
	public Time WorldTime;

	public World world;
	public RayHandler rh;
	public Player pl;
	public Mobs mobs;

    String WorldName = null;
	public void save(String WorldName) {
		File worldFolder = new File(Environment.getExternalStorageDirectory().toString() + "/S.A.U.W./Worlds/" + WorldName);
		
		File map = new File(worldFolder.toString() + "/map");
		File mapFile = new File(map.toString() + "/map.bdb");
		File player = new File(worldFolder.toString() + "/player.bdb");
		
		if (!worldFolder.exists()) worldFolder.mkdir();
		if (!map.exists()) map.mkdir();
		try {
			if (!mapFile.exists()) mapFile.createNewFile();
			if (!player.exists()) player.createNewFile();
			FileHandle mapFile1 = Gdx.files.external("S.A.U.W./Worlds/" + WorldName + "/map/map.bdb");
			FileHandle worldData = Gdx.files.external("S.A.U.W./Worlds/" + WorldName + "/world.bdb");
			FileHandle playerFile = Gdx.files.external("S.A.U.W./Worlds/" + WorldName + "/player.bdb");

			mapFile1.writeBytes(maps.toDataBuffer().toBytes(), false);
			DataBuffer buffer = new DataBuffer();
			buffer.put("player", pl);
			playerFile.writeBytes(buffer.toBytes(), false);
			DataBuffer buffer1 = new DataBuffer();
			buffer1.put("time", WorldTime.getTime());
			worldData.writeBytes(buffer1.toBytes(), false);
		} catch (Exception e) {
			Gdx.app.log("saveError", e.toString());
		}
	}
	public void save() {
		if (WorldName != null) {
			save(WorldName);
		}
	}
	public void createNewWorld() {
		maps.generateWorld(this);
	}
	public void load(String WorldName) {
		this.WorldName = WorldName;
		try {
			pl.data.put("lastWorld", WorldName);
			pl.saveData();
		} catch (Exception e) {
			
		}
		File worldFolder = new File(Environment.getExternalStorageDirectory().toString() + "/S.A.U.W./Worlds/" + WorldName);
		if (worldFolder.exists()) {
			File mapFile = new File(worldFolder + "/map/map.bdb");
			if (mapFile.exists()) {
				FileHandle mapFile1 = Gdx.files.external("S.A.U.W./Worlds/" + WorldName + "/map/map.bdb");
				FileHandle worldData = Gdx.files.external("S.A.U.W./Worlds/" + WorldName + "/world.bdb");
				FileHandle playerFile = Gdx.files.external("S.A.U.W./Worlds/" + WorldName + "/player.bdb");
				DataBuffer buffer = new DataBuffer();
				buffer.readBytes(mapFile1.readBytes());
				int[] map = buffer.getIntArray("mapIds");
				int[] mapDmg = buffer.getIntArray("mapDamage");
				int i = 0;
				for (int y = 0; y < maps.map0.length; y++) {
					for (int x = 0; x < maps.map0[y].length; x++) {
						for (int z = 0; z < maps.map0[y][x].length; z++) {
							setBlock(x, y,  z, map[i]);
							maps.map0[y][x][z].damage = mapDmg[i];
							i++;
						}
					}
				}
				buffer.readBytes(playerFile.readBytes());
				pl.readBytes(buffer.getByteArray("player"));
				buffer.readBytes(worldData.readBytes());
				WorldTime.setTime(buffer.getInt("time"));
			}
		}
	}
	public World(SpriteBatch b, Textures t, Items i, Camera2D cam, Blocks blocks, GameInterface GI, Settings s) {
		this.Textures = t;
		this.b = b;
		//this.pl = pl;
		this.items = i;
		this.cam = cam;
		this.maps = new Maps();
		this.blocks = blocks;
		this.GI = GI;
		this.WorldTime = new Time();
		world = new World(new Vector2(0, 0), false);
		mobs = new Mobs(b, maps, Textures);
		pl = new Player(i, Textures, GI, mobs, maps, s);
	}
	public World(SpriteBatch b, Textures t, Items i, Camera2D cam, Blocks blocks) {
		this.Textures = t;
		this.b = b;
		this.items = i;
		this.cam = cam;
		this.maps = new Maps();
		this.blocks = blocks;
		world = new World(new Vector2(0, 0), false);
	}
	public Body createBox(float posX, float posY, float boxW, float boxH, BodyDef.BodyType type) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = type;
		bodyDef.position.set(posX + boxW / 2.f, posY + boxH / 2.f);

		Body body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxW / 2.f, boxH / 2.f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;

		Fixture fixture = body.createFixture(fixtureDef);
		shape.dispose();

		return body;
	}
	public boolean setBlock(int x, int y, int z, Block block) {
		if (x >= 0 && x < maps.map0[0].length + 1 && y >= 0 && y < maps.map0.length + 1) {
			Tile tile = new Tile(x, y, z, block);
			if (maps.map0[y][x][z] != null && maps.map0[y][x][z].body != null)  world.destroyBody(maps.map0[y][x][z].body);
			maps.map0[y][x][z] = tile;
			if (block.id != 4 && z == 0) tile.setBody(createBox(tile.block.x, tile.block.y, tile.block.width, tile.block.height, BodyDef.BodyType.StaticBody));
			if (z == 0) tile.setLight(rh, block);
			return true;
		}
		return false;
	}
	public boolean setBlock(int x, int y, int id) {
		int z;
		if (maps.map0[y][x][1].id == 4) {
			z = 1;
		} else if (maps.map0[y][x][0].id == 4) {
			z = 0;
		} else {
			return false;
		}
		return setBlock(x, y, z, blocks.getBlockById(id));
	}
	public boolean setBlock(int x, int y, int z, int id) {
		return setBlock(x, y, z, blocks.getBlockById(id));
	}
	public int getHighestBlock(int x, int y) {
		for (int z = 0; z < maps.map0[y][x].length; z++) {
			if (maps.map0[y][x][z].id != 4) {
				return z;
			}
		}
		return -1;
	}
	public void update(Mods mods, Achievements a) {
		world.step(1 / 60f, 6, 2);
		pl.update(this, a);
		mobs.update();
		maps.update(cam, GI, pl, this, blocks, mobs, items);
		if (Gdx.input.isTouched()) {
			if (!isTouched) {
				if (GI.isTouched())
					interfaceTouched = true;
				else worldTouched = true;

				isTouched = true;
			}
		}
		if (!Gdx.input.isTouched() && interfaceTouched)
			interfaceTouched = false;
		if (!Gdx.input.isTouched() && worldTouched) {
			worldTouched = false;
			if (!interfaceTouched && !GI.isInterfaceOpen) {
				double sc = (double)cam.W / WIDTH;
				int cX = (int)(Gdx.input.getX() * sc + cam.X);
				int cY = (int)(cam.H - Gdx.input.getY() * sc + cam.Y);
				int bX = (cX - (cX % (WIDTH / 16))) / (WIDTH / 16);
				int bY = (cY - (cY % (WIDTH / 16))) / (WIDTH / 16);
				mods.HookFunction("itemClick", new Object[]{bX, bY, (maps.map0[bY][bX][1].id != 4) ? 1 : 0, maps.map0[bY][bX][(maps.map0[bY][bX][1].id != 4) ? 1 : 0].id, pl.getCarriedItem()});
				if (Maths.distanceD(pl.posX, pl.posY, bX * WIDTH / 16, bY * WIDTH / 16) <= 1.7 * WIDTH / 16) {
					if (items.getTypeById(pl.getCarriedItem()) == 1) {
						if (setBlock(bX, bY, items.getBlockId(pl.getCarriedItem()))) {
							pl.Inventory[pl.carriedSlot].count -= 1;
						}
					} else if (items.getTypeById(pl.getCarriedItem()) == 2 || pl.getCarriedItem() == 0) {
						if (getHighestBlock(bX, bY) != 2 && getHighestBlock(bX, bY) != -1) {
							int z = -1;
							if (maps.map0[bY][bX][0].id != 4) {
								z = 0;
							} else if (maps.map0[bY][bX][1].id != 4) {
								z = 1;
							} 
							if (z != -1) {
								int instrType;
								if (pl.getCarriedItem() == 0) {
									instrType = 3;
								} else {
									instrType = items.getItemById(pl.getCarriedItem()).intrumentType;
								}

								pl.Inventory[pl.carriedSlot].data = pl.Inventory[pl.carriedSlot].data + maps.map0[bY][bX][z].hit(instrType);
							}
						}
					}
				}
			}
		}
		if (Gdx.input.isTouched()) {
			isTouched = false;
		}
		int yy = r.nextInt(maps.map0.length - 1) + 1;
		int xx = r.nextInt(maps.map0[0].length - 1) + 1;
		if (maps.map0[yy][xx][0].TileEntity != null) {
			maps.map0[yy][xx][0].TileEntity.randomTick(maps.map0[yy][xx][0]);
		}
	}
	public void renderLowLayer() {
		render(false);
	}
	public void renderHighLayer() {
		render(true);
	}
	public void render(boolean isHighestLayer) {
		if (pl != null && mobs != null && isHighestLayer) {
			mobs.render(cam);
			pl.render(b, Textures);
		}
		for (int y = maps.map0.length - 1; y >= 0; y--) {
			for (int x = 0; x < maps.map0[y].length; x++) {
				int z = getHighestBlock(x, y);
				if (z != -1)
					if (Maths.rectCrossing(cam.X, cam.Y, cam.W, cam.H, x * (WIDTH / 16), y * (WIDTH / 16), blocks.getBlockById(maps.map0[y][x][z].id).getSize().x * WIDTH / 16, blocks.getBlockById(maps.map0[y][x][z].id).getSize().y * WIDTH / 16)) {
						if ((!isHighestLayer) || (isHighestLayer && z == 0)) {
							if (z == 2 && ((GI != null) ? !GI.isInterfaceOpen : false)) {
								b.setColor(0.7f, 0.7f, 0.7f, 1);
							}
							if (!isHighestLayer && z == 0 && blocks.getBlockById(maps.map0[y][x][z].id).getTranspanent()) {
								z = z + 1;
							}
							if ((maps.map0[y][x][z].TileEntity == null || (maps.map0[y][x][z].TileEntity != null && maps.map0[y][x][z].TileEntity.renderIf(maps.map0[y][x][z])))) {
								int w = blocks.getBlockById(maps.map0[y][x][z].id).getSize().x * WIDTH / 16;
								int h = blocks.getBlockById(maps.map0[y][x][z].id).getSize().y * WIDTH / 16;
								if (maps.map0[y][x][z].type == 0) {
									b.draw(maps.map0[y][x][z].t, x * (WIDTH / 16) , y * (WIDTH / 16) , w, h);
								} else if (maps.map0[y][x][z].type == 1) {
									b.draw(getConnectingTexture(blocks.getBlockById(maps.map0[y][x][z].id), maps.map0[y + 1][x][z].id, maps.map0[y][x + 1][z].id, maps.map0[y - 1][x][z].id, maps.map0[y][x - 1][z].id), x * (WIDTH / 16) , y * (WIDTH / 16) , w, h);
								}
							}
							if (z == 2 && ((GI != null) ? !GI.isInterfaceOpen : false)) {
								b.setColor(1, 1, 1, 1);
							}
						}
					}
			}
		}
	}
	public Texture getConnectingTexture(Block bl, int uID, int rID, int dID, int lID) {
		if (bl.id == uID && bl.id == dID && bl.id != lID && bl.id != rID) {
			return bl.t0;
		} else if (bl.id != uID && bl.id != dID && bl.id == lID && bl.id == rID) {
			return bl.t1;
		} else if (bl.id != uID && bl.id == dID && bl.id != lID && bl.id == rID) {
			return bl.t2;
		} else if (bl.id != uID && bl.id == dID && bl.id == lID && bl.id != rID) {
			return bl.t3;
		} else if (bl.id == uID && bl.id != dID && bl.id == lID && bl.id != rID) {
			return bl.t4;
		} else if (bl.id == uID && bl.id != dID && bl.id != lID && bl.id == rID) {
			return bl.t5;
		} else if (bl.id == uID && bl.id == dID && bl.id != lID && bl.id == rID) {
			return bl.t6;
		} else if (bl.id != uID && bl.id == dID && bl.id == lID && bl.id == rID) {
			return bl.t7;
		} else if (bl.id == uID && bl.id == dID && bl.id == lID && bl.id != rID) {
			return bl.t8;
		} else if (bl.id == uID && bl.id != dID && bl.id == lID && bl.id == rID) {
			return bl.t9;
		} else if (bl.id == uID && bl.id == dID && bl.id == lID && bl.id == rID) {
			return bl.t10;
		} else if (bl.id != uID && bl.id == dID && bl.id != lID && bl.id != rID) {
			return bl.t11;
		} else if (bl.id != uID && bl.id != dID && bl.id == lID && bl.id != rID) {
			return bl.t12;
		} else if (bl.id == uID && bl.id != dID && bl.id != lID && bl.id != rID) {
			return bl.t13;
		} else if (bl.id != uID && bl.id != dID && bl.id != lID && bl.id == rID) {
			return bl.t14;
		} else if (bl.id != uID && bl.id != dID && bl.id != lID && bl.id != rID) {
			return bl.t15;
		}
		return Textures.undf;
	}
}