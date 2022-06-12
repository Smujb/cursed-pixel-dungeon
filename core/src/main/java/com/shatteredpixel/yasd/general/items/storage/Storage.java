package com.shatteredpixel.yasd.general.items.storage;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.items.Enchantable;
import com.shatteredpixel.yasd.general.items.Item;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

public class Storage {
    /*
    Stores up to 15 items to be transferred between runs. Items are "reset" to a basic level before being stored to prevent abuse.
     */

    public static final String STORAGE_FILE = "storage.dat";
    public static final int MAX_ITEMS = 15;

    private static final String ITEMS	= "items";
    public static ArrayList<Item> items = null;

    //"Resets" the item. To prevent the storage being too powerful, enchantments and upgrade levels are not preserved.
    private static Item resetItem(Item item) {
        item.identify();
        item.level(0);
        item.uncurse();
        if (item instanceof Enchantable) ((Enchantable) item).enchant(null);
        return item;
    }

    public static void add(Item item) {
        if (item.unique) return;

        items = getItems();
        if (items.size() >= MAX_ITEMS) return;

        items.add(resetItem(item));

        Bundle bundle = new Bundle();
        bundle.put( ITEMS, items );

        try {
            FileUtils.bundleToFile(STORAGE_FILE, bundle );
        } catch (IOException e) {
            CPDGame.reportException(e);
        }
    }

    public static Item remove(Item item) {

        items = getItems();
        if (!items.contains(item)) return null;

        items.remove(item);

        Bundle bundle = new Bundle();
        bundle.put( ITEMS, items );

        try {
            FileUtils.bundleToFile(STORAGE_FILE, bundle );
            return resetItem(item);
        } catch (IOException e) {
            CPDGame.reportException(e);
            return null;
        }
    }

    public static boolean isFull() {
        return listLen() >= MAX_ITEMS;
    }

    public static int listLen() {
        return getItems().size();
    }

    @NotNull
    public static ArrayList<Item> getItems() {
	    //If items is null then load file, otherwise return currently loaded items

        if (items != null) {
            return items;
        } else {
            items = new ArrayList<>();
        }

        try {
            Bundle bundle = FileUtils.bundleFromFile(STORAGE_FILE);
            for (Bundlable bundleItem : bundle.getCollection( ITEMS )) {
                if (bundleItem instanceof Item) items.add((Item)bundleItem);
            }
            return items;
        } catch (IOException e) {
            CPDGame.reportException(e);
            return new ArrayList<>();
        }
    }
}
