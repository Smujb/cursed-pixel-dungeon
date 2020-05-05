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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.MainGame;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Slow;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Chilling;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class WandOfFlow extends DamageWand {
    {
        image = ItemSpriteSheet.WAND_FLOW;

        collisionProperties = Ballistica.STOP_TERRAIN;

        element = Element.WATER;
    }

    private HashSet<Integer> affectedCells;
    //the cells to trace fire shots to, for visual effects.
    private HashSet<Integer> visualCells;
    private int direction = 0;

    @Override
    public float min(float lvl) {
        return 1 + lvl;
    }

    @Override
    public float max(float lvl) {
        return 6 + lvl*3;
    }

    @Override
    public void onZap(Ballistica bolt) {
        ArrayList<Char> affectedChars = new ArrayList<>();
        for( int cell : affectedCells){

            //ignore caster cell
            if (cell == bolt.sourcePos){
                continue;
            }

            Char ch = Actor.findChar( cell );
            if (ch != null) {
                affectedChars.add(ch);
            }
        }

        for ( Char ch : affectedChars.toArray( new Char[0] ) ) {
            try {
                processSoulMark(ch, chargesPerCast());
                hit(ch);
                if (Random.Int(2) == 0) {
                    Buff.affect(ch, Slow.class, Slow.DURATION / 3);
                }
                Ballistica trajectory = new Ballistica(ch.pos, bolt.path.get(bolt.dist + 1), Ballistica.MAGIC_BOLT);
                WandOfBlastWave.throwChar(ch, trajectory, 2);
                //Buff.affect(ch, Wet.class, Wet.DURATION);
            } catch (Exception e) {
                MainGame.reportException(e);
            }
        }
    }

    private int left(int direction){
        return direction == 0 ? 7 : direction-1;
    }

    private int right(int direction){
        return direction == 7 ? 0 : direction+1;
    }

    private void spreadWater(int cell, float strength){
        if (strength >= 0 && (Dungeon.level.passable(cell) || Dungeon.level.flammable(cell))){
            affectedCells.add(cell);
            if (strength >= 1.5f) {
                visualCells.remove(cell);
                spreadWater(cell + PathFinder.CIRCLE8[left(direction)], strength - 1.5f);
                spreadWater(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
                spreadWater(cell + PathFinder.CIRCLE8[right(direction)], strength - 1.5f);
            } else {
                visualCells.add(cell);
            }
        } else if (!Dungeon.level.passable(cell))
            visualCells.add(cell);
    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        //need to perform flame spread logic here so we can determine what cells to put flames in.
        affectedCells = new HashSet<>();
        visualCells = new HashSet<>();

        // 4/6/8 distance
        int maxDist = 8;
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
                spreadWater(c + PathFinder.CIRCLE8[left(direction)], strength - 1);
                spreadWater(c + PathFinder.CIRCLE8[direction], strength - 1);
                spreadWater(c + PathFinder.CIRCLE8[right(direction)], strength - 1);
            } else {
                visualCells.add(c);
            }
        }

        //going to call this one manually
        visualCells.remove(bolt.path.get(dist));

        for (int cell : visualCells){
            //this way we only get the cells at the tip, much better performance.
            ((MagicMissile)curUser.sprite.parent.recycle( MagicMissile.class )).reset(
                    MagicMissile.WATER_CONE,
                    curUser.sprite,
                    cell,
                    null
            );
        }
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.WATER_CONE,
                curUser.sprite,
                bolt.path.get(dist/2),
                callback );
        Sample.INSTANCE.play( Assets.SND_ZAP );
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        new Chilling().proc(staff, attacker, defender, damage);
    }
}
