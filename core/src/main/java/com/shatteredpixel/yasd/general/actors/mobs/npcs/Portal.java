package com.shatteredpixel.yasd.general.actors.mobs.npcs;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.LevelHandler;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.buffs.Buff;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.StatueSprite;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.watabou.utils.Callback;

public class Portal extends NPC {

	{
		spriteClass = StatueSprite.class;
	}

	@Override
	protected boolean act() {

		throwItem();

		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}

	@Override
	public void add(Buff buff) {}

	@Override
	protected void onAdd() {
		super.onAdd();
		if (!Dungeon.portalDepths[Dungeon.depth] && normal()) {
			Dungeon.portalDepths[Dungeon.depth] = true;
		}
	}

	private boolean normal() {
		return Dungeon.depth != 0;
	}

	@Override
	public String description() {
		return normal() ? super.description() : Messages.get(this, "desc_surface");
	}

	@Override
	public void damage(int dmg, DamageSrc src) {}

    @Override
	public String defenseVerb() {
		return "";
	}

	@Override
	public boolean interact(Char ch) {
		CPDGame.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				CPDGame.scene().addToFront(new WndPortal(Portal.this));
			}
		});
		return true;
	}

	private static class WndPortal extends Window {
		WndPortal(Portal portal) {
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

			int bottom = (int) message.bottom();
			if (portal.normal()) {
				RedButton btnPort = new RedButton(Messages.get(this, "teleport")) {
					@Override
					protected void onClick() {
						LevelHandler.portSurface();
					}
				};
				btnPort.setRect(0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT);
				add(btnPort);
				bottom = (int) btnPort.bottom();
			} else {
				if (Dungeon.portalDepths.length < 1) {
					RenderedTextBlock msgNoDepths = PixelScene.renderTextBlock( Messages.get(this, "none"), 6 );
					msgNoDepths.maxWidth(WIDTH);
					msgNoDepths.setPos(0, bottom + GAP);
					add( msgNoDepths );
					bottom = (int) msgNoDepths.bottom();
				} else {
					for (int depth = 0; depth < Dungeon.portalDepths.length; depth++) {
						if (Dungeon.portalDepths[depth]) {
							//Ignore the one on depth 0.
							if (depth == 0) {
								continue;
							}
							int finalDepth = depth;
							RedButton button = new RedButton(Messages.get(this, "teleport_depth", finalDepth)) {
								@Override
								protected void onClick() {
									LevelHandler.returnTo(finalDepth, -1);
								}
							};
							button.setRect(0, bottom + GAP, WIDTH, BTN_HEIGHT);
							add(button);
							bottom = (int) button.bottom();
						}
					}
				}
			}

			resize(WIDTH, bottom);
		}
	}
}
