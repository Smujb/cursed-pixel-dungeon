package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PoisonBurst extends Power {

	{
		image = ItemSpriteSheet.POISONBURST;
		mp_cost = 3;

		usesTargeting = true;
	}

	@Override
	public void onZap(Ballistica shot) {
		super.onZap(shot);
		Char ch = Actor.findChar(shot.collisionPos);
		if (ch != null && curUser instanceof Hero) {
			ch.damage(Random.NormalIntRange(1, ((Hero) curUser).maxMP()), new Char.DamageSrc(Element.TOXIC, this));
			Buff.affect(ch, Poison.class).set( 3 + Dungeon.getScaleFactor()/2f );
		}
	}

	@Override
	public void fx(Ballistica shot, Callback callback) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.POISON,
				curUser.sprite,
				shot.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
}

