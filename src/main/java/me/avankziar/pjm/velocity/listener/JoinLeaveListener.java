package main.java.me.avankziar.pjm.velocity.listener;

import java.io.IOException;
import java.util.List;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.pjm.velocity.PJM;
import main.java.me.avankziar.pjm.velocity.assistant.ChatApi;

public class JoinLeaveListener
{
	private PJM plugin;
	
	public JoinLeaveListener(PJM plugin)
	{
		this.plugin = plugin;
	}
	
	@Subscribe
	public void onPlayerJoin(PlayerChooseInitialServerEvent event)
	{
		Player player = event.getPlayer();
		if(plugin.getYamlHandler().getHasPlayedBefore().get(player.getUniqueId().toString()) == null)
		{
			//Play for the first time on the server.
			save(player);
			List<String> msg = plugin.getYamlHandler().getConfig().getStringList("Message.Join.NewPlayer");
			for(Player all : plugin.getServer().getAllPlayers())
			{
				for(String s : msg)
				{
					all.sendMessage(ChatApi.tl(s.replace("%player%", player.getUsername())));
				}
				
			}
		} else
		{
			//Have Played before
			if(!plugin.getYamlHandler().getHasPlayedBefore().getString(player.getUniqueId().toString()).equals(player.getUsername()))
			{
				save(player);
			}
			List<String> msg = plugin.getYamlHandler().getConfig().getStringList("Message.Join.NormalPlayer");
			for(Player all : plugin.getServer().getAllPlayers())
			{
				for(String s : msg)
				{
					all.sendMessage(ChatApi.tl(s.replace("%player%", player.getUsername())));
				}
				
			}
		}
	}
	
	private void save(Player player)
	{
		plugin.getYamlHandler().getHasPlayedBefore().set(player.getUniqueId().toString(), player.getUsername());
		try
		{
			plugin.getYamlHandler().getHasPlayedBefore().save();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Subscribe
	public void onPlayerQuit(DisconnectEvent event)
	{
		final String username = event.getPlayer().getUsername();
		List<String> msg = plugin.getYamlHandler().getConfig().getStringList("Message.Leave.NormalPlayer");
		for(Player all : plugin.getServer().getAllPlayers())
		{
			for(String s : msg)
			{
				all.sendMessage(ChatApi.tl(s.replace("%player%", username)));
			}
			
		}
	}
}