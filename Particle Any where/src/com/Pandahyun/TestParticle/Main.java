package com.Pandahyun.TestParticle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
		getServer().broadcastMessage("On!!");
	}
	
	public void onDisable()
	{
		getServer().broadcastMessage("Off!!");
	}
	
	public boolean onCommand(CommandSender sender, Command label, String command, String[] args)
	{
		if(command.equalsIgnoreCase("test"))
		{
			if
		}
		return false;
	}
}
