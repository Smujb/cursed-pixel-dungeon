package com.shatteredpixel.yasd.general.levels.chapters.watertrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;

public class WaterTrialLevel extends RegularLevel {

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
        return super.mobClasses();
    }

    @Override
    public float[] mobChances() {
        return super.mobChances();
    }
}
