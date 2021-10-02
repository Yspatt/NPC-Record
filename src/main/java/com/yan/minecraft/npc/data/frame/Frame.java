package com.yan.minecraft.npc.data.frame;

import lombok.Data;
import org.bukkit.Location;

@Data
public class Frame {

    public Location location;
    public FrameAction action;

    public Frame(Location location, FrameAction action) {

        this.location = location;
        this.action = action;

    }
}
