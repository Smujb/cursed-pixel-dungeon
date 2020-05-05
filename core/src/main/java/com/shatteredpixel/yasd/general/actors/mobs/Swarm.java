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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.Hunger;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.SwarmSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Swarm extends Mob {

	{
		spriteClass = SwarmSprite.class;
		healthFactor = 3f;
		//HP = HT = 60;
		evasionFactor = 1.2f;
		damageFactor = 0.5f;
		//defenseSkill = 9;

		EXP = 6;
		
		flying = true;

		loot = new SmallRation();
		lootChance = 0.1f; //by default, see rollToDropLoot()
	}
	
	private static final float SPLIT_DELAY	= 1f;
	
	private int generation	= 0;
	
	private static final String GENERATION	= "generation";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( GENERATION, generation );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		generation = bundle.getInt( GENERATION );
		if (generation > 0) EXP = 0;
	}
	
	/*@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 9 );
	}*/

	@Override
	public int attackProc(Char enemy, int damage) {
		Hunger hunger = enemy.buff( Hunger.class );

		if( hunger != null ){

			hunger.reduceHunger( -5 );

		}
		return super.attackProc(enemy, damage);
	}


	@Override
	public int defenseProc(Char enemy, int damage) {

		if (HP >= damage + 2 && !enemy.elementalType().isMagical()) {
			ArrayList<Integer> candidates = new  ArrayList<>();
			boolean[] solid = Dungeon.level.solid();
			
			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (!solid[n] && Actor.findChar( n ) == null) {
					candidates.add( n );
				}
			}
	
			if (candidates.size() > 0) {
				
				Swarm clone = split();
				clone.HP = (HP - damage) / 2;
				clone.pos = Random.element( candidates );
				clone.state = clone.HUNTING;
				
				Dungeon.level.occupyCell(clone);
				
				GameScene.add( clone, SPLIT_DELAY );
				Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );
				
				HP -= clone.HP;
			}
		}
		
		return super.defenseProc(enemy, damage);
	}
	
	/*@Override
	public int attackSkill( Char target ) {
		return 12;
	}*/
	
	private Swarm split() {
		Swarm clone = Mob.create(Swarm.class);
		clone.generation = generation + 1;
		clone.EXP = 0;
		if (buff( Burning.class ) != null) {
			Buff.affect( clone, Burning.class ).reignite( clone );
		}
		if (buff( Poison.class ) != null) {
			Buff.affect( clone, Poison.class ).set(2);
		}
		if (buff(Corruption.class ) != null) {
			Buff.affect( clone, Corruption.class);
		}
		return clone;
	}
}
