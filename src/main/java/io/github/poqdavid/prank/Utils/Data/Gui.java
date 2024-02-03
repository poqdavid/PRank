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

public class Gui implements Serializable {

    private final static long serialVersionUID = -4676257836730449132L;
    @SerializedName("bossbar")
    @Expose
    private Boolean bossbar;
    @SerializedName("actionbar")
    @Expose
    private Boolean actionbar;
    @SerializedName("bossbartext")
    @Expose
    private String bossbartext;
    @SerializedName("actionbartext")
    @Expose
    private String actionbartext;

    /**
     * No args constructor for use in serialization
     */
    public Gui() {
    }

    /**
     * @param bossbartext
     * @param actionbartext
     * @param actionbar
     * @param bossbar
     */
    public Gui(Boolean bossbar, Boolean actionbar, String bossbartext, String actionbartext) {
        super();
        this.bossbar = bossbar;
        this.actionbar = actionbar;
        this.bossbartext = bossbartext;
        this.actionbartext = actionbartext;
    }

    public Boolean getBossbar() {
        return bossbar;
    }

    public void setBossbar(Boolean bossbar) {
        this.bossbar = bossbar;
    }

    public Gui withBossbar(Boolean bossbar) {
        this.bossbar = bossbar;
        return this;
    }

    public Boolean getActionbar() {
        return actionbar;
    }

    public void setActionbar(Boolean actionbar) {
        this.actionbar = actionbar;
    }

    public Gui withActionbar(Boolean actionbar) {
        this.actionbar = actionbar;
        return this;
    }

    public String getBossbartext() {
        return bossbartext;
    }

    public void setBossbartext(String bossbartext) {
        this.bossbartext = bossbartext;
    }

    public Gui withBossbartext(String bossbartext) {
        this.bossbartext = bossbartext;
        return this;
    }

    public String getActionbartext() {
        return actionbartext;
    }

    public void setActionbartext(String actionbartext) {
        this.actionbartext = actionbartext;
    }

    public Gui withActionbartext(String actionbartext) {
        this.actionbartext = actionbartext;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bossbar", bossbar).append("actionbar", actionbar).append("bossbartext", bossbartext).append("actionbartext", actionbartext).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(bossbartext).append(actionbartext).append(actionbar).append(bossbar).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Gui)) {
            return false;
        }
        Gui rhs = ((Gui) other);
        return new EqualsBuilder().append(bossbartext, rhs.bossbartext).append(actionbartext, rhs.actionbartext).append(actionbar, rhs.actionbar).append(bossbar, rhs.bossbar).isEquals();
    }

}
