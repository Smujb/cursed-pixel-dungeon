package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.KindofMisc;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class DragonPendant extends Relic {

    {
        chargePerKill = 5;

        chargePerUse = 100;
    }

    private int dragonID;

    private static final String DRAGON_ID = "dragon_id";

    @Override
    protected void doActivate() {
        super.doActivate();
        doSummon(curUser);
    }

    @Override
    protected boolean critCondition(Char enemy) {
        return getDragon() != null;
    }

    private float chargeFactor() {
        if (curUser == null || !(curUser instanceof Hero)) return 1f;
        Hero hero = (Hero) curUser;
        if (canTypicallyUse(hero)) return 1f;
        int missingAttunement = encumbrance();
        return (float) Math.pow(0.8f, missingAttunement);
    }

    private void doSummon(Char ch) {
        if (charge < MAX_CHARGE) {
            GLog.warning(Messages.get(this, "no_charge"));
            return;
        }
        if (getDragon() != null) {
            GLog.warning("already_spawned");
            return;
        }
        ch.spend(1f);
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (int offset : PathFinder.NEIGHBOURS9) {
            int cell = ch.pos + offset;
            if (Dungeon.level.passable(cell) && Actor.findChar(cell) == null) {
                neighbors.add(cell);
            }
        }

        if (neighbors.size() > 0) {
            DragonPendant.Dragon dragon = Mob.spawnAt(dragonType(), Random.element(neighbors));
            if (dragon != null) {
                dragon.updatePendant(this);
                setDragon(dragon);
                charge = 0f;
                Item.updateQuickslot();
                GLog.positive(Messages.get(this, "dragon_spawned"));
            } else {
                GLog.negative(Messages.get(this, "spawn_failed"));
            }
        }
    }

    @Override
    public String desc() {
        String desc = super.desc() + "\n\n";
        Dragon dragon = getDragon();
        if (dragon != null) {
            desc += dragon.description();
        } else {
            desc += Messages.get(this, "no_dragon");
        }
        return desc;
    }

    public void setDragon(Dragon dragon) {
        if (dragon.getClass() == dragonType() && dragon.pendantType() == getClass()) {
            dragonID = dragon.id();
        }
    }

    public Dragon getDragon() {
        Actor actor = Actor.findById(dragonID);
        if (actor != null && actor.getClass() == dragonType() && ((Char)actor).isAlive()) {
            return ((Dragon)actor);
        } else {
            return null;
        }
    }

    @Override
    public Item upgrade() {
        super.upgrade();
        Dragon dragon = getDragon();
        if (dragon != null) {
            dragon.updatePendant(this);
        }
        return this;
    }

    @Override
    public boolean doEquip(Hero hero) {
        //Have to enforce this as the dragon looks through hero's miscs for a pendant of its type. This is because of the logic the game relies on for linking the pendant and dragon
        for (KindofMisc misc : hero.belongings.miscs) {
            if (misc != null && misc.getClass() == this.getClass()) {
                GLog.warning(Messages.get(this, "cannot_wear_two"));
                return false;
            }
        }
        return super.doEquip(hero);
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        bundle.put(DRAGON_ID, dragonID);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        dragonID = bundle.getInt(DRAGON_ID);
    }

    protected abstract Class<? extends Dragon> dragonType();

    @Override
    public int price() {
        int price = 75;
        if (cursed() && cursedKnown) {
            price /= 2;
        }
        if (levelKnown) {
            if (level() > 0) {
                price *= (level() + 1);
            } else if (level() < 0) {
                price /= (1 - level());
            }
        }
        if (price < 1) {
            price = 1;
        }
        return price;
    }

    protected static abstract class Dragon extends Mob {

        {
            alignment = Alignment.ALLY;

            WANDERING = new Following();
            state = HUNTING;
            intelligentAlly = true;
        }

        private int rangedAttackCooldown = 0;

        private static final String RANGED_ATTK_COOLDOWN = "ranged_cooldown";

        private void updatePendant(@NotNull DragonPendant pen) {
            level = 1 + pen.level();
            alignment = pen.curUser.alignment;
            updateHT(true);
        }

        @Override
        protected boolean act() {
            if (getPendant() == null) {
                alignment = Alignment.ENEMY;
                WANDERING = new Wandering();
            } else {
                alignment = Alignment.ALLY;
                WANDERING = new Following();
            }
            if (rangedAttackCooldown > 0) {
                rangedAttackCooldown--;
            }
            return super.act();
        }

        @Override
        public boolean canAttack(@NotNull Char enemy) {
            //When the hero doesn't have enough strength for the pendant, the dragon may refuse to attack.
            DragonPendant pendant = getPendant();
            if (pendant != null) {
                if (Random.Float() > pendant.chargeFactor()) {
                    return false;
                }
            }
            if (rangedAttackCooldown > 0) {
                range = 1;
                hasMeleeAttack = true;
            } else {
                range = 7;
                if (speed() > 1f) {
                    hasMeleeAttack = false;
                }
            }
            return super.canAttack(enemy);
        }

        @Override
        public void damage(int dmg, DamageSrc src) {
            super.damage(dmg, src);
            hasMeleeAttack = true;
        }

        @Override
        public boolean attack(Char enemy, boolean guaranteed, int dmg, DamageSrc src) {
            if (!hasMeleeAttack || range > 1) {
                rangedAttackCooldown = 25;
            }
            return super.attack(enemy, guaranteed, dmg, src);
        }

        private DragonPendant getPendant() {
            ArrayList<KindofMisc> crystals = Dungeon.hero.belongings.getMiscsOfType(pendantType());
            if (crystals.size() == 1 && crystals.get(0) instanceof DragonPendant) {
                return (DragonPendant) crystals.get(0);
            }
            return null;
        }

        @Override
        public void storeInBundle(Bundle bundle) {
            super.storeInBundle(bundle);
            bundle.put(RANGED_ATTK_COOLDOWN, rangedAttackCooldown);
        }

        @Override
        public void restoreFromBundle(Bundle bundle) {
            super.restoreFromBundle(bundle);
            rangedAttackCooldown = bundle.getInt(RANGED_ATTK_COOLDOWN);
        }

        @Override
        public String description() {
            return super.description() + "\n\n" + Messages.get(Dragon.class, "stats",
                    HP, HT,
                    normalMin(level), normalMax(level),
                    Messages.format( "%d%%", Math.round(accuracyFactor*100)),
                    Messages.format( "%d%%", Math.round(evasionFactor*100)),
                    Messages.format( "%d%%", Math.round(baseSpeed*100)),
                    rangedAttackCooldown);
        }

        protected abstract Class<? extends DragonPendant> pendantType();
    }
}
