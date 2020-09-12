package com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.RelicMeleeWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.utils.GLog;


public abstract class RelicEnchantment extends Weapon.Enchantment {
    @Override
    public int proc(Enchantable weapon, Char attacker, Char defender, int damage) {
        if (weapon instanceof RelicMeleeWeapon) {
            return relicProc((RelicMeleeWeapon)weapon,attacker,defender,damage);
        } else {
            GLog.warning("Inconceivable!");
            return damage;
        }
    }

    public abstract int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage);

    public void activate(RelicMeleeWeapon weapon, Char owner) {
        weapon.use();
        GLog.positive(Messages.get(this,"activated"));
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return null;
    }
}
