package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class HeroicLeap extends Power {

	{
		image = ItemSpriteSheet.HEROICLEAP;
		mp_cost = 5;

		usesTargeting = true;
	}

	private static int SHOCK_TIME	= 3;

	@Override
	public void onZap(Ballistica shot) {
		super.onZap(shot);
		int dest = shot.collisionPos;
		curUser.move(dest);
		Dungeon.level.pressCell(dest);
		Dungeon.observe();
		GameScene.updateFog();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char mob = Actor.findChar(curUser.pos + PathFinder.NEIGHBOURS8[i]);
			if (mob != null && mob != curUser) {
				Buff.prolong(mob, Paralysis.class, SHOCK_TIME);
			}
		}

		CellEmitter.center(dest).burst(Speck.factory(Speck.DUST), 10);
		Camera.main.shake(2, 0.5f);
	}

	@Override
	public void fx(Ballistica shot, Callback callback) {
		callback.call();//No zapping animation, animation is handled in affectCell.
		curUser.sprite.jump(curUser.pos, shot.collisionPos, callback );
	}
}

