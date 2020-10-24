package com.shatteredpixel.yasd.general.items.blueprints;

import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.allies.DragonPendant;
import com.shatteredpixel.yasd.general.items.shield.Shield;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.items.weapon.ranged.RangedWeapon;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class Blueprint extends Item {

    private Class<? extends Item> itemType = null;

    public Blueprint setItemType(Class<? extends Item> itemType) {
        this.itemType = itemType;
        return this;
    }

    @Override
    public int image() {
        if (itemType == null) {
            return 0;
        } else if (itemType.isAssignableFrom(MeleeWeapon.class)) {
            return ItemSpriteSheet.SWORD_BLUEPRINT;
        } else if (itemType.isAssignableFrom(RangedWeapon.class)) {
            return ItemSpriteSheet.BOW_BLUEPRINT;
        } else if (itemType.isAssignableFrom(Shield.class)) {
            //return ItemSpriteSheet.SHIELD_BLUEPRINT;
            return 0;
        } else if (itemType.isAssignableFrom(Wand.class)) {
            //return ItemSpriteSheet.WAND_BLUEPRINT;
            return 0;
        } else if (itemType.isAssignableFrom(DragonPendant.class)) {
            //return ItemSpriteSheet.PENDANT_BLUEPRINT;
            return 0;
        } else {
            return 0;
        }
    }
}
