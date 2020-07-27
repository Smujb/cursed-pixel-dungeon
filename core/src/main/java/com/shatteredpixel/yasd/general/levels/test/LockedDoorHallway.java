package com.shatteredpixel.yasd.general.levels.test;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.keys.BronzeKey;
import com.shatteredpixel.yasd.general.items.keys.IronKey;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class LockedDoorHallway extends MetroidVaniaConnectionRoom {
	private Door.Type type;

	public LockedDoorHallway(Door.Type type) {
		this.type = type;
	}

	public void paint(Level level) {
		Rect c = getConnectionSpace();

		for (Door door : connected.values()) {

			Point start;
			Point mid;
			Point end;

			start = new Point(door);
			if (start.x == left)        start.x++;
			else if (start.y == top)    start.y++;
			else if (start.x == right)  start.x--;
			else if (start.y == bottom) start.y--;

			int rightShift;
			int downShift;

			if (start.x < c.left)           rightShift = c.left - start.x;
			else if (start.x > c.right)     rightShift = c.right - start.x;
			else                            rightShift = 0;

			if (start.y < c.top)            downShift = c.top - start.y;
			else if (start.y > c.bottom)    downShift = c.bottom - start.y;
			else                            downShift = 0;

			//always goes inward first
			if (door.x == left || door.x == right){
				mid = new Point(start.x + rightShift, start.y);
				end = new Point(mid.x, mid.y + downShift);

			} else {
				mid = new Point(start.x, start.y + downShift);
				end = new Point(mid.x + rightShift, mid.y);

			}

			Painter.drawLine( level, start, mid, level.tunnelTile() );
			Painter.drawLine( level, mid, end, level.tunnelTile() );
		}

		int doorNum = 0;
		for (Door door : connected.values()) {
			door.set(doorNum == 0 ? type : Door.Type.TUNNEL);
			doorNum++;
		}
	}

	public static class SilverLockedDoorHallway extends LockedDoorHallway {
		public SilverLockedDoorHallway() {
			super(Door.Type.LOCKED);
		}

		@Override
		public ArrayList<Class<? extends Item>> requiredItems() {
			return new ArrayList<>(Arrays.asList(IronKey.class));
		}
	}

	public static class BronzeLockedDoorHallway extends LockedDoorHallway {
		public BronzeLockedDoorHallway() {
			super(Door.Type.BRONZE);
		}

		@Override
		public ArrayList<Class<? extends Item>> requiredItems() {
			return new ArrayList<>(Arrays.asList(BronzeKey.class));
		}
	}
}
