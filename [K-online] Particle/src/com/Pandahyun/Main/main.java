package com.Pandahyun.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.Pandahyun.Main.Particles;
import com.Pandahyun.Main.Making;

public class main extends JavaPlugin implements Listener {

	public static HashMap<String, Integer> TaskIds = new HashMap<String, Integer>();
	public List<String> Particle = new ArrayList<String>();
	//public List<String> CustomParticle = new ArrayList<String>();
	public List<String> Shape = new ArrayList<String>();
	public static HashMap<String, Location> centers = new HashMap<String, Location>();
	public static List<String> customParticle = new ArrayList<String>();

	public final int BOOK = 403;
	
	public static File Temp = new File ("C:\\" + "temp\\" + "Pandahyun\\" + "Particle\\" + "temp\\" + "temp.txt");
	private File ConfigFile = new File(getDataFolder(), "plugins\\"+ "Particle\\" + "config.yml");

	public File custom = new File(getDataFolder(), "plugins\\" + "Particle\\" + "Custom\\");
	
	//private File CustomParticleFile = new File(getDataFolder(), "plugins\\"+ "Particle");

	int i = 0, j, nMax;

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		restartParticle();
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[K-online]" + ChatColor.WHITE + " ��ƼŬ �Ŀ� �ý��� �⵿!");
		//if (!CustomParticleFile.exists()) CustomParticleFile.mkdirs();
		/*if(!Temp.exists()){
			Temp.mkdir();
			try {
				Temp.createNewFile();
			} catch (IOException e) {}
		}*/
		
		customParticle.clear();
		
		if (!custom.exists())
			custom.mkdirs();
		
		for (String s : custom.list())
		{
			if(s.contains(".pts"))
			{
				FileReader fw;
				try {
					fw = new FileReader(new File(custom, s));
					BufferedReader fb = new BufferedReader(fw);
					String temp;
					try {
						temp = fb.readLine();
						if(s.substring(0, s.length()-4).equalsIgnoreCase(temp))
						{
							customParticle.add(temp);
						}
					} catch (IOException e1) {
						Bukkit.getLogger().info("fb.readLine error");
					}
					
					try {
						fb.close();
						fw.close();
					} catch (IOException e) {
						getServer().getLogger().info("fb.close error");
					}

				} catch (FileNotFoundException e) {
					getServer().getLogger().info("������ ã���� �������ϴ�");
				}
			}
		}
		
		new FileS(this);
		FileS.Reader();
		
		/*for(Entry<String, Location> e : centers.entrySet())
		{
			Making.makeField(e.getKey(), null, e.getValue(), getServer().getWorld("world"));
		}*/
		
		Shape.add("Circle");
		Shape.add("Up_Circle");
		Shape.add("On_Head");
		Shape.add("AngelWing");
		Shape.add("BackWing");

		Particle.add("Flame");
		Particle.add("Heart");
		Particle.add("Smoke");
		Particle.add("Fireworks_spark");
		Particle.add("Spell");
		Particle.add("Snow_Shovel");
		Particle.add("Villager_Happy");
		Particle.add("Villager_Angry");
		Particle.add("Redstone");
		Particle.add("Crit_Magic");
		Particle.add("RandomRedstone");
		Particle.add("Portal");

		getConfig().set("Settings.Shapes", Shape);
		getConfig().set("Settings.Particles", Particle);

		saveConfig();
		
		Particle = getConfig().getStringList("Settings.Particles");
		Shape = getConfig().getStringList("Settings.Shapes");
	}

	public void onDisable() {
		FileS.Writer();
		for (Entry<String, Location> e : centers.entrySet())
		{
			Bukkit.broadcastMessage(e.getKey());
			Making.removeField(e.getKey(), null, getServer().getWorld("world"), true);
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[K-online]" + ChatColor.WHITE + " ��ƼŬ �Ŀ� �ý��� ��������!");
	}

	public boolean onCommand(CommandSender sender, Command label, String command, String[] args) {
		Player player = (Player) sender;
		if (command.equalsIgnoreCase("kparticle") || command.equalsIgnoreCase("kpt")) {
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("help"))
					CommandHelp(player);

				else if (args[0].equalsIgnoreCase("view")) {
					if (args.length > 1) {
						if(player.isOp())
							if (sGetP(args[1]) != null && sGetP(args[1]).isOnline())
								SettingGUI(player, sGetP(args[1]));
							else
								player.sendMessage("[ERROR] ���� �÷��̾� �Դϴ�");
						else
							player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " ������ �����ϴ�!");
					} else
						SettingGUI(player, player);
				}
				
				else if (args[0].equalsIgnoreCase("edit"))
				{
					if (!player.isOp())
					{
						player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " ������ �����ϴ�!");
						return true;
					}
					
					if (args[1].equalsIgnoreCase("list"))
					{
						Making.showFields(player);
						return true;
					}
					
					if (args.length < 2)
					{
						CommandHelp(player);
						return true;
					}
					
					if(args[1].equalsIgnoreCase("make"))
					{
						if(args.length < 3)
						{
							player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " ���ڰ� �����մϴ�!!");
							return true;
						}
						Making.makeField(args[2], player, player.getLocation(), getServer().getWorld("world"));
					}
					
					else if(args[1].equalsIgnoreCase("remove"))
					{
						if(args.length < 3)
						{
							player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " ���ڰ� �����մϴ�!!");
							return true;
						}
						Making.removeField(args[2], player, getServer().getWorld("world"),true);
					}
					
					else if(args[1].equalsIgnoreCase("save"))
					{
						if(args.length < 4)
						{
							player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " ���ڰ� �����մϴ�!!");
							return true;
						}
						Making.scanParticle(args[2], custom, args[3], player, getServer().getWorld("world"));
					}
					
					else
						CommandHelp(player);
				}

				else if (args[0].equalsIgnoreCase("stop")) {
					if (args.length > 1 && player.isOp())
						if(sGetP(args[1]) != null && sGetP(args[1]).isOnline())
						stopParticle(sGetP(args[1]));
						else ;
					else player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " 권한이 없습니다!");
				}

				else if (args[0].equalsIgnoreCase("give")) {
					if(!player.isOp())
					{
						player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " 권한이 없습니다!");
						return true;
					}
					
					if (args.length > 3) {
						Player givePlayer = sGetP(args[1]);
						if (givePlayer != null && givePlayer.isOnline()) {
							if (args[2].equalsIgnoreCase("Particle") || args[2].equalsIgnoreCase("pt"))
								giveItem("[K - Particle] " + args[3], BOOK, 0, 1,
										Arrays.asList(ChatColor.AQUA + "[K-online] Particle",
												Particle.contains(args[3]) ? args[3] : ChatColor.RED + "없는 파티클입니다.",ChatColor.RESET + "Type : Particle"),
										sGetP(args[1]));

							else if (args[2].equalsIgnoreCase("Shape") || args[2].equalsIgnoreCase("sh"))
								giveItem("[K - Particle] " + args[3], BOOK, 0, 1,
										Arrays.asList(ChatColor.AQUA + "[K-online] Particle",
												Shape.contains(args[3]) ? args[3] : ChatColor.RED + "없는 모양입니다.", ChatColor.RESET + "Type : Shape"),
										sGetP(args[1]));
							
							else if (args[2].equalsIgnoreCase("Custom") || args[2].equalsIgnoreCase("ct"))
								giveItem("[K - Particle] " + args[3], BOOK, 0, 1,
										Arrays.asList(ChatColor.AQUA + "[K-online] Particle",
												customParticle.contains(args[3]) ? args[3] : ChatColor.RED + "없는 커스텀입니다", ChatColor.RESET + "Type : Custom"),
										sGetP(args[1]));

							else
								player.sendMessage("[Error] /kpt give <Player> particle (or shape or custom) <String>");
						} else
							player.sendMessage("온라인이 아니거나 없는 플레이어 입니다");
					} else
						player.sendMessage("[Error] /kpt give <Player> particle (or shape or custom) <String>");
					saveConfig();
				}
				
				else if (args[0].equalsIgnoreCase("restart"))
				{
					if(!player.isOp())
					{
						player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " 권한이 없습니다!");
						return true;
					}

					restartParticle();
				}
				
				else if(args[0].equalsIgnoreCase("getBook"))
				{
					if(!player.isOp())
					{
						player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " 권한이 없습니다!");
						return true;
					}
					
					if(args.length < 2)
					{
						CommandHelp(player);
						return true;
					}
					
					if (args[1].equalsIgnoreCase("Particle") || args[1].equalsIgnoreCase("pt"))
						giveItem("[K - Particle] " + args[2], BOOK, 0, 1,
								Arrays.asList(ChatColor.AQUA + "[K-online] Particle",
										Particle.contains(args[2]) ? args[2] : ChatColor.RED + "���� ��ƼŬ�Դϴ�.",ChatColor.RESET + "Type : Particle"),
								player);

					else if (args[1].equalsIgnoreCase("Shape") || args[1].equalsIgnoreCase("sh"))
						giveItem("[K - Particle] " + args[2], BOOK, 0, 1,
								Arrays.asList(ChatColor.AQUA + "[K-online] Particle",
										Shape.contains(args[2]) ? args[2] : ChatColor.RED + "���� ����Դϴ�.", ChatColor.RESET + "Type : Shape"),
								player);
					
					else if (args[1].equalsIgnoreCase("Custom") || args[1].equalsIgnoreCase("ct"))
						giveItem("[K - Particle] " + args[2], BOOK, 0, 1,
								Arrays.asList(ChatColor.AQUA + "[K-online] Particle",
										customParticle.contains(args[2]) ? args[2] : ChatColor.RED + "���� Ŀ���� ����Դϴ�.", ChatColor.RESET + "Type : Custom"),
								player);
					
					else
						CommandHelp(player);
						
				}
				
				else if(args[0].equalsIgnoreCase("list"))
				{
					if(!player.isOp())
					{
						player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " 권한이 없습니다!");
						return true;
					}
					
					if(args.length < 2)
					{
						CommandHelp(player);
						return true;
					}
					
					if(args[1].equalsIgnoreCase("Particle") || args[1].equalsIgnoreCase("pt"))
					{
						player.sendMessage("---파티클 목록---");
						for (String s : Particle)
							player.sendMessage(s);
					}
					
					else if(args[1].equalsIgnoreCase("Custom") || args[1].equalsIgnoreCase("ct"))
					{
						player.sendMessage("---커스텀 목록---");
						for (String s :customParticle)
							player.sendMessage(s);
					}
					
					else if(args[1].equalsIgnoreCase("Shape") || args[1].equalsIgnoreCase("sh"))
					{
						player.sendMessage("---모양 목록---");
						for (String s : Shape)
							player.sendMessage(s);
					}
					
					else
						CommandHelp(player);
				}
				
				else if(args[0].equalsIgnoreCase("remove"))
				{
					if(!player.isOp())
					{
						player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + " 권한이 없습니다!");
						return true;
					}
					
					if(args.length < 3)
					{
						CommandHelp(player);
						return true;
					}
					
					Player tarP = sGetP(args[1]);
					
					if(tarP != null) 
					{
						
						if (args[2].equalsIgnoreCase("Particle") || args[2].equalsIgnoreCase("pt"))
						{
							List<String> Having = getConfig().getStringList("Players." + sGetU(tarP) + ".Having.Particles");
							
							if(Having.contains(args[3])) Having.remove(args[3]);
							else
							{
								player.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RED + "�ش� �÷��̾�� �ش� ��ƼŬ�� ������ ���� �ʽ��ϴ�.");
								return true;
							}
							
							if(Having.isEmpty()) getConfig().set("Players."+ sGetU(tarP) +".Settings.Selected.Particle", "None");
							
							getConfig().set("Players." + sGetU(tarP) + ".Having.Particles", Having);
							saveConfig();
							
							player.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + tarP.getName() + " �� ��ƼŬ�� ���������� ���� �Ͽ����ϴ�.");
						}
							

						else if (args[2].equalsIgnoreCase("Shape") || args[2].equalsIgnoreCase("sh"))
						{
							List<String> Having = getConfig().getStringList("Players." + sGetU(tarP) + ".Having.Shapes");
							
							if(Having.contains(args[3])) Having.remove(args[3]);
							else
							{
								player.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RED + "�ش� �÷��̾�� �ش� ����� ������ ���� �ʽ��ϴ�.");
								return true;
							}
							
							if(Having.isEmpty()) getConfig().set("Players."+ sGetU(tarP) +".Settings.Selected.Shape", "None");
							
							getConfig().set("Players." + sGetU(tarP) + ".Having.Shapes", Having);
							saveConfig();
							
							player.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + tarP.getName() + "�� ����� ���������� ���� �Ͽ����ϴ�.");
						}
						
						else if (args[2].equalsIgnoreCase("Custom") || args[2].equalsIgnoreCase("ct"))
						{
							List<String> Having = getConfig().getStringList("Players." + sGetU(tarP) + ".Having.Customs");
							
							if(Having.contains(args[3])) Having.remove(args[3]);
							else
							{
								player.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RED + "�ش� �÷��̾�� �ش� Ŀ���� ����� ������ ���� �ʽ��ϴ�.");
								return true;
							}
							
							if(Having.isEmpty()) getConfig().set("Players."+ sGetU(tarP) +".Settings.Selected.Shape", "None");
							
							getConfig().set("Players." + sGetU(tarP) + ".Having.Shapes", Having);
							saveConfig();
							
							player.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + tarP.getName() + "�� Ŀ���� ����� ���������� ���� �Ͽ����ϴ�.");
						}
						
						else
							CommandHelp(player);
					}
					
					else
						player.sendMessage(ChatColor.AQUA + "[KPT]" + ChatColor.RED + "�ش� �÷��̾�� ���� �÷��̾� �Դϴ�.");
					
					
				}
				else CommandHelp(player);
			}
			else
				CommandHelp(player);
		}
		else return true;
		return false;
	}

	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();

		if (!(getConfig().contains("Players." + e.getPlayer().getUniqueId().toString()))) {
			List<String> pList = new ArrayList<String>();
			getConfig().set("Players." + e.getPlayer().getUniqueId().toString() + ".Having.Particles", pList);
			getConfig().set("Players." + e.getPlayer().getUniqueId().toString() + ".Having.Shape", pList);
			getConfig().set("Players." + e.getPlayer().getUniqueId().toString() + ".Having.Customs", pList);
			getConfig().set("Players." + e.getPlayer().getUniqueId().toString() + ".Settings.Selected.Shape", "None");
			getConfig().set("Players." + e.getPlayer().getUniqueId().toString() + ".Settings.Selected.Particle",
					"None");
			getConfig().set("Players." + sGetU(p) + ".Settings.OnOff", false);
			saveConfig();
		}

		if(getConfig().getBoolean("Players." + sGetU(p) + ".Settings.OnOff")) runParticle(p);
	}

	@EventHandler
	public void onQuitEvent(PlayerQuitEvent e) {
		if (TaskIds.containsKey(sGetU(e.getPlayer()))) {
			stopParticle(e.getPlayer());
		}
	}

	@EventHandler
	public void onSelectedInventory(InventoryClickEvent event) {
		if (ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("K-Particle - Particles")) {
			event.setCancelled(true);

			if(event.getSlot()==44)
			{
				SettingGUI((Player) event.getWhoClicked(), getServer().getPlayer(event.getInventory().getContents()[44].getItemMeta().getLore().get(1)));
				return;
			}
			
			if (!event.getSlotType().equals(SlotType.OUTSIDE) && !event.getCurrentItem().getType().equals(Material.AIR)
					&& event.getCurrentItem().hasItemMeta()
					&& event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.AQUA + "[KPT]")) {
				Player p = (Player) event.getWhoClicked();
				List<String> Having = getConfig().getStringList("Players." + sGetU(p) + ".Having.Particles");
				int slot = event.getSlot();

				event.setCancelled(true);
				
				setParticle(p, Having.get(slot));
				ParticleGUI(p, getServer().getPlayer(event.getInventory().getContents()[44].getItemMeta().getLore().get(1)));
			}
		}
		
		else if (ChatColor.stripColor(event.getInventory().getName()).equals("K-Particle - Shapes")) {
			event.setCancelled(true);
			
			if(event.getSlot()==44)
			{
				SettingGUI((Player) event.getWhoClicked(), getServer().getPlayer(event.getInventory().getContents()[44].getItemMeta().getLore().get(1)));
				return;
			}
			
			if (!event.getSlotType().equals(SlotType.OUTSIDE) && !event.getCurrentItem().getType().equals(Material.AIR)
					&& event.getCurrentItem().hasItemMeta()
					&& event.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.AQUA + "[KPT]")) {
				Player p = (Player) event.getWhoClicked();
				String s = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
				
				//if(Shape.contains(s.substring(6, s.length())))
				setShape(p, s.substring(6, s.length()));
				
				/*else if(customParticle.contains(s.substring(6, s.length())))
					setShape(p, s.substring(6, s.length()));*/
				ShapeGUI((Player) event.getWhoClicked(),
						getServer().getPlayer(event.getInventory().getContents()[44].getItemMeta().getLore().get(1)));
			}
		}

		else if (ChatColor.stripColor(event.getInventory().getName()).contains("K-Particle - Setting")) {

			if (!event.getSlotType().equals(SlotType.OUTSIDE) && !event.getCurrentItem().getType().equals(Material.AIR)
					&& event.getCurrentItem().hasItemMeta()) {
				Player p = (Player) event.getWhoClicked();
				Player tarP = getServer().getPlayer(event.getInventory().getContents()[0].getItemMeta().getLore().get(1));
				int slot = event.getSlot();

				event.setCancelled(true);

				switch (slot) {
				case 3:
					//p.closeInventory();
					ParticleGUI((Player) event.getWhoClicked(), getServer()
							.getPlayer(event.getInventory().getContents()[0].getItemMeta().getLore().get(1)));
					break;
				case 4:
					//p.closeInventory();
					ShapeGUI((Player) event.getWhoClicked(), getServer()
							.getPlayer(event.getInventory().getContents()[0].getItemMeta().getLore().get(1)));
					break;
				case 5:
					if(getConfig().getBoolean("Players." + p.getUniqueId().toString() + ".Settings.OnOff")) {
						getConfig().set("Players." + tarP.getUniqueId().toString() + ".Settings.OnOff", false);
						stopParticle(tarP);
					}
					else {
						getConfig().set("Players." + tarP.getUniqueId().toString() + ".Settings.OnOff", true);
						runParticle(p);
					}
					
					p.sendMessage("[KPT] " +tarP.getName() + " �� ���� �ٲ� : " + (getConfig().getBoolean("Players." + tarP.getUniqueId().toString() + ".Settings.OnOff") ? "TRUE" : "FALSE"));
					p.closeInventory();
					saveConfig();
				case 8:
					p.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + "GUI�� �������ϴ�.");
					p.closeInventory();
					break;
				}

			}
		}
	}

	@EventHandler
	public void onReloadPlayer(PlayerCommandPreprocessEvent e)
	{
		String s = e.getMessage();
		
		if(s.equalsIgnoreCase("reload"))
		{
			FileS.Writer();
			reloadParticle();
		}
	}
	
	@EventHandler
	public void onReloadServer(ServerCommandEvent e)
	{
		if(e.getCommand().equalsIgnoreCase("reload"))
		{
			FileS.Writer();
			reloadParticle();
		}
	}
	
	//

	public void ParticleGUI(Player showP, Player tarP) {
		Inventory inv = Bukkit.createInventory(null, 45,
				ChatColor.BLUE + "" + ChatColor.BOLD + "K-Particle - Particles");
		List<String> Having = getConfig() .getStringList("Players." + tarP.getUniqueId().toString() + ".Having.Particles");
		
		if (!(Having.isEmpty()))
			for (int i = 0; i < Having.size(); ++i)
				setItem(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + Having.get(i), BOOK, 0, 1,
						Arrays.asList(Having.get(i)
								.equalsIgnoreCase(getConfig().getString(
								"Players." + tarP.getUniqueId().toString() + ".Settings.Selected.Particle"))
								? "�����" : "Ŭ���� ���"), i, inv);
		else {
			showP.sendMessage(ChatColor.RED + "[KPT] " + ChatColor.AQUA + tarP.getName() + ChatColor.RED + " �÷��̾�� ������ �ִ� ��ƼŬ�� �����ϴ�.");
			inv = null; Having = null;
			return;
		}
		setItem(ChatColor.AQUA + "���� Ÿ��", BOOK, 0, 1, Arrays.asList(ChatColor.RESET + "�� GUI�� ������ Ÿ��", tarP.getName()),43, inv);
		setItem(ChatColor.AQUA + "Setting GUI", 324, 0, 1, Arrays.asList(ChatColor.RESET + "�������� ���ư��ϴ�", tarP.getName()),44, inv);
		showP.openInventory(inv);
	}

	public void ShapeGUI(Player showP, Player tarP) {
		Inventory inv = Bukkit.createInventory(null, 45,
				ChatColor.BLUE + "" + ChatColor.BOLD + "K-Particle - Shapes");
		List<String> Having = getConfig().getStringList("Players." + tarP.getUniqueId().toString() + ".Having.Shapes");
		if (!(getConfig().getStringList("Players." + tarP.getUniqueId().toString() + ".Having.Shapes").isEmpty())
				|| !getConfig().getStringList("Players." + tarP.getUniqueId().toString() + ".Having.Customs").isEmpty())
		{
			int i = 0;
			for (; i < Having.size(); ++i)
				setItem(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + Having.get(i), BOOK, 0, 1,
						Arrays.asList(Having.get(i)
								.equalsIgnoreCase(getConfig().getString(
										"Players." + tarP.getUniqueId().toString() + ".Settings.Selected.Shape"))
												? "�����" : "Ŭ���� ���"),i, inv);
			
			Having = getConfig().getStringList("Players." + tarP.getUniqueId().toString() + ".Having.Customs");
			for (String s : Having)
			{
				setItem(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + s, BOOK, 0, 1,
						Arrays.asList(s
								.equalsIgnoreCase(getConfig().getString(
										"Players." + tarP.getUniqueId().toString() + ".Settings.Selected.Shape"))
												? "�����" : "Ŭ���� ���"),i, inv);
				++i;
			}
		}
		else {
			showP.sendMessage(ChatColor.RED + "[KPT] " + ChatColor.AQUA + tarP.getName() + ChatColor.RED + " �÷��̾�� ������ �ִ� ����� �����ϴ�.");
			inv = null; Having = null;
			return;
		}
		setItem(ChatColor.AQUA + "���� Ÿ��", BOOK, 0, 1, Arrays.asList(ChatColor.RESET + "�� GUI�� ������ Ÿ��", tarP.getName()), 43, inv);
		setItem(ChatColor.AQUA + "Setting GUI", 324, 0, 1, Arrays.asList(ChatColor.RESET + "�������� ���ư��ϴ�", tarP.getName()),44, inv);
		showP.openInventory(inv);
	}

	public void SettingGUI(Player showP, Player tarP) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.BLUE + "K-Particle - Setting");

		setItem(ChatColor.AQUA + "���� Ÿ��", BOOK, 0, 1, Arrays.asList(ChatColor.RESET + "�� GUI�� ������ Ÿ��", tarP.getName()), 0, inv);
		setItem(ChatColor.AQUA + "��ƼŬ Ȯ��", BOOK, 0, 1, Arrays.asList("����� ������ �ִ� Particle�� Ȯ�� �մϴ�"), 3, inv);
		setItem(ChatColor.AQUA + "��� Ȯ��", BOOK, 0, 1, Arrays.asList("����� ������ �ִ� Shape�� Ȯ�� �մϴ�"), 4, inv);
		setItem(ChatColor.AQUA + "��ƼŬ OnOff", BOOK, 0, 1, Arrays.asList("Particle is" +
				(getConfig().getBoolean("Players." + tarP.getUniqueId().toString() + ".Settings.OnOff") ? "On" : "Off")), 5, inv);
		setItem(ChatColor.AQUA + "GUI �ݱ�", 324, 0, 1, Arrays.asList("�� GUI�� �ݽ��ϴ�."), 8, inv);

		showP.openInventory(inv);
	}

	@EventHandler
	public void getParticle(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Action a = e.getAction();
		if(!p.getItemInHand().getType().equals(Material.AIR)
		 && p.getItemInHand().hasItemMeta())
		{
			String DisplayName = p.getItemInHand().getItemMeta().getDisplayName();
			List<String> lore = p.getItemInHand().getItemMeta().getLore();
			
			if(a==Action.RIGHT_CLICK_AIR || a==Action.RIGHT_CLICK_BLOCK)
			{
				
				if(p.getItemInHand().getType().getId() == BOOK &&
				p.getItemInHand().getItemMeta().getLore().size() > 1)
				{
					if(Particle.contains(lore.get(1)))
					{
						List<String> Having = getConfig().getStringList("Players." + p.getUniqueId().toString() + ".Having.Particles");
					
						if(Having.contains(lore.get(1)))
							p.sendMessage(ChatColor.AQUA + "[KPT] " + "�̹� ������ ��ʴϴ�");
					
						else
						{
							Having.add(lore.get(1));
							getConfig().set("Players." + p.getUniqueId().toString() + ".Having.Particles", Having);
							saveConfig();
							p.getInventory().remove(p.getItemInHand());
							p.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + DisplayName + " ��ƼŬ�� �����̽��ϴ�\n"
									+ "Ȯ�� ��ɾ� /kpt view �� Ȯ�� �ϽǼ� �ֽ��ϴ�.");
						}
					}
					
					else if(Shape.contains(lore.get(1)))
					{
						List<String> Having = getConfig().getStringList("Players." + p.getUniqueId().toString() + ".Having.Shapes");
					
						if(Having.contains(lore.get(1)))
							p.sendMessage(ChatColor.AQUA + "[KPT] " + "�̹� ������ ��ʴϴ�");
					
						else
						{
							Having.add(lore.get(1));
							getConfig().set("Players." + p.getUniqueId().toString() + ".Having.Shapes", Having);
							saveConfig();
							p.getInventory().remove(p.getItemInHand());
							p.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + DisplayName + " ����� �����̽��ϴ�\n"
									+ "Ȯ�� ��ɾ� /kpt view �� Ȯ�� �ϽǼ� �ֽ��ϴ�.");
						}
					}
					
					else if (customParticle.contains(lore.get(1)))
					{
						List<String> Having = getConfig().getStringList("Players." + p.getUniqueId().toString() + ".Having.Customs");
						
						if(Having.contains(lore.get(1)))
							p.sendMessage(ChatColor.AQUA + "[KPT] " + "�̹� ������ ��ʴϴ�");
						
						else
						{
							Having.add(lore.get(1));
							getConfig().set("Players." + p.getUniqueId().toString() + ".Having.Customs", Having);
							saveConfig();
							p.getInventory().remove(p.getItemInHand());
							p.sendMessage(ChatColor.AQUA + "[KPT] " + ChatColor.RESET + DisplayName + " ����� �����̽��ϴ�\n"
									+ "Ȯ�� ��ɾ� /kpt view �� Ȯ�� �ϽǼ� �ֽ��ϴ�.");
						}
					}
				}
			}
		}
	}

	private void giveItem(String Display, int ID, int DATA, int STACK, List<String> lore, Player p) {
		ItemStack item = new MaterialData(ID, (byte) DATA).toItemStack(STACK);
		ItemMeta itemMeta = item.getItemMeta();

		itemMeta.setDisplayName(Display);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);

		p.getInventory().addItem(item);
	}

	private void setItem(String Display, int ID, int DATA, int STACK, List<String> lore, int loc, Inventory inv) {
		ItemStack item = new MaterialData(ID, (byte) DATA).toItemStack(STACK);
		ItemMeta itemMeta = item.getItemMeta();

		itemMeta.setDisplayName(Display);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		inv.setItem(loc, item);
	}

	public void setParticle(Player player, String Particle) {
		getConfig().set("Players." + player.getUniqueId().toString() + ".Settings.Selected.Particle", Particle);
		saveConfig();
	}

	public void setShape(Player player, String Shape) {
		getConfig().set("Players." + player.getUniqueId().toString() + ".Settings.Selected.Shape", Shape);
		saveConfig();
	}

	public void stopParticle(Player p) {
		getServer().getScheduler().cancelTask(TaskIds.get(sGetU(p)));
		getServer().getLogger().info(getName() + "�� �½�ũ�� �����߽��ϴ�." + TaskIds.get(sGetU(p)));
		TaskIds.remove(sGetU(p));
	}

	public void runParticle(Player p)
	{
		TaskIds.put(sGetU(p),
				(Integer) getServer().getScheduler().scheduleSyncRepeatingTask(this, new Particles(this, p, custom), 0, 5));
	}
	
	public void reloadParticle()
	{
		for(Player p : getServer().getOnlinePlayers()) if(TaskIds.containsKey(sGetU(p))) stopParticle(p);
	}
	
	public void restartParticle()
	{
		for(Player p : getServer().getOnlinePlayers()) if(getConfig().getBoolean("Players." + p.getUniqueId().toString() + ".Settings.OnOff")) runParticle(p);
	}

	public String sGetU(Player p) // ����ũ ���̵� �޾ƿ���
	{
		return p.getUniqueId().toString();
	}

	public void CommandHelp(Player sender) {
		if(sender.isOp())
			sender.sendMessage("\n" + ChatColor.AQUA + "K-Particle ���̿¶��� �Ŀ����� �÷����� By Pandahyun\n" + ChatColor.RESET
					+ "��ɾ� ���� - /kpt help" + "\n�ڽ��� �Ŀ�GUI ���� - /kpt view" + "\n�Ŀ� ��ƼŬ ���߱�[OP] - /kpt stop <Player>"
					+ "\n�ٸ����� �Ŀ�GUI ����[OP] - /kpt view <�÷��̾��>"
					+ "\n�Ŀ� ȿ�� �ֱ�[OP] - /kpt give <�÷��̾��> <Particle or pt or Shape or sh or Custom or ct> <��� ȿ��>"
					+ "\n�Ŀ� ȿ�� ����[OP] - /kpt remove <�÷��̾��> <Particle or pt or Shape or sh or Custom or ct> <���� ȿ��>"
					+ "\n�Ŀ� å �����[OP] - /kpt getBook <Particle or pt or Shape or sh or Custom or ct> <ȿ��>"
					+ "\n�Ŀ� ȿ�� ��� ����[OP] - /kpt list <Particle or pt or Shape or sh or Custom or ct> <ȿ��>"
					+ "\n��ƼŬ �ٽ� �����ϱ�[OP] - /kpt restart ���� ���ε�� �۵������� �۵��� �ȉ��� ��� ���");	
		else
			sender.sendMessage("\n" + ChatColor.AQUA + "K-Particle ���̿¶��� �Ŀ����� �÷����� By Pandahyun\n" + ChatColor.RESET
					+ "��ɾ� ���� - /kpt help" + "\n�ڽ��� �Ŀ�GUI ���� - /kpt view");
	}

	
	
	private Player sGetP(String s) // �÷��̾� �޾ƿ���
	{
		return getServer().getPlayer(s);
	}
}