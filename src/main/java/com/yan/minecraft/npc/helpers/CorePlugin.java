package com.yan.minecraft.npc.helpers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class CorePlugin extends JavaPlugin {

    public String serverVersion;

    @Override
    public void onEnable() {
        enable();
        try {
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        try {
            disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
        load();
    }

    public abstract void enable();

    public abstract void disable() throws SQLException;

    public abstract void load();

    public List<RegisteredServiceProvider<?>> getServices() {
        return Bukkit.getServicesManager().getRegistrations(this);
    }

    public <T> T getService(Class<T> service) {
        Objects.requireNonNull(service, "clazz");

        return Optional
                .ofNullable(Bukkit.getServicesManager().getRegistration(service))
                .map(RegisteredServiceProvider::getProvider)
                .orElseThrow(() -> new IllegalStateException("No registration present for service '" + service.getName() + "'"));
    }

    public <T> T provideService(Class<T> clazz, T instance, ServicePriority priority) {
        Objects.requireNonNull(clazz, "clazz");
        Objects.requireNonNull(instance, "instance");
        Objects.requireNonNull(priority, "priority");

        Bukkit.getServicesManager().register(clazz, instance, this, priority);

        return instance;
    }

    public <T> T provideService(Class<T> clazz, T instance) {
        provideService(clazz, instance, ServicePriority.Normal);
        return instance;
    }

    public void listeners(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void commands(Plugin plugin, Command... commands) {
        for (Command command : commands) {
            ((CraftServer) plugin.getServer()).getCommandMap().register(plugin.getName().toLowerCase(), command);
        }
    }

    public String getServerVersion() {
        return serverVersion;
    }
}
