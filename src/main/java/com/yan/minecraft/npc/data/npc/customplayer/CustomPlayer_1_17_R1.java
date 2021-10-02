package com.yan.minecraft.npc.data.npc.customplayer;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;

import java.lang.reflect.Field;

public class CustomPlayer_1_17_R1 extends EntityPlayer {

    public CustomPlayer_1_17_R1(MinecraftServer minecraftServer, WorldServer worldServer, GameProfile gameProfile) {
        super(minecraftServer, worldServer, gameProfile);
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