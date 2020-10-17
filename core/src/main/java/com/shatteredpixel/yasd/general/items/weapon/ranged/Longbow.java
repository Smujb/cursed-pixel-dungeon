package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Longbow extends Bow {
    {
        image = ItemSpriteSheet.Ranged.LONGBOW;

        damageMultiplier = 0.6f;
        curAmmo = MAX_AMMO = 6;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        //Does damage increasing with distance
        int distance = Dungeon.level.distance(attacker.pos, defender.pos) - 1;
        float multiplier = Math.min(3f, (float)Math.pow(1.125f, distance));
        damage *= multiplier;
        return super.proc(attacker, defender, damage);
    }
}
