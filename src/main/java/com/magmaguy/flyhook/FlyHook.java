package com.magmaguy.flyhook;

import static java.lang.Math.abs;
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
            Location hookLocationXPos = hook.getLocation();
            Location hookLocationXNeg = hook.getLocation();
            
            Location hookLocationYPos = hook.getLocation();
            Location hookLocationYNeg = hook.getLocation();
            
            Location hookLocationZPos = hook.getLocation();
            Location hookLocationZNeg = hook.getLocation();

            //Set up backup locations due to buggy detection
            Location hookLocationBackupXPos = hookLocationXPos;
            Location hookLocationBackupXNeg = hookLocationXNeg;
            
            Location hookLocationBackupYPos = hookLocationYPos;
            Location hookLocationBackupYNeg = hookLocationYNeg;
            
            Location hookLocationBackupZPos = hookLocationZPos;
            Location hookLocationBackupZNeg = hookLocationZNeg;
            
            
            Location hookLocationBackupX2Pos = hookLocationXPos;
            Location hookLocationBackupX2Neg = hookLocationXNeg;
            
            Location hookLocationBackupY2Pos = hookLocationYPos;
            Location hookLocationBackupY2Neg = hookLocationYNeg;
            
            Location hookLocationBackupZ2Pos = hookLocationZPos;
            Location hookLocationBackupZ2Neg = hookLocationZNeg;
            
            
            //Set coordinates to the block under the hook
            hookLocationXPos.setX(hookLocationXPos.getX() + 0.5);
            hookLocationXNeg.setX(hookLocationXNeg.getX() - 0.5);
            
            hookLocationYPos.setY(hookLocationYPos.getY() + 0.5);
            hookLocationYNeg.setY(hookLocationYNeg.getY() - 0.5);
            
            hookLocationZPos.setZ(hookLocationZPos.getZ() + 0.5);
            hookLocationZNeg.setZ(hookLocationZNeg.getZ() - 0.5);
            
            //Set the backup locations to the correct place
            hookLocationBackupXPos.setX(hookLocationBackupXPos.getX() + 1);
            hookLocationBackupXNeg.setX(hookLocationBackupXNeg.getX() - 1);
            
            hookLocationBackupYPos.setY(hookLocationBackupYPos.getY() + 1);
            hookLocationBackupYNeg.setY(hookLocationBackupYNeg.getY() - 1);
            
            hookLocationBackupZPos.setY(hookLocationBackupZPos.getZ() + 1);
            hookLocationBackupZNeg.setY(hookLocationBackupZNeg.getZ() - 1);
            

            hookLocationBackupX2Pos.setX(hookLocationBackupX2Pos.getX() + 2);
            hookLocationBackupX2Neg.setX(hookLocationBackupX2Neg.getX() - 2);
            
            hookLocationBackupY2Pos.setY(hookLocationBackupY2Pos.getY() + 2);
            hookLocationBackupY2Neg.setY(hookLocationBackupY2Neg.getY() - 2);
            
            hookLocationBackupZ2Pos.setY(hookLocationBackupZ2Pos.getZ() + 2);
            hookLocationBackupZ2Neg.setY(hookLocationBackupZ2Neg.getZ() - 2);

            //Find out what those blocks are
            Block hookBlockXPos = hookLocationXPos.getBlock();
            Block hookBlockXNeg = hookLocationYNeg.getBlock();
            
            Block hookBlockYPos = hookLocationYPos.getBlock();
            Block hookBlockYNeg = hookLocationYNeg.getBlock();
            
            Block hookBlockZPos = hookLocationZPos.getBlock();
            Block hookBlockZNeg = hookLocationZNeg.getBlock();
            
            
            Block hookBlockBackupXPos = hookLocationBackupXPos.getBlock();
            Block hookBlockBackupXNeg = hookLocationBackupXNeg.getBlock();
            
            Block hookBlockBackupYPos = hookLocationBackupYPos.getBlock();
            Block hookBlockBackupYNeg = hookLocationBackupYNeg.getBlock();
            
            Block hookBlockBackupZPos = hookLocationBackupYPos.getBlock();
            Block hookBlockBackupZNeg = hookLocationBackupYNeg.getBlock();
            
            
            Block hookBlockBackupX2Pos = hookLocationBackupX2Pos.getBlock();
            Block hookBlockBackupX2Neg = hookLocationBackupX2Neg.getBlock();
            
            Block hookBlockBackupY2Pos = hookLocationBackupY2Pos.getBlock();
            Block hookBlockBackupY2Neg = hookLocationBackupY2Neg.getBlock();
            
            Block hookBlockBackupZ2Pos = hookLocationBackupY2Pos.getBlock();
            Block hookBlockBackupZ2Neg = hookLocationBackupY2Neg.getBlock();


            if(hookBlockXPos.getType() == Material.AIR || hookBlockXNeg.getType() == Material.AIR 
                    || hookBlockYPos.getType() == Material.AIR || hookBlockYNeg.getType() == Material.AIR
                    || hookBlockZPos.getType() == Material.AIR || hookBlockZNeg.getType() == Material.AIR
                    || hookBlockBackupXPos.getType() == Material.AIR || hookBlockBackupXNeg.getType() == Material.AIR
                    || hookBlockBackupYPos.getType() == Material.AIR || hookBlockBackupYNeg.getType() == Material.AIR
                    || hookBlockBackupZPos.getType() == Material.AIR || hookBlockBackupZNeg.getType() == Material.AIR
                    || hookBlockBackupX2Pos.getType() == Material.AIR || hookBlockBackupX2Neg.getType() == Material.AIR
                    || hookBlockBackupY2Pos.getType() == Material.AIR || hookBlockBackupY2Neg.getType() == Material.AIR
                    || hookBlockBackupZ2Pos.getType() == Material.AIR || hookBlockBackupZ2Neg.getType() == Material.AIR
                    )
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
                    newPlayerVelocity.multiply(new Vector(2, 1, 2));
                    newPlayerVelocity.add(new Vector(0, 0.5, 0));

                    player.setVelocity(newPlayerVelocity);
                }
                
                //Higher speed pull case
                else
                {
                    newPlayerVelocity.multiply(new Vector(1.5, 1, 1.5));
                    newPlayerVelocity.add(new Vector(0, 0.5, 0));
                    
                    player.setVelocity(newPlayerVelocity);
                }

            }
        
        }
        
    }
    
}
