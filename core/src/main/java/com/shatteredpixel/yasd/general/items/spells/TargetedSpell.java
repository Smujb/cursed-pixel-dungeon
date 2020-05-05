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

package com.shatteredpixel.yasd.general.items.spells;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public abstract class TargetedSpell extends Spell {
	
	protected int collisionProperties = Ballistica.PROJECTILE;
	
	@Override
	protected void onCast(Hero hero) {
		GameScene.selectCell(targeter);
	}
	
	protected abstract void affectTarget( Ballistica bolt, Char hero );
	
	protected void fx( Ballistica bolt, Callback callback ) {
		MagicMissile.boltFromChar( curUser.sprite.parent,
				MagicMissile.MAGIC_MISSILE,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	private CellSelector.Listener targeter = new CellSelector.Listener(this){
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {

				final TargetedSpell curSpell = (TargetedSpell) source;
				/*(if (source instanceof TargetedSpell) {
					curSpell = (TargetedSpell)source;
				} else {
					return;
				}*/
				
				final Ballistica shot = new Ballistica( curUser.pos, target, curSpell.collisionProperties);
				int cell = shot.collisionPos;
				
				curUser.sprite.zap(cell);
				
				//attempts to target the cell aimed at if something is there, otherwise targets the collision pos.
				if (Actor.findChar(target) != null)
					QuickSlotButton.target(Actor.findChar(target));
				else
					QuickSlotButton.target(Actor.findChar(cell));
				
				curUser.busy();
				Invisibility.dispel();
				
				curSpell.fx(shot, new Callback() {
					public void call() {
						curSpell.affectTarget(shot, curUser);
						curSpell.detach( curUser.belongings.backpack );
						updateQuickslot();
						curUser.spendAndNext( 1f );
					}
				});
				
			}
				
		}
		
		@Override
		public String prompt() {
			return Messages.get(TargetedSpell.class, "prompt");
		}
	};
	
}
