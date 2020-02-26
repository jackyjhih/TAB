package me.neznamy.tab.platforms.bukkit.features;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.neznamy.tab.platforms.bukkit.Main;
import me.neznamy.tab.platforms.bukkit.TabPlayer;
import me.neznamy.tab.platforms.bukkit.packets.method.MethodAPI;
import me.neznamy.tab.shared.ITabPlayer;
import me.neznamy.tab.shared.ProtocolVersion;
import me.neznamy.tab.shared.Shared;
import me.neznamy.tab.shared.features.BossBar.BossBarLine;
import me.neznamy.tab.shared.features.SimpleFeature;

public class BossBar_legacy implements Listener, SimpleFeature {
	
	@Override
	public void load() {
		if (ProtocolVersion.SERVER_VERSION.getMinorVersion() < 9) {
			Bukkit.getPluginManager().registerEvents(this, Main.instance);
			Shared.cpu.startRepeatingMeasuredTask(200, "refreshing bossbar", "BossBar 1.8", new Runnable() {
				public void run() {
					for (ITabPlayer all : Shared.getPlayers()) {
						for (BossBarLine l : all.activeBossBars) {
							Location to = (((TabPlayer)all).player).getEyeLocation().add((((TabPlayer)all).player).getEyeLocation().getDirection().normalize().multiply(25));
							all.sendPacket(MethodAPI.getInstance().newPacketPlayOutEntityTeleport(l.getEntity(), to));
						}
					}
				}
			});
		}
	}
	@EventHandler
	public void a(PlayerChangedWorldEvent e) {
		long time = System.nanoTime();
		ITabPlayer p = Shared.getPlayer(e.getPlayer().getUniqueId());
		if (p == null) return; //teleport in early login event or what
		Shared.getPlayer(e.getPlayer().getUniqueId()).detectBossBarsAndSend();
		Shared.cpu.addFeatureTime("BossBar 1.8", System.nanoTime()-time);
	}
	@EventHandler
	public void a(PlayerRespawnEvent e) {
		long time = System.nanoTime();
		Shared.getPlayer(e.getPlayer().getUniqueId()).detectBossBarsAndSend();
		Shared.cpu.addFeatureTime("BossBar 1.8", System.nanoTime()-time);
	}
	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
	}
	@Override
	public void onJoin(ITabPlayer p) {
	}
	@Override
	public void onQuit(ITabPlayer p) {
	}
	@Override
	public void onWorldChange(ITabPlayer p, String from, String to) {
	}
}