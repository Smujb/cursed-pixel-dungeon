package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.ui.CheckBox;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.Window;

public class WndDevSettings extends Window {

	private static final int WIDTH		    = 112;
	private static final int BTN_HEIGHT	    = 18;
	private static final int GAP 	 		= 6;
	public WndDevSettings() {
		super();
		IconTitle titlebar = new IconTitle();
		titlebar.label(Messages.get(this, "title"));
		titlebar.setRect(0, 0, WIDTH, 0);
		add( titlebar );

		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "debug_report_desc"), 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );

		CheckBox chkDebugReport = new CheckBox(Messages.get(this, "debug_report")){
			@Override
			protected void onClick() {
				super.onClick();
				CPDSettings.debugReport(checked());
			}
		};
		chkDebugReport.setRect(0, message.bottom() + GAP, WIDTH, BTN_HEIGHT);
		chkDebugReport.checked(CPDSettings.debugReport());
		add(chkDebugReport);

		RenderedTextBlock message2 = PixelScene.renderTextBlock( Messages.get(this, "disable_cache_desc"), 6 );
		message2.maxWidth(WIDTH);
		message2.setPos(0, chkDebugReport.bottom() + GAP * 3);
		add( message2 );

		CheckBox chkDisableCache = new CheckBox(Messages.get(this, "disable_cache")){
			@Override
			protected void onClick() {
				super.onClick();
				CPDSettings.mapCache(!checked());
			}
		};
		chkDisableCache.setRect(0, message2.bottom() + GAP, WIDTH, BTN_HEIGHT);
		chkDisableCache.checked(!CPDSettings.mapCache());
		add(chkDisableCache);

		resize(WIDTH, (int) chkDisableCache.bottom());
	}
}

