package com.shatteredpixel.yasd.general.ui.changelist;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.items.allies.WaterDragonPendant;
import com.shatteredpixel.yasd.general.items.armor.glyphs.AntiMagic;
import com.shatteredpixel.yasd.general.items.wands.WandOfMagicMissile;
import com.shatteredpixel.yasd.general.items.wands.WandOfWarding;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.ChangesScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.sprites.NightmareSprite;
import com.shatteredpixel.yasd.general.sprites.RatSprite;
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

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight( CharSprite.NEGATIVE );
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new RatSprite(), "Exp Limits reduced", "Enemies have had their exp limits reduced to prevent overleveling."));
    }

}