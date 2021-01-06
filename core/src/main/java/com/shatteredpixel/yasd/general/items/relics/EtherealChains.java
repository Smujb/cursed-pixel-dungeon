package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Chains;
import com.shatteredpixel.yasd.general.effects.Pushing;
import com.shatteredpixel.yasd.general.mechanics.Ballistica;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.shatteredpixel.yasd.general.tiles.DungeonTilemap;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;

public class EtherealChains extends Relic {

    {
        image = ItemSpriteSheet.ARTIFACT_CHAINS;
    }

    @Override
    protected void doActivate() {
        GameScene.selectCell(caster);
    }

    private CellSelector.Listener caster = new CellSelector.Listener(){

        @Override
        public void onSelect(Integer target) {
            if (target != null && (Dungeon.level.visited[target] || Dungeon.level.mapped[target])){

                //chains cannot be used to go where it is impossible to walk to
                PathFinder.buildDistanceMap(target, BArray.or(Dungeon.level.passable(), Dungeon.level.avoid(), null));
                if (PathFinder.distance[curUser.pos] == Integer.MAX_VALUE){
                    GLog.warning( Messages.get(EtherealChains.class, "cant_reach") );
                    return;
                }

                final Ballistica chain = new Ballistica(curUser.pos, target, Ballistica.STOP_TARGET);

                if (Actor.findChar( chain.collisionPos ) != null){
                    chainEnemy( chain, curUser, Actor.findChar( chain.collisionPos ));
                } else {
                    chainLocation( chain, curUser );
                }
                throwSound();
                Sample.INSTANCE.play( Assets.Sounds.CHAINS );
            }

        }

        @Override
        public String prompt() {
            return Messages.get(EtherealChains.class, "prompt");
        }
    };

    //pulls an enemy to a position along the chain's xPos, as close to the hero as possible
    private void chainEnemy(Ballistica chain, final Char user, final Char enemy ){

        if (enemy.properties().contains(Char.Property.IMMOVABLE)) {
            GLog.warning( Messages.get(this, "cant_pull") );
            return;
        }

        int bestPos = -1;
        for (int i : chain.subPath(1, chain.dist)){
            //prefer to the earliest point on the xPos
            if (!Dungeon.level.solid(i)
                    && Actor.findChar(i) == null
                    && enemy.canOccupy(Dungeon.level, i)){
                bestPos = i;
                break;
            }
        }

        if (bestPos == -1) {
            GLog.info(Messages.get(this, "does_nothing"));
            return;
        }

        final int pulledPos = bestPos;

        int chargeUse = Dungeon.level.distance(enemy.pos, pulledPos)*2;
        if (chargeUse > charge) {
            GLog.warning( Messages.get(this, "no_charge") );
            return;
        } else {
            useCharge(chargeUse);
            updateQuickslot();
        }
        if (user instanceof Hero) {
            user.busy();
        }

        user.sprite.parent.add(new Chains(user.sprite.center(), enemy.sprite.center(), new Callback() {
            public void call() {
                Actor.add(new Pushing(enemy, enemy.pos, pulledPos, new Callback() {
                    public void call() {
                        Dungeon.level.occupyCell(enemy);
                    }
                }));
                enemy.pos = pulledPos;
                Dungeon.observe();
                GameScene.updateFog();
                user.spendAndNext(1f);
            }
        }));
    }

    //pulls the hero along the chain to the collosionPos, if possible.
    private void chainLocation( Ballistica chain, final Char user ){

        //don't pull if the collision spot is in a wall
        if (Dungeon.level.solid(chain.collisionPos)){
            GLog.info( Messages.get(this, "inside_wall"));
            return;
        }

        //don't pull if there are no solid objects next to the pull location
        boolean solidFound = false;
        for (int i : PathFinder.NEIGHBOURS8){
            if (Dungeon.level.solid(chain.collisionPos + i)){
                solidFound = true;
                break;
            }
        }
        if (!solidFound){
            GLog.info( Messages.get(EtherealChains.class, "nothing_to_grab") );
            return;
        }

        final int newHeroPos = chain.collisionPos;

        int chargeUse = Dungeon.level.distance(user.pos, newHeroPos);
        if (chargeUse > charge){
            GLog.warning( Messages.get(EtherealChains.class, "no_charge") );
            return;
        } else {
            useCharge(chargeUse);
            updateQuickslot();
        }
        if (user instanceof Hero) {
            user.busy();
        }
        user.sprite.parent.add(new Chains(user.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(newHeroPos), new Callback() {
            public void call() {
                Actor.add(new Pushing(user, user.pos, newHeroPos, new Callback() {
                    public void call() {
                        Dungeon.level.occupyCell(user);
                    }
                }));
                user.spendAndNext(1f);
                user.pos = newHeroPos;
                Dungeon.observe();
                GameScene.updateFog();
            }
        }));
    }

    @Override
    public String desc() {
        String desc = super.desc();

        if (isEquipped( Dungeon.hero )){
            desc += "\n\n";
            if (cursed())
                desc += Messages.get(this, "desc_cursed");
            else
                desc += Messages.get(this, "desc_equipped");
        }
        return desc;
    }
}
