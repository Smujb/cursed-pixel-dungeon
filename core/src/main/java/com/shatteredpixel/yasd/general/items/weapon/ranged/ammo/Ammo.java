package com.shatteredpixel.yasd.general.items.weapon.ranged.ammo;

import com.shatteredpixel.yasd.general.items.Item;

public abstract class Ammo extends Item {
    {
        stackable = true;

        uncurse();
        cursedKnown = true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }
}
