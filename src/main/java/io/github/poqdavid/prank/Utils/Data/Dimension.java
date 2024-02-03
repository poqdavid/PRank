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
import java.util.ArrayList;
import java.util.List;

public class Dimension implements Serializable {

    private final static long serialVersionUID = -2144831899410204866L;
    @SerializedName("Enable")
    @Expose
    private Boolean enable;
    @SerializedName("Dimensions")
    @Expose
    private List<Integer> dimensions = new ArrayList<Integer>();

    /**
     * No args constructor for use in serialization
     */
    public Dimension() {
    }

    /**
     * @param enable
     * @param dimensions
     */
    public Dimension(Boolean enable, List<Integer> dimensions) {
        super();
        this.enable = enable;
        this.dimensions = dimensions;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Dimension withEnable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public List<Integer> getDimension() {
        return dimensions;
    }

    public void setDimension(List<Integer> dimensions) {
        this.dimensions = dimensions;
    }

    public Dimension withDimension(List<Integer> dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("enable", enable).append("dimensions", dimensions).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(enable).append(dimensions).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Dimension)) {
            return false;
        }
        Dimension rhs = ((Dimension) other);
        return new EqualsBuilder().append(enable, rhs.enable).append(dimensions, rhs.dimensions).isEquals();
    }

}
