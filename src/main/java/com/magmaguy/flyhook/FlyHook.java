package com.magmaguy.flyhook;

import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import static org.bukkit.event.player.PlayerFishEvent.State.FAILED_ATTEMPT;
import static org.bukkit.event.player.PlayerFishEvent.State.FISHING;
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
        
        //Check if player is flying
        EntityPlayer flyingPlayer = ((CraftPlayer)player).getHandle();
        boolean flyingBoolean = flyingPlayer.cB();
        
        if (flyingBoolean)
        {

            //Boost hook speed
            Vector newHookVelocity = hook.getVelocity().clone();

            newHookVelocity.multiply(new Vector(6, 6, 6));

            hook.setVelocity(newHookVelocity);

            //Create a boolean to know if the hook has landed
            boolean hookLanded;

            //Find hook location
            Location hookLocation = hook.getLocation();

            //Set coordinates to the block under the hook
            hookLocation.setY(hookLocation.getY() - 1);

            //Set up backup locations due to buggy detection
            Location hookLocationBackup = hookLocation;
            hookLocationBackup.setY(hookLocationBackup.getY() - 0.5);

            Location hookLocationBackup2 = hookLocation;
            hookLocationBackup2.setY(hookLocationBackup2.getY() - 1);

            //Find out what block that is
            Block hookBlock = hookLocation.getBlock();
            Block hookBlockBackup1 = hookLocationBackup.getBlock();
            Block hookBlockBackup2 = hookLocationBackup2.getBlock();

            if(hookBlock.getType() == Material.AIR || hookBlockBackup1.getType() == Material.AIR 
                    || hookBlockBackup2.getType() == Material.AIR)
            {
                hookLanded = false;
                //getLogger().info("Failed coordinates: " + hookLocation); //Debug code
            }
            else
            {
                hookLanded = true;
            }

            //If the hook has landed (or is in the ground), proceed with the grapple
            if (hookLanded && event.getState() == FISHING || 
                    event.getState() == IN_GROUND && event.getState() == FISHING)
            {
                //Debug code
                String test = player.getDisplayName();
                getLogger().info(test + " has cast a valid line");

                //Change player velocity
                Vector newPlayerVelocity = player.getVelocity();
                newPlayerVelocity.multiply(new Vector(2, 1, 2));
                newPlayerVelocity.add(new Vector(0, 1, 0));

                player.setVelocity(newPlayerVelocity);

            }
        
        }
        
    }
    
}
