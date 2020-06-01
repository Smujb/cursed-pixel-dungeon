package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.windows.WndDevSettings;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;

public class DevSettingsButton extends Button {


	private Image image;

	@Override
	protected void createChildren() {
		super.createChildren();

		image = Icons.WARNING.get();
		add( image );
	}

	@Override
	protected void layout() {
		super.layout();

		image.x = x + (width - image.width)/2f;
		image.y = y + (height - image.height)/2f;
		PixelScene.align(image);
	}

	@Override
	protected void onPointerDown() {
		image.brightness( 1.5f );
		Sample.INSTANCE.play( Assets.SND_CLICK );
	}

	@Override
	protected void onPointerUp() {
		image.resetColor();
	}

	@Override
	protected void onClick() {
		WndDevSettings wnd = new WndDevSettings();
		parent.add(wnd);
	}
}