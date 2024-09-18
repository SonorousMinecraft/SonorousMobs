package com.sereneoasis;

import com.sereneoasis.command.SerenityCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SereneMobs extends JavaPlugin {

    public static SereneMobs plugin;

    @Override
    public void onEnable(){
        plugin = this;
        getLogger().log(Level.INFO, "SereneMobs was enabled successfully.");

        this.getCommand("serenemobs").setExecutor(new SerenityCommand());
    }

    @Override
    public void onDisable(){

    }
}
