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

package io.github.poqdavid.prank.Listeners;

import io.github.poqdavid.prank.PRank;
import io.github.poqdavid.prank.Utils.Data.PlayerData;
import io.github.poqdavid.prank.Utils.Data.Rank;
import io.github.poqdavid.prank.Utils.Tools;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;

import static io.github.poqdavid.prank.Utils.Tools.GetRank;

@SuppressWarnings("unused")
public class PRankListener {

    @Listener
    public void onPlayerJoin(ClientConnectionEvent.Join event) {
        final Player player = Tools.getPlayer(event.getCause()).get();
        final String keyx = "" + player.getUniqueId();

        PlayerData pd = Tools.GetPD(player);

        if (!PRank.getInstance().PlayerData.containsKey(keyx)) {
            PRank.getInstance().PlayerData.put(keyx, pd);
        }

        if (PRank.getInstance().getSettings().getGui().getBossbar()) {
            Rank cr1 = GetRank(pd);

            final String bossBarString = PRank.getInstance().getSettings().getGui().getBossbartext();
            final Text bossBarText = Tools.ProcessText(bossBarString, player);

            ServerBossBar bar = ServerBossBar.builder().from(PRank.defaultbar).percent(Tools.GetRankPercentage("mode1", pd)).build();

            bar.setName(bossBarText);
            bar.addPlayer(player);
            bar.setVisible(true);
            PRank.xpbar.put(player.getUniqueId(), bar);
        }

        if (PRank.getInstance().getSettings().getGui().getActionbar()) {
            Tools.UpdateActionBar(player, pd);
        }
    }

    @Listener
    public void onPlayerDisconnect(ClientConnectionEvent.Disconnect event) {
        final Player player = Tools.getPlayer(event.getCause()).get();
        final String st_uuid = "" + player.getUniqueId();
        PRank.getInstance().PlayerData.remove(st_uuid);
        if (PRank.getInstance().getSettings().getGui().getBossbar()) {
            PRank.xpbar.remove(player.getUniqueId());
        }
        if (PRank.actionbartext.containsKey(player.getUniqueId())) {
            PRank.actionbartext.put(player.getUniqueId(), Text.of("remove>task"));
        }
    }
}
