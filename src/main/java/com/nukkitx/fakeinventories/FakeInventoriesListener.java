package com.nukkitx.fakeinventories;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.network.protocol.DataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import com.nukkitx.fakeinventories.inventory.FakeInventories;
import com.nukkitx.fakeinventories.inventory.FakeInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeInventoriesListener implements Listener {

    private final FakeInventories fakeInventories;

    public FakeInventoriesListener(FakeInventories fakeInventories) {
        this.fakeInventories = fakeInventories;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPacketSend(DataPacketSendEvent event) {
        DataPacket packet = event.getPacket();

        if (!(packet instanceof UpdateBlockPacket)) {
            return;
        }

        List<BlockVector3> positions = fakeInventories.getFakeInventoryPositions(event.getPlayer());
        if (positions == null) {
            return;
        }

        UpdateBlockPacket updateBlock = (UpdateBlockPacket) packet;

        for (BlockVector3 pos : positions) {
            if (pos.x == updateBlock.x && pos.y == updateBlock.y && pos.z == updateBlock.z) {
                event.setCancelled();
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTransaction(InventoryTransactionEvent event) {
        Map<FakeInventory, List<SlotChangeAction>> actions = new HashMap<>();

        Player source = event.getTransaction().getSource();

        for (InventoryAction action : event.getTransaction().getActions()) {
            if (!(action instanceof SlotChangeAction)) {
                continue;
            }

            SlotChangeAction slotChange = (SlotChangeAction) action;
            if (!(slotChange.getInventory() instanceof FakeInventory)) {
                continue;
            }

            FakeInventory inventory = (FakeInventory) slotChange.getInventory();
            List<SlotChangeAction> slotChanges = actions.computeIfAbsent(inventory, fakeInventory -> new ArrayList<>());

            slotChanges.add(slotChange);
        }

        boolean cancel = false;
        for (Map.Entry<FakeInventory, List<SlotChangeAction>> entry : actions.entrySet()) {
            for (SlotChangeAction action : entry.getValue()) {
                if (entry.getKey().onSlotChange(source, action)) {
                    cancel = true;
                }
            }
        }

        if (cancel) {
            event.setCancelled();
        }
    }
}
