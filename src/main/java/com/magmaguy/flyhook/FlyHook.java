package com.magmaguy.flyhook;

import static java.lang.Math.abs;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
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
            Location hookLocationYNeg = hook.getLocation();

            //Set up backup locations due to buggy detection
            Location hookLocationBackupYNeg = hookLocationYNeg;
            
            Location hookLocationBackupY2Neg = hookLocationYNeg;
            
            //Set coordinates to the block under the hook
            hookLocationYNeg.setY(hookLocationYNeg.getY() - 0.5);
            
            //Set the backup locations to the correct place
            hookLocationBackupYNeg.setY(hookLocationBackupYNeg.getY() - 1);
            
            hookLocationBackupY2Neg.setY(hookLocationBackupY2Neg.getY() - 2);

            //Find out what those blocks are            
            Block hookBlockYNeg = hookLocationYNeg.getBlock();

            Block hookBlockBackupYNeg = hookLocationBackupYNeg.getBlock();
            
            Block hookBlockBackupY2Neg = hookLocationBackupY2Neg.getBlock();


            if(hookBlockYNeg.getType() == Material.AIR || hookBlockBackupYNeg.getType() == Material.AIR
                    || hookBlockBackupY2Neg.getType() == Material.AIR)
            {
                hookLanded = false;
                //getLogger().info("Failed coordinates: " + hookLocationY); //Debug code
            }
            else
            {
                hookLanded = true;
            }

            //If the hook has landed (or is in the ground), proceed with the grapple
            if (hookLanded && event.getState() == FISHING || 
                    event.getState() == IN_GROUND)
            {
                //Debug code
                String test = player.getDisplayName();
                getLogger().info(test + " has cast a valid line");

                //Create new vector for swapping
                Vector newPlayerVelocity = player.getVelocity();
                
                //Handle slow speed pulls
                double playerVelocityX = abs(newPlayerVelocity.getX());
                double playerVelocityY = abs(newPlayerVelocity.getY());
                double playerVelocityZ = abs(newPlayerVelocity.getZ());
                
                double playerVelocityTotal = playerVelocityX + playerVelocityY + playerVelocityZ;
                
                double slowVelocityX = 2;
                
                //Slow speed pull case
                if(playerVelocityTotal < slowVelocityX)
                {
                    newPlayerVelocity.multiply(new Vector(2, 2, 2));
                    //newPlayerVelocity.add(new Vector(0, 0.5, 0));             //Experimental harder use

                    player.setVelocity(newPlayerVelocity);
                }
                
                //Regular speed pull case
                else
                {
                    newPlayerVelocity.multiply(new Vector(1.5, 1.5, 1.5));
                    //newPlayerVelocity.add(new Vector(0, 0.5, 0));             //Experimental harder use
                    
                    player.setVelocity(newPlayerVelocity);
                }

            }
        
        }
        
    }
    
}
