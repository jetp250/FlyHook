package com.magmaguy.flyhook;

import static java.lang.Math.abs;
import java.util.Set;
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

    Location targettedLocation;
    Block targettedBlock;
    
    @EventHandler
    public void Grapple(PlayerFishEvent event)
    {      
        //Select player and hook
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        
        //Check what the player is holding
        org.bukkit.inventory.ItemStack playerItem = player.getEquipment().getItemInMainHand();
        
        
        //Block detection
        //Check if the item is a grapppling hook
        if (playerItem.getItemMeta().hasDisplayName())
        {
            //getLogger().info(player + "'s rod has a title.");                   //DEBUG INFO
            
            //Find if player is flying
            EntityPlayer flyingPlayer = ((CraftPlayer)player).getHandle();
            boolean flyingBoolean = flyingPlayer.cB();
            
            //If they are, proceed
            if (flyingBoolean)
            {
                //If the block isn't air, proceed ##TODO - MORE FILTERS (liquids, other)
                if (event.getState() == IN_GROUND)
                {
                        //Vector math
                        //Apply if the block detection occurred correctly
                        Vector test = hook.getLocation().toVector().subtract(player.getLocation().toVector());
                        Vector test2 = test;

                        //getLogger().info(test + " - First vector");                 //DEBUG INFO

                        test = test.normalize();
                        //getLogger().info(test + " - Normalized");                   //DEBUG INFO

                        test.multiply(new Vector(1.5, 1.5, 1.5));

                        double newXVector = abs(test.getX()) * test2.getX();
                        double newYVector = abs(test.getY()) * test2.getY();
                        double newZVector = abs(test.getZ()) * test2.getZ();

                        test2.setX(newXVector);
                        test2.setY(newYVector);
                        test2.setZ(newZVector);

                        player.setVelocity(test2);
                        //getLogger().info(test2 + " - Finalized");                   //DEBUG INFO
                }
                
                //getLogger().info(player + " is flying an Elytra");              //DEBUG INFO
                
                //Check if the player has hit a block
                targettedBlock = player.getTargetBlock((Set<Material>) null, 32);
                
                //Log selected block position
                targettedLocation = targettedBlock.getLocation();
                
                //Teleport hook to the targetted location
                hook.teleport(targettedLocation);
                
                //getLogger().info("teleport location: " + targettedLocation);    //DEBUG INFO
                }
                
        }
        
    }
    
}
