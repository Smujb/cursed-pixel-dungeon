package com.shatteredpixel.yasd.general.items.relics;

import com.shatteredpixel.yasd.general.messages.Messages;
import com.shatteredpixel.yasd.general.scenes.CellSelector;
import com.shatteredpixel.yasd.general.scenes.GameScene;

public abstract class SelectorRelic extends Relic {

    @Override
    protected void doActivate() {
        GameScene.selectCell(CHOOSE);
    }

    protected abstract void onCellSelected(int cell);

    private final CellSelector.Listener CHOOSE = new CellSelector.Listener() {

        @Override
        public void onSelect(Integer cell) {
            if (cell != null) {
                onCellSelected(cell);
                useCharge(chargePerUse);
            }
        }

        @Override
        public String prompt() {
            return Messages.get(SelectorRelic.this, "select_cell_ability");
        }
    };
}
