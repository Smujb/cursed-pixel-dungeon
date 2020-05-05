/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.levels.terrain;

import com.watabou.utils.Bundlable;

//Merges bundlable and KindOfTerrain. Also makes some of the abstract functions of KindOfTerrain into variables.
public abstract class CustomTerrain implements KindOfTerrain, Bundlable {
	public abstract String desc();
	public abstract String name();

	protected boolean passable 	    = false;
	protected boolean losBlocking   = false;
	protected boolean flammable     = false;
	protected boolean secret 		= false;
	protected boolean solid 		= false;
	protected boolean avoid 		= false;
	protected boolean liquid 		= false;
	protected boolean pit 			= false;

	@Override
	public boolean passable() {
		return passable;
	}

	@Override
	public boolean losBlocking() {
		return losBlocking;
	}

	@Override
	public boolean flammable() {
		return flammable;
	}

	@Override
	public boolean secret() {
		return secret;
	}

	@Override
	public boolean solid() {
		return solid;
	}

	@Override
	public boolean avoid() {
		return avoid;
	}

	@Override
	public boolean liquid() {
		return liquid;
	}

	@Override
	public boolean pit() {
		return pit;
	}

	@Override
	public KindOfTerrain discover() {
		return this;
	}
}
