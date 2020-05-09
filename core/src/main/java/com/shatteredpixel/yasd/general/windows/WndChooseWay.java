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

import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.items.TomeOfMastery;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class WndChooseWay extends Window {

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 18;
	private static final float GAP = 2;

	public WndChooseWay(final TomeOfMastery tome, final HeroClass cl) {

		super();
		final HeroSubClass[] subClasses = cl.subClasses();
		ArrayList<RedButton> subClassButtons = new ArrayList<>();
		if (subClasses.length < 1) {
			return;
		}
		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(tome.image(), null));
		titlebar.label(tome.name());
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextBlock hl = PixelScene.renderTextBlock(6);
		hl.text(Messages.get(this, "message"), WIDTH);
		hl.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(hl);
		int extra = 0;
		for (HeroSubClass subClass : subClasses) {
			final HeroSubClass SubClass = subClass;
			RedButton btnWay = new RedButton(SubClass.title().toUpperCase()) {
				@Override
				protected void onClick() {
					hide();
					tome.choose(SubClass);
				}
			};
			RedButton btnDesc = new RedButton(Messages.get(this, "info")) {
				@Override
				protected void onClick() {
					Game.scene().addToFront(new WndTitledMessage(new ItemSprite(tome.image(), null), SubClass.title().toUpperCase(), SubClass.desc()));
				}
			};
			subClassButtons.add(btnWay);

			btnWay.setRect(0, hl.bottom() + GAP + extra, (WIDTH - GAP) * 0.67f, BTN_HEIGHT);
			add(btnWay);
			btnDesc.setRect((WIDTH - GAP) * 0.67f, hl.bottom() + GAP + extra, (WIDTH - GAP) * 0.33f, BTN_HEIGHT);
			add(btnDesc);
			extra += BTN_HEIGHT;
		}

		RedButton btnCancel = new RedButton(Messages.get(this, "cancel")) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect(0, subClassButtons.get(subClassButtons.size() - 1).bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnCancel);

		resize(WIDTH, (int) btnCancel.bottom());
	}
}
