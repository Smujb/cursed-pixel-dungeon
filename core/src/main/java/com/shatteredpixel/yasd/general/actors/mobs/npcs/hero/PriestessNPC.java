package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.quest.WndHeroNPCChat;
import com.watabou.utils.Callback;

public class PriestessNPC extends HeroNPC {

	@Override
	public boolean interact(Char ch) {
		ArrayMap<String, Class<? extends Window>> options = new ArrayMap<>();
		String text = Messages.get(PriestessNPC.this, "introduction", ch.name());
		if (ch instanceof Hero && ((Hero) ch).heroClass == HeroClass.MAGE)
			text += Messages.get(PriestessNPC.this, "extra_dialog_mage");
		/*if (ch.hasBelongings() && ch.belongings.getItem(Relic.class) != null) {
			options.put(Messages.get(PriestessNPC.class, "give_relic"), WndGivenRelic.class);
		}*/
		String finalText = text;
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndHeroNPCChat(heroClass(), finalText, options));
			}
		});
		return super.interact(ch);
	}

	@Override
	public HeroClass heroClass() {
		return HeroClass.PRIESTESS;
	}
}
