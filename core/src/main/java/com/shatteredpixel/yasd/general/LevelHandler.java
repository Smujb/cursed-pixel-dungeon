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

package com.shatteredpixel.yasd.general;

import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.powers.LuckyBadge;
import com.shatteredpixel.yasd.general.levels.DeadEndLevel;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.features.Chasm;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.TextScene;
import com.shatteredpixel.yasd.general.ui.GameLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.FileUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class LevelHandler {

	private static int pos;
	private static boolean fallIntoPit;
	public static int depth;
	public static String key;
	public static Callback callback;

	private static Exception error = null;

	public static String filename(String key, int slot) {
		return GamesInProgress.slotFolder(slot) + "/" + key + ".dat";
	}

	static Level getLevel(String key) {
		return getLevel(key, GamesInProgress.curSlot);
	}


	@Nullable
	static Level getLevel(String key, int save) {

		try {
			Bundle bundle = FileUtils.bundleFromFile(filename(key, save));
			return (Level) bundle.get(Dungeon.LEVEL);
		} catch (IOException e) {
			LevelHandler.error = e;
			return null;
		}
	}

	public enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL, RESET, MOVE, INIT
	}
	private static Mode mode;
	

	private static String loadAsset(Level level) {
		if (level == null) {
			return Assets.Interfaces.SHADOW;
		} else {
			return level.loadImg();
		}
	}

	@Contract(" -> new")
	private static Thread getThread() {
		return new Thread() {
			@Override
			public void run() {

				try {

					if (Dungeon.hero != null){
						Dungeon.hero.spendToWhole();
					}
					Actor.fixTime();

					if (mode == Mode.CONTINUE) {
						restore();
					} else {
						switchDepth(depth, mode, key );
					}

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	@Contract(pure = true)
	public static float getSpeed() {
		float scrollSpeed;
		switch (mode) {
			default:
				scrollSpeed = 0;
				break;
			case CONTINUE:
			case DESCEND:
				scrollSpeed = 5;
				break;
			case FALL:
				scrollSpeed = 50;
				break;
			case ASCEND:
				scrollSpeed = -5;
				break;
			case RETURN:
				scrollSpeed = depth > Dungeon.depth ? 15 : -15;
				break;
		}
		return scrollSpeed;
	}

	@Contract(pure = true)
	public static Mode mode() {
		return mode;
	}

	public static void descend() {
		move(Dungeon.keyForDepth(Dungeon.depth +1), Messages.get(Mode.class, Mode.DESCEND.name()), Mode.DESCEND, Dungeon.depth + 1, -1);
	}

	public static void ascend() {
		move(Dungeon.keyForDepth(Dungeon.depth -1), Messages.get(Mode.class, Mode.ASCEND.name()), Mode.ASCEND, Dungeon.depth - 1, -1);
	}

	public static void fall(boolean fallIntoPit, boolean bossLevel) {
		LevelHandler.fallIntoPit = fallIntoPit;
		if (bossLevel) {//If falling from a boss level, the hero will fall back onto the same floor and it will reset.
			Dungeon.level.reset();
			move(Dungeon.keyForDepth(), Messages.get(Mode.class, Mode.FALL.name()), Mode.FALL, Dungeon.depth, -1);
		} else {
			move(Dungeon.keyForDepth(Dungeon.depth +1), Messages.get(Mode.class, Mode.FALL.name()), Mode.FALL, Dungeon.depth + 1, -1);
		}
	}

	public static void reset() {
		move(Dungeon.keyForDepth(), Messages.get(Mode.class, Mode.RESET.name()), Mode.RESET, Dungeon.depth, -1);
	}

	public static void resurrect() {
		move(Dungeon.keyForDepth(), Messages.get(Mode.class, Mode.RESURRECT.name()), Mode.RESURRECT, Dungeon.depth, -1);
	}

	public static void returnTo(int depth, int pos) {
		returnTo(Dungeon.keyForDepth(depth), depth, pos);
	}

	public static void returnTo(String key, int depth, int pos) {
		move(key, Messages.get(Mode.class, Mode.RETURN.name()), Mode.RETURN, depth, pos);
	}

	public static void portSurface() {
		StoryChapter.Trial.setCurTrial(StoryChapter.Trial.NONE);
		move(Dungeon.keyForDepth(0), Messages.get(Mode.class, Mode.RETURN.name()), Mode.ASCEND, 0, -1);
	}

	public static void waterTrial() {
		StoryChapter.Trial.setCurTrial(StoryChapter.Trial.WATER);
		move(Dungeon.keyForDepth(1), StoryChapter.Trial.WATER.displayName(), Mode.DESCEND, 1, -1);
	}

	public static void earthTrial() {
		StoryChapter.Trial.setCurTrial(StoryChapter.Trial.EARTH);
		move(Dungeon.keyForDepth(1), StoryChapter.Trial.EARTH.displayName(), Mode.DESCEND, 1, -1);
	}

	public static void fireTrial() {
		StoryChapter.Trial.setCurTrial(StoryChapter.Trial.FIRE);
		move(Dungeon.keyForDepth(1), StoryChapter.Trial.FIRE.displayName(), Mode.DESCEND, 1, -1);
	}

	public static void airTrial() {
		StoryChapter.Trial.setCurTrial(StoryChapter.Trial.AIR);
		move(Dungeon.keyForDepth(1), StoryChapter.Trial.AIR.displayName(), Mode.DESCEND, 1, -1);
	}

	public static void doRestore() {
		mode = Mode.CONTINUE;
		TextScene.init(Messages.get(Mode.class, Mode.CONTINUE.name()), Messages.get(LevelHandler.class, "continue"), loadAsset(Dungeon.newLevel( Dungeon.keyForDepth(), false)), getSpeed(), 0.67f, new Callback() {
			@Override
			public void call() {
				CPDGame.switchScene(GameScene.class);
			}
		}, getThread(), CPDSettings.fasterLoading(), false);
	}

	public static void doInit() {
		mode = Mode.DESCEND;
		depth = 0;
		pos = -1;
		key = Dungeon.keyForDepth();
		TextScene.init(Messages.get(Mode.class, Mode.DESCEND.name()), Messages.get(LevelHandler.class, "continue"), loadAsset(Dungeon.newLevel( Dungeon.keyForDepth(), false)), getSpeed(), 0.67f, new Callback() {
			@Override
			public void call() {
				CPDGame.switchScene(GameScene.class);
			}
		}, new Thread() {
			@Override
			public void run() {
				try {
					initGame();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}, CPDSettings.fasterLoading(), false);
	}

	public static void move(String key, String msg, Mode mode, int depth, int pos) {
		LevelHandler.depth = depth;
		LevelHandler.mode = mode;
		LevelHandler.key = key;
		LevelHandler.pos = pos;
		TextScene.init(msg, Messages.get(LevelHandler.class, "continue"), loadAsset(Dungeon.newLevel( Dungeon.keyForDepth(depth), false)), getSpeed(), 0.67f, new Callback() {
			@Override
			public void call() {
				CPDGame.switchScene(GameScene.class);
			}
		}, getThread(), CPDSettings.fasterLoading(), false);
	}

	public static void resetMode() {
		mode = Mode.MOVE;
	}

	private static void initGame() throws IOException {
		Mob.clearHeldAllies();
		Dungeon.level = null;
		Dungeon.hero = null;
		Dungeon.init();
		GameLog.wipe();
		LevelHandler.key = Dungeon.keyForDepth();
		switchDepth(0, Mode.DESCEND, Dungeon.keyForDepth());
	}

	private static void switchDepth(int depth, final Mode mode, String key) throws IOException {
		if (Dungeon.hero != null && Dungeon.level != null) {
			Mob.holdMobs( Dungeon.level );
			Dungeon.saveAll();
		}
		Dungeon.depth = depth;
		if (mode.equals(Mode.RESURRECT) & Dungeon.hero != null & Dungeon.level != null) {
			if (Dungeon.level.locked) {
				Dungeon.hero.resurrect( Dungeon.key );
			} else {
				Dungeon.hero.resurrect( null );
				Dungeon.resetLevel();
				return;
			}
		}


		Level level = null;
		Dungeon.key = key;
		if (mode != Mode.RESET) {
			level = getLevel(key);
		}
		Dungeon.level = null;
		Actor.clear();
		if (level == null) {

			Buff.affect(Dungeon.hero, LuckyBadge.LuckBuff.class, LuckyBadge.LuckBuff.DURATION);

			if (depth > Statistics.deepestFloor) {
				Statistics.deepestFloor = depth;

				Statistics.completedWithNoKilling = Statistics.qualifiedForNoKilling;
			}

			level = Dungeon.newLevel( LevelHandler.key, true );

			Statistics.qualifiedForNoKilling = !Dungeon.bossLevel();
			if (level instanceof DeadEndLevel) {
				Statistics.deepestFloor--;
			}

		}
		int pos = LevelHandler.pos;
		if (pos < 0) {
			if (mode == Mode.ASCEND) {
				pos = level.getExitPos();
			} else if (mode == Mode.FALL) {
				pos = level.fallCell(fallIntoPit);
			} else {
				pos = level.getEntrancePos();
			}
		}
		if (mode == Mode.FALL) {
			Buff.affect( Dungeon.hero, Chasm.Falling.class );
		}

		Dungeon.switchLevel(level, pos);
		if (callback != null) {
			callback.call();
		}
	}

	private static void restore() throws IOException {

		Mob.clearHeldAllies();

		GameLog.wipe();
		Dungeon.loadGame( GamesInProgress.curSlot );
		Level level = getLevel( Dungeon.key, GamesInProgress.curSlot );
		Dungeon.level = null;
		Actor.clear();
		if (level != null) {
			Dungeon.switchLevel( level, Dungeon.hero.pos );
		} else {
			if (Dungeon.key == null) {
				Dungeon.key = Dungeon.keyForDepth();
			}
			level = Dungeon.newLevel(Dungeon.key, true);
			Dungeon.switchLevel( level, level.getEntrancePos() );
			if (error != null) {
				CPDGame.reportException(error);
			}
		}
	}
}
