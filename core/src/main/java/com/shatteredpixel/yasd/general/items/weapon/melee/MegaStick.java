package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import java.util.ArrayList;
import java.util.Arrays;

public class MegaStick extends MeleeWeapon {
    {
        image = ItemSpriteSheet.WEAPON_HOLDER;
        hitSound = Assets.Sounds.HIT_PARRY;
        hitSoundPitch = 1f;

        statScaling = new ArrayList<>(Arrays.asList(Hero.HeroStat.values()));
    }

    @Override
    public int max(float lvl) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int defenseFactor(Char owner) {
        return Integer.MAX_VALUE/2;
    }

    @Override
    public String desc() {
        return "1 shot he ded lmao";
    }

    @Override
    public String name() {
        return "mega sticc";
    }
}
