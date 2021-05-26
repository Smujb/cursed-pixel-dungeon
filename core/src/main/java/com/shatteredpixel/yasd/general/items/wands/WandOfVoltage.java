package com.shatteredpixel.yasd.general.items.wands;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.FlavourBuff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.weapon.melee.hybrid.MagesStaff;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Callback;

public class WandOfVoltage extends DamageWand {

    {
        image = ItemSpriteSheet.Wands.VOLTAGE;

        damageFactor = 0.75f;

        element = Element.SHOCK;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }

    private static final float SHOCK_DAMAGE_FACTOR = 0.2f;

    @Override
    public void onZap(Ballistica attack) {
        int pos = attack.collisionPos;
        Char target = Actor.findChar(pos);
        if (target != null) {
            hit(target);
            Buff.affect(target, Charged.class, Charged.DURATION);
            for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
                if (mob.buff(Charged.class) != null && mob != target) {
                    fx(new Ballistica(target.pos, mob.pos, Ballistica.WONT_STOP), new Callback() {
                        @Override
                        public void call() {
                            Char.DamageSrc src = new Char.DamageSrc(Element.SHOCK, WandOfVoltage.this);
                            mob.damage((int) (damageRoll()*SHOCK_DAMAGE_FACTOR), src);
                            target.damage((int) (damageRoll()*SHOCK_DAMAGE_FACTOR), src);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onHit(MagesStaff staff, Char attacker, Char defender, int damage) {
        Buff.affect(defender, Charged.class, Charged.DURATION*2f);
    }

    @Override
    public String statsDesc() {
        if (levelKnown) return Messages.get(this, "stats_desc", min(), max(), (int) (SHOCK_DAMAGE_FACTOR*100));
        else return Messages.get(this, "stats_desc", defaultMin(), defaultMax(), (int) (SHOCK_DAMAGE_FACTOR*100));
    }

    public static class Charged extends FlavourBuff {

        {
            announced = true;
        }

        public static final float DURATION = 10f;

        @Override
        public int icon() {
            return BuffIndicator.VERTIGO;
        }

        @Override
        public void tintIcon(Image icon) {
            icon.hardlight(0.2f, 0.6f, 1f);
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add( CharSprite.State.CHARGED );
            else if (target.invisible == 0) target.sprite.remove( CharSprite.State.CHARGED );
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc", dispTurns());
        }

        @Override
        public float iconFadePercent() {
            return Math.max(0, (DURATION - visualcooldown()) / DURATION);
        }
    }
}
