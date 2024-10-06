package com.sereneoasis.util;

import com.sereneoasis.SereneMobs;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class Scheduler {

    public static void performTaskLater(long time, Task task) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskLater(SereneMobs.plugin, task::doTask, time);
    }

    public interface Task {
        void doTask();
    }
}
