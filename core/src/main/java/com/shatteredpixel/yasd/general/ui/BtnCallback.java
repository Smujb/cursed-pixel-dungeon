package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.Chrome;
import com.watabou.utils.Callback;

public class BtnCallback extends StyledButton {

	private Callback callback;

	public BtnCallback(String title, Callback onClick) {
		super( Chrome.Type.GREY_BUTTON_TR, title );
		textColor( Window.SHPX_COLOR );
		this.visible = title != null;
		this.callback = onClick;
	}

	@Override
	protected void onClick() {
		super.onClick();
		callback.call();
	}
}
