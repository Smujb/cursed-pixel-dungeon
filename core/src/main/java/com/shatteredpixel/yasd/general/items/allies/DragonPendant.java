package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class DragonPendant extends KindofMisc {

	{
		defaultAction = AC_SUMMON;
	}

	private int dragonID;

	private static final String AC_SUMMON = "summon";

	private static final String CHARGE = "charge";
	private static final String DRAGON_ID = "dragon_id";

	private static final float CHARGE_CAP = 100f;
	private float charge = CHARGE_CAP;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_SUMMON);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_SUMMON)) {
			doSummon(hero);
		}
	}

	private void doSummon(Char ch) {
		if (charge < CHARGE_CAP) {
			GLog.w(Messages.get(this, "no_charge"));
			return;
		}
		if (getDragon() != null) {
			GLog.w("already_spawned");
			return;
		}
		ch.spend(1f);
		ArrayList<Integer> neighbors = new ArrayList<>();
		for (int offset : PathFinder.NEIGHBOURS9) {
			int cell = ch.pos + offset;
			if (Dungeon.level.passable(cell) && Actor.findChar(cell) == null) {
				neighbors.add(cell);
			}
		}

		if (neighbors.size() > 0) {
			Dragon dragon = Mob.spawnAt(dragonType(), Random.element(neighbors));
			if (dragon != null) {
				dragon.updatePendant(this);
				setDragon(dragon);
				charge = 0f;
				GLog.p(Messages.get(this, "dragon_spawned"));
			} else {
				GLog.n(Messages.get(this, "spawn_failed"));
			}
		}
	}

	@Override
	public String desc() {
		String desc = super.desc() + "\n\n";
		Dragon dragon = getDragon();
		if (dragon != null) {
			desc += dragon.description();
		} else {
			desc += Messages.get(this, "no_dragon");
		}
		return desc;
	}

	public void setDragon(Dragon dragon) {
		if (dragon.getClass() == dragonType() && dragon.pendantType() == getClass()) {
			dragonID = dragon.id();
		}
	}

	public Dragon getDragon() {
		Actor actor = Actor.findById(dragonID);
		if (actor != null && actor.getClass() == dragonType() && ((Char)actor).isAlive()) {
			return ((Dragon)actor);
		} else {
			return null;
		}
	}

	@Override
	public Item upgrade() {
		super.upgrade();
		Dragon dragon = getDragon();
		if (dragon != null) {
			dragon.updatePendant(this);
		}
		return this;
	}

	@Override
	public boolean doEquip(Hero hero) {
		//Have to enforce this as the dragon looks through hero's miscs for a pendant of its type.
		for (KindofMisc misc : hero.belongings.miscs) {
			if (misc != null && misc.getClass() == this.getClass()) {
				GLog.w(Messages.get(this, "cannot_wear_two"));
				return false;
			}
		}
		return super.doEquip(hero);
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);
		new ChargeBuff().attachTo(ch);
	}

	private class ChargeBuff extends Buff {
		@Override
		public boolean act() {
			if (getDragon() == null && charge < CHARGE_CAP) {
				charge += 0.25f;
			}
			Item.updateQuickslot();
			spend(TICK);
			return true;
		}
	}

	@Override
	public String status() {
		return Messages.format( "%d%%", Math.round(charge) );
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DRAGON_ID, dragonID);
		bundle.put(CHARGE, charge);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		dragonID = bundle.getInt(DRAGON_ID);
		charge = bundle.getFloat(CHARGE);
	}

	protected abstract Class<? extends Dragon> dragonType();

	protected static abstract class Dragon extends Mob {

		{
			alignment = Alignment.ALLY;

			WANDERING = new Following();
			state = HUNTING;
			intelligentAlly = true;
		}

		private int rangedAttackCooldown = 0;

		private static final String RANGED_ATTK_COOLDOWN = "ranged_cooldown";

		private void updatePendant(@NotNull DragonPendant pen) {
			level = 1 + pen.level();
			updateHT(true);
		}

		@Override
		protected boolean act() {
			if (getPendant() == null) {
				alignment = Alignment.ENEMY;
				WANDERING = new Wandering();
			} else {
				alignment = Alignment.ALLY;
				WANDERING = new Following();
			}
			if (rangedAttackCooldown > 0) {
				rangedAttackCooldown--;
			}
			return super.act();
		}

		@Override
		public boolean canAttack(@NotNull Char enemy) {
			if (rangedAttackCooldown > 0) {
				range = 1;
				hasMeleeAttack = true;
			} else {
				range = 7;
				if (speed() > 1f) {
					hasMeleeAttack = false;
				}
			}
			return super.canAttack(enemy);
		}

		@Override
		public boolean attack(Char enemy, boolean guaranteed, AttackType type) {
			if (!hasMeleeAttack || range > 1) {
				rangedAttackCooldown = 25;
			}
			return super.attack(enemy, guaranteed, type);
		}

		private DragonPendant getPendant() {
			ArrayList<KindofMisc> crystals = Dungeon.hero.belongings.getMiscsOfType(pendantType());
			if (crystals.size() == 1 && crystals.get(0) instanceof DragonPendant) {
				return (DragonPendant) crystals.get(0);
			}
			return null;
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(RANGED_ATTK_COOLDOWN, rangedAttackCooldown);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			rangedAttackCooldown = bundle.getInt(RANGED_ATTK_COOLDOWN);
		}

		protected abstract Class<? extends DragonPendant> pendantType();
	}
}
