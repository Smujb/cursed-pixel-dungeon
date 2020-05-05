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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Chill;
import com.shatteredpixel.yasd.general.actors.buffs.Frost;
import com.shatteredpixel.yasd.general.actors.buffs.Hex;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.actors.buffs.Roots;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.actors.buffs.Vulnerable;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.actors.buffs.Wet;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.Flare;
import com.shatteredpixel.yasd.general.effects.Lightning;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.FlameParticle;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.effects.particles.SparkParticle;
import com.shatteredpixel.yasd.general.items.weapon.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.general.levels.SewerLevel;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public enum Element {
	/*
	The purpose of this file is to make it easier to add types of damage to the game.
	It also gives a central place to group damage sources together - for example the buff Burning, the blob Fire, and the enchantment Blazing.

	Each chapter should have a few basic mobs which simply have the Element Physical or Magical, and several unique ones. This is to make battles
	feel interesting while also having easier mobs.
	 */
	PHYSICAL( false ),
	MAGICAL( true ),

	//Earth
	EARTH( false ),//Damages higher depending on surrounding grass
	GRASS( true ),//Roots
	STONE( false ),//Paralyzes
	SHARP( false ),//Bleeds

	//Fire
	FIRE( true),//Sets on fire
	DESTRUCTION( true ),//Causes "Vulnerable"
	ACID( true ),//Causes Ooze
	DRAIN( true ),//Drains life

	//Water
	WATER( true ),//Wets
	COLD( true ),//Chills
	TOXIC( true ),//Poisons
	CONFUSION( false ),//Confuses

	//Air
	AIR( false ),//Causes "Hex"
	SHOCK( true ),//More dmg in water
	LIGHT( true ),//More dmg vs undead/demonic
	SPIRIT( true ),//Weakens


	//Used for enforcing death in unusual circumstances where other elements wouldn't fit.
	META( true );

	public int attackProc(int damage, Char attacker, Char defender) {
		switch (this) {
			case LIGHT:
				if (defender.properties().contains(Char.Property.UNDEAD) || defender.properties().contains(Char.Property.DEMONIC)) {
					damage *= 1.5;
				}
				if (defender == Dungeon.hero) {
					GameScene.flash(0xFFFFFF);
				}
				break;
			case SHARP:
				if (Random.Int( 2 ) == 0) {
					Buff.affect( defender, Bleeding.class ).set( Math.max(1, damage/2) );
				}
				break;
			case DESTRUCTION:
				Buff.affect(defender, Vulnerable.class, 5);
				break;
			case FIRE:
				Buff.affect(defender, Burning.class).reignite(defender);
				break;
			case WATER:
				Buff.affect( defender, Wet.class, Wet.DURATION );
				break;
			case STONE:
				if (Random.Int(3) == 0 && defender.buff(Paralysis.class) == null) {
					Buff.affect(defender, Paralysis.class, 3f);
				}
				break;
			case COLD:
				if (defender.buff(Frost.class) != null){
					break; //do nothing, can't affect a frozen target
				}
				if (defender.buff(Chill.class) != null){
					//5% more damage per turn of chill
					float chill = defender.buff(Chill.class).cooldown();
					damage = (int) Math.round(damage * Math.pow(1.05f, chill));
				} else {
					defender.sprite.burst( 0xFF99CCFF, 1 + damage / 3 );
				}

				if (defender.isAlive()){
					if (Dungeon.level.liquid(defender.pos))
						Buff.prolong(defender, Chill.class, 4);
					else
						Buff.prolong(defender, Chill.class, 2);
				}
				break;
			case EARTH:
				int grassCells = 0;
				for (int i : PathFinder.NEIGHBOURS9) {
					if (Dungeon.level.map[attacker.pos+i] == Terrain.FURROWED_GRASS
							|| Dungeon.level.map[attacker.pos+i] == Terrain.HIGH_GRASS){
						grassCells++;
					}
				}
				if (grassCells > 0) damage = damage * (20/(20-grassCells));
				break;
			case GRASS:
				Buff.affect(defender, Roots.class, Paralysis.DURATION);
				break;
			case AIR:
				Buff.affect(defender, Hex.class, 5);
				break;
			case ACID:
				if (Random.Int(2) == 0) {
					Buff.affect(defender, Ooze.class).set(20f);
				}
				break;
			case SPIRIT:
				Buff.affect(defender, Weakness.class, 5);
				break;
			case DRAIN:
				int healed = damage / 2;

				if (healed > 0) {

					if (!(defender.properties().contains(Char.Property.UNDEAD) || defender.properties().contains(Char.Property.INORGANIC))) {
						attacker.heal(healed, false);
					}
					defender.sprite.burst(0xFFFFFFFF, 5);
				}
				break;
			case SHOCK:
				if ((Dungeon.level.liquid(defender.pos) && !defender.isFlying()) || defender.buff(Wet.class) != null) {
					damage *= 1.5f;
				}
				break;
			case CONFUSION:
				Buff.affect(defender, Vertigo.class, Vertigo.DURATION);
				break;
			case TOXIC:
				Buff.affect(defender, Poison.class).set(2 + Dungeon.getScaleFactor() / 3f);
				break;
		}

		if (attacker.alignment == Char.Alignment.ENEMY) {
			damage *= Dungeon.difficulty.mobDamageFactor();
		}

		if (Dungeon.hero.fieldOfView[defender.pos] || Dungeon.hero.fieldOfView[attacker.pos]) {
			Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
		}
		return damage;
	}

	public int defenseProc(int damage, Char attacker, Char defender) {
		//switch (this) {
		//	case ACID:
		//		Buff.affect(attacker, Ooze.class).set(20f);
		//		break;
		//}
		return damage;
	}

	/*public int affectDamage(Char ch, int damage) {
		damage = Math.max(damage - ch.drRoll(this), 0);
		return damage;
	}*/

	public void FX(Char ch, int cell, Callback attack) {
		final Char target = Actor.findChar(cell);
		int AMT = 5;
		switch (this) {
			default:
				attack.call();
				break;
			case SHARP:
			case PHYSICAL:
				if (Dungeon.level.adjacent(ch.pos, cell)) {
					attack.call();
				} else {
					((MissileSprite) ch.sprite.parent.recycle(MissileSprite.class)).
							reset(ch.pos, cell, new ThrowingKnife(), attack);
				}
				break;
			case MAGICAL:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.MAGIC_MISSILE,
						ch.sprite,
						cell,
						attack);
				Sample.INSTANCE.play( Assets.SND_ZAP );
				break;
			case DESTRUCTION:
				ch.sprite.parent.add(
						new Beam.DeathRay(ch.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
				attack.call();
				break;
			case FIRE:
				MagicMissile.boltFromChar(ch.sprite.parent,
						MagicMissile.FIRE,
						ch.sprite,
						cell,
						new Callback() {
							@Override
							public void call() {
								attack.call();
								if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
									target.sprite.emitter().burst(FlameParticle.STORM, AMT);
								}
							}
						});
				break;
			case WATER:
				MagicMissile.boltFromChar(ch.sprite.parent,
						MagicMissile.WATER_CONE,
						ch.sprite,
						cell,
						new Callback() {
							@Override
							public void call() {
								attack.call();
								if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
									target.sprite.emitter().burst(SewerLevel.WaterParticle.FACTORY, AMT);
								}
							}
						});
				break;
			case COLD:
				MagicMissile.boltFromChar(ch.sprite.parent,
						MagicMissile.FROST,
						ch.sprite,
						cell,
						new Callback() {
							@Override
							public void call() {
								attack.call();
								if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
									target.sprite.burst( 0xFF99CCFF, AMT);
								}
							}
						});
				Sample.INSTANCE.play(Assets.SND_ZAP);
				break;
			case EARTH:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.EARTH,
						ch.sprite,
						cell,
						attack);
				break;
			case GRASS:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.FOLIAGE_CONE,
						ch.sprite,
						cell,
						attack);
				break;
			case CONFUSION:
				MagicMissile.boltFromChar( ch.sprite.parent,
						MagicMissile.RAINBOW,
						ch.sprite,
						cell,
						attack);
				break;
			case TOXIC:
				MagicMissile.boltFromChar(ch.sprite.parent,
						MagicMissile.TOXIC_VENT,
						ch.sprite,
						cell,
						new Callback() {
							@Override
							public void call() {
								attack.call();
								if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
									target.sprite.emitter().burst(Speck.factory(Speck.BUBBLE_PURPLE), AMT);
								}
							}
						});
				break;
			case AIR: case LIGHT:
				ch.sprite.parent.add(
						new Beam.LightRay(ch.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
				if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
					new Flare(8, 16).color(0xFFFF66, true).show(target.sprite, 2f);
					if (target.properties().contains(Char.Property.UNDEAD) || target.properties().contains(Char.Property.DEMONIC)) {
						target.sprite.emitter().burst(ShadowParticle.UP, AMT);
					}
				}
				attack.call();
				break;
			case ACID:
				MagicMissile.boltFromChar(ch.sprite.parent,
						MagicMissile.ACID,
						ch.sprite,
						cell,
						new Callback() {
							@Override
							public void call() {
								attack.call();
								if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
									target.sprite.emitter().burst(Speck.factory(Speck.BUBBLE_GREEN), AMT);
								}
							}
						});
				break;
			case SHOCK:
				ch.sprite.parent.add(
						new Lightning(ch.pos, DungeonTilemap.raisedTileCenterToWorld(cell), null));//No callback as damaging after lightning anim finishes looks messy
				if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
					target.sprite.emitter().burst(SparkParticle.FACTORY, AMT);
				}
				attack.call();
				break;
			case DRAIN:
				ch.sprite.parent.add(
						new Lightning(ch.pos, DungeonTilemap.raisedTileCenterToWorld(cell), null, 0xFF0000));
				if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
					new Flare(8, 16).color(0xFF0000, true).show(target.sprite, 2f);
				}
				attack.call();
				break;
			case SPIRIT:
				MagicMissile.boltFromChar(ch.sprite.parent,
						MagicMissile.SHADOW,
						ch.sprite,
						cell,
						new Callback() {
							@Override
							public void call() {
								attack.call();
								if (Dungeon.hero.fieldOfView[cell] && (target) != null) {
									target.sprite.emitter().burst(ShadowParticle.CURSE, AMT);
								}
							}
						});
				break;
		}
	}

	Element(boolean magical) {
		this.magical = magical;
	}

	private boolean magical;

	public boolean isMagical() {
		return magical;
	}

	public String label() {
		return Messages.get(this, this.name());
	}
}
