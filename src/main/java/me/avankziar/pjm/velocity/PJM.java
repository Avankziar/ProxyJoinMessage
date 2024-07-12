package main.java.me.avankziar.pjm.velocity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import main.java.me.avankziar.pjm.velocity.database.YamlHandler;
import main.java.me.avankziar.pjm.velocity.database.YamlManager;
import main.java.me.avankziar.pjm.velocity.listener.JoinLeaveListener;

@Plugin(id = "avankziar-proxyjoinmessage", name = "ProxyJoinMessage", version = "1-0-0",
		url = "https://example.org", description = "Greet and say goodbye to players", authors = {"Avankziar"})
public class PJM
{
	private static PJM plugin;
    private final ProxyServer server;
    public Logger logger = null;
    private Path dataDirectory;
    private YamlHandler yamlHandler;
    private YamlManager yamlManager;
    
    @Inject
    public PJM(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) 
    {
    	PJM.plugin = this;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) 
    {
    	logger = Logger.getLogger("PJM");
    	PluginDescription pd = server.getPluginManager().getPlugin("avankziar-proxyjoinmessage").get().getDescription();
        List<String> dependencies = new ArrayList<>();
        pd.getDependencies().stream().allMatch(x -> dependencies.add(x.toString()));
        //https://patorjk.com/software/taag/#p=display&f=ANSI%20Shadow&t=PJM
		logger.info(" ██████╗     ██╗███╗   ███╗ | Id: "+pd.getId());
		logger.info(" ██╔══██╗    ██║████╗ ████║ | Version: "+pd.getVersion().get());
		logger.info(" ██████╔╝    ██║██╔████╔██║ | Author: ["+String.join(", ", pd.getAuthors())+"]");
		logger.info(" ██╔═══╝██   ██║██║╚██╔╝██║ | Dependencies Plugins: ["+String.join(", ", dependencies)+"]");
		logger.info(" ██║    ╚█████╔╝██║ ╚═╝ ██║ | Plugin Website:"+pd.getUrl().toString());
		logger.info(" ╚═╝     ╚════╝ ╚═╝     ╚═╝ | Description: "+(pd.getDescription().isPresent() ? pd.getDescription().get() : "/"));
        
        yamlHandler = new YamlHandler(plugin);
        server.getEventManager().register(this, new JoinLeaveListener(plugin));
    }
    
    public static PJM getPlugin()
    {
    	return PJM.plugin;
    }
    
    public ProxyServer getServer()
    {
    	return server;
    }
    
    public Path getDataDirectory()
    {
    	return dataDirectory;
    }
    
    public YamlHandler getYamlHandler()
    {
    	return yamlHandler;
    }
    
    public YamlManager getYamlManager()
    {
    	return yamlManager;
    }
    
    public void setYamlManager(YamlManager yamlManager)
    {
    	this.yamlManager = yamlManager;
    }
}