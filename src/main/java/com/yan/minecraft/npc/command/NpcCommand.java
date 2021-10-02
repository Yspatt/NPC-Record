package com.yan.minecraft.npc.command;

import com.yan.minecraft.npc.NPCPlugin;
import com.yan.minecraft.npc.model.NPC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class NpcCommand extends Command {

    public NpcCommand() {
        super("npc");
    }

    public Map<String, String> cachedString = NPCPlugin.getInstance().getConfigController().getCachedValues();

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] arguments) {

        if (commandSender instanceof Player player) {
            if (arguments.length == 0) {
                player.sendMessage(cachedString.get("npc-command-help"));
                return true;
            }

            if (!player.hasPermission(" npc.admin." + arguments[0])) {
                player.sendMessage(cachedString.get("npc-no-permission"));
                return true;
            }

            if (arguments[0].equalsIgnoreCase("create")) {
                if (arguments.length < 2) {

                    /*
                     * Send usage from create argument
                     */
                    player.sendMessage(cachedString.get("npc-command-create-help"));
                    return true;

                }
                if (NPCPlugin.getInstance().getNpcController().get(arguments[1]).isPresent()) {

                    /*
                     * The NPC already exists with that name
                     */
                    player.sendMessage(cachedString.get("npc-command-create-already-exists").replace("{name}", arguments[1]));
                    return true;

                }

                NPCPlugin.getInstance().getConfirmInventoryProvider().doInventory(() -> {

                    /*
                     * Create NPC
                     */
                    NPCPlugin.getInstance().getNpcController().createNPC(arguments[1], arguments[2], player.getLocation(), UUID.randomUUID());
                    player.sendMessage(cachedString.get("npc-command-create-created").replace("{name}", arguments[1]));

                }, () -> {

                    /*
                     * Cancel create NPC operation
                     */
                    player.sendMessage(cachedString.get("npc-command-operation-cancel"));

                }).open(player);


            } else if (arguments[0].equalsIgnoreCase("rec")) {
                if (arguments.length < 3) {

                    /*
                     * Send usage from rec argument
                     */
                    player.sendMessage(cachedString.get("npc-command-rec-help"));
                    return true;

                }
                NPC find = NPCPlugin.getInstance().getNpcController().get(arguments[1]).orElse(null);
                if (find != null) {

                    /*
                     * Sends a message that the recording will start in 3 seconds
                     */
                    player.sendMessage(cachedString.get("npc-command-rec-warning"));
                    new BukkitRunnable() {
                        private int i = 0;

                        @Override
                        public void run() {
                            /*
                             * Recording....
                             */
                            NPCPlugin.getInstance().getNpcController().recordings.put(player, find);
                            player.sendMessage(cachedString.get("npc-command-rec-recording").replace("{time}", "" + i));
                            if (i >= Integer.parseInt(arguments[2])) {
                                /*
                                 * The recording has timed out and is ending
                                 */
                                NPCPlugin.getInstance().getNpcController().recordings.remove(player);
                                player.sendMessage(cachedString.get("npc-command-rec-stop"));
                                cancel();
                            }
                            i++;
                        }
                    }.runTaskTimer(NPCPlugin.getInstance(), 3 * 20, 20);
                }


            } else if (arguments[0].equalsIgnoreCase("play")) {
                if (arguments.length < 2) {

                    /*
                     * Send usage from play argument
                     */
                    player.sendMessage(cachedString.get("npc-command-play-help"));
                    return true;
                }
                /*
                 * Run record
                 */
                player.sendMessage(cachedString.get("npc-command-play-playing").replace("{name}", arguments[1]));
                NPCPlugin.getInstance().getNpcController().get(arguments[1]).ifPresent(npc -> {
                    npc.spawn();
                    NPCPlugin.getInstance().getNpcController().replay(npc);
                });


            } else if (arguments[0].equalsIgnoreCase("delete")) {
                if (arguments.length < 2) {
                    /*
                     * Send usage from delete argument
                     */
                    player.sendMessage(cachedString.get("npc-command-delete-help"));
                    return true;
                }
                if (NPCPlugin.getInstance().getNpcController().get(arguments[1]).isEmpty()) {
                    /*
                     * Alert NPC doesn't exists
                     */
                    player.sendMessage(cachedString.get("npc-command-delete-not-exists").replace("{name}", arguments[1]));
                    return true;
                }
                NPCPlugin.getInstance().getConfirmInventoryProvider().doInventory(() -> {
                    NPCPlugin.getInstance().getNpcController().delete(arguments[1]);
                    player.sendMessage(cachedString.get("npc-command-delete-deleted").replace("{name}", arguments[1]));
                }, () -> {
                    /*
                     * Cancel delete NPC operation
                     */
                    player.sendMessage(cachedString.get("npc-command-operation-cancel"));
                }).open(player);
            } else {
                player.sendMessage(cachedString.get("npc-command-help"));
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "[NPC] Only for players");
        }
        return false;
    }
}
