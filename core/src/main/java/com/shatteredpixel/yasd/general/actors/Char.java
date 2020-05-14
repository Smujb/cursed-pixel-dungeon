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

package com.shatteredpixel.yasd.general.actors;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Adrenaline;
import com.shatteredpixel.yasd.general.actors.buffs.AdrenalineSurge;
import com.shatteredpixel.yasd.general.actors.buffs.Aggression;
import com.shatteredpixel.yasd.general.actors.buffs.ArcaneArmor;
import com.shatteredpixel.yasd.general.actors.buffs.Barkskin;
import com.shatteredpixel.yasd.general.actors.buffs.Barrier;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Bless;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Charm;
import com.shatteredpixel.yasd.general.actors.buffs.Chill;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.Doom;
import com.shatteredpixel.yasd.general.actors.buffs.Drowsy;
import com.shatteredpixel.yasd.general.actors.buffs.Drunk;
import com.shatteredpixel.yasd.general.actors.buffs.EarthImbue;
import com.shatteredpixel.yasd.general.actors.buffs.FireImbue;
import com.shatteredpixel.yasd.general.actors.buffs.Frost;
import com.shatteredpixel.yasd.general.actors.buffs.FrostImbue;
import com.shatteredpixel.yasd.general.actors.buffs.Haste;
import com.shatteredpixel.yasd.general.actors.buffs.Hex;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.Levitation;
import com.shatteredpixel.yasd.general.actors.buffs.LifeLink;
import com.shatteredpixel.yasd.general.actors.buffs.MagicalSleep;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.buffs.Momentum;
import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.actors.buffs.Preparation;
import com.shatteredpixel.yasd.general.actors.buffs.ShieldBuff;
import com.shatteredpixel.yasd.general.actors.buffs.Slow;
import com.shatteredpixel.yasd.general.actors.buffs.Speed;
import com.shatteredpixel.yasd.general.actors.buffs.Stamina;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.actors.buffs.Vulnerable;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.actors.buffs.Wet;
import com.shatteredpixel.yasd.general.actors.hero.Belongings;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.Surprise;
import com.shatteredpixel.yasd.general.effects.Wound;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.yasd.general.items.powers.BubbleShield;
import com.shatteredpixel.yasd.general.items.powers.Greed;
import com.shatteredpixel.yasd.general.items.rings.RingOfElements;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.general.items.wands.WandOfLivingEarth;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Blazing;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Blocking;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.levels.features.Door;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.GrimTrap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Earthroot;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.attack.AttackIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Char extends Actor {

	public int pos = 0;

	public CharSprite sprite;

	public int defenseSkill = 0;
	public int attackSkill = 0;
	public int noticeSkill = 0;
	public int sneakSkill = 0;

	public Belongings belongings = null;
	public int STR;
	//public boolean hasBelongings = false;

	protected int numTypes = 1;

	protected int type;

	public int HT;
	public int HP;

	protected float baseSpeed = 1;
	protected PathFinder.Path path;

	public int paralysed = 0;
	public boolean rooted = false;
	public boolean flying = false;
	public int invisible = 0;

	//these are relative to the hero
	public enum Alignment {
		ENEMY,
		NEUTRAL,
		ALLY
	}

	public Alignment alignment;

	public enum AttackType {
		NORMAL {
		},
		SPIN {
		},
		FURY {
		},
		BLOCK {
		},
		CRUSH;

		public int staminaCost() {
			switch (this) {
				case NORMAL: default:
					return 0;
				case SPIN:
				case CRUSH:
					return 3;
				case FURY:
					return 7;
				case BLOCK:
					return 10;
			}
		}

		public AttackIndicator indicator() {
			return new AttackIndicator() {
				{
					type = AttackType.this;
				}
			};
		}

		public boolean attackProc(Char attacker, Char defender, int damage, DamageSrc src) {
			if (attacker instanceof Hero) {
				((Hero) attacker).useStamina(staminaCost());
			}
			switch (this) {
				default:
					break;
				case SPIN:
					damage *= 0.5f;
					for (int pos : PathFinder.NEIGHBOURS8) {
						Char ch = Actor.findChar(attacker.pos+pos);
						if (ch != null && ch != defender) {
							ch.damage(damage, src);
						}
					}
					break;
				case FURY:
					//TODO
					damage *= 0.5f;
					break;
				case BLOCK:
					//TODO
					return true;
				case CRUSH:
					damage *= 1.2f;
					src.ignoreDefense();
					break;
			}
			defender.damage( damage, src );
			return true;
		}

	}

	public int viewDistance = 8;

	public boolean[] fieldOfView = null;

	private HashSet<Buff> buffs = new HashSet<>();

	public boolean isFlying() {
		return ((flying || buff(Levitation.class) != null)
				& buff(Paralysis.class) == null
				& buff(Vertigo.class) == null
				& buff(Frost.class) == null);
	}

	public boolean shoot(Char enemy, MissileWeapon wep) {
		return belongings.shoot(enemy, wep);
	}

	public int missingHP() {
		return HT - HP;
	}

	public float hpPercent() {
		return HP / (float) HT;
	}

	public float missingHPPercent() {
		return 1f - hpPercent();
	}

	public boolean hasBelongings() {
		return belongings != null;
	}

	public int STR() {
		int STR = this.STR;

		AdrenalineSurge buff = buff(AdrenalineSurge.class);
		if (buff != null) {
			STR += buff.boost();
		}

		return STR;
	}

	public Element elementalType() {
		return Element.PHYSICAL;
	}

	public boolean canAttack(Char enemy) {
		if (enemy == null || pos == enemy.pos) {
			return false;
		}

		//can always attack adjacent enemies
		if (Dungeon.level.adjacent(pos, enemy.pos)) {
			return true;
		}

		if (hasBelongings()) {
			KindOfWeapon wep = belongings.getWeapon();
			if (wep != null) {
				return wep.canReach(this, enemy.pos);
			}
		}
		return false;
	}

	public void updateHT(boolean boostHP) {
		int curHT = HT;

		//float multiplier = RingOfPower.HTMultiplier(this);
		//HT = Math.round(multiplier * HT);

		if (buff(ElixirOfMight.HTBoost.class) != null) {
			HT += buff(ElixirOfMight.HTBoost.class).boost();
		}

		if (boostHP) {
			HP += Math.max(HT - curHT, 0);
		}
		HP = Math.min(HP, HT);
	}

	@Override
	protected boolean act() {
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()) {
			fieldOfView = new boolean[Dungeon.level.length()];
		}
		Dungeon.level.updateFieldOfView(this, fieldOfView);
		return false;
	}

	public boolean canInteract(Char ch) {
		return Dungeon.level.adjacent( pos, ch.pos ) && ch.buff(Vertigo.class) == null;
	}

	//swaps places by default
	public boolean interact(Char ch) {

		//can't spawn places if one char has restricted movement
		if (rooted || ch.rooted || buff(Vertigo.class) != null || ch.buff(Vertigo.class) != null){
			return true;
		}

		//don't allow char to swap onto hazard unless they're flying
		//you can swap onto a hazard though, as you're not the one instigating the swap
		if (!Dungeon.level.passable(pos) && !ch.flying){
			return true;
		}

		//can't swap into a space without room
		if (!canOccupy(Dungeon.level, ch.pos)
				|| !ch.canOccupy(Dungeon.level, pos)) {
			return true;
		}

		int curPos = pos;

		moveSprite(pos, ch.pos);
		move(ch.pos);

		ch.sprite.move(ch.pos, curPos);
		ch.move(curPos);

		ch.spend(1 / ch.speed());
		ch.busy();

		return true;
	}

	protected boolean moveSprite(int from, int to) {

		if (sprite.isVisible() && (Dungeon.level.heroFOV[from] || Dungeon.level.heroFOV[to])) {
			sprite.move(from, to);
			return true;
		} else {
			sprite.turnTo(from, to);
			sprite.place(to);
			return true;
		}
	}

	protected static final String POS = "pos";
	protected static final String TAG_HP = "HP";
	protected static final String TAG_HT = "HT";
	protected static final String TAG_SHLD = "SHLD";
	protected static final String BUFFS = "buffs";
	protected static final String TYPE = "type";

	@Override
	public void storeInBundle( Bundle bundle) {

		super.storeInBundle(bundle);

		bundle.put(POS, pos);
		bundle.put(TAG_HP, HP);
		bundle.put(TAG_HT, HT);
		bundle.put(BUFFS, buffs);
		bundle.put(TYPE, type);

		if (hasBelongings()) {
			belongings.storeInBundle(bundle);
		}
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {

		super.restoreFromBundle(bundle);

		pos = bundle.getInt(POS);
		HP = bundle.getInt(TAG_HP);
		HT = bundle.getInt(TAG_HT);
		type = bundle.getInt(TYPE);

		for (Bundlable b : bundle.getCollection(BUFFS)) {
			if (b != null) {
				((Buff) b).attachTo(this);
			}
		}

		if (hasBelongings()) {
			belongings.restoreFromBundle(bundle);
		}

		//pre-0.7.0
	}

	public String name(){
		return Messages.get(this, "name");
	}

	public final boolean attack(Char enemy) {
		return attack(enemy, false);
	}

	//Temporary variable to ensure the next attack is the type wanted.
	public AttackType nextAttack = AttackType.NORMAL;

	public boolean attack(Char enemy, boolean guaranteed) {

		if (enemy == null || enemy == this) return false;

		boolean visibleFight = Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[enemy.pos];

		if (enemy.isInvulnerable(getClass())) {

			if (visibleFight) {
				enemy.sprite.showStatus( CharSprite.POSITIVE, Messages.get(this, "invulnerable") );

				Sample.INSTANCE.play(Assets.SND_MISS);
			}

			return false;

		} else if (hit(this, enemy) || guaranteed) {

			int dmg;
			Preparation prep = buff(Preparation.class);
			if (prep != null) {
				dmg = prep.damageRoll(this, enemy);
			} else {
				dmg = damageRoll();
			}

			if (hasBelongings()) {
				if (belongings.getWeapon() instanceof MissileWeapon) {//Missile Weapons are always equipped in slot 1
					dmg = belongings.getWeapon().damageRoll(this);
				}
			}
			DamageSrc src = defaultSrc();
			if (hasBelongings() && belongings.getWeapon() != null && belongings.getWeapon().breaksArmor(this)) {
				src.ignoreDefense();
			}
			if (dmg < 0) {
				dmg = 0;
			}
			dmg = attackProc(enemy, dmg);
			dmg = enemy.defenseProc(this, dmg);
			// If the enemy is already dead, interrupt the attack.
			// This matters as defence procs can sometimes inflict self-damage, such as armour glyphs.
			if (!enemy.isAlive()) {
				return true;
			}
			//Actually damage them.
			nextAttack.attackProc(this, enemy, dmg, src);

			if (Dungeon.hero.fieldOfView[enemy.pos] || Dungeon.hero.fieldOfView[pos]) {
				Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
			}

			if (buff(FireImbue.class) != null)
				buff(FireImbue.class).proc(enemy);
			if (buff(EarthImbue.class) != null)
				buff(EarthImbue.class).proc(enemy);
			if (buff(FrostImbue.class) != null)
				buff(FrostImbue.class).proc(enemy);

			enemy.sprite.bloodBurstA(sprite.center(), dmg);
			enemy.sprite.flash();

			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {

					Dungeon.fail(getClass());
					GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name())));

				} else if (this == Dungeon.hero) {
					GLog.i(Messages.capitalize(Messages.get(Char.class, "defeat", enemy.name())));
				}
			}

			return true;

		} else {

			if (visibleFight) {
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus(CharSprite.NEUTRAL, defense);

				Sample.INSTANCE.play(Assets.SND_MISS);
			}

			return false;

		}
	}

	public static int INFINITE_ACCURACY = 1_000_000;
	public static int INFINITE_EVASION = 1_000_000;

	public static boolean hit(Char attacker, Char defender) {
		float acuStat = attacker.attackSkill( defender );
		float defStat = defender.defenseSkill( attacker );

		//if accuracy or evasion are large enough, treat them as infinite.
		//note that infinite evasion beats infinite accuracy
		if (defStat >= INFINITE_EVASION){
			return false;
		} else if (acuStat >= INFINITE_ACCURACY){
			return true;
		}
		float acuRoll = Random.Float(acuStat);
		float defRoll = Random.Float(defStat);
		if (attacker.buff(  Hex.class) != null) acuRoll *= 0.8f;
		if (attacker.buff(Bless.class) != null) acuRoll *= 1.25f;
		if (defender.buff(  Hex.class) != null) defRoll *= 0.8f;
		if (defender.buff(Bless.class) != null) defRoll *= 1.25f;
		if (attacker.elementalType().isMagical()) {
			if (Dungeon.level.adjacent(attacker.pos, defender.pos)) {//Magical mobs have reduced accuracy at melee range.
				acuRoll /= 2;
			} else {
				acuRoll *= 2;
			}
		}
		if (defender instanceof Mob && ((Mob)defender).canBeSurpriseAttacked(attacker)) {
			Statistics.sneakAttacks++;
			Badges.validateRogueUnlock();
			if (defender.sprite.visible) {
				if (attacker.buff(Preparation.class) != null) {
					Wound.hit(defender);
				} else {
					Surprise.hit(defender);
				}
			}
			return true;
		}
		return acuRoll >= defRoll;
	}

	public void spendAndNext(float time) {
		spend(time);
		next();
	}

	public int attackSkill(Char target) {
		float accuracy = attackSkill;
		if (hasBelongings()) {
			accuracy = belongings.accuracyFactor(accuracy, target);
		}
		return affectAttackSkill(target, (int) accuracy);
	}

	public int defenseSkill(Char enemy) {
		float evasion = defenseSkill;
		if (hasBelongings()) {
			evasion = belongings.affectEvasion(evasion);
		}
		return affectDefenseSkill(enemy, Math.round(evasion));
	}

	public String defenseVerb() {
		return Messages.get(this, "def_verb");
	}

	public int drRoll(Element element) {
		int dr = 0;
		if (element.isMagical()) {
			if (hasBelongings()) {
				dr += belongings.magicalDR();
			}
		} else {
			if (hasBelongings()) {
				dr += belongings.drRoll();
			}
		}
		return affectDRRoll(element, dr);
	}

	public int damageRoll() {
		int damage;
		if (hasBelongings()) {
			damage = belongings.damageRoll();
		} else {
			damage = 1;
		}
		damage = affectDamageRoll(damage);
		return damage;
	}

	public int miscSlots() {
		return Constants.MISC_SLOTS;
	}

	//FIXME possibly migrate this to a seperate class and use static vars?
	//Used for central stuff that should apply to char and mob alike
	//##############################################################
	//
	protected final int affectDamageRoll(int damage) {
		Weakness weakness = buff(Weakness.class);
		if (weakness != null) {
			damage *= weakness.damageFactor();
		}
		if (buff(Greed.GreedBuff.class) != null) {
			damage *= 2;
		}
		return damage;
	}

	protected final int affectAttackSkill(Char target, int attackSkill) {
		Drunk drunk = buff(Drunk.class);
		if (drunk != null) {
			attackSkill *= drunk.accuracyFactor();
		}
		return attackSkill;
	}

	protected final int affectDefenseSkill(Char enemy, int evasion) {
		if (buff(Wet.class) != null) {
			evasion *= buff(Wet.class).evasionFactor();
		}
		if (paralysed > 0) {
			evasion /= 2;
		}
		Drunk drunk = buff(Drunk.class);
		if (drunk != null) {
			evasion *= drunk.evasionFactor();
		}
		return evasion;
	}

	protected final float affectNoticeSkill(Char enemy, float noticeSkill) {
		if (this.buff(MindVision.class) != null) {
			noticeSkill *= 2;
		}
		if (buff(Blindness.class) != null) {
			noticeSkill /= 2;
		}
		return noticeSkill;
	}

	protected final float affectSneakSkill(float sneakSkill) {
		if (this.buff(Invisibility.class) != null) {
			sneakSkill *= 2;
		}
		return sneakSkill;
	}

	protected final int affectDRRoll(Element element, int dr) {
		if (element.isMagical()) {
			if (buff(ArcaneArmor.class) != null){
				dr += Random.NormalIntRange(0, buff(ArcaneArmor.class).level());
			}
		} else {
			Barkskin bark = buff(Barkskin.class);
			if (bark != null) dr += Random.NormalIntRange(0, bark.level());

			Blocking.BlockBuff block = buff(Blocking.BlockBuff.class);
			if (block != null) dr += block.blockingRoll();
		}
		Vulnerable vulnerable = buff(Vulnerable.class);
		if (vulnerable != null) {
			dr *= vulnerable.defenseFactor();
		}
		return dr;
	}
	//End of central code.
	//##############################################################
	//

	public int attackProc( Char enemy, int damage ) {
		if (hasBelongings()) {
			if (elementalType().isMagical()) {
				damage = belongings.magicalAttackProc(enemy, damage);
			} else {
				damage = belongings.attackProc(enemy, damage);
			}
			if (!enemy.isAlive() && enemy == Dungeon.hero){
				Dungeon.fail(getClass());
				GLog.n( Messages.capitalize(Messages.get(Char.class, "kill", name())) );
			}
			return damage;
		}
		elementalType().attackProc(damage, this, enemy);
		return damage;
	}

	public int defenseProc(Char enemy, int damage) {
		if (hasBelongings()) {
			if (enemy.elementalType().isMagical()) {
				damage = belongings.magicalDefenseProc(enemy, damage);
			} else {
				damage = belongings.defenseProc(enemy, damage);
			}
		}
		if (elementalType().isMagical()) {
			Earthroot.Armor armor = buff(Earthroot.Armor.class);
			if (armor != null) {
				damage = armor.absorb(damage);
			}

			WandOfLivingEarth.RockArmor rockArmor = buff(WandOfLivingEarth.RockArmor.class);
			if (rockArmor != null) {
				damage = rockArmor.absorb(damage);
			}
		}
		elementalType().defenseProc(damage, enemy, this);
		return damage;
	}

	public float speed() {
		float speed = baseSpeed;
		if ( buff( Cripple.class ) != null ) speed /= 2f;
		if ( buff( Stamina.class ) != null) speed *= 1.5f;
		if ( buff( Adrenaline.class ) != null) speed *= 2f;
		if ( buff( Haste.class ) != null) speed *= 3f;
		if (hasBelongings()) {
			speed = belongings.affectSpeed(speed);
		}
		Momentum momentum = buff(Momentum.class);
		if (momentum != null) {
			//((HeroSprite)sprite).sprint( 1f + 0.05f*momentum.stacks());
			speed *= momentum.speedMultiplier();
		}
		return speed;
	}

	public float attackDelay() {
		if (hasBelongings()) {
			return belongings.attackDelay();
		} else {
			return 1f;
		}
	}

	//used so that buffs(Shieldbuff.class) isn't called every time unnecessarily
	private int cachedShield = 0;
	public boolean needsShieldUpdate = true;

	public int shielding(){
		if (!needsShieldUpdate){
			return cachedShield;
		}

		cachedShield = 0;
		for (ShieldBuff s : buffs(ShieldBuff.class)){
			cachedShield += s.shielding();
		}
		needsShieldUpdate = false;
		return cachedShield;
	}

	public boolean canSurpriseAttack(){
		if (hasBelongings()) {
			return belongings.canSurpriseAttack();
		}
		return true;
	}



	@Contract(" -> new")
	private DamageSrc defaultSrc() {
		return new DamageSrc(this.elementalType(), this);
	}

	public final void damage(int dmg,  Char ch) {
		damage(dmg, ch.defaultSrc());
	}

	public final void damage(int dmg,  Buff buff) {
		damage(dmg, buff.defaultSrc());
	}

	public final void damage(int dmg, Element element) {
		damage(dmg, new DamageSrc(element, null));
	}

	public void damage(int dmg,  DamageSrc src) {
		dmg = Math.max(0, dmg);

		if(isInvulnerable(src.getClass())){
			sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
			return;
		}

		BubbleShield.BubbleShieldBuff shield = buff(BubbleShield.BubbleShieldBuff.class);
		if ( shield != null ) {
			shield.detach();
			return;
		}

		if (!src.ignores()) {
			dmg = Math.max(dmg - drRoll(src.getElement()), 0);
		}
		if (this.buff(Drowsy.class) != null && dmg > 0) {
			Buff.detach(this, Drowsy.class);
			GLog.w(Messages.get(this, "pain_resist"));
		}
		if (hasBelongings()) {
			dmg = belongings.affectDamage(dmg, src);
		}

		if (!isAlive()) {
			die(src);
			return;
		}

		if (!(src.getCause() instanceof LifeLink) && buff(LifeLink.class) != null){
			HashSet<LifeLink> links = buffs(LifeLink.class);
			for (LifeLink link : links.toArray(new LifeLink[0])){
				if (Actor.findById(link.object) == null){
					links.remove(link);
					link.detach();
				}
			}
			dmg /= (links.size()+1);
			for (LifeLink link : links){
				Char ch = (Char)Actor.findById(link.object);
				ch.damage(dmg, new DamageSrc(Element.META, link));
				if (!ch.isAlive()){
					link.detach();
				}
			}
		}

		Terror t = buff(Terror.class);
		if (t != null){
			t.recover();
		}
		Charm c = buff(Charm.class);
		if (c != null){
			c.recover();
		}
		if (this.buff(Frost.class) != null){
			Buff.detach( this, Frost.class );
		}
		if (this.buff(MagicalSleep.class) != null){
			Buff.detach(this, MagicalSleep.class);
		}
		if (this.buff(Doom.class) != null && !isImmune(Doom.class)){
			dmg *= 2;
		}

		Class<?> srcClass = src.getClass();
		if (isImmune( srcClass )) {
			dmg = 0;
		}
		dmg = Math.round( dmg * resist( src.getElement() ));


		if (buff( Paralysis.class ) != null) {
			buff( Paralysis.class ).processDamage(dmg);
		}

		int shielded = dmg;
		if (!src.breaksShields()){
			for (ShieldBuff s : buffs(ShieldBuff.class)){
				dmg = s.absorbDamage(dmg);
				if (dmg == 0) break;
			}
		}
		shielded -= dmg;
		HP -= dmg;

		if (sprite != null) {
			sprite.showStatus(HP > HT / 2 ?
							CharSprite.WARNING :
							CharSprite.NEGATIVE,
					Integer.toString(dmg + shielded));
		}

		if (HP < 0) HP = 0;

		if (!isAlive()) {
			die( src );
		}
	}

	public void heal(int amount) {
		heal(amount, false, false);
	}

	public void heal(int amount, boolean shield) {
		heal(amount, shield, true);
	}

	public void heal(int amount, boolean shield, boolean display) {
		int healAmt = Math.min(missingHP(), amount);
		int shieldAmt = amount - healAmt;
		int total;
		if (shield) {
			total = shieldAmt + healAmt;
		} else {
			total = healAmt;
		}
		if (total > 0) {
			if (sprite != null) {
				sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
				if (display) {
					sprite.showStatus(CharSprite.POSITIVE, "+%dHP", total);
				}
			}
			HP += amount;
			if (shield && shieldAmt > 0) {
				Buff.affect(this, Barrier.class).setShield(shieldAmt);
			}
		}
	}

	public void destroy() {
		HP = 0;
		Actor.remove( this );
	}

	public void die( DamageSrc src ) {
		destroy();
		if (src.getCause() != Chasm.class) sprite.die();
	}

	public boolean isAlive() {
		if (HT <= 0) {
			HT = 1;
		}
		return HP > 0;
	}

	public void busy() {

	}

	@Override
	public void spend( float time ) {

		float timeScale = 1f;
		if (buff( Slow.class ) != null) {
			timeScale *= 0.5f;
			//slowed and chilled do not stack
		} else if (buff( Chill.class ) != null) {
			timeScale *= buff( Chill.class ).speedFactor();
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 2.0f;
		}

		super.spend( time / timeScale );
	}

	public synchronized HashSet<Buff> buffs() {
		return new HashSet<>(buffs);
	}

	@SuppressWarnings("unchecked")
	//returns all buffs assignable from the given buff class
	public synchronized <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<>();
		for (Buff b : buffs) {
			if (c.isInstance( b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}

	@SuppressWarnings("unchecked")
	//returns an instance of the specific buff class, if it exists. Not just assignable
	public synchronized  <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (b.getClass() == c) {
				return (T)b;
			}
		}
		return null;
	}

	public synchronized boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}

	public synchronized void add( Buff buff ) {

		buffs.add( buff );
		Actor.add( buff );

		if (sprite != null && buff.announced)
			switch(buff.type){
				case POSITIVE:
					sprite.showStatus(CharSprite.POSITIVE, buff.toString());
					break;
				case NEGATIVE:
					sprite.showStatus(CharSprite.NEGATIVE, buff.toString());
					break;
				case NEUTRAL: default:
					sprite.showStatus(CharSprite.NEUTRAL, buff.toString());
					break;
			}

	}

	public synchronized void remove( Buff buff ) {

		buffs.remove( buff );
		Actor.remove( buff );

	}

	public synchronized void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs( buffClass )) {
			remove( buff );
		}
	}

	@Override
	protected synchronized void onRemove() {
		for (Buff buff : buffs.toArray(new Buff[0])) {
			buff.detach();
		}
	}

	public synchronized void updateSpriteState() {
		for (Buff buff:buffs) {
			buff.fx( true );
		}
	}

	public boolean fieldOfView(int pos) {
		if (fieldOfView == null || Dungeon.level == null) {
			return false;
		} else if (fieldOfView.length != Dungeon.level.length()) {
			fieldOfView = new boolean[Dungeon.level.length()];
			return false;
		} else {
			return fieldOfView[pos];
		}
	}

	public final boolean notice( Char defender, boolean alreadySeen) {
		return Random.Float() < noticeChance(defender, alreadySeen);
	}

	public float noticeChance( Char defender, boolean alreadySeen) {
		if (Dungeon.level.distance(pos, defender.pos) < viewDistance) {
			float perception = (noticeSkill(defender)) / ((Dungeon.level.distance(pos, defender.pos)+1)/2f);
			if (!fieldOfView(defender.pos)) {
				perception /= 2f;
			}
			if (alreadySeen) {
				perception *= 4;
			}
			float stealth = defender.sneakSkill(this);
			//Enforced here so we don't get division by zero error
			if (stealth == 0) {
				return 0f;
			}
			return perception/(perception + stealth);
		} else {
			return 0f;
		}
	}

	public float noticeSkill(Char enemy) {
		float perception = this.noticeSkill;
		if (hasBelongings()) {
			perception = belongings.affectPerception(perception);
		}
		return affectNoticeSkill(enemy, perception);
	}

	public float sneakSkill(Char enemy) {
		float stealth = this.sneakSkill;
		if (hasBelongings()) {
			stealth = belongings.affectStealth(stealth);
		}
		return affectSneakSkill(stealth);
	}

	public void move( int step ) {
		Drunk drunk = buff(Drunk.class);
		if (Dungeon.level.adjacent( step, pos ) && (buff( Vertigo.class ) != null || (drunk != null && drunk.stumbleChance()))) {
			sprite.interruptMotion();
			int newPos = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			if (!(Dungeon.level.passable(newPos) || Dungeon.level.avoid(newPos))
					|| !canOccupy(Dungeon.level, newPos)
					|| Actor.findChar( newPos ) != null)
				return;
			else {
				sprite.move(pos, newPos);
				step = newPos;
			}
		}

		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			Door.leave( pos );
		}

		if (canOccupy(Dungeon.level, step)) {
			pos = step;
		}

		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.level.heroFOV[pos];
		}

		Dungeon.level.occupyCell(this );
	}

	public int distance( Char other ) {
		return Dungeon.level.distance( pos, other.pos );
	}

	public void onMotionComplete() {
		//Does nothing by default
		//The main actor thread already accounts for motion,
		// so calling next() here isn't necessary (see Actor.process)
	}

	public void onAttackComplete() {
		next();
	}

	public void onOperateComplete() {
		next();
	}

	public final boolean[] passableTerrain(Level level) {
		boolean[] passable = new boolean[level.length()];
		for (int i = 0; i < level.length(); i++) {
			passable[i] = canOccupy(level, i);
		}
		return passable;
	}

	public static boolean[] passableTerrain(Char ch, Level level) {
		boolean[] passable = new boolean[level.length()];
		for (int i = 0; i < level.length(); i++) {
			passable[i] = canOccupy(ch, level, i);
		}
		return passable;
	}

	@Contract("null, _, _ -> true")
	public static boolean canOccupy(@Nullable Char ch, Level level, int cell) {
		if (ch == null) {
			return true;
		} else {
			return ch.canOccupy(level, cell);
		}
	}

	public boolean canOccupy(Level level, int cell) {
		if (Char.hasProp(this, Property.LARGE)) {
			return level.openSpace(cell);
		}
		return true;
	}

	protected final HashMap<Element, Float> resistances = new HashMap<>();

	//returns percent effectiveness after resistances
	//TODO currently resistances reduce effectiveness by a static 50%, and do not stack.
	public float resist( Element effect ){
		HashMap<Element, Float> resists = new HashMap<>(resistances);
		for (Property p : properties()){
			for (Element e : p.resistances()) {
				resists.put(e, 0.5f);
			}
		}

		for (Buff b : buffs()){
			for (Element e : b.resistances()) {
				resists.put(e, 0.5f);
			}
		}

		float result = 1f;
		if (resists.containsKey(effect)) {
			result *= resists.get(effect);
		}

		if (effect.isMagical()) {
			result *= RingOfElements.resist(this);
		}
		return result;
	}

	protected final HashSet<Class> immunities = new HashSet<>();

	public boolean isImmune(Class effect ){
		HashSet<Class> immunes = new HashSet<>(immunities);
		for (Property p : properties()){
			immunes.addAll(p.immunities());
		}
		for (Buff b : buffs()){
			immunes.addAll(b.immunities());
		}

		for (Class c : immunes){
			if (c.isAssignableFrom(effect)){
				return true;
			}
		}
		if (hasBelongings()) {
			return belongings.isImmune(effect);
		} else {
			return false;
		}
	}

	//similar to isImmune, but only factors in damage.
	//Is used in AI decision-making
	public boolean isInvulnerable( Class effect ){
		return false;
	}

	protected HashSet<Property> properties = new HashSet<>();

	public HashSet<Property> properties() {
		return new HashSet<>(properties);
	}

	public enum Property{
		BOSS ( new HashSet<Element>(),
				new HashSet<Class>( Arrays.asList(Corruption.class, Aggression.class, Grim.class, GrimTrap.class, ScrollOfRetribution.class, ScrollOfPsionicBlast.class) )),

		MINIBOSS ( new HashSet<Element>(),
				new HashSet<Class>( Arrays.asList(Corruption.class) )),
		UNDEAD,
		DEMONIC,
		INORGANIC ( new HashSet<Element>(),
				new HashSet<Class>( Arrays.asList(Bleeding.class, ToxicGas.class, Poison.class) )),

		BLOB_IMMUNE ( new HashSet<Element>(),
				new HashSet<Class>( Arrays.asList(Blob.class) )),

		FIERY ( new HashSet<Element>( Arrays.asList(Element.FIRE)),
				new HashSet<Class>( Arrays.asList(Burning.class, Blazing.class))),

		ACIDIC ( new HashSet<Element>( Arrays.asList(Element.ACID)),
				new HashSet<Class>( Arrays.asList(Ooze.class))),

		ICY ( new HashSet<Element>( Arrays.asList(Element.COLD)),
				new HashSet<Class>( Arrays.asList(Frost.class, Chill.class))),

		ELECTRIC ( new HashSet<Element>( Arrays.asList(Element.SHOCK, Element.LIGHT)),
				new HashSet<Class>()),

		LARGE,
		WATERY(new HashSet<Element>(Arrays.asList(Element.WATER)), new HashSet<>(Arrays.asList(Wet.class))),
		IMMOVABLE,
		IGNORES_INVISIBLE;

		private HashSet<Element> resistances;
		private HashSet<Class> immunities;

		Property(){
			this(new HashSet<Element>(), new HashSet<Class>());
		}

		Property(HashSet<Element> resistances, HashSet<Class> immunities){
			this.resistances = resistances;
			this.immunities = immunities;
		}


		@Contract(" -> new")
		public HashSet<Element> resistances(){
			return new HashSet<>(resistances);
		}


		@Contract(" -> new")
		public HashSet<Class> immunities(){
			return new HashSet<>(immunities);
		}
	}

	@Contract(value = "null, _ -> false", pure = true)
	public static boolean hasProp(@Nullable Char ch, Property p){
		return (ch != null && ch.properties.contains(p));
	}

	public static class DamageSrc {
		private Object cause;
		private Element element;
		private boolean ignores = false;
		private boolean breakShields = false;

		public DamageSrc(Element element) {
			this(element, null);
		}

		public DamageSrc(Element element, Object source) {
			this.element = element;
			this.cause = source;
		}

		public Element getElement() {
			return element;
		}

		public Object getCause() {
			return cause;
		}

		public DamageSrc breakShields() {
			this.breakShields = true;
			return this;
		}

		boolean breaksShields() {
			return breakShields;
		}

		public DamageSrc ignoreDefense() {
			this.ignores = true;
			return this;
		}

		public boolean ignores() {
			return ignores;
		}
	}
}
