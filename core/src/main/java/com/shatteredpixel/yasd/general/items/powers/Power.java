package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Power extends Item {

	protected abstract Class<? extends Buff> passiveBuff();

	private static final String AC_ACTIVATE = "activate";

	private static final float TIME_TO_ZAP = 1.0f;

	protected int mp_cost = -1;

	int charge = 0;
	float partialCharge = 0;
	int chargeCap = 100;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add(AC_ACTIVATE);
		return actions;
	}

	@Override
	public boolean collect(Bag container, Char ch) {
		boolean collect = super.collect(container, ch);
		if (collect && passiveBuff() != null) {
			Buff.affect(ch, passiveBuff());
		}
		return collect;
	}

	@Override
	public void doDrop(@NotNull Hero hero) {
		if (passiveBuff() != null) {
			Buff buff = hero.buff(passiveBuff());
			if (buff != null) {
				buff.detach();
			}
		}
		super.doDrop(hero);
	}

	@Override
	public int price() {
		return 60 + 20 * Dungeon.getScaleFactor();
	}

	private static final String CHARGE = "charge";
	private static final String PARTIALCHARGE = "partialcharge";
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( CHARGE , charge );
		bundle.put( PARTIALCHARGE , partialCharge );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		if (chargeCap > 0)  charge = Math.min( chargeCap, bundle.getInt( CHARGE ));
		else                charge = bundle.getInt( CHARGE );
		partialCharge = bundle.getFloat( PARTIALCHARGE );
	}
}
