package com.shatteredpixel.yasd.general.windows.quest;

import com.badlogic.gdx.utils.ArrayMap;
import com.shatteredpixel.yasd.general.actors.mobs.npcs.HeroNPC;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.watabou.utils.Callback;

import org.jetbrains.annotations.NotNull;

public class WndHeroNPCChat extends WndChat {

	protected HeroNPC npc = null;

	public WndHeroNPCChat(HeroNPC npc, String message) {
		super(npc.heroClass().icon(), Messages.titleCase(npc.heroClass().title()), message);
		this.npc = npc;
	}

	public WndHeroNPCChat(HeroNPC npc, String message, @NotNull ArrayMap<String, Callback> options) {
		super(npc.heroClass().icon(), Messages.titleCase(npc.heroClass().title()), message, options);
		this.npc = npc;
	}
}
