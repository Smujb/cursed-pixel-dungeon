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

package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Cripple;
import com.shatteredpixel.yasd.general.actors.buffs.Light;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.RainbowParticle;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfMagicMapping;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfPrismaticLight extends DamageWand {

	{
		image = ItemSpriteSheet.WAND_PRISMATIC_LIGHT;

		collisionProperties = Ballistica.MAGIC_BOLT;

		element = Element.LIGHT;
	}
	@Override
	public float min(float lvl){
		return 1+lvl;
	}
	@Override
	public float max(float lvl){
		return 5+3*lvl;
	}

	@Override
	public void onZap(Ballistica beam) {
		affectMap(beam);

		if (Dungeon.isChallenged(Challenges.DARKNESS)) {
			Buff.prolong(curUser, Light.class, 2f + level());
		} else {
			Buff.prolong(curUser, Light.class, 10f + level() * 5);
		}

		Char ch = Actor.findChar(beam.collisionPos);
		if (ch != null) {
			processSoulMark(ch, chargesPerCast());
			affectTarget(ch);
		}
	}

	private void affectTarget(Char ch){

		//three in (5+lvl) chance of failing
		if (Random.Int((int) (5 + actualLevel())) >= 3) {
			Buff.prolong(ch, Blindness.class, 2f + (level() * 0.333f));
			ch.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 6 );
		}

		if (ch.properties().contains(Char.Property.DEMONIC) || ch.properties().contains(Char.Property.UNDEAD)){
			ch.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10+level() );
			Sample.INSTANCE.play(Assets.SND_BURNING);

			hit(ch);
		} else {
			ch.sprite.centerEmitter().burst( RainbowParticle.BURST, 10+level() );

			hit(ch);
		}

	}

	private void affectMap(Ballistica beam){
		boolean noticed = false;
		for (int c : beam.subPath(0, beam.dist)){
			if (!Dungeon.level.insideMap(c)){
				continue;
			}
			for (int n : PathFinder.NEIGHBOURS9){
				int cell = c+n;

				if (Dungeon.level.discoverable[cell])
					Dungeon.level.mapped[cell] = true;

				KindOfTerrain terr = Dungeon.level.map[cell];
				if (Dungeon.level.secret(cell)) {

					Dungeon.level.discover( cell );

					GameScene.discoverTile( cell, terr );
					ScrollOfMagicMapping.discover(cell);

					noticed = true;
				}
			}

			CellEmitter.center(c).burst( RainbowParticle.BURST, Random.IntRange( 1, 2 ) );
		}
		if (noticed)
			Sample.INSTANCE.play( Assets.SND_SECRET );

		GameScene.updateFog();
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
		//cripples enemy
		Buff.prolong( defender, Cripple.class, 1f+staff.level());
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( Random.Int( 0x1000000 ) );
		particle.am = 0.5f;
		particle.setLifespan(1f);
		particle.speed.polar(Random.Float(PointF.PI2), 2f);
		particle.setSize( 1f, 2f);
		particle.radiateXY( 0.5f);
	}

}
