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

package io.github.poqdavid.prank.Utils.Data.PlayerDatas;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Captures implements Serializable {

    private final static long serialVersionUID = -3263287284082037455L;
    @SerializedName("normal")
    @Expose
    private Integer normal;
    @SerializedName("shiny")
    @Expose
    private Integer shiny;
    @SerializedName("legendary")
    @Expose
    private Integer legendary;
    @SerializedName("ultrabeast")
    @Expose
    private Integer ultrabeast;

    /**
     * No args constructor for use in serialization
     */
    public Captures() {
    }

    /**
     * @param legendary
     * @param normal
     * @param ultrabeast
     * @param shiny
     */
    public Captures(Integer normal, Integer shiny, Integer legendary, Integer ultrabeast) {
        super();
        this.normal = normal;
        this.shiny = shiny;
        this.legendary = legendary;
        this.ultrabeast = ultrabeast;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Captures withNormal(Integer normal) {
        this.normal = normal;
        return this;
    }

    public Integer getShiny() {
        return shiny;
    }

    public void setShiny(Integer shiny) {
        this.shiny = shiny;
    }

    public Captures withShiny(Integer shiny) {
        this.shiny = shiny;
        return this;
    }

    public Integer getLegendary() {
        return legendary;
    }

    public void setLegendary(Integer legendary) {
        this.legendary = legendary;
    }

    public Captures withLegendary(Integer legendary) {
        this.legendary = legendary;
        return this;
    }

    public Integer getUltrabeast() {
        return ultrabeast;
    }

    public void setUltrabeast(Integer ultrabeast) {
        this.ultrabeast = ultrabeast;
    }

    public Captures withUltrabeast(Integer ultrabeast) {
        this.ultrabeast = ultrabeast;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("normal", normal).append("shiny", shiny).append("legendary", legendary).append("ultrabeast", ultrabeast).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(legendary).append(normal).append(ultrabeast).append(shiny).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Captures)) {
            return false;
        }
        Captures rhs = ((Captures) other);
        return new EqualsBuilder().append(legendary, rhs.legendary).append(normal, rhs.normal).append(ultrabeast, rhs.ultrabeast).append(shiny, rhs.shiny).isEquals();
    }

}
