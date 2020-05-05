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

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.watabou.noosa.Image;

public class DiveIndicator extends Tag {

	private Image icon;

	public DiveIndicator() {
		super(0xCDD5C0);

		setSize(24, 24);

		visible = false;

	}

	@Override
	protected void createChildren() {
		super.createChildren();
	}

	@Override
	protected synchronized void layout() {
		super.layout();

		if (icon != null) {
			icon.x = x + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
			PixelScene.align(icon);
		}
	}

	private synchronized void updateImage() {

		if (icon != null) {
			icon.killAndErase();
			icon = null;
		}

		try {

			icon = new Image(Dungeon.level.waterTex());

			icon.frame((int) this.x + 8, (int) this.y + 8, (int) this.width - 8, (int) this.height - 8);

			add(icon);

			icon.x = x + (width - icon.width()) / 2;
			icon.y = y + (height - icon.height()) / 2;
			PixelScene.align(icon);

		} catch (Exception e) {
			MainGame.reportException(e);
		}
	}

	@Override
	public synchronized void update() {
		if (!Dungeon.hero.isAlive())
			visible = false;
		else if (Dungeon.level.deepWater(Dungeon.hero.pos)) {
			if (!visible) {
				visible = true;
				updateImage();
				flash();
			}
		} else {
			if (visible) {
				visible = false;
				updateImage();
			}
		}

		super.update();
	}

	@Override
	protected void onClick() {
		Hero hero = Dungeon.hero;
		if (Dungeon.level.deepWater(hero.pos)) {
			LevelHandler.dive(hero.pos);
		}
	}
}
