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

package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.EyeSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

public class Eye extends Mob {

	{
		spriteClass = EyeSprite.Purple.class;

		healthFactor = 1.8f;
		damageFactor = 0.7f;

		viewDistance = Light.DISTANCE;

        flying = true;

		HUNTING = new Hunting();

		loot = new Dewdrop();
		lootChance = 0.5f;

		range = 1;
		shotType = Ballistica.MAGIC_BOLT;

		properties.add(Property.DEMONIC);
	}

	@Override
	public Element elementalType() {
		return beamCharged() ? Element.DESTRUCTION : Element.PHYSICAL;
	}

	@Override
	public int damageRoll() {
		return beamCharged() ? (int) (super.damageRoll() * beamDMGFactor) : super.damageRoll();
	}

	@Override
	public float attackDelay() {
		return beamCharged() ? beamDLY : super.attackDelay();
	}

	private Ballistica beam;
	private int beamTarget = -1;
	private int beamCooldown;
	private float beamCharged;
	protected float beamDLY = 1f;
	protected float beamDMGFactor = 5f;

	public boolean beamCharged() {
		return beamCharged > 0;
	}

	//generates an average of 1 dew, 0.25 seeds, and 0.25 stones
	@Override
	protected Item createLoot() {
		Item loot;
		switch(Random.Int(4)){
			case 0: case 1: default:
				loot = new Dewdrop().quantity(2);
				break;
			case 3:
				loot = Generator.random(Generator.Category.SEED);
				break;
			case 4:
				loot = Generator.random(Generator.Category.STONE);
				break;
		}
		return loot;
	}

	@Override
    public boolean canAttack(@NotNull Char enemy) {

		if (beamCooldown <= 0) {
			Ballistica aim = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);

			if (enemy.invisible == 0 && !isCharmedBy(enemy) && fieldOfView[enemy.pos] && aim.subPath(1, aim.dist).contains(enemy.pos)){
				beam = aim;
				beamTarget = enemy.pos;
				return true;
			} else
				//if the beam is charged, it has to attack, will aim at previous location of target.
				return beamCharged();
		} else
			return super.canAttack(enemy);
	}

	@Override
	protected boolean act() {
		if (beamCharged() && state != HUNTING){
			beamCharged = 0;
		}
		if (beam == null && beamTarget != -1) {
			beam = new Ballistica(pos, beamTarget, Ballistica.STOP_TERRAIN);
			sprite.turnTo(pos, beamTarget);
		}
		if (beamCooldown > 0)
			beamCooldown--;
		return super.act();
	}

	@Override
	protected boolean doAttack( Char enemy ) {

		if (beamCooldown > 0) {
			return super.doAttack(enemy);
		} else if (!beamCharged()){
			((EyeSprite)sprite).charge( enemy.pos );
			spend(attackDelay() * 2f);
			beamCharged = 1;
			return true;
		} else {
			if (sprite != null) {
				sprite.zap( beamTarget );
				spend( attackDelay() );
				return false;
			} else {
				spend( attackDelay() );
				return false;
			}
		}
	}

	public void beamUsed() {
		beamCharged -= beamDLY;
		if (!beamCharged()) {
			beamCooldown = Random.IntRange(4, 6);
		}
	}

	@Override
	public void damage(int dmg,  DamageSrc src) {
		if (!beamCharged()) {//Now immune when beam is charged
			super.damage(dmg, src);
		}
	}

	private static final String BEAM_TARGET     = "beamTarget";
	private static final String BEAM_COOLDOWN   = "beamCooldown";
	private static final String BEAM_CHARGED    = "beamCharged";

	@Override
	public void storeInBundle( Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( BEAM_TARGET, beamTarget);
		bundle.put( BEAM_COOLDOWN, beamCooldown );
		bundle.put( BEAM_CHARGED, beamCharged );
	}

	@Override
	public void restoreFromBundle( Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(BEAM_TARGET))
			beamTarget = bundle.getInt(BEAM_TARGET);
		beamCooldown = bundle.getInt(BEAM_COOLDOWN);
		beamCharged = bundle.getInt(BEAM_CHARGED);
	}

	{
		resistances.put( Element.DESTRUCTION, 0.5f );
	}

	private class Hunting extends Mob.Hunting{
		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			//even if enemy isn't seen, attack them if the beam is charged
			if (beamCharged() && enemy != null && canAttack(enemy)) {
				enemySeen = enemyInFOV;
				return doAttack(enemy);
			}
			return super.act(enemyInFOV, justAlerted);
		}
	}

	public static class SalvoEye extends Eye {
		{
			spriteClass = EyeSprite.Red.class;
			beamDLY = 1/3f;
			beamDMGFactor = 2.5f;
		}
	}
}
