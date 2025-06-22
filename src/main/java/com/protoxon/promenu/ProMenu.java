package com.protoxon.promenu;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.protocol.player.User;
import com.google.inject.Inject;
import com.protoxon.promenu.controller.PacketListener;
import com.protoxon.promenu.database.Database;
import com.protoxon.promenu.database.MapDatabase;
import com.protoxon.promenu.map.MapRegistry;
import com.protoxon.promenu.map.MapType;
import com.protoxon.promenu.map.TestMaps;
import com.protoxon.promenu.menus.search.MapSearchIndex;
import com.protoxon.promenu.user.UserRegistry;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.PluginDescription;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Optional;

import static com.protoxon.promenu.utils.Color.*;

@Plugin(
        id = "promenu",
        name = "ProMenu",
        version = "1.0.0",
        description = "A packet based graphical user interface built for velocity",
        authors = {"protoxon"},
        url = "protoxon.com",
        dependencies = {
                @Dependency(id = "packetevents")
        }
)
public class ProMenu {

    @Inject
    public static Logger logger;
    public static ProxyServer proxy;
    public static ProMenu plugin;
    public static PacketListener packetListener = new PacketListener();

    @Inject //inject the proxy server and logger into the plugin class
    public ProMenu(ProxyServer proxy, Logger logger) {
        ProMenu.logger = logger;
        ProMenu.proxy = proxy;
        plugin = this;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        System.out.println(startMessage());
        MenuCommand.register();
        TestMaps.addMaps();
        initializeMapData();

        // Register packet listeners
        PacketEvents.getAPI().getEventManager().registerListener(packetListener, PacketListenerPriority.NORMAL);
        PacketEvents.getAPI().init(); // Initialize PacketEvents API
    }

    public void initializeMapData() {
        // Read in maps from the database and add them to the map registry
        MapRegistry.addMaps(MapType.MINIGAMES, Database.maps.getMapsByType(MapType.MINIGAMES));
        MapRegistry.addMaps(MapType.ADVENTURE, Database.maps.getMapsByType(MapType.ADVENTURE));
        // Build the search index's
        try {
            MapSearchIndex.rebuildIndex(MapRegistry.getMaps(MapType.MINIGAMES), MapType.MINIGAMES);
            MapSearchIndex.rebuildIndex(MapRegistry.getMaps(MapType.ADVENTURE), MapType.ADVENTURE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    public void OnProxyShutdown(ProxyShutdownEvent event) {
        System.out.println(shutdownMessage());
    }

    // Start message displayed when the plugin is enabled
    public String startMessage() {
        return GREEN + "[ProMenu] " + YELLOW + "v" + getVersion() + RESET + " by " + MAGENTA + getAuthors() + RESET;
    }

    // Shutdown message displayed when the plugin is disabled
    public String shutdownMessage() {
        return GREEN + "[ProMenu] " + YELLOW + "v" + getVersion() + RESET + " by " + MAGENTA + getAuthors() + RESET + RED + " is now disabled." + RESET;    }

    // Returns the plugin version listed in the plugin annotation or unknown if not specified
    public String getVersion() {
        Optional<PluginContainer> pluginContainer = proxy.getPluginManager().getPlugin("promenu");
        if (pluginContainer.isPresent()) {
            PluginDescription description = pluginContainer.get().getDescription();
            return description.getVersion().orElse("unknown");
        }
        return "unknown";
    }

    // Returns the plugin authors listed in the plugin annotation or unknown if not specified
    public String getAuthors() {
        return proxy.getPluginManager().getPlugin("promenu")
                .map(p -> String.join(", ", p.getDescription().getAuthors()))
                .orElse("unknown");
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        Database.players.loadInFavoriteMaps(user);
        Database.players.loadInMapHistory(user);
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        UserRegistry.getUserData(user).saveFavoritesToDatabase();
    }
}
