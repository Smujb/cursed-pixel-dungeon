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

package com.shatteredpixel.yasd.general.sprites;

import com.shatteredpixel.yasd.general.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.GameMath;

public class ItemSpriteSheet {

	public static final int WIDTH = 32;
	public static final int SIZE = 16;

	public static TextureFilm film = new TextureFilm( Assets.Sprites.ITEMS, SIZE, SIZE );

	private static int xy(int x, int y){
		x -= 1; y -= 1;
		return x + WIDTH*y;
	}

	private static void assignItemRect( int item, int width, int height ){
		int x = (item % WIDTH) * SIZE;
		int y = (item / WIDTH) * SIZE;
		film.add( item, x, y, x+width, y+height);
	}

	private static final int PLACEHOLDERS   =                               xy(1, 1);   //16 slots
	//SOMETHING is the default item sprite at position 0. May show up ingame if there are bugs.
	public static final int SOMETHING       = PLACEHOLDERS+0;
	public static final int WEAPON_HOLDER   = PLACEHOLDERS+1;
	public static final int ARMOR_HOLDER    = PLACEHOLDERS+2;
	public static final int MISSILE_HOLDER  = PLACEHOLDERS+3;
	public static final int WAND_HOLDER     = PLACEHOLDERS+4;
	public static final int RING_HOLDER     = PLACEHOLDERS+5;
	public static final int ARTIFACT_HOLDER = PLACEHOLDERS+6;
	public static final int FOOD_HOLDER     = PLACEHOLDERS+7;
	public static final int BOMB_HOLDER     = PLACEHOLDERS+8;
	public static final int POTION_HOLDER   = PLACEHOLDERS+9;
	public static final int SCROLL_HOLDER   = PLACEHOLDERS+11;
	public static final int SEED_HOLDER     = PLACEHOLDERS+10;
	public static final int STONE_HOLDER    = PLACEHOLDERS+12;
	public static final int CATA_HOLDER     = PLACEHOLDERS+13;
	public static final int ELIXIR_HOLDER   = PLACEHOLDERS+14;
	public static final int SPELL_HOLDER    = PLACEHOLDERS+15;
	static{
		assignItemRect(SOMETHING,       8,  13);
		assignItemRect(WEAPON_HOLDER,   14, 14);
		assignItemRect(ARMOR_HOLDER,    14, 12);
		assignItemRect(MISSILE_HOLDER,  15, 15);
		assignItemRect(WAND_HOLDER,     14, 14);
		assignItemRect(RING_HOLDER,     8,  10);
		assignItemRect(ARTIFACT_HOLDER, 15, 15);
		assignItemRect(FOOD_HOLDER,     15, 11);
		assignItemRect(BOMB_HOLDER,     10, 13);
		assignItemRect(POTION_HOLDER,   12, 14);
		assignItemRect(SEED_HOLDER,     10, 10);
		assignItemRect(SCROLL_HOLDER,   15, 14);
		assignItemRect(STONE_HOLDER,    14, 12);
		assignItemRect(CATA_HOLDER,     6,  15);
		assignItemRect(ELIXIR_HOLDER,   12, 14);
		assignItemRect(SPELL_HOLDER,    8,  16);
	}

	private static final int UNCOLLECTIBLE  =                               xy(1, 2);   //16 slots
	public static final int GOLD            = UNCOLLECTIBLE+0;
	public static final int DEWDROP         = UNCOLLECTIBLE+1;
	public static final int PETAL           = UNCOLLECTIBLE+2;
	public static final int SANDBAG         = UNCOLLECTIBLE+3;
	
	public static final int GUIDE_PAGE      = UNCOLLECTIBLE+6;
	public static final int ALCH_PAGE       = UNCOLLECTIBLE+7;
	
	public static final int TENGU_BOMB      = UNCOLLECTIBLE+9;
	public static final int TENGU_SHOCKER   = UNCOLLECTIBLE+10;
	static{
		assignItemRect(GOLD,        15, 13);
		assignItemRect(DEWDROP,     10, 10);
		assignItemRect(PETAL,       8,  8);
		assignItemRect(SANDBAG,     10, 10);
		
		assignItemRect(GUIDE_PAGE,  10, 11);
		assignItemRect(ALCH_PAGE,   10, 11);
		
		assignItemRect(TENGU_BOMB,      10, 10);
		assignItemRect(TENGU_SHOCKER,   10, 10);
	}

	private static final int CONTAINERS     =                               xy(1, 3);   //16 slots
	public static final int BONES           = CONTAINERS+0;
	public static final int REMAINS         = CONTAINERS+1;
	public static final int TOMB            = CONTAINERS+2;
	public static final int GRAVE           = CONTAINERS+3;
	public static final int CHEST           = CONTAINERS+4;
	public static final int LOCKED_CHEST    = CONTAINERS+5;
	public static final int CRYSTAL_CHEST   = CONTAINERS+6;
	public static final int EBONY_CHEST     = CONTAINERS+7;
	static{
		assignItemRect(BONES,           14, 11);
		assignItemRect(REMAINS,         14, 11);
		assignItemRect(TOMB,            14, 15);
		assignItemRect(GRAVE,           14, 15);
		assignItemRect(CHEST,           16, 14);
		assignItemRect(LOCKED_CHEST,    16, 14);
		assignItemRect(CRYSTAL_CHEST,   16, 14);
		assignItemRect(EBONY_CHEST,     16, 14);
	}

	private static final int SINGLE_USE     =                               xy(1, 4);   //16 slots
	public static final int ANKH            = SINGLE_USE+0;
	public static final int STYLUS          = SINGLE_USE+1;
	
	public static final int SEAL            = SINGLE_USE+3;
	public static final int TORCH           = SINGLE_USE+4;
	public static final int BEACON          = SINGLE_USE+5;

	public static final int HONEYPOT        = SINGLE_USE+7;
	public static final int SHATTPOT        = SINGLE_USE+8;
	public static final int MASTERY         = SINGLE_USE+13;
	public static final int KIT             = SINGLE_USE+14;
	static{
		assignItemRect(ANKH,            10, 16);
		assignItemRect(STYLUS,          12, 13);
		
		assignItemRect(SEAL,            9,  15);
		assignItemRect(TORCH,           12, 15);
		assignItemRect(BEACON,          16, 15);
		
		assignItemRect(HONEYPOT,        14, 12);
		assignItemRect(SHATTPOT,        14, 12);
		assignItemRect(MASTERY,         13, 16);
		assignItemRect(KIT,             16, 15);
	}
	
	private static final int BOMBS          =                               xy(1, 5);   //16 slots
	public static final int BOMB            = BOMBS+0;
	public static final int DBL_BOMB        = BOMBS+1;
	public static final int FIRE_BOMB       = BOMBS+2;
	public static final int FROST_BOMB      = BOMBS+3;
	public static final int REGROWTH_BOMB   = BOMBS+4;
	public static final int FLASHBANG       = BOMBS+5;
	public static final int SHOCK_BOMB      = BOMBS+6;
	public static final int HOLY_BOMB       = BOMBS+7;
	public static final int WOOLY_BOMB      = BOMBS+8;
	public static final int NOISEMAKER      = BOMBS+9;
	public static final int ARCANE_BOMB     = BOMBS+10;
	public static final int SHRAPNEL_BOMB   = BOMBS+11;
	
	static{
		assignItemRect(BOMB,            10, 13);
		assignItemRect(DBL_BOMB,        14, 13);
		assignItemRect(FIRE_BOMB,       13, 12);
		assignItemRect(FROST_BOMB,      13, 12);
		assignItemRect(REGROWTH_BOMB,   13, 12);
		assignItemRect(FLASHBANG,       13, 12);
		assignItemRect(SHOCK_BOMB,      10, 13);
		assignItemRect(HOLY_BOMB,       10, 13);
		assignItemRect(WOOLY_BOMB,      10, 13);
		assignItemRect(NOISEMAKER,      10, 13);
		assignItemRect(ARCANE_BOMB,     10, 13);
		assignItemRect(SHRAPNEL_BOMB,   10, 13);
	}


	private static final int KEYS          =                               xy(1, 6);   //16 slots

	public static final int IRON_KEY        = KEYS+0;
	public static final int GOLDEN_KEY      = KEYS+1;
	public static final int CRYSTAL_KEY     = KEYS+2;
	public static final int SKELETON_KEY    = KEYS+3;
	public static final int BRONZE_KEY      = KEYS+4;

	static {

		assignItemRect(IRON_KEY, 8, 14);
		assignItemRect(GOLDEN_KEY, 8, 14);
		assignItemRect(CRYSTAL_KEY, 8, 14);
		assignItemRect(SKELETON_KEY, 8, 14);
		assignItemRect(BRONZE_KEY, 8, 14);
	}

	private static final int AMULET          =                               xy(1, 7);   //16 slots
	public static final int MAIN_AMULET 	= AMULET+0;
	public static final int AMULET_EMPTY 	= AMULET+1;
	public static final int AMULET_FIRE 	= AMULET+2;
	public static final int AMULET_AIR 		= AMULET+3;
	public static final int AMULET_WATER 	= AMULET+4;
	public static final int AMULET_EARTH	= AMULET+5;

	private static final int FLASK          =                               xy(1, 8);   //16 slots
	public static int flask(int charges) {
		return FLASK + 4 - (int) GameMath.gate(0, charges, 4);
	}
	public static final int FLASK_CHARGE = FLASK + 5;

	private static final int BLUEPRINT		 =                               xy(1, 9);   //16 slots
	public static final int GUN_BLUEPRINT 		= BLUEPRINT+0;
	public static final int BOW_BLUEPRINT 		= BLUEPRINT+1;
	public static final int SWORD_BLUEPRINT 	= BLUEPRINT+2;
	public static final int CROSSBOW_BLUEPRINT 	= BLUEPRINT+3;

	public static class Weapons {

		private static final int WEAPON      =                               xy(1, 39);

		public static final int MAGES_STAFF 	 = WEAPON+0;
		public static final int SWORD       	 = WEAPON+1;
		public static final int SPEAR       	 = WEAPON+2;
		public static final int GLOVE       	 = WEAPON+3;
		public static final int AXE         	 = WEAPON+4;
		public static final int SCIMITAR         = WEAPON+5;
		public static final int SHORTSWORD		 = WEAPON+6;
		public static final int GREATSWORD       = WEAPON+7;
		public static final int CHAIN_WHIP		 = WEAPON+8;
		public static final int WHIP 			 = WEAPON+9;
		public static final int GREATAXE         = WEAPON+10;
		public static final int MACE 			 = WEAPON+11;
		public static final int FLAIL 			 = WEAPON+12;
		public static final int DAGGER			 = WEAPON+13;
		public static final int WAKIZASHI		 = WEAPON+14;
		public static final int RUNIC_BLADE		 = WEAPON+15;
		public static final int QUATERSTAFF		 = WEAPON+16;
		public static final int ROYAL_HALBERD	 = WEAPON+17;
		public static final int PLAIN_SWORD		 = WEAPON+18;
		public static final int INSCRIBED_KNIFE  = WEAPON+19;
		public static final int PITCHFORK		 = WEAPON+20;
		public static final int SICKLE           = WEAPON+21;
		public static final int HOE              = WEAPON+22;
		public static final int RAPIER           = WEAPON+23;
		public static final int BUTCHER_KNIFE    = WEAPON+24;
		public static final int HERO_SWORD		 = WEAPON+25;
		public static final int KATANA			 = WEAPON+26;
		public static final int TACHI			 = WEAPON+27;
		public static final int FALCHION		 = WEAPON+28;
		public static final int ESTOC			 = WEAPON+29;
		public static final int MIDNIGHT_CUTLASS = WEAPON+30;
	}

	public static class Shields {
		private static final int SHIELD 	=								 xy(1, 43);

		public static final int ROUND             = SHIELD+0;
		public static final int WOODEN            = SHIELD+1;
		public static final int SPIRITUAL         = SHIELD+2;
		public static final int PANIC         	  = SHIELD+3;//TODO: current sprite is WIP
		public static final int SWIFT         	  = SHIELD+4;
		public static final int GREAT         	  = SHIELD+5;
		public static final int SORCERER		  = SHIELD+6;
		public static final int LIGHT			  = SHIELD+7;
		public static final int BARBED         	  = SHIELD+8;
		public static final int WARP			  = SHIELD+9;//TODO: Now unused
		public static final int RUNIC	          = SHIELD+10;
		public static final int REINFORCED        = SHIELD+11;
		public static final int ROYAL             = SHIELD+12;
		public static final int DEMONIC           = SHIELD+13;//TODO: Unused
		public static final int HERO              = SHIELD+14;
		public static final int PESTILENT		  = SHIELD+15;
		public static final int LACERATING        = SHIELD+16;
		public static final int TITAN 			  = SHIELD+17;
		public static final int DAGGER 			  = SHIELD+18;
		public static final int REFLEX 			  = SHIELD+19;
	}

	public static class Wands {
		private static final int WANDS_ROW_1 =                           xy(1, 47);
		private static final int WANDS_ROW_2 =                           xy(1, 48);

		public static final int MAGIC_MISSILE  = WANDS_ROW_1+0;
		public static final int FIREBLAST = WANDS_ROW_1+1;
		public static final int FROST          = WANDS_ROW_1+2;
		public static final int LIGHTNING      = WANDS_ROW_1+3;
		public static final int DECIMATION	   = WANDS_ROW_1+4;
		public static final int PRISMATIC_LIGHT= WANDS_ROW_1+5;
		public static final int CORROSION      = WANDS_ROW_1+6;
		public static final int LIVING_EARTH   = WANDS_ROW_1+7;
		public static final int BLAST_WAVE     = WANDS_ROW_1+8;
		public static final int CORRUPTION     = WANDS_ROW_1+9;
		public static final int WARDING        = WANDS_ROW_1+10;
		public static final int REGROWTH       = WANDS_ROW_1+11;
		public static final int TRANSFUSION    = WANDS_ROW_1+12;
		public static final int FLOW           = WANDS_ROW_1+13;

		//TODO names for rest of WANDS + 14 to WANDS + 27
		public static final int SWARM 		   	   = WANDS_ROW_1+17;
		public static final int MALAISE 		   = WANDS_ROW_1+22;
		public static final int BINDING 		   = WANDS_ROW_1+24;
		public static final int WARPING 		   = WANDS_ROW_1+26;

		public static final int DISINTEGRATION 	 	= WANDS_ROW_1+28;
		public static final int SMITING	 		 	= WANDS_ROW_1+29;
		public static final int VOLTAGE	         	= WANDS_ROW_1+30;
		public static final int ACID                = WANDS_ROW_1+31;
		static {
			for (int i = WANDS_ROW_1; i < FLOW; i++)
				assignItemRect(i, 14, 14);
		}

		public static final int PLASMA              = WANDS_ROW_2+0;
		public static final int DARKNESS 			= WANDS_ROW_2+1;
		public static final int ICE_BARRIER         = WANDS_ROW_2+2;
		public static final int PERSUASION          = WANDS_ROW_2+3;
		public static final int DAMNATION           = WANDS_ROW_2+4;
		public static final int THORNVINES          = WANDS_ROW_2+5;
		public static final int LIFE_DRAIN          = WANDS_ROW_2+6;
		public static final int DEMONIC_MISSILE     = WANDS_ROW_2+7;
	}

	public static class Ranged {
		private static final int RANGED_ROW_1 =                           xy(1, 51);
		private static final int RANGED_AMMO =                           xy(1, 55);

		public static final int CROSSBOW = 			RANGED_ROW_1+0;
		public static final int SWIFT_CROSSBOW =    RANGED_ROW_1+1;
		public static final int HEAVY_PISTOL =    	RANGED_ROW_1+2;
		//..
		public static final int RIFLE 				= RANGED_ROW_1+4;
		public static final int PRESCISON_RIFLE		= RANGED_ROW_1+4;
		public static final int SPIRIT_BOW 			= RANGED_ROW_1+6;
		public static final int BOW	 				= RANGED_ROW_1+7;
		//..
		public static final int LONGBOW		 			= RANGED_ROW_1+9;
		//..
		public static final int MARKSMAN_BOW 			= RANGED_ROW_1+11;
		public static final int SHREDDER_CROSSBOW 		= RANGED_ROW_1+13;
		public static final int IMPACT_CROSSBOW 		= RANGED_ROW_1+14;


		public static final int BOLT 			= RANGED_AMMO+0;
		public static final int ARROW 			= RANGED_AMMO+1;
		public static final int BULLET			= RANGED_AMMO+2;
		public static final int SPIRIT_ARROW 	= RANGED_AMMO+3;

	}

	public static class Armors {

		private static final int ARMOR = xy(1, 46);

		public static final int CLOTH             = ARMOR+0;
		public static final int LEATHER           = ARMOR+1;
		public static final int MAIL              = ARMOR+2;
		public static final int SCALE             = ARMOR+3;
		public static final int PLATE             = ARMOR+4;
		public static final int WARRIOR           = ARMOR+5;
		public static final int MAGE              = ARMOR+6;
		public static final int ROGUE             = ARMOR+7;
		public static final int HUNTRESS          = ARMOR+8;
		public static final int STUDDED           = ARMOR+9;
		public static final int HIDE			  = ARMOR+10;
		public static final int BANDED			  = ARMOR+11;
		public static final int CHAINMAIL_ELVISH  = ARMOR+12;
		public static final int CHAINMAIL_DWARVISH= ARMOR+13;
		public static final int LIGHT             = ARMOR+14;
		public static final int RINGMAIL 		  = ARMOR+15;
		public static final int DISC              = ARMOR+16;
		public static final int ICE 			  = ARMOR+17;
		public static final int PRIESTESS         = ARMOR+18;

		static{
			assignItemRect(CLOTH,     15, 16);
			assignItemRect(MAGE,      15, 15);
			assignItemRect(ROGUE,     14, 12);
			assignItemRect(HUNTRESS,  13, 15);
		}
	}

	//8 free slots

	private static final int MISSILE_WEP    =                               xy(1, 10);  //16 slots. 3 per tier + boomerang
	
	public static final int DART            = MISSILE_WEP+1;
	public static final int THROWING_KNIFE  = MISSILE_WEP+2;
	public static final int THROWING_STONE  = MISSILE_WEP+3;
	
	public static final int FISHING_SPEAR   = MISSILE_WEP+4;
	public static final int SHURIKEN        = MISSILE_WEP+5;
	public static final int THROWING_CLUB   = MISSILE_WEP+6;
	
	public static final int THROWING_SPEAR  = MISSILE_WEP+7;
	public static final int BOLAS           = MISSILE_WEP+8;
	public static final int KUNAI           = MISSILE_WEP+9;
	
	public static final int JAVELIN         = MISSILE_WEP+10;
	public static final int TOMAHAWK        = MISSILE_WEP+11;
	public static final int BOOMERANG       = MISSILE_WEP+12;
	
	public static final int TRIDENT         = MISSILE_WEP+13;
	public static final int THROWING_HAMMER = MISSILE_WEP+14;
	public static final int FORCE_CUBE      = MISSILE_WEP+15;
	
	static{
		
		assignItemRect(DART,            15, 15);
		assignItemRect(THROWING_KNIFE,  12, 13);
		assignItemRect(THROWING_STONE,  12, 10);
		
		assignItemRect(FISHING_SPEAR,   11, 11);
		assignItemRect(SHURIKEN,        12, 12);
		assignItemRect(THROWING_CLUB,   12, 12);
		
		assignItemRect(THROWING_SPEAR,  13, 13);
		assignItemRect(BOLAS,           15, 14);
		assignItemRect(KUNAI,           15, 15);
		
		assignItemRect(JAVELIN,         16, 16);
		assignItemRect(TOMAHAWK,        13, 13);
		assignItemRect(BOOMERANG,       14, 14);
		
		assignItemRect(TRIDENT,         16, 16);
		assignItemRect(THROWING_HAMMER, 12, 12);
		assignItemRect(FORCE_CUBE,      11, 12);
	}
	
	public static final int TIPPED_DARTS    =                               xy(1, 11);  //16 slots
	public static final int ROT_DART        = TIPPED_DARTS+0;
	public static final int INCENDIARY_DART = TIPPED_DARTS+1;
	public static final int ADRENALINE_DART = TIPPED_DARTS+2;
	public static final int HEALING_DART    = TIPPED_DARTS+3;
	public static final int CHILLING_DART   = TIPPED_DARTS+4;
	public static final int SHOCKING_DART   = TIPPED_DARTS+5;
	public static final int POISON_DART     = TIPPED_DARTS+6;
	public static final int SLEEP_DART      = TIPPED_DARTS+7;
	public static final int PARALYTIC_DART  = TIPPED_DARTS+8;
	public static final int HOLY_DART       = TIPPED_DARTS+9;
	public static final int DISPLACING_DART = TIPPED_DARTS+10;
	public static final int BLINDING_DART   = TIPPED_DARTS+11;
	static {
		for (int i = TIPPED_DARTS; i < TIPPED_DARTS+16; i++)
			assignItemRect(i, 15, 15);
	}




	private static final int RINGS          =                               xy(1, 15);  //16 slots
	public static final int RING_GARNET     = RINGS+0;
	public static final int RING_RUBY       = RINGS+1;
	public static final int RING_TOPAZ      = RINGS+2;
	public static final int RING_EMERALD    = RINGS+3;
	public static final int RING_ONYX       = RINGS+4;
	public static final int RING_OPAL       = RINGS+5;
	public static final int RING_TOURMALINE = RINGS+6;
	public static final int RING_SAPPHIRE   = RINGS+7;
	public static final int RING_AMETHYST   = RINGS+8;
	public static final int RING_QUARTZ     = RINGS+9;
	public static final int RING_AGATE      = RINGS+10;
	public static final int RING_DIAMOND    = RINGS+11;
	static {
		for (int i = RINGS; i < RINGS+16; i++)
			assignItemRect(i, 8, 10);
	}

	private static final int ARTIFACTS          =                            xy(1, 16);  //32 slots
	public static final int ARTIFACT_CLOAK      = ARTIFACTS+0;
	public static final int ARTIFACT_ARMBAND    = ARTIFACTS+1;
	public static final int ARTIFACT_CAPE       = ARTIFACTS+2;
	public static final int ARTIFACT_TALISMAN   = ARTIFACTS+3;
	public static final int ARTIFACT_HOURGLASS  = ARTIFACTS+4;
	public static final int ARTIFACT_TOOLKIT    = ARTIFACTS+5;
	public static final int ARTIFACT_SPELLBOOK  = ARTIFACTS+6;
	public static final int ARTIFACT_BEACON     = ARTIFACTS+7;
	public static final int ARTIFACT_CHAINS     = ARTIFACTS+8;
	public static final int ARTIFACT_HORN1      = ARTIFACTS+9;
	public static final int ARTIFACT_HORN2      = ARTIFACTS+10;
	public static final int ARTIFACT_HORN3      = ARTIFACTS+11;
	public static final int ARTIFACT_HORN4      = ARTIFACTS+12;
	public static final int ARTIFACT_CHALICE1   = ARTIFACTS+13;
	public static final int ARTIFACT_CHALICE2   = ARTIFACTS+14;
	public static final int ARTIFACT_CHALICE3   = ARTIFACTS+15;
	public static final int ARTIFACT_SANDALS    = ARTIFACTS+16;
	public static final int ARTIFACT_SHOES      = ARTIFACTS+17;
	public static final int ARTIFACT_BOOTS      = ARTIFACTS+18;
	public static final int ARTIFACT_GREAVES    = ARTIFACTS+19;
	public static final int ARTIFACT_ROSE1      = ARTIFACTS+20;
	public static final int ARTIFACT_ROSE2      = ARTIFACTS+21;
	public static final int ARTIFACT_ROSE3      = ARTIFACTS+22;
	static{
		assignItemRect(ARTIFACT_CLOAK,      9,  15);
		assignItemRect(ARTIFACT_ARMBAND,    16, 13);
		assignItemRect(ARTIFACT_CAPE,       16, 14);
		assignItemRect(ARTIFACT_TALISMAN,   15, 13);
		assignItemRect(ARTIFACT_HOURGLASS,  13, 16);
		assignItemRect(ARTIFACT_TOOLKIT,    15, 13);
		assignItemRect(ARTIFACT_SPELLBOOK,  13, 16);
		assignItemRect(ARTIFACT_BEACON,     16, 16);
		assignItemRect(ARTIFACT_CHAINS,     16, 16);
		assignItemRect(ARTIFACT_HORN1,      15, 15);
		assignItemRect(ARTIFACT_HORN2,      15, 15);
		assignItemRect(ARTIFACT_HORN3,      15, 15);
		assignItemRect(ARTIFACT_HORN4,      15, 15);
		assignItemRect(ARTIFACT_CHALICE1,   12, 15);
		assignItemRect(ARTIFACT_CHALICE2,   12, 15);
		assignItemRect(ARTIFACT_CHALICE3,   12, 15);
		assignItemRect(ARTIFACT_SANDALS,    16, 6 );
		assignItemRect(ARTIFACT_SHOES,      16, 6 );
		assignItemRect(ARTIFACT_BOOTS,      16, 9 );
		assignItemRect(ARTIFACT_GREAVES,    16, 14);
		assignItemRect(ARTIFACT_ROSE1,      14, 14);
		assignItemRect(ARTIFACT_ROSE2,      14, 14);
		assignItemRect(ARTIFACT_ROSE3,      14, 14);
	}

	private static final int ALCOHOL        =                               xy(1, 18);  //16 slots
	public static final int BEER            = ALCOHOL+0;
	public static final int WHISKEY         = ALCOHOL+1;


	private static final int SCROLLS        =                               xy(1, 19);  //16 slots
	public static final int SCROLL_KAUNAN   = SCROLLS+0;
	public static final int SCROLL_SOWILO   = SCROLLS+1;
	public static final int SCROLL_LAGUZ    = SCROLLS+2;
	public static final int SCROLL_YNGVI    = SCROLLS+3;
	public static final int SCROLL_GYFU     = SCROLLS+4;
	public static final int SCROLL_RAIDO    = SCROLLS+5;
	public static final int SCROLL_ISAZ     = SCROLLS+6;
	public static final int SCROLL_MANNAZ   = SCROLLS+7;
	public static final int SCROLL_NAUDIZ   = SCROLLS+8;
	public static final int SCROLL_BERKANAN = SCROLLS+9;
	public static final int SCROLL_ODAL     = SCROLLS+10;
	public static final int SCROLL_TIWAZ    = SCROLLS+11;
	
	public static final int SCROLL_CATALYST = SCROLLS+13;
	static {
		for (int i = SCROLLS; i < SCROLLS+16; i++)
			assignItemRect(i, 15, 14);
		assignItemRect(SCROLL_CATALYST, 12, 11);
	}
	
	private static final int EXOTIC_SCROLLS =                               xy(1, 20);  //16 slots
	public static final int EXOTIC_KAUNAN   = EXOTIC_SCROLLS+0;
	public static final int EXOTIC_SOWILO   = EXOTIC_SCROLLS+1;
	public static final int EXOTIC_LAGUZ    = EXOTIC_SCROLLS+2;
	public static final int EXOTIC_YNGVI    = EXOTIC_SCROLLS+3;
	public static final int EXOTIC_GYFU     = EXOTIC_SCROLLS+4;
	public static final int EXOTIC_RAIDO    = EXOTIC_SCROLLS+5;
	public static final int EXOTIC_ISAZ     = EXOTIC_SCROLLS+6;
	public static final int EXOTIC_MANNAZ   = EXOTIC_SCROLLS+7;
	public static final int EXOTIC_NAUDIZ   = EXOTIC_SCROLLS+8;
	public static final int EXOTIC_BERKANAN = EXOTIC_SCROLLS+9;
	public static final int EXOTIC_ODAL     = EXOTIC_SCROLLS+10;
	public static final int EXOTIC_TIWAZ    = EXOTIC_SCROLLS+11;
	static {
		for (int i = EXOTIC_SCROLLS; i < EXOTIC_SCROLLS+16; i++)
			assignItemRect(i, 15, 14);
	}
	
	private static final int STONES             =                           xy(1, 21);  //16 slots
	public static final int STONE_PROTECT = STONES+0;
	public static final int STONE_AUGMENTATION  = STONES+1;
	public static final int STONE_AFFECTION     = STONES+2;
	public static final int STONE_BLAST         = STONES+3;
	public static final int STONE_BLINK         = STONES+4;
	public static final int STONE_CLAIRVOYANCE  = STONES+5;
	public static final int STONE_SLEEP         = STONES+6;
	public static final int STONE_DISARM        = STONES+7;
	public static final int STONE_ENCHANT       = STONES+8;
	public static final int STONE_FLOCK         = STONES+9;
	public static final int STONE_INTUITION     = STONES+10;
	public static final int STONE_SHOCK         = STONES+11;
	static {
		for (int i = STONES; i < STONES+16; i++)
			assignItemRect(i, 14, 12);
	}

	private static final int POTIONS        =                               xy(1, 22);  //16 slots
	public static final int POTION_CRIMSON  = POTIONS+0;
	public static final int POTION_AMBER    = POTIONS+1;
	public static final int POTION_GOLDEN   = POTIONS+2;
	public static final int POTION_JADE     = POTIONS+3;
	public static final int POTION_TURQUOISE= POTIONS+4;
	public static final int POTION_AZURE    = POTIONS+5;
	public static final int POTION_INDIGO   = POTIONS+6;
	public static final int POTION_MAGENTA  = POTIONS+7;
	public static final int POTION_BISTRE   = POTIONS+8;
	public static final int POTION_CHARCOAL = POTIONS+9;
	public static final int POTION_SILVER   = POTIONS+10;
	public static final int POTION_IVORY    = POTIONS+11;
	public static final int POTION_CATALYST = POTIONS+13;
	static {
		for (int i = POTIONS; i < POTIONS+16; i++)
			assignItemRect(i, 12, 14);
		assignItemRect(POTION_CATALYST, 6, 15);
	}
	
	private static final int EXOTIC_POTIONS =                               xy(1, 23);  //16 slots
	public static final int EXOTIC_CRIMSON  = EXOTIC_POTIONS+0;
	public static final int EXOTIC_AMBER    = EXOTIC_POTIONS+1;
	public static final int EXOTIC_GOLDEN   = EXOTIC_POTIONS+2;
	public static final int EXOTIC_JADE     = EXOTIC_POTIONS+3;
	public static final int EXOTIC_TURQUOISE= EXOTIC_POTIONS+4;
	public static final int EXOTIC_AZURE    = EXOTIC_POTIONS+5;
	public static final int EXOTIC_INDIGO   = EXOTIC_POTIONS+6;
	public static final int EXOTIC_MAGENTA  = EXOTIC_POTIONS+7;
	public static final int EXOTIC_BISTRE   = EXOTIC_POTIONS+8;
	public static final int EXOTIC_CHARCOAL = EXOTIC_POTIONS+9;
	public static final int EXOTIC_SILVER   = EXOTIC_POTIONS+10;
	public static final int EXOTIC_IVORY    = EXOTIC_POTIONS+11;
	static {
		for (int i = EXOTIC_POTIONS; i < EXOTIC_POTIONS+16; i++)
			assignItemRect(i, 12, 13);
	}

	private static final int SEEDS              =                           xy(1, 24);  //16 slots
	public static final int SEED_ROTBERRY       = SEEDS+0;
	public static final int SEED_FIREBLOOM      = SEEDS+1;
	public static final int SEED_SWIFTTHISTLE   = SEEDS+2;
	public static final int SEED_SUNGRASS       = SEEDS+3;
	public static final int SEED_ICECAP         = SEEDS+4;
	public static final int SEED_STORMVINE      = SEEDS+5;
	public static final int SEED_SORROWMOSS     = SEEDS+6;
	public static final int SEED_DREAMFOIL      = SEEDS+7;
	public static final int SEED_EARTHROOT      = SEEDS+8;
	public static final int SEED_STARFLOWER     = SEEDS+9;
	public static final int SEED_FADELEAF       = SEEDS+10;
	public static final int SEED_BLINDWEED      = SEEDS+11;
	static{
		for (int i = SEEDS; i < SEEDS+16; i++)
			assignItemRect(i, 10, 10);
	}
	
	private static final int BREWS          =                               xy(1, 25);  //8 slots
	public static final int BREW_INFERNAL   = BREWS+0;
	public static final int BREW_BLIZZARD   = BREWS+1;
	public static final int BREW_SHOCKING   = BREWS+2;
	public static final int BREW_CAUSTIC    = BREWS+3;
	public static final int BREW_HOLY_WATER = BREWS+4;
	
	private static final int ELIXIRS        =                               xy(9, 25);  //8 slots
	public static final int ELIXIR_HONEY    = ELIXIRS+0;
	public static final int ELIXIR_AQUA     = ELIXIRS+1;
	public static final int ELIXIR_MIGHT    = ELIXIRS+2;
	public static final int ELIXIR_DRAGON   = ELIXIRS+3;
	public static final int ELIXIR_TOXIC    = ELIXIRS+4;
	public static final int ELIXIR_ICY      = ELIXIRS+5;
	public static final int ELIXIR_ARCANE   = ELIXIRS+6;
	static{
		for (int i = BREWS; i < BREWS+16; i++)
			assignItemRect(i, 12, 14);
	}
	
	                                                                                    //16 free slots
	
	private static final int SPELLS         =                               xy(1, 27);  //16 slots
	public static final int MAGIC_PORTER    = SPELLS+0;
	public static final int PHASE_SHIFT     = SPELLS+1;
	public static final int WILD_ENERGY     = SPELLS+2;
	public static final int RETURN_BEACON   = SPELLS+3;
	public static final int DEGRADE 	    = SPELLS+4;
	public static final int AQUA_BLAST      = SPELLS+5;
	public static final int FEATHER_FALL    = SPELLS+6;
	public static final int RECLAIM_TRAP    = SPELLS+7;
	public static final int SAFE_UPGRADE    = SPELLS+8;
	public static final int CURSE_INFUSE    = SPELLS+9;
	public static final int MAGIC_INFUSE    = SPELLS+10;
	public static final int ALCHEMIZE       = SPELLS+11;
	public static final int RECYCLE         = SPELLS+12;
	public static final int ARCANE_INFUSION = SPELLS+13;
	public static final int SAFE_INFUSION   = SPELLS+14;
	static{
		assignItemRect(MAGIC_PORTER,    12, 11);
		assignItemRect(PHASE_SHIFT,     12, 11);
		assignItemRect(DEGRADE,         12, 11);
		assignItemRect(WILD_ENERGY,      8, 16);
		assignItemRect(RETURN_BEACON,    8, 16);
		assignItemRect(SAFE_INFUSION,    8, 16);
		
		assignItemRect(AQUA_BLAST,      11, 11);
		assignItemRect(FEATHER_FALL,    11, 11);
		assignItemRect(RECLAIM_TRAP,    11, 11);
		
		assignItemRect(CURSE_INFUSE,    10, 15);
		assignItemRect(MAGIC_INFUSE,    10, 15);
		assignItemRect(SAFE_UPGRADE,    10, 15);
		assignItemRect(ALCHEMIZE,       10, 15);
		assignItemRect(RECYCLE,         10, 15);
		assignItemRect(ARCANE_INFUSION, 10, 15);
	}
	
	private static final int FOOD       =                                   xy(1, 28);  //16 slots
	public static final int MEAT        = FOOD+0;
	public static final int STEAK       = FOOD+1;
	public static final int STEWED      = FOOD+2;
	public static final int OVERPRICED  = FOOD+3;
	public static final int CARPACCIO   = FOOD+4;
	public static final int RATION      = FOOD+5;
	public static final int PASTY       = FOOD+6;
	public static final int PUMPKIN_PIE = FOOD+7;
	public static final int CANDY_CANE  = FOOD+8;
	public static final int MEAT_PIE    = FOOD+9;
	public static final int BLANDFRUIT  = FOOD+10;
	public static final int BLAND_CHUNKS= FOOD+11;
	static{
		assignItemRect(MEAT,        15, 11);
		assignItemRect(STEAK,       15, 11);
		assignItemRect(STEWED,      15, 11);
		assignItemRect(OVERPRICED,  14, 11);
		assignItemRect(CARPACCIO,   15, 11);
		assignItemRect(RATION,      16, 12);
		assignItemRect(PASTY,       16, 11);
		assignItemRect(PUMPKIN_PIE, 16, 12);
		assignItemRect(CANDY_CANE,  13, 16);
		assignItemRect(MEAT_PIE,    16, 12);
		assignItemRect(BLANDFRUIT,  9,  12);
		assignItemRect(BLAND_CHUNKS,14, 6);
	}

	private static final int QUEST  =                                       xy(1, 29);  //32 slots
	public static final int SKULL   = QUEST+0;
	public static final int DUST    = QUEST+1;
	public static final int CANDLE  = QUEST+2;
	public static final int EMBER   = QUEST+3;
	public static final int PICKAXE = QUEST+4;
	public static final int ORE     = QUEST+5;
	public static final int TOKEN   = QUEST+6;
	public static final int BLOB    = QUEST+7;
	public static final int SHARD   = QUEST+8;
	static{
		assignItemRect(SKULL,   16, 11);
		assignItemRect(DUST,    12, 11);
		assignItemRect(CANDLE,  12, 12);
		assignItemRect(EMBER,   12, 11);
		assignItemRect(PICKAXE, 14, 14);
		assignItemRect(ORE,     15, 15);
		assignItemRect(TOKEN,   12, 12);
		assignItemRect(BLOB,    10,  9);
		assignItemRect(SHARD,    8, 10);
	}

	private static final int CLASS      =                                   xy(1, 30);  //16 slots
	public static final int BADGE   	= CLASS+0;
	public static final int SOUL        = CLASS+1;
	static {
		assignItemRect(SOUL, 12, 11);
	}

	private static final int BAGS       =                                   xy(1, 31);  //16 slots
	public static final int VIAL        = BAGS+0;
	public static final int POUCH       = BAGS+1;
	public static final int HOLDER      = BAGS+2;
	public static final int BANDOLIER   = BAGS+3;
	public static final int HOLSTER     = BAGS+4;
	static{
		assignItemRect(VIAL,        12, 12);
		assignItemRect(POUCH,       14, 15);
		assignItemRect(HOLDER,      16, 16);
		assignItemRect(BANDOLIER,   15, 16);
		assignItemRect(HOLSTER,     15, 16);
	}

	private static final int RELICWEPS         =                             xy(1, 32);  //16 slots
	public static final int LORSIONSGREATAXE   = RELICWEPS+0;
	public static final int NEPTUNES_TRIDENT   = RELICWEPS+1;
	public static final int MARACARS_BLADES    = RELICWEPS+2;
	public static final int LOTURGOS_CRYSTAL   = RELICWEPS+3;
	public static final int NAHUSSWORD         = RELICWEPS+4;
	public static final int RA_ROTHS_NUNCHUCKS = RELICWEPS+5;
	public static final int THONOTHS_AXE       = RELICWEPS+6;

	private static final int DRAGON_CRYSTALS         =                             xy(1, 33);  //16 slots
	public static final int WHITE_DRAGON_CRYSTAL            = DRAGON_CRYSTALS+0;
	public static final int YELLOW_DRAGON_CRYSTAL       	= DRAGON_CRYSTALS+1;
	public static final int RED_DRAGON_CRYSTAL      		= DRAGON_CRYSTALS+2;
	public static final int PURPLE_DRAGON_CRYSTAL      	    = DRAGON_CRYSTALS+3;
	public static final int LIGHT_BLUE_DRAGON_CRYSTAL	    = DRAGON_CRYSTALS+4;
	public static final int BLUE_DRAGON_CRYSTAL      		= DRAGON_CRYSTALS+5;
	public static final int GREEN_DRAGON_CRYSTAL       	    = DRAGON_CRYSTALS+6;
	public static final int LIGHT_GREEN_DRAGON_CRYSTAL      = DRAGON_CRYSTALS+7;
	public static final int BROWN_DRAGON_CRYSTAL   		  	= DRAGON_CRYSTALS+8;
	public static final int ADORNED_DRAGON_CRYSTAL    	    = DRAGON_CRYSTALS+9;

	private static final int POWERS          =                             xy(1, 35);
	public static int SPELLBOOK            = POWERS+0;
	public static int BLINK        	       = POWERS+3;
	public static final int SPECTRALBLADES = POWERS+10;
	public static final int ENERGIZE       = POWERS+6;

	private static final int POWERS2         =                             xy(1, 36);
	public static final int MOLTENEARTH    = POWERS2+0;
	public static final int GREED          = POWERS2+1;
	public static final int HEROICLEAP     = POWERS2+5;
	public static final int BUBBLESHIELD   = POWERS2+10;
	public static final int POISONBURST    = POWERS2+13;

	private static final int POWERS3         =                             xy(1, 37);
	public static final int TELEKINESIS    = POWERS3+1;
	public static final int WATERPUMP      = POWERS3+4;
	public static final int SURPRISE       = POWERS3+5;
	public static final int RAISEDEAD      = POWERS3+6;
	public static final int SMOKEBOMB      = POWERS3+7;


	//for smaller 8x8 icons that often accompany an item sprite
	public static class Icons {

		private static final int WIDTH = 16;
		public static final int SIZE = 8;

		public static TextureFilm film = new TextureFilm(Assets.Sprites.ITEM_ICONS, SIZE, SIZE);

		private static int xy(int x, int y) {
			x -= 1;
			y -= 1;
			return x + WIDTH * y;
		}

		private static void assignIconRect(int item, int width, int height) {
			int x = (item % WIDTH) * SIZE;
			int y = (item / WIDTH) * SIZE;
			film.add(item, x, y, x + width, y + height);
		}

		private static final int RINGS = xy(1, 1);  //16 slots
		public static final int RING_ACCURACY   = RINGS+0;
		public static final int RING_ELEMENTS   = RINGS+1;
		public static final int RING_ENERGY     = RINGS+2;
		public static final int RING_EVASION    = RINGS+3;
		public static final int RING_FORCE      = RINGS+4;
		public static final int RING_FUROR      = RINGS+5;
		public static final int RING_HASTE      = RINGS+6;
		public static final int RING_MIGHT      = RINGS+7;
		public static final int RING_SHARPSHOOT = RINGS+8;
		public static final int RING_TENACITY   = RINGS+9;
		public static final int RING_WEALTH     = RINGS+10;
		public static final int RING_UNUSED     = RINGS+11;
		static {
			assignIconRect( RING_ACCURACY,      7, 7 );
			assignIconRect( RING_ELEMENTS,      7, 7 );
			assignIconRect( RING_ENERGY,        7, 5 );
			assignIconRect( RING_EVASION,       7, 7 );
			assignIconRect( RING_FORCE,         5, 6 );
			assignIconRect( RING_FUROR,         7, 6 );
			assignIconRect( RING_HASTE,         6, 6 );
			assignIconRect( RING_MIGHT,         7, 7 );
			assignIconRect( RING_SHARPSHOOT,    7, 7 );
			assignIconRect( RING_TENACITY,      6, 6 );
			assignIconRect( RING_WEALTH,        7, 6 );
		}

		private static final int SCROLLS = xy(1, 3);  //16 slots
		public static final int SCROLL_UPGRADE = SCROLLS + 0;
		public static final int SCROLL_IDENTIFY = SCROLLS + 1;
		public static final int SCROLL_REMCURSE = SCROLLS + 2;
		public static final int SCROLL_MIRRORIMG = SCROLLS + 3;
		public static final int SCROLL_RECHARGE = SCROLLS + 4;
		public static final int SCROLL_TELEPORT = SCROLLS + 5;
		public static final int SCROLL_LULLABY = SCROLLS + 6;
		public static final int SCROLL_MAGICMAP = SCROLLS + 7;
		public static final int SCROLL_RAGE = SCROLLS + 8;
		public static final int SCROLL_RETRIB = SCROLLS + 9;
		public static final int SCROLL_TERROR = SCROLLS + 10;
		public static final int SCROLL_TRANSMUTE = SCROLLS + 11;

		static {
			assignIconRect(SCROLL_UPGRADE, 7, 7);
			assignIconRect(SCROLL_IDENTIFY, 4, 7);
			assignIconRect(SCROLL_REMCURSE, 7, 7);
			assignIconRect(SCROLL_MIRRORIMG, 7, 5);
			assignIconRect(SCROLL_RECHARGE, 7, 5);
			assignIconRect(SCROLL_TELEPORT, 7, 7);
			assignIconRect( SCROLL_LULLABY,     7, 6 );
			assignIconRect(SCROLL_MAGICMAP, 7, 7);
			assignIconRect( SCROLL_RAGE,        6, 6 );
			assignIconRect(SCROLL_RETRIB, 5, 6);
			assignIconRect(SCROLL_TERROR, 5, 7);
			assignIconRect(SCROLL_TRANSMUTE, 7, 7);
		}

		private static final int EXOTIC_SCROLLS = xy(1, 4);  //16 slots
		public static final int SCROLL_ENCHANT = EXOTIC_SCROLLS + 0;
		public static final int SCROLL_DIVINATE = EXOTIC_SCROLLS + 1;
		public static final int SCROLL_ANTIMAGIC = EXOTIC_SCROLLS + 2;
		public static final int SCROLL_PRISIMG = EXOTIC_SCROLLS + 3;
		public static final int SCROLL_MYSTENRG = EXOTIC_SCROLLS + 4;
		public static final int SCROLL_PASSAGE = EXOTIC_SCROLLS + 5;
		public static final int SCROLL_AFFECTION = EXOTIC_SCROLLS + 6;
		public static final int SCROLL_FORESIGHT = EXOTIC_SCROLLS + 7;
		public static final int SCROLL_CONFUSION = EXOTIC_SCROLLS + 8;
		public static final int SCROLL_PSIBLAST = EXOTIC_SCROLLS + 9;
		public static final int SCROLL_PETRIF = EXOTIC_SCROLLS + 10;
		public static final int SCROLL_POLYMORPH = EXOTIC_SCROLLS + 11;

		static {
			assignIconRect(SCROLL_ENCHANT, 7, 7);
			assignIconRect( SCROLL_DIVINATE,    7, 6 );
			assignIconRect(SCROLL_ANTIMAGIC, 7, 7);
			assignIconRect(SCROLL_PRISIMG, 5, 7);
			assignIconRect(SCROLL_MYSTENRG, 7, 5);
			assignIconRect(SCROLL_PASSAGE, 5, 7);
			assignIconRect(SCROLL_AFFECTION, 7, 6);
			assignIconRect(SCROLL_FORESIGHT, 7, 5);
			assignIconRect( SCROLL_CONFUSION,   7, 7 );
			assignIconRect(SCROLL_PSIBLAST, 5, 6);
			assignIconRect(SCROLL_PETRIF, 7, 5);
			assignIconRect(SCROLL_POLYMORPH, 7, 6);
		}

		//16 free slots

		private static final int POTIONS = xy(1, 6);  //16 slots
		public static final int POTION_STRENGTH = POTIONS + 0;
		public static final int POTION_RESTORATION = POTIONS + 1;
		public static final int POTION_MINDVIS = POTIONS + 2;
		public static final int POTION_FROST = POTIONS + 3;
		public static final int POTION_LIQFLAME = POTIONS + 4;
		public static final int POTION_TOXICGAS = POTIONS + 5;
		public static final int POTION_HASTE = POTIONS + 6;
		public static final int POTION_INVIS = POTIONS + 7;
		public static final int POTION_LEVITATE = POTIONS + 8;
		public static final int POTION_PARAGAS = POTIONS + 9;
		public static final int POTION_PURITY = POTIONS + 10;
		public static final int POTION_EXP = POTIONS + 11;

		static {
			assignIconRect(POTION_STRENGTH, 7, 7);
			assignIconRect(POTION_RESTORATION, 6, 7);
			assignIconRect(POTION_MINDVIS, 7, 5);
			assignIconRect( POTION_FROST,       7, 7 );
			assignIconRect(POTION_LIQFLAME, 5, 7);
			assignIconRect(POTION_TOXICGAS, 7, 7);
			assignIconRect(POTION_HASTE, 6, 6);
			assignIconRect(POTION_INVIS, 5, 7);
			assignIconRect( POTION_LEVITATE,    6, 7 );
			assignIconRect(POTION_PARAGAS, 7, 7);
			assignIconRect( POTION_PURITY,      5, 7 );
			assignIconRect( POTION_EXP,         7, 7 );
		}

		private static final int EXOTIC_POTIONS = xy(1, 7);  //16 slots
		public static final int POTION_ARENSURGE = EXOTIC_POTIONS + 0;
		public static final int POTION_SHIELDING = EXOTIC_POTIONS + 1;
		public static final int POTION_MAGISIGHT = EXOTIC_POTIONS + 2;
		public static final int POTION_SNAPFREEZ = EXOTIC_POTIONS + 3;
		public static final int POTION_DRGBREATH = EXOTIC_POTIONS + 4;
		public static final int POTION_CORROGAS = EXOTIC_POTIONS + 5;
		public static final int POTION_STAMINA = EXOTIC_POTIONS + 6;
		public static final int POTION_SHROUDFOG = EXOTIC_POTIONS + 7;
		public static final int POTION_STRMCLOUD = EXOTIC_POTIONS + 8;
		public static final int POTION_EARTHARMR = EXOTIC_POTIONS + 9;
		public static final int POTION_CLEANSE = EXOTIC_POTIONS + 10;
		public static final int POTION_HOLYFUROR = EXOTIC_POTIONS + 11;

		static {
			assignIconRect( POTION_ARENSURGE,   7, 7 );
			assignIconRect( POTION_SHIELDING,   6, 6 );
			assignIconRect( POTION_MAGISIGHT,   7, 5 );
			assignIconRect( POTION_SNAPFREEZ,   7, 7 );
			assignIconRect( POTION_DRGBREATH,   7, 7 );
			assignIconRect( POTION_CORROGAS,    7, 7 );
			assignIconRect( POTION_STAMINA,     6, 6 );
			assignIconRect( POTION_SHROUDFOG,   7, 7 );
			assignIconRect( POTION_STRMCLOUD,   7, 7 );
			assignIconRect( POTION_EARTHARMR,   6, 6 );
			assignIconRect( POTION_CLEANSE,     7, 7 );
			assignIconRect( POTION_HOLYFUROR,   5, 7 );
		}
	}

}
