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

package com.shatteredpixel.yasd.general.ui.attack;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Attackable;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.DangerIndicator;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.ui.Tag;
import com.shatteredpixel.yasd.general.ui.TargetHealthIndicator;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class AttackIndicator extends Tag {
	
	private static final float ENABLED	= 1.0f;
	private static final float DISABLED	= 0.3f;

	private ItemSprite sprite = null;

	Mob lastTarget;
	private ArrayList<Mob> candidates = new ArrayList<>();
	
	public AttackIndicator() {
		super( DangerIndicator.COLOR );

		lastTarget = null;
		
		setSize( 24, 24 );

		active = true;
		enable(Dungeon.hero.ready);
		visible(true);
	}

	@Override
	protected void createChildren() {
		super.createChildren();
	}
	
	@Override
	protected synchronized void layout() {
		super.layout();
		
		if (sprite != null) {
			sprite.x = x + (width - sprite.width()) / 2 + 1;
			sprite.y = y + (height - sprite.height()) / 2;
			PixelScene.align(sprite);
		}
	}
	
	@Override
	public synchronized void update() {
		super.update();
		active = true;

		if (Dungeon.hero.isAlive()) {

			enable(Dungeon.hero.ready);
			active = Dungeon.hero.ready;
			visible(true);

		} else {
			visible(false);
			enable(false);
		}
	}
	
	private synchronized void checkEnemies() {

		candidates.clear();
		int v = Dungeon.hero.visibleEnemies();
		for (int i=0; i < v; i++) {
			Mob mob = Dungeon.hero.visibleEnemy( i );
			if ( Dungeon.hero.curItem() != null && Dungeon.hero.curItem().canAttack(mob) ) {
				candidates.add( mob );
			}
		}
		
		if (!candidates.contains( lastTarget )) {
			if (candidates.isEmpty()) {
				lastTarget = null;
			} else {
				active = true;
				AttackIndicator.target(Random.element(candidates));
				updateImage();
				flash();
			}
		}

		visible( true );
		enable( Dungeon.hero.ready );
		active = Dungeon.hero.ready;
	}
	
	private synchronized void updateImage() {
		
		if (sprite != null) {
			sprite.killAndErase();
			sprite = null;
		}

		int image = ItemSpriteSheet.WEAPON_HOLDER;
		if (Dungeon.hero.curItem() != null) {
			image =  ((Item)Dungeon.hero.curItem()).image;
		}
		sprite = new ItemSprite(image, null);
		add( sprite );

		layout();
	}
	
	protected boolean enabled = true;
	private synchronized void enable( boolean value ) {
		enabled = value;
		if (sprite != null) {
			sprite.alpha( value ? ENABLED : DISABLED );
		}
	}
	
	private synchronized void visible( boolean value ) {
		bg.visible = value;
		if (sprite != null) {
			sprite.visible = value;
		}
	}

	@Override
	protected void onClick() {
		super.onClick();
		if (!Dungeon.hero.ready) {
			return;
		}
		if (Dungeon.hero.curItem() == null) {
			onLongClick();
		} else {
			if (lastTarget != null) {
				Dungeon.hero.curItem().use(lastTarget);
			} else {
				checkEnemies();
			}
		}
		active = Dungeon.hero.ready;
		enable(Dungeon.hero.ready);
	}

	@Override
	protected boolean onLongClick() {
		GameScene.selectItem(listener(), WndBag.Mode.ATTACKABLE, Messages.get(QuickSlotButton.class, "select_item"));
		return true;
	}

	private WndBag.Listener listener() {
		return new WndBag.Listener() {
			@Override
			public void onSelect( Item item ) {
				if (item instanceof Attackable) {
					Dungeon.hero.setCurItem((Attackable) item);
					AttackIndicator.this.updateImage();
				}
			}
		};
	}

	public void setLastTarget(Char target ) {
		lastTarget = (Mob) target;
		updateImage();
		TargetHealthIndicator.instance.target(target);
	}

	public static void target( Char target ) {
		GameScene.getAttack().setLastTarget(target);
	}

	public static void updateStates() {
		GameScene.getAttack().updateState();
	}
	
	private void updateState() {
		checkEnemies();
	}
}
