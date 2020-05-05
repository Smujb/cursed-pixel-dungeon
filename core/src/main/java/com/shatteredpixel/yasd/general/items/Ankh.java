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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Flare;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.artifacts.DriedRose;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSprite.Glowing;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class Ankh extends Item {

	{
		image = ItemSpriteSheet.ANKH;
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}

	private static final Glowing WHITE = new Glowing( 0xFFFFCC );

	@Override
	public Glowing glowing() {
		return WHITE;
	}

	public void revive(Char toRevive) {
		revive(toRevive, this);
	}

	public static void revive(Char toRevive, Ankh ankh) {
		toRevive.HP = toRevive.HT;

		//ensures that you'll get to act first in almost any case, to prevent reviving and then instantly dying again.
		PotionOfHealing.cure(toRevive);
		Buff.detach(toRevive, Paralysis.class);
		toRevive.spend(-toRevive.cooldown());
		if (Dungeon.hero.fieldOfView[toRevive.pos]) {
			new Flare(8, 32).color(0xFFFF66, true).show(toRevive.sprite, 2f);
			CellEmitter.get(toRevive.pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		}

		if (toRevive.hasBelongings() && ankh != null) {
			ankh.detach(toRevive.belongings.backpack);
			toRevive.belongings.uncurseEquipped();
		}

		if (toRevive == Dungeon.hero) {
			Sample.INSTANCE.play(Assets.SND_TELEPORT);
			GLog.w(Messages.get(Ankh.class, "revive"));
			Statistics.ankhsUsed++;
		}

		for (Char ch : Actor.chars()){
			if (ch instanceof DriedRose.GhostHero && toRevive == Dungeon.hero){
				((DriedRose.GhostHero) ch).sayAnhk();
				return;
			}
		}
	}
	
	@Override
	public int price() {
		return 100 * quantity;
	}
}
