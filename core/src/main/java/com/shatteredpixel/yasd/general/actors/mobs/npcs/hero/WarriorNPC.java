package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class WarriorNPC extends HeroNPC {

	@Override
	public HeroClass heroClass() {
		return HeroClass.WARRIOR;
	}

	@Override
	public boolean interact(Char ch) {
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				HashMap<String, Window> options = new HashMap<>();
				//options.put("test", new WndChat(heroClass().icon(), "2", "4"));
				//CPDGame.scene().addToFront(new WndChat(heroClass().icon(), "2", "3", options));
			}
		});
		return true;
	}
}
