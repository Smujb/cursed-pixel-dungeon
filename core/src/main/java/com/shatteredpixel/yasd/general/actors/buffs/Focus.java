package com.shatteredpixel.yasd.general.actors.buffs;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Focus extends Buff {
	private static String FOCUS_COOLDOWN = "focus_cooldown";
	private static String DISPLAY = "display";

	{
		type = buffType.POSITIVE;
	}

	private float cooldown = 0;
	private boolean display = true;

	@Override
	public boolean act() {
		spend(TICK);
		return true;
	}

	@Override
	public void spend(float time) {
		loseCooldown(time);
		super.spend(time);
	}

	public void loseCooldown(float amt) {
		cooldown -= amt;
		if (cooldown < 0) {
			cooldown = 0;
			if (target.sprite != null && display) {
				target.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "name"));
				display = false;
			}
		}
	}

	public boolean parryReady() {
		return cooldown <= 0f && target.paralysed == 0;
	}

	public void resetCooldown() {
		cooldown = Random.NormalFloat(6, 7);
		display = true;
	}

	@Override
	public int icon() {
		return BuffIndicator.MIND_VISION;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc") + "\n\n" + Messages.get(this, "cooldown", (double) cooldown);
	}

	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( FOCUS_COOLDOWN, cooldown );
		bundle.put( DISPLAY, display );
	}

	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		cooldown = bundle.getInt( FOCUS_COOLDOWN );
		display = bundle.getBoolean( DISPLAY );
	}
}