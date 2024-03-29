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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Charm;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.BloodParticle;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfTransfusion extends DamageWand {

	{
		image = ItemSpriteSheet.Wands.TRANSFUSION;

		collisionProperties = Ballistica.PROJECTILE;

		statScaling.add(Hero.HeroStat.SUPPORT);
	}

	private boolean freeCharge = false;

	@Override
	public void onZap(Ballistica beam) {

		for (int c : beam.subPath(0, beam.dist))
			CellEmitter.center(c).burst( BloodParticle.BURST, 1 );

		int cell = beam.collisionPos;

		Char ch = Actor.findChar(cell);

		if (ch instanceof Mob){
			
			processSoulMark(ch, chargesPerCast());
			
			//this wand does different things depending on the target.
			
			//heals/shields an ally or a charmed enemy while damaging self
			if (ch.alignment == Char.Alignment.ALLY || ch.buff(Charm.class) != null){
				
				float healing = healAmt();

				ch.heal((int) healing, true);

			//for enemies...
			} else {
				if (ch.properties().contains(Char.Property.UNDEAD)) {
					//harms the undead
					hit(ch);
					ch.sprite.emitter().start(ShadowParticle.UP, 0.05f, (int) (2 * power()));
					Sample.INSTANCE.play(Assets.Sounds.BURNING);
				} else {
					ch.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, (int) (3 * power()) );
				}
				Buff.affect(ch, Charm.class, 3 * power()).object = curUser.id();
			}
			if (!freeCharge) {
				damageHero(selfDMG());
			} else {
				freeCharge = false;
			}
			
		}
		
	}

	//this wand costs health too
	private void damageHero(int damage){
		
		curUser.damage(damage, new Char.DamageSrc(Element.SHADOW, this).ignoreDefense());

		if (!curUser.isAlive()){
			Dungeon.fail( getClass() );
			GLog.negative( Messages.get(this, "ondeath") );
		}
	}

	@Override
	protected int initialCharges() {
		return 1;
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		// lvl 0 - 10%
		// lvl 1 - 18%
		// lvl 2 - 25%
		if (Random.Int( Math.round(Math.max( 0, staff.power() )) + 10 ) >= 9){
			//grants a free use of the staff
			freeCharge = true;
			GLog.positive( Messages.get(this, "charged") );
			attacker.sprite.emitter().burst(BloodParticle.BURST, 20);
		}
	}

	@Override
	public String statsDesc() {
		if (levelKnown) //Always show self damage for the hero, not for cur user
						//This fixes issues when the wand is on the floor (has no cur user) or is owned by a statue
			return Messages.get(this, "stats_desc", selfDMG(Dungeon.hero), healAmt(), min(), max());
		else
			return Messages.get(this, "stats_desc", selfDMG(Dungeon.hero), healAmt(1), min(1), max(1));
	}

	protected int healAmt(float power) {
		return (int) (10*power);
	}

	protected final int healAmt() {
		return healAmt(power());
	}

	private int selfDMG(Char ch) {
		return Math.round(ch.HT*0.10f);
	}

	private int selfDMG() {
		return selfDMG(curUser);
	}

	@Override
	protected void fx(Ballistica beam, Callback callback) {
		curUser.sprite.parent.add(
				new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0xCC0000 );
		particle.am = 0.6f;
		particle.setLifespan(1f);
		particle.speed.polar( Random.Float(PointF.PI2), 2f );
		particle.setSize( 1f, 2f);
		particle.radiateXY(0.5f);
	}

	private static final String FREECHARGE = "freecharge";

	@Override
	public void restoreFromBundle(  Bundle bundle) {
		super.restoreFromBundle(bundle);
		freeCharge = bundle.getBoolean( FREECHARGE );
	}

	@Override
	public void storeInBundle(  Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FREECHARGE, freeCharge );
	}

}
