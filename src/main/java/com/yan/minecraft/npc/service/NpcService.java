package com.yan.minecraft.npc.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yan.minecraft.npc.NPCPlugin;
import com.yan.minecraft.npc.data.frame.Frame;
import com.yan.minecraft.npc.model.Service;
import com.yan.minecraft.npc.util.world.LocationAdapter;
import com.yan.minecraft.npc.util.MasterFile;
import com.yan.minecraft.npc.model.NPC;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.UUID;


public class NpcService implements Service {

    /*


    LOAD ALL NPC FROM DATA.YML

     */

    public MasterFile data = new MasterFile(NPCPlugin.getInstance(),"data.yml");
    public Gson gson = new GsonBuilder().registerTypeAdapter(Location.class,new LocationAdapter()).create();

    @Override
    public void init() {

        ConfigurationSection section = data.getConfigurationSection("npc");
        if (section != null) {
            for (String uuid : section.getKeys(false)) {
                NPC npc = NPCPlugin.getInstance().getNpcController().createNPC(
                        data.getString("npc." + uuid + ".name"),
                        data.getString("npc." + uuid + ".skin"),
                        data.getLocation("npc." + uuid + ".location"),
                        UUID.fromString(uuid)
                );
                List<String> frames = data.getStringList("npc." + uuid + ".frames");
                for (String frame : frames) {
                    npc.frames().add(gson.fromJson(frame,Frame.class));
                }
            }
        }
    }

    @Override
    public void load() {

    }

    @Override
    public void stop() {
        NPCPlugin.getInstance().getNpcController().npcList.forEach(npc ->{
            data.set("npc." + npc.uuid() + ".name",npc.name());
            data.set("npc." + npc.uuid() + ".skin",npc.skin());
            data.setLocation("npc." + npc.uuid() + ".location",npc.location());
            List<String> serializer = Lists.newArrayList();
            for (Frame frame : npc.frames()) {
                serializer.add(gson.toJson(frame,Frame.class));
            }
            data.set("npc." + npc.uuid() + ".frames",serializer);
        });
        data.save();
    }
}
