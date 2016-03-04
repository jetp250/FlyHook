package com.magmaguy.flyhook;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FishHook;
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
    public void Grapple(PlayerFishEvent event)
    {
        
        //Select player and hook
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        
        //Boost hook speed
        Vector newHookSpeed = hook.getVelocity().clone();
        
        newHookSpeed.multiply(new Vector(4, 4, 4));
        
        hook.setVelocity(newHookSpeed);
        
        //Create a boolean to know if the hook has landed
        boolean hookLanded;
        
        //Find hook location
        Location hookLocation = hook.getLocation();
        
        //Set coordinates to the block under the hook
        hookLocation.setY(hookLocation.getY() - 1.5);
        
        //Find out what block that is
        Block hookBlock = hookLocation.getBlock();
        
        if(hookBlock.getType() == Material.AIR)
        {
            hookLanded = false;
            getLogger().info("Failed coordinates: " + hookLocation);
        }
        else
        {
            hookLanded = true;
        }
        
        //If the hook has landed (or is in the ground), proceed with the grapple
        if (hookLanded || event.getState() == IN_GROUND)
        {
            String test = player.getDisplayName();
            getLogger().info(test + " has cast a valid line");
            Vector vector = new Vector(0, 1, 0);
            player.setVelocity(vector);
        }
        
    }
    
}
