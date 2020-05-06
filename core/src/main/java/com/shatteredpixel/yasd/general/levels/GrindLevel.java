package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Gold;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.levels.painters.Painter;
import com.shatteredpixel.yasd.general.levels.painters.SewerPainter;
import com.shatteredpixel.yasd.general.levels.traps.WornDartTrap;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GrindLevel extends RegularLevel {

	@Override
	public float respawnTime() {//Respawn time depends on current number of mobs.
		if (mobs.size() > 6) {
			return super.respawnTime() / 2f;
		} else if  (mobs.size() > 2) {
			return super.respawnTime() / 5f;
		} else {
			return super.respawnTime() / 10f;
		}
	}

	@Override
	protected Class<?>[] trapClasses() {
		return new Class<?>[]{ WornDartTrap.class };
	}

	@Override
	protected float[] trapChances() {
		return new float[]{1};
	}

	@Override
	public String tilesTex() {
		return Assets.TILES_HEAVEN;
	}

	@Override
	public String waterTex() {
		return Assets.WATER_HEAVEN;
	}

	@Override
	public String loadImg() {
		return Assets.WATER_HEAVEN;
	}

	@Override
	protected int specialRooms() {
		return 0;
	}

	@Override
	protected Painter painter() {
		return new SewerPainter();
	}

	@Override
	protected void createItems() {}//Does nothing. Prevents Ghost from spawning, also makes sense that all loot is supposed to come from killing Guardians.

	public static class Guardian extends Mob {

		{
			spriteClass = StatueSprite.class;

			EXP = 0;
			state = WANDERING;
		}
		int lootAmt = 1;

		@Override
		public String description() {
			return Messages.get(Guardian.class,"desc") + "\n\n" + super.description();
		}

		int getScaleFactor() {
			return Math.min(30,Math.max(0, Dungeon.hero.lvl-1));
		}

		public Guardian() {
			super();
			HP = HT = 20 + 6 * getScaleFactor();
			aggro(Dungeon.hero);
		}

		@Override
		public void die(DamageSrc cause) {
			for (int i = 0; i < lootAmt; i++) {
				Item luckybadgedrop = LuckyBadge.tryForBonusDrop(Dungeon.hero);
				if (luckybadgedrop != null) {
					if (luckybadgedrop instanceof Gold || !luckybadgedrop.collect()) {
						Dungeon.level.drop(luckybadgedrop, Dungeon.hero.pos).sprite.drop();
					}
				}
			}
			super.die(cause);
		}

		@Override
		protected boolean act() {
			beckon( Dungeon.hero.pos );
			return super.act();
		}
	}

	public static class GreenGuardian extends Guardian {

		private boolean canParalyze = true;
		private final String CAN_PARALYZE = "can_paralyze";
		{
			spriteClass = GreenGuardianSprite.class;
			evasionFactor = 0.5f;
			accuracyFactor = 2f;
			drFactor = 2f;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			if (canParalyze & Random.Int(3) == 0) {
				Buff.affect(enemy, Paralysis.class, Paralysis.DURATION/2f);
				canParalyze = false;
			}
			return super.attackProc(enemy, damage);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			bundle.put( CAN_PARALYZE, canParalyze );
			super.storeInBundle(bundle);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			canParalyze = bundle.getBoolean( CAN_PARALYZE );
			super.restoreFromBundle(bundle);
		}
	}
	public static class RedGuardian extends Guardian {
		{
			spriteClass = RedGuardianSprite.class;
			baseSpeed = 2f;
			accuracyFactor = 2f;
			damageFactor = 0.75f;
			drFactor = 0.5f;
			evasionFactor = 1.5f;
			attackDelay = 0.5f;
			HP = HT = (int) (super.HT*0.67f);
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			if (Random.Int(3) == 0) {
				enemy.damage(Math.max(10, enemy.HP) / 10, this);
			}
			return super.attackProc(enemy, damage);
		}
	}

	public static class BlueGuardian extends Guardian {
		{
			spriteClass = BlueGuardianSprite.class;
			baseSpeed = 0.5f;
			evasionFactor = 0.5f;
			drFactor = 2f;
			lootAmt = 2;
			resistances.put(Element.SPIRIT, 0.5f);
		}
	}

	public static class PurpleGuardian extends BlueGuardian {
		{
			spriteClass = PurpleGuardianSprite.class;
			lootAmt = 3;//Tanky and rare so 1 (normal) +1 (tanky) +1 (rare)
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			int healAmt = Math.min(4,damage)/4;
			healAmt = Math.min(healAmt, this.HT - this.HP);

			if (healAmt > 0 && this.isAlive()) {
				this.HP += healAmt;
				this.sprite.emitter().start(Speck.factory(Speck.DISCOVER), 0.4f, 1);
				this.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healAmt));
			}
			return super.attackProc(enemy, damage);
		}
	}

	public static class YellowGuardian extends Guardian {
		private int delay = 0;
		private static final int BLINK_DELAY = 5;
		{
			spriteClass = YellowGuardianSprite.class;
			HP = HT = super.HT/2;
			damageFactor = 1.3f;
			drFactor = 0.5f;
			attackDelay = 2f;
		}

		private void blink(int target ) {

			Ballistica route = new Ballistica( pos, target, Ballistica.PROJECTILE);
			int cell = route.collisionPos;

			//can't occupy the same cell as another char, so move back one.
			if (Actor.findChar( cell ) != null && cell != this.pos)
				cell = route.path.get(route.dist-1);

			if (Dungeon.level.avoid(cell)){
				ArrayList<Integer> candidates = new ArrayList<>();
				for (int n : PathFinder.NEIGHBOURS8) {
					cell = route.collisionPos + n;
					if (Dungeon.level.passable(cell) && Actor.findChar( cell ) == null) {
						candidates.add( cell );
					}
				}
				if (candidates.size() > 0) {
					cell = Random.element(candidates);
				} else {
					delay = BLINK_DELAY;
					return;
				}
			}

			ScrollOfTeleportation.appear( this, cell );

			delay = BLINK_DELAY;
		}

		@Override
		protected boolean getCloser( int target ) {
			if (fieldOfView[target] && Dungeon.level.distance( pos, target ) > 2 && delay <= 0) {

				blink( target );
				spend( -1 / speed() );
				return true;

			} else {

				delay--;
				return super.getCloser( target );

			}
		}
	}

	public static class OrangeGuardian extends RedGuardian implements Callback {
		{
			spriteClass = OrangeGuardianSprite.class;
			baseSpeed = 1f;
			resistances.put(Element.FIRE, 0.3f);
			lootAmt = 2;//Rare variant
		}

		@Override
		public void call() {
			next();
		}
	}

	public static class GreenGuardianSprite extends StatueSprite {

		public GreenGuardianSprite(){
			super();
			tint(0, 1, 0, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(0, 1, 0, 0.2f);
		}
	}

	public static class RedGuardianSprite extends StatueSprite {

		public RedGuardianSprite(){
			super();
			tint(1, 0, 0, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(1, 0, 0, 0.2f);
		}
	}

	public static class OrangeGuardianSprite extends StatueSprite {

		public OrangeGuardianSprite() {
			super();
			zap = attack.clone();
			tint(4, 1, 0, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(4, 1, 0, 0.2f);
		}
	}

	public static class BlueGuardianSprite extends StatueSprite {

		public BlueGuardianSprite(){
			super();
			tint(0, 0, 1, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(0, 0, 1, 0.2f);
		}
	}

	public static class PurpleGuardianSprite extends StatueSprite {
		public PurpleGuardianSprite(){
			super();
			tint(1, 0, 1, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(1, 0, 1, 0.2f);
		}
	}

	public static class YellowGuardianSprite extends StatueSprite {

		public YellowGuardianSprite(){
			super();
			tint(1, 1, 0, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(1, 1, 0, 0.2f);
		}
	}
}

