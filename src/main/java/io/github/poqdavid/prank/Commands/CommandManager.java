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
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.HashMap;


public class CommandManager {
    public static CommandSpec PRankCmd;
    public static CommandSpec XpCmd;
    public static CommandSpec XpmCmd;
    private final Game game;
    private final PRank pr;

    public CommandManager(Game game, PRank pr) {
        this.game = game;
        this.pr = pr;
        registerCommands();
    }

    public void registerCommands() {

        HashMap<String, String> xpChoices = new HashMap<String, String>() {{
            put("set", "set");
            put("add", "add");
            put("remove", "remove");
        }};
        XpCmd = CommandSpec.builder()
                .description(Text.of("/xp"))
                .arguments(
                        GenericArguments.player(Text.of("player")),
                        GenericArguments.choices(Text.of("mode"), xpChoices),
                        GenericArguments.integer(Text.of("amount"))
                )
                .executor(new XpCMD())
                .build();

        HashMap<String, String> xpmChoices = new HashMap<String, String>() {{
            put("captures", "captures");
            put("hatch", "hatch");
            put("kills", "kills");
        }};
        XpmCmd = CommandSpec.builder()
                .description(Text.of("/xpm"))
                .arguments(
                        GenericArguments.choices(Text.of("mode"), xpmChoices),
                        GenericArguments.integer(Text.of("amount"))
                )
                .executor(new XpmCMD())
                .build();

        PRankCmd = CommandSpec.builder()
                .description(Text.of("PRank"))
                .child(XpCmd, XpCMD.getAlias())
                .child(XpmCmd, XpmCMD.getAlias())
                .executor(new PRankCMD()).build();


        Sponge.getCommandManager().register(pr, PRankCmd, "prank");
    }
}
