package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.hero.WarriorNPC;
import com.shatteredpixel.yasd.general.sprites.HeroNPCSprite;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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

	@NotNull
	@Contract("_ -> new")
	public static HeroNPC create(@NotNull HeroClass heroClass) {
		switch (heroClass) {
			case WARRIOR:
				return new WarriorNPC();
			case MAGE:
			case ROGUE:
			case HUNTRESS:
			case PRIESTESS:
			default:
				return new HeroNPC() {
					@Override
					public HeroClass heroClass() {
						return heroClass;
					}
				};
		}
	}
}
