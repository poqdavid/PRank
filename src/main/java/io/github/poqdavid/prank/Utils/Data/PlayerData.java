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

package io.github.poqdavid.prank.Utils.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class PlayerData implements Serializable {

    private final static long serialVersionUID = 659465001648508485L;
    @SerializedName("xp")
    @Expose
    private Integer xp;
    @SerializedName("kills")
    @Expose
    private Kills kills;
    @SerializedName("captures")
    @Expose
    private Captures captures;
    @SerializedName("hatch")
    @Expose
    private Hatch hatch;

    /**
     * No args constructor for use in serialization
     */
    public PlayerData() {
        this.setXp(0);
        this.setKills(new Kills(0, 0, 0, 0, 0, 0));
        this.setCaptures(new Captures(0, 0, 0, 0, 0));
        this.setHatch(new Hatch(0, 0, 0));
    }

    /**
     * @param captures
     * @param hatch
     * @param kills
     * @param xp
     */
    public PlayerData(Integer xp, Kills kills, Captures captures, Hatch hatch) {
        super();
        this.xp = xp;
        this.kills = kills;
        this.captures = captures;
        this.hatch = hatch;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public PlayerData withXp(Integer xp) {
        this.xp = xp;
        return this;
    }

    public Kills getKills() {
        return kills;
    }

    public void setKills(Kills kills) {
        this.kills = kills;
    }

    public PlayerData withKills(Kills kills) {
        this.kills = kills;
        return this;
    }

    public Captures getCaptures() {
        return captures;
    }

    public void setCaptures(Captures captures) {
        this.captures = captures;
    }

    public PlayerData withCaptures(Captures captures) {
        this.captures = captures;
        return this;
    }

    public Hatch getHatch() {
        return hatch;
    }

    public void setHatch(Hatch hatch) {
        this.hatch = hatch;
    }

    public PlayerData withHatch(Hatch hatch) {
        this.hatch = hatch;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("xp", xp).append("kills", kills).append("captures", captures).append("hatch", hatch).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(captures).append(hatch).append(kills).append(xp).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PlayerData)) {
            return false;
        }
        PlayerData rhs = ((PlayerData) other);
        return new EqualsBuilder().append(captures, rhs.captures).append(hatch, rhs.hatch).append(kills, rhs.kills).append(xp, rhs.xp).isEquals();
    }

}