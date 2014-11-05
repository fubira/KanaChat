package net.ironingot.nihongochat;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.entity.Player;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.SpongeEventHandler;
import org.spongepowered.api.event.message.CommandEvent;
import org.spongepowered.api.util.command.CommandSource;

public class NihongoChatCommand {
    private NihongoChat plugin;
    private String pluginName;
    private String pluginVersion;

    public NihongoChatCommand(NihongoChat plugin) {
        this.plugin = plugin;
        this.pluginName = "NihongoChat";
        this.pluginVersion = "1.0";
    }

    @SpongeEventHandler(order=Order.DEFAULT)
    public boolean onCommand(CommandEvent event) {
        plugin.logger.info("Command: " +  event.getCommand() + " Arg: " + event.getArguments());

        String command = event.getCommand();        
        String args[] = event.getArguments().split(" ");        

        String cmd = (args.length >= 1) ? args[0].toLowerCase() : "get";
        String option  = (args.length >= 2) ? args[1].toLowerCase() : null;

        try {
            Player player = (Player)event.getSource();

            return executeCommand(player, cmd, option);
        } catch (ClassCastException e) {
            plugin.logger.warn("Command sender is not a player: " + e.toString());
        } 
        return true;
    }

    private boolean executeCommand(Player sender, String command, String option) {
        if (command != null && command.equals("version")) {
            sender.sendMessage(/*ChatColor.GOLD +*/ this.pluginName + "-" + this.pluginVersion);
            return true;
        }

        if (command != null && command.equals("kanji")) {
            if (option.equals("on") || option.equals("true")) {
                plugin.getConfigHandler().setUserKanjiConversion(sender.getName(), Boolean.TRUE);
            }
            if (option.equals("off") || option.equals("false")) {
                plugin.getConfigHandler().setUserKanjiConversion(sender.getName(), Boolean.FALSE);
            }

            if (plugin.getConfigHandler().getUserKanjiConversion(sender.getName())) {
                sender.sendMessage(/*ChatColor.GOLD +*/ pluginName + " Kanji conversion is active.");
            } else {
                sender.sendMessage(/*ChatColor.GOLD +*/ pluginName + " Kanji conversion is inactive.");
            }
            return true;
        }

        if (command != null) {
            if (command.equals("on") || command.equals("true")) {
                plugin.getConfigHandler().setUserMode(sender.getName(), Boolean.TRUE);
            }
            if (command.equals("off") || command.equals("false")) {
                plugin.getConfigHandler().setUserMode(sender.getName(), Boolean.FALSE);
            }
        }

        if (plugin.getConfigHandler().getUserMode(sender.getName()) == Boolean.TRUE) {
            sender.sendMessage(/*ChatColor.GOLD +*/ pluginName + " is active.");
        } else {
            sender.sendMessage(/*ChatColor.GOLD +*/ pluginName + " is inactive.");
        }
        return true;
    }
}