package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;

public interface Enchantable {

    Item enchant(Weapon.Enchantment enchantment);

    default Item enchant() {
        return enchant(Weapon.Enchantment.random());
    }

    boolean hasEnchant(Class<?extends Weapon.Enchantment> type, Char owner);

    Weapon.Enchantment getEnchantment();

    //these are not used to process specific enchant effects, so magic immune doesn't affect them
    boolean hasGoodEnchant();

    boolean hasCurseEnchant();

    int enchPower();
}
