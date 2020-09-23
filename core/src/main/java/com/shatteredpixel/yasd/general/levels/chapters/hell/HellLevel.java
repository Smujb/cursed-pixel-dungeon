package com.shatteredpixel.yasd.general.levels.chapters.hell;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.levels.RegularLevel;
import com.shatteredpixel.yasd.general.levels.painters.HallsPainter;
import com.shatteredpixel.yasd.general.levels.painters.Painter;

public class HellLevel extends RegularLevel {

    @Override
    protected Painter painter() {
        return new HallsPainter();
    }

    @Override
    public String tilesTex() {
        return Assets.Environment.TILES_HALLS;
    }

    @Override
    public String waterTex() {
        return Assets.Environment.WATER_HALLS;
    }

    @Override
    public String loadImg() {
        return Assets.Interfaces.LOADING_HALLS;
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
