package com.shatteredpixel.yasd.general.levels.chapters.airtrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.chapters.TrialLevel;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.PrisonPainter;

public class AirTrialLevel extends TrialLevel {

    @Override
    //TODO new painter
    protected Painter painter() {
        return new PrisonPainter();
    }

    @Override
    //TODO new visuals
    public String tilesTex() {
        return Assets.Environment.TILES_PRISON;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_PRISON;
    }

    @Override
    public String loadImg() {
        return Assets.Interfaces.LOADING_PRISON;
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

