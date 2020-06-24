package com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Adrenaline;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.RelicMeleeWeapon;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Drawing extends RelicEnchantment {
    private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage ) {

        //heals for up to 30% of damage dealt, based on missing HP, ultimately normally distributed
        float missingPercent = (attacker.HT - attacker.HP) / (float)attacker.HT,
                maxHeal = (.025f + missingPercent * .125f) * 2, // min max heal is .025%, consistent with shattered.
                healPercent = 0;
        int tries = 1 + weapon.level()/5;
        do {
            healPercent = Math.max(healPercent, Random.NormalFloat(0,maxHeal));
        } while(tries-- > 0);
        ArrayList<Char> toHeal = new ArrayList<>();
        ArrayList<Char> toDamage = new ArrayList<>();
        toHeal.add(attacker);
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment == Char.Alignment.ALLY) {
                toHeal.add(mob);
            } else {
                toDamage.add(mob);
            }
        }
        int power = Math.round(healPercent * damage);
        for (Char ch : toHeal) {
            ch.heal(power, true, true);
        }

        for (Char ch : toDamage) {
            ch.damage(power, new Char.DamageSrc(Element.DRAIN, this).ignoreDefense());
        }

        return damage;
    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        super.activate(weapon, owner);
        owner.damage(owner.HP/3, new Char.DamageSrc(Element.DRAIN));
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (mob.alignment == Char.Alignment.ALLY) {
                mob.heal(mob.HT - mob.HP);
                Buff.prolong(mob, Adrenaline.class, Adrenaline.DURATION);
            } else {
                Buff.prolong(mob, Blindness.class, 10f);
            }
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return RED;
    }
}