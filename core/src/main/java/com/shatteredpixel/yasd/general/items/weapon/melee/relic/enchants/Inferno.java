package com.shatteredpixel.yasd.general.items.weapon.melee.relic.enchants;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Actor;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.Fire;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.actors.buffs.Burning;
import com.shatteredpixel.yasd.general.actors.mobs.Mob;
import com.shatteredpixel.yasd.general.effects.particles.FlameParticle;
import com.shatteredpixel.yasd.general.items.weapon.melee.relic.RelicMeleeWeapon;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Inferno extends RelicEnchantment {

    private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF4400 );

    @Override
    public int relicProc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage ) {
        // lvl 0 - 33%
        // lvl 1 - 50%
        // lvl 2 - 60%
        int bonusdamage = weapon.damageRoll(attacker)/5;
        for (int n : PathFinder.NEIGHBOURS9) {
            int pos = defender.pos + n;
            Char enemy = Actor.findChar(pos);
            if (enemy != null & (Random.Int(2) == 0 | enemy == defender)) {//Guaranteed to burn the target, may burn adjacent mobs as well.
                if (defender.buff(Burning.class) != null) {
                    Buff.affect(defender, Burning.class).reignite(defender, 8f);
                    defender.damage(bonusdamage, new Char.DamageSrc(Element.FIRE, this));
                } else {
                    Buff.affect(defender, Burning.class).reignite(defender, 8f);
                }
            } else {
                if (Random.Int(2) == 0 & pos != attacker.pos) {
                    GameScene.add(Blob.seed(pos, 3, Fire.class));
                }
            }
        }
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            if (attacker.fieldOfView[mob.pos] & Random.Int(4) == 0) {
                GameScene.add(Blob.seed(mob.pos, 3, Fire.class));
            }
        }


        defender.sprite.emitter().burst(FlameParticle.FACTORY, (weapon.level()+3));
        return damage;

    }

    @Override
    public void activate(RelicMeleeWeapon weapon, Char owner) {
        super.activate(weapon,owner);
        for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
            int damage = mob.HT/2;
            if (mob.properties().contains(Char.Property.BOSS)) {
                damage /=4;
            }
            if (mob.alignment == Char.Alignment.ENEMY) {
                mob.damage(damage, new Char.DamageSrc(Element.FIRE, weapon));
                GameScene.add(Blob.seed(mob.pos, 3, Fire.class));
            }
        }
    }

    @Override
    public ItemSprite.Glowing glowing() {
        return ORANGE;
    }
}
