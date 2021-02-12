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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;
import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.messages.Languages;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.services.Updates;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.GameLog;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Toolbar;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class WndSettings extends WndTabbed {

	private static final int WIDTH_P	    = 122;
	private static final int WIDTH_L	    = 223;

	private static final int SLIDER_HEIGHT	= 24;
	private static final int BTN_HEIGHT	    = 18;
	private static final float GAP          = 2;

	private DisplayTab  display;
	private UITab       ui;
	private DataTab     data;
	private AudioTab    audio;
	private LangsTab    langs;
	private DebugTab    debug;

	public static int last_index = 0;

	public WndSettings() {
		super();

		float height;

		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		display = new DisplayTab();
		display.setSize(width, 0);
		height = display.height();
		add( display );

		add( new IconTab(Icons.get(Icons.DISPLAY)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				display.visible = display.active = value;
				if (value) last_index = 0;
			}
		});

		ui = new UITab();
		ui.setSize(width, 0);
		height = Math.max(height, ui.height());
		add( ui );

		add( new IconTab(Icons.get(Icons.PREFS)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				ui.visible = ui.active = value;
				if (value) last_index = 1;
			}
		});

		data = new DataTab();
		data.setSize(width, 0);
		height = Math.max(height, data.height());
		add( data );

		add( new IconTab(Icons.get(Icons.DATA)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				data.visible = data.active = value;
				if (value) last_index = 2;
			}
		});

		audio = new AudioTab();
		audio.setSize(width, 0);
		height = Math.max(height, audio.height());
		add( audio );

		add( new IconTab(Icons.get(Icons.AUDIO)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				audio.visible = audio.active = value;
				if (value) last_index = 3;
			}
		});

		langs = new LangsTab();
		langs.setSize(width, 0);
		height = Math.max(height, langs.height());
		add( langs );


		IconTab langsTab = new IconTab(Icons.get(Icons.LANGS)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				langs.visible = langs.active = value;
				if (value) last_index = 4;
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				switch(Messages.lang().status()){
					case INCOMPLETE:
						icon.hardlight(1.5f, 0, 0);
						break;
					case UNREVIEWED:
						icon.hardlight(1.5f, 0.75f, 0f);
						break;
				}
			}

		};
		add( langsTab );

		debug = new DebugTab();
		debug.setSize(width, 0);
		height = Math.max(height, debug.height());
		add(debug);

		add( new IconTab(Icons.get(Icons.WARNING)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				debug.visible = debug.active = value;
				if (value) last_index = 5;
			}
		});

		resize(width, (int)Math.ceil(height));

		layoutTabs();

		select(last_index);

	}

	@Override
	public void hide() {
		super.hide();
		//resets generators because there's no need to retain chars for languages not selected
		CPDGame.seamlessResetScene(new Game.SceneChangeCallback() {
			@Override
			public void beforeCreate() {
				Game.platform.resetGenerators();
			}
			@Override
			public void afterCreate() {
				//do nothing
			}
		});
	}

	private static class DebugTab extends Component {
		public DebugTab() {
			super();

			RedButton btnCopyLog = new RedButton(Messages.get(this, "copy_log")) {
				@Override
				protected void onClick() {
					super.onClick();
					Clipboard clipboard = Gdx.app.getClipboard();
					clipboard.setContents(CPDGame.getLog());
				}
			};
			btnCopyLog.setRect(0, 0, WIDTH, BTN_HEIGHT);
			add(btnCopyLog);
		}
	}

	private static class DisplayTab extends Component {

		RenderedTextBlock title;
		ColorBlock sep1;
		OptionSlider optScale;
		CheckBox chkSaver;
		RedButton btnOrientation;
		ColorBlock sep2;
		OptionSlider optBrightness;
		OptionSlider optVisGrid;
		CheckBox chkCutscenes;
		CheckBox chkFastLoad;
		OptionSlider particles;

		@Override
		protected void createChildren() {
			title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
			title.hardlight(TITLE_COLOR);
			add(title);

			sep1 = new ColorBlock(1, 1, 0xFF000000);
			add(sep1);

			if ((int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
				optScale = new OptionSlider(Messages.get(this, "scale"),
						(int)Math.ceil(2* Game.density)+ "X",
						PixelScene.maxDefaultZoom + "X",
						(int)Math.ceil(2* Game.density),
						PixelScene.maxDefaultZoom ) {
					@Override
					protected void onChange() {
						if (getSelectedValue() != CPDSettings.scale()) {
							CPDSettings.scale(getSelectedValue());
							CPDGame.seamlessResetScene();
						}
					}
				};
				optScale.setSelectedValue(PixelScene.defaultZoom);
				add(optScale);
			}

			if (!DeviceCompat.isDesktop() && PixelScene.maxScreenZoom >= 2) {
				chkSaver = new CheckBox(Messages.get(this, "saver")) {
					@Override
					protected void onClick() {
						super.onClick();
						if (checked()) {
							checked(!checked());
							CPDGame.scene().add(new WndOptions(
									Messages.get(DisplayTab.class, "saver"),
									Messages.get(DisplayTab.class, "saver_desc"),
									Messages.get(DisplayTab.class, "okay"),
									Messages.get(DisplayTab.class, "cancel")) {
								@Override
								protected void onSelect(int index) {
									if (index == 0) {
										checked(!checked());
										CPDSettings.powerSaver(checked());
									}
								}
							});
						} else {
							CPDSettings.powerSaver(checked());
						}
					}
				};
				chkSaver.checked( CPDSettings.powerSaver() );
				add( chkSaver );
			}

			if (!DeviceCompat.isDesktop()) {
				btnOrientation = new RedButton(PixelScene.landscape() ?
						Messages.get(this, "portrait")
						: Messages.get(this, "landscape")) {
					@Override
					protected void onClick() {
						CPDSettings.landscape(!PixelScene.landscape());
					}
				};
				add(btnOrientation);
			}

			sep2 = new ColorBlock(1, 1, 0xFF000000);
			add(sep2);

			optBrightness = new OptionSlider(Messages.get(this, "brightness"),
					Messages.get(this, "dark"), Messages.get(this, "bright"), -1, 1) {
				@Override
				protected void onChange() {
					CPDSettings.brightness(getSelectedValue());
				}
			};
			optBrightness.setSelectedValue(CPDSettings.brightness());
			add(optBrightness);

			optVisGrid = new OptionSlider(Messages.get(this, "visual_grid"),
					Messages.get(this, "off"), Messages.get(this, "high"), -1, 2) {
				@Override
				protected void onChange() {
					CPDSettings.visualGrid(getSelectedValue());
				}
			};
			optVisGrid.setSelectedValue(CPDSettings.visualGrid());
			add(optVisGrid);


			chkCutscenes = new CheckBox( Messages.get(this, "cutscenes") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.cutscenes(checked());
				}
			};
			chkCutscenes.checked(CPDSettings.cutscenes());
			add(chkCutscenes);

			chkFastLoad = new CheckBox( Messages.get(this, "fast") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.fasterLoading(checked());
				}
			};
			chkFastLoad.checked(CPDSettings.fasterLoading());
			add(chkFastLoad);

			particles = new OptionSlider(Messages.get(this, "particles"), "1", "6", 1, 6) {
				@Override
				protected void onChange() {
					CPDSettings.particles(getSelectedValue());
				}
			};
			particles.setSelectedValue(CPDSettings.particles());
			add(particles);
		}

		@Override
		protected void layout() {

			float bottom = y;

			title.setPos((width - title.width())/2, bottom + GAP);
			sep1.size(width, 1);
			sep1.y = title.bottom() + 2*GAP;

			bottom = sep1.y + 1;

			if (optScale != null){
				optScale.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
				bottom = optScale.bottom();
			}

			if (width > 200 && chkSaver != null && btnOrientation != null) {
				chkSaver.setRect(0, bottom + GAP, width/2-1, BTN_HEIGHT);
				btnOrientation.setRect(chkSaver.right()+ GAP, bottom + GAP, width/2-1, BTN_HEIGHT);
				bottom = btnOrientation.bottom();
			} else {
				if (chkSaver != null) {
					chkSaver.setRect(0, bottom + GAP, width, BTN_HEIGHT);
					bottom = chkSaver.bottom();
				}

				if (btnOrientation != null) {
					btnOrientation.setRect(0, bottom + GAP, width, BTN_HEIGHT);
					bottom = btnOrientation.bottom();
				}
			}

			sep2.size(width, 1);
			sep2.y = bottom + GAP;
			bottom = sep2.y + 1;

			if (width > 200){
				optBrightness.setRect(0, bottom + GAP, width/2-GAP/2, SLIDER_HEIGHT);
				optVisGrid.setRect(optBrightness.right() + GAP, optBrightness.top(), width/2-GAP/2, SLIDER_HEIGHT);
			} else {
				optBrightness.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
				optVisGrid.setRect(0, optBrightness.bottom() + GAP, width, SLIDER_HEIGHT);
			}

			bottom = optVisGrid.bottom() + 1;

			if (width > 200){
				chkCutscenes.setRect(0, bottom + GAP, width/2-GAP/2, SLIDER_HEIGHT);
				chkFastLoad.setRect(chkCutscenes.right() + GAP, chkCutscenes.top(), width/2-GAP/2, SLIDER_HEIGHT);
			} else {
				chkCutscenes.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
				chkFastLoad.setRect(0, chkCutscenes.bottom() + GAP, width, SLIDER_HEIGHT);
			}

			bottom = chkFastLoad.bottom() + 1;

			particles.setRect(0, bottom, width, SLIDER_HEIGHT);
			height = particles.bottom();
		}

	}

	private static class UITab extends Component {

		RenderedTextBlock title;
		ColorBlock sep1;
		RenderedTextBlock barDesc;
		RedButton btnSplit; RedButton btnGrouped; RedButton btnCentered;
		CheckBox chkFlipToolbar;
		CheckBox chkFlipTags;
		ColorBlock sep2;
		CheckBox chkFullscreen;
		CheckBox chkFont;
		ColorBlock sep3;
		RedButton btnKeyBindings;
		CheckBox chkDarkUI;
		OptionSlider quickslots;

		@Override
		protected void createChildren() {
			title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
			title.hardlight(TITLE_COLOR);
			add(title);

			sep1 = new ColorBlock(1, 1, 0xFF000000);
			add(sep1);

			barDesc = PixelScene.renderTextBlock(Messages.get(this, "mode"), 9);
			add(barDesc);

			btnSplit = new RedButton(Messages.get(this, "split")) {
				@Override
				protected void onClick() {
					textColor(TITLE_COLOR);
					btnGrouped.textColor(WHITE);
					btnCentered.textColor(WHITE);
					CPDSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
					Toolbar.updateLayout();
				}
			};
			if (CPDSettings.toolbarMode().equals(Toolbar.Mode.SPLIT.name()))
				btnSplit.textColor(TITLE_COLOR);
			add(btnSplit);

			btnGrouped = new RedButton(Messages.get(this, "group")) {
				@Override
				protected void onClick() {
					btnSplit.textColor(WHITE);
					textColor(TITLE_COLOR);
					btnCentered.textColor(WHITE);
					CPDSettings.toolbarMode(Toolbar.Mode.GROUP.name());
					Toolbar.updateLayout();
				}
			};
			if (CPDSettings.toolbarMode().equals(Toolbar.Mode.GROUP.name()))
				btnGrouped.textColor(TITLE_COLOR);
			add(btnGrouped);

			btnCentered = new RedButton(Messages.get(this, "center")) {
				@Override
				protected void onClick() {
					btnSplit.textColor(WHITE);
					btnGrouped.textColor(WHITE);
					textColor(TITLE_COLOR);
					CPDSettings.toolbarMode(Toolbar.Mode.CENTER.name());
					Toolbar.updateLayout();
				}
			};
			if (CPDSettings.toolbarMode().equals(Toolbar.Mode.CENTER.name()))
				btnCentered.textColor(TITLE_COLOR);
			add(btnCentered);

			chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.flipToolbar(checked());
					Toolbar.updateLayout();
				}
			};
			chkFlipToolbar.checked(CPDSettings.flipToolbar());
			add(chkFlipToolbar);

			chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.flipTags(checked());
					GameScene.layoutTags();
				}
			};
			chkFlipTags.checked(CPDSettings.flipTags());
			add(chkFlipTags);

			sep2 = new ColorBlock(1, 1, 0xFF000000);
			add(sep2);

			chkFullscreen = new CheckBox(Messages.get(this, "fullscreen")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.fullscreen(checked());
				}
			};
			chkFullscreen.checked(CPDSettings.fullscreen());
			chkFullscreen.enable(DeviceCompat.supportsFullScreen());
			add(chkFullscreen);

			chkFont = new CheckBox(Messages.get(this, "system_font")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDGame.seamlessResetScene(new Game.SceneChangeCallback() {
						@Override
						public void beforeCreate() {
							CPDSettings.systemFont(checked());
						}

						@Override
						public void afterCreate() {
							//do nothing
						}
					});
				}
			};
			chkFont.checked(CPDSettings.systemFont());
			add(chkFont);


			sep3 = new ColorBlock(1, 1, 0xFF000000);
			add(sep3);

			btnKeyBindings = new RedButton(Messages.get(this, "key_bindings")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDGame.scene().addToFront(new WndKeyBindings());
				}
			};

			add(btnKeyBindings);

			chkDarkUI = new CheckBox( Messages.get(this, "dark_ui") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.darkUI(checked());
				}
			};
			chkDarkUI.checked(CPDSettings.darkUI());
			add(chkDarkUI);

			quickslots = new OptionSlider(Messages.get(this, "quickslots"), "" + Constants.MIN_QUICKSLOTS, "" + Constants.MAX_QUICKSLOTS, Constants.MIN_QUICKSLOTS, Constants.MAX_QUICKSLOTS) {
				@Override
				protected void onChange() {
					CPDSettings.quickslots(getSelectedValue());
					Toolbar.updateLayout();
				}
			};
			quickslots.setSelectedValue(CPDSettings.quickslots());
			add(quickslots);

		}

		@Override
		protected void layout() {
			title.setPos((width - title.width())/2, y + GAP);
			sep1.size(width, 1);
			sep1.y = title.bottom() + 2*GAP;

			barDesc.setPos((width-barDesc.width())/2f, sep1.y + 1 + GAP);
			PixelScene.align(barDesc);

			int btnWidth = (int)(width - 2* GAP)/3;
			btnSplit.setRect(0, barDesc.bottom() + GAP, btnWidth, 16);
			btnGrouped.setRect(btnSplit.right()+ GAP, btnSplit.top(), btnWidth, 16);
			btnCentered.setRect(btnGrouped.right()+ GAP, btnSplit.top(), btnWidth, 16);

			if (width > 200) {
				chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP, width/2 - 1, BTN_HEIGHT);
				chkFlipTags.setRect(chkFlipToolbar.right() + GAP, chkFlipToolbar.top(), width/2 -1, BTN_HEIGHT);
				sep2.size(width, 1);
				sep2.y = chkFlipTags.bottom() + 2;
				chkFullscreen.setRect(0, sep2.y + 1 + GAP, width/2 - 1, BTN_HEIGHT);
				chkFont.setRect(chkFullscreen.right() + GAP, chkFullscreen.top(), width/2 - 1, BTN_HEIGHT);
				chkDarkUI.setRect(0, chkFont.bottom() + GAP, width/2 - 1, BTN_HEIGHT);
				quickslots.setRect(chkDarkUI.right() + GAP, chkFont.bottom() + GAP, width/2 - 1, BTN_HEIGHT);
			} else {
				chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP, width, BTN_HEIGHT);
				chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP, width, BTN_HEIGHT);
				sep2.size(width, 1);
				sep2.y = chkFlipTags.bottom() + 2;
				chkFullscreen.setRect(0, sep2.y + 1 + GAP, width, BTN_HEIGHT);
				chkFont.setRect(0, chkFullscreen.bottom() + GAP, width, BTN_HEIGHT);
				chkDarkUI.setRect(0, chkFont.bottom() + 1 + GAP, width, BTN_HEIGHT);
				quickslots.setRect(0, chkDarkUI.bottom() + GAP, width, BTN_HEIGHT);
			}

			if (btnKeyBindings != null){
				sep3.size(width, 1);
				sep3.y = quickslots.bottom() + 2;
				btnKeyBindings.setRect(0, sep3.y + 1 + GAP, width, BTN_HEIGHT);
				height = btnKeyBindings.bottom();
			} else {
				height = quickslots.bottom();
			}
		}

	}

	private static class DataTab extends Component{

		RenderedTextBlock title;
		ColorBlock sep1;
		CheckBox chkNews;
		CheckBox chkUpdates;
		CheckBox chkWifi;

		@Override
		protected void createChildren() {
			title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
			title.hardlight(TITLE_COLOR);
			add(title);

			sep1 = new ColorBlock(1, 1, 0xFF000000);
			add(sep1);

			chkNews = new CheckBox(Messages.get(this, "news")){
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.news(checked());
					//News.clearArticles();
				}
			};
			chkNews.checked(CPDSettings.news());
			add(chkNews);

			chkUpdates = new CheckBox(Messages.get(this, "updates")){
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.updates(checked());
					Updates.clearUpdate();
				}
			};
			chkUpdates.checked(CPDSettings.updates());
			add(chkUpdates);

			if (!DeviceCompat.isDesktop()){
				chkWifi = new CheckBox(Messages.get(this, "wifi")){
					@Override
					protected void onClick() {
						super.onClick();
						CPDSettings.WiFi(checked());
					}
				};
				chkWifi.checked(CPDSettings.WiFi());
				add(chkWifi);
			}
		}

		@Override
		protected void layout() {
			title.setPos((width - title.width())/2, y + GAP);
			sep1.size(width, 1);
			sep1.y = title.bottom() + 2*GAP;

			if (width > 200){
				chkNews.setRect(0, sep1.y + 1 + GAP, width/2-1, BTN_HEIGHT);
				chkUpdates.setRect(chkNews.right() + GAP, chkNews.top(), width/2-1, BTN_HEIGHT);
			} else {
				chkNews.setRect(0, sep1.y + 1 + GAP, width, BTN_HEIGHT);
				chkUpdates.setRect(0, chkNews.bottom()+ GAP, width, BTN_HEIGHT);
			}

			float pos = chkUpdates.bottom();
			if (chkWifi != null){
				chkWifi.setRect(0, pos + GAP, width, BTN_HEIGHT);
				pos = chkWifi.bottom();
			}

			height = pos;

		}
	}

	private static class AudioTab extends Component {

		RenderedTextBlock title;
		ColorBlock sep1;
		OptionSlider optMusic;
		CheckBox chkMusicMute;
		ColorBlock sep2;
		OptionSlider optSFX;
		CheckBox chkMuteSFX;
		CheckBox btnVibrate;

		@Override
		protected void createChildren() {
			title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
			title.hardlight(TITLE_COLOR);
			add(title);

			sep1 = new ColorBlock(1, 1, 0xFF000000);
			add(sep1);

			optMusic = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					CPDSettings.musicVol(getSelectedValue());
				}
			};
			optMusic.setSelectedValue(CPDSettings.musicVol());
			add(optMusic);

			chkMusicMute = new CheckBox(Messages.get(this, "music_mute")){
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.music(!checked());
				}
			};
			chkMusicMute.checked(!CPDSettings.music());
			add(chkMusicMute);

			sep2 = new ColorBlock(1, 1, 0xFF000000);
			add(sep2);

			optSFX = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					CPDSettings.SFXVol(getSelectedValue());
					if (Random.Int(100) == 0){
						Sample.INSTANCE.play(Assets.Sounds.MIMIC);
					} else {
						Sample.INSTANCE.play(Random.oneOf(Assets.Sounds.GOLD,
								Assets.Sounds.HIT,
								Assets.Sounds.ITEM,
								Assets.Sounds.SHATTER,
								Assets.Sounds.EVOKE,
								Assets.Sounds.SECRET));
					}
				}
			};
			optSFX.setSelectedValue(CPDSettings.SFXVol());
			add(optSFX);

			chkMuteSFX = new CheckBox( Messages.get(this, "sfx_mute") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.soundFx(!checked());
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
			};
			chkMuteSFX.checked(!CPDSettings.soundFx());
			add( chkMuteSFX );

			btnVibrate = new CheckBox( Messages.get(this, "vibrate") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.vibrate(checked());
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
			};
			btnVibrate.checked(CPDSettings.vibrate());
			add( btnVibrate );
		}

		@Override
		protected void layout() {
			title.setPos((width - title.width())/2, y + GAP);
			sep1.size(width, 1);
			sep1.y = title.bottom() + 2*GAP;

			optMusic.setRect(0, sep1.y + 1 + GAP, width, SLIDER_HEIGHT);
			chkMusicMute.setRect(0, optMusic.bottom() + GAP, width, BTN_HEIGHT);

			sep2.size(width, 1);
			sep2.y = chkMusicMute.bottom() + GAP;

			optSFX.setRect(0, sep2.y + 1 + GAP, width, SLIDER_HEIGHT);
			chkMuteSFX.setRect(0, optSFX.bottom() + GAP, width, BTN_HEIGHT);

			btnVibrate.setRect(0, chkMuteSFX.bottom() + GAP, width, BTN_HEIGHT);

			height = btnVibrate.bottom();
		}

	}

	private static class LangsTab extends Component{

		final static int COLS_P = 3;
		final static int COLS_L = 4;

		final static int BTN_HEIGHT = 11;

		RenderedTextBlock title;
		ColorBlock sep1;
		RenderedTextBlock txtLangName;
		RenderedTextBlock txtLangInfo;
		ColorBlock sep2;
		RedButton[] lanBtns;
		ColorBlock sep3;
		RenderedTextBlock txtTranifex;
		RedButton btnCredits;

		@Override
		protected void createChildren() {
			title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
			title.hardlight(TITLE_COLOR);
			add(title);

			sep1 = new ColorBlock(1, 1, 0xFF000000);
			add(sep1);

			final ArrayList<Languages> langs = new ArrayList<>(Arrays.asList(Languages.values()));

			Languages nativeLang = Languages.matchLocale(Locale.getDefault());
			langs.remove(nativeLang);
			//move the native language to the top.
			langs.add(0, nativeLang);

			final Languages currLang = Messages.lang();

			txtLangName = PixelScene.renderTextBlock( Messages.titleCase(currLang.nativeName()) , 9 );
			if (currLang.status() == Languages.Status.REVIEWED) txtLangName.hardlight(TITLE_COLOR);
			else if (currLang.status() == Languages.Status.UNREVIEWED) txtLangName.hardlight(CharSprite.WARNING);
			else if (currLang.status() == Languages.Status.INCOMPLETE) txtLangName.hardlight(CharSprite.NEGATIVE);
			add(txtLangName);

			txtLangInfo = PixelScene.renderTextBlock(6);
			if (currLang == Languages.ENGLISH) txtLangInfo.text("This is the source language, written by the developer.");
			else if (currLang.status() == Languages.Status.REVIEWED) txtLangInfo.text(Messages.get(this, "completed"));
			else if (currLang.status() == Languages.Status.UNREVIEWED) txtLangInfo.text(Messages.get(this, "unreviewed"));
			else if (currLang.status() == Languages.Status.INCOMPLETE) txtLangInfo.text(Messages.get(this, "unfinished"));
			if (currLang.status() == Languages.Status.UNREVIEWED) txtLangInfo.setHightlighting(true, CharSprite.WARNING);
			else if (currLang.status() == Languages.Status.INCOMPLETE) txtLangInfo.setHightlighting(true, CharSprite.NEGATIVE);
			add(txtLangInfo);

			sep2 = new ColorBlock(1, 1, 0xFF000000);
			add(sep2);

			lanBtns = new RedButton[langs.size()];
			for (int i = 0; i < langs.size(); i++){
				final int langIndex = i;
				RedButton btn = new RedButton(Messages.titleCase(langs.get(i).nativeName()), 8){
					@Override
					protected void onClick() {
						super.onClick();
						Messages.setup(langs.get(langIndex));
						CPDGame.seamlessResetScene(new Game.SceneChangeCallback() {
							@Override
							public void beforeCreate() {
								CPDSettings.language(langs.get(langIndex));
								GameLog.wipe();
								Game.platform.resetGenerators();
							}
							@Override
							public void afterCreate() {
								//do nothing
							}
						});
					}
				};
				if (currLang == langs.get(i)){
					btn.textColor(TITLE_COLOR);
				} else {
					switch (langs.get(i).status()) {
						case INCOMPLETE:
							btn.textColor(0x888888);
							break;
						case UNREVIEWED:
							btn.textColor(0xBBBBBB);
							break;
					}
				}
				lanBtns[i] = btn;
				add(btn);
			}

			sep3 = new ColorBlock(1, 1, 0xFF000000);
			add(sep3);

			txtTranifex = PixelScene.renderTextBlock(6);
			txtTranifex.text(Messages.get(this, "transifex"));
			add(txtTranifex);

			if (currLang != Languages.ENGLISH) {
				String credText = Messages.titleCase(Messages.get(this, "credits"));
				btnCredits = new RedButton(credText, credText.length() > 9 ? 6 : 9) {
					@Override
					protected void onClick() {
						super.onClick();
						String creds = "";
						String creds2 = "";
						String[] reviewers = currLang.reviewers();
						String[] translators = currLang.translators();

						ArrayList<String> total = new ArrayList<>();
						total.addAll(Arrays.asList(reviewers));
						total.addAll(Arrays.asList(reviewers));
						total.addAll(Arrays.asList(translators));
						int translatorIdx = reviewers.length;

						//we have 2 columns in wide mode
						boolean wide = (2 * reviewers.length + translators.length) > (PixelScene.landscape() ? 15 : 30);

						int i;
						if (reviewers.length > 0) {
							creds += Messages.titleCase(Messages.get(LangsTab.this, "reviewers"));
							creds2 += "";
							boolean col2 = false;
							for (i = 0; i < total.size(); i++) {
								if (i == translatorIdx){
									creds += "\n\n" + Messages.titleCase(Messages.get(LangsTab.this, "translators"));
									creds2 += "\n\n";
									if (col2) creds2 += "\n";
									col2 = false;
								}
								if (wide && col2) {
									creds2 += "\n-" + total.get(i);
								} else {
									creds += "\n-" + total.get(i);
								}
								col2 = !col2 && wide;
							}
						}

						Window credits = new Window(0, 0, 0, Chrome.get(Chrome.Type.TOAST));

						int w = wide ? 125 : 60;

						RenderedTextBlock title = PixelScene.renderTextBlock(6);
						title.text(Messages.titleCase(Messages.get(LangsTab.this, "credits")), w);
						title.hardlight(SHPX_COLOR);
						title.setPos((w - title.width()) / 2, 0);
						credits.add(title);

						RenderedTextBlock text = PixelScene.renderTextBlock(5);
						text.setHightlighting(false);
						text.text(creds, 65);
						text.setPos(0, title.bottom() + 2);
						credits.add(text);

						if (wide) {
							RenderedTextBlock rightColumn = PixelScene.renderTextBlock(5);
							rightColumn.setHightlighting(false);
							rightColumn.text(creds2, 65);
							rightColumn.setPos(65, title.bottom() + 6);
							credits.add(rightColumn);
						}

						credits.resize(w, (int) text.bottom() + 2);
						CPDGame.scene().addToFront(credits);
					}
				};
				add(btnCredits);
			}

		}

		@Override
		protected void layout() {
			title.setPos((width - title.width())/2, y + GAP);
			sep1.size(width, 1);
			sep1.y = title.bottom() + 2*GAP;

			txtLangName.setPos( (width - txtLangName.width())/2f, sep1.y + 1 + GAP );
			PixelScene.align(txtLangName);

			txtLangInfo.setPos(0, txtLangName.bottom() + 2*GAP);
			txtLangInfo.maxWidth((int)width);

			y = txtLangInfo.bottom() + GAP;
			int x = 0;

			sep2.size(width, 1);
			sep2.y = y;
			y += 2;

			int cols = PixelScene.landscape() ? COLS_L : COLS_P;
			int btnWidth = (int)Math.floor((width - (cols-1)) / cols);
			for (RedButton btn : lanBtns){
				btn.setRect(x, y, btnWidth, BTN_HEIGHT);
				btn.setPos(x, y);
				x += btnWidth+1;
				if (x + btnWidth > width){
					x = 0;
					y += BTN_HEIGHT+1;
				}
			}
			if (x > 0){
				y += BTN_HEIGHT+1;
			}

			sep3.size(width, 1);
			sep3.y = y;
			y += 2;

			if (btnCredits != null){
				btnCredits.setSize(btnCredits.reqWidth() + 2, 16);
				btnCredits.setPos(width - btnCredits.width(), y);

				txtTranifex.setPos(0, y);
				txtTranifex.maxWidth((int)btnCredits.left());

				height = Math.max(btnCredits.bottom(), txtTranifex.bottom());
			} else {
				txtTranifex.setPos(0, y);
				txtTranifex.maxWidth((int)width);

				height = txtTranifex.bottom();
			}

		}
	}
}