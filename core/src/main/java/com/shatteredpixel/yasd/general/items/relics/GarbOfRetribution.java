package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class GarbOfRetribution extends Relic {

    {
        image = ItemSpriteSheet.ARTIFACT_CAPE;

        chargePerKill = 10;
        chargePerUse = 0;
    }

    public static void hit(Char user, Char enemy) {
        ArrayList<KindofMisc> miscs = user.belongings.getMiscsOfType(GarbOfRetribution.class);
        if (miscs.size() > 0) {
            GarbOfRetribution garb = (GarbOfRetribution) miscs.get(0);
            garb.hit(enemy);
        }
    }

    private void hit(Char enemy) {
        //Only use as much charge as is necessary to kill the enemy
        int baseDamage = Math.round(0.25f*damageRoll());
        //Round up
        int chargeNeeded = (int) Math.ceil(enemy.HP/(float)baseDamage);
        int chargeToUse = (int) Math.min(chargeNeeded, charge);
        int dmg = chargeToUse*baseDamage;
        enemy.damage(dmg, new Char.DamageSrc(Element.EARTH, this).ignoreDefense().ignoreBarrier());
        charge -= chargeToUse;
    }

    @Override
    //Has no "activate" ability. It's power is passive.
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.remove(AC_ACTIVATE);
        return actions;
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return false;
    }
}
