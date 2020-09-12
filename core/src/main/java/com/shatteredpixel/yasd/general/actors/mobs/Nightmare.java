package com.shatteredpixel.yasd.general.actors.mobs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.SpawnerGas;
import com.shatteredpixel.yasd.general.actors.buffs.LockedFloor;
import com.shatteredpixel.yasd.general.effects.CellEmitter;
import com.shatteredpixel.yasd.general.effects.Speck;
import com.shatteredpixel.yasd.general.items.Amulet;
import com.shatteredpixel.yasd.general.items.scrolls.ScrollOfRetribution;
import com.shatteredpixel.yasd.general.items.scrolls.exotic.ScrollOfPsionicBlast;
import com.shatteredpixel.yasd.general.items.wands.Wand;
import com.shatteredpixel.yasd.general.items.wands.WandOfPrismaticLight;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Grim;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.NightmareSprite;
import com.shatteredpixel.yasd.general.ui.BossHealthBar;
import com.watabou.utils.Random;

public class Nightmare extends Boss {

    {
        spriteClass = NightmareSprite.class;

        hasMeleeAttack = false;
        range = 10;
        baseSpeed = 1.5f;
        damageFactor = 2f;

        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);

        immunities.add(ScrollOfRetribution.class);
        immunities.add(ScrollOfPsionicBlast.class);
        immunities.add(Grim.class);
    }

    @Override
    public Element elementalType() {
        return Element.SHOCK;
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell( Messages.get(this, "notice") );
    }

    @Override
    public void damage( int dmg, DamageSrc src ) {
        if (Random.Int(2) == 1 & dmg > HT/100f) {
            GameScene.add(Blob.seed(Dungeon.hero.pos, 300, SpawnerGas.class));
            int newPos = -1;
            for (int i = 0; i < 20; i++) {
                newPos = Dungeon.level.randomRespawnCell();
                if (newPos != -1) {
                    break;
                }
            }
            if (newPos != -1) {
                CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT), 0.2f, 3);
                pos = newPos;
                sprite.place(pos);
                sprite.visible = Dungeon.hero.fieldOfView[pos];
            }
        }
        if (src.getCause() instanceof Wand & !(src.getCause() instanceof WandOfPrismaticLight)) {
            dmg = Random.Int(dmg);
        }
        boolean bleeding = (HP*2 <= HT);
        super.damage(dmg, src);
        if ((HP*2 <= HT) && !bleeding){
            BossHealthBar.bleed(true);
        }
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null) lock.addTime(dmg*2);
    }

    @Override
    public void die(DamageSrc cause) {
        super.die(cause);
        Dungeon.level.drop(new Amulet.WaterGem(), pos).sprite.drop(pos);
        Dungeon.level.unseal();
    }
}
