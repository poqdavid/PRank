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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PRLogger {
    private final Logger logger;

    public PRLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    public void info(String msg) {
        this.logger.info(CText.get(CText.Colors.YELLOW, 0, msg));
    }

    public void error(String msg) {
        this.logger.error(CText.get(CText.Colors.RED, 0, msg));
    }

    public void error(String msg, Throwable tw) {
        this.logger.error(CText.get(CText.Colors.RED, 0, msg), tw);
    }
}
