package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;

public class RogueNPC extends HeroNPC {

	@Override
	public HeroClass heroClass() {
		return HeroClass.ROGUE;
	}
}
