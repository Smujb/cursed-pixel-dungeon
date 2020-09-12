package com.shatteredpixel.yasd.general.actors.blobs;

import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.mobs.Wraith;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class SpawnerGas extends SmokeScreen {

    private int counter = 0;
    private static final int MAX_COUNTER = 200;

    private static final String COUNTER = "counter";

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(COUNTER, counter);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        counter = bundle.getInt(COUNTER);
    }

    @Override
    public void affectCell(int cell) {
        super.affectCell(cell);
        if (Random.Int(counter) == 0) {
            Minion mob = new Minion();
            mob.state = mob.WANDERING;
            mob.pos = cell;
            GameScene.add(mob);
            counter = MAX_COUNTER;
        }
    }

    public static class Minion extends Wraith {
        {
            properties.add(Property.DEMONIC);
            alignment = Alignment.ENEMY;
        }

        @Override
        public int attackProc(Char enemy, int damage) {
            Buff.prolong( enemy, Blindness.class, 2 );
            return super.attackProc(enemy, damage);
        }

        @Override
        //No necro cheese
        protected void convert() {}
    }
}