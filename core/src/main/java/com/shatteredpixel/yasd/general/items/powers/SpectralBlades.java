package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.missiles.Shuriken;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class SpectralBlades extends Power {

	{
		image = ItemSpriteSheet.SPECTRALBLADES;
		mp_cost = 8;
	}

	private HashMap<Callback, Mob> targets = new HashMap<Callback, Mob>();


	@Override
	protected void onUse(Hero hero) {
		Item proto = new Shuriken();

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (Dungeon.level.distance(hero.pos, mob.pos) <= 12) {

				Callback callback = new Callback() {
					@Override
					public void call() {
						hero.attack( targets.get( this ) );
						targets.remove( this );
					}
				};

				((MissileSprite)hero.sprite.parent.recycle( MissileSprite.class )).
						reset( hero.pos, mob.pos, proto, callback );

				targets.put( callback, mob );
			}
		}

		if (targets.size() == 0) {
			GLog.warning( Messages.get(this, "no_enemies") );
			return;
		}

		hero.sprite.zap( hero.pos );
		hero.busy();
	}
}

