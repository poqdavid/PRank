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

package io.github.poqdavid.prank.Commands;


import io.github.poqdavid.prank.PRank;
import io.github.poqdavid.prank.Permission.PRPermissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class XpmCMD implements CommandExecutor {

    public static Text getDescription() {
        return Text.of("/xpm");
    }

    public static String[] getAlias() {
        return new String[]{"xpm"};
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        final String mode = args.<String>getOne("mode").orElse(null);
        final Integer amount = args.<Integer>getOne("amount").orElse(null);
        if (src instanceof Player) {
            if (src.hasPermission(PRPermissions.COMMAND_XPM_BASE)) {
                if (mode != null) {
                    if (mode.equalsIgnoreCase("captures")) {
                        PRank.getInstance().getSettings().getCaptures().setMultiplier(amount);
                    }

                    if (mode.equalsIgnoreCase("hatch")) {
                        PRank.getInstance().getSettings().getHatch().setMultiplier(amount);
                    }

                    if (mode.equalsIgnoreCase("kills")) {
                        PRank.getInstance().getSettings().getKills().setMultiplier(amount);
                    }
                }
            } else {
                throw new CommandPermissionException(Text.of("You don't have permission to use this command."));
            }
        } else {
            if (mode != null) {
                if (mode.equalsIgnoreCase("captures")) {
                    PRank.getInstance().getSettings().getCaptures().setMultiplier(amount);
                }

                if (mode.equalsIgnoreCase("hatch")) {
                    PRank.getInstance().getSettings().getHatch().setMultiplier(amount);
                }

                if (mode.equalsIgnoreCase("kills")) {
                    PRank.getInstance().getSettings().getKills().setMultiplier(amount);
                }
            }
        }


        return CommandResult.success();
    }
}
