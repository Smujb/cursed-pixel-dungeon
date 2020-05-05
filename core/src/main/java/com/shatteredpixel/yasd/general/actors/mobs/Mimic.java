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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.MimicSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Mimic extends Mob {
	
	protected int level;
	
	{
		spriteClass = MimicSprite.class;

		properties.add(Property.DEMONIC);
		properties.add(Property.MINIBOSS);

		damageFactor = 1.5f;
		accuracyFactor = 1.5f;
		evasionFactor = 0.6f;

		EXP = 0;

		//mimics are neutral when hidden
		alignment = Alignment.NEUTRAL;
		state = PASSIVE;
	}
	
	public ArrayList<Item> items;
	
	private static final String LEVEL	= "level";
	private static final String ITEMS	= "items";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		if (items != null) bundle.put( ITEMS, items );
		bundle.put( LEVEL, level );
	}

	@Override
	public float spawningWeight() {
		return 0f;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		if (bundle.contains( ITEMS )) {
			items = new  ArrayList<>((Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS)));
		}
		if (state != PASSIVE && alignment == Alignment.NEUTRAL){
			alignment = Alignment.ENEMY;
		}
		super.restoreFromBundle(bundle);
	}

	@Override
	public void add(Buff buff) {
		super.add(buff);
		if (buff.type == Buff.buffType.NEGATIVE && alignment == Alignment.NEUTRAL){
			alignment = Alignment.ENEMY;
			stopHiding();
			if (sprite != null) sprite.idle();
		}
	}

	@Override
	public String name() {
		if (alignment == Alignment.NEUTRAL){
			return Messages.get(Heap.class, "chest");
		} else {
			return super.name();
		}
	}

	@Override
	public String description() {
		if (alignment == Alignment.NEUTRAL){
			return Messages.get(Heap.class, "chest_desc");
		} else {
			return super.description();
		}
	}

	@Override
	protected boolean act() {
		if (alignment == Alignment.NEUTRAL && state != PASSIVE){
			alignment = Alignment.ENEMY;
			GLog.w(Messages.get(this, "reveal") );
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.SND_MIMIC);
		}
		return super.act();
	}

	@Override
	public int damageRoll() {
		return alignment == Alignment.NEUTRAL ? (int) (super.damageRoll() * 1.5f) : super.damageRoll();
	}

	@Override
	public CharSprite sprite() {
		MimicSprite sprite = (MimicSprite) super.sprite();
		if (alignment == Alignment.NEUTRAL) sprite.hideMimic();
		return sprite;
	}

	@Override
	public void beckon( int cell ) {
		// Do nothing
	}

	@Override
	public boolean interact(Char c) {
		if (alignment != Alignment.NEUTRAL || c != Dungeon.hero){
			return super.interact(c);
		}
		stopHiding();
		Dungeon.hero.busy();
		Dungeon.hero.sprite.operate(pos);
		if (Dungeon.hero.invisible <= 0
				&& Dungeon.hero.buff(Swiftthistle.TimeBubble.class) == null
				&& Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class) == null){
			return doAttack(Dungeon.hero);
		} else {
			sprite.idle();
			alignment = Alignment.ENEMY;
			Dungeon.hero.spendAndNext(1f);
			return true;
		}
	}

	@Override
	public void onAttackComplete() {
		super.onAttackComplete();
		if (alignment == Alignment.NEUTRAL){
			alignment = Alignment.ENEMY;
			Dungeon.hero.spendAndNext(1f);
		}
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {
		if (state == PASSIVE){
			alignment = Alignment.ENEMY;
			stopHiding();
		}
		super.damage(dmg, src);
	}

	public void stopHiding(){
		state = HUNTING;

		if (Actor.chars().contains(this) && Dungeon.level.heroFOV[pos]) {
			enemy = Dungeon.hero;
			target = Dungeon.hero.pos;
			enemySeen = true;
			GLog.w(Messages.get(this, "reveal") );
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.SND_MIMIC);
		}
	}



	@Override
	public int attackSkill( Char target ) {

		return alignment == Alignment.NEUTRAL ? INFINITE_ACCURACY : super.attackSkill(target);
	}
	
	@Override
	public void rollToDropLoot(){
		
		if (items != null) {
			for (Item item : items) {
				Dungeon.level.drop( item, pos ).sprite.drop();
			}
			items = null;
		}
		super.rollToDropLoot();
	}
	
	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	public static Mimic spawnAt(int pos, Item item, Level level){
		return spawnAt( pos, Arrays.asList(item), Mimic.class, level);
	}

	public static Mimic spawnAt(int pos, Item item, Class<? extends Mimic> mimicType, Level level) {
		return spawnAt(pos, Arrays.asList(item), mimicType, level);
	}

	public static Mimic spawnAt(int pos, List<Item> items, Class<? extends Mimic> mimicType, Level level) {
		
		Mimic m = Mob.create(mimicType, level);
		m.items = new ArrayList<>( items );
		m.enemySeen = true;
		m.pos = pos;

		m.generatePrize();

		return m;
	}

	protected void generatePrize(){

		//generate an extra reward for killing the mimic
		Item reward = null;
		do {
			switch (Random.Int(5)) {
				case 0:
					reward = new  Gold().random();
					break;
				case 1:
					reward = Generator.randomMissile();
					break;
				case 2:
					reward = Generator.randomArmor();
					break;
				case 3:
					reward = Generator.randomWeapon();
					break;
				case 4:
					reward = Generator.random(Generator.Category.RING);
					break;
			}
		} while (reward == null || Challenges.isItemBlocked(reward));
		items.add(reward);
	}
}
