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

import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.Game;
import com.watabou.noosa.PointerArea;

public class WndStory extends Window {

	private static final int WIDTH_P = 125;
	private static final int WIDTH_L = 160;
	private static final int MARGIN = 2;

	/*private static final HashMap<Class<? extends Level>, String> CHAPTERS = new HashMap<>();
	
	static {
		CHAPTERS.put( FirstLevel.class, "sewers" );
		CHAPTERS.put( PrisonLevel.class, "prison" );
		CHAPTERS.put( CavesLevel.class, "caves" );
		CHAPTERS.put( CityLevel.class, "city" );
		CHAPTERS.put( HallsLevel.class, "halls" );
	}*/
	
	private RenderedTextBlock tf;
	
	private float delay;
	
	protected WndStory(String text) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );
		
		tf = PixelScene.renderTextBlock( text, 6 );
		tf.maxWidth(PixelScene.landscape() ?
					WIDTH_L - MARGIN * 2:
					WIDTH_P - MARGIN *2);
		tf.invert();
		tf.setPos(MARGIN, 2);
		add( tf );
		
		add( new PointerArea( chrome ) {
			@Override
			protected void onClick( PointerEvent event ) {
				hide();
			}
		} );
		
		resize( (int)(tf.width() + MARGIN * 2), (int)Math.min( tf.height()+2, 180 ) );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (delay > 0 && (delay -= Game.elapsed) <= 0) {
			shadow.visible = chrome.visible = tf.visible = true;
		}
	}
}
