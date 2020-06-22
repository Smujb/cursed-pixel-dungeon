package com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.MagicalSleep;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.Shopkeeper;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.RelicMeleeWeapon;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Ethereal extends RelicEnchantment {
    private static ItemSprite.Glowing SILVER = new ItemSprite.Glowing( 0x909396 );

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
        defender.sprite.emitter().burst( Speck.factory( Speck.DISCOVER ), (weapon.level() + 1) );
        if (Random.Int( 100-weapon.level()) < 30) {
            Buff.prolong(defender, Blindness.class, 1 + Random.Int(weapon.level()/2+3));
            Buff.prolong(defender, Cripple.class, Cripple.DURATION);
        }
        if (Random.Int(Dungeon.level.distance(attacker.pos, defender.pos)*2 - (weapon.level()/2) + 10) <= 2) {
            Buff.affect(defender, MagicalSleep.class);
        }
        return damage;
    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        super.activate(weapon, owner);
        Buff.affect(owner, MagicalSleep.class);
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (!(mob instanceof Shopkeeper)) {//We don't want him to run away!
                Buff.affect(mob, MagicalSleep.class);
            }
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return SILVER;
    }
}
