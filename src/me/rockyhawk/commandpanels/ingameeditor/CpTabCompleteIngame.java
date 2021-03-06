package me.rockyhawk.commandpanels.ingameeditor;

import me.rockyhawk.commandpanels.CommandPanels;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class CpTabCompleteIngame implements TabCompleter {
    CommandPanels plugin;
    public CpTabCompleteIngame(CommandPanels pl) { this.plugin = pl; }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player && args.length == 1){
            Player p = ((Player) sender).getPlayer();
            if(label.equalsIgnoreCase("cpe") || label.equalsIgnoreCase("cpanele") || label.equalsIgnoreCase("commandpaneledit")){
                ArrayList<String> apanels = new ArrayList<String>(); //all panels
                String tpanels; //tpanels is the temp to check through the files
                try {
                    for(String fileName : plugin.panelFiles) { //will loop through all the files in folder
                        YamlConfiguration temp = YamlConfiguration.loadConfiguration(new File(plugin.panelsf + File.separator + fileName));
                        String key;
                        tpanels = "";
                        if(!plugin.checkPanels(temp)){
                            continue;
                        }
                        for (Iterator var10 = Objects.requireNonNull(temp.getConfigurationSection("panels")).getKeys(false).iterator(); var10.hasNext(); tpanels = tpanels + key + " ") {
                            key = (String) var10.next();
                            if(!key.startsWith(args[0])){
                                //this will narrow down the panels to what the user types
                                continue;
                            }
                            if(sender.hasPermission("commandpanel.panel." + temp.getString("panels." + key + ".perm"))) {
                                if(temp.contains("panels." + key + ".disabled-worlds")){
                                    List<String> disabledWorlds = temp.getStringList("panels." + key + ".disabled-worlds");
                                    assert p != null;

                                    if(!disabledWorlds.contains(p.getWorld().getName())){
                                        apanels.add(key);
                                    }
                                }else{
                                    apanels.add(key);
                                }
                            }
                        }
                        //if file contains opened panel then start
                    }
                }catch(Exception fail){
                    //could not fetch all panel names (probably no panels exist)
                }
                return apanels;
            }
        }
        return null;
    }
}