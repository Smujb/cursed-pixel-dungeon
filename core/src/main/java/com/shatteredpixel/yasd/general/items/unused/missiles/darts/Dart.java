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

package com.shatteredpixel.yasd.general.items.unused.missiles.darts;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.weapon.melee.Crossbow;
import com.shatteredpixel.yasd.general.items.weapon.melee.SwiftCrossbow;
import com.shatteredpixel.yasd.general.items.unused.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Dart extends MissileWeapon {

	{
		image = ItemSpriteSheet.DART;
		hitSound = Assets.Sounds.HIT_ARROW;
		hitSoundPitch = 1.3f;

		//infinite, even with penalties
		baseUses = INFINITE_USES;
	}

	protected static final String AC_TIP = "TIP";
	
	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_TIP );
		return actions;
	}
	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_TIP)){
			GameScene.selectItem(itemSelector, WndBag.Mode.SEED, Messages.get(this, "prompt"));
		}
	}
	
	@Override
	public int min(float lvl) {
		int base = 1 + Dungeon.getScaling()/2;
		if (getCrossbow() != null){
			return  getCrossbow().min(lvl);  //+1 per level or bow level
		} else {
			return  Math.round(base +     //1 base, down from 2
					lvl);    //scaling unchanged
		}
	}

	@Override
	public int max(float lvl) {
		int base = 2 + Dungeon.getScaling();
		if (getCrossbow() != null){
			return getCrossbow().max(lvl);  //+3 per bow level, +2 per level (default scaling +2)
		} else {
			return  Math.round(base +     //2 base, down from 5
					2*lvl);  //scaling unchanged
		}
	}

	private Crossbow crossbow = null;

	public void setCrossbow(Crossbow crossbow) {
		this.crossbow = crossbow;
	}

	private Crossbow getCrossbow() {
		if (curUser == null) {
			return null;
		}
		if (crossbow == null || !crossbow.isEquipped(curUser)) {
			crossbow = null;
			for (KindofMisc misc : curUser.belongings.miscs) {
				//Pick the highest level crossbow.
				if (misc instanceof Crossbow && (crossbow == null || misc.level() > crossbow.level())) {
					crossbow = (Crossbow) misc;
				}
			}
		}
		return crossbow;
	}

	@Override
	public float castDelay(Char user, int dst) {
		Crossbow crossbow = getCrossbow();
		float delay;
		if (crossbow == null) {
			delay = super.castDelay(user, dst);
		} else {
			delay = crossbow.speedFactor(user);
		}
		Char ch = Actor.findChar(dst);
		if (ch != null) {
			SwiftCrossbow.SwiftDartAttack swift = ch.buff(SwiftCrossbow.SwiftDartAttack.class);
			if (swift != null) {
				delay /= 2f;
			}
		}
		return delay;
	}

	@Override
	public void throwSound() {
		Crossbow crossbow = getCrossbow();
		if (crossbow != null) {
			Sample.INSTANCE.play(Assets.Sounds.ATK_CROSSBOW, 1, Random.Float(0.87f, 1.15f));
		} else {
			super.throwSound();
		}
	}
	
	@Override
	public boolean hasEnchant(Class<? extends Enchantment> type, Char owner) {
		if (getCrossbow() != null && getCrossbow().hasEnchant(type, owner)){
			return true;
		} else {
			return super.hasEnchant(type, owner);
		}
	}
	
	@Override
	public int proc(Char attacker, Char defender, int damage) {

		Crossbow crossbow = getCrossbow();

		if (crossbow != null && crossbow.enchantment != null && attacker.buff(MagicImmune.class) == null){
			level(crossbow.level());
			damage = crossbow.enchantment.proc(this, attacker, defender, damage);
			level(0);
		}
		if (crossbow instanceof SwiftCrossbow) {
			Buff.affect(defender, SwiftCrossbow.SwiftDartAttack.class, this.speedFactor(attacker)*2f);
		}
		return super.proc(attacker, defender, damage);
	}
	
	@Override
	public int price() {
		return super.price()/2; //half normal value
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener(this) {
		
		@Override
		public void onSelect(final Item item) {
			
			if (item == null) return;

			final Dart dart = (Dart) source;
			
			final int maxToTip = Math.min(dart.quantity(), item.quantity()*2);
			final int maxSeedsToUse = (maxToTip+1)/2;
			
			final int singleSeedDarts;
			
			final String[] options;
			
			if (dart.quantity() == 1){
				singleSeedDarts = 1;
				options = new String[]{
						Messages.get(Dart.class, "tip_one"),
						Messages.get(Dart.class, "tip_cancel")};
			} else {
				singleSeedDarts = 2;
				if (maxToTip <= 2){
					options = new String[]{
							Messages.get(Dart.class, "tip_two"),
							Messages.get(Dart.class, "tip_cancel")};
				} else {
					options = new String[]{
							Messages.get(Dart.class, "tip_all", maxToTip, maxSeedsToUse),
							Messages.get(Dart.class, "tip_two"),
							Messages.get(Dart.class, "tip_cancel")};
				}
			}
			
			TippedDart tipResult = TippedDart.getTipped((Plant.Seed) item, 1);
			
			GameScene.show(new WndOptions(Messages.get(Dart.class, "tip_title"),
					Messages.get(Dart.class, "tip_desc", tipResult.name()) + "\n\n" + tipResult.desc(),
					options){
				
				@Override
				protected void onSelect(int index) {
					super.onSelect(index);
					
					if (index == 0 && options.length == 3){
						if (item.quantity() <= maxSeedsToUse){
							item.detachAll( curUser.belongings.backpack );
						} else {
							item.quantity(item.quantity() - maxSeedsToUse);
						}
						
						if (maxToTip < dart.quantity()){
							dart.quantity(dart.quantity() - maxToTip);
						} else {
							dart.detachAll(curUser.belongings.backpack);
						}
						
						TippedDart newDart = TippedDart.getTipped((Plant.Seed) item, maxToTip);
						if (!newDart.collect()) Dungeon.level.drop(newDart, curUser.pos).sprite.drop();
						
						curUser.spend( 1f );
						curUser.busy();
						curUser.sprite.operate(curUser.pos);
						
					} else if ((index == 1 && options.length == 3) || (index == 0 && options.length == 2)){
						item.detach( curUser.belongings.backpack );
						
						if (dart.quantity() <= singleSeedDarts){
							dart.detachAll( curUser.belongings.backpack );
						} else {
							dart.quantity(dart.quantity() - singleSeedDarts);
						}
						
						TippedDart newDart = TippedDart.getTipped((Plant.Seed) item, singleSeedDarts);
						if (!newDart.collect()) Dungeon.level.drop(newDart, curUser.pos).sprite.drop();
						
						curUser.spend( 1f );
						curUser.busy();
						curUser.sprite.operate(curUser.pos);
					}
				}
			});
			
		}
		
	};
}
