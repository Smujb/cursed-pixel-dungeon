package com.shatteredpixel.yasd.general.items.rings;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.messages.Messages;

import java.text.DecimalFormat;

public abstract class HeroStatRing extends Ring {
	public String statsInfo() {
		return Messages.get(this, "stats", new DecimalFormat("#.###").format(hpDropOffReduction() * 0.1));
	}

	public static float hpDropOffReduction() {
		return 0.1f;
	}

	@Override
	public boolean doEquip(Hero hero) {
		if (super.doEquip(hero)){
			hero.updateHT( false );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean doUnequip(Char hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)){
			hero.updateHT( false );
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Item upgrade() {
		super.upgrade();
		updateTargetHT();
		return this;
	}

	@Override
	public Item level(int value) {
		super.level(value);
		updateTargetHT();
		return this;
	}

	private void updateTargetHT(){
		if (buff != null && buff.target != null) {
			buff.target.updateHT( false );
		}
	}
}
