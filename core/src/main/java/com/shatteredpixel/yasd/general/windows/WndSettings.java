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
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.OptionSlider;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Toolbar;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

//TODO seeing as a fair bit of this is platform-dependant, might be better to have a per-platform wndsettings
public class WndSettings extends WndTabbed {

	private static final int WIDTH		    = 112;
	private static final int HEIGHT         = 180;
	private static final int SLIDER_HEIGHT	= 24;
	private static final int BTN_HEIGHT	    = 18;
	private static final int GAP_TINY 		= 2;
	private static final int GAP_SML 		= 6;
	private static final int GAP_LRG 		= 18;

	private DisplayTab display;
	private UITab ui;
	private AudioTab audio;

	private static int last_index = 0;

	public WndSettings() {
		super();

		display = new DisplayTab();
		add( display );

		ui = new UITab();
		add( ui );

		audio = new AudioTab();
		add( audio );

		add( new IconTab(Icons.get(Icons.DISPLAY)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				display.visible = display.active = value;
				if (value) last_index = 0;
			}
		});

		add( new IconTab(Icons.get(Icons.PREFS)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				ui.visible = ui.active = value;
				if (value) last_index = 1;
			}
		});

		add( new IconTab(Icons.get(Icons.AUDIO)){
			@Override
			protected void select(boolean value) {
				super.select(value);
				audio.visible = audio.active = value;
				if (value) last_index = 2;
			}
		});

		resize(WIDTH, HEIGHT);

		layoutTabs();

		select(last_index);

	}

	private static class DisplayTab extends Group {

		public DisplayTab() {
			super();

			OptionSlider scale = new OptionSlider(Messages.get(this, "scale"),
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
			if ((int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
				scale.setSelectedValue(PixelScene.defaultZoom);
				scale.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
				add(scale);
			}

			float bottom = scale.bottom();

			if (!DeviceCompat.isDesktop()) {
				CheckBox chkSaver = new CheckBox( Messages.get( this, "saver" ) ) {
					@Override
					protected void onClick() {
						super.onClick();
						if (checked()) {
							checked( !checked() );
							CPDGame.scene().add(new WndOptions(
									Messages.get( DisplayTab.class, "saver" ),
									Messages.get( DisplayTab.class, "saver_desc" ),
									Messages.get( DisplayTab.class, "okay" ),
									Messages.get( DisplayTab.class, "cancel" ) ) {
								@Override
								protected void onSelect( int index ) {
									if (index == 0) {
										checked( !checked() );
										CPDSettings.powerSaver( checked() );
									}
								}
							} );
						} else {
							CPDSettings.powerSaver( checked() );
						}
					}
				};
				if (PixelScene.maxScreenZoom >= 2) {
					chkSaver.setRect( 0, scale.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT );
					chkSaver.checked( CPDSettings.powerSaver() );
					add( chkSaver );
				}

				//TODO also need to disable this in android splitscreen
				RedButton btnOrientation = new RedButton( PixelScene.landscape() ?
						Messages.get( this, "portrait" )
						: Messages.get( this, "landscape" ) ) {
					@Override
					protected void onClick() {
						CPDSettings.landscape( !PixelScene.landscape() );
					}
				};
				btnOrientation.setRect( 0, chkSaver.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT );
				add( btnOrientation );

				bottom = btnOrientation.bottom();
			}


			OptionSlider brightness = new OptionSlider(Messages.get(this, "brightness"),

					Messages.get(this, "dark"), Messages.get(this, "bright"), -1, 1) {
				@Override
				protected void onChange() {
					CPDSettings.brightness(getSelectedValue());
				}
			};
			brightness.setSelectedValue(CPDSettings.brightness());
			brightness.setRect(0, bottom + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(brightness);

			OptionSlider tileGrid = new OptionSlider(Messages.get(this, "visual_grid"),
					Messages.get(this, "off"), Messages.get(this, "high"), -1, 2) {
				@Override
				protected void onChange() {
					CPDSettings.visualGrid(getSelectedValue());
				}
			};
			tileGrid.setSelectedValue(CPDSettings.visualGrid());
			tileGrid.setRect(0, brightness.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(tileGrid);

			CheckBox chkCutscenes = new CheckBox( Messages.get(this, "cutscenes") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.cutscenes(checked());
				}
			};

			chkCutscenes.setRect(0, tileGrid.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkCutscenes.checked(CPDSettings.cutscenes());
			add(chkCutscenes);

			CheckBox chkInterlevelScene = new CheckBox( Messages.get(this, "fast") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.fastInterlevelScene(checked());
				}
			};

			chkInterlevelScene.setRect(0, chkCutscenes.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkInterlevelScene.checked(CPDSettings.fastInterlevelScene());
			add(chkInterlevelScene);

			OptionSlider particles = new OptionSlider(Messages.get(this, "particles"), "1", "6", 1, 6) {
				@Override
				protected void onChange() {
					CPDSettings.particles(getSelectedValue());
				}
			};
			particles.setSelectedValue(CPDSettings.particles());
			particles.setRect(0, chkInterlevelScene.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(particles);
		}
	}

	private static class UITab extends Group {

		public UITab() {
			super();

			RenderedTextBlock barDesc = PixelScene.renderTextBlock(Messages.get(this, "mode"), 9);
			barDesc.setPos((WIDTH - barDesc.width()) / 2f, GAP_TINY);
			PixelScene.align(barDesc);
			add(barDesc);

			RedButton btnSplit = new RedButton(Messages.get(this, "split")) {
				@Override
				protected void onClick() {
					CPDSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
					Toolbar.updateLayout();
				}
			};
			btnSplit.setRect(0, barDesc.bottom() + GAP_TINY, 36, 16);
			add(btnSplit);

			RedButton btnGrouped = new RedButton(Messages.get(this, "group")) {
				@Override
				protected void onClick() {
					CPDSettings.toolbarMode(Toolbar.Mode.GROUP.name());
					Toolbar.updateLayout();
				}
			};
			btnGrouped.setRect(btnSplit.right() + GAP_TINY, btnSplit.top(), 36, 16);
			add(btnGrouped);

			RedButton btnCentered = new RedButton(Messages.get(this, "center")) {
				@Override
				protected void onClick() {
					CPDSettings.toolbarMode(Toolbar.Mode.CENTER.name());
					Toolbar.updateLayout();
				}
			};
			btnCentered.setRect(btnGrouped.right() + GAP_TINY, btnSplit.top(), 36, 16);
			add(btnCentered);

			CheckBox chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.flipToolbar(checked());
					Toolbar.updateLayout();
				}
			};
			chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipToolbar.checked(CPDSettings.flipToolbar());
			add(chkFlipToolbar);

			final CheckBox chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.flipTags(checked());
					GameScene.layoutTags();
				}
			};
			chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFlipTags.checked(CPDSettings.flipTags());
			add(chkFlipTags);

			CheckBox chkFullscreen = new CheckBox(Messages.get(this, "fullscreen")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.fullscreen(checked());
				}
			};
			chkFullscreen.setRect(0, chkFlipTags.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			chkFullscreen.checked(CPDSettings.fullscreen());
			if (DeviceCompat.isDesktop()) {
				chkFullscreen.text("Fullscreen");
			}
			chkFullscreen.enable(DeviceCompat.supportsFullScreen());
			add(chkFullscreen);

			CheckBox chkFont = new CheckBox(Messages.get(this, "system_font")) {
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
			chkFont.setRect(0, chkFullscreen.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkFont.checked(CPDSettings.systemFont());
			add(chkFont);

			RedButton btnKeyBindings = new RedButton(Messages.get(this, "key_bindings")) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDGame.scene().addToFront(new WndKeyBindings());
				}
			};

			btnKeyBindings.setRect(0, chkFont.bottom() + GAP_SML, WIDTH, BTN_HEIGHT);
			add(btnKeyBindings);

			CheckBox chkDarkUI = new CheckBox( Messages.get(this, "dark_ui") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.darkUI(checked());
				}
			};

			chkDarkUI.setRect(0, btnKeyBindings.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			chkDarkUI.checked(CPDSettings.darkUI());
			add(chkDarkUI);

			OptionSlider quickslots = new OptionSlider(Messages.get(this, "quickslots"), "" + Constants.MIN_QUICKSLOTS, "" + Constants.MAX_QUICKSLOTS, Constants.MIN_QUICKSLOTS, Constants.MAX_QUICKSLOTS) {
				@Override
				protected void onChange() {
					CPDSettings.quickslots(getSelectedValue());
					Toolbar.updateLayout();
				}
			};
			quickslots.setSelectedValue(CPDSettings.quickslots());
			quickslots.setRect(0, chkDarkUI.bottom() + GAP_TINY, WIDTH, SLIDER_HEIGHT);
			add(quickslots);
		}

	}

	private class AudioTab extends Group {

		public AudioTab() {
			OptionSlider musicVol = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
				@Override
				protected void onChange() {
					CPDSettings.musicVol(getSelectedValue());
				}
			};
			musicVol.setSelectedValue(CPDSettings.musicVol());
			musicVol.setRect(0, 0, WIDTH, SLIDER_HEIGHT);
			add(musicVol);

			CheckBox musicMute = new CheckBox(Messages.get(this, "music_mute")){
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.music(!checked());
				}
			};
			musicMute.setRect(0, musicVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			musicMute.checked(!CPDSettings.music());
			add(musicMute);


			OptionSlider SFXVol = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
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
			SFXVol.setSelectedValue(CPDSettings.SFXVol());
			SFXVol.setRect(0, musicMute.bottom() + GAP_LRG, WIDTH, SLIDER_HEIGHT);
			add(SFXVol);

			CheckBox btnSound = new CheckBox( Messages.get(this, "sfx_mute") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.soundFx(!checked());
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
			};
			btnSound.setRect(0, SFXVol.bottom() + GAP_TINY, WIDTH, BTN_HEIGHT);
			btnSound.checked(!CPDSettings.soundFx());
			add( btnSound );

			CheckBox btnVibrate = new CheckBox( Messages.get(this, "vibrate") ) {
				@Override
				protected void onClick() {
					super.onClick();
					CPDSettings.vibrate(checked());
					Sample.INSTANCE.play( Assets.Sounds.CLICK );
				}
			};
			btnVibrate.setRect(0, btnSound.bottom() + GAP_LRG, WIDTH, BTN_HEIGHT);
			btnVibrate.checked(CPDSettings.vibrate());
			add( btnVibrate );

			resize( WIDTH, (int)btnVibrate.bottom());
		}

	}
}
