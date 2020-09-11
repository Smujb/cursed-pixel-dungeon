package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.GooWarn;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.effects.particles.ElmoParticle;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.CharSprite;
import com.shatteredpixel.yasd.general.sprites.GooSprite;
import com.shatteredpixel.yasd.general.utils.BArray;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WaterTrialGoo extends Mob {

    {
        spriteClass = GooSprite.WaterTrialGoo.class;

        properties.add(Property.DEMONIC);
        properties.add(Property.ACIDIC);
        EXP = 3;
        baseSpeed = 2f;
        damageFactor = 2/3f;
        healthFactor = 2f;
    }

    private int pumpedUp = 0;

    @Override
    public Element elementalType() {
        return Element.ACID;
    }

    @Override
    public int damageRoll() {
        if (pumpedUp > 0) {
            pumpedUp = 0;
            PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid(), null ), 2 );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE)
                    CellEmitter.get(i).burst(ElmoParticle.FACTORY, 10);
            }
            Sample.INSTANCE.play( Assets.Sounds.BURNING );
            return super.damageRoll() * 3;
        } else {
            return super.damageRoll();
        }
    }

    @Override
    public void damage(int dmg, DamageSrc src) {
        if (pumpedUp > 0) dmg /= 2;
        super.damage(dmg, src);
    }
    @Override
    public int attackSkill( Char target ) {
        int attack = 45;
        if (HP*2 <= HT) attack = 15;
        if (pumpedUp > 0) attack *= 2;
        return attack;
    }

    @Override
    public int defenseSkill(Char enemy) {
        return 20;
    }

    @Override
    public boolean act() {

        if (Dungeon.level.liquid(pos) && HP < HT) {
            sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );

            HP+= 3;
        }

        return super.act();
    }

    @Override
    public boolean canAttack(Char enemy) {
        return (pumpedUp > 0) ? distance( enemy ) <= 2 : super.canAttack(enemy);
    }

    @Override
    public int attackProc( Char enemy, int damage ) {
        damage = super.attackProc( enemy, damage );

        if (pumpedUp > 0) {
            Camera.main.shake( 3, 0.2f );
        }

        return damage;
    }

    @Override
    protected boolean doAttack( Char enemy ) {
        if (pumpedUp == 1) {
            ((GooSprite)sprite).pumpUp();
            PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid(), null ), 2 );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE)
                    GameScene.add(Blob.seed(i, 2, GooWarn.class));
            }
            pumpedUp++;

            spend( attackDelay() );

            return true;
        } else if (pumpedUp >= 2 || Random.Int( (HP*2 <= HT) ? 2 : 3     ) > 0) {

            boolean visible = Dungeon.level.heroFOV[pos];

            if (visible) {
                if (pumpedUp >= 2) {
                    ((GooSprite) sprite).pumpAttack();
                }
                else
                    sprite.attack( enemy.pos );
            } else {
                attack( enemy );
            }

            spend( attackDelay() );

            return !visible;

        } else {

            pumpedUp++;

            ((GooSprite)sprite).pumpUp();

            for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
                int j = pos + PathFinder.NEIGHBOURS9[i];
                if (!Dungeon.level.solid(j)) {
                    GameScene.add(Blob.seed(j, 2, GooWarn.class));
                }
            }

            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "!!!") );
                GLog.negative( Messages.get(this, "pumpup") );
            }

            spend( attackDelay() );

            return true;
        }
    }

    @Override
    public boolean attack(Char enemy, boolean guaranteed) {
        boolean result = super.attack( enemy, guaranteed);
        pumpedUp = 0;
        return result;
    }

    @Override
    protected boolean getCloser( int target ) {
        pumpedUp = 0;
        return super.getCloser( target );
    }

    @Override
    public void move( int step ) {
        super.move(step);
    }

    private final String PUMPEDUP = "pumpedup";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( PUMPEDUP , pumpedUp );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        super.restoreFromBundle( bundle );

        pumpedUp = bundle.getInt( PUMPEDUP );

    }
}
