package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.items.shield.RoundShield;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.windows.quest.WndChat;
import com.shatteredpixel.yasd.general.windows.quest.WndHeroNPCChat;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Arrays;

public class WarriorNPC extends HeroNPC {

	private static final String REASON_HERE_QUESTION = "reason_here";
	private static final String SPOKEN_AS_ROGUE = "spoken_as_rogue";
	private static final String SHIELD_GIVEN = "shield_given";
	//TODO special stuff
	private static final String IS_TEACHER = "is_teacher";

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
		if (ch instanceof Hero) {
			Hero h = (Hero) ch;
			if (h.heroClass == HeroClass.ROGUE) {
				if (h.subClass != HeroSubClass.BRAWLER || h.getResilience() < 5) {
					addQuestFlag(SPOKEN_AS_ROGUE);
					introduction = Messages.get(this, "rogue");
				} else if (questlineFlagCompleted(SPOKEN_AS_ROGUE)) {
					introduction = Messages.get(this, "rogue_redeem");
				}
			}
			if (new ArrayList<>(Arrays.asList(HeroClass.WARRIOR.subClasses())).contains(h.subClass) && !questlineFlagCompleted(SHIELD_GIVEN)) {
				options.put(Messages.get(this, "training"), WndChat.asCallback(new Training()));
			} else if (h.getResilience() > 6) {
				options.put(Messages.get(this, "teach"), WndChat.asCallback(new Teacher()));
			}
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

	public final class Training extends WndHeroNPCChat {
		public Training() {
			super(WarriorNPC.this, Messages.get(WarriorNPC.class, "training_response"));
			Dungeon.level.drop(new RoundShield().level(Dungeon.hero.getFocus()), Dungeon.hero.pos);
			addQuestFlag(SHIELD_GIVEN);
		}
	}

	public final class Teacher extends WndHeroNPCChat {
		public Teacher() {
			super(WarriorNPC.this, Messages.get(WarriorNPC.class, "teach_response"));
			addQuestFlag(IS_TEACHER);
		}
	}
}
