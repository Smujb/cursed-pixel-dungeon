package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;

public class Rapier extends MeleeWeapon {
    {
        image = ItemSpriteSheet.Weapons.RAPIER;

        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.5f;
        damageFactor = 0.8f;
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        Ballistica attack = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_CHARS | Ballistica.STOP_TERRAIN);
        if (attack.path.size() > 2) {
            int newDefPos = attack.path.get(2);
            int newAttkPos = defender.pos;
            moveChar(defender, newDefPos);
            moveChar(attacker, newAttkPos);
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected boolean hasProperties() {
        return true;
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "advance_enemy");
    }

    public static void moveChar(Char ch, int newPos) {
        int initialPos = ch.pos;
        if (Dungeon.level.solid(newPos) || Actor.findChar(newPos) != null || !Char.canOccupy(ch, Dungeon.level, newPos)) return;
        Actor.addDelayed(new Pushing(ch, ch.pos, newPos, new Callback() {
            public void call() {
                if (initialPos != ch.pos) {
                    //something caused movement before pushing resolved, cancel to be safe.
                    ch.sprite.place(ch.pos);
                    return;
                }
                ch.pos = newPos;
                Dungeon.level.occupyCell(ch);
                if (ch == Dungeon.hero) {
                    Dungeon.observe();
                }
            }
        }), -1);
    }
}
