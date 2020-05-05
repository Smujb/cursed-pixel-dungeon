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

import java.util.Calendar;

public enum Holiday {
    NONE,
    EASTER, //1st and 2nd week of April
    HWEEN,//2nd week of october though first day of november
    XMAS; //3rd week of december through first week of january

    public static Holiday getHoliday() {
        final Calendar calendar = Calendar.getInstance();
        switch(calendar.get(Calendar.MONTH)){
            case Calendar.JANUARY:
                if (calendar.get(Calendar.WEEK_OF_MONTH) == 1)
                    return XMAS;
                break;
            case Calendar.OCTOBER:
                if (calendar.get(Calendar.WEEK_OF_MONTH) >= 2)
                    return HWEEN;
                break;
            case Calendar.NOVEMBER:
                if (calendar.get(Calendar.DAY_OF_MONTH) == 1)
                    return HWEEN;
                break;
            case Calendar.DECEMBER:
                if (calendar.get(Calendar.WEEK_OF_MONTH) >= 3)
                    return XMAS;
                break;
            case Calendar.APRIL:
                if (calendar.get(Calendar.WEEK_OF_MONTH) < 3) {
                    return EASTER;
                }
                break;
        }
        return NONE;
    }
}
