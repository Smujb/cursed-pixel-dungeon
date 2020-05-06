package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.bags.Bag;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Power extends Item {

	protected abstract Class<? extends Buff> passiveBuff();

	private static final String AC_ACTIVATE = "activate";

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add(AC_ACTIVATE);
		return actions;
	}

	@Override
	public boolean collect(Bag container, Char ch) {
		boolean collect = super.collect(container, ch);
		if (collect) {
			Buff.affect(ch, passiveBuff());
		}
		return collect;
	}

	@Override
	public void doDrop(@NotNull Hero hero) {
		Buff buff = hero.buff(passiveBuff());
		if (buff != null) {
			buff.detach();
		}
		super.doDrop(hero);
	}
}
