package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.food.MeatPie;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.food.SmallRation;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfUpgrade;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.levels.GrindLevel;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
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
import com.watabou.utils.Random;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LuckyBadge extends Power {

	{
		image = ItemSpriteSheet.BADGE;

		cursed = false;

		defaultAction = AC_INFO;
	}

	public static final String AC_GRIND = "grind";
	public static final String AC_HOME = "home";
	public static final String AC_RETURN = "return";
	private static String MOB_LEVEL_FACTOR = "mob_level_factor";
	private static String MOB_SPAWN_FACTOR = "mob_spawn_factor";
	private static String SCORE = "score";
	private static String SCORE_BEATEN = "score_beaten";

	public static float mobLevelFactor = 1f;
	public static float mobSpawnFactor = 1f;
	public static int score = 0;
	public static boolean scoreBeaten = false;

	public enum Type {NONE, GRIND, SPEED}

	//Testing
	public Type type = Type.GRIND;
	private static final float INCREASE_PER_DROP = 25;

	private static int dropsToRare = 0;
	private static float dropsToUpgrade = INCREASE_PER_DROP;

	private static final String RETURN_POS = "returnPos";
	private static final String RETURN_DEPTH = "returnDepth";
	private static final String RETURN_KEY = "returnKey";

	private static int returnPos = -1;
	private static String returnKey = null;
	private static int returnDepth = -1;
	private static boolean latestDropWasRare = false;

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = new ArrayList<>();
		if (Dungeon.key.equals(AC_GRIND) || Dungeon.key.equals(AC_HOME)) {
			actions.add(AC_RETURN);
		} else {
			if (type == Type.GRIND) {
				actions.add(AC_GRIND);
			}
			actions.add(AC_HOME);
		}
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
				break;
			case AC_HOME:
				returnKey = Dungeon.key;
				returnPos = hero.pos;
				returnDepth = Dungeon.depth;
				LevelHandler.move(AC_HOME, Messages.get(this, AC_HOME), LevelHandler.Mode.RETURN, 0, -1);
				break;
			case AC_RETURN:
				doReturn();
				break;
		}
	}

	public static void doReturn() {
		if (scoreBeaten) {
			GLog.p(Messages.get(LuckyBadge.class, "new_high_score", score));
			scoreBeaten = false;
		}
		score = 0;
		Hero hero = Dungeon.hero;
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
				mob.die(new Char.DamageSrc(Element.META));
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
		bundle.put(MOB_LEVEL_FACTOR, mobLevelFactor);
		bundle.put(MOB_SPAWN_FACTOR, mobSpawnFactor);
		bundle.put(SCORE_BEATEN, scoreBeaten);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnKey = bundle.getString(RETURN_KEY);
		returnPos = bundle.getInt(RETURN_POS);
		returnDepth = bundle.getInt(RETURN_DEPTH);
		score = bundle.getInt(SCORE);
		mobLevelFactor = bundle.getFloat(MOB_LEVEL_FACTOR);
		mobSpawnFactor = bundle.getFloat(MOB_SPAWN_FACTOR);
		scoreBeaten = bundle.getBoolean(SCORE_BEATEN);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc") + "\n\n" + statsInfo();
	}

	public String statsInfo() {
		if (isIdentified()) {
			float dropChance = 100f * (1 / dropsToUpgrade);
			if (dropsToUpgrade <= 0) {//Display 100% chance if the value goes negative, as a scroll is guaranteed
				dropChance = 100f;
			}
			return Messages.get(this, "stats", new DecimalFormat("#.##").format(dropChance));
		} else {
			return Messages.get(this, "typical_stats");
		}
	}

	public static Item tryForBonusDrop() {
		Item item;
		do {
			if ((dropsToUpgrade < 1) || (Random.Int((int) dropsToUpgrade) == 0)) {
				item = new ScrollOfUpgrade();
				dropsToUpgrade += INCREASE_PER_DROP;
			} else {
				if ((dropsToRare <= 0 || Random.Int(dropsToRare) == 0) & !latestDropWasRare) {// 1/10 chance
					item = genRareDrop();
					latestDropWasRare = true;
					dropsToUpgrade -= 3;
					dropsToRare += 10;
				} else {
					item = genStandardDrop();
					dropsToUpgrade--;
					dropsToRare--;
				}

			}
		} while (Challenges.isItemBlocked(item));

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
			} while (scroll == null || scroll instanceof ScrollOfUpgrade);
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
					result = Generator.random(Generator.Category.ARTIFACT);
					break;
				case 1:
					result = Generator.random(Generator.Category.RING);
					break;
				case 2:
					result = Generator.random(Generator.Category.WAND);
					break;
				case 3:
					result = Generator.random(Generator.Category.DRAGON_PENDANT);
			}
			result.randomHigh();
			result.cursed = false;
			result.cursedKnown = true;
			return result;
		} else { //10% chance
			if (Random.Int(3) != 0){
				Weapon weapon = Generator.randomWeapon();
				weapon.randomHigh();
				weapon.enchant(Weapon.Enchantment.random());
				weapon.cursed = false;
				weapon.cursedKnown = true;
				return weapon;
			} else {
				Armor armor = Generator.randomArmor();
				armor.randomHigh();
				armor.inscribe(Armor.Glyph.random());
				armor.cursed = false;
				armor.cursedKnown = true;
				return armor;
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

			if (mobLevelFactor > 3) {
				mobLevelFactor = 3;
			}

			OptionSlider powerSlider = new OptionSlider(Messages.get(this, "choose_power"),
					"1x", "3x", 1, 3) {
				@Override
				protected void onChange() {
					mobLevelFactor = getSelectedValue();
				}
			};
			powerSlider.setSelectedValue((int) mobLevelFactor);
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
					LevelHandler.move(AC_GRIND, Messages.get(this, AC_GRIND), LevelHandler.Mode.RETURN, 0, -1);
				}
			};
			okButton.setRect(0, pos, WIDTH, BTN_HEIGHT);
			add(okButton);

			pos = okButton.bottom() + GAP;

			resize(WIDTH, (int) pos);
		}
	}
}
