package com.shatteredpixel.yasd.general.levels.chapters.watertrial;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.mobs.Elemental;
import com.shatteredpixel.yasd.general.actors.mobs.MagicCrab;
import com.shatteredpixel.yasd.general.actors.mobs.WaterLarva;
import com.shatteredpixel.yasd.general.actors.mobs.WaterTrialGoo;
import com.shatteredpixel.yasd.general.levels.chapters.TrialLevel;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;

public class WaterTrialLevel extends TrialLevel {

    @Override
    //TODO new painter
    protected Painter painter() {
        return new SewerPainter()
                .setWater(feeling == Feeling.WATER ? 1.00f : 0.75f, 5)
                .setGrass(feeling == Feeling.GRASS ? 0.50f : 0.20f, 4)
                .setTraps(nTraps(), trapClasses(), trapChances());
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

    @Override
    public Class<?>[] mobClasses() {
        return new Class[] {Elemental.Water.class, WaterTrialGoo.class, WaterLarva.class, MagicCrab.class};
    }

    @Override
    public float[] mobChances() {
        return new float[]{3f, 1f, 5f, 3f};
    }
}
