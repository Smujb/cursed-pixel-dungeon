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

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.Recharging;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.NPC;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.effects.Splash;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.ThornVineSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfThornvines extends Wand {
    {
        image = ItemSpriteSheet.WAND_THORNVINES;
        collisionProperties = Ballistica.STOP_TARGET | Ballistica.STOP_TERRAIN;
    }

    private ThornVine thornVine = null;

    public ThornVine findThornVine() {
        thornVine = null;
        if (Dungeon.level != null) {
            for (Mob m : Dungeon.level.mobs) {
                if (m instanceof ThornVine && m.alignment == curUser.alignment) {//Checks for alignment so that Statues can use ThornVines wand even if hero has spawned one
                    thornVine = (ThornVine) m;
                }
            }
        }
        return thornVine;
    }

    @Override
    public void activate(Char ch) {
        thornVine = findThornVine();
        super.activate(ch);
    }

    @Override
    public boolean tryToZap(Char owner, int target) {
        thornVine = findThornVine();
        return super.tryToZap(owner, target) & thornVine == null;//Can't zap if a thorn vine already exists
    }

    @Override
    public void onZap(Ballistica bolt) {


        if (findThornVine() == null) {
            new ThornVine().spawnAt(bolt.collisionPos, actualLevel(), curCharges, curUser);
        }
    }

    private static class ThornVine extends NPC {
        {
            spriteClass = ThornVineSprite.class;
            properties.add(Property.IMMOVABLE);
            alignment = Alignment.ALLY;
        }

        float level;
        float charges;

        private final String LEVEL = "level";
        private final String CHARGES = "charges";


        public ThornVine(float level, int charges, Char owner) {
            this.level = level;
            this.charges = charges;
            this.alignment = owner.alignment;
        }

        public ThornVine() {
            this(0, 1, Dungeon.hero);
        }

        @Override
        public void storeInBundle( Bundle bundle) {
            bundle.put(LEVEL, level);
            bundle.put(CHARGES, charges);
            super.storeInBundle(bundle);
        }

        @Override
        public void restoreFromBundle( Bundle bundle) {
            level = bundle.getInt(LEVEL);
            charges = bundle.getInt(CHARGES);
            super.restoreFromBundle(bundle);
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }

        @Override
        public int damageRoll() {
            return (int) (Random.Int(2,(int) (3 + level))*charges);
        }

        @Override
        public int defenseSkill(Char enemy) {
            return 0;
        }
        //Always hit, always hits
        @Override
        public int attackSkill(Char target) {
            return Integer.MAX_VALUE;
        }

        private int setHP() {
            return (int) ((20 + this.level*5));
        }

        @Override
        public int defenseProc(Char enemy, int damage) {
            if (Random.Int(  3 ) >= 2) {

                Buff.affect(enemy, Bleeding.class).set(damage/3f);
                Splash.at( enemy.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                        enemy.sprite.blood(), 10 );

            }
            Buff.prolong( enemy, Cripple.class, 3f );
            return super.defenseProc(enemy, damage);
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            if (Random.Int(  3 ) >= 2) {

                Buff.affect(enemy, Bleeding.class).set(damage/3f);
                Splash.at( enemy.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                        enemy.sprite.blood(), 10 );

            }
            Buff.prolong( enemy, Cripple.class, 3f );
            return super.attackProc(enemy, damage);
        }

        @Override
        public int drRoll(Element element) {
            return (int) (1 + level*2);
        }

        @Override
        public boolean interact(Char ch) {
            Buff.affect(Dungeon.hero, Recharging.class, (HP/(float)HT)*charges);
            die(new DamageSrc(Element.EARTH, null));
            return true;
        }

        public ThornVine spawnAt(int pos, float level, int charges, Char owner ) {
            if (Dungeon.level.passable(pos)) {
                ThornVine TV = new ThornVine(level, charges, owner);
                if (Actor.findChar(pos) == null) {
                    TV.pos = pos;
                } else {
                    int closest = -1;
                    boolean[] passable = Dungeon.level.passable();
                    for (int n : PathFinder.NEIGHBOURS9) {
                        int c = pos + n;
                        if (passable[c] && Actor.findChar( c ) == null
                                && (closest == -1 || (Dungeon.level.trueDistance(c, curUser.pos) < (Dungeon.level.trueDistance(closest, curUser.pos))))) {
                            closest = c;
                        }
                    }

                    if (closest == -1){
                        curUser.sprite.centerEmitter().burst(MagicMissile.EarthParticle.ATTRACT, 8 + (int)level/2);
                        return null; //do not spawn Thorn Vine
                    } else {
                        TV.pos = closest;
                    }
                }

                TV.HP = TV.HT = setHP();
                TV.HP *= (charges/((float) new WandOfThornvines().initialCharges())+level);
                GameScene.add(TV);
                TV.state = TV.HUNTING;

                TV.sprite.centerEmitter().burst(MagicMissile.EarthParticle.BURST, 5 + (int)level / 2);

                return TV;
            } else {
                return null;
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (Random.Int( staff.level() + 3 ) >= 2) {

            Buff.affect(defender, Bleeding.class).set(damage/3f);
            Splash.at( defender.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
                    defender.sprite.blood(), 10 );

        }

    }

    @Override
    protected void fx(Ballistica bolt, Callback callback) {
        MagicMissile.boltFromChar( curUser.sprite.parent,
                MagicMissile.FOLIAGE_CONE,
                curUser.sprite,
                bolt.path.get(bolt.dist),
                callback );
    }
    @Override
    public void staffFx(MagesStaff.StaffParticle particle) {
        particle.color( ColorMath.random(0x004400, 0x88CC44) );
        particle.am = 1f;
        particle.setLifespan(1f);
        particle.setSize( 1f, 1.5f);
        particle.shuffleXY(0.5f);
        float dst = Random.Float(11f);
        particle.x -= dst;
        particle.y += dst;
    }

    @Override
    protected int initialCharges() {
        return 4;
    }

    protected int chargesPerCast() {
        return Math.max(1, curCharges);
    }
}
