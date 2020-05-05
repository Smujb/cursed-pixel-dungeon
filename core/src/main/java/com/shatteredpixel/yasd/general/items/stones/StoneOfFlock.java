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

package com.shatteredpixel.yasd.general.items.stones;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Sheep;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class StoneOfFlock extends Runestone {
	
	{
		image = ItemSpriteSheet.STONE_FLOCK;
	}
	
	@Override
	protected void activate(int cell) {
	
		for (int i : PathFinder.NEIGHBOURS9){
			
			if (!Dungeon.level.solid(cell + i)
					&& !Dungeon.level.pit(cell + i)
					&& Actor.findChar(cell + i) == null) {
				
				Sheep sheep = new Sheep();
				sheep.lifespan = Random.IntRange(5, 8);
				sheep.pos = cell + i;
				GameScene.add(sheep);
				Dungeon.level.occupyCell(sheep);
				
				CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
			}
		}
		CellEmitter.get(cell).burst(Speck.factory(Speck.WOOL), 4);
		Sample.INSTANCE.play(Assets.SND_PUFF);
		
	}
	
}
