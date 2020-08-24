package com.shatteredpixel.yasd.general.levels;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.levels.tiled.TiledMapLevel;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GrindLevel extends TiledMapLevel {

	{
		viewDistance = 20;
	}



	public int spawn;
	public static String SPAWN = "spawn";

	@Override
	public float respawnTime() {
		return 10f/LuckyBadge.mobSpawnFactor;
	}

	@Override
	public void create(String key) {
		super.create(key);
		spawn = getEntrance().centerCell(this);
		set(getEntrancePos(), Terrain.PEDESTAL);
		clearExitEntrance();
	}

	@Override
	public int getEntrancePos() {
		return spawn;
	}

	@Override
	//For now. Reworked tiles are needed.
	public String tilesTex() {
		return Assets.Environment.TILES_HEAVEN;
	}

	@Override
	public String waterTex() {
		return Assets.Environment.WATER_HEAVEN;
	}

	@Override
	public String loadImg() {
		return Assets.Environment.WATER_HEAVEN;
	}

	@Override
	protected String mapName() {
		return "maps/grind-depth.tmx";
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(SPAWN, spawn);
	}

	@Override
	public void restoreFromBundle(@NotNull Bundle bundle) {
		super.restoreFromBundle(bundle);
		spawn = bundle.getInt(SPAWN);
	}

	@Override
	public Class<?>[] mobClasses() {
		return new Class[] {
				RedGuardian.class,
				BlueGuardian.class,
				GreenGuardian.class,
				YellowGuardian.class
		};
	}

	@Override
	public float[] mobChances() {
		return new float[] {
				3,
				1,
				2,
				3
		};
	}

	@Override
	public int nMobs() {
		return 10;
	}

	public static class Guardian extends Mob {

		{
			spriteClass = StatueSprite.class;

			EXP = 0;
			state = WANDERING;

			immunities.add(ScrollOfPsionicBlast.class);
		}

		@Override
		public String description() {
			return Messages.get(Guardian.class,"desc") + "\n\n" + super.description();
		}

		@Override
		public int defenseSkill(Char enemy) {
			return 0;
		}

		@Override
		public void die(DamageSrc cause) {
			if (cause.getElement() != Element.META) {
				for (int i = 0; i < 3 + 2 * LuckyBadge.mobLevelBoost; i++) {
					int ofs;
					do {
						ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
					} while (!Dungeon.level.passable(pos + ofs));
					Dungeon.level.drop(LuckyBadge.tryForBonusDrop(), pos + ofs).sprite.drop(pos);
					LuckyBadge.score++;
				}
				if (CPDSettings.validateGrindingHighScore(LuckyBadge.score)) {
					LuckyBadge.scoreBeaten = true;
				}
			}
			super.die(cause);
		}

		@Override
		protected boolean act() {
			if (alignment != Dungeon.hero.alignment) {
				beckon(Dungeon.hero.pos);
			}
			return super.act();
		}

		@Override
		protected void onCreate() {
			super.onCreate();
			level += LuckyBadge.mobLevelBoost;
			updateHT(true);
		}

		@Override
		public int drRoll(Element element) {
			return (int) (super.drRoll(element));
		}
	}

	public static class GreenGuardian extends Guardian {

		{
			spriteClass = GreenGuardianSprite.class;
			accuracyFactor = 2f;
			drFactor = 2f;
		}

		@Override
		public Element elementalType() {
			return Element.STONE;
		}
	}
	public static class RedGuardian extends Guardian {
		{
			spriteClass = RedGuardianSprite.class;
			baseSpeed = 2f;
			accuracyFactor = 2f;
			damageFactor = 0.75f;
			drFactor = 0.5f;
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
			drFactor = 2f;
			resistances.put(Element.SHADOW, 0.5f);
		}
	}

	public static class PurpleGuardian extends BlueGuardian {
		{
			spriteClass = PurpleGuardianSprite.class;
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

	public static class OrangeGuardian extends RedGuardian {
		{
			spriteClass = OrangeGuardianSprite.class;
			baseSpeed = 1f;
			resistances.put(Element.FIRE, 0.3f);

			range = 4;
		}

		@Override
		public Element elementalType() {
			return Element.DESTRUCTION;
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

