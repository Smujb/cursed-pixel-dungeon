package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.tiled.TiledMapLevel;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.noosa.Group;

public class SurfaceLevel extends TiledMapLevel {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
	}

	private static final String MAP_NAME = "maps/surface.tmx";

	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

	@Override
	public String loadImg() {
		return Assets.LOADING_SEWERS;
	}

	@Override
	protected String mapName() {
		return MAP_NAME;
	}

	@Override
	public Mob createMob() {
		return null;
	}

	public Actor respawner() {
		return null;
	}

	@Override
	public String tileName( Terrain tile ) {
		if (tile == Terrain.WATER) {
			return Messages.get(SewerLevel.class, "water_name");
		}
		return super.tileName(tile);
	}

	@Override
	public String tileDesc(Terrain tile) {
		switch (tile) {
			case EMPTY_DECO:
				return Messages.get(SewerLevel.class, "empty_deco_desc");
			case BOOKSHELF:
				return Messages.get(SewerLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		SewerLevel.addSewerVisuals(this, visuals);
		return visuals;
	}


	/*private static final Terrain W = Terrain.WALL;
	private static final Terrain D = Terrain.DOOR;
	private static final Terrain L = Terrain.LOCKED_DOOR;
	private static final Terrain e = Terrain.EMPTY;
	private static final Terrain A = Terrain.WATER;
	private static final Terrain m = Terrain.EMPTY_SP;
	private static final Terrain g = Terrain.GRASS;

	private static final Terrain B = Terrain.BOOKSHELF;

	private static final Terrain S = Terrain.STATUE;

	private static final Terrain E = Terrain.ENTRANCE;
	private static final Terrain X = Terrain.EXIT;

	private static final Terrain M = Terrain.WALL_DECO;
	private static final Terrain P = Terrain.PEDESTAL;

	private static final Terrain[] MAP_START =
			{
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, m, m, m, W, m, W, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, m, m, m, D, e, e, e, W, W,
					W, m, m, m, W, m, W, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, m, m, m, W, e, e, e, W, W,
					W, W, D, W, W, m, W, W, W, W, W, D, W, W, e, e, e, e, W, m, m, m, m, m, m, m, W, e, e, e, W, W,
					W, m, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, D, m, m, m, m, m, m, m, W, e, W, W, W, W,
					W, W, m, m, W, W, W, W, W, W, W, W, W, W, e, e, e, e, W, m, m, m, m, m, m, m, W, e, e, e, e, W,
					W, W, m, m, W, B, B, B, B, W, m, m, m, W, e, e, e, e, W, m, m, m, W, W, W, W, W, W, W, W, W, W,
					W, W, m, m, W, B, P, P, B, W, m, m, m, W, e, e, e, e, W, m, m, m, W, W, W, W, W, W, W, W, W, W,
					W, W, m, m, W, B, P, P, B, W, m, m, m, W, e, e, e, e, W, m, m, m, W, m, m, m, m, m, m, m, m, W,
					W, W, m, m, M, m, m, m, m, W, W, W, D, W, e, e, e, e, W, W, W, W, W, m, m, m, m, m, m, m, m, W,
					W, W, m, m, W, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, W, m, m, W, m, m, m, m, W, e, e, e, e, e, S, S, e, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, W, m, m, L, m, m, m, m, D, e, e, e, e, m, m, m, m, e, e, e, e, D, m, m, m, m, m, m, m, m, W,
					W, W, W, W, W, W, W, W, W, W, e, e, e, e, m, A, A, X, e, e, e, e, W, W, D, W, W, W, W, D, W, W,
					W, e, e, e, e, e, e, e, e, e, e, e, m, m, m, A, A, m, m, m, e, e, e, e, e, e, e, e, e, e, e, W,
					W, e, e, e, e, e, e, e, e, e, e, S, m, A, A, A, A, A, A, m, S, e, e, e, e, e, e, e, e, e, e, W,
					W, e, e, e, e, e, e, e, e, e, e, S, m, A, A, A, A, A, A, m, S, e, e, e, e, e, e, e, e, e, e, W,
					W, e, e, e, e, e, e, e, e, e, e, e, m, m, m, A, A, m, m, m, e, e, e, e, e, e, e, e, e, e, e, W,
					W, W, W, D, W, W, W, W, W, W, e, e, e, e, m, A, A, m, e, e, e, e, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, m, m, m, m, W, e, e, e, e, m, m, m, m, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, e, e, e, W, m, m, m, m, W, e, e, e, e, e, S, S, e, e, e, e, e, D, m, m, m, m, m, m, m, m, W,
					W, e, e, e, W, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, W, D, W, W, m, m, m, m, W, W, W, D, W, e, e, e, e, W, D, W, W, W, W, W, W, W, m, m, m, m, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, E, W, m, m, m, m, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, m, W, m, m, m, m, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, D, m, m, W, W, W, D, W, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, m, W, g, g, g, g, W,
					W, W, W, W, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, m, W, e, e, e, e, W,
					W, m, m, m, W, W, W, W, W, W, W, W, W, W, e, e, e, e, W, W, W, W, W, W, W, W, W, g, g, g, g, W,
					W, m, m, m, D, e, e, e, W, W, W, W, W, W, e, e, e, e, W, W, W, W, W, W, W, W, W, e, e, e, e, W,
					W, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, g, g, g, g, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W
			};*/

}