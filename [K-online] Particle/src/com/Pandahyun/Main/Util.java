package com.Pandahyun.Main;

import org.bukkit.entity.Player;

import com.Pandahyun.Main.main;

public class Util {

	main m = new main();

	String[] var = new String[2];

	String[] temp;

	public void Setting(Player player) {
			m.getConfig().createSection("Players."+player.getUniqueId());
			m.getConfig().set("Players"+player.getUniqueId(), "BB");
	}

}
