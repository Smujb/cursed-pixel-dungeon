package com.shatteredpixel.yasd.general.levels.chapters.watertrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.mobs.Elemental;
import com.shatteredpixel.yasd.general.levels.chapters.TrialLevel;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;

public class WaterTrialLevel extends TrialLevel {

    @Override
    //TODO new painter
    protected Painter painter() {
        return new SewerPainter();
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_WATER_TRIAL;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_SEWERS;
    }

    @Override
    public String loadImg() {
        return Assets.Interfaces.LOADING_SEWERS;
    }

    //TODO bestiary
    @Override
    public Class<?>[] mobClasses() {
        return new Class[] {Elemental.Water.class};
    }

    @Override
    public float[] mobChances() {
        return new float[]{1f};
    }
}
