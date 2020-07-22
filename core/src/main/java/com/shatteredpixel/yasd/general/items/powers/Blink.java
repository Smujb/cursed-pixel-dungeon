package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;

public class Blink extends Power {

	{
		image = ItemSpriteSheet.BLINK;
		mp_cost = 2;

		usesTargeting = true;
	}

	@Override
	public void onZap(Ballistica shot) {
		int level = 0;
		if (curUser instanceof Hero) {
			level = ((Hero)curUser).getFocus();
		}

		int dist = Math.min(shot.dist, level);

		int cell;

		int num = 0;
		do {
			cell = shot.path.get(dist - num);
			num++;
			if (dist-num < 0) {
				return;
			}
		} while (Actor.findChar(cell) != null || !curUser.canOccupy(Dungeon.level, cell) || Dungeon.level.solid(cell));

		curUser.sprite.visible = true;
		appear(Dungeon.hero, cell);
		Dungeon.level.pressCell(cell);
		Dungeon.observe();
	}

	public static void appear(Char ch, int pos) {

		ch.sprite.interruptMotion();

		ch.move(pos);
		ch.sprite.place(pos);

		if (ch.invisible == 0) {
			ch.sprite.alpha(0);
			ch.sprite.parent.add(new AlphaTweener(ch.sprite, 1, 0.4f));
		}

		ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
		Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
	}
}
