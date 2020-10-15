package com.shatteredpixel.yasd.general.items.weapon.ranged.ammo;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public abstract class Ammo extends Item {
    {
        stackable = true;

        cursed = false;
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
