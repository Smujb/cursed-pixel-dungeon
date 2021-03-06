/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Cursed Pixel Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.hero.MageNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.HeroSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.ScrollPane;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.Locale;

public class WndHero extends WndTabbed {
	
	private static final int WIDTH		= 115;
	private static final int HEIGHT		= 160;
	
	private StatsTab stats;
	private BuffsTab buffs;
	private AbilitiesTab abilities;

	private LabeledTab abilitiesTab = new LabeledTab( Messages.get(this, "abilities") ) {
		protected void select( boolean value ) {
			super.select( value );
			abilities.visible = abilities.active = selected;
		}
	} ;

	private LabeledTab buffsTab = new LabeledTab( Messages.get(this, "buffs") ) {
		protected void select( boolean value ) {
			super.select( value );
			buffs.visible = buffs.active = selected;
		}
	};

	private LabeledTab statsTab = new LabeledTab( Messages.get(this, "stats") ) {
		protected void select( boolean value ) {
			super.select( value );
			stats.visible = stats.active = selected;
		}
	};

	private SmartTexture icons;
	private TextureFilm film;

	public void switchToAbilities() {
		abilities.visible = abilities.active = abilitiesTab.selected = true;
		abilitiesTab.select(true);
		buffsTab.select(false);
		statsTab.select(false);
		buffs.visible = buffs.active = stats.visible = stats.active = false;
	}
	
	public WndHero() {
		
		super();
		
		resize( WIDTH, HEIGHT );
		
		icons = TextureCache.get( Assets.Interfaces.BUFFS_LARGE );
		film = new TextureFilm( icons, 16, 16 );
		
		stats = new StatsTab();
		add( stats );
		
		buffs = new BuffsTab();
		add( buffs );
		buffs.setRect(0, 0, WIDTH, HEIGHT);
		buffs.setupList();

		abilities = new AbilitiesTab();
		add( abilities );
		
		add( statsTab );

		add( buffsTab );

		add( abilitiesTab );
		layoutTabs();
		
		select( 0 );
	}

	private static class WndConfirmIncrease extends Window {

		public WndConfirmIncrease(Hero.HeroStat stat, RenderedTextBlock block) {
			Hero hero = Dungeon.hero;
			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			title.label( Messages.get(this, "title", stat.getName()));
			title.color(Window.SHPX_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add(title);

			RenderedTextBlock message = PixelScene.renderTextBlock(
					Messages.get(this, "increase_stat_info",
							Messages.format( "+%d%%", Math.round((stat.hpBoost(hero.getStat(stat), hero)-1f)*100)),
							stat.getName()) + " " + Messages.get(this, stat.name()),
					6 );
			message.maxWidth(WIDTH);
			message.setPos(0, title.bottom() + GAP);
			add( message );

			RedButton buttonConfirm = new RedButton(Messages.get(this, "confirm")) {
				@Override
				protected void onClick() {
					super.onClick();
					hero.increaseStat(stat);
					hero.DistributionPoints--;
					block.text(Integer.toString(hero.getStat(stat)));
					hide();
				}
			};
			buttonConfirm.setRect(0, message.bottom() + GAP, WIDTH, BTN_HEIGHT);
			add(buttonConfirm);

			resize(WIDTH, (int) (buttonConfirm.bottom() + GAP));
		}
	}

	private class AbilitiesTab extends Group {

		private static final int GAP = 6;

		private float pos;
		private static final int BTN_WIDTH  = 20;
		private static final int BTN_HEIGHT	= 20;

		private class StatIncreaseButton extends RedButton {

			private final Hero.HeroStat stat;
			private RenderedTextBlock block;

			public StatIncreaseButton(Hero.HeroStat stat, RenderedTextBlock block) {
				super("+");
				this.stat = stat;
				this.block = block;
				setRect(WIDTH*0.8f, pos-BTN_HEIGHT, BTN_WIDTH, BTN_HEIGHT);
			}

			@Override
			protected void onClick() {
				if (Dungeon.hero.DistributionPoints > 0) {
					CPDGame.scene().addToFront(new WndConfirmIncrease(stat, block));
				} else {
					GLog.warning(Messages.get(WndHero.class,"no_points"));
				}
			}
		}

		public AbilitiesTab() {
			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			title.label( Messages.get(this, "title"));
			title.color(Window.SHPX_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add(title);

			pos = title.bottom() + GAP;

			//Current points to distribute
			statSlot( Messages.get(this, "points"), hero.DistributionPoints, hero.DistributionPoints > 0 ? Constants.Colours.PURE_GREEN : Constants.Colours.PURE_WHITE);
			pos += GAP;

			for (Hero.HeroStat stat : Hero.HeroStat.values()) {
				RenderedTextBlock textBlock = statSlot(stat.getName(), hero.getStat(stat), stat.colour());
				StatIncreaseButton statBtn = new StatIncreaseButton(stat, textBlock);
				add( statBtn );
				pos += GAP;
			}
		}

		private RenderedTextBlock statSlot( String label, String value, int colour ) {

			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 10 );
			txt.setPos(0, pos);
			txt.hardlight(colour);
			add( txt );

			txt = PixelScene.renderTextBlock( value, 10 );
			txt.setPos(WIDTH * 0.6f, pos);
			PixelScene.align(txt);
			add( txt );

			pos += GAP + txt.height();
			return txt;
		}

		private RenderedTextBlock statSlot( String label, int value, int colour ) {
			return statSlot( label, Integer.toString( value ), colour );
		}

		public float height() {
			return pos;
		}
	}


	
	private class StatsTab extends Group {
		
		private static final int GAP = 6;
		
		private float pos;
		
		public StatsTab() {
			
			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			if (hero.name().equals(hero.className()))
				title.label( Messages.get(this, "title", hero.lvl, hero.className() ).toUpperCase( Locale.ENGLISH ) );
			else
				title.label((hero.name() + "\n" + Messages.get(this, "title", hero.lvl, hero.className())).toUpperCase(Locale.ENGLISH));
			title.color(Window.SHPX_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add(title);

			pos = title.bottom() + 2*GAP;

			if (hero.shielding() > 0) statSlot( Messages.get(this, "health"), hero.HP + "+" + hero.shielding() + "/" + hero.HT );
			else statSlot( Messages.get(this, "health"), (hero.HP) + "/" + hero.HT );
			statSlot( Messages.get(this, "mana") , hero.mp + "/" + hero.maxMP() );
			statSlot( Messages.get(this, "exp"), hero.exp + "/" + hero.maxExp() );

			pos += GAP;

			statSlot( Messages.get(this, "gold"), Statistics.goldCollected );
			statSlot( Messages.get(this, "depth"), Statistics.deepestFloor );

			pos += GAP;

			if (hero.heroClass == HeroClass.MAGE) {
				RedButton button = new RedButton(Messages.get(MageNPC.class, "view_storage")) {
					@Override
					protected void onClick() {
						GameScene.show(new WndStorage());
					}
				};
				button.setRect(0, pos, width, BTN_HEIGHT);
				add(button);
				pos = button.bottom() + GAP;
			}
		}

		private void statSlot( String label, String value ) {
			
			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 8 );
			txt.setPos(0, pos);
			add( txt );
			
			txt = PixelScene.renderTextBlock( value, 8 );
			txt.setPos(WIDTH * 0.6f, pos);
			PixelScene.align(txt);
			add( txt );
			
			pos += GAP + txt.height();
		}
		
		private void statSlot( String label, int value ) {
			statSlot( label, Integer.toString( value ) );
		}
		
		public float height() {
			return pos;
		}
	}
	
	private class BuffsTab extends Component {
		
		private static final int GAP = 2;
		
		private float pos;
		private ScrollPane buffList;
		private ArrayList<BuffSlot> slots = new ArrayList<>();
		
		public BuffsTab() {
			buffList = new ScrollPane( new Component() ){
				@Override
				public void onClick( float x, float y ) {
					int size = slots.size();
					for (int i=0; i < size; i++) {
						if (slots.get( i ).onClick( x, y )) {
							break;
						}
					}
				}
			};
			add(buffList);
		}
		
		@Override
		protected void layout() {
			super.layout();
			buffList.setRect(0, 0, width, height);
		}
		
		private void setupList() {
			Component content = buffList.content();
			for (Buff buff : Dungeon.hero.buffs()) {
				if (buff.icon() != BuffIndicator.NONE) {
					BuffSlot slot = new BuffSlot(buff);
					slot.setRect(0, pos, WIDTH, slot.icon.height());
					content.add(slot);
					slots.add(slot);
					pos += GAP + slot.height();
				}
			}
			content.setSize(buffList.width(), pos);
			buffList.setSize(buffList.width(), buffList.height());
		}

		private class BuffSlot extends Component {

			private Buff buff;

			Image icon;
			RenderedTextBlock txt;

			public BuffSlot( Buff buff ){
				super();
				this.buff = buff;
				int index = buff.icon();

				icon = new Image( icons );
				icon.frame( film.get( index ) );
				buff.tintIcon(icon);
				icon.y = this.y;
				add( icon );

				txt = PixelScene.renderTextBlock( buff.toString(), 8 );
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
				PixelScene.align(txt);
				add( txt );

			}

			@Override
			protected void layout() {
				super.layout();
				icon.y = this.y;
				txt.setPos(
						icon.width + GAP,
						this.y + (icon.height - txt.height()) / 2
				);
			}
			
			protected boolean onClick ( float x, float y ) {
				if (inside( x, y )) {
					GameScene.show(new WndInfoBuff(buff));
					return true;
				} else {
					return false;
				}
			}
		}
	}
}
