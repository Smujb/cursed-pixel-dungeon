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

package com.shatteredpixel.yasd.general.levels.traps;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Statue;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class GuardianTrap extends Trap {

	{
		color = RED;
		shape = STARS;
	}

	@Override
	public void activate() {

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			mob.beckon( pos );
		}

		if (Dungeon.level.heroFOV[pos]) {
			GLog.warning( Messages.get(this, "alarm") );
			CellEmitter.center(pos).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
		}

		Sample.INSTANCE.play( Assets.Sounds.ALERT );

		for (int i = 0; i < Math.round(Dungeon.getScaleModifier()); i++){
			Guardian guardian = Mob.create(Guardian.class);
			guardian.state = guardian.WANDERING;
			guardian.pos = Dungeon.level.randomRespawnCell(guardian);
			GameScene.add(guardian);
			guardian.beckon(Dungeon.hero.pos );
		}

	}

	public static class Guardian extends Statue {

		{
			spriteClass = GuardianSprite.class;

            state = WANDERING;
			lootChance = 0.5f;
		}

		@Override
		public void dropGear() {}

		@Override
		public void beckon(int cell) {
			//Beckon works on these ones, unlike their superclass.
			notice();

			if (state != HUNTING) {
				state = WANDERING;
			}
			target = cell;
		}

	}

	public static class GuardianSprite extends StatueSprite {

		public GuardianSprite(){
			super();
			tint(0, 0, 1, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(0, 0, 1, 0.2f);
		}
	}
}
