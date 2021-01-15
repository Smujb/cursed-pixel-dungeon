package com.shatteredpixel.yasd.general.levels.chapters.firetrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.mobs.Elemental;
import com.shatteredpixel.yasd.general.actors.mobs.Kupua;
import com.shatteredpixel.yasd.general.levels.chapters.TrialLevel;
import com.shatteredpixel.yasd.general.levels.painters.CityPainter;
import com.shatteredpixel.yasd.general.levels.painters.Painter;


public class FireTrialLevel extends TrialLevel {

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

    @Override
    public Class<?>[] mobClasses() {
        return new Class[] {
                Elemental.Fire.Adult.class,
                Kupua.class
        };
    }

    @Override
    public float[] mobChances() {
        return new float[] {
                2f,
                1f
        };
    }
}
