package com.nukkitx.fakeinventories.inventory.event;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import com.nukkitx.fakeinventories.inventory.FakeInventory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeInventoryCloseEvent implements Cancellable {

    private final Player player;
    private final FakeInventory inventory;
    private boolean cancelled = false;

    public FakeInventoryCloseEvent(Player player, FakeInventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    @Override
    public void setCancelled() {
        this.cancelled = true;
    }
}
