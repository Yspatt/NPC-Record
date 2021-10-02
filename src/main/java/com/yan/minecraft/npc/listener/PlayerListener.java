package com.yan.minecraft.npc.listener;

import com.yan.minecraft.npc.NPCPlugin;
import com.yan.minecraft.npc.controller.NpcController;
import com.yan.minecraft.npc.data.frame.Frame;
import com.yan.minecraft.npc.data.frame.FrameAction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerListener implements Listener {

    public NpcController npcController = NPCPlugin.getInstance().getNpcController();

    @EventHandler
    private void move(PlayerMoveEvent event) {
        if (npcController.recordings.containsKey(event.getPlayer())) {
            npcController.recordings.get(event.getPlayer()).frames().add(new Frame(event.getPlayer().getLocation(), FrameAction.WALK));
        }
    }

    @EventHandler
    private void hit(PlayerInteractEvent event) {
        if (npcController.recordings.containsKey(event.getPlayer())) {
            npcController.recordings.get(event.getPlayer()).frames().add(new Frame(event.getPlayer().getLocation(), FrameAction.HIT));
        }
    }

    @EventHandler
    private void sneak(PlayerToggleSneakEvent event) {
        if (npcController.recordings.containsKey(event.getPlayer())) {
            if (event.isSneaking()) {
                npcController.recordings.get(event.getPlayer()).frames().add(new Frame(event.getPlayer().getLocation(), FrameAction.SNEAK_ON));
            } else {
                npcController.recordings.get(event.getPlayer()).frames().add(new Frame(event.getPlayer().getLocation(), FrameAction.SNEAK_OFF));
            }
        }
    }
}
