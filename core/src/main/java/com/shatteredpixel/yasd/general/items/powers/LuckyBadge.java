package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Boss;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.food.MeatPie;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.levels.GrindLevel;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.tweeners.Delayer;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class LuckyBadge extends Power {

	{
		image = ItemSpriteSheet.BADGE;

		uncurse();

		defaultAction = AC_INFO;
	}

	public static final String AC_GRIND = "grind";
	public static final String AC_REMATCH = "rematch";
	public static final String AC_RETURN = "return";

	private static final String MOB_LEVEL_FACTOR = "mob_level_factor";
	private static final String MOB_SPAWN_FACTOR = "mob_spawn_factor";
	private static final String SCORE = "score";
	private static final String SCORE_BEATEN = "score_beaten";
	private static final String HERO_HP = "hero_hp";
	private static final String BOSS_REFIGHTS = "boss_refights";

	public static float mobLevelBoost = 1f;
	public static float mobSpawnFactor = 1f;
	public static int score = 0;
	public static boolean scoreBeaten = false;
	public static int heroHP;

	public enum Type {NONE, GRIND, SPEED}

	//Testing
	public Type type = Type.GRIND;

	private static int dropsToRare = 0;

	private static final String RETURN_POS = "returnPos";
	private static final String RETURN_DEPTH = "returnDepth";
	private static final String RETURN_KEY = "returnKey";

	private static int returnPos = -1;
	private static String returnKey = null;
	private static int returnDepth = -1;
	private static boolean latestDropWasRare = false;

	private ArrayList<String> rematchLevels = new ArrayList<>();

	public static ArrayList<String> rematchLevels() {
		LuckyBadge badge = Dungeon.hero.belongings.getItem(LuckyBadge.class);
		if (badge == null) return new ArrayList<>();
		else return badge.rematchLevels;
	}

	public static void addRematch(String boss) {
		LuckyBadge badge = Dungeon.hero.belongings.getItem(LuckyBadge.class);
		if (badge == null) return;
		badge.rematchLevels.add(boss);
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = new ArrayList<>();
		//FIXME Removing the grind depth option for now, commenting out in case I want to revert this
		/*if (Dungeon.key.equals(AC_GRIND) || rematchLevels.contains(Dungeon.key)) {
			actions.add(AC_RETURN);
		} else {
			if (type == Type.GRIND) {
				actions.add(AC_GRIND);
			}
			if (!rematchLevels.isEmpty()) actions.add(AC_REMATCH);
		}*/
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		switch (action) {
			case AC_GRIND:
				CPDGame.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						Game.scene().addToFront(new WndChooseGrind());
					}
				});
				heroHP = hero.HP;
				break;
			case AC_REMATCH:
				if (rematchLevels.isEmpty()) {
					GLog.negative(Messages.get(Boss.class, "not_fought"));
				} else {
					CPDGame.runOnRenderThread(new Callback() {
						@Override
						public void call() {
							Game.scene().addToFront(new WndChooseRefight());
						}
					});
					heroHP = hero.HP;
				}
				break;
			case AC_RETURN:
				doReturn();
				break;
		}
	}

	public static void doReturn() {
		if (scoreBeaten) {
			GLog.positive(Messages.get(LuckyBadge.class, "new_high_score", score));
			scoreBeaten = false;
		}
		score = 0;
		Hero hero = Dungeon.hero;
		hero.HP = heroHP;
		for (Heap heap : Dungeon.level.heaps.valueList()) {
			for (Item item : heap.items.toArray(new Item[0])) {
				((MissileSprite) hero.sprite.parent.recycle(MissileSprite.class)).
						reset(heap.pos,
								hero.sprite,
								item,
								new Callback() {
									@Override
									public void call() {
										if (item.collect(hero.belongings.backpack, hero)) {
											GameScene.pickUp(item, hero.pos);
										} else {
											Dungeon.level.drop(item, hero.pos).sprite.drop(heap.pos);
										}
									}
								});
			}
			heap.destroy();
		}
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (mob.alignment == Char.Alignment.ENEMY || mob instanceof GrindLevel.Guardian) {
				//Remove but don't kill the mob, so it doesn't drop loot
				mob.sprite.emitter().burst( ShadowParticle.CURSE, 5 );
				Dungeon.level.mobs.remove(mob);
			}
		}
		Game.scene().add(new Delayer(2f) {
			@Override
			protected void updateValues(float progress) {
				hero.busy();
				if (progress >= 1f) {
					if (returnKey == null) {
						returnKey = Dungeon.keyForDepth();
					}
					if (returnDepth < 0) {
						returnDepth = 0;
					}
					LevelHandler.returnTo(returnKey, returnDepth, returnPos);
					returnDepth = -1;
					returnPos = -1;
					returnKey = null;
				}
			}
		});
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(RETURN_KEY, returnKey);
		bundle.put(RETURN_POS, returnPos);
		bundle.put(RETURN_DEPTH, returnDepth);
		bundle.put(SCORE, score);
		bundle.put(MOB_LEVEL_FACTOR, mobLevelBoost);
		bundle.put(MOB_SPAWN_FACTOR, mobSpawnFactor);
		bundle.put(SCORE_BEATEN, scoreBeaten);
		bundle.put(HERO_HP, heroHP);
		bundle.put(BOSS_REFIGHTS, rematchLevels.toArray(new String[0]));
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnKey = bundle.getString(RETURN_KEY);
		returnPos = bundle.getInt(RETURN_POS);
		returnDepth = bundle.getInt(RETURN_DEPTH);
		score = bundle.getInt(SCORE);
		mobLevelBoost = bundle.getFloat(MOB_LEVEL_FACTOR);
		mobSpawnFactor = bundle.getFloat(MOB_SPAWN_FACTOR);
		scoreBeaten = bundle.getBoolean(SCORE_BEATEN);
		heroHP = bundle.contains(HERO_HP) ? bundle.getInt(HERO_HP) : -1;
		rematchLevels = new ArrayList<>(Arrays.asList(bundle.getStringArray(BOSS_REFIGHTS)));
	}

	@Override
	public boolean collect(Bag container, @NotNull Char ch) {
		if (heroHP == -1 && ch instanceof Hero) {
			heroHP = ch.HP;
		}
		return super.collect(container, ch);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc") + "\n\n" + statsInfo();
	}

	public String statsInfo() {
		if (isIdentified()) {
			return Messages.get(this, "stats");
		} else {
			return Messages.get(this, "typical_stats");
		}
	}

	public static Item tryForBonusDrop() {
		Item item;
		do {
			if ((dropsToRare <= 0 || Random.Int(dropsToRare) == 0) & !latestDropWasRare) {// 1/10 chance
				item = genRareDrop();
				latestDropWasRare = true;
				dropsToRare += 10;
			} else {
				item = genStandardDrop();
				dropsToRare--;
			}
		} while (!item.canSpawn());

		return item;
	}

	private static Item genStandardDrop(){
		float roll = Random.Float();
		if (roll < 0.3f){ //30% chance
			Item result = new Gold().random();
			result.quantity(Math.round(result.quantity() * Random.NormalFloat(0.33f, 1f)));
			return result;
		} else if (roll < 0.8f){ //50% chance
			return genBasicConsumable();
		} else { //20% chance
			return genExoticConsumable();
		}

	}

	private static Item genBasicConsumable(){
		float roll = Random.Float();
		if (roll < 0.4f){ //40% chance
			//
			Scroll scroll;
			do {
				scroll = (Scroll) Generator.random(Generator.Category.SCROLL);
			} while (scroll == null);
			return scroll;
		} else if (roll < 0.6f){ //20% chance to drop a minor food item
			return Random.Int(2) == 0 ? new SmallRation() : new MysteryMeat();
		} else { //40% chance
			return Generator.random(Generator.Category.POTION);
		}
	}

	private static Item genExoticConsumable(){
		float roll = Random.Float();
		if (roll < 0.2f){ //20% chance
			return Generator.random(Generator.Category.POTION_EXOTIC);
		} else if (roll < 0.5f) { //30% chance
			Scroll scroll;
			do {
				scroll = (Scroll) Generator.random(Generator.Category.SCROLL_EXOTIC);
			} while (scroll == null);
			return scroll;
		} else { //50% chance
			return Random.Int(2) == 0 ? new SmallRation() : new MysteryMeat();
		}
	}

	private static Item genRareDrop(){
		float roll = Random.Float();
		if (roll < 0.3f){ //30% chance
			Item result = new Gold().random();
			result.quantity(Math.round(result.quantity() * Random.NormalFloat(3f, 6f)));
			return result;
		} else if (roll < 0.6f){ //30% chance
			return genHighValueConsumable();
		} else if (roll < 0.9f){ //30% chance
			Item result;
			int random = Random.Int(4);
			switch (random){
				default:
				case 1:
					result = Generator.random(Generator.Category.RING);
					break;
				case 2:
					result = Generator.random(Generator.Category.WAND);
					break;
				case 3:
					result = Generator.random(Generator.Category.RELIC);
			}
			result.uncurse();
			result.cursedKnown = true;
			return result;
		} else { //10% chance
			if (Random.Int(3) != 0){
				Weapon weapon = Generator.randomWeapon();
				weapon.enchant(Weapon.Enchantment.random());
				weapon.uncurse();
				weapon.cursedKnown = true;
				return weapon;
			} else {
				Shield shield = (Shield) Generator.random(Generator.Category.SHIELD);
				//shield.inscribe(Armor.Glyph.random());
				shield.uncurse();
				shield.cursedKnown = true;
				return shield;
			}
		}
	}

	private static Item genHighValueConsumable(){
		switch( Random.Int(4) ){ //25% chance each
			case 0: default:
				return Generator.random(Generator.Category.SPELL);
			case 1:
				return new StoneOfEnchantment().quantity(3);
			case 2:
				return Generator.random(Generator.Category.ELIXIR);
			case 3:
				return new MeatPie();
		}
	}

	public static void dropItem(Char ch, int quantity) {
		ArrayList<Integer> positions = new ArrayList<>();
		//Build a list of all passable squares adjacent to the mob
		for (int j = 0; j < 8; j++) {
			int pos = ch.pos + PathFinder.NEIGHBOURS8[j];
			if (Dungeon.level.passable(pos)) {
				positions.add(pos);
			}
		}

		for (int i = 0; i < quantity; i++) {
			Dungeon.level.drop(LuckyBadge.tryForBonusDrop(), Random.element(positions)).sprite.drop(ch.pos);
		}
	}

	public static class LuckBuff extends FlavourBuff {

		public static final float DURATION = 100f;

		{
			type = buffType.POSITIVE;
		}

		@Override
		public int icon() {
			return BuffIndicator.LIGHT;
		}

		public int nDrops() {
			return Math.max(1, Math.round(10*(cooldown()/DURATION)));
		}

		@Override
		public String desc() {
			return Messages.get(this, "desc", dispTurns());
		}

		@Override
		public float iconFadePercent() {
			return Math.max(0, (DURATION - visualcooldown()) / DURATION);
		}
	}

	public static class WndChooseRefight extends Window {
		public WndChooseRefight() {
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new Image(Assets.Interfaces.LOADING_SEWERS));
			titlebar.label(Messages.get(this, "title"));
			titlebar.setRect(0, 0, WIDTH, 0);
			add( titlebar );

			float pos = titlebar.bottom() + GAP;

			RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "body", CPDSettings.getGrindingHighScore()), 6 );
			message.maxWidth(WIDTH);
			message.setPos(0, pos);
			add( message );

			pos = message.bottom() + GAP;

			for (String bossID : rematchLevels()) {
				RedButton button = new RedButton(Messages.get(Boss.class, bossID)) {
					@Override
					protected void onClick() {
						returnKey = Dungeon.key;
						returnPos = Dungeon.hero.pos;
						returnDepth = Dungeon.depth;
						LevelHandler.move(bossID, Messages.get(this, AC_REMATCH), LevelHandler.Mode.RESET, 0, -1);
					}
				};
				button.setRect(0, pos, WIDTH, BTN_HEIGHT);
				add(button);
				pos += button.height() + GAP;
			}
			resize(WIDTH, (int) (pos + GAP));
		}
	}

	public static class WndChooseGrind extends Window {
		public WndChooseGrind() {
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new Image(Assets.Interfaces.LOADING_PRISON));
			titlebar.label(Messages.get(this, "title"));
			titlebar.setRect(0, 0, WIDTH, 0);
			add( titlebar );

			float pos = titlebar.bottom() + GAP;

			RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "body", CPDSettings.getGrindingHighScore()), 6 );
			message.maxWidth(WIDTH);
			message.setPos(0, pos);
			add( message );

			pos = message.bottom() + GAP;

			if (mobLevelBoost > 5) {
				mobLevelBoost = 5;
			}

			OptionSlider powerSlider = new OptionSlider(Messages.get(this, "choose_power"),
					"-1", "+5", -1, 5) {
				@Override
				protected void onChange() {
					mobLevelBoost = getSelectedValue();
				}
			};
			powerSlider.setSelectedValue((int) mobLevelBoost);
			powerSlider.setRect(0, pos, WIDTH, BTN_HEIGHT);
			add(powerSlider);

			pos = powerSlider.bottom() + GAP;

			OptionSlider spawnDelaySlider = new OptionSlider(Messages.get(this, "choose_spawn_delay"),
					"1x", "0.25x", 1, 4) {
				@Override
				protected void onChange() {
					mobSpawnFactor = getSelectedValue();
				}
			};
			spawnDelaySlider.setSelectedValue((int) mobSpawnFactor);
			spawnDelaySlider.setRect(0, pos, WIDTH, BTN_HEIGHT);
			add(spawnDelaySlider);


			pos = spawnDelaySlider.bottom() + GAP;

			Button okButton = new RedButton(Messages.get(this, "confirm")) {
				@Override
				protected void onClick() {
					super.onClick();
					returnKey = Dungeon.key;
					returnPos = Dungeon.hero.pos;
					returnDepth = Dungeon.depth;
					LevelHandler.move(AC_GRIND, Messages.get(LuckyBadge.class, AC_GRIND), LevelHandler.Mode.RETURN, 0, -1);
				}
			};
			okButton.setRect(0, pos, WIDTH, BTN_HEIGHT);
			add(okButton);

			pos = okButton.bottom() + GAP;

			resize(WIDTH, (int) pos);
		}
	}
}
