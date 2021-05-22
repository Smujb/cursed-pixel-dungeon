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
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.NPC;
import com.shatteredpixel.yasd.general.effects.MagicMissile;
import com.shatteredpixel.yasd.general.items.weapon.melee.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.WardSprite;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfWarding extends DamageWand {

	{
		image = ItemSpriteSheet.Wands.WARDING;

		damageFactor = 1.2f;

		statScaling.add(Hero.HeroStat.RESILIENCE);
	}

	@Override
	protected int collisionProperties(int target) {
		if (Dungeon.level.heroFOV[target])  return Ballistica.STOP_TARGET;
		else                                return Ballistica.PROJECTILE;
	}

	private boolean wardAvailable = true;
	
	@Override
	public boolean tryToZap(Char owner, int target) {
		
		int currentWardEnergy = 0;
		for (Char ch : Actor.chars()){
			if (ch instanceof Ward){
				currentWardEnergy += ((Ward) ch).tier;
			}
		}
		
		int maxWardEnergy = 0;
		for (Buff buff : curUser.buffs()){
			if (buff instanceof Wand.Charger){
				if (((Charger) buff).wand() instanceof WandOfWarding){
					maxWardEnergy += 2 + ((Charger) buff).wand().power();
				}
			}
		}
		
		wardAvailable = (currentWardEnergy < maxWardEnergy);
		
		Char ch = Actor.findChar(target);
		if (ch instanceof Ward){
			if (!wardAvailable && ((Ward) ch).tier <= 3){
				GLog.warning( Messages.get(this, "no_more_wards"));
				return false;
			}
		} else {
			if ((currentWardEnergy + 1) > maxWardEnergy){
				GLog.warning( Messages.get(this, "no_more_wards"));
				return false;
			}
		}
		
		return super.tryToZap(owner, target);
	}
	
	@Override
	public void onZap(Ballistica bolt) {

		int target = bolt.collisionPos;
		Char ch = Actor.findChar(target);
		if (ch != null && !(ch instanceof Ward)){
			if (bolt.dist > 1) target = bolt.path.get(bolt.dist-1);

			ch = Actor.findChar(target);
			if (ch != null && !(ch instanceof Ward)){
				GLog.warning( Messages.get(this, "bad_location"));
				Dungeon.level.pressCell(bolt.collisionPos);
				return;
			}
		}

		if (!Dungeon.level.passable(target)){

			GLog.warning( Messages.get(this, "bad_location"));
			Dungeon.level.pressCell(target);
			
		} else if (ch != null){
			if (ch instanceof Ward){
				if (wardAvailable) {
					((Ward) ch).upgrade((int)power());
				} else {
					((Ward) ch).wandHeal( (int)power() );
				}
				ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Ward) ch).tier);
			} else {
				GLog.warning( Messages.get(this, "bad_location"));
				Dungeon.level.pressCell(target);
			}

		} else {
			Ward ward = Mob.create(Ward.class);
			ward.pos = target;
			ward.wandLevel = (int)power();
			GameScene.add(ward, 1f);
			Dungeon.level.occupyCell(ward);
			ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, ward.tier);
			Dungeon.level.pressCell(target);
			ward.alignment = curUser.alignment;
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile m = MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.WARD,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		
		if (bolt.dist > 10){
			m.setSpeed(bolt.dist*20);
		}
		Sample.INSTANCE.play(Assets.Sounds.ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

		int level = Math.round(Math.max( 0, staff.power() ));

		// lvl 0 - 20%
		// lvl 1 - 33%
		// lvl 2 - 43%
		if (Random.Int( level + 5 ) >= 4) {
			for (Char ch : Actor.chars()){
				if (ch instanceof Ward){
					((Ward) ch).wandHeal(level);
					ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Ward) ch).tier);
				}
			}
		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color( 0x8822FF );
		particle.am = 0.3f;
		particle.setLifespan(3f);
		particle.speed.polar(Random.Float(PointF.PI2), 0.3f);
		particle.setSize( 1f, 2f);
		particle.radiateXY(2.5f);
	}

	private static float realMin(float lvl) {
		return Math.round(2*lvl);    //level scaling
	}

	private static float realMax(float lvl) {
		return (int) (15*lvl*1.2f);   //level scaling
	}

	@Override
	public float min(float lvl) {
		return realMin(lvl);
	}

	@Override
	public float max(float lvl) {
		return realMax(lvl);
	}

	@Override
	public String statsDesc() {
		if (levelKnown)
			return Messages.get(this, "stats_desc", power()+2);
		else
			return Messages.get(this, "stats_desc", 2);
	}

	public static class Ward extends NPC {

		public int tier = 1;
		private int wandLevel = 1;

		public int totalZaps = 0;

		{
			spriteClass = WardSprite.class;

			properties.add(Char.Property.IMMOVABLE);
			properties.add(Property.INORGANIC);

			viewDistance = 4;
			range = viewDistance;
			state = WANDERING;

		}

		@Override
		protected boolean act() {
			throwItem();
			return super.act();
		}


		{
			immunities.add( Corruption.class );
		}

		@Override
		public String name() {
			return Messages.get(this, "name_" + tier);
		}

		@Override
		public Element elementalType() {
			return Element.DESTRUCTION;
		}

		public void upgrade(int wandLevel ){
			if (this.wandLevel < wandLevel){
				this.wandLevel = wandLevel;
			}

			wandHeal(0);

			switch (tier){
				case 1: case 2: default:
					break; //do nothing
				case 3:
					HT = 30;
					HP = 10 + (5-totalZaps)*4;
					break;
				case 4:
					HT = 48;
					HP += 18;
					break;
				case 5:
					HT = 70;
					HP += 22;
					break;
				case 6:
					wandHeal(wandLevel);
					break;
			}

			if (tier < 6){
				tier++;
				viewDistance++;
				range++;
				if (sprite != null){
					((WardSprite)sprite).updateTier(tier);
					sprite.place(pos);
				}
				GameScene.updateFog(pos, viewDistance+1);
			}
		}

		private void wandHeal( int wandLevel ){
			if (this.wandLevel < wandLevel){
				this.wandLevel = wandLevel;
			}

			int heal;
			switch(tier){
				default:
					return;
				case 4:
					heal = 8;
					break;
				case 5:
					heal = 10;
					break;
				case 6:
					heal = 15;
					break;
			}

			heal(heal);
			if (sprite != null) sprite.showStatus(CharSprite.POSITIVE, Integer.toString(heal));
		}

		@Override
		public int defenseSkill(Char enemy) {
			if (tier > 3){
				defenseSkill = 4 + Dungeon.getScaling();
			}
			return super.defenseSkill(enemy);
		}

		@Override
        public float attackDelay() {
			if (tier > 3){
				return 1f;
			} else {
				return 2f;
			}
		}

		@Override
		public boolean attack(Char enemy, boolean guaranteed, int dmg, DamageSrc src) {
			boolean hit = super.attack(enemy, true, dmg, src);
			if (hit) {
				totalZaps++;
				switch (tier) {
					case 1: case 2: case 3: default:
						if (totalZaps >= (2*tier-1)){
							die(new DamageSrc(Element.META, this));
						}
						break;
					case 4:
						damage(5, this);
						break;
					case 5:
						damage(6, this);
						break;
					case 6:
						damage(7, this);
						break;
				}
			}
			return false;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(Math.round(realMin(wandLevel)), Math.round(realMax(wandLevel)));
		}

		@Override
		protected boolean getCloser(int target) {
			return false;
		}

		@Override
		protected boolean getFurther(int target) {
			return false;
		}

		@Override
		public CharSprite sprite() {
			WardSprite sprite = (WardSprite) super.sprite();
			sprite.linkVisuals(this);
			return sprite;
		}

		@Override
		public void updateSpriteState() {
			super.updateSpriteState();
			((WardSprite)sprite).updateTier(tier);
			sprite.place(pos);
		}
		
		@Override
		public void destroy() {
			super.destroy();
			Dungeon.observe();
			GameScene.updateFog(pos, viewDistance+1);
		}
		
		@Override
		public boolean canInteract(Char ch) {
			return (this.alignment == Alignment.ALLY);
		}

		@Override
		public boolean interact( Char c ) {
			if (c != Dungeon.hero){
				return true;
			}
			if (alignment == c.alignment) {
				Game.runOnRenderThread(new Callback() {
					@Override
					public void call() {
						GameScene.show(new WndOptions(Messages.get(Ward.this, "dismiss_title"),
								Messages.get(Ward.this, "dismiss_body"),
								Messages.get(Ward.this, "dismiss_confirm"),
								Messages.get(Ward.this, "dismiss_cancel")) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									die(new DamageSrc(Element.META, null));
								}
							}
						});
					}
				});
				return true;
			} else {
				return super.interact(c);
			}
		}

		@Override
		public String description() {return Messages.get(this, "desc_" + tier, 2+wandLevel, 8 + 4*wandLevel, tier );
		}

		private static final String TIER = "tier";
		private static final String WAND_LEVEL = "wand_level";
		private static final String TOTAL_ZAPS = "total_zaps";

		@Override
		public void storeInBundle( Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(TIER, tier);
			bundle.put(WAND_LEVEL, wandLevel);
			bundle.put(TOTAL_ZAPS, totalZaps);
		}

		@Override
		public void restoreFromBundle( Bundle bundle) {
			super.restoreFromBundle(bundle);
			tier = bundle.getInt(TIER);
			viewDistance = 3 + tier;
			wandLevel = bundle.getInt(WAND_LEVEL);
			totalZaps = bundle.getInt(TOTAL_ZAPS);
		}
		
		{
			properties.add(Property.IMMOVABLE);
		}
	}
}
