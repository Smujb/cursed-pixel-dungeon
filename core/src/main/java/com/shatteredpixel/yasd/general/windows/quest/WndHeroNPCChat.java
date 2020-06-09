package com.shatteredpixel.yasd.general.windows.quest;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.Window;

import org.jetbrains.annotations.NotNull;

public class WndHeroNPCChat extends WndChat {

	public WndHeroNPCChat(HeroClass heroClass, String message) {
		super(heroClass.icon(), Messages.titleCase(heroClass.title()), message);
	}

	public WndHeroNPCChat(HeroClass heroClass, String message, @NotNull ArrayMap<String, Class<? extends Window>> options) {
		super(heroClass.icon(), Messages.titleCase(heroClass.title()), message, options);
	}
}
