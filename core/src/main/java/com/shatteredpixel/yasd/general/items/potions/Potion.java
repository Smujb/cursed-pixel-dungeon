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

package com.shatteredpixel.yasd.general.items.potions;

	import com.shatteredpixel.yasd.general.Assets;
	import com.shatteredpixel.yasd.general.Badges;
	import com.shatteredpixel.yasd.general.Challenges;
	import com.shatteredpixel.yasd.general.Dungeon;
	import com.shatteredpixel.yasd.general.Statistics;
	import com.shatteredpixel.yasd.general.actors.Actor;
	import com.shatteredpixel.yasd.general.actors.Char;
	import com.shatteredpixel.yasd.general.actors.blobs.Fire;
	import com.shatteredpixel.yasd.general.actors.buffs.Buff;
	import com.shatteredpixel.yasd.general.actors.buffs.Burning;
	import com.shatteredpixel.yasd.general.actors.buffs.Ooze;
	import com.shatteredpixel.yasd.general.actors.hero.Hero;
	import com.shatteredpixel.yasd.general.effects.Splash;
	import com.shatteredpixel.yasd.general.items.Generator;
	import com.shatteredpixel.yasd.general.items.Item;
	import com.shatteredpixel.yasd.general.items.ItemStatusHandler;
	import com.shatteredpixel.yasd.general.items.Recipe;
	import com.shatteredpixel.yasd.general.items.bags.Bag;
	import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfHoneyedHealing;
	import com.shatteredpixel.yasd.general.items.potions.exotic.ExoticPotion;
	import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfCleansing;
	import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfCorrosiveGas;
	import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfShroudingFog;
	import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfSnapFreeze;
	import com.shatteredpixel.yasd.general.items.potions.exotic.PotionOfStormClouds;
	import com.shatteredpixel.yasd.general.journal.Catalog;
	import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
	import com.shatteredpixel.yasd.general.messages.Messages;
	import com.shatteredpixel.yasd.general.plants.Blindweed;
	import com.shatteredpixel.yasd.general.plants.Dreamfoil;
	import com.shatteredpixel.yasd.general.plants.Earthroot;
	import com.shatteredpixel.yasd.general.plants.Fadeleaf;
	import com.shatteredpixel.yasd.general.plants.Firebloom;
	import com.shatteredpixel.yasd.general.plants.Icecap;
	import com.shatteredpixel.yasd.general.plants.Plant;
	import com.shatteredpixel.yasd.general.plants.Rotberry;
	import com.shatteredpixel.yasd.general.plants.Sorrowmoss;
	import com.shatteredpixel.yasd.general.plants.Starflower;
	import com.shatteredpixel.yasd.general.plants.Stormvine;
	import com.shatteredpixel.yasd.general.plants.Sungrass;
	import com.shatteredpixel.yasd.general.plants.Swiftthistle;
	import com.shatteredpixel.yasd.general.scenes.GameScene;
	import com.shatteredpixel.yasd.general.sprites.ItemSprite;
	import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
	import com.shatteredpixel.yasd.general.utils.GLog;
	import com.shatteredpixel.yasd.general.windows.WndBag;
	import com.shatteredpixel.yasd.general.windows.WndOptions;
	import com.watabou.noosa.audio.Sample;
	import com.watabou.utils.Bundle;
	import com.watabou.utils.Random;
	import com.watabou.utils.Reflection;

	import org.jetbrains.annotations.NotNull;

	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.HashSet;

public class Potion extends Item {

	public static final String AC_DRINK = "DRINK";

	private static final float TIME_TO_DRINK = 1f;

	private static final Class<?>[] potions = {
			PotionOfRestoration.class,
			PotionOfExperience.class,
			PotionOfToxicGas.class,
			PotionOfLiquidFlame.class,
			PotionOfMana.class,
			PotionOfParalyticGas.class,
			PotionOfLevitation.class,
			PotionOfMindVision.class,
			PotionOfPurity.class,
			PotionOfInvisibility.class,
			PotionOfHaste.class,
			PotionOfFrost.class
	};

	private static final HashMap<String, Integer> colors = new HashMap<String, Integer>() {
		{
			put("crimson",ItemSpriteSheet.POTION_CRIMSON);
			put("amber",ItemSpriteSheet.POTION_AMBER);
			put("golden",ItemSpriteSheet.POTION_GOLDEN);
			put("jade",ItemSpriteSheet.POTION_JADE);
			put("turquoise",ItemSpriteSheet.POTION_TURQUOISE);
			put("azure",ItemSpriteSheet.POTION_AZURE);
			put("indigo",ItemSpriteSheet.POTION_INDIGO);
			put("magenta",ItemSpriteSheet.POTION_MAGENTA);
			put("bistre",ItemSpriteSheet.POTION_BISTRE);
			put("charcoal",ItemSpriteSheet.POTION_CHARCOAL);
			put("silver",ItemSpriteSheet.POTION_SILVER);
			put("ivory",ItemSpriteSheet.POTION_IVORY);
		}
	};
	
	private static final HashSet<Class<?extends Potion>> mustThrowPots = new HashSet<>();
	static{
		mustThrowPots.add(PotionOfToxicGas.class);
		mustThrowPots.add(PotionOfLiquidFlame.class);
		mustThrowPots.add(PotionOfParalyticGas.class);
		mustThrowPots.add(PotionOfFrost.class);
		
		//exotic
		mustThrowPots.add(PotionOfCorrosiveGas.class);
		mustThrowPots.add(PotionOfSnapFreeze.class);
		mustThrowPots.add(PotionOfShroudingFog.class);
		mustThrowPots.add(PotionOfStormClouds.class);
		
		//also all brews, hardcoded
	}
	
	private static final HashSet<Class<?extends Potion>> canThrowPots = new HashSet<>();
	static{
		canThrowPots.add(AlchemicalCatalyst.class);
		
		canThrowPots.add(PotionOfPurity.class);
		canThrowPots.add(PotionOfLevitation.class);
		
		//exotic
		canThrowPots.add(PotionOfCleansing.class);
		
		//elixirs
		canThrowPots.add(ElixirOfHoneyedHealing.class);
	}
	
	protected static ItemStatusHandler<Potion> handler;
	
	protected String color;
	
	{
		stackable = true;
		defaultAction = AC_DRINK;
	}
	
	@SuppressWarnings("unchecked")
	public static void initColors() {
		handler = new ItemStatusHandler<>( (Class<? extends Potion>[])potions, colors );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		ArrayList<Class<?extends Item>> classes = new ArrayList<>();
		for (Item i : items){
			if (i instanceof ExoticPotion){
				if (!classes.contains(ExoticPotion.exoToReg.get(i.getClass()))){
					classes.add(ExoticPotion.exoToReg.get(i.getClass()));
				}
			} else if (i instanceof Potion){
				if (!classes.contains(i.getClass())){
					classes.add(i.getClass());
				}
			}
		}
		handler.saveClassesSelectively( bundle, classes );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<>( (Class<? extends Potion>[])potions, colors, bundle );
	}
	
	public Potion() {
		super();
		reset();
	}
	
	//anonymous potions are always IDed, do not affect ID status,
	//and their sprite is replaced by a placeholder if they are not known,
	//useful for items that appear in UIs, or which are only spawned for their effects
	protected boolean anonymous = false;
	public Potion anonymize(){
		if (!isKnown()) image = ItemSpriteSheet.POTION_HOLDER;
		anonymous = true;
		return this;
	}

	@Override
	public void reset(){
		super.reset();
		if (handler != null && handler.contains(this)) {
			image = handler.image(this);
			color = handler.label(this);
		}
		setAction();
	}
	
	@Override
	public boolean collect(Bag container, @NotNull Char ch) {
		if (super.collect( container, ch)){
			setAction();
			return true;
		} else {
			return false;
		}
	}
	
	public void setAction(){
		if (isKnown() && mustThrowPots.contains(this.getClass())) {
			defaultAction = AC_THROW;
		} else if (isKnown() &&canThrowPots.contains(this.getClass())){
			defaultAction = AC_INFO;
		} else {
			defaultAction = AC_DRINK;
		}
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_DRINK );
		return actions;
	}
	
	@Override
	public void execute( final Hero hero, String action ) {

		super.execute( hero, action );
		
		if (action.equals( AC_DRINK )) {
			if (isKnown() && mustThrowPots.contains(getClass())) {

				GameScene.show(
						new WndOptions(Messages.get(Potion.class, "harmful"),
								Messages.get(Potion.class, "sure_drink"),
								Messages.get(Potion.class, "yes"), Messages.get(Potion.class, "no")) {
							@Override
							protected void onSelect(int index) {
								if (index == 0) {
									drink(hero);
								}
							}
						}
				);

			} else {
				drink(hero);
			}
		}
	}
	
	@Override
	public void doThrow( final Hero hero ) {

		if (isKnown()
				&& !mustThrowPots.contains(this.getClass())
				&& !canThrowPots.contains(this.getClass())) {
		
			GameScene.show(
				new WndOptions( Messages.get(Potion.class, "beneficial"),
						Messages.get(Potion.class, "sure_throw"),
						Messages.get(Potion.class, "yes"), Messages.get(Potion.class, "no") ) {
					@Override
					protected void onSelect(int index) {
						if (index == 0) {
							Potion.super.doThrow( hero );
						}
					}
				}
			);
			
		} else {
			super.doThrow( hero );
		}
	}
	
	protected void drink( Hero hero ) {
		
		Potion potion = (Potion) detach( hero.belongings.backpack );
		if (potion != null) {

			hero.spend(TIME_TO_DRINK);
			hero.busy();
			potion.apply(hero);

			Sample.INSTANCE.play(Assets.Sounds.DRINK);

			hero.sprite.operate(hero.pos);
		} else {
			GLog.debug("Could not drink potion.");
		}
	}
	
	@Override
	protected void onThrow( int cell ) {
		if (Dungeon.level.getTerrain(cell) == Terrain.WELL || Dungeon.level.pit()[cell]) {
			
			super.onThrow( cell );
			
		} else  {

			Dungeon.level.pressCell( cell );
			shatter( cell );
			
		}
	}
	
	public void apply( Hero hero ) {
		shatter( hero.pos );
	}
	
	public void shatter( int cell ) {
		if (Dungeon.level.heroFOV[cell]) {
			GLog.info( Messages.get(Potion.class, "shatter") );
			Sample.INSTANCE.play( Assets.Sounds.SHATTER );
			splash( cell );
		}
	}

	@Override
	public void cast(final Char user, int dst ) {
			super.cast(user, dst);
	}
	
	public boolean isKnown() {
		return anonymous || (handler != null && handler.isKnown( this ));
	}
	
	public void setKnown() {
		if (!anonymous) {
			if (!isKnown()) {
				handler.know(this);
				updateQuickslot();
				Potion p = Dungeon.hero.belongings.getItem(getClass());
				if (p != null)  p.setAction();
				if (ExoticPotion.regToExo.get(getClass()) != null) {
					p = Dungeon.hero.belongings.getItem(ExoticPotion.regToExo.get(getClass()));
					if (p != null) p.setAction();
				}
			}
			
			if (Dungeon.hero.isAlive()) {
				Catalog.setSeen(getClass());
			}
		}
	}
	
	@Override
	public Item identify() {

		setKnown();
		return super.identify();
	}
	
	@Override
	public String name() {
		return isKnown() ? super.name() : Messages.get(this, color);
	}
	
	@Override
	public String info() {
		return isKnown() ? desc() : Messages.get(this, "unknown_desc");
	}
	
	@Override
	public boolean isIdentified() {
		return isKnown();
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	public static HashSet<Class<? extends Potion>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Potion>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == potions.length;
	}
	
	protected int splashColor(){
		return anonymous ? 0x00AAFF : ItemSprite.pick( image, 5, 9 );
	}
	
	protected void splash( int cell ) {

		Fire fire = (Fire)Dungeon.level.blobs.get( Fire.class );
		if (fire != null)
			fire.clear( cell );

		final int color = splashColor();

		Char ch = Actor.findChar(cell);
		if (ch != null) {
			Buff.detach(ch, Burning.class);
			Buff.detach(ch, Ooze.class);
			Splash.at( ch.sprite.center(), color, 5 );
		} else {
			Splash.at( cell, color, 5 );
		}
	}
	
	@Override
	public int price() {
		return 60 * quantity;
	}
	
	public static class PlaceHolder extends Potion {
		
		{
			image = ItemSpriteSheet.POTION_HOLDER;
		}
		
		@Override
		public boolean isSimilar( Item item) {
			return ExoticPotion.regToExo.containsKey(item.getClass())
					|| ExoticPotion.regToExo.containsValue(item.getClass());
		}
		
		@Override
		public String info() {
			return "";
		}
	}
	
	public static class SeedToPotion extends Recipe {
		
		public static HashMap<Class<?extends Plant.Seed>, Class<?extends Potion>> types = new HashMap<>();
		static {
			types.put(Blindweed.Seed.class,     PotionOfInvisibility.class);
			types.put(Dreamfoil.Seed.class,     PotionOfPurity.class);
			types.put(Earthroot.Seed.class,     PotionOfParalyticGas.class);
			types.put(Fadeleaf.Seed.class,      PotionOfMindVision.class);
			types.put(Firebloom.Seed.class,     PotionOfLiquidFlame.class);
			types.put(Icecap.Seed.class,        PotionOfFrost.class);
			types.put(Rotberry.Seed.class,      PotionOfMana.class);
			types.put(Sorrowmoss.Seed.class,    PotionOfToxicGas.class);
			types.put(Starflower.Seed.class,    PotionOfMana.class);
			types.put(Stormvine.Seed.class,     PotionOfLevitation.class);
			types.put(Sungrass.Seed.class,      PotionOfRestoration.class);
			types.put(Swiftthistle.Seed.class,  PotionOfHaste.class);
		}
		
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 3) {
				return false;
			}
			
			for (Item ingredient : ingredients){
				if (!(ingredient instanceof Plant.Seed
						&& ingredient.quantity() >= 1
						&& types.containsKey(ingredient.getClass()))){
					return false;
				}
			}
			return true;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 0;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			for (Item ingredient : ingredients){
				ingredient.quantity(ingredient.quantity() - 1);
			}
			
			ArrayList<Class<?extends Plant.Seed>> seeds = new ArrayList<>();
			for (Item i : ingredients) {
				if (!seeds.contains(i.getClass())) {
					seeds.add((Class<? extends Plant.Seed>) i.getClass());
				}
			}
			
			Item result;
			
			if ( (seeds.size() == 2 && Random.Int(4) == 0)
					|| (seeds.size() == 3 && Random.Int(2) == 0)) {

				result = Generator.randomUsingDefaults( Generator.Category.POTION );
				
			} else {
				result = Reflection.newInstance(types.get(Random.element(ingredients).getClass()));
				
			}

			if (seeds.size() == 1){
				result.identify();
			}


			while (result instanceof PotionOfRestoration
					&& (Dungeon.isChallenged(Challenges.NO_HEALING))) {
				result = Generator.randomUsingDefaults(Generator.Category.POTION);
			}
			
			Statistics.potionsCooked++;
			Badges.validatePotionsCooked();
			
			return result;
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
				{
					name = Messages.get(SeedToPotion.class, "name");
				}
				
				@Override
				public String info() {
					return "";
				}
			};
		}
	}
}
