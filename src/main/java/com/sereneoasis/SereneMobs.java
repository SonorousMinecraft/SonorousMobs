package com.sereneoasis;

import com.sereneoasis.command.SerenityCommand;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SereneMobs extends JavaPlugin {

    public static SereneMobs plugin;

    public static void main(String[] args) {
        ByteBuddyAgent.install();

    }
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
