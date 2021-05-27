package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMana;
import com.shatteredpixel.yasd.general.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.shatteredpixel.yasd.general.windows.WndStorage;
import com.shatteredpixel.yasd.general.windows.quest.WndChat;
import com.shatteredpixel.yasd.general.windows.quest.WndHeroNPCChat;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Arrays;

public class MageNPC extends HeroNPC {

	private static final String WAND_GIVEN = "wand_given";
	//TODO special stuff
	private static final String IS_TEACHER = "is_teacher";

	@Override
	public HeroClass heroClass() {
		return HeroClass.MAGE;
	}

	@Override
	public boolean interact(Char ch) {
		ArrayMap<String, Callback> options = new ArrayMap<>();

		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				options.put(Messages.get(MageNPC.this, "mana"), WndChat.asCallback(WndBuyManaPotion.class));
				options.put(Messages.get(MageNPC.class, "view_storage"), WndChat.asCallback(WndStorage.class));
				if (ch instanceof Hero) {
					Hero h = (Hero) ch;
					if (new ArrayList<>(Arrays.asList(HeroClass.MAGE.subClasses())).contains(h.subClass) && !questlineFlagCompleted(WAND_GIVEN)) {
						options.put(Messages.get(MageNPC.class, "ask_magic"), WndChat.asCallback(Magic.class));
					} else if (h.getFocus() > 6) {
						options.put(Messages.get(MageNPC.class, "ask_teach"), WndChat.asCallback(Teacher.class));
					}
				}
				options.put(Messages.get(MageNPC.this, "nothing"), WndChat.asCallback(NoResponse.class));
				GameScene.show(new WndHeroNPCChat(MageNPC.this, Messages.get(MageNPC.this, "introduction", ch.name()), options));
			}
		});
		return super.interact(ch);
	}

	public final class NoResponse extends WndHeroNPCChat {
		public NoResponse() {
			super(MageNPC.this, Messages.get(MageNPC.class, "no_response"));
		}
	}

	public final class Magic extends WndHeroNPCChat {
		public Magic() {
			super(MageNPC.this, Messages.get(MageNPC.class, "magic_response"));
			Dungeon.level.drop(new WandOfMagicMissile().level(Dungeon.hero.getFocus()), Dungeon.hero.pos);
			addQuestFlag(WAND_GIVEN);
		}
	}

	public final class Teacher extends WndHeroNPCChat {
		public Teacher() {
			super(MageNPC.this, Messages.get(MageNPC.class, "teach_response"));
			addQuestFlag(IS_TEACHER);
		}
	}

	public static class WndBuyManaPotion extends WndOptions {

		public static int cost() {
			return (50 + 20 * Dungeon.hero.lvl);
		}

		public WndBuyManaPotion() {
			super(Messages.get(WndBuyManaPotion.class, "title"), Messages.get(WndBuyManaPotion.class, "desc", cost()), Messages.get(WndBuyManaPotion.class, "buy"));
		}

		@Override
		protected void onSelect(int index) {
			super.onSelect(index);
			if (Dungeon.gold > cost()) {
				Dungeon.gold -= cost();
				new PotionOfMana().identify().collect();
			} else {
				GLog.negative(Messages.get(this, "not_enough"));
			}
		}
	}
}
