package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Roots;
import com.shatteredpixel.yasd.general.plants.Earthroot;
import com.shatteredpixel.yasd.general.sprites.EarthenDragonSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class EarthenDragonPendant extends DragonPendant {

	{
		image = ItemSpriteSheet.BROWN_DRAGON_CRYSTAL;
	}

	@Override
	protected Class<? extends Dragon> dragonType() {
		return EarthenDragon.class;
	}

	public static class EarthenDragon extends DragonPendant.Dragon {
		{
			spriteClass = EarthenDragonSprite.class;
			healthFactor = 1.3f;
			damageFactor = 0.8f;
			resistances.put(Element.EARTH, 0f);
		}

		@Override
		public Element elementalType() {
			return Element.EARTH;
		}

		@Override
		public int defenseProc(Char enemy, int damage) {
			Buff.affect(this, Roots.class, Paralysis.DURATION/2f);
			Buff.affect(this, Earthroot.Armor.class).level(HT);
			return super.defenseProc(enemy, damage);
		}

		@Override
		protected Class<? extends DragonPendant> pendantType() {
			return EarthenDragonPendant.class;
		}
	}
}