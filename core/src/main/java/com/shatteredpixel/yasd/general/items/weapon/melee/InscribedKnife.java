package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Amok;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Doom;
import com.shatteredpixel.yasd.general.actors.hero.Belongings;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.hero.HeroSubClass;
import com.shatteredpixel.yasd.general.actors.mobs.Wraith;
import com.shatteredpixel.yasd.general.effects.particles.EnergyParticle;
import com.shatteredpixel.yasd.general.effects.particles.ShadowParticle;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.items.allies.DragonPendant;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.ui.QuickSlotButton;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class InscribedKnife extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.INSCRIBED_KNIFE;

        bones = false;

        usesTargeting = true;

        unique = true;

        statScaling.add(Hero.HeroStat.SUPPORT);
    }
    private float charge = 0;
    private static final int MAX_CHARGE = 40;

    private static final String AC_CURSE = "CURSE";
    private static final String AC_SUMMON = "SUMMON";
    public static final String CHARGE = "charge";
    private static final int CURSE_AMT = 30;
    private static final int SUMMON_AMT = 40;

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);

        bundle.put(CHARGE, charge);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        charge = bundle.getInt(CHARGE);
        super.restoreFromBundle(bundle);
    }

    private void charge(float amount) {
        charge += amount;
        charge = Math.min(charge, MAX_CHARGE);
    }

    private void loseCharge(float amount) {
        charge -= amount;
        charge = Math.max(charge,0);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        if (attacker instanceof Hero && ((Hero)attacker).subClass == HeroSubClass.MEDIC) {
            ((Hero)attacker).sprite.centerEmitter().burst( EnergyParticle.FACTORY, 15 );
            Belongings b = ((Hero) attacker).belongings;
            for (KindofMisc misc : b.miscs) {
                if (misc instanceof DragonPendant) {
                    ((DragonPendant)misc).charge(1);
                }
            }
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions( hero );
        actions.add(AC_CURSE);
        actions.add(AC_SUMMON);
        return actions;
    }

    @Override
    public void onHeroGainExp(float levelPercent, Hero hero) {
        if (this.isEquipped(Dungeon.hero)) {
            super.onHeroGainExp(levelPercent, hero);
            charge(1f + 0.1f*level());//Charge gained scales slowly with level
        }
    }

    @Override
    public String desc() {
        return super.desc() + "\n\n" + Messages.get(this, "charge_desc", (int)charge, MAX_CHARGE);
    }

    protected CellSelector.Listener curse = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            if (isEquipped(curUser)) {
                int cell = target;
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));
                Char enemy = Actor.findChar(cell);
                if (enemy != null) {
                    if (!enemy.isImmune(Grim.class)) {
                        enemy.die(new Char.DamageSrc(Element.SHADOW, new Grim()));
                    } else {
                        Buff.affect(enemy, Doom.class);
                    }
                    enemy.sprite.emitter().burst(ShadowParticle.CURSE, 6);
                    GLog.info( Messages.get(InscribedKnife.class, "curse_message") );
                    InscribedKnife.this.loseCharge(InscribedKnife.CURSE_AMT);
                } else {
                    GLog.info( Messages.get(InscribedKnife.class, "curse_fail") );
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(this, "prompt_curse");
        }
    };

    private CellSelector.Listener summon = new  CellSelector.Listener() {

        @Override
        public void onSelect(Integer target) {
            if (isEquipped(curUser)) {
                int cell = target;
                if (Actor.findChar(target) != null)
                    QuickSlotButton.target(Actor.findChar(target));
                else
                    QuickSlotButton.target(Actor.findChar(cell));
                Wraith w;
                if ((w = Wraith.spawnAt(cell)) != null) {
                    Buff.affect(w, Amok.class, 40f);
                    GLog.info(Messages.get(InscribedKnife.class, "summon_message"));
                    InscribedKnife.this.loseCharge(InscribedKnife.SUMMON_AMT);
                } else {
                    GLog.warning(Messages.get(InscribedKnife.class, "summon_fail"));
                }
            }
        }

        @Override
        public String prompt() {
            return Messages.get(this, "prompt_summon");
        }
    };

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        if (action.equals(AC_CURSE) && isEquipped(hero)) {
            if (charge >= CURSE_AMT) {
                GameScene.selectCell(curse);
            } else {
                GLog.info( Messages.get(this, "no_charge") );
            }
        } else if (action.equals(AC_SUMMON) && isEquipped(hero)) {
            if (charge >= SUMMON_AMT) {
                GameScene.selectCell(summon);
            } else {
                GLog.info( Messages.get(this, "no_charge") );
            }
        }
    }
}

