/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.Statistics;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
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
	private static final int HEIGHT		= 150;
	
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
		
		icons = TextureCache.get( Assets.BUFFS_LARGE );
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

	private class AbilitiesTab extends Group {

		private static final int GAP = 7;

		private float pos;
		private static final int BTN_WIDTH  = 20;
		private static final int BTN_HEIGHT	= 20;

		private abstract class statIncreaseButton extends RedButton {

			statIncreaseButton() {
				super("+");
				setRect(WIDTH*0.8f, pos-BTN_HEIGHT, BTN_WIDTH, BTN_HEIGHT);
			}

			@Override
			protected void onClick() {
				if (Dungeon.hero.DistributionPoints > 0) {
					Dungeon.hero.DistributionPoints--;
					onBackPressed();
					increaseStat();
					WndHero window = new WndHero();
					window.switchToAbilities();
					GameScene.show(window);
				} else {
					GLog.w(Messages.get(WndHero.class,"no_points"));
				}
			}
			protected abstract void increaseStat();
		}

		AbilitiesTab() {

			Hero hero = Dungeon.hero;

			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar(hero.heroClass, hero.tier()) );
			title.label( Messages.get(this, "title"));
			title.color(Window.SHPX_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add(title);

			pos = title.bottom() + GAP;

			//Current points to distribute
			statSlot( Messages.get(this, "points"), hero.DistributionPoints );
			pos += GAP;

			//Power
			statSlot( Messages.get(this, "power"), hero.getPower() );
			statIncreaseButton btnPower = new statIncreaseButton() {
				@Override
				protected void increaseStat() {
					Dungeon.hero.increasePower();
				}
			};
			add( btnPower );
			pos += GAP;
			//Focus
			statSlot( Messages.get(this, "focus"), hero.getFocus() );
			statIncreaseButton btnFocus = new statIncreaseButton() {
				@Override
				protected void increaseStat() {
					Dungeon.hero.increaseFocus();
				}
			};
			add( btnFocus );
			pos += GAP;
			//Perception
			statSlot( Messages.get(this, "perception"), hero.getPerception() );
			statIncreaseButton btnExpertise = new statIncreaseButton() {
				@Override
				protected void increaseStat() {
					Dungeon.hero.increasePerception();
				}
			};
			add( btnExpertise );
			pos += GAP;
			//Evasion
			statSlot( Messages.get(this, "evasion"), hero.getEvasion());
			statIncreaseButton btnStealth = new statIncreaseButton() {
				@Override
				protected void increaseStat() {
					Dungeon.hero.increaseEvasion();
				}
			};
			add( btnStealth );
			pos += GAP;
			RedButton btnInfo = new RedButton(Messages.get(this, "info")) {
				@Override
				protected void onClick() {
					MainGame.scene().addToFront(new WndTitledMessage( HeroSprite.avatar(hero.heroClass, 6 ), Messages.get(AbilitiesTab.class, "info_title"), Messages.get(AbilitiesTab.class, "info_desc")));
				}
			};
			btnInfo.setRect(WIDTH*0.1f, pos, WIDTH*0.8f, BTN_HEIGHT);
			add(btnInfo);
			pos = btnInfo.bottom();

		}

		private void statSlot( String label, String value ) {

			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 10 );
			txt.setPos(0, pos);
			if (Dungeon.hero.DistributionPoints > 0) {
				txt.hardlight(0x00FF00);
			}
			add( txt );

			txt = PixelScene.renderTextBlock( value, 10 );
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

			statSlot( Messages.get(this, "str"), hero.STR() );
			if (hero.shielding() > 0) statSlot( Messages.get(this, "health"), hero.HP + "+" + hero.shielding() + "/" + hero.HT );
			else statSlot( Messages.get(this, "health"), (hero.HP) + "/" + hero.HT );
			statSlot( Messages.get(this, "exp"), hero.exp + "/" + hero.maxExp() );

			statSlot( Messages.get(this, "morale"), ((int) ((hero.morale / hero.MAX_MORALE)*100) + "%") );

			pos += GAP;

			statSlot( Messages.get(this, "gold"), Statistics.goldCollected );
			statSlot( Messages.get(this, "depth"), Statistics.deepestFloor );

			pos += GAP;
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
