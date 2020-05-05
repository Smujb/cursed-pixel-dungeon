/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Awareness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.MindVision;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.shatteredpixel.yasd.general.windows.WndError;
import com.watabou.noosa.Image;
import com.watabou.utils.PlatformSupport;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DeveloperItem extends Item {
	/*
	This file is mainly not translated as it is for my personal use and the use of testers. Any developer of a mod of this one can feel free to translate it or replace the strings.

	Basically a set of developer tools, allows me to instantly warp to any level and instantly obtain any item so I can test stuff without needing the PC
	 */

	{
		image = ItemSpriteSheet.SKULL;

		cursed = false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions =  super.actions(hero);
		actions.add(AC_DEBUG);
		actions.add(AC_MAP);
		actions.add(AC_KILL);
		actions.add(AC_TP);
		actions.add(AC_ITEM);
		actions.add(AC_CHOOSEDEPTH);
		return actions;
	}

	private static final String AC_DEBUG = "debug";
	private static final String AC_MAP = "map";
	private static final String AC_TP = "tp";
	private static final String AC_CHOOSEDEPTH = "test";
	private static final String AC_ITEM = "item";
	private static final String AC_KILL = "kill";


	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	private static final String BASE_NAME = "com.shatteredpixel.yasd.general.items.";
	private static int getWidth() {
		return 150;
	}

	public static class WndGetItem extends Window {

		private IconTitle titlebar;
		private final Class[] itemClass = new Class[1];
		private final int[] quantity = new int[1];
		private final int[] level = new int[1];
		private final boolean[] identified = new boolean[1];
		private final boolean[] cursed = new boolean[1];
		private RenderedTextBlock message;

		@NotNull
		private static String name(@NotNull Class item) {
			return item.getName().replace(BASE_NAME, "");
		}

		private static Item getItem(Class itemClass) {
			Item item;
			try {
				item = (Item) itemClass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				item = null;
				MainGame.scene().addToFront(new WndError(e.getMessage()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				item = null;
				MainGame.scene().addToFront(new WndError(e.getMessage()));
			}
			return item;
		}

		@Override
		public void onBackPressed() {}//Do nothing

		WndGetItem(Item item) {
			super();
			//Use arrays here so they can be modified by inner classes while still being able to be changed.
			itemClass[0] = item.getClass();
			quantity[0] = 1;
			level[0] = 0;
			cursed[0] = false;
			identified[0] = false;

			titlebar = new IconTitle();

			item = getItem(itemClass[0]);
			titlebar.icon(new ItemSprite(item.image(), null));
			titlebar.label(Messages.titleCase(item.name()));
			titlebar.setRect(0, 0, getWidth(), 0);
			add(titlebar);

			message = PixelScene.renderTextBlock("Choose an item (currently " + name(itemClass[0]) + ")", 6);
			message.maxWidth(getWidth());
			message.setPos(0, titlebar.bottom() + GAP);
			add(message);

			final WndGetItem window = this;

			RedButton btnChoose = new RedButton( "Input Item" ) {
				@Override
				protected void onClick() {
					MainGame.platform.promptTextInput("Enter id of an item you want: ", name(itemClass[0]), Integer.MAX_VALUE, false, "OK", "CANCEL", new PlatformSupport.TextCallback() {

						@Override
						public void onSelect(boolean positive, String text) {
							if (positive) {
								try {
									itemClass[0] = Class.forName(BASE_NAME + text);
									GLog.p("Successfully fetched item.");
								} catch (ClassNotFoundException e) {
									MainGame.reportException(e);
									MainGame.scene().addToFront(new WndError(e.getLocalizedMessage()));
								}
								window.update();
							}
						}
					});
				}
			};
			btnChoose.setRect(0, message.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add( btnChoose );

			OptionSlider quantitySlider = new OptionSlider("Choose quantity",
					"1", "25", 1, 25) {
				@Override
				protected void onChange() {
					quantity[0] = getSelectedValue();
					update();
				}
			};
			quantitySlider.setSelectedValue(quantity[0]);
			quantitySlider.setRect(0, btnChoose.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(quantitySlider);

			OptionSlider levelSlider = new OptionSlider("Choose upgrade level",
					"0", "" + Constants.UPGRADE_LIMIT, 0, Constants.UPGRADE_LIMIT) {
				@Override
				protected void onChange() {
					level[0] = getSelectedValue();
					update();
				}
			};
			levelSlider.setSelectedValue(level[0]);
			levelSlider.setRect(0, quantitySlider.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(levelSlider);

			CheckBox checkIdentified = new CheckBox("Identified") {
				@Override
				protected void onClick() {
					super.onClick();
					WndGetItem.this.identified[0] = checked();
				}
			};
			checkIdentified.setRect(0, levelSlider.bottom(), getWidth(), BTN_HEIGHT);
			checkIdentified.checked(identified[0]);
			add(checkIdentified);

			CheckBox checkCursed = new CheckBox("Cursed") {
				@Override
				protected void onClick() {
					super.onClick();
					WndGetItem.this.cursed[0] = checked();
				}
			};
			checkCursed.setRect(0, checkIdentified.bottom() + GAP, getWidth(), BTN_HEIGHT);
			checkCursed.checked(cursed[0]);
			add(checkCursed);

			RedButton btnGo = new RedButton( "Collect" ) {
				@Override
				protected void onClick() {
					Item item = getItem(itemClass[0]);
					if (item == null) return;
					Char ch = Dungeon.hero;
					int num = 1;
					if (cursed[0]) {
						item.curse();
					}

					if (identified[0]) {
						item.identify();
					}

					if (quantity[0] > 1) {
						if (item.stackable) {
							item.quantity(quantity[0]);
						} else {
							num = quantity[0];
						}
					}

					if (level[0] > 0) {
						item.level(level[0]);
					}
					for (int i = 0; i < num; i++) {
						if (item.collect(ch.belongings.backpack, ch)) {
							GLog.p("Successfully added item " + item.name() + " to entity #" + ch.id() + "'s backpack.");
						} else {
							GLog.i("Item " + item.name() + " could not be added to entity #" + ch.id() + "'s backpack, so it was dropped below them.");
							Dungeon.level.drop(item, ch.pos).sprite.drop();
						}
					}

				}
			};
			btnGo.setRect(0, checkCursed.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnGo );

			RedButton btnCancel = new RedButton( "Close" ) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect(getWidth()/2, checkCursed.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnCancel );

			resize(getWidth(), (int) (btnGo.bottom() + GAP));

		}

		@Override
		public synchronized void update() {
			super.update();
			Item item = getItem(itemClass[0]);
			titlebar.icon(new ItemSprite(item.image(), null));
			titlebar.label(Messages.titleCase(item.name()));
			titlebar.setRect(0, 0, getWidth(), 0);

			message.text("Choose an item (currently " + name(itemClass[0]) + ")");
			message.maxWidth(getWidth());
			message.setPos(0, titlebar.bottom() + GAP);
		}
	}

	public static class WndChooseDepth extends Window {

		private String[] key;

		private int[] depth;

		WndChooseDepth(Level level) {
			super();
			depth = new int[1];
			depth[0] = Dungeon.depth;
			key = new String[1];
			key[0] = Dungeon.key;
			IconTitle titlebar = new IconTitle();
			titlebar.icon( new Image( level.loadImg() ));
			titlebar.label("Choose Level");
			titlebar.setRect(0, 0, getWidth(), 0);
			add( titlebar );

			RenderedTextBlock message = PixelScene.renderTextBlock( "Choose depth and level key:", 6 );
			message.maxWidth(getWidth());
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );
			RedButton btnChoose = new RedButton( "Enter Key" ) {
				@Override
				protected void onClick() {
					MainGame.platform.promptTextInput("Enter key of level to fetch: ", Random.element(Dungeon.staticLevels.keySet().toArray(new String[0])), Integer.MAX_VALUE, false, "OK", "CANCEL", new PlatformSupport.TextCallback() {

						@Override
						public void onSelect(boolean positive, String text) {
							if (positive) {
								key[0] = text.toLowerCase();
								Level level = Dungeon.newLevel(key[0], false);
								if (level != null) {
									icon(new Image(level.loadImg() == null ? level.loadImg() : Assets.SHADOW));
									WndChooseDepth.this.update();
									GLog.p("Level exists.");
								} else {
									GLog.n("You won't find anything much there...");
								}
							}
						}
					});
				}
			};
			btnChoose.setRect(0, message.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add( btnChoose );

			OptionSlider depthSlider = new OptionSlider("Choose depth (Currently: " + Dungeon.depth + ")",
					"0", "" + Constants.MAX_DEPTH, 0, Constants.MAX_DEPTH) {
				@Override
				protected void onChange() {
					depth[0] = getSelectedValue();
					update();
				}
			};
			depthSlider.setSelectedValue(Dungeon.depth);
			depthSlider.setRect(0, btnChoose.bottom() + GAP, getWidth(), BTN_HEIGHT);
			add(depthSlider);

			RedButton btnCancel = new RedButton( "CANCEL" ) {
				@Override
				protected void onClick() {
					hide();
				}
			};
			btnCancel.setRect(0, depthSlider.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnCancel );

			RedButton btnGo = new RedButton( "GO" ) {
				@Override
				protected void onClick() {
					LevelHandler.move(key[0], "", LevelHandler.Mode.MOVE, depth[0], -1);
				}
			};
			btnGo.setRect(getWidth()/2, depthSlider.bottom() + GAP, getWidth()/2, BTN_HEIGHT);
			add( btnGo );

			resize(getWidth(), (int) btnGo.bottom());
		}
	}

	@Override
	public void execute(Hero hero, String action) {
		super.execute(hero, action);
		switch (action) {
			case AC_DEBUG:
				GLog.p("Current level key: " + Dungeon.key + " (capitalisation important).\n");
				int[] coords = Dungeon.level.getXY(hero.pos);
				GLog.p("Position in map (X, Y): " + coords[0] + ", " + coords[1] + ".");
				break;
			case AC_MAP:
				new ScrollOfMagicMapping().doRead();
				Buff.affect(hero, Awareness.class, Awareness.DURATION*5);
				Buff.affect(hero, MindVision.class, MindVision.DURATION);
				break;
			case AC_TP:
				new ScrollOfTeleportation().empoweredRead();
				break;
			case AC_CHOOSEDEPTH:
				MainGame.scene().addToFront(new WndChooseDepth(Dungeon.level));
				break;
			case AC_ITEM:
				MainGame.scene().addToFront(new WndGetItem(this));
				break;
			case AC_KILL:
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					mob.die(new Char.DamageSrc(Element.SPIRIT).ignoreDefense());
					GLog.i("All ded");
				}
				break;
		}
	}
}
