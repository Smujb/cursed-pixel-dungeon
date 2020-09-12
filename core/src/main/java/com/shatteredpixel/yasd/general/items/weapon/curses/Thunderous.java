package com.shatteredpixel.yasd.general.items.weapon.curses;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Element;
import com.shatteredpixel.yasd.general.actors.Char;
import com.shatteredpixel.yasd.general.actors.hero.Hero;
import com.shatteredpixel.yasd.general.effects.Lightning;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.weapon.Weapon;
import com.shatteredpixel.yasd.general.items.weapon.enchantments.Shocking;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class Thunderous extends Weapon.Enchantment {

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	private ArrayList<Char> affected = new ArrayList<>();

	private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

	@Override
	public boolean curse() {
		return true;
	}

	@Override
	public int proc(Enchantable weapon, Char attacker, Char defender, int damage) {

		if (attacker instanceof Hero) {
			Hero hero = ((Hero)attacker);
			if (hero.useMP(hero.maxMP())) {
				affected.clear();
				arcs.clear();

				Shocking.arc(attacker, defender, 2, affected, arcs);

				for (Char ch : affected) {
					ch.damage(Math.round(hero.maxMP() * 3), new Char.DamageSrc(Element.SHOCK, this).ignoreDefense());
				}

				attacker.sprite.parent.addToFront(new Lightning(arcs, new Callback() {
					@Override
					public void call() {
						Camera.main.shake(3, 0.2f);
					}
				}));
				Sample.INSTANCE.play(Assets.Sounds.LIGHTNING);

				Camera.main.shake(5, 0.2f);
			}
		}
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
}
