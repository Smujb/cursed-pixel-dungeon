package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.AlchemyScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.noosa.Game;

public class Alchemy extends Power {

	{
		image = ItemSpriteSheet.ARTIFACT_TOOLKIT;
	}

	protected WndBag.Mode mode = WndBag.Mode.POTION;

	private Energy energy = new Energy();

	@Override
	protected void onUse(Hero hero) {
		super.onUse(hero);
		if (cursed)                                                GLog.w( Messages.get(this, "cursed") );
		else if (hero.visibleEnemies() > hero.mindVisionEnemies.size()) GLog.i( Messages.get(this, "enemy_near") );
		else {
			AlchemyScene.setProvider(energy);
			Game.switchScene(AlchemyScene.class);
		}
	}

	@Override
	public String desc() {
		String result = Messages.get(this, "desc");
		if (cursed)             result += "\n\n" + Messages.get(this, "desc_cursed");
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
