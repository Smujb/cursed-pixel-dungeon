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

package com.shatteredpixel.yasd.general.plants;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.particles.LeafParticle;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.wands.WandOfRegrowth;
import com.shatteredpixel.yasd.general.levels.Level;
import com.shatteredpixel.yasd.general.levels.terrain.KindOfTerrain;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;

public abstract class Plant implements Bundlable {

	public String plantName = Messages.get(this, "name");
	
	public int image;
	public int pos;

	protected Class<? extends Plant.Seed> seedClass;

	public void trigger(){

		Char ch = Actor.findChar(pos);

		if (ch instanceof Hero){
			((Hero) ch).interrupt();
		}

		wither();
		activate( ch );
	}
	
	public abstract void activate( Char ch );
	
	public void wither() {
		Dungeon.level.uproot( pos );

		if (Dungeon.level.heroFOV[pos]) {
			CellEmitter.get( pos ).burst( LeafParticle.GENERAL, 6 );
		}

		float seedChance = 0f;
		for (Char c : Actor.chars()){
			if (c instanceof WandOfRegrowth.Lotus){
				WandOfRegrowth.Lotus l = (WandOfRegrowth.Lotus) c;
				if (l.inRange(pos)){
					seedChance = Math.max(seedChance, l.seedPreservation());
				}
			}
		}

		if (Random.Float() < seedChance){
			if (seedClass != null && seedClass != Rotberry.Seed.class) {
				Dungeon.level.drop(Reflection.newInstance(seedClass), pos).sprite.drop();
			}
		}
	}
	
	private static final String POS	= "pos";

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		pos = bundle.getInt( POS );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {
		bundle.put( POS, pos );
	}
	
	public String desc() {
		return Messages.get(this, "desc");
	}
	
	public static class Seed extends Item {

		public static final String AC_PLANT	= "PLANT";
		
		private static final float TIME_TO_PLANT = 1f;
		
		{
			stackable = true;
			defaultAction = AC_THROW;
		}
		
		protected Class<? extends Plant> plantClass;
		
		@Override
		public ArrayList<String> actions( Hero hero ) {
			ArrayList<String> actions = super.actions( hero );
			actions.add( AC_PLANT );
			return actions;
		}
		
		@Override
		protected void onThrow( int cell ) {
			if (Dungeon.level.getTerrain(cell) == Terrain.ALCHEMY
					|| Dungeon.level.pit(cell)
					|| Dungeon.level.trap(cell) != null
					|| Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
				super.onThrow( cell );
			} else {
				Dungeon.level.plant( this, cell );
				if (Dungeon.hero.subClass == HeroSubClass.WARDEN) {
					for (int i : PathFinder.NEIGHBOURS8) {
						KindOfTerrain c = Dungeon.level.getTerrain(cell + i);
						if ( c == Terrain.EMPTY || c == Terrain.EMPTY_DECO
								|| c == Terrain.EMBERS || c == Terrain.GRASS){
							Dungeon.level.set(cell + i, Terrain.FURROWED_GRASS);
							GameScene.updateMap(cell + i);
							CellEmitter.get( cell + i ).burst( LeafParticle.LEVEL_SPECIFIC, 4 );
						}
					}
				}
			}
		}
		
		@Override
		public void execute( Hero hero, String action ) {

			super.execute (hero, action );

			if (action.equals( AC_PLANT )) {
							
				hero.spend( TIME_TO_PLANT );
				hero.busy();
				((Seed)detach( hero.belongings.backpack )).onThrow( hero.pos );
				
				hero.sprite.operate( hero.pos );
				
			}
		}
		
		public Plant couch( int pos, Level level ) {
			if (level != null && level.heroFOV != null && level.heroFOV[pos]) {
				Sample.INSTANCE.play(Assets.Sounds.PLANT);
			}
			Plant plant = Reflection.newInstance(plantClass);
			plant.pos = pos;
			return plant;
		}
		
		@Override
		public boolean isUpgradable() {
			return false;
		}
		
		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public int price() {
			return 10 * quantity;
		}

		@Override
		public String desc() {
			return Messages.get(plantClass, "desc");
		}

		@Override
		public String info() {
			return Messages.get( Seed.class, "info", desc() );
		}
		
		public static class PlaceHolder extends Seed {
			
			{
				image = ItemSpriteSheet.SEED_HOLDER;
			}
			
			@Override
			public boolean isSimilar( Item item) {
				return item instanceof Plant.Seed;
			}
			
			@Override
			public String info() {
				return "";
			}
		}
	}
}
