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

package com.shatteredpixel.yasd.general.ui.changelist;

import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.items.spells.MagicalInfusion;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.ChangesScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.PiranhaSprite;
import com.shatteredpixel.yasd.general.sprites.ShopkeeperSprite;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.shatteredpixel.yasd.general.sprites.WraithSprite;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.Window;

import java.util.ArrayList;

public class YASD_log {
	
	public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){
		add_v0_2_X_Changes(changeInfos);
		add_v0_1_X_Changes(changeInfos);
	}

	private static void add_v0_2_X_Changes(ArrayList<ChangeInfo> changeInfos) {
		ChangeInfo changes = new ChangeInfo( "0.2 - release", true, "");
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add( changes );


		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"),false,null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add( changes );


		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Chapter length", "Chapters are now 6 depths long"));

		changes.addButton(new ChangeButton(new MagicalInfusion(),
				"Magical Infusion reworked:\n" +
						"_-_ Now increases the tier of an item rather than upgrading it\n" +
						"_-_ Tier 5 is the highest tier"));

		changes.addButton(new ChangeButton(Icons.get(Icons.COMPASS), "Attack/defense system reworked", "_-_ Armours now block magical as well as physical damage\n" +
				"_-_ Mobs with magic always use it, even at melee\n" +
				"_-_ Weapons are now divided into classes, meaning you can find them at any tier (and they may change sprites/names depending on tier)"));

		changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SANDALS, null), "Tall Grass",
				"Tall grass can now rarely drop Stones."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_DAMNATION, null), "New Wands", "Added several wands from YAPD and some original ones too:\n" +
				"_-_ Wand of Damnation\n" +
				"_-_ Wand of Acid\n" +
				"_-_ Wand of Darkness\n" +
				"_-_ Wand of Flow\n" +
				"_-_ Wand of Plasma Bolt\n" +
				"_-_ Wand of Thornvines"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.BEER, null), "Alcohol", "Alcohol has been reworked so it is less run-damaging:\n" +
				"_-_ No longer speeds morale loss\n" +
				"_-_ Now gives hero the \"Drunk\" buff"));

		changes.addButton(new ChangeButton(new StatueSprite(), "Animated Statues", "Animated Statues can now use gear in the same way as the hero:\n" +
				"_-_ Always have all 5 slots filled\n" +
				"_-_ Have a maximum total number of upgrades they can get depending on depth\n" +
				"_-_ Revive themselves using Blessed Ankhs when on low HP. The number of times this can be done depends on depth."));

		changes.addButton(new ChangeButton(new WraithSprite(), "Wraiths", "Wraiths now have more YAPD style behaviour:\n" +
				"_-_ Ranged, fire draining bolts\n" +
				"_-_ Can teleport away with a chance\n" +
				"_-_ Have more than 0 HP and less evasion."));

		changes.addButton(new ChangeButton(new ShopkeeperSprite(), "shops", "Shops are reworked:\n" +
				"_-_ Shopkeeper now summons guardians when killed, but leaves stuff behind\n" +
				"_-_ Guardians are Animated Statues that have 1 ankh always and don't drop gear, but also resist a number of effects.\n" +
				"_-_ Shops can also provide upgraded gear, and no longer have Ankhs..."));


		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"),false,null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);


		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_AGATE), "New/replaced rings", "New rings have been added along with the stat system:\n" +
				"Ring of Power increases Power (replaces Might), Ring of Focus increases Focus (replaces Energy), Ring of Expertise increases Expertise (replaces Accuracy and Evasion) and lastly Ring of Luck which replaces nothing."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), "Altar rooms", "Vanilla Altar rooms have returned, but they no longer provide the Scroll of Wipe Out\n\n" +
				"Instead, they grant an Ankh." +
				"(For those who have not played Vanilla much or at all, you will find a room with a blue flame, kill mobs on the flame to get the reward)"));

		changes.addButton(new ChangeButton(Icons.get(Icons.CHECKED), "Difficulty", "Difficulty settings added - Easy, Normal or Hard."));

		changes.addButton(new ChangeButton(Icons.get(Icons.CHALLENGE_ON), "Challenges", "Some challenges reworked:\n" +
				"_-_ Forbidden Runes now blocks SoU altogether\n" +
				"_-_ New challenge: Rust. Items can only be half repaired, and Stone of Repair is gone.\n" +
				"_-_ New Challenge: Collapse. The Dungeon collapses beneath your feet..."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.GREATSWORD), "Degradation", "Degradation has been added:\n" +
				"_-_ It depends on difficulty level\n" +
				"_-_ Degraded items lose an upgrade; if they are at +0 they instead become cursed. Cursed items do not degrade.\n" +
				"_-_ Stones of Repair can be found to repair items fully. Scrolls of Upgrade also do this.\n" +
				"_-_ There are also some other features to degradation, such as only your  first armour degrading, and weapons only being degraded when they attack."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.STONE_ENCHANT), "Enchantments", "Stones of enchantment have replaced Arcane Styli as chapter based drops. This gives you a chance to enchant your weapons and armours."));

		changes.addButton(new ChangeButton(new PiranhaSprite(), "Diving", "Diving mechanic added:\n" +
				"_-_ Hero can dive when over deep water.\n" +
				"_-_ This will send them to an alternative map, where they have limited air.\n" +
				"_-_ This area is full of Piranhas, and a new mob stolen from Prismatic PD: Jellyfish"));

		changes.addButton(new ChangeButton(Icons.get(HeroClass.WARRIOR), "Stats system", "Added a stat system:\n" +
				"_-_ When leveling up, hero will get distribution points. These can be used to upgrade skills.\n" +
				"_-_ Power - increases the hero's strength.\n" +
				"_-_ Focus - increases the damage of the hero's wands\n" +
				"_-_ Expertise - increases accuracy and evasion\n" +
				"_-_ Luck - increases luck in finding good gear, etc"));


		changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
		changes.hardlight( CharSprite.POSITIVE );
		changeInfos.add(changes);


		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.ANKH), "(Blessed) Ankhs", "Ankhs are now always blessed. Additionally, they cleanse equipped items."));


		changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
		changes.hardlight( CharSprite.NEGATIVE );
		changeInfos.add(changes);


		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Morale System", "Morale system was too unfair and has been reworked to no only be affected by attacks, not bleed/poison."));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE_YAPD, null), "Wand balance", "Wands have been nerfed, as the stat \"Focus\" affects them:\n" +
				"Wands on average now do less damage. Wands that do not do direct damage have had other changes."));
	}

	private static void add_v0_1_X_Changes(ArrayList<ChangeInfo> changeInfos) {
		ChangeInfo changes = new ChangeInfo( "0.1 - release", true, "");
		changes.hardlight(Window.TITLE_COLOR);
		changeInfos.add(changes);
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"),false,null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);
		
		changes.addButton(new ChangeButton(Icons.get(Icons.COMPASS), "Equipment System Reworked", "The equipment system has been completely overhauled:\n" +
				"_-_ The hero now has 5 equip slots, and Wands must be equipped\n" +
				"_-_ Each slot can hold a weapon, armour, a wand, a ring or an artifact\n" +
				"_-_ When using multiple weapons, you will attack with one after the other, and strength requirements will be increased\n" +
				"_-_ When using multiple armours, all defense rolls will be added together and all glyphs will proc from left to right, and strength requirements will be increased."));

		changes.addButton(new ChangeButton(Icons.get(Icons.RANKINGS), "Upgrade Limits", "All items are capped at +3"));
		
		changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"),false,null);
		changes.hardlight( Window.TITLE_COLOR );
		changeInfos.add(changes);

		changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "Morale System", "A Darkest PD style \"Morale\" system has been implemented:\n" +
				"_-_ Taking large amounts of damage, starving and some other things will reduce hero's Morale\n" +
				"_-_ Low Morale will cause your accuracy, evasion and Wand charge speed to be reduced\n" +
				"_-_ You can increase Morale a little by eating or leveling up, and a lot through Scrolls of Lullaby and Beer or Whiskey\n" +
				"_-_ Be careful though, as Beer and Whiskey will also permanently increase the rate at which the hero loses morale"));

		changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.WAND_LIFE_DRAIN, null), "Wand of Life Drain", "Wand of Life Drain from YAPD has been added:\n" +
				"_-_ Functionality is basically the same as YAPD\n" +
				"_-_ Consumes all charges, heavy damage, heals the hero if the enemy is not undead"));
	}
	
	
}
