package com.magmaguy.flyhook;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class FlyHookListener implements Listener {

	private double strength = 2.0;
	private int maxRange = 32;
	private boolean disableFireworks = false;
	private boolean enableCriticals = true;

	// Methods to access the fields from main class
	public void setPower(double strength) {
		this.strength = strength;
	}

	public void setFireworksDisabled(boolean value) {
		this.disableFireworks = value;
	}

	public void setMaxRange(int range) {
		this.maxRange = range;
	}
	
	public void setCriticalsEnabled(boolean value) {
		this.enableCriticals = value;
	}

	// Disabling firework boosts
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		// Check if the player is gliding
		if (!disableFireworks || !event.getPlayer().isGliding()) {
			return;
		}

		Player player = event.getPlayer();

		// Check, if the player had fireworks in hands
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		ItemStack offHand = player.getInventory().getItemInOffHand();

		if (mainHand.getType() == Material.FIREWORK || offHand.getType() == Material.FIREWORK) {
			// Cancel the event
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void grapple(PlayerFishEvent event) {
		// Select the player
		Player player = event.getPlayer();

		// Check, if the player is gliding and fishing
		if (!player.isGliding() || event.getState() != State.FISHING) {
			return;
		}

		// Check which block the player has targetted
		Block targettedBlock = player.getTargetBlock((Set<Material>) null, maxRange);
		Location origin = targettedBlock.getLocation().add(0.5, 0.5, 0.5);
		Material blockMaterial = targettedBlock.getType();

		// Making sure the block is valid, we don't want water and lava to be
		// hookable.
		if (blockMaterial != Material.AIR && !targettedBlock.isLiquid()) {
			// Teleport the hook to the target block
			event.getHook().teleport(origin, TeleportCause.PLUGIN);

			// Play the particles to show that the hook has hit
			player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,
					origin.clone().subtract(player.getLocation().getDirection()), 32, 0, 0, 0, 0.3);

			// Vector math
			// Applies if the block detection occurred correctly
			Vector toHookVector = origin.subtract(player.getLocation()).toVector();

			toHookVector.normalize().multiply(strength).add(player.getVelocity());

			player.setVelocity(toHookVector);

			event.getHook().remove();
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		if (!enableCriticals) {
			return;
		}

		DamageCause cause = event.getCause();
		Entity damager = event.getDamager();

		// Messy, but short and easy way to get the player.
		damager = cause == DamageCause.PROJECTILE ? (Entity) ((Projectile) damager).getShooter() : damager;

		// Make sure the damager was a player.
		if (damager.getType() != EntityType.PLAYER) {
			return;
		}

		Player player = (Player) damager;

		// Player has to be flying
		if (!player.isGliding()) {
			return;
		}

		// Increase the damage.
		event.setDamage(event.getDamage() * 3);
		
	}
}
