package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;

public class LloydsBeacon extends SelectorRelic {

    {
        image = ItemSpriteSheet.ARTIFACT_BEACON;

        chargePerKill = 30;

        //Charge is consumed separately
        chargePerUse = 0;
    }

    private String lastLvlKey = null;
    private int pos = -1;

    private static final int HERO_TP_COST = 30;
    private static final int CHAR_TP_COST = 10;

    private static final String LAST_LVL_KEY = "last_lvl_key";
    private static final String POS = "pos";

    @Override
    protected void onCellSelected(int cell) {
        if (curUser != null && Ballistica.canHit(curUser, cell, Ballistica.PROJECTILE)) {
            Char ch = Actor.findChar(cell);
            if (ch == null) {
                pos = cell;
                lastLvlKey = Dungeon.level.key;
            } else {
                tpChar(ch);
            }
            curUser.sprite.parent.add(
                    new Beam.LightRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(cell)));
        }
    }

    protected void tpChar(Char ch) {
        int posToTeleport = getPos();
        if (posToTeleport == -1 || ch.properties().contains(Char.Property.IMMOVABLE) || curUser.buff(LockedFloor.class) != null) {
            GLog.negative(Messages.get(this, "cannot_teleport"));
            return;
        }
        ScrollOfTeleportation.teleportToLocation(ch, posToTeleport);

        //Teleporting yourself consumes more charge
        useCharge(ch instanceof Hero ? HERO_TP_COST : CHAR_TP_COST);
    }

    protected int getPos() {
        if (lastLvlKey == null || !lastLvlKey.equals(Dungeon.level.key)) pos = -1;
        return pos;
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return false;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(POS, pos);
        bundle.put(LAST_LVL_KEY, lastLvlKey);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        pos = bundle.getInt(POS);
        lastLvlKey = bundle.getString(LAST_LVL_KEY);
    }
}
