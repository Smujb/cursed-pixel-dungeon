package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.windows.quest.WndChat;
import com.shatteredpixel.yasd.general.windows.quest.WndHeroNPCChat;
import com.watabou.utils.Callback;

public class WarriorNPC extends HeroNPC {

	private static final String REASON_HERE_QUESTION = "reason_here";

	@Override
	public HeroClass heroClass() {
		return HeroClass.WARRIOR;
	}

	@Override
	public boolean interact(Char ch) {
		ArrayMap<String, Callback> options = new ArrayMap<>();
		String introduction = Messages.get(WarriorNPC.this, "introduction", ch.name());
		if (!questlineFlagCompleted(REASON_HERE_QUESTION)) {
			options.put(Messages.get(WarriorNPC.this, "for_dungeon"), WndChat.asCallback(new ForDungeonResponse()));
			options.put(Messages.get(WarriorNPC.this, "for_amulet"), WndChat.asCallback(new ForAmuletResponse()));
			introduction += Messages.get(this, "why_here");
		}
		String finalIntroduction = introduction;
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				CPDGame.scene().addToFront(new WndHeroNPCChat(WarriorNPC.this, finalIntroduction, options));
			}
		});
		return super.interact(ch);
	}

	public final class ForDungeonResponse extends WndHeroNPCChat {
		public ForDungeonResponse() {
			super(WarriorNPC.this, Messages.get(WarriorNPC.class, "for_dungeon_response"));
			addQuestFlag(REASON_HERE_QUESTION);
		}
	}

	public final class ForAmuletResponse extends WndHeroNPCChat {
		public ForAmuletResponse() {
			super(WarriorNPC.this, Messages.get(WarriorNPC.class, "for_amulet_response"));
			addQuestFlag(REASON_HERE_QUESTION);
		}
	}

	public final class ForReputationResponse extends WndHeroNPCChat {
		public ForReputationResponse() {
			super(WarriorNPC.this, Messages.get(WarriorNPC.class, "for_reputation_response"));
		}
	}
}
