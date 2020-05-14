package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Wraith;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class RaiseDead extends Power {

	{
		image = ItemSpriteSheet.RAISEDEAD;
		mp_cost = 8;
	}

	@Override
	protected void onUse(Hero hero) {
		super.onUse(hero);
		Mob.spawnAround4(Dead.class, hero.pos);
	}

	public static class Dead extends Wraith {
		{
			alignment = Alignment.ALLY;
		}

		@Override
		public int damageRoll() {
			return super.damageRoll()*2;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			new FlavourBuff(){
				{actPriority = VFX_PRIO;}
				public boolean act() {
					target.die(new DamageSrc(Element.META, this));
					return super.act();
				}
			}.attachTo(this);
			return super.attackProc(enemy, damage);
		}
	}
}
