package com.yan.minecraft.npc.model;

import com.yan.minecraft.npc.data.frame.Frame;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

public interface NPC {

     String name();
     String skin();
     Location location();
     UUID uuid();
     List<Frame> frames();

     void create(String name,String skin,Location location,UUID uuid);



    void spawn();
    void destroy();
    void walk(Frame record,Frame oldRecord);
    void sneak(boolean b);
    void hit();


}
