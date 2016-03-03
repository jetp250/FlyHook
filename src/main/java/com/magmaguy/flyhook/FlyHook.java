package com.magmaguy.flyhook;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import static org.bukkit.event.player.PlayerFishEvent.State.IN_GROUND;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class FlyHook  extends JavaPlugin implements Listener{
    
    //Determine behaviour on startup
    @Override
    public void onEnable(){
        
        getLogger().info("FlyHook - enabled!");
        this.getServer().getPluginManager().registerEvents(this, this);

        
    }
    
    //Determine behaviour on shutdown
    @Override
    public void onDisable(){
        
        getLogger().info("FlyHook - shutting down!");
        
    }
    
    public void FlyHook(){
        
    }
    
    @EventHandler
    public void Fishing(PlayerFishEvent event)
    {
        Player player = event.getPlayer();
        
        if (event.getState() == IN_GROUND)
        {
            String test = player.getDisplayName();
            getLogger().info(test);
            Vector vector = new Vector(0, 3, 0);
            player.setVelocity(vector);
        }
        
    }
    
}
