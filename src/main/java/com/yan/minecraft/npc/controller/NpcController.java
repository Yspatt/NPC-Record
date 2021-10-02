package com.yan.minecraft.npc.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yan.minecraft.npc.NPCPlugin;
import com.yan.minecraft.npc.data.frame.Frame;
import com.yan.minecraft.npc.data.frame.FrameAction;
import com.yan.minecraft.npc.model.NPC;
import com.yan.minecraft.npc.data.npc.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class NpcController {

    public List<NPC> npcList = Lists.newArrayList();
    public Map<Player,NPC> recordings = Maps.newHashMap();


    public Optional<NPC> get(String name) {return npcList.stream().filter(npc -> npc.name().equalsIgnoreCase(name)).findFirst();}
    public void delete(String name){
        npcList.remove(get(name).orElse(null));
    }

    public NPC createNPC(String name, String skin, Location location, UUID uuid) {
        String serverVersion = NPCPlugin.getInstance().getServerVersion();
        NPC npc = null;
        switch (serverVersion) {
            case "v1_17_R1" -> npc = new NPC_1_17_R1();
            case "v1_15_R1" -> npc = new NPC_1_15_R1();
            case "v1_12_R1" -> npc = new NPC_1_12_R1();
            case "v1_8_R1" -> npc = new NPC_1_8_R1();
        }
        if (npc != null) {
            npc.create(name, skin, location, uuid);
            npcList.add(npc);

        } else {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[NPC] Cannot create NPC, reason: Version not Supported");
            return null;
        }
        return npc;
    }


    public void replay(NPC npc){
            new BukkitRunnable(){
                int index = 1;
                @Override
                public void run() {
                    if (index >= npc.frames().size()-1){
                        npc.destroy();
                        cancel();
                    }
                    Frame record = npc.frames().get(index);
                    Frame oldRecord = npc.frames().get(index-1);

                    npc.walk(record,oldRecord);

                    if (record.getAction() == FrameAction.HIT){
                        npc.hit();
                    }
                    if (record.getAction() == FrameAction.SNEAK_ON){
                        npc.sneak(true);
                    }
                    if (record.getAction() == FrameAction.SNEAK_OFF){
                        npc.sneak(false);
                    }
                    index++;
                }
            }.runTaskTimer(NPCPlugin.getInstance(),0,0);
    }


    public String[] getNpcSkin(String name) {
        try {
            URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            return new String[] {texture, signature};
        } catch (IOException e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
            return null;
        }
    }
}
