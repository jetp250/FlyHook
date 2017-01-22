package com.magmaguy.flyhook;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
	private Material GRAPPLING_HOOK = Material.FISHING_ROD;

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

	// Disabling firework boosts
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		// Check if the player is gliding
		if (!disableFireworks || !event.getPlayer().isGliding()) {
			return;
		}

		Player p = event.getPlayer();

		// Check, if the player had fireworks in hands
		ItemStack mainHand = p.getInventory().getItemInMainHand();
		ItemStack offHand = p.getInventory().getItemInOffHand();

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

		// Check what the player is holding
		ItemStack mainHand = player.getInventory().getItemInMainHand();
		ItemStack offHand = player.getInventory().getItemInOffHand();

		// Check for the fishing rod
		if (mainHand.getType() != GRAPPLING_HOOK && offHand.getType() != GRAPPLING_HOOK) {
			return;
		}

		// Check which block the player has targetted
		Block targettedBlock = player.getTargetBlock((Set<Material>) null, maxRange);
		Location origin = targettedBlock.getLocation().add(0.5, 0.5, 0.5);
		Material blockMaterial = targettedBlock.getType();

		// Making sure the block is valid, we don't want water and lava to be hookable.
		if (targettedBlock != null && blockMaterial != Material.AIR && !targettedBlock.isLiquid()) {
			// Teleport the hook to the target block
			event.getHook().teleport(origin, TeleportCause.PLUGIN);

			// Play the particles to show that the hook has hit
			player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,
					origin.clone().subtract(player.getLocation().getDirection()), 32, 0, 0, 0, 0.3);

			// Vector math
			// Applies if the block detection occurred correctly
			Vector toHookVector = origin.subtract(player.getLocation()).toVector();

			toHookVector.normalize().multiply(strength).add(player.getVelocity().normalize());

			player.setVelocity(toHookVector);
		}
	}

	// Allow players to mount an enderdragon if they have a fishing hook
	@EventHandler
	public void dragonHijack(EntityDamageByEntityEvent event) {

		if (event.getEntityType().equals(EntityType.ENDER_DRAGON) && event.getDamager() instanceof Player) {

			Player player = (Player) event.getDamager();

			if (player.getInventory().getItemInMainHand().getType() == GRAPPLING_HOOK
					|| player.getInventory().getItemInOffHand().getType() == GRAPPLING_HOOK) {

				// Mount the player
				event.getEntity().setPassenger(player);

			}
		}
	}

	@EventHandler
	public void damageCrits(EntityDamageEvent event) {

		Entity potentialDragon = event.getEntity();

		if (event.getEntityType() == EntityType.ENDER_DRAGON) {

			LivingEntity dragonLivingEntity = (LivingEntity) potentialDragon;
			Entity passenger = dragonLivingEntity.getPassenger();

			// Check if the passenger is actually a player, to not break plugins
			// that work with passengers.
			if (passenger != null && passenger instanceof Player) {

				// Apply the crit damage to the dragon
				double critBonus = dragonLivingEntity.getLastDamage() * 4;
				event.setDamage(event.getDamage() + critBonus);

			}
		}
	}
}
