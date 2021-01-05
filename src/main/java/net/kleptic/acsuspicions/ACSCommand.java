package net.kleptic.acsuspicions;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ACSCommand extends Command {
    
    ACSuspicions plugin;
    
    public ACSCommand(ACSuspicions plugin, String name) {
        super(name);
        this.plugin = plugin;
    }
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        switch(args[0]){
        case "report":
        
        if(sender.hasPermission("acsuspicions.report")){
            String playerName = args[1];
            UUID player = ProxyServer.getInstance().getPlayer(playerName).getUniqueId();
            String server = args[2];
            String hacktype = args[3];
            plugin.getProxy().getScheduler().runAsync(plugin, () -> {
                
                int previousSuspicions = plugin.getDatabase().countSuspicions(player);
                plugin.getDatabase().addSuspicion(new Suspicion(player, new Timestamp(System.currentTimeMillis()), hacktype, server));
                List<String> commands;
                if(plugin.getConfig().getSection("actions").getKeys().contains(Integer.toString(previousSuspicions))) commands = plugin.getConfig().getStringList("actions."+previousSuspicions);
                else commands = plugin.getConfig().getStringList("actions.n");
                plugin.getProxy().getScheduler().schedule(plugin, () -> {
                    for(String command : commands){
                        String actualCommand = command.replace("{PLAYER}", playerName)
                                .replace("{SERVER}", server)
                                .replace("{HACK}", hacktype)
                                .replace("{N}", Integer.toString(previousSuspicions))
                                .replace("{N+1}", Integer.toString(previousSuspicions+1));
                        
                        
                        plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), actualCommand);
                    }
                }, 0, TimeUnit.MILLISECONDS);
                
                
                
            });
            
        }
        case "reload":
            if(sender.hasPermission("acsuspicions.reload")){
                plugin.reload();
            }
    }}
}
