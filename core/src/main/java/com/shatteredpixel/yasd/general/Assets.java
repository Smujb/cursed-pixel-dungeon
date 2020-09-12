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

public class Assets {

	public static class Effects {
		public static final String EFFECTS		= "effects/effects.png";
		public static final String FIREBALL		= "effects/fireball.png";
		public static final String SPECKS		= "effects/specks.png";
		public static final String SPELL_ICONS	= "effects/spell_icons.png";
	}

	public static class Environment {
		public static final String TERRAIN_FEATURES	= "environment/terrain_features.png";

		public static final String VISUAL_GRID	= "environment/visual_grid.png";
		public static final String WALL_BLOCKING= "environment/wall_blocking.png";

		public static final String TILES_SEWERS	= "environment/tiles_sewers.png";
		public static final String TILES_PRISON	= "environment/tiles_prison.png";
		public static final String TILES_CAVES	= "environment/tiles_caves.png";
		public static final String TILES_CITY	= "environment/tiles_city.png";
		public static final String TILES_HALLS	= "environment/tiles_halls.png";
		public static final String TILES_HEAVEN = "environment/tiles_heaven.png";
		public static final String TILES_WATER_TRIAL = "environment/tiles_water_challenge.png";
		public static final String TILES_EARTH_TRIAL = "environment/tiles_earth_challenge.png";
		public static final String TILES_AIR_TRIAL = "environment/tiles_temple.png";

		public static final String WATER_SEWERS	= "environment/water0.png";
		public static final String WATER_PRISON	= "environment/water1.png";
		public static final String WATER_CAVES	= "environment/water2.png";
		public static final String WATER_CITY	= "environment/water3.png";
		public static final String WATER_HALLS	= "environment/water4.png";
		public static final String WATER_HEAVEN	= "environment/water_heaven.png";

		public static class Custom {
			public static final String WEAK_FLOOR = "environment/custom_tiles/weak_floor.png";
			public static final String SEWER_BOSS = "environment/custom_tiles/sewer_boss.png";
			public static final String PRISON_QUEST = "environment/custom_tiles/prison_quests.png";
			public static final String PRISON_EXIT_OLD = "environment/custom_tiles/prison_exit_old.png";
			public static final String PRISON_EXIT_NEW = "environment/custom_tiles/prison_exit_new.png";
			public static final String CAVES_BOSS = "environment/custom_tiles/caves_boss.png";
			public static final String CITY_BOSS = "environment/custom_tiles/city_boss.png";
			public static final String HALLS_SP = "environment/custom_tiles/halls_special.png";
		}
	}

	//TODO include other font assets here? Some are platform specific though...
	public static class Fonts {
		public static final String PIXELFONT= "fonts/pixel_font.png";
	}

	public static class Interfaces {
		public static final String ARCS_BG  = "interfaces/arcs1.png";
		public static final String ARCS_FG  = "interfaces/arcs2.png";

		public static final String BANNERS	= "interfaces/banners.png";
		public static final String BADGES	= "interfaces/badges.png";
		public static final String LOCKED	= "interfaces/locked_badge.png";

		public static final String CHROME	= "interfaces/chrome.png";
		public static final String ICONS	= "interfaces/icons.png";
		public static final String STATUS	= "interfaces/status_pane.png";
		public static final String STATUS_DARK	 = "interfaces/status_pane_dark.png";
		public static final String MENU		= "interfaces/menu_button.png";
		public static final String HP_BAR	= "interfaces/hp_bar.png";
		public static final String MANA_BAR = "interfaces/morale_bar.png";
		public static final String STAMINA  	 = "interfaces/stamina_bar.png";
		public static final String SHLD_BAR = "interfaces/shield_bar.png";
		public static final String XP_BAR	= "interfaces/exp_bar.png";
		public static final String TOOLBAR	= "interfaces/toolbar.png";
		public static final String TOOLBAR_DARK  = "interfaces/toolbar_dark.png";
		public static final String SHADOW   = "interfaces/shadow.png";
		public static final String BOSSHP   = "interfaces/boss_hp.png";

		public static final String SURFACE	= "interfaces/surface.png";

		public static final String LOADING_SEWERS	= "interfaces/loading_sewers.png";
		public static final String LOADING_PRISON	= "interfaces/loading_prison.png";
		public static final String LOADING_CAVES	= "interfaces/loading_caves.png";
		public static final String LOADING_CITY	    = "interfaces/loading_city.png";
		public static final String LOADING_HALLS	= "interfaces/loading_halls.png";

		public static final String BUFFS_SMALL	= "interfaces/buffs.png";
		public static final String BUFFS_LARGE	= "interfaces/large_buffs.png";
		public static final String CONS_ICONS   = "interfaces/consumable_icons.png";
	}

	//these points to resource bundles, not raw asset files
	public static class Messages {
		public static final String ACTORS   = "messages/actors/actors";
		public static final String ITEMS    = "messages/items/items";
		public static final String JOURNAL  = "messages/journal/journal";
		public static final String LEVELS   = "messages/levels/levels";
		public static final String MISC     = "messages/misc/misc";
		public static final String PLANTS   = "messages/plants/plants";
		public static final String SCENES   = "messages/scenes/scenes";
		public static final String UI       = "messages/ui/ui";
		public static final String WINDOWS  = "messages/windows/windows";
	}

	public static class Music {
		public static final String TITLE_THEME   = "music/title_theme.mp3";
		public static final String CITY_THEME    = "music/city_theme.mp3";
		public static final String PRISONS_THEME = "music/prisons_theme.mp3";
		public static final String SURFACE		 = "music/surface.ogg";
	}

	public static class Sounds {
		public static final String CLICK	= "sounds/snd_click.mp3";
		public static final String BADGE	= "sounds/snd_badge.mp3";
		public static final String GOLD		= "sounds/snd_gold.mp3";

		public static final String OPEN		= "sounds/snd_door_open.mp3";
		public static final String UNLOCK	= "sounds/snd_unlock.mp3";
		public static final String ITEM		= "sounds/snd_item.mp3";
		public static final String DEWDROP	= "sounds/snd_dewdrop.mp3";
		public static final String STEP		= "sounds/snd_step.mp3";
		public static final String WATER	= "sounds/snd_water.mp3";
		public static final String GRASS	= "sounds/grass.mp3";
		public static final String TRAMPLE	= "sounds/trample.mp3";
		public static final String STURDY	= "sounds/sturdy.mp3";
		public static final String DESCEND	= "sounds/snd_descend.mp3";
		public static final String EAT		= "sounds/snd_eat.mp3";
		public static final String READ		= "sounds/snd_read.mp3";
		public static final String LULLABY	= "sounds/snd_lullaby.mp3";
		public static final String DRINK	= "sounds/snd_drink.mp3";
		public static final String SHATTER	= "sounds/snd_shatter.mp3";
		public static final String ZAP		= "sounds/snd_zap.mp3";
		public static final String LIGHTNING= "sounds/snd_lightning.mp3";
		public static final String LEVELUP	= "sounds/snd_levelup.mp3";
		public static final String DEATH	= "sounds/snd_death.mp3";
		public static final String CHALLENGE= "sounds/snd_challenge.mp3";
		public static final String CURSED	= "sounds/snd_cursed.mp3";
		public static final String TRAP		= "sounds/snd_trap.mp3";
		public static final String EVOKE	= "sounds/snd_evoke.mp3";
		public static final String TOMB		= "sounds/snd_tomb.mp3";
		public static final String ALERT	= "sounds/snd_alert.mp3";
		public static final String MELD		= "sounds/snd_meld.mp3";
		public static final String BOSS		= "sounds/snd_boss.mp3";
		public static final String BLAST	= "sounds/snd_blast.mp3";
		public static final String PLANT	= "sounds/snd_plant.mp3";
		public static final String RAY		= "sounds/snd_ray.mp3";
		public static final String BEACON	= "sounds/snd_beacon.mp3";
		public static final String TELEPORT	= "sounds/snd_teleport.mp3";
		public static final String CHARMS	= "sounds/snd_charms.mp3";
		public static final String MASTERY	= "sounds/snd_mastery.mp3";
		public static final String PUFF		= "sounds/snd_puff.mp3";
		public static final String ROCKS	= "sounds/snd_rocks.mp3";
		public static final String BURNING	= "sounds/snd_burning.mp3";
		public static final String FALLING	= "sounds/snd_falling.mp3";
		public static final String GHOST	= "sounds/snd_ghost.mp3";
		public static final String SECRET	= "sounds/snd_secret.mp3";
		public static final String BONES	= "sounds/snd_bones.mp3";
		public static final String BEE      = "sounds/snd_bee.mp3";
		public static final String DEGRADE  = "sounds/snd_degrade.mp3";
		public static final String MIMIC    = "sounds/snd_mimic.mp3";
		public static final String DEBUFF   = "sounds/debuff.mp3";
		public static final String CHARGEUP = "sounds/chargeup.mp3";
		public static final String GAS      = "sounds/gas.mp3";
		public static final String CHAINS   = "sounds/chains.mp3";

		public static final String HIT		        = "sounds/hit.mp3";
		public static final String MISS		        = "sounds/miss.mp3";
		public static final String HIT_SLASH        = "sounds/hit_slash.mp3";
		public static final String HIT_STAB         = "sounds/hit_stab.mp3";
		public static final String HIT_CRUSH        = "sounds/hit_crush.mp3";
		public static final String HIT_MAGIC        = "sounds/hit_magic.mp3";
		public static final String HIT_STRONG       = "sounds/hit_strong.mp3";
		public static final String HIT_PARRY        = "sounds/hit_parry.mp3";
		public static final String ATK_SPIRITBOW    = "sounds/atk_spiritbow.mp3";
		public static final String ATK_CROSSBOW     = "sounds/atk_crossbow.mp3";
		public static final String HEALTH_WARN      = "sounds/health_warn.mp3";
		public static final String HEALTH_CRITICAL  = "sounds/health_critical.mp3";
		public static final String HIT_ARROW        = "sounds/hit_arrow.mp3";


		public static final String[] all = new String[]{
				CLICK, BADGE, GOLD,
				OPEN, UNLOCK, ITEM, DEWDROP, HIT, MISS, STEP, WATER, GRASS, TRAMPLE, STURDY,
				DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, LIGHTNING, LEVELUP, DEATH,
				CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
				TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
				BEE, DEGRADE, MIMIC
		};

		/*public static final String[] all = new String[]{
				CLICK, BADGE, GOLD,

				OPEN, UNLOCK, ITEM, DEWDROP, STEP, WATER, GRASS, TRAMPLE, STURDY,

				HIT, MISS, HIT_SLASH, HIT_STAB, HIT_CRUSH, HIT_MAGIC, HIT_STRONG, HIT_PARRY,
				HIT_ARROW, ATK_SPIRITBOW, ATK_CROSSBOW, HEALTH_WARN, HEALTH_CRITICAL,

				DESCEND, EAT, READ, LULLABY, DRINK, SHATTER, ZAP, LIGHTNING, LEVELUP, DEATH,

				CHALLENGE, CURSED, TRAP, EVOKE, TOMB, ALERT, MELD, BOSS, BLAST, PLANT, RAY, BEACON,
				TELEPORT, CHARMS, MASTERY, PUFF, ROCKS, BURNING, FALLING, GHOST, SECRET, BONES,
				BEE, DEGRADE, MIMIC, DEBUFF, CHARGEUP, GAS, CHAINS
		};*/
	}

	public static class Splashes {
		public static final String WARRIOR	= "splashes/warrior.jpg";
		public static final String MAGE		= "splashes/mage.jpg";
		public static final String ROGUE	= "splashes/rogue.jpg";
		public static final String HUNTRESS	= "splashes/huntress.jpg";
		public static final String AVATARS	= "splashes/avatars.png";
	}

	public static class Sprites {
		public static final String ITEMS	    = "sprites/items.png";
		public static final String ITEM_ICONS   = "sprites/item_icons.png";

		public static final String WARRIOR	= "sprites/warrior.png";
		public static final String MAGE		= "sprites/mage.png";
		public static final String ROGUE	= "sprites/rogue.png";
		public static final String HUNTRESS	= "sprites/huntress.png";
		public static final String PRIESTESS= "sprites/priestess.png";
		public static final String PET		= "sprites/pet.png";
		public static final String AMULET	= "sprites/amulet.png";

		public static final String RAT		= "sprites/rat.png";
		public static final String BRUTE	= "sprites/brute.png";
		public static final String SPINNER	= "sprites/spinner.png";
		public static final String DM300	= "sprites/dm300.png";
		public static final String WRAITH	= "sprites/wraith.png";
		public static final String UNDEAD	= "sprites/undead.png";
		public static final String KING		= "sprites/king.png";
		public static final String PIRANHA	= "sprites/piranha.png";
		public static final String EYE		= "sprites/eye.png";
		public static final String GNOLL	= "sprites/gnoll.png";
		public static final String CRAB		= "sprites/crab.png";
		public static final String GOO		= "sprites/goo.png";
		public static final String SWARM	= "sprites/swarm.png";
		public static final String SKELETON	= "sprites/skeleton.png";
		public static final String SHAMAN	= "sprites/shaman.png";
		public static final String THIEF	= "sprites/thief.png";
		public static final String TENGU	= "sprites/tengu.png";
		public static final String SHEEP	= "sprites/sheep.png";
		public static final String KEEPER	= "sprites/shopkeeper.png";
		public static final String BAT		= "sprites/bat.png";
		public static final String ELEMENTAL= "sprites/elemental.png";
		public static final String MONK		= "sprites/monk.png";
		public static final String WARLOCK	= "sprites/warlock.png";
		public static final String GOLEM	= "sprites/golem.png";
		public static final String STATUE	= "sprites/statue.png";
		public static final String SUCCUBUS	= "sprites/succubus.png";
		public static final String SCORPIO	= "sprites/scorpio.png";
		public static final String FISTS	= "sprites/yog_fists.png";
		public static final String YOG		= "sprites/yog.png";
		public static final String LARVA	= "sprites/larva.png";
		public static final String GHOST	= "sprites/ghost.png";
		public static final String MAKER	= "sprites/wandmaker.png";
		public static final String TROLL	= "sprites/blacksmith.png";
		public static final String IMP		= "sprites/demon.png";
		public static final String RATKING	= "sprites/ratking.png";
		public static final String BEE      = "sprites/bee.png";
		public static final String MIMIC    = "sprites/mimic.png";
		public static final String ROT_LASH = "sprites/rot_lasher.png";
		public static final String ROT_HEART= "sprites/rot_heart.png";
		public static final String GUARD    = "sprites/guard.png";
		public static final String WARDS    = "sprites/wards.png";
		public static final String GUARDIAN	= "sprites/guardian.png";
		public static final String SLIME	= "sprites/slime.png";
		public static final String SNAKE	= "sprites/snake.png";
		public static final String NECRO	= "sprites/necromancer.png";
		public static final String GHOUL	= "sprites/ghoul.png";
		public static final String RIPPER	= "sprites/ripper.png";
		public static final String SPAWNER	= "sprites/spawner.png";
		public static final String DM100	= "sprites/dm100.png";
		public static final String PYLON	= "sprites/pylon.png";
		public static final String JELLYFISH= "sprites/jellyfish.png";
		public static final String DM200	= "sprites/dm200.png";
		public static final String THORNVINE= "sprites/mob_thornvine.png";
		public static final String LOTUS	= "sprites/lotus.png";
		public static final String DRAGONS       = "sprites/dragons.png";
		public static final String LIGHTNING_TOWER       = "sprites/littower.png";
		public static final String WATER_LARVA       = "sprites/water_larva.png";
		public static final String NIGHTMARE	       = "sprites/nightmare.png";
	}
}
