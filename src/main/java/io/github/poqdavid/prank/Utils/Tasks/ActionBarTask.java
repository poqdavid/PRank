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

package io.github.poqdavid.prank.Utils.Tasks;

import io.github.poqdavid.prank.PRank;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.function.Consumer;

public class ActionBarTask implements Consumer<Task> {
    public Player player;

    public ActionBarTask(Player player) {
        this.player = player;
    }

    @Override
    public void accept(Task task) {
        if (PRank.actionbartext.containsKey(player.getUniqueId())) {
            Text t = PRank.actionbartext.get(player.getUniqueId());

            if (t.toPlain().contains("remove>task")) {
                PRank.getInstance().getLogger().info("Removed Task");
                task.cancel();
            } else {
                this.Run(this.player);
            }
        }
    }

    private void Run(Player player) {
        try {
            if (PRank.actionbartext.containsKey(this.player.getUniqueId())) {
                final Text t = PRank.actionbartext.get(this.player.getUniqueId());
                if (t != Text.of("remove>task")) {
                    player.sendMessage(ChatTypes.ACTION_BAR, t);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
