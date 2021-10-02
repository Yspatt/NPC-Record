package com.yan.minecraft.npc.data.npc.customplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;

import java.lang.reflect.Field;

public class CustomPlayer_1_12_R1 extends EntityPlayer {

    public CustomPlayer_1_12_R1(MinecraftServer minecraftServer, WorldServer worldServer, GameProfile gameProfile) {
        super(minecraftServer, worldServer, gameProfile,new PlayerInteractManager(worldServer));
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;
        try
        {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(object);
        }
        catch(NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            e.printStackTrace();
        }
        return o;
    }
}