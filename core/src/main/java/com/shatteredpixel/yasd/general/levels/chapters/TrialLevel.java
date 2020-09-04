package com.shatteredpixel.yasd.general.levels.chapters;

import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;

public abstract class TrialLevel extends RegularLevel {

    @Override
    protected boolean build() {
        boolean build = super.build();
        if (build) {
            int entrance = getEntrancePos();
            setEntrance(-1);
            set(entrance, Terrain.PEDESTAL);
        }
        return build;
    }
}