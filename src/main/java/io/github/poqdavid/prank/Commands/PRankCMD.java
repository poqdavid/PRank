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
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.net.MalformedURLException;
import java.net.URL;

public class PRankCMD implements CommandExecutor {

    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        this.Help(src);
        return CommandResult.success();
    }

    public void Help(CommandSource src) {
        PaginationService paginationService = PRank.getInstance().getGame().getServiceManager().provide(PaginationService.class).get();
        PaginationList.Builder builder = paginationService.builder();
        URL url1 = null;
        try {
            url1 = new URL("https://about.me/poqdavid/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (src instanceof Player) {
            Text h1 = Text.builder("Author: ").color(TextColors.BLUE).style(TextStyles.BOLD).build();
            Text h2 = Text.builder("POQDavid").color(TextColors.GRAY).style(TextStyles.BOLD).onHover(TextActions.showText(Text.of(url1.toString()))).onClick(TextActions.openUrl(url1)).build();

            builder.title(Text.of(TextColors.DARK_AQUA, "PRank - V" + PRank.getInstance().getVersion()))
                    .header(Text.of(h1, h2))
                    .contents(
                            Text.of(TextColors.BLUE, TextStyles.ITALIC, "")
                    )
                    .padding(Text.of("="))
                    .sendTo(src);
        } else {
            Text h1 = Text.builder("Author: ").color(TextColors.BLUE).style(TextStyles.BOLD).build();
            Text h2 = Text.builder("POQDavid - https://about.me/poqdavid/").color(TextColors.GRAY).style(TextStyles.BOLD).onHover(TextActions.showText(Text.of(url1.toString()))).onClick(TextActions.openUrl(url1)).build();

            builder.title(Text.of(TextColors.DARK_AQUA, "PRank - V" + PRank.getInstance().getVersion()))
                    .header(Text.of(h1, h2))
                    .contents(
                            Text.of(TextColors.BLUE, TextStyles.ITALIC, "")
                    )
                    .padding(Text.of("="))
                    .sendTo(src);

        }
    }
}
