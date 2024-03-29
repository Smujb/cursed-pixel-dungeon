package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.hero.HuntressNPC;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.hero.MageNPC;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.hero.PriestessNPC;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.hero.RogueNPC;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.hero.WarriorNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
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
		sprite.turnTo( pos, ch.pos );
		return true;
	}

	@Override
	public void damage(int dmg, DamageSrc src) {}

	@Override
	public void die(DamageSrc cause) {}

	@Override
	protected boolean act() {

		throwItem();

		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}

    @Override
	public String defenseVerb() {
		return "";
	}

	@Override
	public String name() {
		return Messages.titleCase(heroClass().title());
	}

	@NotNull
	@Contract("_ -> new")
	public static HeroNPC create(@NotNull HeroClass heroClass) {
		switch (heroClass) {
			default:
			case WARRIOR:
				return new WarriorNPC();
			case MAGE:
				return new MageNPC();
			case ROGUE:
				return new RogueNPC();
			case HUNTRESS:
				return new HuntressNPC();
			case PRIESTESS:
				return new PriestessNPC();
		}
	}
}
