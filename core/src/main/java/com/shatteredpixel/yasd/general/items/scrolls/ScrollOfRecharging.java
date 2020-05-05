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

package com.shatteredpixel.yasd.general.items.scrolls;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.Recharging;
import com.shatteredpixel.yasd.general.effects.SpellSprite;
import com.shatteredpixel.yasd.general.effects.particles.EnergyParticle;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRecharging extends Scroll {

	public static final float BUFF_DURATION = 30f;

	{
		initials = 6;
	}

	@Override
	public void doRead() {

		Buff.affect(curUser, Recharging.class, BUFF_DURATION);
		charge(curUser);
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();

		GLog.i( Messages.get(this, "surge") );
		SpellSprite.show( curUser, SpellSprite.CHARGE );
		setKnown();

		readAnimation();
	}
	
	@Override
	public void empoweredRead() {
		doRead();
		Buff.append(curUser, Recharging.class, BUFF_DURATION/3f);
	}
	
	public static void charge( Char ch ) {
		ch.sprite.centerEmitter().burst( EnergyParticle.FACTORY, 15 );
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
