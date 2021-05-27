package com.shatteredpixel.yasd.general.windows.quest;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Callback;
import com.watabou.utils.Reflection;

import org.jetbrains.annotations.NotNull;

public class WndHeroNPCChat extends WndChat {

	public WndHeroNPCChat(Class<? extends HeroNPC> npc, String message) {
		super(Reflection.forceNewInstance(npc).heroClass().icon(), Messages.titleCase(Reflection.forceNewInstance(npc).heroClass().title()), message);
	}

	public WndHeroNPCChat(Class<? extends  HeroNPC> npc, String message, @NotNull ArrayMap<String, Callback> options) {
		super(Reflection.forceNewInstance(npc).heroClass().icon(), Messages.titleCase(Reflection.forceNewInstance(npc).heroClass().title()), message, options);
	}
}
