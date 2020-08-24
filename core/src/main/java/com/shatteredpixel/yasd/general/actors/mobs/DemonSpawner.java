/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Cursed Pixel Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.SpawnerSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DemonSpawner extends Mob {

	{
		spriteClass = SpawnerSprite.class;

		healthFactor = 0.7f;
		evasionFactor = 0f;

		EXP = 25;

        state = PASSIVE;

		loot = PotionOfHealing.class;
		lootChance = 1f;

		properties.add(Property.IMMOVABLE);
		properties.add(Property.MINIBOSS);
		properties.add(Property.DEMONIC);
	}

	@Override
	public void beckon(int cell) {
		//do nothing
	}

	@Override
	public boolean reset() {
		return true;
	}

	private float spawnCooldown = 0;

	public boolean spawnRecorded = false;


	@Override
	protected boolean act() {
		if (!spawnRecorded){
			Statistics.spawnersAlive++;
			spawnRecorded = true;
		}

		spawnCooldown--;
		if (spawnCooldown <= 0){
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8) {
				if (!Dungeon.level.solid(pos+n) && Actor.findChar( pos+n ) == null) {
					candidates.add( pos+n );
				}
			}

			if (!candidates.isEmpty()) {
				RipperDemon spawn = Mob.create(RipperDemon.class);

				spawn.pos = Random.element( candidates );
				spawn.state = spawn.HUNTING;

				Dungeon.level.occupyCell(spawn);

				GameScene.add( spawn, 1 );
				if (sprite.visible) {
					Actor.addDelayed(new Pushing(spawn, pos, spawn.pos), -1);
				}

				spawnCooldown += 60;
				if (Dungeon.depth > 21){
					//60/53.33/46.67/40 turns to spawn on floor 21/22/23/24
					spawnCooldown -= Math.min(20, (Dungeon.depth-21)*6.67);
				}
			}
		}
		return super.act();
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {
		float threshold = HT/4f;
		if (dmg >= threshold){
			dmg = (int) ((threshold-1) + Math.sqrt((24/threshold)*(dmg - (threshold-1)) + 1) - 1)/2;
		}
		super.damage(dmg, src);
	}

	@Override
	public void die(DamageSrc cause) {
		if (spawnRecorded){
			Statistics.spawnersAlive--;
		}
		GLog.highlight(Messages.get(this, "on_death"));
		super.die(cause);
	}

	private static final String SPAWN_COOLDOWN = "spawn_cooldown";
	private static final String SPAWN_RECORDED = "spawn_recorded";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SPAWN_COOLDOWN, spawnCooldown);
		bundle.put(SPAWN_RECORDED, spawnRecorded);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		spawnCooldown = bundle.getFloat(SPAWN_COOLDOWN);
		spawnRecorded = bundle.getBoolean(SPAWN_RECORDED);
	}
}
