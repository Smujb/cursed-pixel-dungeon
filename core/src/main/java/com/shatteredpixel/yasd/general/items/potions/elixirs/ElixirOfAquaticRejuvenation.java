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

package com.shatteredpixel.yasd.general.items.potions.elixirs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.potions.PotionOfRestoration;
import com.shatteredpixel.yasd.general.items.quest.GooBlob;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class ElixirOfAquaticRejuvenation extends Elixir {
	
	{
		//TODO finish visuals
		image = ItemSpriteSheet.ELIXIR_AQUA;
	}
	
	@Override
	public void apply(Hero hero) {
		Buff.affect(hero, AquaHealing.class).set(Math.round(hero.HT * 1.5f));
	}
	
	@Override
	public int price() {
		//prices of ingredients
		return quantity * (30 + 50);
	}
	
	public static class AquaHealing extends Buff {
		
		{
			type = buffType.POSITIVE;
			announced = true;
		}
		
		private int left;
		
		public void set( int amount ){
			if (amount > left) left = amount;
		}
		
		@Override
		public boolean act() {
			
			if (Dungeon.level.liquid()[target.pos] && target.HP < target.HT){
				float healAmt = GameMath.gate( 1, target.HT/100f, left );
				healAmt = Math.min(healAmt, target.HT - target.HP);
				if (Random.Float() < (healAmt % 1)){
					healAmt = (float)Math.ceil(healAmt);
				} else {
					healAmt = (float)Math.floor(healAmt);
				}
				target.heal((int) healAmt, false);
				left -= healAmt;
			}
			
			if (left <= 0){
				detach();
			} else {
				spend(TICK);
			}
			return true;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.HEALING;
		}
		
		@Override
		public void tintIcon(Image icon) {
			icon.hardlight(0, 0.75f, 0.75f);
		}

		@Override
		public float iconFadePercent() {
			float max = Math.round(target.HT * 1.5f);
			return Math.max(0, (max - left) / max);
		}
		
		@Override
		public String toString() {
			return Messages.get(this, "name");
		}
		
		@Override
		public String desc() {
			return Messages.get(this, "desc", left);
		}
		
		private static final String LEFT = "left";
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			super.storeInBundle( bundle );
			bundle.put( LEFT, left );
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			super.restoreFromBundle( bundle );
			left = bundle.getInt( LEFT );
			
		}
	}
	
	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {
		
		{
			inputs =  new Class[]{PotionOfRestoration.class, GooBlob.class};
			inQuantity = new int[]{1, 1};
			
			cost = 6;
			
			output = ElixirOfAquaticRejuvenation.class;
			outQuantity = 1;
		}
		
	}
	
}
