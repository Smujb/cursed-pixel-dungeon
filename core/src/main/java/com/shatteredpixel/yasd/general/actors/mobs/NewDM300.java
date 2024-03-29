package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.ToxicGas;
import com.shatteredpixel.yasd.general.actors.buffs.Barrier;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.actors.buffs.Paralysis;
import com.shatteredpixel.yasd.general.actors.buffs.Sleep;
import com.shatteredpixel.yasd.general.effects.BlobEmitter;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.EarthParticle;
import com.shatteredpixel.yasd.general.effects.particles.SparkParticle;
import com.shatteredpixel.yasd.general.items.quest.MetalShard;
import com.shatteredpixel.yasd.general.items.wands.WandOfBlastWave;
import com.shatteredpixel.yasd.general.levels.chapters.caves.NewCavesBossLevel;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.DM300Sprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;
import com.watabou.utils.RectF;

public class NewDM300 extends Boss {

	{
		spriteClass = DM300Sprite.class;

		healthFactor = 1.6f;
		damageFactor = 2/3f;
		drFactor = 2f;
		elementaldrFactor = 2f;
		perceptionFactor = 3f;

        properties.add(Property.BOSS);
		properties.add(Property.INORGANIC);
		properties.add(Property.LARGE);

		rematchLevel = "dm-300";
	}

	public int pylonsActivated = 0;
	public boolean supercharged = false;
	public boolean chargeAnnounced = false;

	private int turnsSinceLastAbility = -1;
	private int abilityCooldown = Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);

	private static final int MIN_COOLDOWN = 5;
	private static final int MAX_COOLDOWN = 9;

	private int lastAbility = 0;
	private static final int NONE = 0;
	private static final int GAS = 1;
	private static final int ROCKS = 2;

	private static final String PYLONS_ACTIVATED = "pylons_activated";
	private static final String SUPERCHARGED = "supercharged";
	private static final String CHARGE_ANNOUNCED = "charge_announced";

	private static final String TURNS_SINCE_LAST_ABILITY = "turns_since_last_ability";
	private static final String ABILITY_COOLDOWN = "ability_cooldown";

	private static final String LAST_ABILITY = "last_ability";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(PYLONS_ACTIVATED, pylonsActivated);
		bundle.put(SUPERCHARGED, supercharged);
		bundle.put(CHARGE_ANNOUNCED, chargeAnnounced);
		bundle.put(TURNS_SINCE_LAST_ABILITY, turnsSinceLastAbility);
		bundle.put(ABILITY_COOLDOWN, abilityCooldown);
		bundle.put(LAST_ABILITY, lastAbility);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		pylonsActivated = bundle.getInt(PYLONS_ACTIVATED);
		supercharged = bundle.getBoolean(SUPERCHARGED);
		chargeAnnounced = bundle.getBoolean(CHARGE_ANNOUNCED);
		turnsSinceLastAbility = bundle.getInt(TURNS_SINCE_LAST_ABILITY);
		abilityCooldown = bundle.getInt(ABILITY_COOLDOWN);
		lastAbility = bundle.getInt(LAST_ABILITY);

		if (turnsSinceLastAbility != -1){
			BossHealthBar.assignBoss(this);
			if (!supercharged && pylonsActivated == 2) BossHealthBar.bleed(true);
		}
	}

	@Override
	protected boolean act() {
		GameScene.add(Blob.seed(pos, 0, FallingRocks.class));
		GameScene.add(Blob.seed(pos, 0, ToxicGas.class));

		//ability logic only triggers if DM is not supercharged
		if (!supercharged){
			if (turnsSinceLastAbility >= 0) turnsSinceLastAbility++;

			//in case DM-300 hasn't been able to act yet
			if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
				fieldOfView = new boolean[Dungeon.level.length()];
				Dungeon.level.updateFieldOfView( this, fieldOfView );
			}

			//determine if DM can reach its enemy
			boolean canReach;
			if (enemy == null){
				if (Dungeon.level.adjacent(pos, Dungeon.hero.pos)){
					canReach = true;
				} else {
					canReach = (Dungeon.findStep(this, Dungeon.hero.pos, Dungeon.level.openSpace(), fieldOfView, true) != -1);
				}
			} else {
				if (Dungeon.level.adjacent(pos, enemy.pos)){
					canReach = true;
				} else {
					canReach = (Dungeon.findStep(this, enemy.pos, Dungeon.level.openSpace(), fieldOfView, true) != -1);
				}
			}

			if (state != HUNTING){
				if (Dungeon.hero.invisible <= 0 && canReach){
					beckon(Dungeon.hero.pos);
				}
			} else {

				if (enemy == null) enemy = Dungeon.hero;

				if (!canReach){

					if (fieldOfView[enemy.pos] && turnsSinceLastAbility >= MIN_COOLDOWN){

						lastAbility = GAS;
						turnsSinceLastAbility = 0;
						spend(TICK);

						GLog.warning(Messages.get(this, "vent"));
						if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
							sprite.zap(enemy.pos);
							return false;
						} else {
							ventGas(enemy);
							Sample.INSTANCE.play(Assets.Sounds.GAS);
							return true;
						}

					}

				} else {
					if (turnsSinceLastAbility > abilityCooldown) {

						if (lastAbility == NONE) {
							//50/50 either ability
							lastAbility = Random.Int(2) == 0 ? GAS : ROCKS;
						} else if (lastAbility == GAS) {
							//more likely to use rocks
							lastAbility = Random.Int(4) == 0 ? GAS : ROCKS;
						} else {
							//more likely to use gas
							lastAbility = Random.Int(4) != 0 ? GAS : ROCKS;
						}

						//doesn't spend a turn if enemy is at a distance
						if (Dungeon.level.adjacent(pos, enemy.pos)){
							spend(TICK);
						}

						turnsSinceLastAbility = 0;
						abilityCooldown = Random.NormalIntRange(MIN_COOLDOWN, MAX_COOLDOWN);

						if (lastAbility == GAS) {
							GLog.warning(Messages.get(this, "vent"));
							if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
								sprite.zap(enemy.pos);
								return false;
							} else {
								ventGas(enemy);
								Sample.INSTANCE.play(Assets.Sounds.GAS);
								return true;
							}
						} else {
							GLog.warning(Messages.get(this, "rocks"));
							if (sprite != null && (sprite.visible || enemy.sprite.visible)) {
								((DM300Sprite)sprite).slam(enemy.pos);
								return false;
							} else {
								dropRocks(enemy);
								Sample.INSTANCE.play(Assets.Sounds.ROCKS);
								return true;
							}
						}
					}
				}
			}
		} else {

			if (!chargeAnnounced){
				yell(Messages.get(this, "supercharged"));
				chargeAnnounced = true;
			}

			if (state == WANDERING && Dungeon.hero.invisible <= 0){
				beckon(Dungeon.hero.pos);
				state = HUNTING;
				enemy = Dungeon.hero;
				spend(TICK);
			}

		}

		return super.act();
	}

	private boolean invulnWarned = false;

	@Override
	protected Char chooseEnemy() {
		Char enemy = super.chooseEnemy();
		if (supercharged && enemy == null){
			enemy = Dungeon.hero;
		}
		return enemy;
	}

	@Override
	public void move(int step) {
		super.move(step);

		Camera.main.shake( supercharged ? 3 : 1, 0.25f );

		if (Dungeon.level.trap(step) != null && state == HUNTING) {

			//don't gain energy from cells that are energized
			if (NewCavesBossLevel.PylonEnergy.volumeAt(pos, NewCavesBossLevel.PylonEnergy.class) > 0){
				return;
			}

			if (Dungeon.level.heroFOV[step]) {
				if (buff(Barrier.class) == null) {
					GLog.warning(Messages.get(this, "shield"));
				}
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);
				sprite.emitter().start(SparkParticle.STATIC, 0.05f, 20);
			}

			Buff.affect(this, Barrier.class).setShield( 30 + (HT - HP)/10);

		}
	}

	@Override
	public float speed() {
		return super.speed() * (supercharged ? 2 : 1);
	}

	@Override
	public void notice() {
		super.notice();
		if (!BossHealthBar.isAssigned()) {
			BossHealthBar.assignBoss(this);
			turnsSinceLastAbility = 0;
			yell(Messages.get(this, "notice"));
		}
	}

	public void onZapComplete(){
		ventGas(enemy);
		next();
	}

	public void ventGas( Char target ){
		Dungeon.hero.interrupt();

		int gasVented = 0;

		Ballistica trajectory = new Ballistica(pos, target.pos, Ballistica.STOP_TARGET);

		for (int i : trajectory.subPath(0, trajectory.dist)){
			GameScene.add(Blob.seed(i, 20, ToxicGas.class));
			gasVented += 20;
		}

		GameScene.add(Blob.seed(trajectory.collisionPos, 100, ToxicGas.class));

		if (gasVented < 250){
			int toVentAround = (int)Math.ceil((250 - gasVented)/8f);
			for (int i : PathFinder.NEIGHBOURS8){
				GameScene.add(Blob.seed(pos+i, toVentAround, ToxicGas.class));
			}

		}

		Sample.INSTANCE.play(Assets.Sounds.GAS);
	}

	public void onSlamComplete(){
		dropRocks(enemy);
		next();
	}

	public void dropRocks( Char target ) {

		Dungeon.hero.interrupt();
		final int rockCenter;

		if (Dungeon.level.adjacent(pos, target.pos)){
			int oppositeAdjacent = target.pos + (target.pos - pos);
			Ballistica trajectory = new Ballistica(target.pos, oppositeAdjacent, Ballistica.MAGIC_BOLT);
			WandOfBlastWave.throwChar(target, trajectory, 2, 0);
			if (target == Dungeon.hero){
				Dungeon.hero.interrupt();
			}
			rockCenter = trajectory.path.get(Math.min(trajectory.dist, 2));
		} else {
			rockCenter = target.pos;
		}

		//we handle this through an actor as it gives us fine-grainted control over when the blog acts vs. when the hero acts
		//FIXME this is really messy to just get some fine-grained control. would be nice to build this into blob functionality, or just not use blobs for this at all
		Actor a = new Actor() {

			{
				actPriority = HERO_PRIO+1;
			}

			@Override
			protected boolean act() {

				//pick an adjacent cell to the hero as a safe cell. This cell is less likely to be in a wall or containing hazards
				int safeCell;
				do {
					safeCell = rockCenter + PathFinder.NEIGHBOURS8[Random.Int(8)];
				} while (safeCell == pos
						|| (Dungeon.level.solid(safeCell) && Random.Int(2) == 0)
						|| (Blob.volumeAt(safeCell, NewCavesBossLevel.PylonEnergy.class) > 0 && Random.Int(2) == 0));

				int start = rockCenter - Dungeon.level.width() * 3 - 3;
				int pos;
				for (int y = 0; y < 7; y++) {
					pos = start + Dungeon.level.width() * y;
					for (int x = 0; x < 7; x++) {
						if (!Dungeon.level.insideMap(pos)) {
							pos++;
							continue;
						}
						//add rock cell to pos, if it is not solid, and isn't the safecell
						if (!Dungeon.level.solid(pos) && pos != safeCell && Random.Int(Dungeon.level.distance(rockCenter, pos)) == 0) {
							//don't want to overly punish players with slow move or attack speed
							GameScene.add(Blob.seed(pos, 1, FallingRocks.class));
						}
						pos++;
					}
				}
				Actor.remove(this);
				return true;
			}
		};
		Actor.addDelayed(a, Math.min(target.cooldown(), 3*TICK));

	}

	@Override
	public void damage(int dmg, DamageSrc src) {
		super.damage(dmg, src);
		if (isInvulnerable(src.getClass())){
			return;
		}

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !isImmune(src.getClass())) lock.addTime(dmg);

		int threshold = HT/3 * (2- pylonsActivated);

		if (HP < threshold){
			HP = threshold;
			supercharge();
		}

	}

	@Override
	public boolean isInvulnerable(Class effect) {
		if (supercharged && !invulnWarned){
			invulnWarned = true;
			GLog.warning(Messages.get(this, "charging_hint"));
		}
		return supercharged;
	}

	public void supercharge(){
		supercharged = true;
		((NewCavesBossLevel)Dungeon.level).activatePylon();
		pylonsActivated++;

		spend(3f);
		yell(Messages.get(this, "charging"));
		sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "invulnerable"));
		((DM300Sprite)sprite).charge();
		chargeAnnounced = false;
	}

	public boolean isSupercharged(){
		return supercharged;
	}

	public void loseSupercharge(){
		supercharged = false;
		sprite.resetColor();

		if (pylonsActivated < 2){
			yell(Messages.get(this, "charge_lost"));
		} else {
			yell(Messages.get(this, "pylons_destroyed"));
			BossHealthBar.bleed(true);
		}
	}

	@Override
	public boolean isAlive() {
		return HP > 0 || pylonsActivated < 2;
	}

	@Override
	public void die( DamageSrc cause ) {

		super.die( cause );

		GameScene.bossSlain();
		Dungeon.level.unseal();

		//60% chance of 2 shards, 30% chance of 3, 10% chance for 4. Average of 2.5
		int shards = Random.chances(new float[]{0, 0, 6, 3, 1});
		for (int i = 0; i < shards; i++){
			int ofs;
			do {
				ofs = PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Dungeon.level.passable(pos + ofs));
			Dungeon.level.drop( new MetalShard(), pos + ofs ).sprite.drop( pos );
		}

		Badges.validateBossSlain();

		yell( Messages.get(this, "defeated") );
	}

	@Override
	protected boolean getCloser(int target) {
		if (super.getCloser(target)){
			return true;
		} else {

			if (rooted || target == pos) {
				return false;
			}

			int bestpos = pos;
			for (int i : PathFinder.NEIGHBOURS8){
				if (Dungeon.level.openSpace(pos+i) && Actor.findChar(pos+i) == null &&
						Dungeon.level.distance(bestpos, target) > Dungeon.level.distance(pos+i, target)){
					bestpos = pos+i;
				}
			}
			if (bestpos != pos){
				move( bestpos );
				return true;
			}

			if (!supercharged || state != HUNTING || Dungeon.level.adjacent(pos, target)){
				return false;
			}

			for (int i : PathFinder.NEIGHBOURS8){
				if (Actor.findChar(pos+i) == null &&
						Dungeon.level.trueDistance(bestpos, target) > Dungeon.level.trueDistance(pos+i, target)){
					bestpos = pos+i;
				}
			}
			if (bestpos != pos){
				Sample.INSTANCE.play( Assets.Sounds.ROCKS );


				Rect gate = NewCavesBossLevel.gate;
				for (int i : PathFinder.NEIGHBOURS9){
					if (Dungeon.level.getTerrain(pos+i) == Terrain.WALL || Dungeon.level.getTerrain(pos+i) == Terrain.WALL_DECO){
						Point p = Dungeon.level.cellToPoint(pos+i);
						if (p.y < gate.bottom && p.x > gate.left-2 && p.x < gate.right+2){
							continue; //don't break the gate or walls around the gate
						}
						Dungeon.level.set(pos+i, Terrain.EMPTY_DECO);
						GameScene.updateMap(pos+i);
					}
				}
				Dungeon.level.cleanWalls();
				Dungeon.observe();
				spend(3f);
				bestpos = pos;

				for (int i : PathFinder.NEIGHBOURS8){
					if (Actor.findChar(pos+i) == null && Dungeon.level.openSpace(pos+i) &&
							Dungeon.level.trueDistance(bestpos, target) > Dungeon.level.trueDistance(pos+i, target)){
						bestpos = pos+i;
					}
				}

				if (bestpos != pos) {
					move(bestpos);
				}
				Camera.main.shake( 5, 1f );

				return true;
			}

			return false;
		}
	}

	@Override
	public String description() {
		String desc = super.description();
		if (supercharged) {
			desc += "\n\n" + Messages.get(this, "desc_supercharged");
		}
		return desc;
	}

	{
		immunities.add(Sleep.class);

		/*resistances.add(Terror.class);
		resistances.add(Charm.class);
		resistances.add(Vertigo.class);
		resistances.add(Cripple.class);
		resistances.add(Chill.class);
		resistances.add(Frost.class);
		resistances.add(Roots.class);
		resistances.add(Slow.class);*/
		resistances.put(Element.CHAOS, 0.25f);
	}

	public static class FallingRocks extends Blob {

		{
			alwaysVisible = true;
		}

		@Override
		protected void evolve() {

			boolean rocksFell = false;

			int cell;
			for (int i = area.left; i < area.right; i++){
				for (int j = area.top; j < area.bottom; j++){
					cell = i + j* Dungeon.level.width();
					off[cell] = cur[cell] > 0 ? cur[cell] - 1 : 0;

					if (off[cell] > 0) {
						volume += off[cell];
					}

					if (cur[cell] > 0 && off[cell] == 0){

						CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );

						Char ch = Actor.findChar(cell);
						if (ch != null && !(ch instanceof NewDM300)){
							Buff.prolong( ch, Paralysis.class, 3 );
						}

						rocksFell = true;
					}
				}
			}

			if (rocksFell){
				Camera.main.shake( 3, 0.7f );
				Sample.INSTANCE.play(Assets.Sounds.ROCKS);
			}

		}

		@Override
		public void use(BlobEmitter emitter) {
			super.use(emitter);

			emitter.bound = new RectF(0, -0.2f, 1, 0.4f);
			emitter.pour(EarthParticle.FALLING, 0.1f);
		}

		@Override
		public String tileDesc() {
			return Messages.get(this, "desc");
		}
	}
}