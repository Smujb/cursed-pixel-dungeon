package com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Barrier;
import com.shatteredpixel.yasd.general.actors.buffs.Bleeding;
import com.shatteredpixel.yasd.general.actors.buffs.BlobImmunity;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.buffs.Poison;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.RelicMeleeWeapon;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;

public class Vicious extends RelicEnchantment {
    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
    private final float DURATION = 50f;

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
        Buff.affect( defender, Poison.class ).set( 3 + Dungeon.depth / 2f );
        Buff.affect( defender, Bleeding.class ).set( damage );
        Buff.affect( defender, Cripple.class, Cripple.DURATION );
        return damage;
    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        super.activate(weapon, owner);
        Buff.affect( owner, MagicImmune.class, DURATION );
        Buff.affect( owner, BlobImmunity.class, DURATION );
        Buff.affect( owner, Barrier.class ).setShield(owner.HT);
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
}
