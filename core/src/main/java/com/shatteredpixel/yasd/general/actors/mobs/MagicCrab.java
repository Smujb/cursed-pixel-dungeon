package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.items.potions.PotionOfFrost;
import com.shatteredpixel.yasd.general.sprites.CrabSprite;

public class MagicCrab extends Mob {
    {
        spriteClass = CrabSprite.MagicCrab.class;

        healthFactor = 0.7f;
        hasMeleeAttack = false;
        viewDistance = Light.DISTANCE;

        EXP = 2;

        loot = new PotionOfFrost();
        lootChance = 0.2f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public Element elementalType() {
        return Element.COLD;
    }
}
