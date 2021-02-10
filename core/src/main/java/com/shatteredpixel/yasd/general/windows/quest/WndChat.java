package com.shatteredpixel.yasd.general.windows.quest;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.windows.IconTitle;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

public class WndChat extends Window {

	protected static final int WIDTH_P    = 120;
	protected static final int WIDTH_L    = 160;

	public WndChat(Image icon, String title, String message) {
		this(icon, title, message, new ArrayMap<>());
	}

	public WndChat(Image icon, String title, String message, ArrayMap<String, Callback> options) {
		super();

		Component titlebar = new IconTitle( icon, title );
		int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

		titlebar.setRect( 0, 0, width, 0 );
		add(titlebar);

		RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
		text.text( message, width );
		text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
		add( text );

		int bottom = (int) (text.bottom() + GAP);

		for (String option : options.keys()) {
			RedButton button = new RedButton(option) {
				@Override
				protected void onClick() {
					hide();
					options.get(option).call();
				}
			};
			button.setRect(0, bottom, width, BTN_HEIGHT);
			add(button);
			bottom += BTN_HEIGHT;
		}
		resize(width, bottom);
	}

	public static Callback asCallback(Window window) {
		return new Callback() {
			@Override
			public void call() {
				CPDGame.scene().addToFront(window);
			}
		};
	}

	public static Callback asCallback(Class<? extends Window> window) {
		return asCallback(Reflection.newInstance(window));
	}
}
