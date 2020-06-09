package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.quest.WndHeroNPCChat;
import com.watabou.utils.Callback;

public class WarriorNPC extends HeroNPC {

	@Override
	public HeroClass heroClass() {
		return HeroClass.WARRIOR;
	}

	@Override
	public boolean interact(Char ch) {
		ArrayMap<String, Class<? extends Window>> options = new ArrayMap<>();
		options.put(Messages.get(WarriorNPC.this, "for_dungeon"), ForDungeonResponse.class);
		options.put(Messages.get(WarriorNPC.this, "for_amulet"), ForAmuletResponse.class);
		if (ch instanceof Hero) {
			if (((Hero) ch).heroClass == HeroClass.ROGUE) {
				options.put(Messages.get(WarriorNPC.this, "for_reputation"), ForReputationResponse.class);
			}
		}
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				CPDGame.scene().addToFront(new WndHeroNPCChat(heroClass(), Messages.get(WarriorNPC.this, "introduction", ch.name()), options));
			}
		});
		return super.interact(ch);
	}

	public static final class ForDungeonResponse extends WndHeroNPCChat {
		public ForDungeonResponse() {
			super(HeroClass.WARRIOR, Messages.get(WarriorNPC.class, "for_dungeon_response"));
		}
	}

	public static final class ForAmuletResponse extends WndHeroNPCChat {
		public ForAmuletResponse() {
			super(HeroClass.WARRIOR, Messages.get(WarriorNPC.class, "for_amulet_response"));
		}
	}

	public static final class ForReputationResponse extends WndHeroNPCChat {
		public ForReputationResponse() {
			super(HeroClass.WARRIOR, Messages.get(WarriorNPC.class, "for_reputation_response"));
		}
	}
}
