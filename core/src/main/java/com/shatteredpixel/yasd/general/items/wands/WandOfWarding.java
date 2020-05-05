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
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
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
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfWarding extends DamageWand {

	{
		collisionProperties = Ballistica.STOP_TARGET;

		image = ItemSpriteSheet.WAND_WARDING;
	}
	
	private boolean wardAvailable = true;
	
	@Override
	public boolean tryToZap(Char owner, int target) {
		
		int currentWardEnergy = 0;
		for (Char ch : Actor.chars()){
			if (ch instanceof Ward){
				currentWardEnergy += ((Ward) ch).tier + 1;
			}
		}
		
		int maxWardEnergy = 0;
		for (Buff buff : curUser.buffs()){
			if (buff instanceof Wand.Charger){
				if (((Charger) buff).wand() instanceof WandOfWarding){
					maxWardEnergy += 3 + ((Charger) buff).wand().actualLevel()*3;
				}
			}
		}
		
		wardAvailable = (currentWardEnergy < maxWardEnergy);
		
		Char ch = Actor.findChar(target);
		if (ch instanceof Ward){
			if (!wardAvailable && ((Ward) ch).tier <= 3){
				GLog.w( Messages.get(this, "no_more_wards"));
				return false;
			}
		} else {
			if ((currentWardEnergy + 2) > maxWardEnergy){
				GLog.w( Messages.get(this, "no_more_wards"));
				return false;
			}
		}
		
		return super.tryToZap(owner, target);
	}
	
	@Override
	public void onZap(Ballistica bolt) {
		
		Char ch = Actor.findChar(bolt.collisionPos);
		if (!curUser.fieldOfView[bolt.collisionPos] || !Dungeon.level.passable(bolt.collisionPos)){
			GLog.w( Messages.get(this, "bad_location"));
			Dungeon.level.pressCell(bolt.collisionPos);
			
		} else if (ch != null){
			if (ch instanceof Ward){
				if (wardAvailable) {
					((Ward) ch).upgrade((int)actualLevel());
				} else {
					((Ward) ch).wandHeal( (int)actualLevel() );
				}
				ch.sprite.emitter().burst(MagicMissile.WardParticle.UP, ((Ward) ch).tier);
			} else {
				GLog.w( Messages.get(this, "bad_location"));
				Dungeon.level.pressCell(bolt.collisionPos);
			}

		} else if (canPlaceWard(bolt.collisionPos)){
			Ward ward = Mob.create(Ward.class);
			ward.pos = bolt.collisionPos;
			ward.wandLevel = (int)actualLevel();
			GameScene.add(ward, 1f);
			Dungeon.level.occupyCell(ward);
			ward.sprite.emitter().burst(MagicMissile.WardParticle.UP, ward.tier);
			Dungeon.level.pressCell(bolt.collisionPos);
			ward.alignment = curUser.alignment;
		} else {
			GLog.w( Messages.get(this, "bad_location"));
			Dungeon.level.pressCell(bolt.collisionPos);
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
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {

		int level = Math.max( 0, staff.level() );

		// lvl 0 - 20%
		// lvl 1 - 33%
		// lvl 2 - 43%
		if (Random.Int( level + 5 ) >= 4) {
			for (Char ch : Actor.chars()){
				if (ch instanceof Ward){
					((Ward) ch).wandHeal(staff.level());
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

	private static boolean canPlaceWard(int pos){

		for (int i : PathFinder.CIRCLE8){
			if (Actor.findChar(pos+i) instanceof Ward){
				return false;
			}
		}

		return true;

	}

	private static float realMin(float lvl) {
		return 2 + lvl;
	}

	private static float realMax(float lvl) {
		return 8 + 4 * lvl;
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
			return Messages.get(this, "stats_desc", (int)(actualLevel()+3));
		else
			return Messages.get(this, "stats_desc", 3);
	}

	public static class Ward extends NPC {

		public int tier = 1;
		private int wandLevel = 1;

		private int totalZaps = 0;

		{
			spriteClass = WardSprite.class;

			alignment = curUser.alignment;

			properties.add(Char.Property.IMMOVABLE);

			viewDistance = 3;
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
					HP = HT = 30;
					break;
				case 4:
					HT = 48;
					HP = Math.round(48*(HP/30f));
					break;
				case 5:
					HT = 70;
					HP = Math.round(70*(HP/48f));
					break;
			}

			if (tier < 6){
				tier++;
				viewDistance++;
				range++;
				updateSpriteState();
				GameScene.updateFog(pos, viewDistance+1);
			}
		}

		private void wandHeal( int wandLevel ){
			if (this.wandLevel < wandLevel){
				this.wandLevel = wandLevel;
			}

			switch(tier){
				default:
					break;
				case 4:
					HP = Math.min(HT, HP+6);
					break;
				case 5:
					HP = Math.min(HT, HP+8);
					break;
				case 6:
					HP = Math.min(HT, HP+12);
					break;
			}
		}

		@Override
		public int defenseSkill(Char enemy) {
			if (tier > 3){
				defenseSkill = 4 + Dungeon.getScaleFactor();
			}
			return super.defenseSkill(enemy);
		}

		@Override
		public int drRoll(Element element) {
			if (tier > 3){
				return Math.round(Random.NormalIntRange(0, 3 + Dungeon.getScaleFactor() /2) / (7f - tier));
			} else {
				return 0;
			}
		}

		@Override
        public float attackDelay() {
			switch (tier){
				case 1: case 2: default:
					return 2f;
				case 3: case 4:
					return 1.5f;
				case 5: case 6:
					return 1f;
			}
		}

		@Override
		public boolean attack(Char enemy, boolean guaranteed) {
			boolean hit = super.attack(enemy, true);
			if (hit) {
				totalZaps++;
				switch (tier) {
					case 1:
					default:
						if (totalZaps >= tier) {
							die(new DamageSrc(Element.META, this));
						}
						break;
					case 2:
					case 3:
						if (totalZaps > tier) {
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
			if (alignment == curUser.alignment) {
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
		public String description() {
			return Messages.get(this, "desc_" + tier, 2+wandLevel, 8 + 4*wandLevel );
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
			viewDistance = 2 + tier;
			wandLevel = bundle.getInt(WAND_LEVEL);
			totalZaps = bundle.getInt(TOTAL_ZAPS);
		}
		
		{
			properties.add(Property.IMMOVABLE);
		}
	}
}
