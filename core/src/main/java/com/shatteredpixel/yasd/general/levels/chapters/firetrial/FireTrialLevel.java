package com.shatteredpixel.yasd.general.levels.chapters.firetrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.painters.CityPainter;
import com.shatteredpixel.yasd.general.levels.painters.Painter;


public class FireTrialLevel extends RegularLevel {

    @Override
    //TODO new painter
    protected Painter painter() {
        return new CityPainter();
    }

    @Override
    //TODO new visuals
    public String tilesTex() {
        return Assets.Environment.TILES_CITY;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_CITY;
    }

    @Override
    public String loadImg() {
        return Assets.Interfaces.LOADING_CITY;
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
