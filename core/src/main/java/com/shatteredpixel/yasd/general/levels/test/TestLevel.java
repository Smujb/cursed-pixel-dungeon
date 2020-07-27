package com.shatteredpixel.yasd.general.levels.test;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;
import com.watabou.utils.Random;

public class TestLevel extends RegularLevel {

	@Override
	protected Painter painter() {
		return new SewerPainter();
	}

	@Override
	public String tilesTex() {
		return Assets.Environment.TILES_CAVES;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_CAVES;
	}

	@Override
	public String loadImg() {
		return Assets.Interfaces.LOADING_CAVES;
	}

	@Override
	protected Class<?>[] connectionRoomClasses() {
		return new Class<?>[] {LockedDoorHallway.BronzeLockedDoorHallway.class, LockedDoorHallway.SilverLockedDoorHallway.class};
	}

	@Override
	protected float[] connectionRoomChances() {
		return new float[] {1f, 1f};
	}

	@Override
	protected int standardRooms() {
		//5 to 7, average 5.57
		return 5+ Random.chances(new float[]{4, 2, 1});
	}

	@Override
	protected int specialRooms() {
		//1 to 3, average 1.67
		return 1+Random.chances(new float[]{4, 4, 2});
	}
}
