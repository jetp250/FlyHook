package com.magmaguy.flyhook;

import static java.lang.Math.abs;
import java.util.Set;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import static org.bukkit.event.player.PlayerFishEvent.State.FISHING;
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
    
    Location targettedBlock;
    Material blockMaterial;
    
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
                //getLogger().info(player + " is flying an Elytra");              //DEBUG INFO
                
                //If the block isn't air, proceed ##TODO - MORE FILTERS (liquids, other)
                if (targettedBlock != null && blockMaterial != Material.AIR)
                {
                        //Vector math
                        //Apply if the block detection occurred correctly
                        Vector toHookVector = targettedBlock.toVector().subtract(player.getLocation().toVector());
                        Vector toHookVectorCopy = toHookVector;

                        //getLogger().info(toHookVector + " - First vector");                 //DEBUG INFO

                        toHookVector = toHookVector.normalize();
                        //getLogger().info(toHookVector + " - Normalized");                   //DEBUG INFO

                        toHookVector.multiply(new Vector(1.0, 1.0, 1.0));

                        double newXVector = abs(toHookVector.getX()) * toHookVectorCopy.getX() + player.getVelocity().getX();
                        double newYVector = abs(toHookVector.getY()) * toHookVectorCopy.getY() + player.getVelocity().getY();
                        double newZVector = abs(toHookVector.getZ()) * toHookVectorCopy.getZ() + player.getVelocity().getZ();

                        toHookVectorCopy.setX(newXVector);
                        toHookVectorCopy.setY(newYVector);
                        toHookVectorCopy.setZ(newZVector);

                        player.setVelocity(toHookVectorCopy);
                        //getLogger().info(toHookVectorCopy + " - Finalized");                   //DEBUG INFO
                        
                        targettedBlock = null;
                        
                }
                
                if (event.getState() == FISHING)
                {
                    //Check which block the player has targetted
                    targettedBlock = player.getTargetBlock((Set<Material>) null, 32).getLocation();
                    blockMaterial = player.getTargetBlock((Set<Material>) null, 32).getType();
                    
                    //Teleport hook to the targetted location
                    hook.teleport(targettedBlock);
                    
                    //getLogger().info("teleport location: " + targettedLocation);    //DEBUG INFO
                
                }
                
                if (targettedBlock != null && blockMaterial != Material.AIR)
                {
                    
                    player.getWorld().playEffect(player.getLocation(), Effect.COLOURED_DUST, null);
                    
                }
                
            }
                
        }
        
    }
    
}
