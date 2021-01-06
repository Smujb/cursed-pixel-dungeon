package com.shatteredpixel.yasd.general.items.powers;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.unused.armor.RogueArmor;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfTeleportation;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class SmokeBomb extends Power {

	{
		image = ItemSpriteSheet.SMOKEBOMB;
		mp_cost = 4;
	}

	@Override
	protected void onUse(Hero hero) {
		GameScene.selectCell( teleporter );
	}

	private CellSelector.Listener teleporter = new CellSelector.Listener() {

		@Override
		public void onSelect( Integer target ) {
			if (target != null) {

				PathFinder.buildDistanceMap(curUser.pos, BArray.not(Dungeon.level.solid(),null), 8);

				if ( PathFinder.distance[target] == Integer.MAX_VALUE ||
						!Dungeon.level.heroFOV[target] ||
						Actor.findChar( target ) != null) {

					GLog.warning( Messages.get(SmokeBomb.class, "fov") );
					return;
				}

				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (Dungeon.level.heroFOV[mob.pos]) {
						Buff.prolong( mob, Blindness.class, 2 );
						if (mob.state == mob.HUNTING) mob.state = mob.WANDERING;
						mob.sprite.emitter().burst( Speck.factory( Speck.LIGHT ), 4 );
					}
				}

				ScrollOfTeleportation.appear( curUser, target );
				CellEmitter.get( target ).burst( Speck.factory( Speck.WOOL ), 10 );
				Sample.INSTANCE.play( Assets.Sounds.PUFF );
				Dungeon.level.pressCell( target );
				Dungeon.observe();
				GameScene.updateFog();
			}
		}

		@Override
		public String prompt() {
			return Messages.get(RogueArmor.class, "prompt");
		}
	};
}

