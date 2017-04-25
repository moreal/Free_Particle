package com.Pandahyun.Main;

import java.util.HashMap;

import org.bukkit.Bukkit;

//import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.Pandahyun.*;

public class Particles extends BukkitRunnable {

	public Player _p;

	public int _x = 0, _y = 0, _z = 0, TaskId;
	
	//public Hash

	public JavaPlugin plugin;

	public Particles(JavaPlugin plugin, Player player, HashMap<String, Integer> hash) {
		this.plugin = plugin;
		_p = player;
	}
	
	@Override
	public void run() {
		plugin.getServer().broadcastMessage("hello"+_p.getUniqueId().toString());
		if (this.plugin.getConfig().getBoolean("Players." + _p.getUniqueId().toString() + ".Settings.OnOff")) {
			_p.sendMessage("non");
			if(main.TaskIds.containsKey(_p.getUniqueId().toString())) _p.sendMessage("Ok");
			else _p.sendMessage("NONO");
			Flame(_p);
			//setPosition();
		} else
		{
			Bukkit.getServer().getScheduler().cancelTask(main.TaskIds.get(_p.getUniqueId().toString()));
		}
			
	}

	public void Flame(Player player) {
		int i = 0, j;
		Location lc = player.getLocation().add(0, 1, 0);
		for (j = 0; j < 2; j++) {
			lc = player.getLocation().add(0, 1, 0);
			lc.setX(lc.getX() + Math.sin(i));
			lc.setZ(lc.getZ() + Math.cos(i));
			ParticleEffect.FLAME.display(0, 0, 0, 0.000001F, 1, lc, 192);
			i++;
		}
		if (i == 360)
			i = 0;
	}

	public void s(Player p) {
		// Bukkit.getScheduler().
		Flame(p);
	}

	public void setPosition() {

	}
}