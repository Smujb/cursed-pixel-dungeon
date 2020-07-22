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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Fire;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Blazing;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfFireblast extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_FIREBOLT;

		collisionProperties = Ballistica.STOP_TERRAIN;
	}

	//1x/2x/3x damage
	@Override
	public float min(float lvl){
		return (1+lvl) * chargesPerCast();
	}

	//1x/2x/3x damage
	@Override
	public float max(float lvl){
		return (6+4*lvl) * chargesPerCast();
	}

	//the actual affected cells
	private HashSet<Integer> affectedCells;
	//the cells to trace fire shots to, for visual effects.
	private HashSet<Integer> visualCells;
	private int direction = 0;
	
	@Override
	public void onZap(Ballistica bolt) {
		
		ArrayList<Char> affectedChars = new ArrayList<>();
		for( int cell : affectedCells.toArray(new Integer[0]) ){
			
			//ignore caster cell
			if (cell == bolt.sourcePos){
				continue;
			}
			
			//only ignite cells directly near caster if they are flammable
			if (!Dungeon.level.adjacent(bolt.sourcePos, cell)
					|| Dungeon.level.flammable(cell)){
				GameScene.add( Blob.seed( cell, 1+chargesPerCast(), Fire.class ) );
			}
			
			Char ch = Actor.findChar( cell );
			if (ch != null) {
				affectedChars.add(ch);
			}
		}
		
		for ( Char ch : affectedChars.toArray(new Char[0]) ){
			processSoulMark(ch, chargesPerCast());
			hit(ch);
			Buff.affect( ch, Burning.class ).reignite( ch );
			switch(chargesPerCast()){
				case 1:
					break; //no effects
				case 2:
					Buff.affect(ch, Cripple.class, 4f); break;
				case 3:
					Buff.affect(ch, Paralysis.class, 4f); break;
			}
		}
	}

	//burn... BURNNNNN!.....
	private void spreadFlames(int cell, float strength){
		if (strength >= 0 && (Dungeon.level.passable(cell) || Dungeon.level.flammable(cell))){
			affectedCells.add(cell);
			if (strength >= 1.5f) {
				visualCells.remove(cell);
				spreadFlames(cell + PathFinder.CIRCLE8[left(direction)], strength - 1.5f);
				spreadFlames(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
				spreadFlames(cell + PathFinder.CIRCLE8[right(direction)], strength - 1.5f);
			} else {
				visualCells.add(cell);
			}
		} else if (!Dungeon.level.passable(cell))
			visualCells.add(cell);
	}

	private int left(int direction){
		return direction == 0 ? 7 : direction-1;
	}

	private int right(int direction){
		return direction == 7 ? 0 : direction+1;
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//acts like blazing enchantment
		new Blazing().proc( staff, attacker, defender, damage);
	}

	@Override
	protected void fx( Ballistica bolt, Callback callback ) {
		//need to perform flame spread logic here so we can determine what cells to put flames in.
		affectedCells = new HashSet<>();
		visualCells = new HashSet<>();

		// 4/6/8 distance
		int maxDist = 2 + 2*chargesPerCast();
		int dist = Math.min(bolt.dist, maxDist);

		for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
			if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
				direction = i;
				break;
			}
		}

		float strength = maxDist;
		for (int c : bolt.subPath(1, dist)) {
			strength--; //as we start at dist 1, not 0.
			affectedCells.add(c);
			if (strength > 1) {
				spreadFlames(c + PathFinder.CIRCLE8[left(direction)], strength - 1);
				spreadFlames(c + PathFinder.CIRCLE8[direction], strength - 1);
				spreadFlames(c + PathFinder.CIRCLE8[right(direction)], strength - 1);
			} else {
				visualCells.add(c);
			}
		}

		//going to call this one manually
		visualCells.remove(bolt.path.get(dist));

		for (int cell : visualCells){
			//this way we only get the cells at the tip, much better performance.
			((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
					MagicMissile.FIRE_CONE,
					curUser.sprite,
					cell,
					null
			);
		}
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.FIRE_CONE,
				curUser.sprite,
				bolt.path.get(dist/2),
				callback );
		Sample.INSTANCE.play( Assets.Sounds.ZAP );
	}

	@Override
	protected int chargesPerCast() {
		//consumes all charges, up to 3
		return Math.max(1,Math.min(3,curCharges));
	}

	@Override
	public String statsDesc() {
		if (levelKnown)
			return Messages.get(this, "stats_desc", chargesPerCast(), min(), max());
		else
			return Messages.get(this, "stats_desc", chargesPerCast(), defaultMin(), defaultMax());
	}

	@Override
	protected int initialCharges() {
		return 3;
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0xEE7722 );
		particle.am = 0.5f;
		particle.setLifespan(0.6f);
		particle.acc.set(0, -40);
		particle.setSize( 0f, 3f);
		particle.shuffleXY( 1.5f );
	}

}
