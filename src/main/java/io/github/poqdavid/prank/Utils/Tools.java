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

package io.github.poqdavid.prank.Utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import io.github.poqdavid.prank.PRank;
import io.github.poqdavid.prank.Utils.Data.PlayerData;
import io.github.poqdavid.prank.Utils.Data.Rank;
import io.github.poqdavid.prank.Utils.Tasks.ActionBarTask;
import io.github.poqdavid.prank.Utils.gson.GsonUTCDateAdapter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.context.ContextManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.apache.commons.io.FileUtils;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Dimension;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Tools {

    public static boolean WriteFile(File file, String content, PRank pr) {
        FileWriter filew = null;

        if (file.getParentFile().mkdirs()) {
            pr.getLogger().error("Created missing directories");
        }

        if (file.exists()) {
            try {
                filew = new FileWriter(file.toString(), false);
                filew.write(content);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (filew != null) {
                    try {
                        filew.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        } else {
            try {
                if (file.createNewFile()) {
                    if (!content.equals("lock")) {
                        pr.getLogger().info("Created new file: " + file.getName());
                    }
                    WriteFile(file, content, pr);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static Player getPlayer(CommandSource src, PRank pr) {
        final Server server = pr.getGame().getServer();
        return server.getPlayer(((Player) src.getCommandSource().get()).getUniqueId()).get();
    }

    public static EntityPlayerMP getPlayerE(CommandSource src, PRank pr) {
        final Server server = pr.getGame().getServer();
        return (EntityPlayerMP) server.getPlayer(((Player) src.getCommandSource().get()).getUniqueId()).get();
    }

    public static Optional<Player> getPlayer(Cause cause) {
        final Optional<Player> player = cause.first(Player.class);
        return player;
    }

    public static void savetojson(Path file, Settings jsonob, PRank pr) {
        final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (jsonob == null) {
            WriteFile(file.toFile(), "{}", pr);
        } else {
            WriteFile(file.toFile(), gson.toJson(jsonob, jsonob.getClass()), pr);
        }
    }

    public static Settings loadfromjson(Path file, Settings defob, PRank pr) {

        if (!Files.exists(file)) {
            try {
                savetojson(file, defob, pr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final Gson gson = new Gson();
        Settings out = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file.toString()));
            out = gson.fromJson(br, defob.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return out;
    }

    public static void saveplayerdata(PlayerData pd, Path filePath, PRank pr) {
        final File file = filePath.toFile();
        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).setPrettyPrinting().disableHtmlEscaping().create();

        if (pd == null) {
            WriteFile(file, "{}", pr);
        } else {
            WriteFile(file, gson.toJson(pd), pr);
        }
    }

    public static PlayerData loadplayerdata(Path filePath, PRank pr) {
        final File file = filePath.toFile();
        if (!file.exists()) {
            Tools.WriteFile(file, "{}", pr);
        }

        final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonUTCDateAdapter()).create();
        Type type = new TypeToken<PlayerData>() {
        }.getType();

        PlayerData data = null;
        try {
            data = gson.fromJson(FileUtils.readFileToString(file, "UTF-8"), type);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static boolean hasPermission(User user, String permission) {
        ContextManager contextManager = PRank.getInstance().LPAPI.getContextManager();
        ImmutableContextSet contextSet = contextManager.getContext(user).orElseGet(contextManager::getStaticContext);

        CachedPermissionData permissionData = user.getCachedData().getPermissionData(QueryOptions.contextual(contextSet));
        return permissionData.checkPermission(permission).asBoolean();
    }

    public static PlayerData GetPD(Player player) {
        final Path filep = Paths.get(Paths.get(PRank.getInstance().recordsDir.toString(), player.getUniqueId().toString()).toString() + ".json");
        PlayerData pd = new PlayerData();
        if (filep.toFile().exists()) {
            pd = Tools.loadplayerdata(filep, PRank.getInstance());
        } else {
            Tools.saveplayerdata(pd, filep, PRank.getInstance());
            pd = Tools.loadplayerdata(filep, PRank.getInstance());
        }

        return pd;
    }

    public static Rank GetRank(PlayerData pd) {
        Rank temp = null;
        Rank fr = PRank.getInstance().getSettings().getRanks().get(0);
        if (pd.getXp() < fr.getXp()) {
            Rank xrank = new Rank("none", 0, "", "--");
            temp = xrank;
        } else {
            for (Rank r : PRank.getInstance().getSettings().getRanks()) {
                if (r.getXp() <= pd.getXp()) {
                    temp = r;
                }
            }
        }


        if (temp == null) {

            temp = PRank.getInstance().getSettings().getRanks().get(0);
        }
        return temp;
    }

    public static Boolean isLegendary(Pokemon p) {
        return EnumSpecies.legendaries.contains(p.getSpecies().getPokemonName());
    }

    public static Boolean isUltraBeast(Pokemon p) {

        return EnumSpecies.ultrabeasts.contains(p.getSpecies().getPokemonName());
    }

    public static Boolean isShiny(Pokemon p) {
        return p.isShiny();
    }

    public static Boolean isBoss(Pokemon p) {
        boolean temp = false;
        EntityPixelmon ep = p.getPixelmonIfExists();
        if (ep != null) {
            temp = ep.isBossPokemon();
        }
        return temp;
    }

    public static PlayerData CalcKills(PlayerData pdx, Pokemon ep) {
        PlayerData pd = pdx;
        final boolean isLegendary = Tools.isLegendary(ep);
        final boolean isUltraBeast = Tools.isUltraBeast(ep);
        final boolean isShiny = Tools.isShiny(ep);
        final boolean isBoss = Tools.isBoss(ep);

        if (!isShiny & !isBoss & !isLegendary & !isUltraBeast) {
            pd.getKills().setNormal(pd.getKills().getNormal() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getKills().getNormal()) * PRank.getInstance().getSettings().getKills().getMultiplier());
        }

        if (isShiny) {
            pd.getKills().setShiny(pd.getKills().getShiny() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getKills().getShiny()) * PRank.getInstance().getSettings().getKills().getMultiplier());
        }

        if (isBoss) {
            pd.getKills().setBoss(pd.getKills().getBoss() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getKills().getBoss()) * PRank.getInstance().getSettings().getKills().getMultiplier());
        }

        if (isLegendary) {
            pd.getKills().setLegendary(pd.getKills().getLegendary() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getKills().getLegendary()) * PRank.getInstance().getSettings().getKills().getMultiplier());
        }

        if (isUltraBeast) {
            pd.getKills().setUltrabeast(pd.getKills().getUltrabeast() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getKills().getUltrabeast()) * PRank.getInstance().getSettings().getKills().getMultiplier());
        }

        return pd;
    }

    public static PlayerData CalcCaptures(PlayerData pdx, Pokemon ep) {
        PlayerData pd = pdx;
        final boolean isUltraBeast = Tools.isUltraBeast(ep);
        final boolean isShiny = Tools.isShiny(ep);

        if (!isShiny & !isUltraBeast) {
            pd.getCaptures().setNormal(pd.getCaptures().getNormal() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getCaptures().getNormal()) * PRank.getInstance().getSettings().getCaptures().getMultiplier());
        }

        if (isShiny) {
            pd.getCaptures().setShiny(pd.getCaptures().getShiny() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getCaptures().getShiny()) * PRank.getInstance().getSettings().getCaptures().getMultiplier());
        }

        if (isUltraBeast) {
            pd.getCaptures().setUltrabeast(pd.getCaptures().getUltrabeast() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getCaptures().getUltrabeast()) * PRank.getInstance().getSettings().getCaptures().getMultiplier());
        }

        return pd;
    }

    public static PlayerData CalcHatch(PlayerData pdx, Pokemon ep) {
        PlayerData pd = pdx;

        final boolean isShiny = Tools.isShiny(ep);

        if (!isShiny) {
            pd.getHatch().setNormal(pd.getHatch().getNormal() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getHatch().getNormal()) * PRank.getInstance().getSettings().getHatch().getMultiplier());
        }

        if (isShiny) {
            pd.getHatch().setShiny(pd.getHatch().getShiny() + 1);
            pd.setXp((pd.getXp() + PRank.getInstance().getSettings().getHatch().getShiny()) * PRank.getInstance().getSettings().getHatch().getMultiplier());
        }

        return pd;
    }

    public static void SetXP(Integer amount, Player player) throws CommandException {
        if (amount < 0) {
            throw new CommandPermissionException(Text.of("Error amount can't be less than 0."));
        }

        String uuid = player.getUniqueId().toString();
        PlayerData pd = PRank.getInstance().PlayerData.get(uuid);
        final Path filePath = Paths.get(Paths.get(PRank.getInstance().recordsDir.toString(), uuid).toString() + ".json");

        pd.setXp(amount);

        Tools.UpdateScoreInfo(pd, uuid);

        if (PRank.getInstance().getSettings().getGui().getBossbar()) {
            Tools.UpdateEXPBar(player, pd);
        }

        if (PRank.getInstance().getSettings().getGui().getActionbar()) {
            Tools.UpdateActionBar(player, pd);
        }

        PRank.getInstance().PlayerData.put(uuid, pd);
        Tools.saveplayerdata(pd, filePath, PRank.getInstance());
    }

    public static void AddXP(Integer amount, Player player) throws CommandException {
        String uuid = player.getUniqueId().toString();
        PlayerData pd = PRank.getInstance().PlayerData.get(uuid);

        Tools.SetXP(pd.getXp() + amount, player);
    }

    public static void RemoveXP(Integer amount, Player player) throws CommandException {
        String uuid = player.getUniqueId().toString();
        PlayerData pd = PRank.getInstance().PlayerData.get(uuid);

        Tools.SetXP(pd.getXp() - amount, player);
    }

    public static void UpdateScore(String scoretype, String st_uuid, PlayerData pdx, Pokemon pe) {
        LuckPerms api = PRank.getInstance().LPAPI;
        final Path filep1 = Paths.get(Paths.get(PRank.getInstance().recordsDir.toString(), st_uuid).toString() + ".json");
        PlayerData pd = pdx;

        final Rank cr = GetRank(pd);

        if (scoretype.equals("kill")) {
            pd = Tools.CalcKills(pd, pe);
        }

        if (scoretype.equals("capture")) {
            pd = Tools.CalcCaptures(pd, pe);
        }

        if (scoretype.equals("hatch")) {
            pd = Tools.CalcHatch(pd, pe);
        }

        User u = api.getUserManager().getUser(UUID.fromString(st_uuid));
        Rank r = GetRank(pd);
        String snode = "group." + r.getId();

        Node node = Node.builder(snode).build();
        PRank.getInstance().PlayerData.put(st_uuid, pd);
        Tools.saveplayerdata(pd, filep1, PRank.getInstance());
        Rank fr = PRank.getInstance().getSettings().getRanks().get(0);


        if (!(pd.getXp() < fr.getXp())) {
            if (!snode.equals("group.none")) {
                if (!hasPermission(u, snode)) {

                    for (Rank r2 : PRank.getInstance().getSettings().getRanks()) {
                        Node nodex = Node.builder("group." + r2.getId()).build();

                        if (u != null) {
                            u.data().remove(nodex);
                        }
                    }

                    if (u != null) {
                        u.data().add(node);
                    }

                    api.getUserManager().saveUser(u);
                    Player p1 = Tools.GetSpongePlayer(st_uuid);
                    if (r.getCommand().contains("<separator>")) {
                        final String[] commands = r.getCommand().split("<separator>");
                        for (String cmd : commands) {

                            String cmdx = Tools.ProcessString(cmd, p1);
                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdx);
                        }
                    } else {
                        String cmdx = Tools.ProcessString(r.getCommand(), p1);
                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdx);
                    }
                }
            }

        }

        PRank.getInstance().PlayerData.put(st_uuid, pd);
        Tools.saveplayerdata(pd, filep1, PRank.getInstance());
    }


    public static void UpdateScoreInfo(PlayerData pd, String st_uuid) {
        LuckPerms api = PRank.getInstance().LPAPI;
        User u = api.getUserManager().getUser(UUID.fromString(st_uuid));
        Rank r = GetRank(pd);
        String snode = "group." + r.getId();

        Node node = Node.builder(snode).build();
        Rank fr = PRank.getInstance().getSettings().getRanks().get(0);

        if (!(pd.getXp() < fr.getXp())) {
            if (!snode.equals("group.none")) {
                if (!hasPermission(u, snode)) {

                    for (Rank r2 : PRank.getInstance().getSettings().getRanks()) {
                        Node nodex = Node.builder("group." + r2.getId()).build();
                        if (u != null) {
                            u.data().remove(nodex);
                        }
                    }

                    if (u != null) {
                        u.data().add(node);
                    }

                    api.getUserManager().saveUser(u);
                    Player p1 = Tools.GetSpongePlayer(st_uuid);
                    if (r.getCommand().contains("<separator>")) {
                        final String[] commands = r.getCommand().split("<separator>");
                        for (String cmd : commands) {

                            String cmdx = Tools.ProcessString(cmd, p1);
                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdx);
                        }
                    } else {
                        String cmdx = Tools.ProcessString(r.getCommand(), p1);
                        Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmdx);
                    }
                }
            }

        }
    }

    public static void UpdateEXPBar(EntityLivingBase PEntity, PlayerData pd) {
        final EntityPlayer player = (EntityPlayer) PEntity;
        final Optional<Player> os_player = Sponge.getServer().getPlayer(player.getUniqueID());
        os_player.ifPresent(value -> Tools.UpdateEXPBar(value, pd));
    }

    public static void UpdateEXPBar(Player player, PlayerData pd) {
        ServerBossBar bar = PRank.xpbar.get(player.getUniqueId());
        bar.setPercent(Tools.GetRankPercentage("mode1", pd));

        final String bossBarString = PRank.getInstance().getSettings().getGui().getBossbartext();

        final Text bossBarText = Tools.ProcessText(bossBarString, player);
        bar.setName(bossBarText);

    }

    public static void UpdateActionBar(Player player, PlayerData pd) {
        final String actionBarString = PRank.getInstance().getSettings().getGui().getActionbartext();
        final Text actionBarText = Tools.ProcessText(actionBarString, player);

        PRank.actionbartext.put(player.getUniqueId(), actionBarText);

        if (!Tools.TaskAvilable(player.getUniqueId() + "", "ActionBar")) {
            Task.builder().execute(new ActionBarTask(player))
                    .async().interval(2, TimeUnit.SECONDS)
                    .name(player.getUniqueId() + ">ActionBar")
                    .submit(PRank.getInstance());

        }
    }

    public static Integer GetNeededXP(PlayerData pd) {
        Integer temp = 0;
        Rank cr1 = GetRank(pd);
        Rank fr = PRank.getInstance().getSettings().getRanks().get(0);
        Rank lr = PRank.getInstance().getSettings().getRanks().get(PRank.getInstance().getSettings().getRanks().size() - 1);
        if (cr1.getTitle().equals(lr.getTitle())) {
            temp = 100;
        } else {
            if (pd.getXp() < fr.getXp()) {
                temp = fr.getXp();
            } else {
                Rank nr = PRank.getInstance().getSettings().getRanks().get(PRank.getInstance().getSettings().getRanks().indexOf(cr1) + 1);
                temp = nr.getXp() - cr1.getXp();
            }
        }
        return temp;
    }

    public static Integer GetCurrentXP(PlayerData pd) {
        Integer temp = 0;
        Rank cr1 = GetRank(pd);
        Rank fr = PRank.getInstance().getSettings().getRanks().get(0);
        Rank lr = PRank.getInstance().getSettings().getRanks().get(PRank.getInstance().getSettings().getRanks().size() - 1);
        if (cr1.getTitle().equals(lr.getTitle())) {
            temp = 100;
        } else {
            if (pd.getXp() < fr.getXp()) {
                temp = pd.getXp();
            } else {
                temp = pd.getXp() - cr1.getXp();
            }
        }
        return temp;
    }

    public static Float GetRankPercentage(String mode, PlayerData pd) {
        float temp = 0f;
        Rank cr1 = GetRank(pd);
        Rank lr = PRank.getInstance().getSettings().getRanks().get(PRank.getInstance().getSettings().getRanks().size() - 1);

        if (cr1.getTitle().equals(lr.getTitle())) {
            if (mode.equals("mode1")) {
                temp = 1.0f;
            }
            if (mode.equals("mode2")) {
                temp = 100f;
            }

        } else {
            int xpt = Tools.GetNeededXP(pd);

            int xpg = Tools.GetCurrentXP(pd);

            float percentage1 = (xpg * 100 / xpt);
            if (mode.equals("mode1")) {
                temp = (percentage1 * 1.0f) / 100;
            }
            if (mode.equals("mode2")) {
                temp = percentage1;
            }
        }
        return temp;
    }

    public static Boolean TaskAvilable(String uuid, String st_lookup) {
        Boolean temp_out = false;
        for (Task task : Sponge.getScheduler().getScheduledTasks(PRank.getInstance())) {
            if (task.getName().contains(uuid)) {
                if (task.getName().contains(st_lookup)) {
                    temp_out = true;
                }
            }
        }
        return temp_out;
    }

    public static Double GetMAXXP(int level) {
        double temp = 0;

        if (level <= 16) {
            temp = Math.pow(level, 2) + 6 * level;
        }

        if (level >= 17 & level <= 31) {
            temp = 2.5 * Math.pow(level, 2) - 40.5 * level + 360;
        }

        if (level >= 32) {
            temp = 4.5 * Math.pow(level, 2) - 162.5 * level + 2220;
        }

        return temp;
    }

    public static Player GetSpongePlayer(EntityLivingBase PEntity) {
        Player s_player = null;
        final EntityPlayer player = (EntityPlayer) PEntity;
        final Optional<Player> os_player = Sponge.getServer().getPlayer(player.getUniqueID());

        if (os_player.isPresent()) {
            s_player = os_player.get();
        }

        return s_player;
    }

    public static Player GetSpongePlayer(String st_uuid) {
        Player s_player = null;
        //final EntityPlayer player = (EntityPlayer) PEntity;
        final Optional<Player> os_player = Sponge.getServer().getPlayer(UUID.fromString(st_uuid));

        if (os_player.isPresent()) {
            s_player = os_player.get();
        }

        return s_player;
    }

    public static Text ProcessText(String text, Player player) {
        final Text tx = TextSerializers.FORMATTING_CODE.deserialize(text);
        return PRank.getInstance().phService.replacePlaceholders(tx, player, player);
    }

    public static String ProcessString(String text, Player player) {
        final Text tx = TextSerializers.FORMATTING_CODE.deserialize(text);
        return TextSerializers.FORMATTING_CODE.serialize(PRank.getInstance().phService.replacePlaceholders(tx, player, player));
    }
}