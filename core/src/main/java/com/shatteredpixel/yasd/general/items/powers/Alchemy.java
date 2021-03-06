package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.AlchemyScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Game;

public class Alchemy extends Power {

	{
		image = ItemSpriteSheet.ARTIFACT_TOOLKIT;

		timeToUse = 0f;
	}

	@Override
	protected void onUse(Hero hero) {
		super.onUse(hero);
		if (cursed())                                                GLog.warning( Messages.get(this, "cursed") );
		else if (hero.visibleEnemies() > hero.mindVisionEnemies.size()) GLog.info( Messages.get(this, "enemy_near") );
		else {
			AlchemyScene.setProvider(new Energy());
			Game.switchScene(AlchemyScene.class);
		}
	}

	@Override
	public String desc() {
		String result = Messages.get(this, "desc");
		if (cursed())             result += "\n\n" + Messages.get(this, "desc_cursed");
		else                    result += "\n\n" + Messages.get(this, "desc_hint");

		return result;
	}

	public static class Energy extends PowerBuff implements AlchemyScene.AlchemyProvider {

		@Override
		public int getEnergy() {
			return Dungeon.hero.mp;
		}

		@Override
		public void spendEnergy(int reduction) {
			Dungeon.hero.useMP(reduction);
		}
	}
}
