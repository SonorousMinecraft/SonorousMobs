package com.sereneoasis;

import com.sereneoasis.ability.BendingManager;
import com.sereneoasis.ability.config.ConfigManager;
import com.sereneoasis.ability.data.AbilityDataManager;
import com.sereneoasis.ability.data.ArchetypeDataManager;import net.bytebuddy.agent.ByteBuddyAgent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SereneMobs extends JavaPlugin {

    public static SereneMobs plugin;

    private static AbilityDataManager abilityDataManager;

    private static ArchetypeDataManager archetypeDataManager;

    public static ArchetypeDataManager getArchetypeDataManager() {
        return archetypeDataManager;
    }

    public static void main(String[] args) {
        ByteBuddyAgent.install();

    }
    @Override
    public void onEnable(){
        plugin = this;
        getLogger().log(Level.INFO, "SereneMobs was enabled successfully.");

//        this.getCommand("serenemobs").setExecutor(new SerenityCommand());

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new BendingManager(), 0, 1);

        new ConfigManager();

        archetypeDataManager = new ArchetypeDataManager();

        abilityDataManager = new AbilityDataManager();

    }

    @Override
    public void onDisable(){
        super.onDisable();
    }
}
