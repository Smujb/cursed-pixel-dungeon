package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.quest.WndHeroNPCChat;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class WarriorNPC extends HeroNPC {

	@Override
	public HeroClass heroClass() {
		return HeroClass.WARRIOR;
	}

	@Override
	public boolean interact(Char ch) {
		HashMap<String, Window> options = new HashMap<>();
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				//options.put(Messages.get(WarriorNPC.this, "for_dungeon"), new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "for_dungeon_response")));
				//options.put(Messages.get(WarriorNPC.this, "for_amulet"), new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "for_amulet_response")));
				//if (ch instanceof Hero) {
				//	if (((Hero) ch).heroClass == HeroClass.ROGUE) {
				//		options.put(Messages.get(WarriorNPC.this, "for_reputation"), new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "for_reputation_response")));
				//	}
				//}
				CPDGame.scene().addToFront(new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "introduction", ch.name()), options));
			}
		});
		return super.interact(ch);
	}
}
