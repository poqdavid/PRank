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

public class Hatch implements Serializable {

    private final static long serialVersionUID = 5028114401528267763L;
    @SerializedName("normal")
    @Expose
    private Integer normal;
    @SerializedName("shiny")
    @Expose
    private Integer shiny;

    /**
     * No args constructor for use in serialization
     */
    public Hatch() {
    }

    /**
     * @param normal
     * @param shiny
     */
    public Hatch(Integer normal, Integer shiny) {
        super();
        this.normal = normal;
        this.shiny = shiny;
    }

    public Integer getNormal() {
        return normal;
    }

    public void setNormal(Integer normal) {
        this.normal = normal;
    }

    public Hatch withNormal(Integer normal) {
        this.normal = normal;
        return this;
    }

    public Integer getShiny() {
        return shiny;
    }

    public void setShiny(Integer shiny) {
        this.shiny = shiny;
    }

    public Hatch withShiny(Integer shiny) {
        this.shiny = shiny;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("normal", normal).append("shiny", shiny).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(normal).append(shiny).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Hatch)) {
            return false;
        }
        Hatch rhs = ((Hatch) other);
        return new EqualsBuilder().append(normal, rhs.normal).append(shiny, rhs.shiny).isEquals();
    }

}
