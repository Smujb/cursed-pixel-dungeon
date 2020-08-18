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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Belongings;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.Ankh;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.rings.Ring;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.journal.Notes;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class Statue extends Mob implements Callback {
	
	{
		spriteClass = StatueSprite.class;

		EXP = 0;
		state = PASSIVE;
		
		properties.add(Property.INORGANIC);

		belongings = new Belongings(this);
	}

	int ankhs = Math.max(1, level / Constants.CHAPTER_LENGTH);//1 Ankh per chapter

	@Override
	public float spawningWeight() {
		return 0f;
	}
	
	public Statue() {
		super();

		//belongings.setWeapon((KindOfWeapon) Generator.randomWeapon().level(0).uncurse().identify());
		//belongings.setArmor((Armor) Generator.randomArmor().level(0).uncurse().identify());

		for (int i = 0; i < belongings.miscs.length; i++) {
			belongings.miscs[i] = newItem();
			belongings.miscs[i].activate(this);
		}

		upgradeItems();
		
		HP = HT = 30 + Dungeon.getScaleFactor() * 10;
	}

	@Override
	public float sneakSkill(Char enemy) {
		return defenseSkill(enemy);
	}

	@Override
	public float noticeSkill(Char enemy) {
		return attackSkill(enemy);
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 4 + Dungeon.getScaleFactor();
	}

	@Override
	public int attackSkill(Char target) {
		return 10 + Dungeon.getScaleFactor();
	}

	@Override
	public boolean canAttack(@NotNull Char enemy) {
		if (Dungeon.level.adjacent( pos, enemy.pos )) {
			return super.canAttack( enemy );
		} else if ((wand = wandToAttack(enemy)) != null) {
			return new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		} else {
			return false;
		}
	}

	@NotNull
	private KindofMisc newItem() {
		boolean con = false;
		KindofMisc item;
		do {
			int type = Random.Int(4);
			switch (type) {
				case 0: default:
					item = ((KindofMisc) Generator.random(Generator.Category.RING));
					if (belongings.getMiscsOfType(Ring.class).size() < 3) {
						con = true;
					}
					break;
				case 2: case 3:
					item = ((KindofMisc) Generator.random(Generator.Category.WAND));
					if (belongings.getMiscsOfType(Wand.class).size() < 3) {
						con = true;
					}
					break;
			}
		} while (!con || Challenges.isItemBlocked(item));

		item.level(0);
		item.cursed = false;
		item.identify();
		return item;
	}

	protected void upgradeItems() {
		int sous = Dungeon.getScaleFactor()*5;
		EquipableItem item;
		if (belongings.miscs.length > 0 || belongings.getWeapon() != null || belongings.getArmor() != null) {
			do {
				do {
					item = null;
					int slot = Random.Int(belongings.miscs.length);
					if (slot < belongings.miscs.length) {
						item = belongings.miscs[slot];
					}
				} while (item == null || !item.isUpgradable());//If the item is not upgradeable (An artifact or +3) chose another. Also, if it is null (nothing equipped in that slot)
				item.upgrade();
				sous--;
			} while (sous > 0);
		}
	}

	private Wand wand = null;

	private void wandZap(Char enemy) {
		if (enemy != null) {
			if (wand == null) {
				wand = wandToAttack(enemy);
			}
			wand.activate(this);
			if (wand instanceof WandOfWarding) {//Wand of Warding cannot zap directly
				int closest = findClosest(enemy, pos);

				if (closest == -1){
					sprite.centerEmitter().burst(MagicMissile.WardParticle.FACTORY, 8);
					spend(TICK);
					next();
				} else {
					wand.zap(closest);
				}
			} else {
				wand.zap(enemy.pos);
			}

		}
	}

	@Override
	public CharSprite sprite() {
		CharSprite sprite = super.sprite();
		Armor armor = belongings.getArmor();
		if (armor != null) {
			((StatueSprite) sprite).setArmor(armor.appearance());
		}
		return sprite;
	}

	private Wand wandToAttack(Char enemy) {
		if (enemy != null ) {
			ArrayList<KindofMisc> Wands = belongings.getMiscsOfType(Wand.class);
			ArrayList<Wand> UsableWands = new ArrayList<>();
			for (int i = 0; i < Wands.size(); i++) {
				Wand Wand = ((Wand) Wands.get(i));
				if (Wand.tryToZap(this, enemy.pos)) {
					UsableWands.add(Wand);
				}
			}
			if (UsableWands.size() > 0) {
				return Random.element(UsableWands);
			}
		}
		return null;

	}
	
	@Override
	protected boolean act() {
		if (Dungeon.level.heroFOV[pos]) {
			Notes.add( Notes.Landmark.STATUE );
		}
		return super.act();
	}
	
	@Override
	public void damage(int dmg,  DamageSrc src) {

		if (state == PASSIVE) {
			state = HUNTING;
			return;
		}
		if (dmg > HP & ankhs > 0) {
			Ankh.revive(this, null);
			ankhs--;
		}
		super.damage( dmg, src);
	}

	protected boolean doAttack( Char enemy ) {
		if (Dungeon.level.adjacent( pos, enemy.pos )) {
			return super.doAttack( enemy );
		} else if (belongings.getMiscsOfType(Wand.class).size() > 0) {
			wandZap(enemy);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void beckon( int cell ) {
		// Do nothing
	}

	public void dropGear() {
		ArrayList<Item> items = new ArrayList<>(Arrays.asList(belongings.miscs));
		items.add(belongings.getWeapon());
		items.add(belongings.getArmor());
		for (Item item : items) {
			if (item != null) {
				Dungeon.level.drop(item.identify(), pos).sprite.drop();
			}
		}
	}

	@Override
	public void die( DamageSrc cause ) {
		dropGear();
		super.die( cause );
	}
	
	@Override
	public void destroy() {
		Notes.remove( Notes.Landmark.STATUE );
		super.destroy();
	}
	
	@Override
	public boolean reset() {
		state = PASSIVE;
		return true;
	}

	@Override
	public String description() {
		StringBuilder description = new StringBuilder(super.description() + "_");
		for (int i=0; i < belongings.miscs.length; i++) {
			if (belongings.miscs[i] != null) {
				description.append(belongings.miscs[i].name()).append("_ \n\n_");
			}
		}
		return description + "_";
	}

	private static final String ANKHS = "ankhs";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		belongings.storeInBundle(bundle);
		bundle.put(ANKHS, ankhs);
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		belongings.restoreFromBundle(bundle);
		ankhs = bundle.getInt(ANKHS);
	}

	{
		resistances.put(Element.SHADOW, 0.5f);
	}

	@Override
	public void call() {
		next();
	}
}
