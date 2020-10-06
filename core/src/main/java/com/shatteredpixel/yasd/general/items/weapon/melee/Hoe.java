package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.levels.features.HighGrass;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

public class Hoe extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.HOE;

        hitSound = Assets.Sounds.HIT_PARRY;
        hitSoundPitch = 0.6f;

        damageMultiplier = 0.85f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        for (int i : PathFinder.NEIGHBOURS9) {
            int cell = attacker.pos + i;
            if (Dungeon.level.getTerrain(cell) == Terrain.HIGH_GRASS) {
                HighGrass.trample(Dungeon.level, cell, true);
            }
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected boolean hasProperties() {
        return true;
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "grass");
    }
}
