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
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.CorrosiveGas;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.Invisibility;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.MirrorSprite;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public class MirrorImage extends NPC {
	
	{
		spriteClass = MirrorSprite.class;
		
		HP = HT = 1;
		defenseSkill = 1;
		
		alignment = Alignment.ALLY;
		state = HUNTING;
		
		//before other mobs
		actPriority = MOB_PRIO + 1;
	}
	
	private Char user;
	private int heroID;
	public int armTier;
	
	@Override
	protected boolean act() {
		
		if ( user == null ){
			user = (Hero)Actor.findById(heroID);
			if ( user == null ){
				die(new DamageSrc(Element.META, null));
				sprite.killAndErase();
				return true;
			}
		}
		if (user instanceof Hero) {
			if (((Hero)user).tier() != armTier) {
				armTier = ((Hero)user).tier();
				((MirrorSprite) sprite).updateArmor(armTier);
			}
		}
		
		return super.act();
	}
	
	private static final String HEROID	= "hero_id";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( HEROID, heroID );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		heroID = bundle.getInt( HEROID );
	}
	
	public void duplicate( Char hero ) {
		this.user = hero;
		heroID = this.user.id();
		Buff.affect(this, MirrorInvis.class, Short.MAX_VALUE);
	}
	
	@Override
	public int damageRoll() {
		int damage = user.damageRoll();
		return (damage+1)/2; //half user damage, rounded up
	}
	
	@Override
	public int attackSkill( Char target ) {
		return user.attackSkill(target);
	}
	
	@Override
	public int defenseSkill(Char enemy) {
		if (user != null) {
			int baseEvasion;
			if (user instanceof Hero){
				baseEvasion = 4 + ((Hero)user).lvl;
			} else {
				baseEvasion = 4 + Dungeon.depth;
			}
			int heroEvasion = user.defenseSkill(enemy);
			
			//if the user has more/less evasion, 50% of it is applied
			return super.defenseSkill(enemy) * (baseEvasion + heroEvasion) / 2;
		} else {
			return 0;
		}
	}
	
	@Override
    public float attackDelay() {
		return user.attackDelay(); //handles ring of furor
	}
	
	@Override
    public boolean canAttack(@NotNull Char enemy) {
		return super.canAttack(enemy) || (user.belongings.getWeapon() != null && user.belongings.getWeapon().canReach(this, enemy.pos));
	}

    @Override
	public int attackProc( Char enemy, int damage ) {
		damage = super.attackProc( enemy, damage );
		
		MirrorInvis buff = buff(MirrorInvis.class);
		if (buff != null){
			buff.detach();
		}
		
		if (enemy instanceof Mob) {
			((Mob)enemy).aggro( this );
		}
		if (user.belongings.getWeapon() != null){
			return user.belongings.getWeapon().proc( this, enemy, damage );
		} else {
			return damage;
		}
	}
	
	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		
		//pre-0.7.0 saves
		if (heroID == 0){
			heroID = Dungeon.hero.id();
		}
		
		user = (Hero)Actor.findById(heroID);
		if (user != null) {
			armTier = ((Hero)user).tier();
		}
		((MirrorSprite)s).updateArmor( armTier );
		return s;
	}
	
	{
		immunities.add( ToxicGas.class );
		immunities.add( CorrosiveGas.class );
		immunities.add( Burning.class );
		immunities.add( Corruption.class );
	}
	
	public static class MirrorInvis extends Invisibility {
		
		{
			announced = false;
		}
		
		@Override
		public int icon() {
			return BuffIndicator.NONE;
		}
	}
}