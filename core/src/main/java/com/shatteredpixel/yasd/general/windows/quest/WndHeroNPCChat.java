package com.shatteredpixel.yasd.general.windows.quest;

import com.shatteredpixel.yasd.general.actors.hero.HeroClass;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.ui.Window;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class WndHeroNPCChat extends WndChat {

	public WndHeroNPCChat(HeroClass heroClass, String message) {
		super(heroClass.icon(), Messages.titleCase(heroClass.title()), message);
	}

	public WndHeroNPCChat(HeroClass heroClass, String message, @NotNull HashMap<String, Window> options) {
		super(heroClass.icon(), Messages.titleCase(heroClass.title()), message, options);
	}
}
