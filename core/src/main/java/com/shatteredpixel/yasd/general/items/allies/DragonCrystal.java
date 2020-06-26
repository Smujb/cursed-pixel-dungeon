package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class DragonCrystal extends KindofMisc {

	private int dragonID;

	private static final String AC_SUMMON = "summon";

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
				dragon.setDragonCrystal(this);
				setDragon(dragon);
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
		if (dragon.getClass() == dragonType() && dragon.crystalType() == getClass()) {
			dragonID = dragon.id();
		}
	}

	public Dragon getDragon() {
		Actor actor = Actor.findById(dragonID);
		if (actor != null && actor.getClass() == dragonType()) {
			return ((Dragon)actor);
		} else {
			return null;
		}
	}

	@Override
	public boolean doEquip(Hero hero) {
		//Have to enforce this as the dragon looks through hero's miscs for a crystal of its type.
		for (KindofMisc misc : hero.belongings.miscs) {
			if (misc != null && misc.getClass() == this.getClass()) {
				GLog.w(Messages.get(this, "cannot_wear_two"));
				return false;
			}
		}
		return super.doEquip(hero);
	}

	protected abstract Class<? extends Dragon> dragonType();

	protected static abstract class Dragon extends Mob {

		{
			alignment = Alignment.ALLY;

			WANDERING = new Following();
			intelligentAlly = true;
		}

		private void setDragonCrystal(@NotNull DragonCrystal cry) {
			level = 3 + cry.level();
			updateHT(true);
		}

		@Override
		protected boolean act() {
			if (getDragonCrystal() == null) {
				alignment = Alignment.ENEMY;
				WANDERING = new Wandering();
			} else {
				alignment = Alignment.ALLY;
				WANDERING = new Following();
			}
			return super.act();
		}

		private DragonCrystal getDragonCrystal() {
			ArrayList<KindofMisc> crystals = Dungeon.hero.belongings.getMiscsOfType(crystalType());
			if (crystals.size() == 1 && crystals.get(0) instanceof DragonCrystal) {
				return (DragonCrystal) crystals.get(0);
			}
			return null;
		}

		protected abstract Class<? extends DragonCrystal> crystalType();
	}
}
