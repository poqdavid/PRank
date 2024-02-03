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

public class Kills implements Serializable {

    private final static long serialVersionUID = -2063783106430618409L;
    @SerializedName("normal")
    @Expose
    private Integer normal;
    @SerializedName("shiny")
    @Expose
    private Integer shiny;
    @SerializedName("legendary")
    @Expose
    private Integer legendary;
    @SerializedName("boss")
    @Expose
    private Integer boss;
    @SerializedName("ultrabeast")
    @Expose
    private Integer ultrabeast;
    @SerializedName("multiplier")
    @Expose
    private Integer multiplier;

    /**
     * No args constructor for use in serialization
     */
    public Kills() {
    }

    /**
     * @param normal
     * @param shiny
     * @param legendary
     * @param boss
     * @param ultrabeast
     * @param multiplier
     */
    public Kills(Integer normal, Integer shiny, Integer legendary, Integer boss, Integer ultrabeast, Integer multiplier) {
        super();
        this.normal = normal;
        this.shiny = shiny;
        this.legendary = legendary;
        this.boss = boss;
        this.ultrabeast = ultrabeast;
        this.multiplier = multiplier;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Kills withNormal(Integer normal) {
        this.normal = normal;
        return this;
    }

    public Integer getShiny() {
        return shiny;
    }

    public void setShiny(Integer shiny) {
        this.shiny = shiny;
    }

    public Kills withShiny(Integer shiny) {
        this.shiny = shiny;
        return this;
    }

    public Integer getLegendary() {
        return legendary;
    }

    public void setLegendary(Integer legendary) {
        this.legendary = legendary;
    }

    public Kills withLegendary(Integer legendary) {
        this.legendary = legendary;
        return this;
    }

    public Integer getBoss() {
        return boss;
    }

    public void setBoss(Integer boss) {
        this.boss = boss;
    }

    public Kills withBoss(Integer boss) {
        this.boss = boss;
        return this;
    }

    public Integer getUltrabeast() {
        return ultrabeast;
    }

    public void setUltrabeast(Integer ultrabeast) {
        this.ultrabeast = ultrabeast;
    }

    public Kills withUltrabeast(Integer ultrabeast) {
        this.ultrabeast = ultrabeast;
        return this;
    }

    public Integer getMultiplier() {
        if(multiplier == 0){
            return 1;
        }
        else {
            return multiplier;
        }
    }

    public void setMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
    }

    public Kills withMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("normal", normal).append("shiny", shiny).append("legendary", legendary).append("boss", boss).append("ultrabeast", ultrabeast).append("multiplier", multiplier).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(legendary).append(boss).append(normal).append(ultrabeast).append(shiny).append(multiplier).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Kills)) {
            return false;
        }
        Kills rhs = ((Kills) other);
        return new EqualsBuilder().append(legendary, rhs.legendary).append(boss, rhs.boss).append(normal, rhs.normal).append(ultrabeast, rhs.ultrabeast).append(shiny, rhs.shiny).append(multiplier, rhs.multiplier).isEquals();
    }

}
