package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.food.FrozenCarpaccio;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.sprites.CrabSprite;
import com.watabou.utils.Random;

public class MagicCrab extends Mob {
    {
        spriteClass = CrabSprite.MagicCrab.class;

        healthFactor = 0.7f;
        hasMeleeAttack = false;
        viewDistance = Light.DISTANCE;

        EXP = 2;

        loot = new PotionOfHealing();
        lootChance = 0.2f;

        properties.add(Property.DEMONIC);
    }

    @Override
    public Element elementalType() {
        return Element.COLD;
    }

    @Override
    protected Item createLoot() {
        //(9-count) / 9 chance of getting healing, otherwise mystery meat
        if (Random.Float() < ((4f - Dungeon.LimitedDrops.MAGIC_CRAB_HP.count) / 4f)) {
            Dungeon.LimitedDrops.MAGIC_CRAB_HP.count++;
            return (Item)loot;
        } else {
            return new FrozenCarpaccio();
        }
    }
}
