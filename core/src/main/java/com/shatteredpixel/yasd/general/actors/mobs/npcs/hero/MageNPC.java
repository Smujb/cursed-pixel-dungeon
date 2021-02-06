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
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.shatteredpixel.yasd.general.windows.WndStorage;
import com.shatteredpixel.yasd.general.windows.quest.WndHeroNPCChat;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.Arrays;

public class MageNPC extends HeroNPC {

	@Override
	public HeroClass heroClass() {
		return HeroClass.MAGE;
	}

	@Override
	public boolean interact(Char ch) {
		ArrayMap<String, Class<? extends Window>> options = new ArrayMap<>();
		options.put(Messages.get(MageNPC.this, "mana"), WndBuyManaPotion.class);
		options.put(Messages.get(MageNPC.class, "view_storage"), WndStorage.class);
		if (ch instanceof Hero) {
			Hero h = (Hero) ch;
			if (new ArrayList<>(Arrays.asList(HeroClass.MAGE.subClasses())).contains(h.subClass)) {
				options.put(Messages.get(MageNPC.class, "ask_magic"), Magic.class);
			}
		}
		options.put(Messages.get(MageNPC.this, "nothing"), NoResponse.class);
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				GameScene.show(new WndHeroNPCChat(heroClass(), Messages.get(MageNPC.this, "introduction", ch.name()), options));
			}
		});
		return super.interact(ch);
	}

	public static final class NoResponse extends WndHeroNPCChat {
		public NoResponse() {
			super(HeroClass.MAGE, Messages.get(MageNPC.class, "no_response"));
		}
	}

	public static final class Magic extends WndHeroNPCChat {
		public Magic() {
			super(HeroClass.MAGE, Messages.get(MageNPC.class, "magic_response"));
			Dungeon.level.drop(new WandOfMagicMissile().level(Dungeon.hero.getFocus()), Dungeon.hero.pos);
			Dungeon.LimitedDrops.MAGE_NPC_MAGIC_MISSILE.drop();
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
