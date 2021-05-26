package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Barrier;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.LifeLink;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Beam;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.watabou.utils.Callback;

public class WandOfBinding extends DamageWand {

    {
        image = ItemSpriteSheet.Wands.BINDING;

        damageFactor = 0.5f;

        element = Element.MAGICAL;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }

    @Override
    public void onZap(Ballistica attack) {
        Char ch = Actor.findChar(attack.collisionPos);
        int power = damageRoll()/2;
        if (ch != null) {
            if (ch.alignment == Char.Alignment.ALLY) {
                ch.heal(power, true, true);
            } else {
                hit(ch);
                Buff.affect(ch, LifeLink.class, 10f).object = curUser.id();
                Buff.affect(curUser, LifeLink.class, 10f).object = ch.id();
                Buff.affect(curUser, Barrier.class).setShield(power);
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Buff.affect(attacker, LifeLink.class, 5f).object = defender.id();
    }

    @Override
    protected void fx(Ballistica beam, Callback callback) {
        curUser.sprite.parent.add(
                new Beam.HealthRay(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(beam.collisionPos)));
        callback.call();
    }
}
