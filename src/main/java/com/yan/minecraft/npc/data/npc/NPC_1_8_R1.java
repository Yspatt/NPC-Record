package com.yan.minecraft.npc.data.npc;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.yan.minecraft.npc.NPCPlugin;
import com.yan.minecraft.npc.data.frame.Frame;
import com.yan.minecraft.npc.model.NPC;
import com.yan.minecraft.npc.data.npc.customplayer.CustomPlayer_1_8_R1;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftServer;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class NPC_1_8_R1 implements NPC {

    private CustomPlayer_1_8_R1 entityPlayer;
    private String name;
    private String skin;
    private Location location;
    private UUID uuid;
    private List<Frame> frames;

    @Override
    public String name() {
        return name;
    }

    @Override
    public String skin() {
        return skin;
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public UUID uuid() {
        return uuid;
    }

    @Override
    public List<Frame> frames() {
        return frames;
    }

    @Override
    public void create(String name, String skin, Location location, UUID uuid) {
        this.name = name;
        this.skin = skin;
        this.location = location;
        this.uuid = uuid;
        this.frames = Lists.newArrayList();
    }

    @Override
    public void spawn() {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();

        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(uuid, name);

        entityPlayer = new CustomPlayer_1_8_R1(nmsServer, worldServer, gameProfile);
        String[] a = NPCPlugin.getInstance().getNpcController().getNpcSkin(skin);
        gameProfile.getProperties().put("textures", new Property("textures", a[0], a[1]));
        entityPlayer.setLocation(frames.get(0).getLocation().getX(), frames.get(0).getLocation().getY(), frames.get(0).getLocation().getZ(), frames.get(0).getLocation().getYaw(), frames.get(0).getLocation().getPitch());

        PacketPlayOutPlayerInfo addPlayerPacket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer);
        PacketPlayOutPlayerInfo removePlayerPacket = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer);
        PacketPlayOutNamedEntitySpawn spawnPlayerPacket = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
            playerConnection.sendPacket(addPlayerPacket);
            playerConnection.sendPacket(spawnPlayerPacket);
            playerConnection.sendPacket(removePlayerPacket);
        });
    }

    @Override
    public void destroy() {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        Bukkit.getOnlinePlayers().forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().playerConnection.sendPacket(destroy);
        });
    }

    @Override
    public void walk(Frame record, Frame oldrecord) {
        short x = (short) ((record.getLocation().getX() * 32 - oldrecord.getLocation().getX() * 32) * 128);
        short y = (short) ((record.getLocation().getY() * 32 - oldrecord.getLocation().getY() * 32) * 128);
        short z = (short) ((record.getLocation().getZ() * 32 - oldrecord.getLocation().getZ() * 32) * 128);

        PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutRelEntityMoveLook();
        setPrivateField(PacketPlayOutEntity.class, packet, "a", entityPlayer.getId());
        setPrivateField(PacketPlayOutEntity.class, packet, "b", x);
        setPrivateField(PacketPlayOutEntity.class, packet, "c", y);
        setPrivateField(PacketPlayOutEntity.class, packet, "d", z);
        PacketPlayOutEntityHeadRotation p2 = new PacketPlayOutEntityHeadRotation();
        setPrivateField(PacketPlayOutEntityHeadRotation.class, p2, "a", entityPlayer.getId());
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().playerConnection.sendPacket(packet);
            craftPlayer.getHandle().playerConnection.sendPacket(p2);
        }
    }

    @Override
    public void sneak(boolean b) {
        DataWatcher dw = new DataWatcher(entityPlayer);
        dw.a(0, (byte) (b ? 2 : 0));
        Bukkit.getOnlinePlayers().forEach(player -> {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), dw, true));
        });

    }

    @Override
    public void hit() {
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation(entityPlayer, 0);
        Bukkit.getOnlinePlayers().forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().playerConnection.sendPacket(animation);
        });
    }


    private void setPrivateField(@SuppressWarnings("rawtypes") Class type, Object object, String name, Object value) {
        try {
            Field f = type.getDeclaredField(name);
            f.setAccessible(true);
            f.set(object, value);
            f.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
