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

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.weapon.SpiritBow;
import com.shatteredpixel.yasd.general.items.unused.missiles.Bolas;
import com.shatteredpixel.yasd.general.items.unused.missiles.FishingSpear;
import com.shatteredpixel.yasd.general.items.unused.missiles.HeavyBoomerang;
import com.shatteredpixel.yasd.general.items.unused.missiles.Javelin;
import com.shatteredpixel.yasd.general.items.unused.missiles.Kunai;
import com.shatteredpixel.yasd.general.items.unused.missiles.Shuriken;
import com.shatteredpixel.yasd.general.items.unused.missiles.ThrowingKnife;
import com.shatteredpixel.yasd.general.items.unused.missiles.ThrowingSpear;
import com.shatteredpixel.yasd.general.items.unused.missiles.Trident;
import com.shatteredpixel.yasd.general.items.unused.missiles.darts.Dart;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Ammo;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Arrow;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Bolt;
import com.shatteredpixel.yasd.general.items.weapon.ranged.ammo.Bullet;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

	private static final float SPEED	= 240f;

	private Callback callback;

	public void reset( int from, int to, Item item, Callback listener ) {
		reset( DungeonTilemap.tileToWorld( from ), DungeonTilemap.tileToWorld( to ), item, listener);
	}

	public void reset( Visual from, Visual to, Item item, Callback listener ) {
		reset(from.center(this), to.center(this), item, listener );
	}

	public void reset( Visual from, int to, Item item, Callback listener ) {
		reset(from.center(this), DungeonTilemap.tileToWorld( to ), item, listener );
	}

	public void reset( int from, Visual to, Item item, Callback listener ) {
		reset(DungeonTilemap.tileToWorld( from ), to.center(this), item, listener );
	}

	public void reset( PointF from, PointF to, Item item, Callback listener) {
		revive();

		if (item == null)   view(0, null);
		else                view( item );

		setup( from,
				to,
				item,
				listener );
	}

	private static final int DEFAULT_ANGULAR_SPEED = 720;

	private static final HashMap<Class<?extends Item>, Integer> ANGULAR_SPEEDS = new HashMap<>();
	static {
		ANGULAR_SPEEDS.put(Dart.class,          0);
		ANGULAR_SPEEDS.put(ThrowingKnife.class, 0);
		ANGULAR_SPEEDS.put(FishingSpear.class,  0);
		ANGULAR_SPEEDS.put(ThrowingSpear.class, 0);
		ANGULAR_SPEEDS.put(Kunai.class,         0);
		ANGULAR_SPEEDS.put(Javelin.class,       0);
		ANGULAR_SPEEDS.put(Trident.class,       0);

		ANGULAR_SPEEDS.put(SpiritBow.SpiritArrow.class,       0);
		ANGULAR_SPEEDS.put(ScorpioSprite.ScorpioShot.class,   0);
		ANGULAR_SPEEDS.put(Ammo.class, 0);

		//720 is default

		ANGULAR_SPEEDS.put(HeavyBoomerang.class,1440);
		ANGULAR_SPEEDS.put(Bolas.class,         1440);

		ANGULAR_SPEEDS.put(Shuriken.class,      2160);

		ANGULAR_SPEEDS.put(TenguSprite.TenguShuriken.class,      2160);
	}

	//TODO it might be nice to have a source and destination angle, to improve thrown getWeapons visuals
	private void setup( PointF from, PointF to, Item item, Callback listener ){

		originToCenter();

		this.callback = listener;

		point( from );

		PointF d = PointF.diff( to, from );
		speed.set(d).normalize().scale(SPEED);

		angularSpeed = DEFAULT_ANGULAR_SPEED;
		for (Class<?extends Item> cls : ANGULAR_SPEEDS.keySet()){
			if (cls.isAssignableFrom(item.getClass())){
				angularSpeed = ANGULAR_SPEEDS.get(cls);
				break;
			}
		}

		angle = 135 - (float)(Math.atan2( d.x, d.y ) / 3.1415926 * 180);

		if (d.x >= 0){
			flipHorizontal = false;

		} else {
			angularSpeed = -angularSpeed;
			angle += 90;
			flipHorizontal = true;
		}
		updateFrame();

		float speed = SPEED;

		if (item instanceof SpiritBow.SpiritArrow
				|| item instanceof ScorpioSprite.ScorpioShot
				|| item instanceof TenguSprite.TenguShuriken
				|| item instanceof Arrow
				|| item instanceof Bolt){
			speed *= 1.5f;
		} else if (item instanceof Bullet) {
			speed *= 4f;
		}

		PosTweener tweener = new PosTweener( this, to, d.length() / speed );
		tweener.listener = this;
		parent.add( tweener );
	}

	@Override
	public void onComplete( Tweener tweener ) {
		kill();
		if (callback != null) {
			callback.call();
		}
	}
}
