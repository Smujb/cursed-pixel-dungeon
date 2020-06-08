package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.sprites.HeroNPCSprite;

public abstract class HeroNPC extends NPC {

	{
		spriteClass = HeroNPCSprite.class;
	}

	public abstract HeroClass heroClass();

	@Override
	public boolean interact(Char ch) {
		return true;
	}

	@Override
	public void damage(int dmg, DamageSrc src) {}

	@Override
	public void die(DamageSrc cause) {}
}
