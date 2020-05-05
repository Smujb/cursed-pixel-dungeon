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
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Difficulty;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.YASDSettings;
import com.shatteredpixel.yasd.general.GamesInProgress;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.journal.Journal;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.IntroScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.ActionIndicator;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.IconButton;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;

public class WndStartGame extends Window {
	
	private static final int WIDTH    = 120;
	private static final int HEIGHT   = 140;
	private static final int SLIDER_HEIGHT	= 20;
	private static final int GAP_TINY 		= 2;

	public WndStartGame(final int slot, boolean longPress){
		
		Badges.loadGlobal();
		Journal.loadGlobal();

		if (!longPress) {
			YASDSettings.testing(false);
		}

		if (GamesInProgress.selectedClass == null) {
			HeroClass cl = null;
			switch (YASDSettings.lastClass()) {
				case 0:
					cl = HeroClass.WARRIOR;
					break;
				case 1:
					cl = HeroClass.MAGE;
					break;
				case 2:
					cl = HeroClass.ROGUE;
					break;
				case 3:
					cl = HeroClass.HUNTRESS;
					break;
			}
			GamesInProgress.selectedClass = cl;
		}
		
		RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(this, "title"), 12 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos( (WIDTH - title.width())/2f, 3);
		PixelScene.align(title);
		add(title);
		
		float heroBtnSpacing = (WIDTH - 4*HeroBtn.WIDTH)/5f;
		
		float curX = heroBtnSpacing;
		for (HeroClass cl : HeroClass.values()){
			HeroBtn button = new HeroBtn(cl);
			button.setRect(curX, title.height() + 7, HeroBtn.WIDTH, HeroBtn.HEIGHT);
			curX += HeroBtn.WIDTH + heroBtnSpacing;
			add(button);
		}
		
		ColorBlock separator = new ColorBlock(1, 1, 0xFF222222);
		separator.size(WIDTH, 1);
		separator.x = 0;
		separator.y = title.bottom() + 6 + HeroBtn.HEIGHT;
		add(separator);
		
		HeroPane ava = new HeroPane();
		ava.setRect(20, separator.y + 2, WIDTH-30, 80);
		add(ava);
		
		RedButton start = new RedButton(Messages.get(this, "start")){
			@Override
			protected void onClick() {
				if (GamesInProgress.selectedClass == null) return;
				
				super.onClick();
				
				GamesInProgress.curSlot = slot;
				Dungeon.hero = null;
				ActionIndicator.action = null;
				Dungeon.difficulty = Difficulty.fromInt(YASDSettings.difficulty());//I could just call YASDSettings.difficulty() every time I want to check difficulty, but that would mean that changing it on separate runs would interfere with each other.
				YASDSettings.lastClass(GamesInProgress.selectedClass);
				if (YASDSettings.intro()) {
					YASDSettings.intro( false );
					Game.switchScene( IntroScene.class );
				} else {
					LevelHandler.doInit();
				}
			}
			
			@Override
			public void update() {
				if( !visible && GamesInProgress.selectedClass != null){
					visible = true;
				}
				super.update();
			}
		};
		start.visible = false;
		start.setRect(0, HEIGHT - 20, WIDTH, 20);
		add(start);

		OptionSlider difficulty = new OptionSlider(Messages.get(this, "difficulty"),
				Messages.get(this, "easy"), Messages.get(this, "hard"), 1, Difficulty.maxDiff()) {
			@Override
			protected void onChange() {
				YASDSettings.difficulty(getSelectedValue());
			}
		};
		difficulty.setSelectedValue(YASDSettings.difficulty());
		difficulty.setRect(0, start.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
		add(difficulty);

		if (DeviceCompat.isDebug() || Badges.isUnlocked(Badges.Badge.VICTORY)){
			IconButton challengeButton = new IconButton(
					Icons.get( YASDSettings.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF)){
				@Override
				protected void onClick() {
					MainGame.scene().addToFront(new WndChallenges(YASDSettings.challenges(), true) {
						public void onBackPressed() {
							super.onBackPressed();
							icon( Icons.get( YASDSettings.challenges() > 0 ?
									Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF ) );
						}
					} );
				}
				
				@Override
				public void update() {
					if( !visible && GamesInProgress.selectedClass != null){
						visible = true;
					}
					super.update();
				}
			};
			challengeButton.setRect(WIDTH - 20, HEIGHT - 20, 20, 20);
			challengeButton.visible = false;
			add(challengeButton);
			
		} else {
			Dungeon.challenges = 0;
			YASDSettings.challenges(0);
		}

		int bottom = (int) difficulty.bottom();

		if (longPress) {
			bottom += 20;
			final CheckBox BoxTesting = new CheckBox("TEST MODE"){
				@Override
				protected void onClick() {
					super.onClick();
					YASDSettings.testing(checked());
				}
			};
			BoxTesting.setRect(0, difficulty.bottom(), WIDTH, 20);
			BoxTesting.checked(YASDSettings.testing());
			add(BoxTesting);
		}
		
		resize(WIDTH, bottom);
		
	}
	
	private static class HeroBtn extends Button {
		
		private HeroClass cl;
		
		private Image hero;
		
		private static final int WIDTH = 24;
		private static final int HEIGHT = 16;
		
		HeroBtn ( HeroClass cl ){
			super();
			
			this.cl = cl;

			add(hero = new Image(cl.spritesheet(), 0, 90, 12, 15));
			
		}
		
		@Override
		protected void layout() {
			super.layout();
			if (hero != null){
				hero.x = x + (width - hero.width()) / 2f;
				hero.y = y + (height - hero.height()) / 2f;
				PixelScene.align(hero);
			}
		}
		
		@Override
		public void update() {
			super.update();
			if (cl != GamesInProgress.selectedClass){
				if (cl.locked()){
					hero.brightness(0.3f);
				} else {
					hero.brightness(0.6f);
				}
			} else {
				hero.brightness(1f);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			if(cl.locked()){
				MainGame.scene().addToFront( new WndMessage(cl.unlockMsg()));
			} else {
				GamesInProgress.selectedClass = cl;
			}
		}
	}
	
	private static class HeroPane extends Component {
		
		private HeroClass cl;
		
		private Image avatar;
		
		private IconButton heroItem;
		private IconButton heroLoadout;
		private IconButton heroMisc;
		private IconButton heroSubclass;
		
		private RenderedTextBlock name;
		
		private static final int BTN_SIZE = 20;
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			avatar = new Image(Assets.AVATARS);
			avatar.scale.set(2f);
			add(avatar);
			
			heroItem = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					MainGame.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_item")));
				}
			};
			heroItem.setSize(BTN_SIZE, BTN_SIZE);
			add(heroItem);
			
			heroLoadout = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					MainGame.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_loadout")));
				}
			};
			heroLoadout.setSize(BTN_SIZE, BTN_SIZE);
			add(heroLoadout);
			
			heroMisc = new IconButton(){
				@Override
				protected void onClick() {
					if (cl == null) return;
					MainGame.scene().addToFront(new WndMessage(Messages.get(cl, cl.name() + "_desc_misc")));
				}
			};
			heroMisc.setSize(BTN_SIZE, BTN_SIZE);
			add(heroMisc);
			
			heroSubclass = new IconButton(new ItemSprite(ItemSpriteSheet.MASTERY, null)){
				@Override
				protected void onClick() {
					if (cl == null) return;
					String msg = Messages.get(cl, cl.name() + "_desc_subclasses");
					for (HeroSubClass sub : cl.subClasses()){
						msg += "\n\n" + sub.desc();
					}
					MainGame.scene().addToFront(new WndMessage(msg));
				}
			};
			heroSubclass.setSize(BTN_SIZE, BTN_SIZE);
			add(heroSubclass);
			
			name = PixelScene.renderTextBlock(12);
			add(name);
			
			visible = false;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			avatar.x = x;
			avatar.y = y + (height - avatar.height() - name.height() - 4)/2f;
			PixelScene.align(avatar);
			
			name.setPos(
					x + (avatar.width() - name.width())/2f,
					avatar.y + avatar.height() + 3
			);
			PixelScene.align(name);
			
			heroItem.setPos(x + width - BTN_SIZE, y);
			heroLoadout.setPos(x + width - BTN_SIZE, heroItem.bottom());
			heroMisc.setPos(x + width - BTN_SIZE, heroLoadout.bottom());
			heroSubclass.setPos(x + width - BTN_SIZE, heroMisc.bottom());
		}
		
		@Override
		public synchronized void update() {
			super.update();
			if (GamesInProgress.selectedClass != cl){
				cl = GamesInProgress.selectedClass;
				if (cl != null) {
					avatar.frame(cl.ordinal() * 24, 0, 24, 32);
					
					name.text(Messages.capitalize(cl.title()));
					
					switch(cl){
						case WARRIOR:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SEAL, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.RATION, null));
							break;
						case MAGE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.HOLDER, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE, null));
							break;
						case ROGUE:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.DAGGER, null));
							heroMisc.icon(Icons.get(Icons.DEPTH));
							break;
						case HUNTRESS:
							heroItem.icon(new ItemSprite(ItemSpriteSheet.SPIRIT_BOW, null));
							heroLoadout.icon(new ItemSprite(ItemSpriteSheet.GLOVES, null));
							heroMisc.icon(new ItemSprite(ItemSpriteSheet.BOLAS, null));
							break;
					}
					
					layout();
					
					visible = true;
				} else {
					visible = false;
				}
			}
		}
	}

}
