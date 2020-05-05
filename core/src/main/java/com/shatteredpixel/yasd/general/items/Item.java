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

package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Badges;
import com.shatteredpixel.yasd.general.Challenges;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.armor.Armor;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.yasd.general.journal.Catalog;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.MissileSprite;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndItem;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Item implements Bundlable {

	protected static final String TXT_TO_STRING_LVL		= "%s %+d";
	protected static final String TXT_TO_STRING_X		= "%s x%d";
	public static final String AC_INFO = "INFO_WINDOW";
	
	protected static final float TIME_TO_THROW		= 1.0f;
	protected static final float TIME_TO_PICK_UP	= 1.0f;
	protected static final float TIME_TO_DROP		= 1.0f;
	
	public static final String AC_DROP		= "DROP";
	public static final String AC_THROW		= "THROW";
	
	public String defaultAction = AC_INFO;
	public boolean usesTargeting;

	public Class<? extends Bag> necessaryBag = null;
	
	protected String name = Messages.get(this, "name");
	public int image = 0;

	public static final float MAXIMUM_DURABILITY = 1000;
	public float curDurability = MAXIMUM_DURABILITY;
	public boolean saidAlmostBreak = false;
	
	public boolean stackable = false;
	protected int quantity = 1;
	public boolean dropsDownHeap = false;
	
	private int level = 0;
	public boolean levelKnown = false;
	
	public boolean cursed;
	public boolean cursedKnown;
	
	// Unique items persist through revival
	public boolean unique = false;

	// whether an item can be included in heroes remains
	public boolean bones = false;

	private final boolean testing = false;
	
	private static Comparator<Item> itemComparator = new Comparator<Item>() {
		@Override
		public int compare( Item lhs, Item rhs ) {
			return Generator.Category.order( lhs ) - Generator.Category.order( rhs );
		}
	};

	public boolean canDegrade() {
		return false;
	}

	public float degradedPercent() {
		return curDurability/MAXIMUM_DURABILITY;
	}

	public void fullyRepair() {
		if (!Dungeon.isChallenged(Challenges.NO_REPAIR)) {
			curDurability = MAXIMUM_DURABILITY;
		} else {
			curDurability += MAXIMUM_DURABILITY/2;
			curDurability = Math.min(curDurability,MAXIMUM_DURABILITY);
		}
		saidAlmostBreak = false;
	}

	public final void use() {
		use(defaultDegradeAmount());
	}

	public final void use(float amount) {
		use(amount, false);
	}

	public void use(float amount, boolean override) {
		if (curUser == null) {//curUser may be null if activate() has not yet been called (such as on game start). This prevents the next check from throwing an error.
			curUser = Dungeon.hero;
		}
		if (level() < 0 | !(isEquipped(curUser) | override) | cursed) {//Unequipped items should never degrade, as they should not be usable. Exception is the Wand imbued in the Mage's staff, this workaround is made for that.
			return;
		}
		curDurability -= amount;
		if (curDurability <= 0) {
			GLog.n(Messages.get(this,"broken"),this.name());
			Sample.INSTANCE.play(Assets.SND_DEGRADE);
			fullyRepair();
			if (level > 0) {
				degrade();
			} else {
				curse();
				/*cursed = true;
				if (this instanceof MeleeWeapon) {
					((MeleeWeapon)this).enchant(Weapon.Enchantment.randomCurse());
				} else if (this instanceof Armor) {
					((Armor)this).inscribe(Armor.Glyph.randomCurse());
				}*/
			}

		} else if (curDurability <= MAXIMUM_DURABILITY*0.2f & !saidAlmostBreak) {
			GLog.w(Messages.get(this,"almost_break",this.name()));
			saidAlmostBreak = true;
		}
	}

	public int defaultDegradeAmount() {
		return Dungeon.difficulty.degradationAmount();
	}

	public void curse() {
		cursed = true;
	}

	public void uncurse() {
		cursed = false;
	}
	
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = new ArrayList<>();
		actions.add( AC_DROP );
		actions.add( AC_THROW );
		return actions;
	}
	
	public boolean doPickUp( Hero hero ) {
		if (collect( hero.belongings.backpack, hero)) {
			
			GameScene.pickUp( this, hero.pos );
			Sample.INSTANCE.play( Assets.SND_ITEM );
			hero.spendAndNext( TIME_TO_PICK_UP );
			return true;
			
		} else {
			return false;
		}
	}
	
	public void doDrop( Hero hero ) {
		hero.spendAndNext(TIME_TO_DROP);
		int pos = hero.pos;
		Dungeon.level.drop(detachAll(hero.belongings.backpack), pos).sprite.drop(pos);
	}

	//resets an item's properties, to ensure consistency between runs
	public void reset(){
		//resets the name incase the language has changed.
		name = Messages.get(this, "name");
	}

	public void doThrow( Hero hero ) {
		GameScene.selectCell(thrower);
	}
	
	public void execute( Hero hero, String action ) {
		
		curUser = hero;
		//curItem = this;
		
		if (action.equals( AC_DROP )) {
			
			if (hero.belongings.backpack.contains(this) || isEquipped(hero)) {
				doDrop(hero);
			}
			
		} else if (action.equals( AC_THROW )) {
			
			if (hero.belongings.backpack.contains(this) || isEquipped(hero)) {
				doThrow(hero);
			}
			
		} else if (action.equals(AC_INFO)) {
			if (!testing) {
				GameScene.show(new WndItem(null, this, true));
			} else {
				LevelHandler.descend();
			}
		}
	}
	
	public void execute( Hero hero ) {
		execute( hero, defaultAction );
	}
	
	protected void onThrow( int cell ) {
		Heap heap = Dungeon.level.drop( this, cell );
		if (!heap.isEmpty()) {
			heap.sprite.drop( cell );
		}
	}
	
	//takes two items and merges them (if possible)
	public Item merge( Item other ){
		if (isSimilar( other )){
			quantity += other.quantity;
			other.quantity = 0;
		}
		return this;
	}
	
	public boolean collect( Bag container,  Char ch ) {
		
		ArrayList<Item> items = container.items;

		if (necessaryBag != null && !necessaryBag.isInstance(container)) {
			for (Item item:items) {
				if (item instanceof Bag && ((Bag)item).grab( this ) && necessaryBag.isInstance(item)) {
					if (collect( (Bag)item, ch)){
						return true;
					}
				}
			}
			return false;
		}

		curUser = ch;
		if (items.contains( this )) {
			return true;
		}
		
		for (Item item:items) {
			if (item instanceof Bag && ((Bag)item).grab( this )) {
				return collect( (Bag)item, ch);
			}
		}
		
		if (stackable) {
			for (Item item:items) {
				if (isSimilar( item )) {
					item.merge( this );
					updateQuickslot();
					return true;
				}
			}
		}
		
		if (items.size() < container.size) {
			
			if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
				Badges.validateItemLevelAquired( this );
			}
			
			items.add( this );
			Dungeon.quickslot.replacePlaceholder(this);
			updateQuickslot();
			Collections.sort( items, itemComparator );
			return true;
			
		} else {
			GLog.n( Messages.get(Item.class, "pack_full", container.name()) );
			return false;
			
		}
	}
	
	public final boolean collect() {
		return collect( Dungeon.hero.belongings.backpack, Dungeon.hero );
	}
	
	//returns a new item if the split was sucessful and there are now 2 items, otherwise null
	public Item split( int amount ){
		if (amount <= 0 || amount >= quantity()) {
			return null;
		} else {
			//pssh, who needs copy constructors?
			Item split = Reflection.newInstance(getClass());
			
			if (split == null){
				return null;
			}
			
			Bundle copy = new Bundle();
			this.storeInBundle(copy);
			split.restoreFromBundle(copy);
			split.quantity(amount);
			quantity -= amount;
			
			return split;
		}
	}

	@Nullable
	public final Item detach(Bag container ) {
		
		if (quantity <= 0) {
			return null;
			
		} else if (quantity == 1) {

			if (stackable){
				Dungeon.quickslot.convertToPlaceholder(this);
			}
			return detachAll( container );
			
		} else {
			
			
			Item detached = split(1);
			updateQuickslot();
			if (detached != null) detached.onDetach( );
			return detached;
			
		}
	}
	
	public final Item detachAll( Bag container ) {
		Dungeon.quickslot.clearItem( this );
		updateQuickslot();

		for (Item item : container.items) {
			if (item == this) {
				container.items.remove(this);
				item.onDetach();
				return this;
			} else if (item instanceof Bag) {
				Bag bag = (Bag)item;
				if (bag.contains( this )) {
					return detachAll( bag );
				}
			}
		}
		
		return this;
	}
	
	public boolean isSimilar( Item item ) {
		return level == item.level && getClass() == item.getClass();
	}

	protected void onDetach(){}

	//returns the true level of the item, only affected by modifiers which are persistent (e.g. curse infusion)
	public int level(){
		return level;
	}

	public Item level( int value ){
		level = value;

		updateQuickslot();
		return this;
	}
	
	public Item upgrade() {
		
		this.level++;

		updateQuickslot();

		fullyRepair();
		
		return this;
	}
	
	@Contract("_ -> this")
	final public Item upgrade(int n ) {
		for (int i=0; i < n; i++) {
			upgrade();
		}
		
		return this;
	}
	
	public Item degrade() {
		
		this.level--;
		
		return this;
	}
	
	@Contract("_ -> this")
	final public Item degrade(int n ) {
		for (int i=0; i < n; i++) {
			degrade();
		}
		
		return this;
	}
	
	public int visiblyUpgraded() {
		return levelKnown ? level() : 0;
	}
	
	public boolean visiblyCursed() {
		return cursed && cursedKnown;
	}

	public boolean isUpgradable() {
		if (Constants.UPGRADE_LIMIT == -1) {
			return true;
		}
		return level() < upgradeLimit();
	}

	public int upgradeLimit() {
		int limit = Constants.UPGRADE_LIMIT;
		if ((this instanceof Armor && ((Armor)this).curseInfusionBonus) || (this instanceof MeleeWeapon && ((MeleeWeapon)this).curseInfusionBonus) || (this instanceof Wand && ((Wand)this).curseInfusionBonus)) {
			limit++;
		}
		return limit;
	}
	
	public boolean isIdentified() {
		return levelKnown && cursedKnown;
	}


	public boolean isEquipped( Char owner ) {
		return owner.belongings.miscs[0] == this || owner.belongings.miscs[1] == this || owner.belongings.miscs[2] == this || owner.belongings.miscs[3] == this || owner.belongings.miscs[4] == this;
	}

	public Item identify() {
		if (level() > 0 && !isIdentified() && Dungeon.hero.isAlive()) {
			Dungeon.hero.gainMorale(Math.min((float) level(),3));
		}
		levelKnown = true;
		cursedKnown = true;

		if (Dungeon.hero != null && Dungeon.hero.isAlive()) {
			Catalog.setSeen(getClass());
		}
		
		return this;
	}
	
	public void onHeroGainExp( float levelPercent, Hero hero ){
		//do nothing by default
	}
	
	public static void evoke( Hero hero ) {
		hero.sprite.emitter().burst( Speck.factory( Speck.EVOKE ), 5 );
	}
	
	@Override
	public String toString() {

		String name = name();

		if (visiblyUpgraded() != 0)
			name = Messages.format( TXT_TO_STRING_LVL, name, visiblyUpgraded()  );

		if (quantity > 1)
			name = Messages.format( TXT_TO_STRING_X, name, quantity );

		return name;

	}
	
	public String name() {
		return name;
	}
	
	@Contract(pure = true)
	public final String trueName() {
		return name;
	}
	
	public int image() {
		return image;
	}
	
	public ItemSprite.Glowing glowing() {
		return null;
	}

	public Emitter emitter() {
		Emitter emitter = new Emitter();
		if (cursed && cursedKnown) {
			emitter.pour(ShadowParticle.CURSE, 0.15f);
		}
		return emitter;
	}
	
	public String info() {
		return desc();
	}
	
	public String desc() {
		return Messages.get(this, "desc");
	}
	
	public int quantity() {
		return quantity;
	}
	
	public Item quantity( int value ) {
		quantity = value;
		return this;
	}
	
	public int price() {
		return 0;
	}
	
	public Item virtual(){
		Item item = Reflection.newInstance(getClass());
		if (item == null) return null;
		
		item.quantity = 0;
		item.level = level;
		return item;
	}
	
	public Item random() {
		return this;
	}
	
	public String status() {
		return quantity != 1 ? Integer.toString( quantity ) : null;
	}
	
	public static void updateQuickslot() {
			QuickSlotButton.refresh();
	}
	
	private static final String QUANTITY		= "quantity";
	private static final String LEVEL			= "level";
	private static final String LEVEL_KNOWN		= "levelKnown";
	private static final String CURSED			= "cursed";
	private static final String CURSED_KNOWN	= "cursedKnown";
	private static final String QUICKSLOT		= "quickslotpos";
	private static final String DURABILITY      = "durability";
	
	@Override
	public void storeInBundle(  Bundle bundle ) {
		bundle.put( QUANTITY, quantity );
		bundle.put( LEVEL, level );
		bundle.put( LEVEL_KNOWN, levelKnown );
		bundle.put( CURSED, cursed );
		bundle.put( CURSED_KNOWN, cursedKnown );
		bundle.put( DURABILITY, curDurability );
		if (Dungeon.quickslot.contains(this)) {
			bundle.put( QUICKSLOT, Dungeon.quickslot.getSlot(this) );
		}
	}
	
	@Override
	public void restoreFromBundle(  Bundle bundle ) {
		quantity	= bundle.getInt( QUANTITY );
		levelKnown	= bundle.getBoolean( LEVEL_KNOWN );
		cursedKnown	= bundle.getBoolean( CURSED_KNOWN );
		curDurability = bundle.getFloat( DURABILITY );
		
		int level = bundle.getInt( LEVEL );
		level(level);
		
		cursed	= bundle.getBoolean( CURSED );

		//only want to populate slot on first load.
		if (Dungeon.hero == null) {
			if (bundle.contains(QUICKSLOT)) {
				Dungeon.quickslot.setSlot(bundle.getInt(QUICKSLOT), this);
			}
		}
	}

	public int throwPos( Char user, int dst){
		return new Ballistica( user.pos, dst, Ballistica.PROJECTILE ).collisionPos;
	}
	
	public void cast( final Char user, final int dst ) {
		
		final int cell = throwPos( user, dst );
		user.sprite.zap( cell );
		if (user instanceof Hero) {
			user.busy();
		}


		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );

		Char enemy = Actor.findChar( cell );
		QuickSlotButton.target(enemy);
		
		final float delay = castDelay(user, dst);

		if (enemy != null) {
			((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
					reset(user.sprite,
							enemy.sprite,
							this,
							new Callback() {
						@Override
						public void call() {
							curUser = user;
							Item.this.detach(user.belongings.backpack).onThrow(cell);
							user.spendAndNext(delay);
						}
					});
		} else {
			((MissileSprite) user.sprite.parent.recycle(MissileSprite.class)).
					reset(user.sprite,
							cell,
							this,
							new Callback() {
						@Override
						public void call() {
							curUser = user;
							Item.this.detach(user.belongings.backpack).onThrow(cell);
							user.spendAndNext(delay);
						}
					});
		}
	}
	
	public float castDelay( Char user, int dst ){
		return TIME_TO_THROW;
	}
	
	protected static Char curUser = null;
	//protected static Item curItem = null;
	protected CellSelector.Listener thrower = new CellSelector.Listener(this) {
		@Override
		public void onSelect( Integer target ) {
			if (target != null) {
				Item item = (Item) source;
				item.cast( curUser, target );
			}
		}
		@Override
		public String prompt() {
			return Messages.get(Item.class, "prompt");
		}
	};
}
