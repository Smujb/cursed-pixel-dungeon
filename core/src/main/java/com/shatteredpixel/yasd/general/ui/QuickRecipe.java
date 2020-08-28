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

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.DewVial;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.Generator;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.Recipe;
import com.shatteredpixel.yasd.general.items.bombs.Bomb;
import com.shatteredpixel.yasd.general.items.food.Blandfruit;
import com.shatteredpixel.yasd.general.items.food.Food;
import com.shatteredpixel.yasd.general.items.food.MeatPie;
import com.shatteredpixel.yasd.general.items.food.MysteryMeat;
import com.shatteredpixel.yasd.general.items.food.Pasty;
import com.shatteredpixel.yasd.general.items.food.StewedMeat;
import com.shatteredpixel.yasd.general.items.potions.AlchemicalCatalyst;
import com.shatteredpixel.yasd.general.items.potions.Potion;
import com.shatteredpixel.yasd.general.items.potions.brews.BlizzardBrew;
import com.shatteredpixel.yasd.general.items.potions.brews.BrewOfHolyWater;
import com.shatteredpixel.yasd.general.items.potions.brews.CausticBrew;
import com.shatteredpixel.yasd.general.items.potions.brews.InfernalBrew;
import com.shatteredpixel.yasd.general.items.potions.brews.ShockingBrew;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfArcaneArmor;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfDragonsBlood;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfIcyTouch;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfMight;
import com.shatteredpixel.yasd.general.items.potions.elixirs.ElixirOfToxicEssence;
import com.shatteredpixel.yasd.general.items.potions.exotic.ExoticPotion;
import com.shatteredpixel.yasd.general.items.quest.GooBlob;
import com.shatteredpixel.yasd.general.items.scrolls.Scroll;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.yasd.general.items.spells.Alchemize;
import com.shatteredpixel.yasd.general.items.spells.AquaBlast;
import com.shatteredpixel.yasd.general.items.spells.ArcaneCatalyst;
import com.shatteredpixel.yasd.general.items.spells.ArcaneInfusion;
import com.shatteredpixel.yasd.general.items.spells.BeaconOfReturning;
import com.shatteredpixel.yasd.general.items.spells.CurseInfusion;
import com.shatteredpixel.yasd.general.items.spells.Degrade;
import com.shatteredpixel.yasd.general.items.spells.FeatherFall;
import com.shatteredpixel.yasd.general.items.spells.MagicalInfusion;
import com.shatteredpixel.yasd.general.items.spells.MagicalPorter;
import com.shatteredpixel.yasd.general.items.spells.PhaseShift;
import com.shatteredpixel.yasd.general.items.spells.ReclaimTrap;
import com.shatteredpixel.yasd.general.items.spells.Recycle;
import com.shatteredpixel.yasd.general.items.spells.SafeUpgrade;
import com.shatteredpixel.yasd.general.items.spells.WildEnergy;
import com.shatteredpixel.yasd.general.items.stones.Runestone;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.plants.Plant;
import com.shatteredpixel.yasd.general.scenes.AlchemyScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.windows.WndBag;
import com.shatteredpixel.yasd.general.windows.WndInfoItem;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class QuickRecipe extends Component {
	
	private ArrayList<Item> ingredients;
	
	private ArrayList<ItemSlot> inputs;
	private Arrow arrow;
	private ItemSlot output;
	
	public QuickRecipe(Recipe.SimpleRecipe r){
		this(r, r.getIngredients(), r.sampleOutput(null));
	}
	
	public QuickRecipe(Recipe r, ArrayList<Item> inputs, final Item output) {
		
		ingredients = inputs;
		int cost = r.cost(inputs);
		boolean hasInputs = true;
		this.inputs = new ArrayList<>();
		for (final Item in : inputs) {
			anonymize(in);
			ItemSlot curr;
			curr = new ItemSlot(in) {
				@Override
				protected void onClick() {
					CPDGame.scene().addToFront(new WndInfoItem(in));
				}
			};
			
			ArrayList<Item> similar = Dungeon.hero.belongings.getAlchemyInputs(in);
			int quantity = 0;
			for (Item sim : similar) {
				//if we are looking for a specific item, it must be IDed
				if (sim.isIdentified()) quantity += sim.quantity();
			}

			if (in instanceof Dewdrop) {
				quantity = DewVial.volume();
			}
			
			if (quantity < in.quantity()) {
				curr.sprite.alpha(0.3f);
				hasInputs = false;
			}
			curr.showExtraInfo(false);
			add(curr);
			this.inputs.add(curr);
		}
		
		if (cost > 0) {
			arrow = new Arrow(Icons.get(Icons.ARROW), cost);
			arrow.hardlightText(0x00CCFF);
		} else {
			arrow = new Arrow(Icons.get(Icons.ARROW));
		}
		if (hasInputs) {
			arrow.icon.tint(1, 1, 0, 1);
			if (!(CPDGame.scene() instanceof AlchemyScene)) {
				arrow.enable(false);
			}
		} else {
			arrow.icon.color(0, 0, 0);
			arrow.enable(false);
		}
		add(arrow);
		
		anonymize(output);
		this.output = new ItemSlot(output){
			@Override
			protected void onClick() {
				CPDGame.scene().addToFront(new WndInfoItem(output));
			}
		};
		if (!hasInputs){
			this.output.sprite.alpha(0.3f);
		}
		this.output.showExtraInfo(false);
		add(this.output);
		
		layout();
	}
	
	@Override
	protected void layout() {
		
		height = 16;
		width = 0;
		
		for (ItemSlot item : inputs){
			item.setRect(x + width, y, 16, 16);
			width += 16;
		}
		
		arrow.setRect(x + width, y, 14, 16);
		width += 14;
		
		output.setRect(x + width, y, 16, 16);
		width += 16;
	}
	
	//used to ensure that un-IDed items are not spoiled
	private void anonymize(Item item){
		if (item instanceof Potion){
			((Potion) item).anonymize();
		} else if (item instanceof Scroll){
			((Scroll) item).anonymize();
		}
	}
	
	public class Arrow extends IconButton {
		
		BitmapText text;
		
		public Arrow(){
			super();
		}
		
		public Arrow(Image icon ){
			super( icon );
		}
		
		public Arrow(Image icon, int count ){
			super( icon );
			text = new BitmapText( Integer.toString(count), PixelScene.pixelFont);
			text.measure();
			add(text);
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (text != null){
				text.x = x;
				text.y = y;
				PixelScene.align(text);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			
			//find the window this is inside of and close it
			Group parent = this.parent;
			while (parent != null){
				if (parent instanceof Window){
					((Window) parent).hide();
					break;
				} else {
					parent = parent.parent;
				}
			}
			
			((AlchemyScene) CPDGame.scene()).populate(ingredients, Dungeon.hero.belongings);
		}
		
		public void hardlightText(int color ){
			if (text != null) text.hardlight(color);
		}
	}
	
	//gets recipes for a particular alchemy guide page
	//a null entry indicates a break in section
	public static ArrayList<QuickRecipe> getRecipes( int pageIdx ){
		ArrayList<QuickRecipe> result = new ArrayList<>();
		switch (pageIdx){
			case 0: default:
				result.add(new QuickRecipe( new Potion.SeedToPotion(), new ArrayList<>(Arrays.asList(new Plant.Seed.PlaceHolder().quantity(3))), new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
					{
						name = Messages.get(Potion.SeedToPotion.class, "name");
					}
					
					@Override
					public String info() {
						return "";
					}
				}));
				return result;
			case 1:
				Recipe r = new Scroll.ScrollToStone();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					Scroll scroll = (Scroll) Reflection.newInstance(cls);
					if (!scroll.isKnown()) scroll.anonymize();
					ArrayList<Item> in = new ArrayList<Item>(Arrays.asList(scroll));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 2:
				result.add(new QuickRecipe( new StewedMeat.oneMeat() ));
				result.add(new QuickRecipe( new StewedMeat.twoMeat() ));
				result.add(new QuickRecipe( new StewedMeat.threeMeat() ));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new MeatPie.Recipe(),
						new ArrayList<Item>(Arrays.asList(new Pasty(), new Food(), new MysteryMeat.PlaceHolder())),
						new MeatPie()));
				result.add(new QuickRecipe(new GooBlob.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new Blandfruit.CookFruit(),
						new ArrayList<>(Arrays.asList(new Blandfruit(), new Plant.Seed.PlaceHolder())),
						new Blandfruit(){
							{
								name = Messages.get(Blandfruit.class, "cooked");
							}
							
							@Override
							public String info() {
								return "";
							}
						}));
				return result;
			case 3:
				r = new Bomb.EnhanceBomb();
				int i = 0;
				for (Class<?> cls : Bomb.EnhanceBomb.validIngredients.keySet()){
					if (i == 2){
						result.add(null);
						i = 0;
					}
					Item item = (Item) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(new Bomb(), item));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					i++;
				}
				return result;
			case 4:
				r = new ExoticPotion.PotionToExotic();
				for (Class<?> cls : Generator.Category.POTION.classes){
					Potion pot = (Potion) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(pot, new Plant.Seed.PlaceHolder().quantity(2)));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 5:
				r = new ExoticScroll.ScrollToExotic();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					Scroll scroll = (Scroll) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(scroll, new Runestone.PlaceHolder().quantity(2)));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 6:
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Plant.Seed.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Runestone.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Runestone.PlaceHolder())), new ArcaneCatalyst()));
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Plant.Seed.PlaceHolder())), new ArcaneCatalyst()));
				return result;
			case 7:
				result.add(new QuickRecipe(new CausticBrew.Recipe()));
				result.add(new QuickRecipe(new InfernalBrew.Recipe()));
				result.add(new QuickRecipe(new BlizzardBrew.Recipe()));
				result.add(new QuickRecipe(new ShockingBrew.Recipe()));
				result.add(new QuickRecipe(new BrewOfHolyWater.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ElixirOfHoneyedHealing.Recipe()));
				result.add(new QuickRecipe(new ElixirOfMight.Recipe()));
				result.add(new QuickRecipe(new ElixirOfAquaticRejuvenation.Recipe()));
				result.add(new QuickRecipe(new ElixirOfDragonsBlood.Recipe()));
				result.add(new QuickRecipe(new ElixirOfIcyTouch.Recipe()));
				result.add(new QuickRecipe(new ElixirOfToxicEssence.Recipe()));
				result.add(new QuickRecipe(new ElixirOfArcaneArmor.Recipe()));
				return result;
			case 8:
				result.add(new QuickRecipe(new MagicalPorter.Recipe()));
				result.add(new QuickRecipe(new PhaseShift.Recipe()));
				result.add(new QuickRecipe(new WildEnergy.Recipe()));
				result.add(new QuickRecipe(new Recycle.Recipe()));
				result.add(new QuickRecipe(new BeaconOfReturning.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new AquaBlast.Recipe()));
				result.add(new QuickRecipe(new Alchemize.Recipe()));
				result.add(new QuickRecipe(new FeatherFall.Recipe()));
				result.add(new QuickRecipe(new ReclaimTrap.Recipe()));
				result.add(new QuickRecipe(new CurseInfusion.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new Degrade.Recipe()));
				result.add(new QuickRecipe(new SafeUpgrade.Recipe()));
				result.add(new QuickRecipe(new ArcaneInfusion.Recipe()));
				result.add(new QuickRecipe(new MagicalInfusion.Recipe()));
				return result;
		}
	}
	
}
