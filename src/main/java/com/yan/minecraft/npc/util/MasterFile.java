package com.yan.minecraft.npc.util;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.*;
import org.bukkit.configuration.file.*;

import java.io.*;

import org.bukkit.configuration.*;
import org.bukkit.*;

import java.nio.charset.Charset;
import java.util.*;

public class MasterFile {

    private File file;
    private FileConfiguration fileConfiguration;
    String fileName;

    public MasterFile(final Plugin plugin, String fileName) {
        this(plugin, null, fileName);
    }

    public MasterFile(Plugin plugin, String folder, String fileName) {
        this.fileName = fileName;
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        if (!fileName.isEmpty()) {
            fileName = (fileName.endsWith(".yml") ? fileName : (fileName + ".yml"));
        }

        new File(plugin.getDataFolder() + (folder == null ? "" : File.separator + folder)).mkdirs();

        if (folder == null) {
            this.file = new File(plugin.getDataFolder(), fileName.isEmpty() ? "config.yml" : fileName);
        } else {
            this.file = new File(plugin.getDataFolder(), fileName.isEmpty() ? "config.yml" : folder + File.separator + fileName);
        }

        try {
            if (!file.exists()) {
                if (folder == null) {
                    plugin.saveResource(fileName, false);
                } else {
                    if (!this.file.exists()) {
                        createNewFile();
                    }
                }
            }
            this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
            this.fileConfiguration.loadFromString(Files.toString(file, Charset.forName("UTF-8")));
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
        }
    }

    public void createNewFile() throws IOException {
        this.file.createNewFile();
    }

    public Object get(final String path, Object defaultValue) {
        Object object = this.fileConfiguration.get(path);

        return object == null ? defaultValue : object;
    }

    public Object get(final String path) {
        return this.get(path, null);
    }

    public String getString(final String path) {
        return ChatColor.translateAlternateColorCodes('&', (String) this.get(path, ""));
    }

    public int getInt(final String path) {
        return (int) this.get(path, 0);
    }

    public List<?> getList(final String path) {
        return (List<?>) this.get(path, Lists.newArrayList());
    }

    public List<String> getStringList(final String path) {
        List<String> list = Lists.newArrayList();
        for (String s : (List<String>) this.get(path, Lists.newArrayList())) {
            list.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return list;
    }

    public EntityType getEntityType(final String path) {
        return EntityType.valueOf(getString(path));
    }

    public double getDouble(final String path) {
        return (double) this.get(path, 0.0);
    }

    public float getFloat(final String path) {
        return (float) this.get(path, 0.0);
    }

    public List<Integer> getIntegetList(final String path) {
        return (List<Integer>) this.get(path, Lists.newArrayList());
    }

    public List<Double> getDoubleList(final String path) {
        return (List<Double>) this.get(path, Lists.newArrayList());
    }

    public List<Float> getFloatList(final String path) {
        return (List<Float>) this.get(path, Lists.newArrayList());
    }

    public boolean getBoolean(final String path) {
        return (Boolean) this.get(path, false);
    }

    public void set(final String path, final Object value) {
        this.fileConfiguration.set(path, value);
    }

    public ConfigurationSection getConfigurationSection(final String path) {
        return this.fileConfiguration.getConfigurationSection(path);
    }

    public boolean contains(final String path) {
        return this.get(path) != null;
    }

    public void setLocation(String path, Location location) {
        if (location == null) {
            this.set(path, null);
            return;
        }
        String locationString = location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ() + ";" + location.getYaw() + ";" + location.getPitch();
        this.set(path, locationString);
    }

    public Location getLocation(String path) {
        String locationString = (String) this.get(path, null);

        if (locationString == null) {
            return null;
        }

        String[] locationSplit = locationString.split(";");

        String world = locationSplit[0];
        int x = Integer.valueOf(locationSplit[1]);
        int y = Integer.valueOf(locationSplit[2]);
        int z = Integer.valueOf(locationSplit[3]);
        float yaw = Float.valueOf(locationSplit[4]);
        float pitch = Float.valueOf(locationSplit[5]);

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}