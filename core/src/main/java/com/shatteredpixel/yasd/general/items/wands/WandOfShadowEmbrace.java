package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Terror;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class WandOfShadowEmbrace extends DamageWand {

    {
        image = ItemSpriteSheet.Wands.SHADOW_EMBRACE;

        element = Element.SHADOW;
    }

    @Override
    public float min(float lvl) {
        return super.min(lvl) * (Dungeon.checkNight() ? 1.5f : 0.5f);
    }

    @Override
    public float max(float lvl) {
        return super.max(lvl) * (Dungeon.checkNight() ? 1.5f : 0.5f);
    }

    @Override
    public void onZap(Ballistica attack) {
        Char ch = Actor.findChar(attack.collisionPos);
        if (ch != null) {
            hit(ch);
            if (Dungeon.checkNight() && Random.Int(5) == 0) {
                Buff.affect(ch, Terror.class, 5f);
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        if (Dungeon.checkNight() && Random.Int(2) == 0) {
            Buff.affect(defender, Terror.class, 5f);
        }
    }

    @Override
    public boolean canSpawn() {
        return super.canSpawn() && Dungeon.checkNight();
    }
}
