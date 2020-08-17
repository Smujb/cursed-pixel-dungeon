/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Cursed Pixel Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general;

public class Constants {

	//Number of upgrades Curse Infusion adds.
    public static final int CURSE_INFUSION_BONUS_AMT = 3;

    //Default Upgrade Limit. Set to -1 to remove the limit.
    public static final int UPGRADE_LIMIT = 15;

    //Wand charges cap.
    public static final int WAND_CHARGE_CAP = 10;

    //Can't find items above this tier.
    public static final int MAXIMUM_TIER = 8;


    //
    //############################## CHAR/MOB STUFF ##############################
    //
    //Number of misc slots for char.
    public static final int MISC_SLOTS = 5;

    //
    //############################## DUNGEON STUFF ##############################
    //


    //Chapter length.
    public static final int CHAPTER_LENGTH = 5;

    //Number of chapters.
    public static final int NUM_CHAPTERS = 5;

    //Bonus floors.
    public static final int BONUS_FLOORS = 1;

    //Number of floors.
    public static final int MAX_DEPTH = CHAPTER_LENGTH * NUM_CHAPTERS + BONUS_FLOORS;

    //Hero exp cap
    public static final int HERO_LEVEL_CAP = 60;

    //SoU per chapter
    public static final int SOU_PER_CHAPTER = 3;

    //
    //############################## UI STUFF ##############################
    //

    public static final int MAX_QUICKSLOTS = 8;
    public static final int MIN_QUICKSLOTS = 4;

    public static class Colours {

        public static final int PURE_RED   = 0xFF0000;
        public static final int PURE_GREEN = 0x00FF00;
        public static final int PURE_BLUE  = 0x0000FF;
        public static final int YELLOW     = 0xFFFF00;
        public static final int PURPLE     = 0xFF00FF;
        public static final int LIGHT_BLUE = 0x6496FF;
        public static final int PURE_WHITE = 0xFFFFFF;

    }
}
