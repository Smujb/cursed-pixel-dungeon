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

package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.levels.terrain.CustomTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.tiles.CustomTilemap;
import com.shatteredpixel.yasd.general.tiles.DungeonTerrainTilemap;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.noosa.Image;

public class WndInfoCell extends Window {
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	
	public WndInfoCell( int cell ) {
		
		super();
		
		KindOfTerrain tile = Dungeon.level.map[cell];
		if (Dungeon.level.liquid()[cell]) {
			tile = Terrain.WATER;
		} else if (Dungeon.level.pit()[cell]) {
			tile = Terrain.CHASM;
		}

		CustomTilemap customTile = null;
		Image customImage = null;
		int x = cell % Dungeon.level.width();
		int y = cell / Dungeon.level.width();
		for (CustomTilemap i : Dungeon.level.customTiles){
			if ((x >= i.tileX && x < i.tileX+i.tileW) &&
					(y >= i.tileY && y < i.tileY+i.tileH)){
				if ((customImage = i.image(x - i.tileX, y - i.tileY)) != null) {
					x -= i.tileX;
					y -= i.tileY;
					customTile = i;
					break;
				}
			}
		}


		String desc = "";

		IconTitle titlebar = new IconTitle();
		if (customTile != null){
			titlebar.icon(customImage);

			String customName = customTile.name(x, y);
			if (customName != null) {
				titlebar.label(customName);
			} else {
				String name = "";
				if (tile instanceof Terrain) {
					name = Dungeon.level.tileName((Terrain)tile);
				} else if (tile instanceof CustomTerrain) {
					name = ((CustomTerrain)tile).name();
				}
				titlebar.label(name);
			}

			String customDesc = customTile.desc(x, y);
			if (customDesc != null) {
				desc += customDesc;
			} else {
				if (tile instanceof Terrain) {
					desc += Dungeon.level.tileDesc((Terrain) tile);
				} else if (tile instanceof CustomTerrain) {
					desc += ((CustomTerrain)tile).desc();
				}
			}

		} else {

			if (tile == Terrain.WATER) {
				Image water = new Image(Dungeon.level.waterTex());
				water.frame(0, 0, DungeonTilemap.SIZE, DungeonTilemap.SIZE);
				titlebar.icon(water);
			} else {
				titlebar.icon(DungeonTerrainTilemap.tile( cell, tile ));
			}
			String name = "";
			if (tile instanceof Terrain) {
				name = Dungeon.level.tileName((Terrain)tile);
			} else if (tile instanceof CustomTerrain) {
				name = ((CustomTerrain)tile).name();
			}
			titlebar.label(name);
			if (tile instanceof Terrain) {
				desc += Dungeon.level.tileDesc((Terrain)tile);
			} else if (tile instanceof CustomTerrain) {
				desc += ((CustomTerrain)tile).desc();
			}
			//desc += Dungeon.level.tileDesc(tile);

		}
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextBlock info = PixelScene.renderTextBlock(6);
		add(info);

		for (Blob blob:Dungeon.level.blobs.values()) {
			if (blob.volume > 0 && blob.cur[cell] > 0 && blob.tileDesc() != null) {
				if (desc.length() > 0) {
					desc += "\n\n";
				}
				desc += blob.tileDesc();
			}
		}
		
		info.text( desc.length() == 0 ? Messages.get(this, "nothing") : desc );
		info.maxWidth(WIDTH);
		info.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
		
		resize( WIDTH, (int)info.bottom()+2 );
	}
}
