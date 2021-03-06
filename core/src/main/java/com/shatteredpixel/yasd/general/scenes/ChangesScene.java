/*
 *
 *  * Pixel Dungeon
 *  * Copyright (C) 2012-2015 Oleg Dolya
 *  *
 *  * Shattered Pixel Dungeon
 *  * Copyright (C) 2014-2019 Evan Debenham
 *  *
 *  * Cursed Pixel Dungeon
 *  * Copyright (C) 2014-2020 Samuel Braithwaite
 *  *
 *  * This program is free software: you can redistribute it and/or modify
 *  * it under the terms of the GNU General Public License as published by
 *  * the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.scenes;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.Archs;
import com.shatteredpixel.yasd.general.ui.ExitButton;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.ScrollPane;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.ui.changelist.ChangeInfo;
import com.shatteredpixel.yasd.general.ui.changelist.CursedChanges;
import com.shatteredpixel.yasd.general.ui.changelist.YASD_log;
import com.shatteredpixel.yasd.general.ui.changelist.v0_1_X_Changes;
import com.shatteredpixel.yasd.general.ui.changelist.v0_2_X_Changes;
import com.shatteredpixel.yasd.general.ui.changelist.v0_3_X_Changes;
import com.shatteredpixel.yasd.general.ui.changelist.v0_4_X_Changes;
import com.shatteredpixel.yasd.general.ui.changelist.v0_5_X_Changes;
import com.shatteredpixel.yasd.general.ui.changelist.v0_6_X_Changes;
import com.shatteredpixel.yasd.general.ui.changelist.v0_7_X_Changes;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class ChangesScene extends PixelScene {
	
	public static int changesSelected = 0;
	
	@Override
	public void create() {
		super.create();

		Music.INSTANCE.play( Assets.Music.TITLE_THEME, true );

		int w = Camera.main.width;
		int h = Camera.main.height;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 9 );
		title.hardlight(Window.TITLE_COLOR);
		title.setPos(
				(w - title.width()) / 2f,
				(20 - title.height()) / 2f
		);
		align(title);
		add(title);

		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );

		NinePatch panel = Chrome.get(Chrome.Type.TOAST);

		int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
		int ph = h - 35;

		panel.size( pw, ph );
		panel.x = (w - pw) / 2f;
		panel.y = title.bottom() + 4;
		align( panel );
		add( panel );
		
		final ArrayList<ChangeInfo> changeInfos = new ArrayList<>();
		
		switch (changesSelected){
			default:
				CursedChanges.addAllChanges(changeInfos);
				break;
			case 1:
				YASD_log.addAllChanges(changeInfos);
				break;
			case 2:
				v0_7_X_Changes.addAllChanges(changeInfos);
				v0_6_X_Changes.addAllChanges(changeInfos);
				v0_5_X_Changes.addAllChanges(changeInfos);
				v0_4_X_Changes.addAllChanges(changeInfos);
				v0_3_X_Changes.addAllChanges(changeInfos);
				v0_2_X_Changes.addAllChanges(changeInfos);
				v0_1_X_Changes.addAllChanges(changeInfos);
				break;
		}

		ScrollPane list = new ScrollPane( new Component() ){

			@Override
			public void onClick(float x, float y) {
				for (ChangeInfo info : changeInfos){
					if (info.onClick( x, y )){
						return;
					}
				}
			}

		};
		add( list );

		Component content = list.content();
		content.clear();

		float posY = 0;
		float nextPosY = 0;
		boolean second = false;
		for (ChangeInfo info : changeInfos){
			if (info.major) {
				posY = nextPosY;
				second = false;
				info.setRect(0, posY, panel.innerWidth(), 0);
				content.add(info);
				posY = nextPosY = info.bottom();
			} else {
				if (!second){
					second = true;
					info.setRect(0, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = info.bottom();
				} else {
					second = false;
					info.setRect(panel.innerWidth()/2f, posY, panel.innerWidth()/2f, 0);
					content.add(info);
					nextPosY = Math.max(info.bottom(), nextPosY);
					posY = nextPosY;
				}
			}
		}

		content.setSize( panel.innerWidth(), (int)Math.ceil(posY) );

		list.setRect(
				panel.x + panel.marginLeft(),
				panel.y + panel.marginTop() - 1,
				panel.innerWidth(),
				panel.innerHeight() + 2);
		list.scrollTo(0, 0);

		String[] labels = new String[] {"Cursed PD", "YASD/Powered PD", "Shattered PD"};
		if (w < 200) {
			labels = new String[] {"CPD", "YASD/PPD", "SPD"};
		}

		RedButton btnCPD = new RedButton(labels[0]){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 0) {
					changesSelected = 0;
					CPDGame.seamlessResetScene();
				}
			}
		};
		if (changesSelected == 0) btnCPD.textColor(Window.TITLE_COLOR);
		btnCPD.setRect(0, list.bottom()+5, w/3f, 14);
		add(btnCPD);

		RedButton btnPPD = new RedButton(labels[1]){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 1) {
					changesSelected = 1;
					CPDGame.seamlessResetScene();
				}
			}
		};
		if (changesSelected == 1) btnPPD.textColor(Window.TITLE_COLOR);
		btnPPD.setRect(btnCPD.right() + 2, list.bottom()+5, w/3f, 14);
		add(btnPPD);
		
		RedButton btnSPD = new RedButton(labels[2]){
			@Override
			protected void onClick() {
				super.onClick();
				if (changesSelected != 2) {
					changesSelected = 2;
					CPDGame.seamlessResetScene();
				}
			}
		};
		if (changesSelected == 2) btnSPD.textColor(Window.TITLE_COLOR);
		btnSPD.setRect(btnPPD.right() + 2, btnPPD.top(), w/3f, 14);
		add(btnSPD);

		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );

		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		CPDGame.switchScene(TitleScene.class);
	}

}
