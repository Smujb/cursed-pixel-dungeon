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

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Barrier;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Doom;
import com.shatteredpixel.yasd.general.actors.buffs.LifeLink;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.powers.HeroicLeap;
import com.shatteredpixel.yasd.general.items.powers.MoltenEarth;
import com.shatteredpixel.yasd.general.items.powers.RaiseDead;
import com.shatteredpixel.yasd.general.items.powers.SmokeBomb;
import com.shatteredpixel.yasd.general.items.powers.SpectralBlades;
import com.shatteredpixel.yasd.general.items.quest.MetalShard;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.items.unused.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.general.levels.chapters.city.NewCityBossLevel;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.KingSprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;


public class DwarfKing extends Boss {

	{
		spriteClass = KingSprite.class;

		healthFactor = 1.3f;

		properties.add(Property.BOSS);

		rematchLevel = "dwarf_king";
	}

	private int phase = 1;
	private int summonsMade = 0;

	private float summonCooldown = 0;
	private float abilityCooldown = 0;
	private static final int MIN_COOLDOWN = 10;
	private static final int MAX_COOLDOWN = 14;

	private int lastAbility = 0;
	private static final int NONE = 0;
	private static final int LINK = 1;
	private static final int TELE = 2;

	private static final String PHASE = "phase";
	private static final String SUMMONS_MADE = "summons_made";

	private static final String SUMMON_CD = "summon_cd";
	private static final String ABILITY_CD = "ability_cd";
	private static final String LAST_ABILITY = "last_ability";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( PHASE, phase );
		bundle.put( SUMMONS_MADE, summonsMade );
		bundle.put( SUMMON_CD, summonCooldown );
		bundle.put( ABILITY_CD, abilityCooldown );
		bundle.put( LAST_ABILITY, lastAbility );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		phase = bundle.getInt(PHASE);
		summonsMade = bundle.getInt(SUMMONS_MADE);
		summonCooldown = bundle.getFloat(SUMMON_CD);
		abilityCooldown = bundle.getFloat(ABILITY_CD);
		lastAbility = bundle.getInt(LAST_ABILITY);

		if (phase == 2) properties.add(Property.IMMOVABLE);
	}

	@Override
	protected boolean act() {
		if (phase == 1) {

			if (summonCooldown <= 0 && summonSubject(3)) {
				summonsMade++;
				summonCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
			} else if (summonCooldown > 0) {
				summonCooldown--;
			}

			if (paralysed > 0){
				spend(TICK);
				return true;
			}

			if (abilityCooldown <= 0) {

				if (lastAbility == NONE) {
					//50/50 either ability
					lastAbility = Random.Int(2) == 0 ? LINK : TELE;
				} else if (lastAbility == LINK) {
					//more likely to use tele
					lastAbility = Random.Int(8) == 0 ? LINK : TELE;
				} else {
					//more likely to use link
					lastAbility = Random.Int(8) != 0 ? LINK : TELE;
				}

				if (lastAbility == LINK && lifeLinkSubject()) {
					abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
					spend(TICK);
					return true;
				} else if (teleportSubject()) {
					lastAbility = TELE;
					abilityCooldown += Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);
					spend(TICK);
					return true;
				}

			} else {
				abilityCooldown--;
			}

		} else if (phase == 2) {
			if (summonsMade < 4) {
				if (summonsMade == 0) {
					sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
					Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
					yell(Messages.get(this, "wave_1"));
				}
				summonSubject(3, DKGhoul.class);
				spend(3 * TICK);
				summonsMade++;
				return true;
			} else if (shielding() <= 200 && summonsMade < 8) {
				if (summonsMade == 4) {
					sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
					Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
					yell(Messages.get(this, "wave_2"));
				}
				if (summonsMade == 7) {
					summonSubject(3, Random.Int(2) == 0 ? DKMonk.class : DKWarlock.class);
				} else {
					summonSubject(3, DKGhoul.class);
				}
				summonsMade++;
				spend(TICK);
				return true;
			} else if (shielding() <= 100 && summonsMade < 12) {
				sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
				Sample.INSTANCE.play(Assets.Sounds.CHALLENGE);
				yell(Messages.get(this, "wave_3"));
				summonSubject(4, DKWarlock.class);
				summonSubject(4, DKMonk.class);
				summonSubject(4, DKGhoul.class);
				summonSubject(4, DKGhoul.class);
				summonsMade = 12;
				spend(TICK);
				return true;
			} else {
				if (summonSubject(3)) summonsMade++;
				spend(TICK);
				return true;
			}
		} else if (phase == 3 && buffs(Summoning.class).size() < 4) {
			if (summonSubject(3)) summonsMade++;

		}
		return super.act();
	}

	private boolean summonSubject( int delay ){
		//4th summon is always a monk or warlock, otherwise ghoul
		if (summonsMade % 4 == 3){
			return summonSubject( delay, Random.Int(2) == 0 ? DKMonk.class : DKWarlock.class );
		} else {
			return summonSubject( delay, DKGhoul.class );
		}
	}

	private boolean summonSubject( int delay, Class<?extends Mob> type ){
		Summoning s = new Summoning();
		s.pos = ((NewCityBossLevel)Dungeon.level).getSummoningPos();
		if (s.pos == -1) return false;
		s.summon = type;
		s.delay = delay;
		s.attachTo(this);
		return true;
	}

	private HashSet<Mob> getSubjects(){
		HashSet<Mob> subjects = new HashSet<>();
		for (Mob m : Dungeon.level.mobs){
			if (m.alignment == alignment && (m instanceof Ghoul || m instanceof Monk || m instanceof Warlock)){
				subjects.add(m);
			}
		}
		return subjects;
	}

	private boolean lifeLinkSubject(){
		Mob furthest = null;

		for (Mob m : getSubjects()){
			boolean alreadyLinked = false;
			for (LifeLink l : m.buffs(LifeLink.class)){
				if (l.object == id()) alreadyLinked = true;
			}
			if (!alreadyLinked) {
				if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
					furthest = m;
				}
			}
		}

		if (furthest != null) {
			Buff.append(furthest, LifeLink.class, 100f).object = id();
			Buff.append(this, LifeLink.class, 100f).object = furthest.id();
			yell(Messages.get(this, "lifelink_" + Random.IntRange(1, 2)));
			sprite.parent.add(new Beam.HealthRay(sprite.destinationCenter(), furthest.sprite.destinationCenter()));
			return true;

		}
		return false;
	}

	private boolean teleportSubject(){
		if (enemy == null) return false;

		Mob furthest = null;

		for (Mob m : getSubjects()){
			if (furthest == null || Dungeon.level.distance(pos, furthest.pos) < Dungeon.level.distance(pos, m.pos)){
				furthest = m;
			}
		}

		if (furthest != null){

			float bestDist;
			int bestPos = pos;

			Ballistica trajectory = new Ballistica(enemy.pos, pos, Ballistica.STOP_TARGET);
			int targetCell = trajectory.path.get(trajectory.dist+1);
			//if the position opposite the direction of the hero is open, go there
			if (Actor.findChar(targetCell) == null && !Dungeon.level.solid(targetCell)){
				bestPos = targetCell;

				//Otherwise go to the neighbour cell that's open and is furthest
			} else {
				bestDist = Dungeon.level.trueDistance(pos, enemy.pos);

				for (int i : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(pos+i) == null
							&& !Dungeon.level.solid(pos+i)
							&& Dungeon.level.trueDistance(pos+i, enemy.pos) > bestDist){
						bestPos = pos+i;
						bestDist = Dungeon.level.trueDistance(pos+i, enemy.pos);
					}
				}
			}

			Actor.add(new Pushing(this, pos, bestPos));
			pos = bestPos;

			//find closest cell that's adjacent to enemy, place subject there
			bestDist = Dungeon.level.trueDistance(enemy.pos, pos);
			bestPos = enemy.pos;
			for (int i : PathFinder.NEIGHBOURS8){
				if (Actor.findChar(enemy.pos+i) == null
						&& !Dungeon.level.solid(enemy.pos+i)
						&& Dungeon.level.trueDistance(enemy.pos+i, pos) < bestDist){
					bestPos = enemy.pos+i;
					bestDist = Dungeon.level.trueDistance(enemy.pos+i, pos);
				}
			}

			if (bestPos != enemy.pos) ScrollOfTeleportation.appear(furthest, bestPos);
			yell(Messages.get(this, "teleport_" + Random.IntRange(1, 2)));
			return true;
		}
		return false;
	}

	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			yell(Messages.get(this, "notice"));
		}
	}

	@Override
	public boolean isInvulnerable(Class effect) {
		return phase == 2 && effect != KingDamager.class || super.isInvulnerable(effect);
	}

	@Override
	public void damage(int dmg, DamageSrc src) {
		if (isInvulnerable(src.getClass())){
			super.damage(dmg, src);
		} else if (phase == 3 && !(src.getCause() instanceof Viscosity.DeferedDamage)){
			Viscosity.DeferedDamage deferred = Buff.affect( this, Viscosity.DeferedDamage.class );
			deferred.prolong( dmg );

			sprite.showStatus( CharSprite.WARNING, Messages.get(Viscosity.class, "deferred", dmg) );
			return;
		}
		int preHP = HP;
		super.damage(dmg, src);


		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg/3f);

		if (phase == 1) {
			int dmgTaken = preHP - HP;
			abilityCooldown -= dmgTaken/8f;
			summonCooldown -= dmgTaken/8f;
			if (HP <= HT/3) {
				HP = HT/3;
				sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
				ScrollOfTeleportation.appear(this, NewCityBossLevel.throne);
				properties.add(Property.IMMOVABLE);
				phase = 2;
				summonsMade = 0;
				sprite.idle();
				Buff.affect(this, DKBarrior.class).setShield(HT);
				for (Summoning s : buffs(Summoning.class)) {
					s.detach();
				}
				for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (m instanceof Ghoul || m instanceof Monk || m instanceof Warlock) {
						m.die(new DamageSrc(Element.META));
					}
				}
			}
		} else if (phase == 2 && shielding() == 0) {
			properties.remove(Property.IMMOVABLE);
			phase = 3;
			summonsMade = 1; //monk/warlock on 3rd summon
			sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
			Sample.INSTANCE.play( Assets.Sounds.CHALLENGE );
			yell(  Messages.get(this, "enraged", Dungeon.hero.name()) );
		} else if (phase == 3 && preHP > 20 && HP < 20){
			yell( Messages.get(this, "losing") );
		}
	}

	@Override
	public boolean isAlive() {
		return super.isAlive() || phase != 3;
	}


	@Override
	public void die(DamageSrc cause) {
		GameScene.bossSlain();

		super.die( cause );

		Item item;
		switch (Dungeon.hero.heroClass) {
			case MAGE:
				item = new MoltenEarth();
				break;
			case ROGUE:
				item = new SmokeBomb();
				break;
			case WARRIOR: default:
				item = new HeroicLeap();
				break;
			case HUNTRESS:
				item = new SpectralBlades();
				break;
			case PRIESTESS:
				item = new RaiseDead();
				break;
		}
		if (Dungeon.hero.belongings.getItem(item.getClass()) != null) {
			item = new MetalShard();
		}

		if (Dungeon.level.solid(pos)) {
			Heap h = Dungeon.level.heaps.get(pos);
			if (h != null) {
				for (Item i : h.items) {
					Dungeon.level.drop(i, pos + Dungeon.level.width());
				}
				h.destroy();
			}
			Dungeon.level.drop(item, pos + Dungeon.level.width()).sprite.drop(pos);
		} else {
			//if the king is on his throne, drop the toolkit below
			Dungeon.level.drop(item, pos).sprite.drop();
		}

		Badges.validateBossSlain();

		Dungeon.level.unseal();

		for (Mob m : getSubjects()){
			m.die(new DamageSrc(Element.META));
		}

		yell( Messages.get(this, "defeated") );
	}

	@Override
	public boolean isImmune(Class effect) {
		//immune to damage amplification from doomed in 2nd phase or later, but it can still be applied
		if (phase > 1 && effect == Doom.class && buff(Doom.class) != null ){
			return true;
		}
		return super.isImmune(effect);
	}

	public static class DKGhoul extends Ghoul {
		{
			state = HUNTING;
		}

		@Override
		protected boolean act() {
			partnerID = -2; //no partners
			return super.act();
		}
	}

	public static class DKMonk extends Monk {
		{
			state = HUNTING;
		}
	}

	public static class DKWarlock extends Warlock {
		{
			state = HUNTING;
		}
	}

	public static class Summoning extends Buff {

		private int delay;
		private int pos;
		private Class<?extends Mob> summon;

		private Emitter particles;

		public int getPos() {
			return pos;
		}

		@Override
		public boolean act() {
			delay--;

			if (delay <= 0){

				if (summon == DKWarlock.class){
					particles.burst(ShadowParticle.CURSE, 10);
					Sample.INSTANCE.play(Assets.Sounds.CURSED);
				} else if (summon == DKMonk.class){
					particles.burst(ElmoParticle.FACTORY, 10);
					Sample.INSTANCE.play(Assets.Sounds.BURNING);
				} else {
					particles.burst(Speck.factory(Speck.BONE), 10);
					Sample.INSTANCE.play(Assets.Sounds.BONES);
				}
				particles = null;

				if (Actor.findChar(pos) != null){
					ArrayList<Integer> candidates = new ArrayList<>();
					for (int i : PathFinder.NEIGHBOURS8){
						if (Dungeon.level.passable(pos+i) && Actor.findChar(pos+i) == null){
							candidates.add(pos+i);
						}
					}
					if (!candidates.isEmpty()){
						pos = Random.element(candidates);
					}
				}
				Char ch = Actor.findChar(pos);
				Mob m = Mob.create(summon);
				if (ch == null) {
					m.pos = pos;
                    GameScene.add(m);
					m.state = m.HUNTING;
					if (((DwarfKing)target).phase == 2){
						Buff.affect(m, KingDamager.class);
					}
				} else {
					ch.damage(m.damageRoll(), new DamageSrc(Element.SHADOW, m));
					if (((DwarfKing)target).phase == 2){
						target.damage(target.HT/12, new KingDamager());
					}
				}

				detach();
			}

			spend(TICK);
			return true;
		}

		@Override
		public void fx(boolean on) {
			if (on && particles == null) {
				particles = CellEmitter.get(pos);

				if (summon == DKWarlock.class){
					particles.pour(ShadowParticle.UP, 0.1f);
				} else if (summon == DKMonk.class){
					particles.pour(ElmoParticle.FACTORY, 0.1f);
				} else {
					particles.pour(Speck.factory(Speck.RATTLE), 0.1f);
				}

			} else if (!on && particles != null) {
				particles.on = false;
			}
		}

		private static final String DELAY = "delay";
		private static final String POS = "pos";
		private static final String SUMMON = "summon";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(DELAY, delay);
			bundle.put(POS, pos);
			bundle.put(SUMMON, summon);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			delay = bundle.getInt(DELAY);
			pos = bundle.getInt(POS);
			summon = bundle.getClass(SUMMON);
		}
	}

	public static class KingDamager extends Buff {

		@Override
		public boolean act() {
			if (target.alignment != Alignment.ENEMY){
				detach();
			}
			spend( TICK );
			return true;
		}

		@Override
		public void detach() {
			super.detach();
			for (Mob m : Dungeon.level.mobs){
				if (m instanceof DwarfKing){
					m.damage(m.HT/12, this);
				}
			}
		}
	}

	public static class DKBarrior extends Barrier {

		@Override
		public boolean act() {
			incShield();
			return super.act();
		}

		@Override
		public int icon() {
			return BuffIndicator.NONE;
		}
	}
}
