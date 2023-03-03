package com.nukkitx.fakeinventories;

import cn.nukkit.plugin.PluginBase;
import com.nukkitx.fakeinventories.inventory.FakeInventories;
import lombok.Getter;

public class FakeInventoriesPlugin extends PluginBase {

    @Getter
    private static FakeInventoriesPlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // register listener
        getServer().getPluginManager().registerEvents(new FakeInventoriesListener(new FakeInventories()), this);
    }
}
