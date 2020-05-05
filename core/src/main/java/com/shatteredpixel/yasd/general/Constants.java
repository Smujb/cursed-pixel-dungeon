/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Yet Another Shattered Dungeon
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
    /*
    Purpose of this file is to have constants that can be used to quickly tweak in-game things.
    Makes modding easier and also helps me in future.

    (I may rework some of my other mods off this in the future as I'm quite happy with engine functionality atm)
     */
    //
    //############################## EQUIPMENT STUFF ##############################
    //
    public static final boolean DEGRADATION = true;

    //Number of upgrades Curse Infusion adds.
    public static final int CURSE_INFUSION_BONUS_AMT = 1;

    //Default Upgrade Limit. Set to -1 to remove the limit.
    public static final int UPGRADE_LIMIT = 3;

    //Wand charges cap.
    public static final int WAND_CHARGE_CAP = 10;

    //Can't find items above this tier.
    public static final int MAXIMUM_TIER = 5;


    //
    //############################## CHAR/MOB STUFF ##############################
    //
    //Number of misc slots for char.
    public static final int MISC_SLOTS = 3;

    public static final boolean MORALE = true;

    //
    //############################## DUNGEON STUFF ##############################
    //


    //Chapter length. WIP.
    public static final int CHAPTER_LENGTH = 6;

    //Number of chapters.
    public static final int NUM_CHAPTERS = 5;

    //Bonus floors.
    public static final int BONUS_FLOORS = 1;

    //Number of floors.
    public static final int MAX_DEPTH = CHAPTER_LENGTH * NUM_CHAPTERS + BONUS_FLOORS;

    //Hero exp cap
    public static final int HERO_EXP_CAP = MAX_DEPTH + 4;

    //SoU per chapter
    public static final int SOU_PER_CHAPTER = 3;

    //
    //############################## UI STUFF ##############################
    //

    //TODO: add ability for more quickslots
    public static final int MAX_QUICKSLOTS = 6;
    public static final int MIN_QUICKSLOTS = 2;

}
