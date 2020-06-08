package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
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
				options.put(Messages.get(WarriorNPC.this, "for_dungeon"), new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "for_dungeon_response")));
				options.put(Messages.get(WarriorNPC.this, "for_amulet"), new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "for_amulet_response")));
				GameScene.show(new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "introduction", ch.name()), options));
			}
		});
		return super.interact(ch);
	}
}
