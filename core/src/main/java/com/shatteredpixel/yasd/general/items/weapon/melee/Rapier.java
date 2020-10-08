package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
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
        damageFactor = 0.7f;

        statScaling.add(Hero.HeroStat.ASSAULT);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (!advance(attacker, defender)) {
            //40% extra damage if enemy is against a wall
            damage *= 1.4;
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected boolean hasProperties() {
        return true;
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "advance_enemy") + "\n" + Messages.get(this, "damage_wall", 40);
    }

    public static boolean advance(Char attacker, Char defender) {
        Ballistica attack = new Ballistica(attacker.pos, defender.pos, Ballistica.STOP_CHARS | Ballistica.STOP_TERRAIN);
        if (attack.path.size() > 2) {
            int newDefPos = attack.path.get(2);
            int newAttkPos = defender.pos;
            //Ensure the attacker only moves if the defender has.
            if (moveChar(defender, newDefPos, true)) {
                //Don't check for chars as previous char having moved won't have registered yet.
                moveChar(attacker, newAttkPos, false);
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean moveChar(Char ch, int newPos, boolean checkChars) {
        int initialPos = ch.pos;
        //Ensure destination is passable, and prevent large chars moving into spaces they can't move out of.
        if (Dungeon.level.solid(newPos) || (Actor.findChar(newPos) != null && checkChars) || !Char.canOccupy(ch, Dungeon.level, newPos)) return false;
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
        return true;
    }
}
