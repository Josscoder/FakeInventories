package com.nukkitx.fakeinventories.inventory;

import com.nukkitx.fakeinventories.inventory.event.FakeInventoryCloseEvent;
import com.nukkitx.fakeinventories.inventory.event.FakeSlotChangeEvent;

public interface FakeInventoryListener {

    void onSlotChange(FakeSlotChangeEvent event);
    void onClose(FakeInventoryCloseEvent event);
}
