package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.RatSprite;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.watabou.utils.Callback;

public class Portal extends NPC {
	{
		spriteClass = RatSprite.class;

		HP = HT = 1;
	}

	@Override
	protected void onAdd() {
		super.onAdd();
		if (!Dungeon.portalDepths.contains(Dungeon.depth)) {
			CPDGame.runOnRenderThread(new Callback() {
				@Override
				public void call() {
					Dungeon.portalDepths.add(Dungeon.depth);
				}
			});
		}
	}

	@Override
	public int defenseSkill(Char enemy) {
		return Char.INFINITE_EVASION;
	}

	@Override
	public String defenseVerb() {
		return "";
	}

	@Override
	public boolean interact(Char ch) {
		CPDGame.scene().addToFront(new WndPortal());
		return super.interact(ch);
	}

	private static class WndPortal extends Window {
		WndPortal() {
			super();

			IconTitle titlebar = new IconTitle();
			titlebar.label(Messages.get(this, "title"));
			titlebar.setRect(0, 0, WIDTH, 0);
			add( titlebar );

			String msg = Messages.get(this, "body");

			RenderedTextBlock message = PixelScene.renderTextBlock( msg, 6 );
			message.maxWidth(WIDTH);
			message.setPos(0, titlebar.bottom() + GAP);
			add( message );

			RedButton btnPort = new RedButton( Messages.get(this, "teleport") ) {
				@Override
				protected void onClick() {
					LevelHandler.portSurface();
				}
			};
			btnPort.setRect(0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT);
			add( btnPort );
		}
	}
}
