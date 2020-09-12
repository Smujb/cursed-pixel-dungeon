package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.items.powers.Blink;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.PathFinder;

public class WarpShield extends Shield {

    {
        image = ItemSpriteSheet.Shields.WARP;

        defenseMultiplier = 0.75f;
        chargePerTurn = 1f;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }

    @Override
    public int proc(Char attacker, Char enemy, int damage, boolean parry) {
        if (Dungeon.level.adjacent(attacker.pos, enemy.pos)) {
            super.proc(attacker, enemy, damage, parry);
        } else {
            int closest = -1;
            boolean[] passable = Dungeon.level.passable();

            for (int n : PathFinder.NEIGHBOURS9) {
                int c = enemy.pos + n;
                if (passable[c] && Actor.findChar( c ) == null
                        && (closest == -1 || (Dungeon.level.trueDistance(c, attacker.pos) < (Dungeon.level.trueDistance(closest, attacker.pos))))) {
                    closest = c;
                }
            }
            if (closest == -1) {
                GLog.negative(Messages.get(this, "cant_warp"));
            } else {
                attacker.sprite.visible = true;
                Blink.appear(attacker, closest);
                Dungeon.level.pressCell(closest);
                Dungeon.observe();
            }
        }
        return super.proc(attacker, enemy, damage, parry);
    }
}
