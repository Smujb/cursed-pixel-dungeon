package com.shatteredpixel.yasd.general.levels.chapters.earthtrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.chapters.TrialLevel;
import com.shatteredpixel.yasd.general.levels.painters.CavesPainter;
import com.shatteredpixel.yasd.general.levels.painters.Painter;

public class EarthTrialLevel extends TrialLevel {

    @Override
    //TODO new painter
    protected Painter painter() {
        return new CavesPainter();
    }

    @Override
    //TODO new visuals
    public String tilesTex() {
        return Assets.Environment.TILES_EARTH_TRIAL;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CAVES;
    }

    @Override
    public String loadImg() {
        return Assets.Interfaces.LOADING_CAVES;
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

