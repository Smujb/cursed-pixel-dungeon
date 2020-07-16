package com.shatteredpixel.yasd.general.attacks;

import com.shatteredpixel.yasd.general.actors.Char;
import com.watabou.utils.Callback;

public abstract class AttackType {

	public abstract float staminaCost();

	public boolean doAttack(Char attacker, Char enemy) {
		if (attacker.sprite != null && (attacker.sprite.visible || enemy.sprite.visible)) {
			attacker.sprite.attack(enemy.pos, new Callback() {
				@Override
				public void call() {
					//attacker.onAttackComplete();
					attack(attacker, enemy, false);
				}
			});
			attacker.spend( attacker.attackDelay() );
			return false;
		} else {
			attack(attacker, enemy, false);
			attacker.spend( attacker.attackDelay() );
			return true;
		}
	}

	public abstract boolean attack(Char attacker, Char enemy, boolean guaranteed);
}
