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

package com.shatteredpixel.yasd.general.actors.hero;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Bones;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.GamesInProgress;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Alchemy;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Barkskin;
import com.shatteredpixel.yasd.general.actors.buffs.Berserk;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Combo;
import com.shatteredpixel.yasd.general.actors.buffs.Focus;
import com.shatteredpixel.yasd.general.actors.buffs.Foresight;
import com.shatteredpixel.yasd.general.actors.buffs.Hunger;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.buffs.Momentum;
import com.shatteredpixel.yasd.general.actors.buffs.MpRegen;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Preparation;
import com.shatteredpixel.yasd.general.actors.buffs.Regeneration;
import com.shatteredpixel.yasd.general.actors.buffs.SnipersMark;
import com.shatteredpixel.yasd.general.actors.buffs.StaminaRegen;
import com.shatteredpixel.yasd.general.actors.buffs.Vertigo;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.CheckedCell;
import com.shatteredpixel.yasd.general.items.Ankh;
import com.shatteredpixel.yasd.general.items.Attackable;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Heap.Type;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.armor.glyphs.Viscosity;
import com.shatteredpixel.yasd.general.items.artifacts.AlchemistsToolkit;
import com.shatteredpixel.yasd.general.items.artifacts.EtherealChains;
import com.shatteredpixel.yasd.general.items.artifacts.HornOfPlenty;
import com.shatteredpixel.yasd.general.items.artifacts.TalismanOfForesight;
import com.shatteredpixel.yasd.general.items.artifacts.TimekeepersHourglass;
import com.shatteredpixel.yasd.general.items.keys.BronzeKey;
import com.shatteredpixel.yasd.general.items.keys.CrystalKey;
import com.shatteredpixel.yasd.general.items.keys.GoldenKey;
import com.shatteredpixel.yasd.general.items.keys.IronKey;
import com.shatteredpixel.yasd.general.items.keys.Key;
import com.shatteredpixel.yasd.general.items.keys.SkeletonKey;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.items.potions.PotionOfExperience;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.rings.RingOfAssault;
import com.shatteredpixel.yasd.general.items.rings.RingOfElements;
import com.shatteredpixel.yasd.general.items.rings.RingOfExecution;
import com.shatteredpixel.yasd.general.items.rings.RingOfFocus;
import com.shatteredpixel.yasd.general.items.rings.RingOfResilience;
import com.shatteredpixel.yasd.general.items.rings.RingOfSupport;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.items.weapon.SpiritBow;
import com.shatteredpixel.yasd.general.items.weapon.missiles.MissileWeapon;
import com.shatteredpixel.yasd.general.journal.Notes;
import com.shatteredpixel.yasd.general.levels.GrindLevel;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.levels.interactive.InteractiveArea;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.Trap;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Swiftthistle;
import com.shatteredpixel.yasd.general.scenes.AlchemyScene;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.ui.attack.AttackIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndHero;
import com.shatteredpixel.yasd.general.windows.WndTradeItem;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class Hero extends Char {

	{
		actPriority = HERO_PRIO;

		alignment = Alignment.ALLY;

		attackSkill = 10;
		defenseSkill = 4;
		immunities.add(Amok.class);
	}

	public enum HeroStat {
		EXECUTION {
			{
				baseBoost = 0.3f;
			}
		},
		FOCUS,
		RESILIENCE {
			{
				baseBoost = 0.4f;
			}
		},
		ASSAULT,
		SUPPORT {
			{
				baseBoost = 0.3f;
			}
		};

		protected float baseBoost = 0.35f;

		public String getName() {
			return Messages.get(HeroStat.class, name());
		}

		public float hpBoost(int curLevel) {
			return (float) (1 + (baseBoost * Math.pow(0.9f, Math.max(0, curLevel-1))));
		}

		public int colour() {
			switch (this) {
				case EXECUTION: default:
					return Constants.Colours.PURPLE;
				case FOCUS:
					return Constants.Colours.LIGHT_BLUE;
				case RESILIENCE:
					return Constants.Colours.PURE_GREEN;
				case ASSAULT:
					return Constants.Colours.PURE_RED;
				case SUPPORT:
					return Constants.Colours.YELLOW;
			}
		}
	}

	private int execution = 1;
	private int focus = 1;
	private int resilience = 1;
	private int assault = 1;
	private int support = 1;
	public int DistributionPoints = 0;

	private static final float TIME_TO_REST = 1f;
	private static final float TIME_TO_SEARCH = 2f;
	private static final float HUNGER_FOR_SEARCH = 6f;

	public HeroClass heroClass = HeroClass.ROGUE;
	public HeroSubClass subClass = HeroSubClass.NONE;


	private Attackable curItem = null;

	public Attackable curItem() {
		if (curItem == null) {
			if (belongings.miscs[0] instanceof Attackable) {
				curItem = (Attackable) belongings.miscs[0];
			} else {
				return null;
			}
		}
		if (belongings.contains((Item) curItem)) {
			return curItem;
		} else {
			ArrayList<Item> items = belongings.getAllSimilar((Item) curItem);
			if (items.isEmpty()) {
				return curItem = null;
			} else {
				return curItem = (Attackable) items.get(0);
			}
		}
	}

	public void setCurItem(Attackable item) {
		curItem = item;
	}

	public boolean ready = false;
	private boolean damageInterrupt = true;
	public HeroAction curAction = null;
	public HeroAction lastAction = null;

	private Char enemy;

	public boolean resting = false;

	public int lvl = 1;
	public int exp = 0;

	private int maxMP = 10;
	public int mp = maxMP;

	private int maxStamina = 10;
	public float stamina = maxStamina;

	public int HTBoost = 0;

	private ArrayList<Mob> visibleEnemies;

	//This list is maintained so that some logic checks can be skipped
	// for enemies we know we aren't seeing normally, resulting in better performance

	public ArrayList<Mob> mindVisionEnemies = new ArrayList<>();

	public Hero() {
		super();
		belongings = new Belongings(this);

		HP = HT = 20;

		updateMP();
		updateStamina();
		updateHT(true);

		visibleEnemies = new ArrayList<>();
	}

	public int levelToScaleFactor() {
		return lvl;
	}

	@Override
	public void updateHT(boolean boostHP) {
		int preHT = HT;
		HT = 40 + HTBoost;
		for (HeroStat stat : HeroStat.values()) {
			for (int i = 1; i < getStat(stat); i++) {
				HT *= stat.hpBoost(i);
			}
		}
		heal(HT - preHT);
		super.updateHT(boostHP);
	}

	private void updateMP() {
		int oldMax = maxMP;
		maxMP = Math.round(10 + lvl*0.8f + getFocus());
		mp += (maxMP - oldMax);
	}

	public int maxMP() {
		return maxMP;
	}

	public boolean useMP(int amount) {
		if (mp >= amount) {
			mp -= amount;
			return true;
		} else {
			return false;
		}
	}

	private void updateStamina() {
		maxStamina = 10;
	}

	public int maxStamina() {
		return maxStamina;
	}

	public boolean canUseStamina(float amount) {
		return stamina - amount > 0;
	}

	public boolean useStamina(float amount) {
		if (Dungeon.depth == 0) {
			return true;
		}
		if (canUseStamina(amount)) {
			stamina -= amount;
			return true;
		} else {
			return false;
		}
	}

	;

	public int getExecution() {
		return execution + RingOfExecution.statBonus(this);
	}

	public int getResilience() {
		return resilience + RingOfResilience.statBonus(this);
	}

	public int getFocus() {
		return focus + RingOfFocus.statBonus(this);
	}

	public int getAssault() {
		return assault + RingOfAssault.statBonus(this);
	}

	public int getSupport() {
		return support + RingOfSupport.statBonus(this);
	}

	public void setExecution(int execution) {
		this.execution = execution;
	}

	public void increaseExecution() {
		setExecution(execution + 1);
	}

	public void setResilience(int resilience) {
		this.resilience = resilience;
	}

	public void increaseResilience() {
		setResilience(resilience + 1);
	}

	public void setFocus(int focus) {
		this.focus = focus;
		updateMP();
	}

	public void increaseFocus() {
		setFocus(focus + 1);
	}

	public void setAssault(int assault) {
		this.assault = assault;
	}

	public void increaseAssault() {
		setAssault(assault + 1);
	}

	public void setSupport(int support) {
		this.support = support;
		updateHT(true);
	}

	public void increaseSupport() {
		setSupport(support + 1);
	}

	public void increaseStat(HeroStat stat) {
		updateHT(true);
		switch (stat) {
			case EXECUTION:
				increaseExecution();
				return;
			case FOCUS:
				increaseFocus();
				return;
			case RESILIENCE:
				increaseResilience();
				return;
			case ASSAULT:
				increaseAssault();
				return;
			case SUPPORT:
				increaseSupport();
				return;
		}
	}

	public int getStat(HeroStat stat) {
		int execution = getExecution();
		int focus = getFocus();
		int resilience = getResilience();
		int assult = getAssault();
		int support = getSupport();
		switch (stat) {
			case EXECUTION: default:
				return execution;
			case FOCUS:
				return focus;
			case RESILIENCE:
				return resilience;
			case ASSAULT:
				return assult;
			case SUPPORT:
				return support;
		}
	}
	public void increasePoints(int increase) {
		DistributionPoints += increase;
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				WndHero window = new WndHero();
				window.switchToAbilities();
				GameScene.show(window);
			}
		});
	}

	@Override
	public float resist(Element effect) {
		return super.resist(effect);
	}

	private static final String ATTACK = "attackSkill";
	private static final String DEFENSE = "evasion";
	private static final String STRENGTH = "STR";
	private static final String LEVEL = "lvl";
	private static final String EXPERIENCE = "exp";
	private static final String HTBOOST = "htboost";
	private static final String MANA = "mp";
	private static final String MAX_MANA = "max_mp";
	private static final String STAMINA = "stamina";
	private static final String MAX_STAMINA = "max_stamina";
	private static final String POWER = "power";
	private static final String FOCUS = "focus";
	private static final String PERCEPTION = "expertise";
	private static final String EVASION = "combatskill";
	private static final String ATTUNEMENT = "attunement";
	private static final String DISTRIBUTIONPOINTS = "distribution-points";
	private static final String CUR_ITEM = "cur-item";

	@Override
	public void storeInBundle(Bundle bundle) {
		heroClass.storeInBundle(bundle);
		subClass.storeInBundle(bundle);

		bundle.put(ATTACK, attackSkill);
		bundle.put(DEFENSE, defenseSkill);

		bundle.put(LEVEL, lvl);
		bundle.put(EXPERIENCE, exp);

		bundle.put(HTBOOST, HTBoost);

		bundle.put(MANA, mp);
		bundle.put(MAX_MANA, maxMP);
		bundle.put(STAMINA, stamina);
		bundle.put(MAX_STAMINA, maxStamina);

		//Hero stats
		bundle.put(POWER, execution);
		bundle.put(FOCUS, focus);
		bundle.put(PERCEPTION, resilience);
		bundle.put(EVASION, assault);
		bundle.put(ATTUNEMENT, support);
		bundle.put(DISTRIBUTIONPOINTS, DistributionPoints);

		bundle.put( CUR_ITEM, (Bundlable) curItem);
		super.storeInBundle(bundle);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		heroClass = HeroClass.restoreInBundle(bundle);
		subClass = HeroSubClass.restoreInBundle(bundle);

		attackSkill = bundle.getInt(ATTACK);
		defenseSkill = bundle.getInt(DEFENSE);

		lvl = bundle.getInt(LEVEL);
		exp = bundle.getInt(EXPERIENCE);

		HTBoost = bundle.getInt(HTBOOST);

		mp = bundle.getInt(MANA);
		maxMP = bundle.getInt(MAX_MANA);
		stamina = bundle.getInt(STAMINA);
		maxStamina = bundle.getInt(MAX_STAMINA);

		//Hero stats
		execution = bundle.getInt(POWER);
		focus = bundle.getInt(FOCUS);
		resilience = bundle.getInt(PERCEPTION);
		assault = bundle.getInt(EVASION);
		support = bundle.getInt(ATTUNEMENT);
		DistributionPoints = bundle.getInt(DISTRIBUTIONPOINTS);

		if (bundle.contains(CUR_ITEM)) {
			curItem = (Attackable) bundle.get(CUR_ITEM);
		}
		super.restoreFromBundle(bundle);
	}

	@Override
	public int miscSlots() {
		int slots = super.miscSlots();
		if (heroClass == HeroClass.ROGUE) {
			slots++;
		}
		return slots;
	}

	@Override
	public float sneakSkill(Char enemy) {
		sneakSkill = 9 + (int) (lvl * 0.8f) + getExecution();
		return super.sneakSkill(enemy);
	}

	@Override
	public float noticeSkill(Char enemy) {
		noticeSkill = 4 + (int) (lvl * 0.8f) + getAssault();
		return super.noticeSkill(enemy);
	}

	public static void preview(GamesInProgress.Info info, Bundle bundle) {
		info.level = bundle.getInt(LEVEL);
		info.str = bundle.getInt(STRENGTH);
		info.exp = bundle.getInt(EXPERIENCE);
		info.hp = bundle.getInt(Char.TAG_HP);
		info.ht = bundle.getInt(Char.TAG_HT);
		info.shld = bundle.getInt(Char.TAG_SHLD);
		info.heroClass = HeroClass.restoreInBundle(bundle);
		info.subClass = HeroSubClass.restoreInBundle(bundle);
	}

	public String className() {
		return subClass == null || subClass == HeroSubClass.NONE ? heroClass.title() : subClass.title();
	}

	@Override
	public String name() {
		return className();
	}

	@Override
	public void hitSound(float pitch) {
		KindOfWeapon weapon = belongings.getWeapon();
		if ( weapon != null ){
			weapon.hitSound(pitch);
		} /*else if (RingOfForce.getBuffedBonus(this, RingOfForce.Force.class) > 0) {
			//pitch deepens by 2.5% (additive) per point of strength, down to 75%
			super.hitSound( pitch * GameMath.gate( 0.75f, 1.25f - 0.025f*STR(), 1f) );
		} */else {
			super.hitSound(pitch * 1.1f);
		}
	}

	@Override
	public boolean blockSound(float pitch) {
		KindOfWeapon weapon = belongings.getWeapon();
		if ( weapon != null && weapon.defenseFactor(this) >= 4 ){
			Sample.INSTANCE.play( Assets.Sounds.HIT_PARRY, 1, pitch);
			return true;
		}
		return super.blockSound(pitch);
	}

	public void live() {
		Buff.affect(this, Regeneration.class);
		Buff.affect(this, Hunger.class);
		Buff.affect(this, MpRegen.class);
		Buff.affect(this, StaminaRegen.class);
	}

	//TODO rework hero sprites
	public int tier() {
		return 6;
	}

	@Override
	public int attackSkill(Char target) {
		attackSkill = 9 + (int) (lvl * 0.8f) + getResilience();
		return super.attackSkill(target);
	}

	@Override
	public int defenseSkill(Char enemy) {
		defenseSkill = 3 + (int) (lvl * 0.8f) + getSupport();
		return super.defenseSkill(enemy);
	}

	@Override
	public void spend(float time) {
		justMoved = false;
		TimekeepersHourglass.timeFreeze freeze = buff(TimekeepersHourglass.timeFreeze.class);
		if (freeze != null) {
			freeze.processTime(time);
			return;
		}

		Swiftthistle.TimeBubble bubble = buff(Swiftthistle.TimeBubble.class);
		if (bubble != null) {
			bubble.processTime(time);
			return;
		}

		super.spend(time);
	}

	@Override
	public void spendAndNext(float time) {
		busy();
		super.spendAndNext(time);
	}

	@Override
	public boolean act() {
		com.shatteredpixel.yasd.general.actors.buffs.Focus f = buff(Focus.class);
		if (subClass == HeroSubClass.BRAWLER) {
			Buff.affect(this, Focus.class);
		} else if (f != null) {
			f.detach();
		}

		if (subClass == HeroSubClass.ASSASSIN && Preparation.canAttatch(this) && buff(Preparation.class) == null) {
			Buff.affect(this, Preparation.class);
		}

		//calls to dungeon.observe will also update hero's local FOV.
		fieldOfView = Dungeon.level.heroFOV;

		if (!ready) {
			//do a full observe (including fog update) if not resting.
			if (!resting || buff(MindVision.class) != null || buff(Awareness.class) != null) {
				Dungeon.observe();
			} else {
				//otherwise just directly re-calculate FOV
				Dungeon.level.updateFieldOfView(this, fieldOfView);
			}
		}

		checkVisibleMobs();
		BuffIndicator.refreshHero();

		if (paralysed > 0) {

			curAction = null;

			spendAndNext(TICK);
			return false;
		}

		boolean actResult;
		if (curAction == null) {

			if (resting) {
				spend(TIME_TO_REST);
				next();
			} else {
				ready();
			}

			actResult = false;

		} else {

			resting = false;

			ready = false;

			if (curAction instanceof HeroAction.Move) {
				actResult = actMove((HeroAction.Move) curAction);

			} else if (curAction instanceof HeroAction.Interact) {
				actResult = actInteract((HeroAction.Interact) curAction);

			} else if (curAction instanceof HeroAction.Buy) {
				actResult = actBuy((HeroAction.Buy) curAction);

			} else if (curAction instanceof HeroAction.PickUp) {
				actResult = actPickUp((HeroAction.PickUp) curAction);

			} else if (curAction instanceof HeroAction.OpenChest) {
				actResult = actOpenChest((HeroAction.OpenChest) curAction);

			} else if (curAction instanceof HeroAction.Unlock) {
				actResult = actUnlock((HeroAction.Unlock) curAction);

			} else if (curAction instanceof HeroAction.Attack) {
				actResult = actAttack((HeroAction.Attack) curAction);

			} else if (curAction instanceof HeroAction.Alchemy) {
				actResult = actAlchemy((HeroAction.Alchemy) curAction);

			} else if (curAction instanceof HeroAction.InteractCell) {
				actResult = actInteractCell((HeroAction.InteractCell) curAction);

			} else {
				actResult = false;
			}
		}

		if (subClass == HeroSubClass.WARDEN && Dungeon.level.getTerrain(pos) == Terrain.FURROWED_GRASS) {
			Buff.affect(this, Barkskin.class).set(lvl + 5, 1);
		}

		return actResult;
	}

	@Override
	public int level() {
		return lvl;
	}

	@Override
	public void busy() {
		ready = false;
	}

	public void ready() {
		if (sprite.looping()) sprite.idle();
		curAction = null;
		damageInterrupt = true;
		ready = true;

		AttackIndicator.updateStates();

		GameScene.ready();
		Buff.detach(buff(Shield.Parry.class));
	}

	public void interrupt() {
		if (isAlive() && curAction != null &&
				((curAction instanceof HeroAction.Move && curAction.dst != pos) ||
						(curAction instanceof HeroAction.Ascend || curAction instanceof HeroAction.Descend))) {
			lastAction = curAction;
		}
		curAction = null;
		GameScene.resetKeyHold();
	}

	@Override
	public void onMotionComplete() {
		GameScene.checkKeyHold();
	}

	public void resume() {
		curAction = lastAction;
		lastAction = null;
		damageInterrupt = false;
		next();
	}

	private boolean actMove(HeroAction.Move action) {

		if (getCloser(action.dst)) {
			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actInteractCell(HeroAction.InteractCell action) {
		int dst = action.dst;
		InteractiveArea area = action.area;
		if (area.posInside(Dungeon.level, pos)) {
			area.interact(this);
			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}

	}

	private boolean actInteract(HeroAction.Interact action) {

		Char ch = action.ch;

		if (ch.canInteract(this)) {

			ready();
			sprite.turnTo(pos, ch.pos);
			return ch.interact(this);

		} else {

			if (fieldOfView[ch.pos] && getCloser(ch.pos)) {

				return true;

			} else {
				ready();
				return false;
			}

		}
	}

	private boolean actBuy(@NotNull HeroAction.Buy action) {
		int dst = action.dst;
		//May change back, but I think making it consistent is a good move
		//if (pos == dst || Dungeon.level.adjacent( pos, dst )) {
		if (pos == dst) {

			ready();

			Heap heap = Dungeon.level.heaps.get(dst);
			if (heap != null && heap.type == Type.FOR_SALE && heap.size() == 1) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show( new WndTradeItem( heap ) );
					}
				});
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAlchemy(@NotNull HeroAction.Alchemy action) {
		int dst = action.dst;
		if (Dungeon.level.distance(dst, pos) <= 1) {

			ready();

			AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
			if (kit != null && kit.isCursed()) {
				GLog.warning(Messages.get(AlchemistsToolkit.class, "cursed"));
				return false;
			}

			Alchemy alch = (Alchemy) Dungeon.level.blobs.get(Alchemy.class);
			//TODO logic for a well having dried up?
			if (alch != null) {
				Alchemy.alchPos = dst;
				AlchemyScene.setProvider(alch);
			}
			CPDGame.switchScene(AlchemyScene.class);
			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actPickUp(@NotNull HeroAction.PickUp action) {
		int dst = action.dst;
		if (pos == dst) {

			Heap heap = Dungeon.level.heaps.get(pos);
			if (heap != null) {
				Item item = heap.peek();
				if (item.doPickUp(this)) {
					heap.pickUp();

					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof Key) {
						//Do Nothing
					} else {

						boolean important =
								(item instanceof ScrollOfUpgrade && ((Scroll) item).isKnown()) ||
										(item instanceof PotionOfExperience && ((Potion) item).isKnown());
						if (important) {
							GLog.positive(Messages.get(this, "you_now_have", item.name()));
						} else {
							GLog.info(Messages.get(this, "you_now_have", item.name()));
						}
					}

					curAction = null;
				} else {
					if (item instanceof Dewdrop
							|| item instanceof TimekeepersHourglass.sandBag
							|| item instanceof Key) {
						//Do Nothing
					} else {
						GLog.newLine();
						GLog.negative(Messages.get(this, "you_cant_have", item.name()));
					}

					heap.sprite.drop();
					ready();
				}
			} else {
				ready();
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actOpenChest(@NotNull HeroAction.OpenChest action) {
		int dst = action.dst;
		if (Dungeon.level.adjacent(pos, dst) || pos == dst) {

			Heap heap = Dungeon.level.heaps.get(dst);
			if (heap != null && (heap.type != Type.HEAP && heap.type != Type.FOR_SALE)) {

				if ((heap.type == Type.LOCKED_CHEST && Notes.keyCount(new GoldenKey(Dungeon.key)) < 1)
						|| (heap.type == Type.CRYSTAL_CHEST && Notes.keyCount(new CrystalKey(Dungeon.key)) < 1)) {

					GLog.warning(Messages.get(this, "locked_chest"));
					ready();
					return false;

				}

				switch (heap.type) {
					case TOMB:
						Sample.INSTANCE.play(Assets.Sounds.TOMB);
						Camera.main.shake(1, 0.5f);
						break;
					case SKELETON:
					case REMAINS:
						break;
					default:
						Sample.INSTANCE.play(Assets.Sounds.UNLOCK);
				}

				sprite.operate(dst);

			} else {
				ready();
			}

			return false;

		} else if (getCloser(dst)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actUnlock(@NotNull HeroAction.Unlock action) {
		int doorCell = action.dst;
		if (Dungeon.level.adjacent(pos, doorCell)) {

			boolean hasKey = false;
			KindOfTerrain door = Dungeon.level.getTerrain(doorCell);

			if (door == Terrain.LOCKED_DOOR
					&& Notes.keyCount(new IronKey(Dungeon.key)) > 0) {

				hasKey = true;

			} else if (door == Terrain.LOCKED_EXIT
					&& Notes.keyCount(new SkeletonKey(Dungeon.key)) > 0) {

				hasKey = true;

			} else if (door == Terrain.BRONZE_LOCKED_DOOR
					&& Notes.keyCount(new BronzeKey(Dungeon.key)) > 0) {
				hasKey = true;
			}

			if (hasKey) {

				sprite.operate(doorCell);

				Sample.INSTANCE.play(Assets.Sounds.UNLOCK);

			} else {
				GLog.warning(Messages.get(this, "locked_door"));
				ready();
			}

			return false;

		} else if (getCloser(doorCell)) {

			return true;

		} else {
			ready();
			return false;
		}
	}

	private boolean actAttack(@NotNull HeroAction.Attack action) {
		if (doAttack(action.target)) {
			return false;
		} else {

			if (fieldOfView[enemy.pos] && getCloser(enemy.pos)) {

				return true;

			} else {
				ready();
				return false;
			}
		}
	}

	public boolean doAttack(Char enemy) {
		this.enemy = enemy;
		StaminaRegen.regen = false;
		if (enemy.isAlive() && canAttack(enemy) && !isCharmedBy(enemy) && belongings.getWeapon() != null) {
			belongings.getWeapon().doAttack(this, enemy);
			return true;
		}
		return false;
	}

	public Char enemy() {
		return enemy;
	}

	public void rest(boolean fullRest) {
		spendAndNext(TIME_TO_REST);
		if (!fullRest) {
			sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "wait"));
		}
		resting = fullRest;
	}

	@Override
	public int attackProc(final Char enemy, int damage) {
		KindOfWeapon wep = belongings.getWeapon();
		damage = super.attackProc(enemy, damage);
		if (subClass == HeroSubClass.SNIPER) {
			if (wep instanceof MissileWeapon && !(wep instanceof SpiritBow.SpiritArrow)) {
				Actor.add(new Actor() {

					{
						actPriority = VFX_PRIO;
					}

					@Override
					protected boolean act() {
						if (enemy.isAlive()) {
							Buff.prolong(Hero.this, SnipersMark.class, SnipersMark.DURATION).object = enemy.id();
						}
						Actor.remove(this);
						return true;
					}
				});
			}
		}


		return damage;
	}

	@Override
	public int defenseProc(@NotNull Char enemy, int damage) {

		if (enemy.elementalType().isMagical()) {
			damage *= RingOfElements.resist(enemy);
		}

		if (damage > 0 && subClass == HeroSubClass.BERSERKER) {
			Berserk berserk = Buff.affect(this, Berserk.class);
			berserk.damage(damage);
		}

		damage = super.defenseProc(enemy, damage);

		//damageMorale(damage);

		return damage;
	}

	private void processShake(int dmg) {
		float shake;
		if (dmg <= 0) {
			return;
		}
		int effectiveHP = Math.max(HT / 2, HP);
		shake = ((float) dmg / (float) effectiveHP) * 4f;

		if (shake > 0) {
			if (shake > 0.5f) {
				float divisor = 3 + 12 * ((HP + shielding()) / (float) (HT + shielding()));
				GameScene.flash((int) (0xFF / divisor) << 16);
				if (isAlive()) {
					if (shake >= 1/3f) {
						Sample.INSTANCE.play(Assets.Sounds.HEALTH_CRITICAL, 1/3f + shake * 2f);
					} else {
						Sample.INSTANCE.play(Assets.Sounds.HEALTH_WARN, shake * 2f);
					}
				}
			}
			CPDGame.shake(shake);
		}
	}

	@Override
	public void damage(int dmg, @NotNull DamageSrc src) {
		int preHP = HP;
		if (!(src.getCause() instanceof Hunger || src.getCause() instanceof Viscosity.DeferedDamage) && damageInterrupt) {
			interrupt();
			resting = false;
		}

		super.damage(dmg, src);

		int postHP = HP;
		//Ensures that the damage actually taken is what is measured, not the number given initially.
		int damageTaken = preHP - postHP;
		if (!src.ignores() && isAlive()) {
			processShake(damageTaken);
		}
	}

	public void checkVisibleMobs() {
		ArrayList<Mob> visible = new ArrayList<>();

		boolean newMob = false;

		Mob target = null;
		for (Mob m : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (fieldOfView[m.pos] && m.alignment == Alignment.ENEMY) {
				visible.add(m);
				if (!visibleEnemies.contains(m)) {
					newMob = true;
				}

				if (!mindVisionEnemies.contains(m) && QuickSlotButton.autoAim(m) != -1) {
					if (target == null) {
						target = m;
					} else if (distance(target) > distance(m)) {
						target = m;
					}
				}
			}
		}

		Char lastTarget = QuickSlotButton.lastTarget;
		if (target != null && (lastTarget == null ||
				!lastTarget.isAlive() ||
				!fieldOfView[lastTarget.pos]) ||
				(lastTarget instanceof WandOfWarding.Ward && mindVisionEnemies.contains(lastTarget))) {
			QuickSlotButton.target(target);
		}

		if (newMob) {
			interrupt();
			if (resting){
				Dungeon.observe();
				resting = false;
			}
		}

		visibleEnemies = visible;
	}

	public int visibleEnemies() {
		return visibleEnemies.size();
	}

	public Mob visibleEnemy(int index) {
		return visibleEnemies.get(index % visibleEnemies.size());
	}

	private boolean walkingToVisibleTrapInFog = false;

	//FIXME this is a fairly crude way to track this, really it would be nice to have a short
	//history of hero actions
	public boolean justMoved = false;

	protected boolean getCloser(final int target) {

		if (target == pos)
			return false;

		if (rooted) {
			Camera.main.shake(1, 1f);
			return false;
		}

		int step = -1;

		if (Dungeon.level.adjacent(pos, target)) {

			path = null;

			if (Actor.findChar(target) == null) {
				if (Dungeon.level.pit(target) && !flying && !Dungeon.level.solid(target)) {
					if (!Chasm.jumpConfirmed) {
						Chasm.heroJump(this);
						interrupt();
					} else {
						Chasm.heroFall(target);
					}
					return false;
				}
				if (Dungeon.level.passable(target) || Dungeon.level.avoid(target)) {
					step = target;
				}
				if (walkingToVisibleTrapInFog
						&& Dungeon.level.hasTrap(target)
						&& Dungeon.level.trap(target).visible) {
					return false;
				}
			}

		} else {

			boolean newPath = false;
			if (path == null || path.isEmpty() || !Dungeon.level.adjacent(pos, path.getFirst()))
				newPath = true;
			else if (path.getLast() != target)
				newPath = true;
			else {
				if (!Dungeon.level.passable(path.get(0)) || Actor.findChar(path.get(0)) != null) {
					newPath = true;
				}
			}

			if (newPath) {

				int len = Dungeon.level.length();
				boolean[] p = Dungeon.level.passable();
				boolean[] v = Dungeon.level.visited;
				boolean[] m = Dungeon.level.mapped;
				boolean[] passable = new boolean[len];
				for (int i = 0; i < len; i++) {
					passable[i] = p[i] && (v[i] || m[i]);
				}

				PathFinder.Path newpath = Dungeon.findPath(this, target, passable, fieldOfView, true);
				if (newpath != null && path != null && newpath.size() > 2 * path.size()) {
					path = null;
				} else {
					path = newpath;
				}
			}

			if (path == null) return false;
			step = path.removeFirst();

		}

		if (step != -1) {

			float speed = speed();
			if (Dungeon.isChallenged(Challenges.COLLAPSING_FLOOR) & (Dungeon.level.getTerrain(pos) == Terrain.EMPTY || Dungeon.level.getTerrain(pos) == Terrain.EMPTY_SP || Dungeon.level.getTerrain(pos) == Terrain.EMBERS)) {
				if (CPDGame.scene() instanceof GameScene) {
					if (!isFlying()) {
						Dungeon.level.set(pos, Terrain.CHASM);
					}
					GameScene.updateMap(pos);
					if (Dungeon.level.heroFOV[pos]) Dungeon.observe();
				}
			}

			sprite.move(pos, step);
			move(step);

			spend(1 / speed);

			justMoved = true;

			search(false);

			if (subClass == HeroSubClass.FREERUNNER) {
				Buff.affect(this, Momentum.class).gainStack();
			}

			//FIXME this is a fairly sloppy fix for a crash involving pitfall traps.
			//really there should be a way for traps to specify whether action should continue or
			//not when they are pressed.
			return LevelHandler.mode() != LevelHandler.Mode.FALL;

		} else {

			return false;

		}

	}

	public boolean handle(int cell) {

		if (cell == -1) {
			return false;
		}

		Char ch;
		Heap heap;
		InteractiveArea area = Dungeon.level.findArea(cell);
		//TODO: replace this with an InteractiveArea
		if (Dungeon.level.getTerrain(cell) == Terrain.ALCHEMY && cell != pos) {

			curAction = new HeroAction.Alchemy(cell);

		} else if (fieldOfView[cell] && (ch = Actor.findChar(cell)) instanceof Mob) {

			if (ch.alignment != Alignment.ENEMY && ch.buff(Amok.class) == null) {
				curAction = new HeroAction.Interact(ch);
			} else {
				curAction = new HeroAction.Attack(ch);
			}

		} else if ((heap = Dungeon.level.heaps.get(cell)) != null
				//moving to an item doesn't auto-pickup when enemies are near...
				&& (visibleEnemies.size() == 0 || cell == pos ||
				//...but only for standard heaps, chests and similar open as normal.
				(heap.type != Type.HEAP && heap.type != Type.FOR_SALE))) {

			switch (heap.type) {
				case HEAP:
					curAction = new HeroAction.PickUp(cell);
					break;
				case FOR_SALE:
					curAction = heap.size() == 1 && heap.peek().price() > 0 ?
							new HeroAction.Buy(cell) :
							new HeroAction.PickUp(cell);
					break;
				default:
					curAction = new HeroAction.OpenChest(cell);
			}

		} else if (Dungeon.level.getTerrain(cell) == Terrain.LOCKED_DOOR || Dungeon.level.getTerrain(cell) == Terrain.LOCKED_EXIT || Dungeon.level.getTerrain(cell) == Terrain.BRONZE_LOCKED_DOOR) {

			curAction = new HeroAction.Unlock(cell);

		} else if (area != null) {

			curAction = new HeroAction.InteractCell(area, cell);

		} else {

			walkingToVisibleTrapInFog = !Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell]
					&& Dungeon.level.trap(cell) != null && Dungeon.level.trap(cell).visible;

			curAction = new HeroAction.Move(cell);
			lastAction = null;

		}


		return true;
	}

	public final void earnExp(int exp, Class source) {
		earnExp(exp, source, true);
	}

	public void earnExp(int exp, Class source, boolean show) {

		if (exp > 0 && show) {
			Dungeon.hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(Mob.class, "exp", exp));
		}
		this.exp += exp;
		float percent = exp / (float) maxExp();

		EtherealChains.chainsRecharge chains = buff(EtherealChains.chainsRecharge.class);
		if (chains != null) chains.gainExp(percent);

		HornOfPlenty.hornRecharge horn = buff(HornOfPlenty.hornRecharge.class);
		if (horn != null) horn.gainCharge(percent);

		AlchemistsToolkit.kitEnergy kit = buff(AlchemistsToolkit.kitEnergy.class);
		if (kit != null) kit.gainCharge(percent);

		Berserk berserk = buff(Berserk.class);
		if (berserk != null) berserk.recover(percent);

		if (source != PotionOfExperience.class) {
			for (Item i : belongings) {
				i.onHeroGainExp(percent, this);
			}
		}

		boolean levelUp = false;
		while (this.exp >= maxExp() && lvl < Constants.HERO_LEVEL_CAP) {
			this.exp -= maxExp();
			lvl++;
			levelUp = true;

			if (buff(ElixirOfMight.HTBoost.class) != null) {
				buff(ElixirOfMight.HTBoost.class).onLevelUp();
			}

			updateHT(true);
		}

		if (levelUp) {

			if (sprite != null) {
				GLog.newLine();
				GLog.positive(Messages.get(this, "new_level"), lvl);
				sprite.showStatus(CharSprite.POSITIVE, Messages.get(Hero.class, "level_up"));
				Sample.INSTANCE.play(Assets.Sounds.LEVELUP);
			}
			increasePoints(1);

			Item.updateQuickslot();

			Badges.validateLevelReached();
		}
	}

	public int maxExp() {
		return maxExp(lvl);
	}

	@Contract(pure = true)
	public static int maxExp(int lvl) {
		return 5 + lvl * 5;
	}

	public boolean isStarving() {
		return Buff.affect(this, Hunger.class).isStarving();
	}

	@Override
	public void add(Buff buff) {

		if (buff(TimekeepersHourglass.timeStasis.class) != null)
			return;

		super.add(buff);

		if (sprite != null) {
			String msg = buff.heroMessage();
			if (msg != null) {
				GLog.warning(msg);
			}

			if (buff instanceof Paralysis || buff instanceof Vertigo) {
				interrupt();
			}

		}

		BuffIndicator.refreshHero();
	}

	@Override
	public void remove(Buff buff) {
		super.remove(buff);

		BuffIndicator.refreshHero();
	}

	@Override
	public void die(DamageSrc cause) {

		curAction = null;

		Ankh ankh = null;

		//look for ankhs in player inventory, prioritize ones which are blessed.
		for (Item item : belongings) {
			if (item instanceof Ankh) {
				if (ankh == null) {
					ankh = (Ankh) item;
				}
			}
		}

		if (Dungeon.level instanceof GrindLevel && Dungeon.key.equals(LuckyBadge.AC_GRIND)) {
			Ankh.revive(this, null);
			GLog.positive(Messages.get(LuckyBadge.class, "saved_death"));
			LuckyBadge.doReturn();
			return;
		} else if (ankh != null) {
			ankh.revive(this);
			return;
		}

		Actor.fixTime();
		super.die(cause);
		reallyDie(cause);
	}

	public static void reallyDie(DamageSrc cause) {

		int length = Dungeon.level.length();
		KindOfTerrain[] map = Dungeon.level.getMap();
		boolean[] visited = Dungeon.level.visited;
		boolean[] discoverable = Dungeon.level.discoverable;

		for (int i = 0; i < length; i++) {

			KindOfTerrain terr = map[i];

			if (discoverable[i]) {

				visited[i] = true;
				if (terr.secret()) {
					Dungeon.level.discover(i);
				}
			}
		}

		Bones.leave();

		Dungeon.observe();
		GameScene.updateFog();

		Dungeon.hero.belongings.identify();

		int pos = Dungeon.hero.pos;

		ArrayList<Integer> passable = new ArrayList<>();
		for (Integer ofs : PathFinder.NEIGHBOURS8) {
			int cell = pos + ofs;
			if ((Dungeon.level.passable(cell) || Dungeon.level.avoid(cell)) && Dungeon.level.heaps.get(cell) == null) {
				passable.add(cell);
			}
		}
		Collections.shuffle(passable);

		ArrayList<Item> items = new ArrayList<>(Dungeon.hero.belongings.backpack.items);
		for (Integer cell : passable) {
			if (items.isEmpty()) {
				break;
			}

			Item item = Random.element(items);
			Dungeon.level.drop(item, cell).sprite.drop(pos);
			items.remove(item);
		}

		GameScene.gameOver();

		if (cause.getCause() instanceof Hero.Doom) {
			((Hero.Doom) cause.getCause()).onDeath();
		}

		Dungeon.deleteGame(GamesInProgress.curSlot, true);
	}

	//effectively cache this buff to prevent having to call buff(Berserk.class) a bunch.
	//This is relevant because we call isAlive during drawing, which has both performance
	//and concurrent modification implications if that method calls buff(Berserk.class)
	private Berserk berserk;

	@Override
	public boolean isAlive() {

		if (HP <= 0) {
			if (berserk == null) berserk = buff(Berserk.class);
			return berserk != null && berserk.berserking();
		} else {
			berserk = null;
			return super.isAlive();
		}
	}

	@Override
	public void move(int step) {
		boolean wasHighGrass = Dungeon.level.getTerrain(step) == Terrain.HIGH_GRASS;

		super.move(step);

		if (!flying) {
			if (Dungeon.level.liquid(pos)) {
				Sample.INSTANCE.play(Assets.Sounds.WATER, 1, Random.Float(0.8f, 1.25f));
			} else if (Dungeon.level.getTerrain(pos) == Terrain.EMPTY_SP) {
				Sample.INSTANCE.play(Assets.Sounds.STURDY, 1, Random.Float(0.96f, 1.05f));
			} else if (Dungeon.level.getTerrain(pos) == Terrain.GRASS
					|| Dungeon.level.getTerrain(pos) == Terrain.EMBERS
					|| Dungeon.level.getTerrain(pos) == Terrain.FURROWED_GRASS) {
				if (step == pos && wasHighGrass) {
					Sample.INSTANCE.play(Assets.Sounds.TRAMPLE, 1, Random.Float( 0.96f, 1.05f ) );
				} else {
					Sample.INSTANCE.play( Assets.Sounds.GRASS, 1, Random.Float( 0.96f, 1.05f ) );
				}
			} else {
				Sample.INSTANCE.play(Assets.Sounds.STEP, 1, Random.Float(0.96f, 1.05f));
			}
		}
	}

	@Override
	public void onAttackComplete() {

		AttackIndicator.target(enemy);

		boolean hit = attack(enemy);

		if (subClass == HeroSubClass.GLADIATOR) {
			if (hit) {
				Buff.affect(this, Combo.class).hit(enemy);
			} else {
				Combo combo = buff(Combo.class);
				if (combo != null) combo.miss(enemy);
			}
		}

		Invisibility.dispel();
		spend(attackDelay());

		curAction = null;

		super.onAttackComplete();
	}

	@Override
	public void onOperateComplete() {

		if (curAction instanceof HeroAction.Unlock) {

			int doorCell = ((HeroAction.Unlock) curAction).dst;
			KindOfTerrain door = Dungeon.level.getTerrain(doorCell);

			if (Dungeon.level.distance(pos, doorCell) <= 1) {
				boolean hasKey = false;
				if (door == Terrain.LOCKED_DOOR) {
					hasKey = Notes.remove(new IronKey(Dungeon.key));
					if (hasKey) Dungeon.level.set(doorCell, Terrain.DOOR);
				} else if (door == Terrain.BRONZE_LOCKED_DOOR) {
					hasKey = Notes.remove(new BronzeKey(Dungeon.key));
					if (hasKey) Dungeon.level.set(doorCell, Terrain.DOOR);
				} else if (door == Terrain.LOCKED_EXIT) {
					hasKey = Notes.remove(new SkeletonKey(Dungeon.key));
					if (hasKey) Dungeon.level.set(doorCell, Terrain.UNLOCKED_EXIT);
				}

				if (hasKey) {
					GameScene.updateKeyDisplay();
					GameScene.updateMap(doorCell);
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		} else if (curAction instanceof HeroAction.OpenChest) {

			Heap heap = Dungeon.level.heaps.get(((HeroAction.OpenChest) curAction).dst);

			if (Dungeon.level.distance(pos, heap.pos) <= 1) {
				boolean hasKey = true;
				if (heap.type == Type.SKELETON || heap.type == Type.REMAINS) {
					Sample.INSTANCE.play(Assets.Sounds.BONES);
				} else if (heap.type == Type.LOCKED_CHEST) {
					hasKey = Notes.remove(new GoldenKey(Dungeon.key));
				} else if (heap.type == Type.CRYSTAL_CHEST) {
					hasKey = Notes.remove(new CrystalKey(Dungeon.key));
				}

				if (hasKey) {
					GameScene.updateKeyDisplay();
					heap.open(this);
					spend(Key.TIME_TO_UNLOCK);
				}
			}

		}
		curAction = null;

		super.onOperateComplete();
	}

	public boolean search(boolean intentional) {

		if (!isAlive()) return false;

		boolean smthFound = false;

		int distance = heroClass == HeroClass.ROGUE ? 2 : 1;

		boolean foresight = buff(Foresight.class) != null;

		if (foresight) distance++;

		int cx = pos % Dungeon.level.width();
		int cy = pos / Dungeon.level.width();
		int ax = cx - distance;
		if (ax < 0) {
			ax = 0;
		}
		int bx = cx + distance;
		if (bx >= Dungeon.level.width()) {
			bx = Dungeon.level.width() - 1;
		}
		int ay = cy - distance;
		if (ay < 0) {
			ay = 0;
		}
		int by = cy + distance;
		if (by >= Dungeon.level.height()) {
			by = Dungeon.level.height() - 1;
		}

		TalismanOfForesight.Foresight talisman = buff(TalismanOfForesight.Foresight.class);
		boolean cursed = talisman != null && talisman.isCursed();

		for (int y = ay; y <= by; y++) {
			for (int x = ax, p = ax + y * Dungeon.level.width(); x <= bx; x++, p++) {

				if (fieldOfView[p] && p != pos) {

					if (intentional) {
						GameScene.effectOverFog(new CheckedCell(p, pos));
					}

					if (Dungeon.level.secret(p)) {

						Trap trap = Dungeon.level.trap(p);

						float chance;
						//searches aided by foresight always succeed, even if trap isn't searchable
						if (foresight) {
							chance = 1f;

							//otherwise if the trap isn't searchable, searching always fails
						} else if (trap != null && !trap.canBeSearched) {
							chance = 0f;

							//intentional searches always succeed against regular traps and doors
						} else if (intentional) {
							chance = 1f;

							//unintentional searches always fail with a cursed talisman
						} else if (cursed) {
							chance = 0f;

							//unintentional trap detection scales from 40% at floor 0 to 30% at floor 25
						} else if (Dungeon.level.hasTrap(p)) {
							chance = 0.4f - (Dungeon.depth / 250f);
							//GLog.p("trap");

							//unintentional door detection scales from 20% at floor 0 to 0% at floor 20
						} else {
							chance = 0.2f - (Dungeon.depth / 100f);
							//GLog.p("door/wall");

						}

						if (Random.Float() < chance) {

							KindOfTerrain oldValue = Dungeon.level.getTerrain(p);

							GameScene.discoverTile(p, oldValue);

							Dungeon.level.discover(p);

							ScrollOfMagicMapping.discover(p);

							smthFound = true;

							if (talisman != null){
								if (oldValue == Terrain.SECRET_DOOR) {
									talisman.charge(10);
								} else {
									talisman.charge(2);
								}
							}
						}
					}
				}
			}
		}


		if (intentional) {
			sprite.showStatus(CharSprite.DEFAULT, Messages.get(this, "search"));
			sprite.operate(pos);
			if (!Dungeon.level.locked) {
				if (cursed) {
					GLog.negative(Messages.get(this, "search_distracted"));
					Buff.affect(this, Hunger.class).reduceHunger(TIME_TO_SEARCH - (2 * HUNGER_FOR_SEARCH));
				} else {
					Buff.affect(this, Hunger.class).reduceHunger(TIME_TO_SEARCH - HUNGER_FOR_SEARCH);
				}
			}
			spendAndNext(TIME_TO_SEARCH);

		}

		if (smthFound) {
			GLog.warning(Messages.get(this, "noticed_smth"));
			Sample.INSTANCE.play(Assets.Sounds.SECRET);
			interrupt();
		}

		return smthFound;
	}

	public void resurrect(String resetLevel) {

		HP = HT;
		Dungeon.gold = 0;
		exp = 0;

		belongings.resurrect(resetLevel);

		live();
	}

	@Override
	public void next() {
		if (isAlive())
			super.next();
	}

	public interface Doom {
		void onDeath();
	}
}
