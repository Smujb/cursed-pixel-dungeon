package com.shatteredpixel.yasd.general.items.weapon.ranged;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.weapon.melee.Rapier;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class ImpactCrossbow extends Crossbow {
    {
        image = ItemSpriteSheet.Ranged.IMPACT_CROSSBOW;

        damageMultiplier = 1.8f;
        range = 3;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Ballistica attack = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_CHARS | Ballistica.STOP_TERRAIN);
        if (attack.path.size() > 2) {
            Rapier.moveChar(defender, attack.path.get(2), true);
        }
        return super.proc(attacker, defender, damage);
    }
}
