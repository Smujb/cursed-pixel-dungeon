/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Cursed Pixel Dungeon
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

package com.shatteredpixel.yasd.general.levels.features;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.particles.LeafParticle;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Camouflage;
import com.shatteredpixel.yasd.general.items.artifacts.SandalsOfNature;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.watabou.utils.Random;

public class HighGrass {
	
	//prevents items dropped from grass, from trampling that same grass.
	//yes this is a bit ugly, oh well.
	private static boolean freezeTrample = false;

	public static void trample( Level level, int pos ) {
		
		if (freezeTrample) return;
		
		Char ch = Actor.findChar(pos);
		
		if (level.getTerrain(pos) == Terrain.FURROWED_GRASS){
			if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.HUNTRESS){
				//Do nothing
				freezeTrample = true;
			} else {
				if (level.feeling == Level.Feeling.EMBER) {
					Dungeon.level.set(pos, Terrain.EMBERS);
				} else {
					Dungeon.level.set(pos, Terrain.GRASS);
				}
			}
			
		} else {
			if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.HUNTRESS){
				Dungeon.level.set(pos, Terrain.FURROWED_GRASS);
				freezeTrample = true;
			} else {
				Dungeon.level.set(pos, Terrain.GRASS);
			}
			
			int naturalismLevel = 0;
			
			if (ch != null) {
				SandalsOfNature.Naturalism naturalism = ch.buff( SandalsOfNature.Naturalism.class );
				if (naturalism != null) {
					if (!naturalism.isCursed()) {
						naturalismLevel = naturalism.itemLevel() + 1;
						naturalism.charge();
					} else {
						naturalismLevel = -1;
					}
				}
			}
			
			if (naturalismLevel >= 0) {
				// Seed, scales from 1/20 to 1/4
				if (Random.Int(20 - (naturalismLevel * 4)) == 0) {
					level.drop(Generator.random(Generator.Category.SEED), pos).sprite.drop();
				}

				if (Random.Int(30 - (naturalismLevel * 5)) == 0) {//Stones more rarely drop
					level.drop(Generator.random(Generator.Category.STONE), pos).sprite.drop();
				}
				
				// Dew, scales from 1/6 to 1/3
				if (Random.Int(24 - (naturalismLevel * 3)) <= 3) {
					level.drop(new Dewdrop(), pos).sprite.drop();
				}
			}
			
			if (ch instanceof Hero) {
				Hero hero = (Hero) ch;

				//Camouflage
				boolean hasCamoflage = false;
				if (hero.belongings.armor != null && hero.belongings.armor.hasGlyph(Camouflage.class, hero)) {
					hasCamoflage = true;
				}

				if (hasCamoflage) {
					Buff.affect(hero, Camouflage.Camo.class).set(5);
				}
			}
			
		}
		
		freezeTrample = false;
		
		if (CPDGame.scene() instanceof GameScene) {
			GameScene.updateMap(pos);
			
			CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, 4);
			if (Dungeon.level.heroFOV[pos]) Dungeon.observe();
		}
	}
}
