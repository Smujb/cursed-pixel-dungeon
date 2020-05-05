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

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.blobs.WellWater;
import com.shatteredpixel.yasd.general.levels.features.Door;
import com.shatteredpixel.yasd.general.levels.features.HighGrass;
import com.watabou.noosa.Group;

import java.util.Arrays;
import java.util.HashSet;

public enum Terrain implements KindOfTerrain {

	NONE {
		@Override
		public void setup() {}
	},
	CHASM {
		@Override
		public void setup() {
			avoid = true;
			pit = true;
		}
	},
	EMPTY {
		@Override
		public void setup() {
			passable = true;
		}
	},
	GRASS {
		@Override
		public void setup() {
			passable = true;
			flammable = true;
		}
	},
	EMPTY_WELL {
		@Override
		public void setup() {
			passable = true;
		}
	},
	WATER {
		@Override
		public void setup() {
			passable = true;
			liquid = true;
		}
	},
	DEEP_WATER {
		@Override
		public void setup() {
			passable = true;
			liquid = true;
		}
	},
	WALL {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}
	},
	CRACKED_WALL {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}

		@Override
		public boolean explodable() {
			return true;
		}
	},
	SECRET_CRACKED_WALL {
		@Override
		public void setup() {
			secret = true;
			losBlocking = true;
			solid = true;
		}

		@Override
		public boolean explodable() {
			return true;
		}

		@Override
		public Terrain discover() {
			return CRACKED_WALL;
		}
	},
	DOOR {
		@Override
		public void setup() {
			passable = true;
			losBlocking = true;
			flammable = true;
			solid = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			Door.enter(cell);
		}
	},
	OPEN_DOOR {
		@Override
		public void setup() {
			passable = true;
			flammable = true;
		}
	},
	ENTRANCE {
		@Override
		public void setup() {
			passable = true;
		}
	},
	EXIT {
		@Override
		public void setup() {
			passable = true;
		}
	},
	EMBERS {
		@Override
		public void setup() {
			passable = true;
		}
	},
	DRY {
		@Override
		public void setup() {
			passable = true;
		}
	},
	LOCKED_DOOR {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}
	},
	BRONZE_LOCKED_DOOR {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}
	},
	PEDESTAL {
		@Override
		public void setup() {
			passable = true;
		}
	},
	WALL_DECO {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
		}
	},
	BARRICADE {
		@Override
		public void setup() {
			flammable = true;
			solid = true;
			losBlocking = true;
		}
	},
	EMPTY_SP {
		@Override
		public void setup() {
			passable = true;
		}
	},
	HIGH_GRASS {
		@Override
		public void setup() {
			passable = true;
			losBlocking = true;
			flammable = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			HighGrass.trample(Dungeon.level, cell);
		}
	},
	FURROWED_GRASS {
		@Override
		public void setup() {
			passable = true;
			losBlocking = true;
			flammable = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			HIGH_GRASS.press(cell, hard);
		}
	},
	SECRET_DOOR {
		@Override
		public void setup() {
			losBlocking = true;
			solid = true;
			secret = true;
		}

		@Override
		public Terrain discover() {
			return DOOR;
		}
	},
	INTERACTION {
		@Override
		public void setup() {
			passable = true;
		}
	},
	EMPTY_DECO {
		@Override
		public void setup() {
			passable = true;
		}
	},
	LOCKED_EXIT {
		@Override
		public void setup() {
			solid = true;
		}
	},
	UNLOCKED_EXIT {
		@Override
		public void setup() {
			passable = true;
		}
	},
	SIGN {
		@Override
		public void setup() {
			//passable = true;
			//flammable = true;
			solid = true;//Currently these are unused except for visual tile overrides where we want terrain to be solid with no other properties. Will likely change later.
		}
	},
	WELL {
		@Override
		public void setup() {
			avoid = true;
		}

		@Override
		public void press(int cell, boolean hard) {
			WellWater.affectCell(cell);
		}
	},
	STATUE {
		@Override
		public void setup() {
			solid = true;
		}
	},
	STATUE_SP {
		@Override
		public void setup() {
			solid = true;
		}
	},
	BOOKSHELF {
		@Override
		public void setup() {
			flammable = true;
			solid = true;
			losBlocking = true;
		}
	},
	ALCHEMY {
		@Override
		public void setup() {
			solid = true;
		}
	};

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

	Terrain() {
		setup();
	}

	public abstract void setup();

	@Override
	public KindOfTerrain discover() {
		return this;
	}

	@Override
	public boolean explodable() {
		return flammable;
	}

	@Override
	public Group getVisual(int pos, int x, int y) {
		return null;
	}

	@Override
	public boolean waterStitchable() {
		return new HashSet<>(Arrays.asList(
				Terrain.EMPTY, Terrain.GRASS, Terrain.EMPTY_WELL,
				Terrain.ENTRANCE, Terrain.EXIT, Terrain.EMBERS,
				Terrain.BARRICADE, Terrain.HIGH_GRASS, Terrain.FURROWED_GRASS, Terrain.EMPTY_DECO,
				Terrain.SIGN, Terrain.WELL, Terrain.STATUE, Terrain.ALCHEMY,
				Terrain.DOOR, Terrain.OPEN_DOOR, Terrain.LOCKED_DOOR, Terrain.BRONZE_LOCKED_DOOR
		)).contains(this);
	}

	public void press(int cell, boolean hard) {}
}