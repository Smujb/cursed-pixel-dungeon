package com.shatteredpixel.yasd.general.items.weapon.melee;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.particles.LeafParticle;
import com.shatteredpixel.yasd.general.levels.features.HighGrass;
import com.shatteredpixel.yasd.general.levels.terrain.Terrain;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.utils.PathFinder;

public class Hoe extends MeleeWeapon {

    {
        image = ItemSpriteSheet.Weapons.HOE;

        hitSound = Assets.Sounds.HIT_PARRY;
        hitSoundPitch = 0.6f;

        damageFactor = 0.85f;

        statScaling.add(Hero.HeroStat.FOCUS);
    }

    @Override
    public int proc(Char attacker, Char defender, int damage) {
        for (int n : PathFinder.NEIGHBOURS9) {
            int c = defender.pos + n;
            if (c >= 0 && c < Dungeon.level.length()) {
                if (Dungeon.level.terrainIsOneOf(c, Terrain.HIGH_GRASS, Terrain.GRASS, Terrain.FURROWED_GRASS)) {
                    if (Dungeon.level.heroFOV[c]) {
                        CellEmitter.get(c).burst(LeafParticle.LEVEL_SPECIFIC, 4);
                    }
                    HighGrass.trample(Dungeon.level, c, true);
                    GameScene.updateMap(c);
                }
            }
        }
        return super.proc(attacker, defender, damage);
    }

    @Override
    protected String propsDesc() {
        return super.propsDesc() + "\n" + Messages.get(this, "grass");
    }
}
