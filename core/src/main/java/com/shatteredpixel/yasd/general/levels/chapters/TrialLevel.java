package com.shatteredpixel.yasd.general.levels.chapters;

import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.traps.AlarmTrap;
import com.shatteredpixel.yasd.general.levels.traps.BurningTrap;
import com.shatteredpixel.yasd.general.levels.traps.ChillingTrap;
import com.shatteredpixel.yasd.general.levels.traps.ConfusionTrap;
import com.shatteredpixel.yasd.general.levels.traps.FlockTrap;
import com.shatteredpixel.yasd.general.levels.traps.GrippingTrap;
import com.shatteredpixel.yasd.general.levels.traps.OozeTrap;
import com.shatteredpixel.yasd.general.levels.traps.PoisonDartTrap;
import com.shatteredpixel.yasd.general.levels.traps.ShockingTrap;
import com.shatteredpixel.yasd.general.levels.traps.SummoningTrap;
import com.shatteredpixel.yasd.general.levels.traps.TeleportationTrap;
import com.shatteredpixel.yasd.general.levels.traps.ToxicTrap;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public abstract class TrialLevel extends RegularLevel {

    public int spawn;
    public static String SPAWN = "spawn";

    @Override
    public void create(String key) {
        super.create(key);
        spawn = getEntrance().centerCell(this);
        set(spawn, Terrain.PEDESTAL);
        setEntrance(-1);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(SPAWN, spawn);
    }

    @Override
    public void restoreFromBundle(@NotNull Bundle bundle) {
        super.restoreFromBundle(bundle);
        spawn = bundle.getInt(SPAWN);
    }

    @Override
    public int getEntrancePos() {
        return spawn;
    }

    @Override
    protected Class<?>[] trapClasses() {
        return new Class[]{
                ChillingTrap.class, ShockingTrap.class, ToxicTrap.class, BurningTrap.class, PoisonDartTrap.class,
                AlarmTrap.class, OozeTrap.class, GrippingTrap.class,
                ConfusionTrap.class, FlockTrap.class, SummoningTrap.class, TeleportationTrap.class, };
    }

    @Override
    protected float[] trapChances() {
        return new float[]{
                4, 4, 4, 4, 4,
                2, 2, 2,
                1, 1, 1, 1 };
    }

    @Override
    protected int standardRooms() {
        //6 to 8, average 6.66
        return 6+ Random.chances(new float[]{4, 2, 2});
    }

    @Override
    protected int specialRooms() {
        //1 to 3, average 1.83
        return 1+Random.chances(new float[]{3, 4, 3});
    }
}