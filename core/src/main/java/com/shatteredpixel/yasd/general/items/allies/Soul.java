package com.shatteredpixel.yasd.general.items.allies;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Adrenaline;
import com.shatteredpixel.yasd.general.actors.buffs.Bless;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Corruption;
import com.shatteredpixel.yasd.general.actors.buffs.DeferredDeath;
import com.shatteredpixel.yasd.general.actors.buffs.Weakness;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.actors.mobs.Rat;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Soul extends Item {
    {
        image = ItemSpriteSheet.SOUL;
        unique = true;
        cursed = false;
    }
    private Class<? extends Mob> mob = Rat.class;

    private static final float TIME_TO_CONSUME = 3f;
    private static final float TIME_TO_SUMMON = 1f;
    private static final float TIME_TO_SACRIFICE = 1f;
    private static final float MINION_LIFETIME = 15f;

    private static final String AC_CONSUME = "consume";
    private static final String AC_SACRIFICE = "sacrifice";
    private static final String AC_SUMMON = "summon";
    private static final String MOB = "mob";

    @Override
    public ItemSprite.Glowing glowing() {
        try {
            return new ItemSprite.Glowing(mob.newInstance().sprite.blood());
        } catch (Exception e) {
            return new ItemSprite.Glowing(0x000000);
        }
    }

    @Override
    public ArrayList<String> actions(Hero hero) {
        ArrayList<String> actions = super.actions(hero);
        actions.add(AC_CONSUME);
        actions.add(AC_SACRIFICE);
        actions.add(AC_SUMMON);
        return actions;
    }

    public Soul setMob(Class<? extends Mob> type) {
        mob = type;
        return this;
    }

    public Soul setMob(Mob type) {
        return setMob(type.getClass());
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(MOB, mob);
        super.storeInBundle(bundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        mob = bundle.getClass(MOB);
        super.restoreFromBundle(bundle);
    }

    private void ACConsume(Hero hero) {
        detach(hero.belongings.backpack);
        hero.spend(TIME_TO_CONSUME);
        hero.busy();
        hero.sprite.operate( hero.pos );
        hero.next();
        hero.heal(hero.HT/5, true, true);
        Buff.affect(hero, Weakness.class, Weakness.DURATION);
    }

    private void ACSacrifice(Hero hero) {
        hero.spend(TIME_TO_SACRIFICE);
        hero.busy();
        hero.sprite.operate( hero.pos );
        hero.next();
        hero.damage(hero.HT/Random.NormalIntRange(5, 20), new Char.DamageSrc(Element.SHADOW, this));
        Buff.prolong(hero, Adrenaline.class, 1 + 2*Random.Int((int) Adrenaline.DURATION-1, (int)Adrenaline.DURATION));
        Buff.prolong(hero, Bless.class, Bless.DURATION);
    }

    private void ACSummon(Hero hero) {
        if (mob != null) {
            int pos = hero.pos;
            int newPos = -1;
            if (Actor.findChar( pos ) != null) {
                ArrayList<Integer> candidates = new ArrayList<Integer>();
                boolean[] passable = Dungeon.level.passable();

                for (int n : PathFinder.NEIGHBOURS8) {
                    int c = pos + n;
                    if (passable[c] && Actor.findChar( c ) == null) {
                        candidates.add( c );
                    }
                }

                newPos = candidates.size() > 0 ? Random.element( candidates ) : -1;
                if (newPos != -1) {
                    Mob toSpawn = Mob.spawnAt(mob, newPos);
                    if (toSpawn != null) {
                        Buff.affect(toSpawn, Corruption.class);
                        Buff.affect(toSpawn, DeferredDeath.class, MINION_LIFETIME);//Minion lasts 15 turns only
                        //toSpawn.followHero();
                        detach(hero.belongings.backpack);
                        hero.spend(TIME_TO_SUMMON);
                        hero.busy();
                        hero.sprite.operate( hero.pos );
                        hero.next();
                    }
                }
            }
        }
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    public void execute(Hero hero, String action) {
        super.execute(hero, action);
        switch (action) {
            case AC_CONSUME:
                ACConsume(hero);
                break;
            case AC_SACRIFICE:
                ACSacrifice(hero);
                break;
            case AC_SUMMON:
                ACSummon(hero);
                break;
        }
    }
}
