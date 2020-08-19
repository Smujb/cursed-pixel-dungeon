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

package com.shatteredpixel.yasd.general.utils;

import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Signal;

public class GLog {

	public static final String TAG = "GAME";
	
	public static final String POSITIVE		= "++ ";
	public static final String NEGATIVE		= "-- ";
	public static final String WARNING		= "** ";
	public static final String HIGHLIGHT	= "@@ ";
	public static final String DEBUG   		= "[DEBUG]: ";
	public static final String NEW_LINE	    = "\n";
	
	public static Signal<String> update = new Signal<>();
	
	public static void info(String text, Object... args ) {
		
		if (args.length > 0) {
			text = Messages.format( text, args );
		}
		
		DeviceCompat.log( TAG, text );
		update.dispatch( text );
	}

	public static void newLine(){
		update.dispatch( NEW_LINE );
	}

	public static void positive(String text, Object... args ) {
		info( POSITIVE + text, args );
	}
	
	public static void negative(String text, Object... args ) {
		info( NEGATIVE + text, args );
	}
	
	public static void warning(String text, Object... args ) {
		info( WARNING + text, args );
	}
	
	public static void highlight(String text, Object... args ) {
		info( HIGHLIGHT + text, args );
	}

	public static void debug(String text, Object... args ) {
		if (CPDSettings.debugReport()) {
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			StringBuilder toLog = new StringBuilder(DEBUG + text);
			toLog.append("\n" + "Trace:\n");
			for (StackTraceElement element : trace) {
				toLog.append(element.toString()).append("\n");
			}
			info(toLog.toString(), args);
		}
	}
}
