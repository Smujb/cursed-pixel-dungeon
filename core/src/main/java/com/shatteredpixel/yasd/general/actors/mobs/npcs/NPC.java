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

package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Heap;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.WndTitledMessage;
import com.watabou.noosa.Image;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public abstract class NPC extends Mob {

	{
		HP = HT = 1;
		EXP = 0;

		alignment = Alignment.NEUTRAL;
		state = PASSIVE;
	}

	protected void throwItem() {
		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null && heap.type == Heap.Type.HEAP) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int( 8 )];
			} while (!Dungeon.level.passable(n) && !Dungeon.level.avoid(n));
			Dungeon.level.drop( heap.pickUp(), n ).sprite.drop( pos );
		}
	}

	@Override
	public void beckon( int cell ) {
	}

	public static class WndChat extends WndTitledMessage {

		public WndChat(Image icon, String title, String message) {
			this(icon, title, message, new HashMap<>());
		}

		public WndChat(Image icon, String title, String message, @NotNull final HashMap<String, Window> options) {
			super(icon, title, message);

			int bottom = height;

			for (String option : options.keySet()) {
				RedButton button = new RedButton(option) {
					@Override
					protected void onClick() {
						hide();
						CPDGame.scene().addToFront(options.get(option));
					}
				};
				button.setRect(0, bottom, width, BTN_HEIGHT);
				add(button);
				bottom += BTN_HEIGHT;
				resize(width, (int) button.bottom());
			}
		}
	}
}