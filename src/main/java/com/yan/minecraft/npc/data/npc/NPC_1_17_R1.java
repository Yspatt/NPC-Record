package com.yan.minecraft.npc.data.npc;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.yan.minecraft.npc.NPCPlugin;
import com.yan.minecraft.npc.data.frame.Frame;
import com.yan.minecraft.npc.model.NPC;
import com.yan.minecraft.npc.data.npc.customplayer.CustomPlayer_1_17_R1;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityPose;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class NPC_1_17_R1 implements NPC {

    private CustomPlayer_1_17_R1 entityPlayer;
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
        String[] a = NPCPlugin.getInstance().getNpcController().getNpcSkin(skin);
        gameProfile.getProperties().put("textures", new Property("textures", a[0], a[1]));
        entityPlayer = new CustomPlayer_1_17_R1(nmsServer, worldServer, gameProfile);

        entityPlayer.setLocation(frames.get(0).getLocation().getX(), frames.get(0).getLocation().getY(), frames.get(0).getLocation().getZ(), frames.get(0).getLocation().getYaw(), frames.get(0).getLocation().getPitch());

        Bukkit.getOnlinePlayers().forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            PacketPlayOutPlayerInfo addPlayerPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, entityPlayer);
            PacketPlayOutPlayerInfo removePlayerPacket = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b, entityPlayer);
            PacketPlayOutNamedEntitySpawn spawnPlayerPacket = new PacketPlayOutNamedEntitySpawn(entityPlayer);
            craftPlayer.getHandle().b.sendPacket(addPlayerPacket);
            craftPlayer.getHandle().b.sendPacket(spawnPlayerPacket);
            craftPlayer.getHandle().b.sendPacket(removePlayerPacket);
        });
    }

    @Override
    public void destroy() {
        PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityPlayer.getId());
        Bukkit.getOnlinePlayers().forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().b.sendPacket(destroy);
        });
    }

    @Override
    public void walk(Frame record, Frame oldrecord) {
        short x = (short) ((record.getLocation().getX() * 32 - oldrecord.getLocation().getX() * 32) * 128);
        short y = (short) ((record.getLocation().getY() * 32 - oldrecord.getLocation().getY() * 32) * 128);
        short z = (short) ((record.getLocation().getZ() * 32 - oldrecord.getLocation().getZ() * 32) * 128);
        PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(entityPlayer.getId(), x, y, z, (byte) record.getLocation().getYaw(), (byte) record.getLocation().getPitch(), true);
        PacketPlayOutEntityHeadRotation rotation = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) record.getLocation().getYaw());
        for (Player player : Bukkit.getOnlinePlayers()) {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().b.sendPacket(packet);
            craftPlayer.getHandle().b.sendPacket(rotation);
        }
    }

    @Override
    public void sneak(boolean b) {
        DataWatcher dw = new DataWatcher(entityPlayer);
        dw.register(new DataWatcherObject<>(6, DataWatcherRegistry.s), (b ? EntityPose.f : EntityPose.a));
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), dw, true);
        Bukkit.getOnlinePlayers().forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().b.sendPacket(metadata);
        });
    }

    @Override
    public void hit() {
        PacketPlayOutAnimation animation = new PacketPlayOutAnimation(entityPlayer, 0);
        Bukkit.getOnlinePlayers().forEach(player -> {
            CraftPlayer craftPlayer = ((CraftPlayer) player);
            craftPlayer.getHandle().b.sendPacket(animation);
        });
    }
}
