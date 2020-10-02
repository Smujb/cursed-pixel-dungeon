package com.shatteredpixel.yasd.general.ui.changelist;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.items.DewVial;
import com.shatteredpixel.yasd.general.items.allies.PoisonDragonPendant;
import com.shatteredpixel.yasd.general.items.allies.WaterDragonPendant;
import com.shatteredpixel.yasd.general.items.armor.curses.Bulk;
import com.shatteredpixel.yasd.general.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.yasd.general.items.spells.MagicalInfusion;
import com.shatteredpixel.yasd.general.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Kinetic;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.ChangesScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.NightmareSprite;
import com.shatteredpixel.yasd.general.sprites.RatSprite;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.shatteredpixel.yasd.general.ui.Icons;
import com.shatteredpixel.yasd.general.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class CursedChanges {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_0_4_Changes(changeInfos);
        add_0_3_Changes(changeInfos);
        add_old_Changes(changeInfos);
    }

    public static void add_0_4_Changes(ArrayList<ChangeInfo> changeInfos) {

    }

    public static void add_0_3_Changes(ArrayList<ChangeInfo> changeInfos) {

    }

    public static void add_old_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("Pre-rework Cursed", true, "Note: these changes may not be entirely accurate to current Cursed. Many things have changed since then.");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.MASTERY, null), "Tome of Mastery",
                "Hero starts with Tome if they have beaten game on that class."));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BADGE, null), "Lucky Badge",
                "Ring of Wealth and Alchemist's Toolkit have been reworked:\n" +
                        "_-_ No longer must be equipped\n" +
                        "_-_ Ring of Wealth replaced with SPS Lucky Badge gfx"));

        changes.addButton( new ChangeButton( new Image(Assets.Sprites.WARRIOR, 0, 90, 12, 15), "Starting Equipment",
                "Starting Equipment has received minor changes again:\n" +
                        "_-_ Hero starts with 3 Healing Potions and 1 Purity."));

        changes.addButton( new ChangeButton( new Image(Assets.Sprites.PRIESTESS, 0, 90, 12, 15), "New Class",
                "New Class added!\n" +
                        "_-_ Starts with Poison Dragon and unique Inscribed Knife\n" +
                        "_-_ Has subclasses Necromancer and Medic\n" +
                        "_-_ Can command Allies and has other perks to do with them."));

        changes.addButton( new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), "Bug Fixes",
                "Fixed more instances of 'Save File not Found'\n" +
                        "Fixed transmuting Tier 6 items crashing the game\n" +
                        "Fixed broken plant gfx for Earth Challenge [Beta Only]\n" +
                        "Fixed various bugs with Dragon Crystals"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.Armors.PLATE, AntiMagic.TEAL), "Anti Magic Glyph removed",
                "This glyph was far too powerful, especially alongside Ring of Elements."));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"),false,null);
        changes.hardlight( CharSprite.WARNING );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Sprites.SPINNER, 144, 0, 16, 16), "Bug Fixes",
                "Fixed many instances of 'Save File not Found'"));

        changes.addButton(new ChangeButton(new MagicalInfusion(),
                "Magical Infusion reworked:\n" +
                        "_-_ Now increases the tier of an item rather than upgrading it\n" +
                        "_-_ Enchantments are no longer lost from upgrades\n" +
                        "_-_ Tier 6 is the highest tier"));

        changes.addButton(new ChangeButton(new StatueSprite(), "Animated Statues",
                "Animated Statues buffed:\n" +
                        "_-_ Animated Statue HP scales at 10/depth insead of 5/depth.\n" +
                        "_-_ Equipment dropped has bonus upgrades scaling with depth.\n" +
                        "_-_ Hero is resistant to Grim in the case of a Statue with it."));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.Armors.HUNTRESS, null), "Huntress Reworked",
                "Major changes to Huntress:\n" +
                        "_-_ Spirit Bow can now be upgraded\n" +
                        "_-_ Scaling of Bow depends on hero level\n" +
                        "_-_ Bow starts at +1\n" +
                        "_-_ Huntress starts with 16 HP, scaling at 4 per level, to compensate\n" +
                        "_-_ Huntress can see enemies 5 blocks away, up from 2"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SANDALS, null), "Tall Grass",
                "Tall grass can now drop Stones."));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_AGATE, null), "Misc Slots",
                "Misc slots have been reworked so that Wands must be equipped. To compensate, the player now has 4 slots."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new WandOfWarding(),"Wand of Warding added!"));

        changes.addButton(new ChangeButton(new NightmareSprite(), "Water Challenge", "Water Challenge is now accessible in release APK!\n" +
                "_-_ New enemy 'Water Elemental' - Splits on hit, has good HP and does some magic damage\n" +
                "_-_ New enemy 'Goo' - functions like the boss Goo, but scaled up\n" +
                "_-_ New enemy 'Water Larva' - very low HP, but has massive melee damage\n" +
                "_-_ New enemy 'Magic Crab' - medium HP, flees from hero, shoots ice that slows them down."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_BISTRE),"Challenge updates!", "New \"Corrosion\" Challenge!\n" +
                "- All enemies spawn Corrosion + Vertigo gas\n" +
                "- Faith is my Armour challenge removed due to being too unfair"));

        changes.addButton(new ChangeButton(new WaterDragonPendant(), "New Dragons have ben added!\n" +
                "- Water Dragon - High Evasion, low HP, Fast, regens only in water\n" +
                "- Earthen Dragon - Low Evasion, normal HP and damage, old Entanglement procs when hit and roots enemies\n" +
                "- Vampiric Dragon - Low damage, no regen, but steals HP from enemies"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.Weapons.GREATSWORD, Kinetic.YELLOW), "Swift",
                "Added a new enchantment: Swift. This enchantment gives an item a chance not to consume a turn on use."));

        changes.addButton(new ChangeButton(new PoisonDragonPendant(),
                "A new misc type has been added: Allies. They can be summoned from crystals and will help the player."));



        changes.addButton( new ChangeButton(new DewVial(),
                "The Dew Vial now has the 'Water' functionality, which allows the player to turn several surrounding tiles into water. It will consume up to 5 dew drops and will be more powerful the more Dew the player has."));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_EMERALD, null), "Ring Of Wealth",
                "A few changes have been made to the Ring of Wealth:\n" +
                        "_-_ It no longer spawns naturally\n" +
                        "_-_ A +10 ROW spawns in the first shop for about 800 Gold. Better save up!\n" +
                        "_-_ It can be used to farm for Scrolls of Upgrade\n" +
                        "_-_ It now shows the chance to drop a Scroll of Upgrade in the description"));

        changes.addButton(new ChangeButton(Icons.get(Icons.WARNING), "Upgrade Limits",
                "All items in the game have been given upgrade limits:\n" +
                        "_-_ Most equipment is capped at +15.\n" +
                        "_-_ Wands can be upgraded to +20 by the Mage. Additionally, Battlemage can upgrade his staff to +25, but other wands are still capped at +20.\n" +
                        "_-_ Warrior, in turn, can upgrade armour higher. While most armour is capped at the regular level, if the Warrior has his Broken Seal attatched, the limit will be temporarily increased to +20. Warrior also can upgrade his Broken Seal to +5.\n" +
                        "_-_ Rogue can upgrade weapons more. They are capped at +20 for him and +15 for others\n" +
                        "_-_ Huntress can simply upgrade thrown weapons to +20 instead of +15. I may have to buff this in future, possibly to extend this bonus to the Ring of Sharpshooting as well."));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight( CharSprite.POSITIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.Sprites.KING, 1, 0, 14, 16), "Bosses buffed again",
                "_-_ DM300 now has 800 HP and resists large amounts of damage\n" +
                        "_-_ Dwarf King now has 800 HP and a larger arena. Skeletons also deal damage scaling with the number there are."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.TOMB),"Crypt Rooms Changed", "Crypt rooms have been rebalanced to include same upgrade scaling with depth as Animated Statues"));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_EMERALD, null), "Ring of Wealth changed",
                "Ring of Wealth dropped an annoying amount of +0 Loot. This has been changed:\n" +
                        "_-_ +0 armour/weapon can't drop anymore.\n" +
                        "_-_ If a Ring/Artifact would be dropped, a Wand or Dragon Crystal may be dropped instead.\n" +
                        "_-_ Food drops nerfed; now half as likely and always Mystery Meat or small Ration."));

        changes.addButton( new ChangeButton(new WandOfMagicMissile(),"Wand of Magic Missile can now be enchanted!"));


        changes.addButton(new ChangeButton(Icons.get(Icons.SKULL), "Enemies Buffed",
                "Enemies have been buffed at all levels. The amount of extra HP that they have been given scales with depth. This is mainly due to more Scrolls Of Upgrade being availible and the player having higher HP.\n\n" +
                        "_-_ Sewers enemies are unchanged, Prision enemies have 50% more HP, Caves enemies 75% more and enemies in the Dwarven City and Demon Halls have double HP\n"+
                        "_-_ A new Demon Halls enemy has been added: the Goo. It is a version of the first boss, edited to be challenging. Better watch out!\n"+
                        "_-_ Evil Eye's Deathgaze can also now destroy all terrain!\n" +
                        "_-_ Warlocks now attack every other turn but deal 30-40 DMG"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.Armors.PLATE, Bulk.BLACK), "Curses Rebalanced",
                "Most curses have been changed to be more viable:\n" +
                        "_-_ Metabolism now heals 5% of max HP instead of 4 dmg\n" +
                        "_-_ Anti Entropy now freezes the enemy for longer. It's also more viable due to the 'Water' Dew Vial function\n" +
                        "_-_ Bulk reduces all incoming damage by 15%\n" +
                        "_-_ Corrosion now activates more often, with a 50% chance\n" +
                        "_-_ Displacement now gives 5 turns of invisibility\n" +
                        "_-_ Multiplicity has not been changed yet\n" +
                        "_-_ Overgrowth has not been changed yet\n" +
                        "_-_ Stench now gives 5 turns of gas immunity. This is not enough to take no damage from the gas, but it's enough to buy you some escape time."));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.Weapons.SWORD, Bulk.BLACK), "Curses Rebalanced",
                "Most curses have been changed to be more viable:\n" +
                        "_-_ Annoying curse now amoks enemies and procs more often\n" +
                        "_-_ Displacing curse now puts enemies it teleports into a magical sleep\n" +
                        "_-_ Fragile curse now inflicts paralysis depending on the amount it has degraded\n" +
                        "_-_ Friendly curse now charms the enemy for longer than the player\n" +
                        "_-_ Polarized curse now does 0 dmg or 2.2x dmg\n" +
                        "_-_ Sacrificial curse now increases dmg depending on HP taken (imo still probably not viable, needs a bigger rework)\n" +
                        "_-_ Wayward curse hs not been changed yet"));




        changes.addButton( new ChangeButton(new Image(Assets.Sprites.KING, 1, 0, 14, 16), "Bosses changed",
                "_-_ All bosses now drop 2-4 Scrolls of Upgrade, with an average of 2.5\n\n" +
                        "_-_ All bosses have had their HP buffed to compensate for higher upgrade levels; Goo has 150 instead of 100, Tengu has 240 instead of 120, DM300 has 400 instead of 200, Dwarf King has 500 instead of 300 and Yog has 1500 instead of 300.\n\n" +
                        "_-_ More extensive reworks are planned, especially for Dwarf King and Tengu."));

        changes.addButton( new ChangeButton( new Image(Assets.Sprites.WARRIOR, 0, 90, 12, 15), "Starting Equipment",
                "Due to surviving the sewers being heavily dependant on RNG, all starting equipment has been buffed:\n\n" +
                        "_-_ Huntress starts with +1 gloves and +1 Spirit Bow.\n" +
                        "_-_ Mage starts with a +1 Staff of Disintegration\n" +
                        "_-_ Warrior starts with an equipped Ring of Might and +1 Leather Armour, as well as his trusty Shortsword with a free upgrade\n" +
                        "_-_ Rogue simply starts with his Dagger upgraded twice\n\n"+
                        "Additionally, the player's inventory now has 35 slots, up from 20, and a new bag to store food\n" +
                        "The Warrior also has more HP than other classes, and the Huntress less."));

        changes.addButton( new ChangeButton(new WandOfMagicMissile(),
                "A few wand balance changes have been made:\n" +
                        "_-_ Magic Missile now has doubled scaling, upgrading by +2/+5 instead of +1/+2\n" +
                        "_-_ Wand of Frost scaling buffed\n" +
                        "_-_ Wand of Blast Wave now inflicts Vertigo on targets\n" +
                        "_-_ Wand of Living Earth has been implemented directly from 0.7.4 with existing sprites. HP of guardian increased scaling.\n" +
                        "_-_ Transfusion self-shielding reduced, now charming scales with upgrades\n" +
                        "_-_ Most wands have had scaling buffed by +1 max damage to be able to compete with weapons. For example, Lightning is equal to tier-5 scaling."));


        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new RatSprite(), "Exp Limits reduced", "Enemies have had their exp limits reduced to prevent overleveling."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.Weapons.SWORD, Bulk.BLACK), "Grim and Vampiric",
                "Grim and Vampiric have been changed:\n" +
                        "_-_ Grim was far too powerful with fast weapons. For this reason, it's chance to proc is half the damage dealt out of the current enemy HP\n" +
                        "_-_ Vampiric has been replaced with Shielding - this enchantment functions the same but grants shielding instead of HP and procs more often."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.POTION_AZURE, null), "Potion of Healing nerfed",
                "Potion of Healing was far too effective. As a result, it now heals 1/3 of missing HP and shields for the same amount. This is instant, however."));

        changes.addButton(new ChangeButton(new ItemSprite(ItemSpriteSheet.RING_EMERALD, null), "Rings Nerfed",
                "Rings were far too powerful. No longer:\n" +
                        "_-_ All rings now scale linearly.\n" +
                        "_-_ They cap at sensible values. For example, +15 Furor grants 4x attack speed and +15 Elements provides 50% resistance.\n" +
                        "_-_ Ring of Tenacity has been removed from the game."));



    }

}