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

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.scenes.TitleScene;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.utils.DeviceCompat;

import static com.shatteredpixel.yasd.general.scenes.AboutScene.POPUP_YASD;

public class DiscordButton extends Button {
	
	private Image image;
	
	@Override
	protected void createChildren() {
		super.createChildren();
		
		image = Icons.DISCORD.get();
		add( image );
	}
	
	@Override
	protected void layout() {
		super.layout();
		
		image.x = x + (width - image.width)/2f;
		image.y = y + (height - image.height)/2f;
		PixelScene.align(image);
	}
	
	@Override
	protected void onPointerDown() {
		image.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.SND_CLICK );
	}
	
	@Override
	protected void onPointerUp() {
		image.resetColor();
	}
	
	@Override
	protected void onClick() {
		WndOptions wnd = new WndOptions(Messages.get(TitleScene.class, "discord"),
				Messages.get(TitleScene.class, "discord_body"),
				Messages.get(TitleScene.class, "discord_button")){
			@Override
			protected void onSelect(int index) {
				DeviceCompat.openURI("https://" + POPUP_YASD);
			}
		};
		parent.add(wnd);
	}
}
