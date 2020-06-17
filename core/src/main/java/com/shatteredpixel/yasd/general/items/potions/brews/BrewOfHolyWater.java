package com.shatteredpixel.yasd.general.items.potions.brews;

import com.shatteredpixel.yasd.general.Assets;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.actors.blobs.Blob;
import com.shatteredpixel.yasd.general.actors.blobs.HolyWater;
import com.shatteredpixel.yasd.general.items.Dewdrop;
import com.shatteredpixel.yasd.general.items.potions.PotionOfHealing;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class BrewOfHolyWater extends Brew {
	{
		image = ItemSpriteSheet.BREW_HOLY_WATER;
	}

	@Override
	public void shatter(int cell) {
		if (Dungeon.level.heroFOV[cell]) {
			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}

		GameScene.add( Blob.seed( cell, 1000, HolyWater.class ) );
	}

	@Override
	public int price() {
		//prices of ingredients
		return quantity * 80;
	}

	public static class Recipe extends com.shatteredpixel.yasd.general.items.Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{Dewdrop.class, Dewdrop.class, PotionOfHealing.class};
			inQuantity = new int[]{1, 1, 1};

			cost = 8;

			output = BrewOfHolyWater.class;
			outQuantity = 1;
		}

	}
}
