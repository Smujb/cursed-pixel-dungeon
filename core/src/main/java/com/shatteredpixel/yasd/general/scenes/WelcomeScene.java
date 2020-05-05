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

package com.shatteredpixel.yasd.general.scenes;

import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.YASDSettings;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.Rankings;
import com.shatteredpixel.yasd.general.effects.BannerSprites;
import com.shatteredpixel.yasd.general.effects.Fireball;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.StyledButton;
import com.shatteredpixel.yasd.general.windows.WndStartGame;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.FileUtils;

public class WelcomeScene extends PixelScene {

	private static int LATEST_UPDATE = MainGame.v0_3_3;

	@Override
	public void create() {
		super.create();

		final int previousVersion = YASDSettings.version();

		if (MainGame.versionCode == previousVersion) {
			MainGame.switchNoFade(TitleScene.class);
			return;
		}

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Image title = BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON );
		title.brightness(0.6f);
		add( title );
		
		float topRegion = Math.max(title.height, h*0.45f);
		
		title.x = (w - title.width()) / 2f;
		if (landscape()) {
			title.y = (topRegion - title.height()) / 2f;
		} else {
			title.y = 20 + (topRegion - title.height() - 20) / 2f;
		}

		align(title);

		Image signs = new Image( BannerSprites.get( BannerSprites.Type.PIXEL_DUNGEON_SIGNS ) ) {
			private float time = 0;
			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float)Math.sin( time += Game.elapsed ));
				if (time >= 1.5f*Math.PI) time = 0;
			}
			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		signs.x = title.x + (title.width() - signs.width())/2f;
		signs.y = title.y;
		add( signs );
		
		StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				if (previousVersion == 0){
					YASDSettings.version(MainGame.versionCode);
					WelcomeScene.this.add(new WndStartGame(1, false));
				} else {
					updateVersion(previousVersion);
					MainGame.switchScene(TitleScene.class);
				}
			}
		};

		//FIXME these buttons are very low on 18:9 devices
		if (previousVersion != 0){
			StyledButton changes = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(TitleScene.class, "changes")){
				@Override
				protected void onClick() {
					super.onClick();
					updateVersion(previousVersion);
					MainGame.switchScene(ChangesScene.class);
				}
			};
			okay.setRect(title.x, h-25, (title.width()/2)-2, 21);
			add(okay);

			changes.setRect(okay.right()+2, h-25, (title.width()/2)-2, 21);
			changes.icon(Icons.get(Icons.CHANGES));
			add(changes);
		} else {
			okay.text(Messages.get(TitleScene.class, "enter"));
			okay.setRect(title.x, h-25, title.width(), 21);
			okay.icon(Icons.get(Icons.ENTER));
			add(okay);
		}

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		if (previousVersion == 0) {
			message = Messages.get(this, "welcome_msg");
		} else if (previousVersion <= MainGame.versionCode) {
			if (previousVersion < LATEST_UPDATE){
				message = Messages.get(this, "update_intro");
				message += "\n\n" + Messages.get(this, "update_msg");
			} else {
				message = Messages.get(this, "patch_intro");
				message += "\n";
				message += "\n" + Messages.get(this, "patch");

			}
		} else {
			message = Messages.get(this, "what_msg");
		}
		text.text(message, w-20);
		float textSpace = h - title.y - (title.height() - 10) - okay.height() - 2;
		text.setPos((w - text.width()) / 2f, title.y+(title.height() - 10) + ((textSpace - text.height()) / 2));
		add(text);

	}

	private void updateVersion(int previousVersion){
		
		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){
			try {
				Rankings.INSTANCE.load();
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
			}
		}
		
		YASDSettings.version(MainGame.versionCode);
	}

	private void placeTorch( float x, float y ) {
		Fireball fb = new Fireball();
		fb.setPos( x, y );
		add( fb );
	}
	
}
