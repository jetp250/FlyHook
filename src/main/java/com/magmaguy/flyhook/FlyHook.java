package com.magmaguy.flyhook;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class FlyHook extends JavaPlugin {

	private FileConfiguration config;
	private File configFile;
	private FlyHookListener listener;

	@Override
	public void onEnable() {

		// Register the listener class and initialize the variable.
		this.getServer().getPluginManager().registerEvents(listener = new FlyHookListener(), this);

		// Setup & load config.
		this.configFile = new File(this.getDataFolder(), "Config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
		if (!configFile.exists()) {
			saveConfig();
		}
		config.options()
				.header("This is the configuration file of FlyHook." + "\nAs you can see, there are lots to configure."
						+ "\n\nThe 'Power' option defines the speed boost when hooking.");

		// Getting the values from config
		listener.setPower(getConfigEntry(config, "Power", 2.0, true));
		listener.setFireworksDisabled(getConfigEntry(config, "Disable Fireworks", true, true));
		listener.setMaxRange(getConfigEntry(config, "Max Hooking Range", 32, true));

		// Saving the possible changes
		saveConfig();

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("flyhook")) {
			// Making sure the player has permission to reload the config
			if (!sender.hasPermission("flyhook.reload")) {
				sender.sendMessage("§cInsufficient permissions.");
				return false;
			}
			// Getting the new values from config
			loadConfig();
			listener.setPower(getConfigEntry(config, "Power", 2.0, true));
			listener.setFireworksDisabled(getConfigEntry(config, "Disable Fireworks", true, true));
			listener.setMaxRange(getConfigEntry(config, "Max Hooking Range", 32, true));

			sender.sendMessage("§aConfig reloaded!");
		}
		return false;
	}

	// An easy method of getting values from config, although it has an 'unchecked' cast.
	@SuppressWarnings("unchecked")
	private <T> T getConfigEntry(FileConfiguration config, String path, T def, boolean create) {
		if (config.contains(path)) {
			return (T) config.get(path, def);
		}
		if (create) {
			config.set(path, def);
		}
		return def;
	}

	public void saveConfig() {
		try {
			config.save(configFile);
		} catch (Exception e) {
		}
	}

	public void loadConfig() {
		try {
			config.load(configFile);
		} catch (Exception e) {
		}
	}

}
