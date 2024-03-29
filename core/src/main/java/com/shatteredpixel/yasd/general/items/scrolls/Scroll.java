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

package com.shatteredpixel.yasd.general.items.scrolls;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.buffs.Blindness;
import com.shatteredpixel.yasd.general.actors.buffs.MagicImmune;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.ItemStatusHandler;
import com.shatteredpixel.yasd.general.items.Recipe;
import com.shatteredpixel.yasd.general.items.artifacts.UnstableSpellbook;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfAntiMagic;
import com.shatteredpixel.yasd.general.items.stones.Runestone;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAffection;
import com.shatteredpixel.yasd.general.items.stones.StoneOfAugmentation;
import com.shatteredpixel.yasd.general.items.stones.StoneOfBlast;
import com.shatteredpixel.yasd.general.items.stones.StoneOfBlink;
import com.shatteredpixel.yasd.general.items.stones.StoneOfClairvoyance;
import com.shatteredpixel.yasd.general.items.stones.StoneOfDeepenedSleep;
import com.shatteredpixel.yasd.general.items.stones.StoneOfDisarming;
import com.shatteredpixel.yasd.general.items.stones.StoneOfEnchantment;
import com.shatteredpixel.yasd.general.items.stones.StoneOfFlock;
import com.shatteredpixel.yasd.general.items.stones.StoneOfIntuition;
import com.shatteredpixel.yasd.general.items.stones.StoneOfProtection;
import com.shatteredpixel.yasd.general.items.stones.StoneOfShock;
import com.shatteredpixel.yasd.general.journal.Catalog;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.HeroSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Scroll extends Item {
	
	public static final String AC_READ	= "READ";
	
	protected static final float TIME_TO_READ	= 1f;

	protected int mpCost = 0;

	private static final Class<?>[] scrolls = {
		ScrollOfIdentify.class,
		ScrollOfMagicMapping.class,
		ScrollOfRecharging.class,
		ScrollOfRemoveCurse.class,
		ScrollOfTeleportation.class,
		ScrollOfGreed.class,
		ScrollOfRage.class,
		ScrollOfTerror.class,
		ScrollOfLullaby.class,
		ScrollOfTransmutation.class,
		ScrollOfRetribution.class,
		ScrollOfMirrorImage.class
	};

	private static final HashMap<String, Integer> runes = new HashMap<String, Integer>() {
		{
			put("KAUNAN",ItemSpriteSheet.SCROLL_KAUNAN);
			put("SOWILO",ItemSpriteSheet.SCROLL_SOWILO);
			put("LAGUZ",ItemSpriteSheet.SCROLL_LAGUZ);
			put("YNGVI",ItemSpriteSheet.SCROLL_YNGVI);
			put("GYFU",ItemSpriteSheet.SCROLL_GYFU);
			put("RAIDO",ItemSpriteSheet.SCROLL_RAIDO);
			put("ISAZ",ItemSpriteSheet.SCROLL_ISAZ);
			put("MANNAZ",ItemSpriteSheet.SCROLL_MANNAZ);
			put("NAUDIZ",ItemSpriteSheet.SCROLL_NAUDIZ);
			put("BERKANAN",ItemSpriteSheet.SCROLL_BERKANAN);
			put("ODAL",ItemSpriteSheet.SCROLL_ODAL);
			put("TIWAZ",ItemSpriteSheet.SCROLL_TIWAZ);
		}
	};
	
	protected static ItemStatusHandler<Scroll> handler;
	
	protected String rune;
	
	{
		stackable = true;
		defaultAction = AC_READ;
	}
	
	@SuppressWarnings("unchecked")
	public static void initLabels() {
		handler = new ItemStatusHandler<>( (Class<? extends Scroll>[])scrolls, runes );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	public static void saveSelectively( Bundle bundle, ArrayList<Item> items ) {
		ArrayList<Class<?extends Item>> classes = new ArrayList<>();
		for (Item i : items){
			if (i instanceof ExoticScroll){
				if (!classes.contains(ExoticScroll.exoToReg.get(i.getClass()))){
					classes.add(ExoticScroll.exoToReg.get(i.getClass()));
				}
			} else if (i instanceof Scroll){
				if (!classes.contains(i.getClass())){
					classes.add(i.getClass());
				}
			}
		}
		handler.saveClassesSelectively( bundle, classes );
	}

	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<>( (Class<? extends Scroll>[])scrolls, runes, bundle );
	}
	
	public Scroll() {
		super();
		reset();
	}
	
	//anonymous scrolls are always IDed, do not affect ID status,
	//and their sprite is replaced by a placeholder if they are not known,
	//useful for items that appear in UIs, or which are only spawned for their effects
	protected boolean anonymous = false;
	public void anonymize(){
		if (!isKnown()) image = ItemSpriteSheet.SCROLL_HOLDER;
		anonymous = true;
	}
	
	
	@Override
	public void reset(){
		super.reset();
		if (handler != null && handler.contains(this)) {
			image = handler.image(this);
			rune = handler.label(this);
		}
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_READ );
		return actions;
	}
	
	@Override
	public void execute(  Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_READ )) {

			if (hero.buff(MagicImmune.class) != null) {
				GLog.warning(Messages.get(this, "no_magic"));
			//InventoryScroll uses Mana in onSelect.
			} else if (!((isKnown() && this instanceof InventoryScroll) || hero.useMP(mpCost))) {
				GLog.warning(Messages.get(this, "no_mana"));
			} else if (hero.buff(Blindness.class) != null) {
				GLog.warning(Messages.get(this, "blinded"));
			} else if (hero.buff(UnstableSpellbook.bookRecharge.class) != null
					&& hero.buff(UnstableSpellbook.bookRecharge.class).isCursed()
					&& !(this instanceof ScrollOfRemoveCurse || this instanceof ScrollOfAntiMagic)) {
				GLog.negative(Messages.get(this, "cursed"));
			} else {
				curUser = hero;
				Scroll scroll = (Scroll) detach(hero.belongings.backpack);
				if (scroll != null) {
					scroll.doRead();
				} else {
					GLog.debug("Could not read scroll.");
				}
			}

		}
	}
	
	public abstract void doRead();
	
	//currently unused. Used to be used for unstable spellbook prior to 0.7.0 [Evan]. I [SmuJamesB] may add a subclass focusing on these...
	public void empoweredRead(){}

	protected void readAnimation() {
		if (curUser instanceof Hero) {
			curUser.spend(TIME_TO_READ);
			curUser.busy();
			((HeroSprite) curUser.sprite).read();
		}
	}
	
	public boolean isKnown() {
		return anonymous || (handler != null && handler.isKnown( this ));
	}
	
	public void setKnown() {
		if (!anonymous) {
			if (!isKnown()) {
				handler.know(this);
				updateQuickslot();
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
		return isKnown() ? name : Messages.get(this, rune);
	}
	
	@Override
	public String info() {
		return isKnown() ?
				(desc() + (mpCost > 0 ? Messages.get(this, "cost", mpCost) : "")) :
			Messages.get(this, "unknown_desc");
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return isKnown();
	}
	
	public static HashSet<Class<? extends Scroll>> getKnown() {
		return handler.known();
	}
	
	public static HashSet<Class<? extends Scroll>> getUnknown() {
		return handler.unknown();
	}
	
	public static boolean allKnown() {
		return handler.known().size() == scrolls.length;
	}
	
	@Override
	public int price() {
		return 60 * quantity;
	}
	
	public static class PlaceHolder extends Scroll {
		
		{
			image = ItemSpriteSheet.SCROLL_HOLDER;
		}
		
		@Override
		public boolean isSimilar( Item item) {
			return ExoticScroll.regToExo.containsKey(item.getClass())
					|| ExoticScroll.regToExo.containsValue(item.getClass());
		}
		
		@Override
		public void doRead() {}
		
		@Override
		public String info() {
			return "";
		}
	}
	
	public static class ScrollToStone extends Recipe {
		
		private static HashMap<Class<?extends Scroll>, Class<?extends Runestone>> stones = new HashMap<>();
		private static HashMap<Class<?extends Scroll>, Integer> amnts = new HashMap<>();
		static {
			stones.put(ScrollOfIdentify.class,      StoneOfIntuition.class);
			amnts.put(ScrollOfIdentify.class,       3);
			
			stones.put(ScrollOfLullaby.class,       StoneOfDeepenedSleep.class);
			amnts.put(ScrollOfLullaby.class,        3);
			
			stones.put(ScrollOfMagicMapping.class,  StoneOfClairvoyance.class);
			amnts.put(ScrollOfMagicMapping.class,   3);
			
			stones.put(ScrollOfMirrorImage.class,   StoneOfFlock.class);
			amnts.put(ScrollOfMirrorImage.class,    3);
			
			stones.put(ScrollOfRetribution.class,   StoneOfBlast.class);
			amnts.put(ScrollOfRetribution.class,    2);
			
			stones.put(ScrollOfRage.class,          StoneOfProtection.class);
			amnts.put(ScrollOfRage.class,           3);
			
			stones.put(ScrollOfRecharging.class,    StoneOfShock.class);
			amnts.put(ScrollOfRecharging.class,     2);
			
			stones.put(ScrollOfRemoveCurse.class,   StoneOfDisarming.class);
			amnts.put(ScrollOfRemoveCurse.class,    2);
			
			stones.put(ScrollOfTeleportation.class, StoneOfBlink.class);
			amnts.put(ScrollOfTeleportation.class,  2);
			
			stones.put(ScrollOfTerror.class,        StoneOfAffection.class);
			amnts.put(ScrollOfTerror.class,         3);
			
			stones.put(ScrollOfTransmutation.class, StoneOfAugmentation.class);
			amnts.put(ScrollOfTransmutation.class,  2);
			
			stones.put(ScrollOfGreed.class,       StoneOfEnchantment.class);
			amnts.put(ScrollOfGreed.class,        2);
		}
		
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			return ingredients.size() == 1
					&& ingredients.get(0).isIdentified()
					&& ingredients.get(0) instanceof Scroll
					&& stones.containsKey(ingredients.get(0).getClass());
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 0;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			Scroll s = (Scroll) ingredients.get(0);
			
			s.quantity(s.quantity() - 1);
			
			return Reflection.newInstance(stones.get(s.getClass())).quantity(amnts.get(s.getClass()));
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			Scroll s = (Scroll) ingredients.get(0);
			return Reflection.newInstance(stones.get(s.getClass())).quantity(amnts.get(s.getClass()));
		}
	}
}
