package com.yan.minecraft.npc;

import com.yan.minecraft.npc.command.NpcCommand;
import com.yan.minecraft.npc.controller.ConfigController;
import com.yan.minecraft.npc.controller.NpcController;
import com.yan.minecraft.npc.helpers.CorePlugin;
import com.yan.minecraft.npc.inventory.ConfirmInventoryProvider;
import com.yan.minecraft.npc.listener.PlayerListener;
import com.yan.minecraft.npc.service.NpcService;
import lombok.Getter;

@Getter
public class NPCPlugin extends CorePlugin {

    public ConfigController configController;
    public NpcController npcController;
    public NpcService npcService;

    public ConfirmInventoryProvider confirmInventoryProvider;

    public String serverVersion;


    @Override
    public void enable() {

        configController = new ConfigController();
        npcController = new NpcController();

        npcService = new NpcService();
        npcService.init();

        configController.loadCache();

        confirmInventoryProvider = new ConfirmInventoryProvider();

        listeners(this,new PlayerListener());
        commands(this,new NpcCommand());

    }

    @Override
    public void disable() {
        npcService.stop();
    }

    @Override
    public void load() {
    }
    public static NPCPlugin getInstance(){
        return NPCPlugin.getPlugin(NPCPlugin.class);
    }

}
