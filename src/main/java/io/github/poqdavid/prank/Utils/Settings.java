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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.poqdavid.prank.PRank;
import io.github.poqdavid.prank.Utils.Data.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Settings implements Serializable {

    private final static long serialVersionUID = 5241787346360941374L;
    @SerializedName("gui")
    @Expose
    private Gui gui;
    @SerializedName("kills")
    @Expose
    private Kills kills;
    @SerializedName("captures")
    @Expose
    private Captures captures;
    @SerializedName("hatch")
    @Expose
    private Hatch hatch;
    @SerializedName("ranks")
    @Expose
    private List<Rank> ranks = new ArrayList<Rank>();
    @SerializedName("dimension")
    @Expose
    private Dimension dimension;

    /**
     * No args constructor for use in serialization
     */
    public Settings() {
        this.setGui(new Gui(true, true, "&6%prankplayer% &f- &aXP &a%prankcurrentxp%&f/&a%prankneededxp% &f- &d%prankxppercent%%", "&6%prankplayer%"));
        this.setKills(new Kills(10, 20, 100, 200, 300, 1));
        this.setCaptures(new Captures(30, 50, 100, 200, 1));
        this.setHatch(new Hatch(30, 50, 1));

        List<Rank> r = new ArrayList<Rank>();
        r.add(new Rank("rank1", 100, "say You got rank1 %player%<separator>say gratz %player% with uuid of %player_uuid%", "Rank 1"));
        r.add(new Rank("rank2", 200, "say You got rank2 %player%", "Rank 2"));
        r.add(new Rank("rank3", 300, "say You got rank3 %player%", "Rank 3"));
        r.add(new Rank("rank4", 400, "say You got rank4 %player%", "Rank 4"));
        this.setRanks(r);

        List<Integer> dims = new ArrayList<Integer>();
        dims.add(0);
        dims.add(-1);
        this.setDimension(new Dimension(false, dims));
    }

    /**
     * @param gui
     * @param kills
     * @param captures
     * @param hatch
     * @param ranks
     * @param dimension
     */
    public Settings(Gui gui, Kills kills, Captures captures, Hatch hatch, List<Rank> ranks, Dimension dimension) {
        super();
        this.gui = gui;
        this.kills = kills;
        this.captures = captures;
        this.hatch = hatch;
        this.ranks = ranks;
        this.dimension = dimension;
    }

    public void Load(Path file, PRank pr) {
        try {
            Settings sets = Tools.loadfromjson(file, new Settings(), pr);
            this.setGui(sets.getGui());
            this.setKills(sets.getKills());
            this.setCaptures(sets.getCaptures());
            this.setHatch(sets.getHatch());
            this.setRanks(sets.getRanks());
            this.setDimension(sets.getDimension());
        } catch (Exception e) {
            this.Save(file, pr);
            this.Load(file, pr);
        }
    }

    public void Save(Path file, PRank pr) {
        Tools.savetojson(file, this, pr);
    }

    public Gui getGui() {
        return gui;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public Settings withGui(Gui gui) {
        this.gui = gui;
        return this;
    }

    public Kills getKills() {
        return kills;
    }

    public void setKills(Kills kills) {
        this.kills = kills;
    }

    public Settings withKills(Kills kills) {
        this.kills = kills;
        return this;
    }

    public Captures getCaptures() {
        return captures;
    }

    public void setCaptures(Captures captures) {
        this.captures = captures;
    }

    public Settings withCaptures(Captures captures) {
        this.captures = captures;
        return this;
    }

    public Hatch getHatch() {
        return hatch;
    }

    public void setHatch(Hatch hatch) {
        this.hatch = hatch;
    }

    public Settings withHatch(Hatch hatch) {
        this.hatch = hatch;
        return this;
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public void setRanks(List<Rank> ranks) {
        this.ranks = ranks;
    }

    public Settings withRanks(List<Rank> ranks) {
        this.ranks = ranks;
        return this;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Settings withDimension(Dimension dimension) {
        this.dimension = dimension;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("gui", gui).append("kills", kills).append("captures", captures).append("hatch", hatch).append("ranks", ranks).append("dimension", dimension).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ranks).append(gui).append(captures).append(hatch).append(kills).append(dimension).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Settings)) {
            return false;
        }
        Settings rhs = ((Settings) other);
        return new EqualsBuilder().append(ranks, rhs.ranks).append(gui, rhs.gui).append(captures, rhs.captures).append(hatch, rhs.hatch).append(kills, rhs.kills).append(dimension, rhs.dimension).isEquals();
    }

}