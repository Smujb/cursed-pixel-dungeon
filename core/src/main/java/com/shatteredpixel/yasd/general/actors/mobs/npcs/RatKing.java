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

package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.RatKingSprite;

public class RatKing extends NPC {

	{
		spriteClass = RatKingSprite.class;
		
		state = SLEEPING;
	}

    @Override
	public float speed() {
		return 2f;
	}
	
	@Override
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public void damage(int dmg,  DamageSrc src) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	//***This functionality is for when rat king may be summoned by a distortion trap

	@Override
	protected void onAdd() {
		super.onAdd();
		if (Dungeon.depth != 5){
			yell(Messages.get(this, "confused"));
		}
	}

	@Override
	protected boolean act() {
		if (Dungeon.depth < 6){
			if (pos == Dungeon.level.getExitPos()){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.getExitPos();
			}
		} else if (Dungeon.depth > 6){
			if (pos == Dungeon.level.getEntrancePos()){
				destroy();
				sprite.killAndErase();
			} else {
				target = Dungeon.level.getEntrancePos();
			}
		}
		return super.act();
	}

	//***
	
	@Override
	public boolean interact(Char c) {
		sprite.turnTo( pos, c.pos );

		if (c != Dungeon.hero){
			return super.interact(c);
		}
		if (state == SLEEPING) {
			notice();
			yell( Messages.get(this, "not_sleeping") );
			state = WANDERING;
		} else {
			yell( Messages.get(this, "what_is_it") );
		}
		return true;
	}
	
	@Override
	public String description() {
		return ((RatKingSprite)sprite).festive ?
				Messages.get(this, "desc_festive")
				: super.description();
	}
}
