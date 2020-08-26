package com.shatteredpixel.yasd.general.items.shield;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
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
    }

    @Override
    public void affectEnemy(Char enemy, boolean parry) {
        if (curUser == null) return;
        if (Dungeon.level.adjacent(curUser.pos, enemy.pos)) {
            super.affectEnemy(enemy, parry);
        } else {
            int closest = -1;
            boolean[] passable = Dungeon.level.passable();

            for (int n : PathFinder.NEIGHBOURS9) {
                int c = enemy.pos + n;
                if (passable[c] && Actor.findChar( c ) == null
                        && (closest == -1 || (Dungeon.level.trueDistance(c, curUser.pos) < (Dungeon.level.trueDistance(closest, curUser.pos))))) {
                    closest = c;
                }
            }
            if (closest == -1) {
                GLog.negative(Messages.get(this, "cant_warp"));
            } else {
                curUser.sprite.visible = true;
                Blink.appear(curUser, closest);
                Dungeon.level.pressCell(closest);
                Dungeon.observe();
            }
        }
    }
}
