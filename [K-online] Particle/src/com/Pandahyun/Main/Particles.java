package com.Pandahyun.Main;

import org.bukkit.Bukkit;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Particles extends BukkitRunnable {

	public Player _p;

	public int _x = 0, _y = 0, _z = 0, i = 0;

	Location lc;
	
	public JavaPlugin plugin;

	public Particles(JavaPlugin plugin, Player player) {
		this.plugin = plugin;
		_p = player;
	}

	@Override
	public void run() {
		plugin.getServer().broadcastMessage("hello" + _p.getUniqueId().toString());
		if (this.plugin.getConfig().getBoolean("Players." + _p.getUniqueId().toString() + ".Settings.OnOff")) {
			_p.sendMessage("non");
			if (main.TaskIds.containsKey(_p.getUniqueId().toString()))
				_p.sendMessage("Ok");
			else
				_p.sendMessage("NONO");
			setPosition();
			showParticle();
		} else {
			Bukkit.getServer().getScheduler().cancelTask(main.TaskIds.get(_p.getUniqueId().toString()));
		}

	}
	
	public void  circle()
	{
		lc = _p.getLocation().add(0, 1, 0);
		lc.setX(lc.getX() + Math.cos(i * 0.5));
		lc.setZ(lc.getZ() + Math.sin(i * 0.5));
		i++;
		if (i == 360)
			i = 0;
	}
	
	public void  up_circle()
	{
		lc = _p.getLocation().add(0, 1, 0);
		lc.setX(lc.getX() + Math.cos(i * 0.5));
		lc.setZ(lc.getZ() + Math.sin(i * 0.5));
		lc.setY(lc.getY() + i/360);
		i++;
		if (i == 360)
			i = 0;
	}
	
	public void on_head()
	{
		lc = _p.getLocation().add(0,2,0);
	}
	
	public void angelwing()
	{
		lc = _p.getLocation().add(0,2,0);
		lc.setX(lc.getX() + Math.cos(i * 0.5)*0.5);
		lc.setZ(lc.getZ() + Math.sin(i * 0.5)*0.5);
	}

	public void setPosition() {
		if(this.plugin.getConfig().getString("Players."+_p.getUniqueId().toString()+".Settings.Selected.Shape").equalsIgnoreCase("None")||
				this.plugin.getConfig().getStringList("Players."+_p.getUniqueId().toString()+".Having.Shape")!=null)
			_p.sendMessage("[Error] 아무래도 당신에게는 가지고 있는 파티클 모양이 없는 것 같습니다");
		else if(this.plugin.getConfig().getString("Players."+_p.getUniqueId().toString()+".Settings.Selected.Shape").toString().equalsIgnoreCase("Circle")) circle();
		else if(this.plugin.getConfig().getString("Players."+_p.getUniqueId().toString()+".Settings.Selected.Shape").toString().equalsIgnoreCase("Up_Circle")) up_circle();
		else if(this.plugin.getConfig().getString("Players."+_p.getUniqueId().toString()+".Settings.Selected.Shape").toString().equalsIgnoreCase("On_Head")) on_head();
		else if(this.plugin.getConfig().getString("Players."+_p.getUniqueId().toString()+".Settings.Selected.Shape").toString().equalsIgnoreCase("AngelWing")) angelwing();
	}
	
	
	public void Flame() {
		ParticleEffect.FLAME.display(0, 0, 0, 0.000001F, 1, lc, 192);
	}
	
	public void showParticle() {
		if(this.plugin.getConfig().getString("Players."+_p.getUniqueId().toString()+".Settings.Selected.Particles").equalsIgnoreCase("None")||
				this.plugin.getConfig().getStringList("Players."+_p.getUniqueId().toString()+".Having.Particles")!=null)
			_p.sendMessage("[Error] 아무래도 당신에게는 가지고 있는 파티클이 없는 것 같습니다");
		else if(this.plugin.getConfig().getString("Players."+_p.getUniqueId().toString()+".Settings.Selected.Shape").toString().equalsIgnoreCase("Flame")) Flame();
	}
}
