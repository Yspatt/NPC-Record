package com.yan.minecraft.npc.data.npc.customplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;

public class CustomPlayer_1_17_R1 extends EntityPlayer {

    public CustomPlayer_1_17_R1(MinecraftServer minecraftServer, WorldServer worldServer, GameProfile gameProfile) {
        super(minecraftServer, worldServer, gameProfile);
    }
}