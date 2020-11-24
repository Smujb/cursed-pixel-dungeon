package com.shatteredpixel.yasd.general.windows;

import com.shatteredpixel.yasd.general.CPDGame;
import com.shatteredpixel.yasd.general.Dungeon;
import com.shatteredpixel.yasd.general.items.EquipableItem;
import com.shatteredpixel.yasd.general.items.Item;
import com.shatteredpixel.yasd.general.items.storage.Storage;
import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.GameScene;
import com.shatteredpixel.yasd.general.scenes.PixelScene;
import com.shatteredpixel.yasd.general.sprites.ItemSprite;
import com.shatteredpixel.yasd.general.ui.RedButton;
import com.shatteredpixel.yasd.general.ui.RenderedTextBlock;
import com.shatteredpixel.yasd.general.ui.ScrollPane;
import com.shatteredpixel.yasd.general.ui.Window;
import com.shatteredpixel.yasd.general.utils.GLog;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndStorage extends Window {

    private static final int GAP = 2;

    private float pos;
    private final ScrollPane itemList;
    private final ArrayList<ItemSlot> slots = new ArrayList<>();
    private final ArrayList<Item> items;

    public WndStorage() {
        items = Storage.getItems();
        itemList = new ScrollPane(new Component()) {
            @Override
            public void onClick(float x, float y) {
                int size = slots.size();
                for (int i = 0; i < size; i++) {
                    if (slots.get(i).onClick(x, y, WndStorage.this)) {
                        break;
                    }
                }
            }
        };
        add(itemList);
        setupList();
    }

    private void setupList() {
        slots.clear();
        Component content = itemList.content();
        RedButton btnAddItem = new RedButton(Messages.get(WndStorage.this, "put_item")) {
            @Override
            protected void onClick() {
                hide();
                GameScene.selectItem(itemChooser, WndBag.Mode.EQUIPMENT, Messages.get(WndStorage.this, "choose_item"));
            }
        };
        btnAddItem.setRect(0, 0, WIDTH, BTN_HEIGHT);
        btnAddItem.enable(!Storage.isFull());
        add(btnAddItem);
        pos = btnAddItem.bottom() + GAP;

        for (Item it : items) {
            ItemSlot slot = new ItemSlot(it);
            slot.setRect(0, pos, WIDTH, BTN_HEIGHT);
            content.add(slot);
            slots.add(slot);
            pos += GAP + slot.height();
        }
        content.setSize(itemList.width(), pos);
        itemList.setSize(itemList.width(), itemList.height());
        resize(WIDTH, WIDTH);
        itemList.setRect(0, 0, width, height);
    }

    private static final WndBag.Listener itemChooser = new WndBag.Listener() {
        @Override
        public void onSelect(Item item) {
            if (item instanceof EquipableItem) {
                if (Storage.isFull()) {
                    GLog.negative(Messages.get(WndStorage.class, "full"));
                } else if (Dungeon.hero.belongings.backpack.contains(item) && item.detach(Dungeon.hero.belongings.backpack) != null) {
                    Storage.add(item);
                } else if (item.isEquipped(Dungeon.hero) && ((EquipableItem) item).doUnequip(Dungeon.hero, false)) {
                    Storage.add(item);
                }
            }
        }
    };

    private class ItemSlot extends Component {

        private final Item item;

        Image icon;
        RenderedTextBlock txt;

        public ItemSlot(Item item) {
            super();
            this.item = item;

            icon = new ItemSprite(item);
            icon.y = this.y;
            add(icon);

            txt = PixelScene.renderTextBlock(Messages.titleCase(item.name()), 8);
            txt.setPos(icon.height + GAP, this.y - (int) (icon.height - txt.bottom()) / 2f);
            add(txt);

        }

        @Override
        protected void layout() {
            super.layout();
            icon.y = this.y;
            txt.setPos(icon.width + GAP, pos + (int) (icon.height - txt.bottom()) / 2f);
        }

        protected boolean onClick(float x, float y, WndStorage parentWindow) {
            if (inside(x, y)) {
                if (item != null) {
                    CPDGame.scene().addToFront(new WndTakeItem(item, parentWindow));
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    public static class WndTakeItem extends WndInfoItem {

        public WndTakeItem(Item item, WndStorage parentWindow) {
            super(item);
            int bottom = height;
            RedButton btnTake = new RedButton(Messages.get(WndTakeItem.this, "take")) {
                @Override
                protected void onClick() {
                    if (item.collect()) {
                        Storage.remove(item);
                        GameScene.pickUp(item, Dungeon.hero.pos);
                        hide();
                        parentWindow.hide();
                    }
                }
            };
            btnTake.setRect(0, bottom, width, BTN_HEIGHT);
            add(btnTake);
            bottom += BTN_HEIGHT;
            resize(width, bottom);
        }
    }
}
