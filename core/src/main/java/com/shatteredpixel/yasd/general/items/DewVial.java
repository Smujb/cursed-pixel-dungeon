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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.StormCloud;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;

import java.util.ArrayList;

public class DewVial extends Item {

	private static final int MAX_VOLUME	= 20;

	private static final String AC_DRINK	= "drink";
	private static final String AC_WATER	= "water";

	private static final float TIME_TO_DRINK = 1f;

	private static final String TXT_STATUS	= "%d/%d";

	{
		image = ItemSpriteSheet.VIAL;

		defaultAction = AC_DRINK;

		unique = true;
	}

	private int volume = 0;

	private static final String VOLUME	= "volume";

	@Override
	public void storeInBundle(  Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( VOLUME, volume );
	}

	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		super.restoreFromBundle( bundle );
		volume	= bundle.getInt( VOLUME );
	}

	//If required by Alchemy, don't consume it.
	@Override
	public Item replaceForAlchemy() {
		if (volume > 0) {
			return new Dewdrop();
		} else {
			return null;
		}
	}

	public static int volume() {
		DewVial dewVial = Dungeon.hero.belongings.getItem(DewVial.class);
		if (dewVial == null) {
			return 0;
		} else {
			return dewVial.volume;
		}
	}

	public static boolean useDew(int amount) {
		DewVial dewVial = Dungeon.hero.belongings.getItem(DewVial.class);
		if (dewVial != null) {
			dewVial.volume--;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (volume > 0) {
			actions.add( AC_DRINK );
			actions.add( AC_WATER );
		}
		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

		super.execute(hero, action);

		if (action.equals(AC_DRINK)) {

			if (volume > 0) {

				float missingMPPercent = 1f - (hero.mp / (float) hero.maxMP());

				//trimming off 0.01 drops helps with floating point errors
				int dropsNeeded = (int) Math.ceil((missingMPPercent / 0.05f) - 0.01f);
				dropsNeeded = (int) GameMath.gate(1, dropsNeeded, volume);

				//20 drops for a full heal normally
				int heal = Math.round(hero.mp * 0.1f * dropsNeeded);

				int effect = Math.min(hero.maxMP() - hero.mp, heal);
				if (effect > 0) {
					hero.mp += effect;
					hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1 + dropsNeeded / 5);
					hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "value", effect));
				}

				volume -= dropsNeeded;

				hero.spend(TIME_TO_DRINK);
				hero.busy();

				Sample.INSTANCE.play(Assets.Sounds.DRINK);
				hero.sprite.operate(hero.pos);

				updateQuickslot();


			} else {
				GLog.warning(Messages.get(this, "empty"));
			}

		} else if (action.equals(AC_WATER)) {
			if (volume > 0) {
				int dropsNeeded = Math.min(volume, 5);
				GameScene.add(Blob.seed(hero.pos, 20 * dropsNeeded, StormCloud.class));
				hero.spend(TIME_TO_DRINK);
				hero.busy();
				volume -= dropsNeeded;
				hero.sprite.operate(hero.pos);

				updateQuickslot();
			} else {
				GLog.warning(Messages.get(this, "empty"));
			}
		}
	}

	public void empty() {volume = 0; updateQuickslot();}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	public boolean isFull() {
		return volume >= MAX_VOLUME;
	}

	public void collectDew( Dewdrop dew ) {

		GLog.info( Messages.get(this, "collected") );
		volume += dew.quantity;
		if (volume >= MAX_VOLUME) {
			volume = MAX_VOLUME;
			GLog.positive( Messages.get(this, "full") );
		}

		updateQuickslot();
	}

	public void fill() {
		volume = MAX_VOLUME;
		updateQuickslot();
	}

	@Override
	public String status() {
		return Messages.format( TXT_STATUS, volume, MAX_VOLUME );
	}

}
