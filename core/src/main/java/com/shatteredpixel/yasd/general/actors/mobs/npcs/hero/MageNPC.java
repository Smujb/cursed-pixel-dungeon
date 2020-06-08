package com.shatteredpixel.yasd.general.actors.mobs.npcs.hero;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.items.potions.PotionOfMana;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.utils.Callback;

import java.util.HashMap;

public class MageNPC extends HeroNPC {

	@Override
	public HeroClass heroClass() {
		return HeroClass.MAGE;
	}

	@Override
	public boolean interact(Char ch) {
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				HashMap<String, Window> options = new HashMap<>();
				options.put(Messages.get(MageNPC.this, "yes"), new WndBuyManaPotion());
				options.put(Messages.get(MageNPC.this, "no"), new WndHeroNPCChat(heroClass(), Messages.get(MageNPC.this, "no_response")));
				GameScene.show(new WndHeroNPCChat(heroClass(), Messages.get(MageNPC.this, "introduction", ch.name()), options));
			}
		});
		return super.interact(ch);
	}

	private static class WndBuyManaPotion extends WndOptions {

		public static int cost() {
			return (50 + 20 * Dungeon.hero.lvl);
		}

		private WndBuyManaPotion() {
			super(Messages.get(WndBuyManaPotion.class, "title"), Messages.get(WndBuyManaPotion.class, "desc", cost()), Messages.get(WndBuyManaPotion.class, "buy"));
		}

		@Override
		protected void onSelect(int index) {
			super.onSelect(index);
			if (Dungeon.gold > cost()) {
				Dungeon.gold -= cost();
				new PotionOfMana().identify().collect();
			} else {
				GLog.n(Messages.get(this, "not_enough"));
			}
		}
	}
}
