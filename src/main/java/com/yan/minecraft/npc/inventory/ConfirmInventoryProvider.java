package com.yan.minecraft.npc.inventory;

import com.yan.minecraft.npc.util.item.Item;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ConfirmInventoryProvider implements InventoryProvider {

    private Runnable confirm;
    private Runnable cancel;

    public final SmartInventory doInventory(Runnable confirm, Runnable cancel) {

        this.confirm = confirm;
        this.cancel = cancel;

        return SmartInventory.builder()
                .id("confirmInventory")
                .provider(new ConfirmInventoryProvider())
                .size(3, 9)
                .title(ChatColor.GREEN + "[NPC] Chose an option")
                .closeable(false)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents inventoryContents) {

        inventoryContents.set(1, 4, ClickableItem.of(new Item(Material.GREEN_WOOL).name(ChatColor.GREEN + "CONFIRM").build(), action -> {
            confirm.run();
            player.closeInventory();
        }));

        inventoryContents.set(1, 6, ClickableItem.of(new Item(Material.RED_WOOL).name(ChatColor.RED + "CANCEL").build(), action -> {
            cancel.run();
            player.closeInventory();
        }));

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
