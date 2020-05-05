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

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.watabou.noosa.TextureFilm;

public class BlueJellyFishSprite extends MobSprite{

	public BlueJellyFishSprite(){
		renderShadow = false;
		perspectiveRaise = 0.2f;

		texture(Assets.JELLYFISH);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(1, true);
		idle.frames(frames, 0, 1);

		run = new Animation(10, true);
		run.frames(frames, 0, 1);

		attack = new Animation(14, false);
		attack.frames(frames, 3, 4, 5, 6, 7);

		zap = attack.clone();

		die = new Animation(4, false);
		die.frames(frames, 8, 9, 10, 11);

		play(idle);
	}

	@Override
	public void link(Char ch){
		super.link(ch);
		renderShadow = false;
	}

	@Override
	public void onComplete(Animation anim){
		super.onComplete(anim);

		if(anim == attack){
			GameScene.ripple(ch.pos);
		}
	}
}
