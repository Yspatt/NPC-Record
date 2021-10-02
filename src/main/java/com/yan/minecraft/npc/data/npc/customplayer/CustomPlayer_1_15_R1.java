package com.yan.minecraft.npc.data.npc.customplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;

public class CustomPlayer_1_15_R1 extends EntityPlayer {

    public CustomPlayer_1_15_R1(MinecraftServer minecraftServer, WorldServer worldServer, GameProfile gameProfile) {
        super(minecraftServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));
    }
}