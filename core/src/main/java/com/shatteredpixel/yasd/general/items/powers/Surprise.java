package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.Wound;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Surprise extends Power {

	{
		image = ItemSpriteSheet.SURPRISE;

		mp_cost = 5;

		usesTargeting = true;
	}

	@Override
	public void onZap(Ballistica shot) {
		super.onZap(shot);
		int pos = shot.collisionPos;
		Hero hero = ((Hero)curUser);
		Char ch = Actor.findChar(pos);
		int damage;
		if (ch != null) {
			damage = Random.NormalIntRange(hero.getFocus(), hero.getFocus()*3);
			if (ch instanceof Mob && ((Mob)ch).surprisedBy(curUser)) {
				damage *= 3;
				Wound.hit(ch);
			}
			ch.damage(damage, new Char.DamageSrc(Element.SHADOW, this));
		}
	}

	@Override
	public void fx(Ballistica shot, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.BONE,
				curUser.sprite,
				shot.collisionPos,
				callback);
	}
}

