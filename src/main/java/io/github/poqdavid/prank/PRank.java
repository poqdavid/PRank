/*
 *     This file is part of PRank.
 *
 *     PRank is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     PRank is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with PRank.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     Copyright (c) POQDavid <https://about.me/poqdavid>
 *     Copyright (c) contributors
 */

package io.github.poqdavid.prank;

import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.Pixelmon;
import io.github.poqdavid.prank.Commands.CommandManager;
import io.github.poqdavid.prank.Listeners.PRankListener;
import io.github.poqdavid.prank.Listeners.PixelmonEventListener;
import io.github.poqdavid.prank.Permission.PRPermissions;
import io.github.poqdavid.prank.Utils.CText;
import io.github.poqdavid.prank.Utils.Data.Rank;
import io.github.poqdavid.prank.Utils.PRLogger;
import io.github.poqdavid.prank.Utils.Settings;
import io.github.poqdavid.prank.Utils.Tools;
import me.rojo8399.placeholderapi.Placeholder;
import me.rojo8399.placeholderapi.PlaceholderService;
import me.rojo8399.placeholderapi.Source;
import net.luckperms.api.LuckPerms;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.github.poqdavid.prank.Utils.Tools.GetRank;
@Plugin(id = PluginData.id, name = "@name@", version = "@version@", description = "@description@", url = PluginData.url, authors = {"@authors@"}, dependencies = {@Dependency(id = "placeholderapi", optional = false), @Dependency(id = "pixelmon", version = "8.4.3", optional = false)})
@ConfigSerializable
public class PRank {

    public static Map<UUID, ServerBossBar> xpbar = new HashMap<>();
    public static Map<UUID, Text> actionbartext = new HashMap<>();

    public static ServerBossBar defaultbar = ServerBossBar.builder().color(BossBarColors.GREEN).name(Text.of("--")).overlay(BossBarOverlays.PROGRESS).percent(0.0f).visible(false).build();
    private static PRank PRank;
    final private PRLogger logger;
    private final Settings settings;
    private final Path configdirpath;
    private final Path configfullpath;
    private final Path dataDir;
    private final PluginContainer pluginContainer;
    public LuckPerms LPAPI;
    public PermissionService permservice;
    public PermissionDescription.Builder permdescbuilder;
    public Path recordsDir;
    public Map<String, io.github.poqdavid.prank.Utils.Data.PlayerData> PlayerData;
    public PlaceholderService phService;
    @Inject
    private Game game;
    private CommandManager cmdManager;

    @Inject
    public PRank(@ConfigDir(sharedRoot = true) Path path, Logger logger, PluginContainer container) {
        this.dataDir = Sponge.getGame().getSavesDirectory().resolve(PluginData.id);
        this.pluginContainer = container;
        this.logger = new PRLogger(CText.get(CText.Colors.BLUE, 1, "P") + CText.get(CText.Colors.MAGENTA, 0, "R"));
        this.configdirpath = path.resolve(PluginData.id);
        this.configfullpath = Paths.get(this.getConfigPath().toString(), "config.json");
        this.settings = new Settings();
        this.recordsDir = Paths.get(this.getConfigPath().toString(), "records");
        this.PlayerData = new HashMap<>();
    }

    @Nonnull
    public static PRank getInstance() {
        return PRank;
    }

    @Nonnull
    public Path getConfigPath() {
        return this.configdirpath;
    }

    @Nonnull
    public PluginContainer getPluginContainer() {
        return this.pluginContainer;
    }

    @Nonnull
    public String getVersion() {
        return PluginData.version;
    }

    @Nonnull
    public PRLogger getLogger() {
        return logger;
    }

    @Nonnull
    public LuckPerms getLPapi() {
        return LPAPI;
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Inject
    public void setGame(Game game) {
        this.game = game;
    }

    @Nonnull
    public Settings getSettings() {
        return this.settings;
    }

    @Listener
    public void onGamePreInit(@Nullable final GamePreInitializationEvent event) {
        this.logger.info("Plugin Initializing...");
        PRank = this;
    }

    @Listener
    public void onChangeServiceProvider(ChangeServiceProviderEvent event) {
        if (event.getService().equals(PermissionService.class)) {
            this.permservice = (PermissionService) event.getNewProviderRegistration().getProvider();
        }
    }

    @Listener
    public void onGameInit(@Nullable final GameInitializationEvent event) {
        final ProviderRegistration<PlaceholderService> providerPhService = Sponge.getServiceManager().getRegistration(PlaceholderService.class).orElse(null);

        if (providerPhService == null) {
            this.logger.error("Unable to initialize plugin. PixelMMO requires a PlaceholderService.");
            return;
        }

        //providerPhService.ifPresent(placeholderServiceProviderRegistration -> phService = placeholderServiceProviderRegistration.getProvider());
        phService = providerPhService.getProvider();
        //phService = Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);


        if (Sponge.getServiceManager().getRegistration(PermissionService.class).get().getPlugin().getId().equalsIgnoreCase("sponge")) {
            this.logger.error("Unable to initialize plugin. PRank requires a PermissionService like  LuckPerms, PEX, PermissionsManager.");
            return;
        }

        Optional<ProviderRegistration<LuckPerms>> provider = Sponge.getServiceManager().getRegistration(LuckPerms.class);
        provider.ifPresent(luckPermsProviderRegistration -> LPAPI = luckPermsProviderRegistration.getProvider());

        this.permdescbuilder = this.permservice.newDescriptionBuilder(this.getPluginContainer());
        if (this.permdescbuilder != null) {

            this.permdescbuilder
                    .id(PRPermissions.COMMAND_MAIN)
                    .description(Text.of("Allows the use of /prank"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(PRPermissions.COMMAND_XP_BASE)
                    .description(Text.of("Allows the use of /xp"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(PRPermissions.COMMAND_XP_SET)
                    .description(Text.of("Allows the use of /xp set"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(PRPermissions.COMMAND_XP_ADD)
                    .description(Text.of("Allows the use of /xp add"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(PRPermissions.COMMAND_XP_REMOVE)
                    .description(Text.of("Allows the use of /xp remove"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            this.permdescbuilder
                    .id(PRPermissions.COMMAND_XPM_BASE)
                    .description(Text.of("Allows the use of /xpm"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
        }

        try {
            if (!Files.exists(this.configdirpath)) {
                Files.createDirectories(this.configdirpath);
            }
        } catch (final IOException ex) {
            this.logger.error("Error on creating root plugin directory: {}", ex);
        }

        this.settings.Load(this.configfullpath, this);
        PRankListener listener = new PRankListener();
        Sponge.getEventManager().registerListeners(this, listener);

        Pixelmon.EVENT_BUS.register(new PixelmonEventListener());
        this.logger.info("Plugin Initialized successfully!");
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        this.logger.info("Loading...");

        phService.loadAll(this, this).stream().map(builder -> {

            switch (builder.getId()) {
                case "prankplayer":
                    return builder.description("Gets player Rank");
                case "prankcurrentxp":
                    return builder.description("Gets player current xp");
                case "prankneededxp":
                    return builder.description("Gets player needed xp");
                case "prankxppercent":
                    return builder.description("Gets player xp percentage");

            }
            return builder;
        }).map(builder -> builder.author(PluginData.author1).version(PluginData.version)).forEach(builder -> {
            try {
                builder.buildAndRegister();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        this.cmdManager = new CommandManager(game, this);
        this.logger.info("Loaded!");
    }

    @Placeholder(id = "prankplayer")
    public String getPrank(@Source Player src) {
        io.github.poqdavid.prank.Utils.Data.PlayerData pd = Tools.GetPD(src);
        Rank cr1 = GetRank(pd);
        return cr1.getTitle();
    }

    @Placeholder(id = "prankcurrentxp")
    public String getCurrentXP(@Source Player src) {
        io.github.poqdavid.prank.Utils.Data.PlayerData pd = Tools.GetPD(src);
        return "" + Tools.GetCurrentXP(pd);
    }

    @Placeholder(id = "prankneededxp")
    public String getNeededXP(@Source Player src) {
        io.github.poqdavid.prank.Utils.Data.PlayerData pd = Tools.GetPD(src);

        return "" + Tools.GetNeededXP(pd);
    }

    @Placeholder(id = "prankxppercent")
    public String getRankPercentage(@Source Player src) {
        io.github.poqdavid.prank.Utils.Data.PlayerData pd = Tools.GetPD(src);

        return "" + Tools.GetRankPercentage("mode2", pd).intValue();
    }


}
