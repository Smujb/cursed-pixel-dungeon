package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Power extends Item {

	{
		defaultAction = AC_ACTIVATE;

		unique = true;
	}

	protected Class<? extends Buff> passiveBuff = null;

	private static final String AC_ACTIVATE = "activate";

	protected float timeToUse = 1.0f;

	protected int mp_cost = -1;
	protected int collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;

	int charge = 0;
	float partialCharge = 0;
	int chargeCap = 100;

	public CorruptedSpell corruptedVersion() {
		return null;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add(AC_ACTIVATE);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		if (action.equals(AC_ACTIVATE)) {
			activatePower(hero);
		}
	}

	protected void activatePower(Hero hero) {
		if (usesTargeting) {
			GameScene.selectCell(zapper);
		} else if (mp_cost == -1 || use()) {
			onUse(hero);
		} else {
			GLog.n(Messages.get(this, "no_mp"));
		}
	}

	protected void onUse(Hero hero) {
		spendTime();
	}

	private void spendTime() {
		if (timeToUse > 0) {
			curUser.spendAndNext(timeToUse);
		}
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	protected boolean use() {
		return curUser instanceof Hero && ((Hero) curUser).useMP(mp_cost);
	}

	protected CellSelector.Listener zapper = new  CellSelector.Listener(this) {

		@Override
		public void onSelect( Integer target ) {

			if (target != null) {

				//FIXME this safety check shouldn't be necessary
				//it would be better to eliminate the curItem static variable.
				final Power curPower = ((Power)source);

				final Ballistica shot = new Ballistica(curUser.pos, target, curPower.collisionProperties);
				int cell = shot.collisionPos;

				if (target == curUser.pos || cell == curUser.pos) {
					GLog.i(Messages.get(Wand.class, "self_target"));
					return;
				} else if (curUser.buff(MagicImmune.class) != null) {
					GLog.w(Messages.get(Wand.class, "no_magic"));
					return;
				} else if (!curPower.use()) {
					GLog.n(Messages.get(this, "no_mp"));
					curPower.spendTime();
					return;
				}

				curUser.sprite.zap(cell);

				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null) {
					QuickSlotButton.target(Actor.findChar(target));
				} else {
					QuickSlotButton.target(Actor.findChar(cell));
				}

				curPower.fx(shot, new Callback() {
					public void call() {
						curPower.onZap(shot);
						curPower.spendTime();
					}
				});

			}
		}

		@Override
		public String prompt() {
			return Messages.get(Wand.class, "prompt");
		}
	};

	public void onZap(Ballistica shot) {}

	public void fx(Ballistica shot, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.MAGIC_MISSILE,
				curUser.sprite,
				shot.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	public boolean collect(Bag container, Char ch) {
		boolean collect = super.collect(container, ch);
		if (collect && passiveBuff != null) {
			Buff.affect(ch, passiveBuff);
		}
		return collect;
	}

	@Override
	public void doDrop(@NotNull Hero hero) {
		if (passiveBuff != null) {
			Buff buff = hero.buff(passiveBuff);
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

	abstract static class PowerBuff extends Buff {

	}
}
