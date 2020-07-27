package com.shatteredpixel.yasd.general.levels.test;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.levels.rooms.connection.ConnectionRoom;

import java.util.ArrayList;

public abstract class MetroidVaniaConnectionRoom extends ConnectionRoom {
	public abstract ArrayList<Class<? extends Item>> requiredItems();
}
