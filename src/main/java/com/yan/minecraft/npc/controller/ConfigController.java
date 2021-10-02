package com.yan.minecraft.npc.controller;

import com.google.common.collect.Maps;
import com.yan.minecraft.npc.NPCPlugin;
import com.yan.minecraft.npc.util.MasterFile;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

@Getter
public class ConfigController {

    public MasterFile config = new MasterFile(NPCPlugin.getInstance(),"config.yml");
    public HashMap<String,String> cachedValues = Maps.newHashMap();


    public void loadCache(){
        ConfigurationSection section = config.getConfigurationSection("");
        if (section != null){
            for (String key : section.getKeys(true)) {
                System.out.println(key);
                cachedValues.put(key, ChatColor.translateAlternateColorCodes('&',config.getString(key).replace("{break}","\n")));
            }
        }
        System.out.println(cachedValues);
    }


}
