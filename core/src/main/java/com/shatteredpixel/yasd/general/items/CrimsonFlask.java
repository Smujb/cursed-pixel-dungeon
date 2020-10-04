package com.shatteredpixel.yasd.general.items;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Constants;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Healing;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.particles.BloodParticle;
import com.shatteredpixel.yasd.general.items.bags.Bag;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.shatteredpixel.yasd.general.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class CrimsonFlask extends Item {

    {
        cursed = false;
        cursedKnown = true;

        defaultAction = AC_DRINK;
    }

    public static final float HEAL_TIME = 3f;

    @Override
    public boolean isUpgradable() {
        return false;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    private static final int MAX_CHARGES = 4;
    private int charges = MAX_CHARGES;

    @Override
    public int image() {
        return ItemSpriteSheet.flask(charges);
    }

    @Override
    public void setupEmitters(ItemSprite sprite) {
        super.setupEmitters(sprite);
        Emitter emitter = emitter(sprite);
        emitter.pour(BloodParticle.FACTORY, 0.1f);
    }

    @Override
    public String status() {
        return charges + "/" + MAX_CHARGES;
    }

    private static String AC_DRINK = "drink";

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = new ArrayList<>();
        actions.add(AC_DRINK);
        return actions;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_DRINK)) {
            if (hero.hpPercent() <= 0.3f) {
                doDrink(hero);
            } else {
                CPDGame.runOnRenderThread(new Callback() {
                    @Override
                    public void call() {
                        CPDGame.scene().addToFront(new WndOptions(Messages.get(CrimsonFlask.this, "should_drink"), Messages.get(CrimsonFlask.this, "healing_waste"), Messages.get(CrimsonFlask.this, "yes"), Messages.get(CrimsonFlask.this, "no")) {
                            @Override
                            protected void onSelect(int index) {
                                super.onSelect(index);
                                if (index == 0) {
                                    doDrink(hero);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    public void doDrink(Char ch) {
        if (charges > 0) {
            charges--;
            Camera.main.shake(4f, 1f);
            GameScene.flash(Constants.Colours.PURE_WHITE);
            Sample.INSTANCE.play(Assets.Sounds.DRINK);
            Buff.affect(ch, Healing.class).setHeal(Math.round(ch.HT*0.75f), 0.05f, 0);
            PotionOfHealing.cure(ch);
            ch.busy();
            ch.spend(HEAL_TIME);
            if (ch instanceof Hero) {
                ch.sprite.operate(ch.pos);
            }
            updateQuickslot();
            GLog.positive(Messages.get(this, "heal"));
        } else {
            GLog.negative(Messages.get(this, "no_charges"));
        }
    }

    public static class Charge extends Item {

        {
            image = ItemSpriteSheet.FLASK_CHARGE;
        }

        @Override
        public boolean collect(Bag container, Char ch) {
            CrimsonFlask flask = ch.belongings.getItem(CrimsonFlask.class);
            if (flask != null) {
                flask.charges++;
                GLog.positive(Messages.get(this, "collect"));
                return true;
            }
            GLog.negative(Messages.get(this, "no_flask"));
            return false;
        }
    }
}
