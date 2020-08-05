/*
 *
 *   Pixel Dungeon
 *   Copyright (C) 2012-2015 Oleg Dolya
 *
 *   Shattered Pixel Dungeon
 *   Copyright (C) 2014-2019 Evan Debenham
 *
 *   Yet Another Shattered Dungeon
 *   Copyright (C) 2014-2020 Samuel Braithwaite
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 */

package com.shatteredpixel.yasd.general.ui;

import com.shatteredpixel.yasd.general.CPDSettings;
import com.shatteredpixel.yasd.general.Chrome;
import com.shatteredpixel.yasd.general.Difficulty;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DifficultyButton extends StyledButton {

	public static int WIDTH = 80;
	public static int HEIGHT = 20;

	private Difficulty difficulty;
	public static ArrayList<DifficultyButton> buttonArrayList = new ArrayList<>();

	public DifficultyButton(@NotNull Difficulty difficulty) {
		super(Chrome.Type.GREY_BUTTON_TR, difficulty.title(), 9);
		this.difficulty = difficulty;
		buttonArrayList.add(this);
		updateDifficulty();
	}

	@Override
	protected void onClick() {
		super.onClick();
		CPDSettings.difficulty(difficulty);
		for (DifficultyButton difficultyButton : buttonArrayList) {
			difficultyButton.updateDifficulty();
		}
	}

	private void updateDifficulty() {
		if (difficulty == CPDSettings.difficulty()) {
			icon(Icons.get(Icons.CHALLENGE_ON));
		} else {
			icon(Icons.get(Icons.CHALLENGE_OFF));
		}
		enable(difficulty.isUnlocked());
	}
}
