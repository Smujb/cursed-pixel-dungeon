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

package com.shatteredpixel.yasd.general.scenes;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.watabou.gltextures.TextureCache;
import com.watabou.glwrap.Blending;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NoosaScript;
import com.watabou.noosa.NoosaScriptNoLighting;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.SkinnedBlock;
import com.watabou.utils.Callback;

//FIXME very messy, going to have to rework at some point. Still better than ILS at least...
public class TextScene extends PixelScene {

	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}

	private Phase phase;
	private float timeLeft;
	private float waitingTime;
	private RenderedTextBlock message;

	private static String text = "TEST";
	private static String continueText = null;
	private static Thread thread = null;
	private static String bgTex;
	private static float scrollSpeed = 0;
	private static Callback onFinish = null;
	private static float fadeTime;
	private static boolean autoFinish;

	@Override
	public void create() {
		super.create();

		if (bgTex == null) {
			try {
				bgTex = Dungeon.newLevel(Dungeon.keyForDepth(), false).loadImg();
				if (bgTex == null || bgTex.isEmpty()) bgTex = Assets.SHADOW;
			} catch (Exception e) {
				bgTex = Assets.SHADOW;
			}
		}

		if (onFinish == null) {
			onFinish = new Callback() {
				@Override
				public void call() {
					MainGame.switchScene(GameScene.class);
				}
			};
		}

		SkinnedBlock bg = new SkinnedBlock(Camera.main.width, Camera.main.height, bgTex ){
			@Override
			protected NoosaScript script() {
				return NoosaScriptNoLighting.get();
			}

			@Override
			public void draw() {
				Blending.disable();
				super.draw();
				Blending.enable();
			}

			@Override
			public void update() {
				super.update();
				offset(0, Game.elapsed * scrollSpeed);
			}
		};
		bg.scale(4, 4);
		add(bg);

		Image im = new Image(TextureCache.createGradient(0xAA000000, 0xBB000000, 0xCC000000, 0xDD000000, 0xFF000000)){
			@Override
			public void update() {
				super.update();
				if (phase == Phase.FADE_IN)         aa = Math.max( 0, (timeLeft - (fadeTime - 0.667f)));
				else if (phase ==  Phase.FADE_OUT)   aa = Math.max( 0, (0.667f - timeLeft));
				else                                aa = 0;
			}
		};
		im.angle = 90;
		im.x = Camera.main.width;
		im.scale.x = Camera.main.height/5f;
		im.scale.y = Camera.main.width;
		add(im);

		message = PixelScene.renderTextBlock( text, 9 );
		message.maxWidth((int) (Camera.main.width*0.8f));
		message.setPos(
				(Camera.main.width - message.width()) / 2,
				(Camera.main.height - message.height()) / 2
		);
		align( message );
		add( message );

		phase = Phase.FADE_IN;

		timeLeft = fadeTime;

		waitingTime = 0f;

		if (thread != null) {
			thread.start();
		}
	}

	@Override
	public void update() {
		super.update();
		waitingTime += Game.elapsed;

		float p = timeLeft / fadeTime;

		switch (phase) {

			case FADE_IN:
				message.alpha(1 - p);
				if ((timeLeft -= Game.elapsed) <= 0) {
					if ((thread == null || !thread.isAlive())) {
						if (autoFinish) {
							fadeOut();
						} else {
							if (continueText != null) {
								message.text(continueText);
								message.setPos(
										(Camera.main.width - message.width()) / 2,
										(Camera.main.height - message.height()) / 2
								);
								align(message);
							}
							PointerArea hotArea = new PointerArea(0, 0, Camera.main.width, Camera.main.height) {
								@Override
								protected void onClick(PointerEvent event) {
									if (phase != Phase.FADE_OUT) {
										fadeOut();
										destroy();
									}
								}
							};
							add(hotArea);
						}
					}
				}
				break;

			case FADE_OUT:
				message.alpha(p);

				if ((timeLeft -= Game.elapsed) <= 0) {
					if (onFinish != null) {
						onFinish.call();
					}
					thread = null;
				}
				break;

		}
	}

	public static void init(String text, String continueText, String bgTex, float scrollSpeed, float fadeTime, Callback onFinish, Thread thread, boolean autoFinish) {
		String firstLine = text;
		Callback callback = onFinish;
		if (text.contains("\n")) {
			firstLine = text.split("\n")[0];
			final String finalText = text.replace(firstLine+"\n", "");
			callback = new Callback() {
				@Override
				public void call() {
					init(finalText, continueText, bgTex, scrollSpeed, fadeTime, onFinish, thread, autoFinish);
				}
			};
		}
		TextScene.text = firstLine;
		TextScene.thread = thread;
		TextScene.bgTex = bgTex;
		TextScene.scrollSpeed = scrollSpeed;
		TextScene.onFinish = callback;
		TextScene.fadeTime = fadeTime;
		TextScene.continueText = continueText;
		TextScene.autoFinish = autoFinish;

		MainGame.switchScene(TextScene.class);
	}

	private void fadeOut() {
		phase = Phase.FADE_OUT;
		timeLeft = fadeTime;
	}

	@Override
	protected void onBackPressed() {
		if ((thread == null || !thread.isAlive()) && phase != Phase.FADE_OUT) {
			fadeOut();
		}
	}
}
