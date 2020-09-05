package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.StoryChapter;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.WandmakerSprite;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class TrialGiver extends NPC {

    {
        //TODO sprite
        spriteClass = WandmakerSprite.class;
    }

    @Override
    protected boolean act() {
        throwItem();
        return super.act();
    }

    @Override
    public boolean interact(Char ch) {
        Game.runOnRenderThread(new Callback() {
            @Override
            public void call() {
                Game.scene().addToFront(new WndTrialGiver(TrialGiver.this));
            }
        });
        return false;
    }

    @Override
    public int defenseSkill( Char enemy ) {
        return 1000;
    }

    @Override
    public void damage( int dmg, DamageSrc src ) {
    }

    @Override
    public void add( Buff buff ) {
    }

    @Override
    public boolean reset() {
        return true;
    }

    public static class WndTrialGiver extends Window {

        private static final int WIDTH = 120;
        private static final int BTN_HEIGHT = 20;
        private static final float GAP = 2;

        public WndTrialGiver(final TrialGiver trialGiver) {
            super();
            IconTitle titlebar = new IconTitle();
            titlebar.icon(trialGiver.sprite());
            titlebar.label(Messages.titleCase(trialGiver.name()));
            titlebar.setRect(0, 0, WIDTH, 0);
            add(titlebar);

            String msg = "";
            msg = Messages.get(trialGiver, "chat", Dungeon.hero.name());

            RenderedTextBlock message = PixelScene.renderTextBlock(msg, 6);
            message.maxWidth(WIDTH);
            message.setPos(0, titlebar.bottom() + GAP);
            add(message);

            float bottom = message.bottom() + GAP;
            for (StoryChapter.Trial trial : StoryChapter.Trial.values()) {
                if (trial != StoryChapter.Trial.NONE) {
                    RedButton btnTrial = new RedButton(trial.displayName()) {
                        @Override
                        protected void onClick() {
                            trial.warpTo();
                        }
                    };
                    btnTrial.setRect(0, bottom, WIDTH, BTN_HEIGHT);
                    add(btnTrial);
                    bottom = btnTrial.bottom();
                }
            }
            resize(WIDTH, (int) bottom);
        }
    }
}
