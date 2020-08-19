package com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Doom;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.items.KindOfWeapon;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.LorsionsGreataxe;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.RelicMeleeWeapon;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Random;

public class Damning extends RelicEnchantment {
    private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
    @Override
    public ItemSprite.Glowing glowing() {
        return BLACK;
    }
    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
        if (Random.Int(2) == 0) {
            Buff.affect(defender, Doom.class);
        } else {
            Buff.prolong(defender, Weakness.class, 5);
        }
        return damage;
    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        GameScene.selectCell(crush);
    }
    private CellSelector.Listener crush = new  CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            Char enemy;
            KindOfWeapon Axe = Dungeon.hero.belongings.getWeapon();
            if (target != null && Axe instanceof LorsionsGreataxe) {
                int cell = target;
                LorsionsGreataxe Greataxe = (LorsionsGreataxe) Axe;
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));
                enemy = Actor.findChar(cell);
                if (enemy != null) {
                    if (Dungeon.hero.canAttack(enemy)) {
                        Greataxe.prepare();
                        Dungeon.hero.sprite.attack(enemy.pos);
                        //Dungeon.hero.spendAndNext(Greataxe.speedFactor(Dungeon.hero));//This is enforced here so that augments make a difference
                        Damning.super.activate(Greataxe,Dungeon.hero);
                    } else {
                        GLog.negative( Messages.get(Damning.class, "short_reach") );
                    }
                } else {
                    GLog.warning( Messages.get(Damning.class, "no_enemy") );
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(Damning.class, "prompt");
        }
    };
}
