package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.WaterLarvaSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WaterLarva extends Mob {

    {
        spriteClass = WaterLarvaSprite.class;

        healthFactor = 0.25f;
        evasionFactor = 0.7f;
        damageFactor = 1.3f;

        EXP = 1;

        SLEEPING = new Sleeping();
        WANDERING = new Wandering();
        state = SLEEPING;

        properties.add(Property.DEMONIC);
    }

    protected int partnerID = -1;

    private static final String PARTNER_ID = "partner_id";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put( PARTNER_ID, partnerID );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );
        partnerID = bundle.getInt( PARTNER_ID );
    }

    @Override
    public float spawningWeight() {
        return 0.5f;
    }

    private void createPartner() {
        ArrayList<Integer> candidates = new ArrayList<>();

        int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
        for (int n : neighbours) {
            if (Dungeon.level.passable(n) && Actor.findChar( n ) == null) {
                candidates.add( n );
            }
        }

        if (!candidates.isEmpty()){
            WaterLarva child = Mob.create(WaterLarva.class);
            child.partnerID = this.id();
            this.partnerID = child.id();
            if (state != SLEEPING) {
                child.state = child.WANDERING;
            }

            child.pos = Random.element( candidates );

            Dungeon.level.occupyCell(child);

            GameScene.add( child );
            if (sprite.visible) {
                Actor.addDelayed( new Pushing( child, pos, child.pos ), -1 );
            }
        }
    }

    @Override
    protected boolean act() {
        //create a child
        if (partnerID == -1){
            createPartner();
        } else {
            WaterLarva larva = (WaterLarva) Actor.findById(partnerID);
            if (larva == null || !larva.isAlive()) {
                spend(2f);
                //Create partner after acting
                partnerID = -1;
            }
        }
        return super.act();
    }

    private class Sleeping extends Mob.Sleeping {
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            WaterLarva partner = (WaterLarva) Actor.findById( partnerID );
            if (partner != null && partner.state != partner.SLEEPING){
                state = WANDERING;
                target = partner.pos;
                return true;
            } else {
                return super.act( enemyInFOV, justAlerted );
            }
        }
    }

    private class Wandering extends Mob.Wandering {

        @Override
        protected boolean continueWandering() {
            enemySeen = false;

            WaterLarva partner = (WaterLarva) Actor.findById( partnerID );
            if (partner != null && (partner.state != partner.WANDERING || Dungeon.level.distance( pos,  partner.target) > 1)){
                target = partner.pos;
                int oldPos = pos;
                if (getCloser( target )){
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                } else {
                    spend( TICK );
                    return true;
                }
            } else {
                return super.continueWandering();
            }
        }
    }
}
