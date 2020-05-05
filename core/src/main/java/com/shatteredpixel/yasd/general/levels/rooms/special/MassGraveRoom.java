/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.levels.rooms.special;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Skeleton;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.potions.PotionOfLiquidFlame;
import com.shatteredpixel.yasd.general.items.quest.CorpseDust;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.tiles.CustomTilemap;
import com.watabou.noosa.Image;
import com.watabou.noosa.Tilemap;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MassGraveRoom extends SpecialRoom {
	
	@Override
	public int minWidth() { return 7; }
	
	@Override
	public int minHeight() { return 7; }
	
	public void paint(Level level){

		Door entrance = entrance();
		entrance.set(Door.Type.BARRICADE);
		level.addItemToSpawn(new PotionOfLiquidFlame());

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY_SP);

		Bones b = new Bones();

		b.setRect(left+1, top, width()-2, height()-1);
		level.customTiles.add(b);

		//50% 1 skeleton, 50% 2 skeletons
		for (int i = 0; i <= Random.Int(2); i++){
			Skeleton skele = Mob.create(Skeleton.class, level);

			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.findMob(pos) != null);
			skele.pos = pos;
			level.mobs.add( skele );
		}

		ArrayList<Item> items = new ArrayList<>();
		//100% corpse dust, 2x100% 1 coin, 2x30% coins, 1x60% random item, 1x30% armour
		items.add(new CorpseDust());
		items.add(new Gold(1));
		items.add(new Gold(1));
		if (Random.Float() <= 0.3f) items.add(new Gold());
		if (Random.Float() <= 0.3f) items.add(new Gold());
		if (Random.Float() <= 0.6f) items.add(Generator.random());
		if (Random.Float() <= 0.3f) items.add(Generator.randomArmor());

		for (Item item : items){
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get(pos) != null);
			Heap h = level.drop(item, pos);
			h.setHauntedIfCursed();
			h.type = Heap.Type.SKELETON;
		}
	}

	public static class Bones extends CustomTilemap {

		private static final int WALL_OVERLAP   = 3;
		private static final int FLOOR          = 7;

		{
			texture = Assets.PRISON_QUEST;
		}

		@Override
		public Tilemap create() {
			Tilemap v = super.create();
			int[] data = new int[tileW*tileH];
			for (int i = 0; i < data.length; i++){
				if (i < tileW)  data[i] = WALL_OVERLAP;
				else            data[i] = FLOOR;
			}
			v.map( data, tileW );
			return v;
		}

		@Override
		public Image image(int tileX, int tileY) {
			if (tileY == 0) return null;
			else            return super.image(tileX, tileY);
		}

		@Override
		public String name(int tileX, int tileY) {
			return Messages.get(this, "name");
		}

		@Override
		public String desc(int tileX, int tileY) {
			if (Dungeon.hero.morale > Dungeon.hero.MAX_MORALE/2) {
				return Messages.get(this, "desc");
			} else {
				return Messages.get(this, "desc_low_morale");
			}
		}
	}
}
