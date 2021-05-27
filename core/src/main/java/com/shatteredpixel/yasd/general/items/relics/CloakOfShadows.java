package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.BuffIndicator;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;

import org.jetbrains.annotations.NotNull;

public class CloakOfShadows extends Relic {

    {
        image = ItemSpriteSheet.ARTIFACT_CLOAK;

        statScaling.add(Hero.HeroStat.EXECUTION);
    }

    private boolean stealthed = false;
    private Buff stealth = null;

    @Override
    protected void doActivate() {
        if (!stealthed){
            if (!isEquipped(curUser)) GLog.info( Messages.get(Relic.class, "need_to_equip") );
            else if (cursed())       GLog.info( Messages.get(this, "cursed") );
            else if (charge <= 0)  GLog.info( Messages.get(this, "no_charge") );
            else {
                stealthed = true;
                curUser.spend( 1f );
                curUser.busy();
                Sample.INSTANCE.play(Assets.Sounds.MELD);
                stealth = new CloakStealth();
                stealth.attachTo(curUser);
                if (curUser.sprite.parent != null) {
                    curUser.sprite.parent.add(new AlphaTweener(curUser.sprite, 0.4f, 0.4f));
                } else {
                    curUser.sprite.alpha(0.4f);
                }
                curUser.sprite.operate(curUser.pos);
            }
        } else {
            stealthed = false;
            stealth.detach();
            stealth = null;
            curUser.spend( 1f );
            if (curUser instanceof Hero) {
                curUser.sprite.operate(curUser.pos);
            }
        }
    }

    @Override
    public void activate(Char ch){
        super.activate(ch);
        if (stealthed) {
            stealth = new CloakStealth();
            stealth.attachTo(ch);
        }
    }

    @Override
    public boolean doUnequip(Char hero, boolean collect, boolean single) {
        if (super.doUnequip(hero, collect, single)){
            stealthed = false;
            return true;
        } else
            return false;
    }

    private static final String STEALTHED = "stealthed";

    @Override
    public void storeInBundle(  Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( STEALTHED, stealthed );
    }

    @Override
    public void restoreFromBundle(  Bundle bundle ) {
        super.restoreFromBundle(bundle);
        stealthed = bundle.getBoolean( STEALTHED );
    }

    @Override
    public int price() {
        return 0;
    }

    public class CloakStealth extends Buff {

        {
            type = buffType.POSITIVE;
        }

        int turnsToCost = 0;

        @Override
        public int icon() {
            return BuffIndicator.INVISIBLE;
        }

        @Override
        public float iconFadePercent() {
            return (5f - turnsToCost) / 5f;
        }

        @Override
        public boolean attachTo(@NotNull Char target ) {
            if (super.attachTo( target )) {
                target.invisible++;
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean act(){
            turnsToCost--;

            if (turnsToCost <= 0){
                charge -= 10;
                if (charge < 0) {
                    charge = 0;
                    detach();
                    GLog.warning(Messages.get(this, "no_charge"));
                    ((Hero) target).interrupt();
                }
                updateQuickslot();
            }

            spend( TICK );

            return true;
        }

        public void dispel(){
            updateQuickslot();
            detach();
        }

        @Override
        public void fx(boolean on) {
            if (on) target.sprite.add( CharSprite.State.INVISIBLE );
            else if (target.invisible == 0) target.sprite.remove( CharSprite.State.INVISIBLE );
        }

        @Override
        public String toString() {
            return Messages.get(this, "name");
        }

        @Override
        public String desc() {
            return Messages.get(this, "desc");
        }

        @Override
        public void detach() {
            if (target.invisible > 0)
                target.invisible--;
            stealthed = false;

            updateQuickslot();
            super.detach();
        }

        private static final String TURNSTOCOST = "turnsToCost";

        @Override
        public void storeInBundle( Bundle bundle) {
            super.storeInBundle(bundle);

            bundle.put( TURNSTOCOST , turnsToCost);
        }

        @Override
        public void restoreFromBundle( Bundle bundle) {
            super.restoreFromBundle(bundle);

            turnsToCost = bundle.getInt( TURNSTOCOST );
        }
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return stealthed;
    }
}
