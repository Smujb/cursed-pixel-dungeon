package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Roots;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.plants.Earthroot;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

public class SandalsOfNature extends Relic {

    {
        image = ItemSpriteSheet.ARTIFACT_SANDALS;

        chargePerUse = 10;

        statScaling.add(Hero.HeroStat.RESILIENCE);
    }

    private static final float ROOT_DURATION = 5f;


    @Override
    protected void doActivate() {
        super.doActivate();
        Buff.affect(curUser, Earthroot.Armor.class).level(curUser.HT);
        Buff.affect(curUser, Roots.class, ROOT_DURATION*(1f/power()));
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return Dungeon.level.getTerrain(enemy.pos) == Terrain.GRASS;
    }
}
