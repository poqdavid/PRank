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

package io.github.poqdavid.prank.Listeners;

import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.api.events.EggHatchEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.TurnEndEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.github.poqdavid.prank.PRank;
import io.github.poqdavid.prank.Utils.Data.PlayerData;
import io.github.poqdavid.prank.Utils.Tools;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PixelmonEventListener {

    @SubscribeEvent
    public void PixelmonEggHatch(EggHatchEvent event) {
        EntityPlayerMP epm = event.getPokemon().getOwnerPlayer();
        Pokemon ep = event.getPokemon();
        final String st_uuid1 = "" + epm.getUniqueID();
        PlayerData pd1 = PRank.getInstance().PlayerData.get(st_uuid1);

        if (PRank.getInstance().getSettings().getDimension().getEnable()) {
            if (PRank.getInstance().getSettings().getDimension().getDimension().contains(epm.dimension)) {
                PixelmonEggHatchTask(st_uuid1, epm, ep, pd1);
            }
        } else {
            PixelmonEggHatchTask(st_uuid1, epm, ep, pd1);
        }
    }

    private void PixelmonEggHatchTask(String st_uuid1, EntityPlayerMP epm, Pokemon ep, PlayerData pd1) {
        Tools.UpdateScore("hatch", st_uuid1, pd1, ep);

        if (PRank.getInstance().getSettings().getGui().getBossbar()) {
            Tools.UpdateEXPBar(epm, pd1);
        }

        if (PRank.getInstance().getSettings().getGui().getActionbar()) {
            Tools.UpdateActionBar(Tools.GetSpongePlayer(epm), pd1);
        }
    }

    @SubscribeEvent
    public void PixelmonCapture(CaptureEvent.SuccessfulCapture event) {
        EntityPlayerMP epm = event.player;
        EntityPixelmon ep = event.getPokemon();
        final String st_uuid1 = "" + epm.getUniqueID();
        PlayerData pd1 = PRank.getInstance().PlayerData.get(st_uuid1);

        if (PRank.getInstance().getSettings().getDimension().getEnable()) {
            if (PRank.getInstance().getSettings().getDimension().getDimension().contains(epm.dimension)) {
                PixelmonCaptureTask(st_uuid1, epm, ep, pd1);
            }
        } else {
            PixelmonCaptureTask(st_uuid1, epm, ep, pd1);
        }
    }

    private void PixelmonCaptureTask(String st_uuid1, EntityPlayerMP epm, EntityPixelmon ep, PlayerData pd1) {
        Tools.UpdateScore("capture", st_uuid1, pd1, ep.getPokemonData());

        if (PRank.getInstance().getSettings().getGui().getBossbar()) {
            Tools.UpdateEXPBar(epm, pd1);
        }

        if (PRank.getInstance().getSettings().getGui().getActionbar()) {
            Tools.UpdateActionBar(Tools.GetSpongePlayer(epm), pd1);
        }
    }

    @SubscribeEvent
    public void PixelmonBattleEnd(BattleEndEvent event) {
        BattleParticipant bp1 = event.bc.participants.get(0);
        BattleParticipant bp2 = event.bc.participants.get(1);

        EntityPlayerMP P1Entity = null;
        EntityPlayerMP P2Entity = null;

        int dim = 0;

        if (bp1.getEntity() instanceof EntityPlayerMP) {
            P1Entity = (EntityPlayerMP) bp1.getEntity();
            dim = P1Entity.dimension;
        }

        if (bp2.getEntity() instanceof EntityPlayerMP) {
            P2Entity = (EntityPlayerMP) bp2.getEntity();
            dim = P2Entity.dimension;
        }

        if (PRank.getInstance().getSettings().getDimension().getEnable()) {
            if (PRank.getInstance().getSettings().getDimension().getDimension().contains(dim)) {
                PixelmonBattleEndTask(bp1, bp2, P1Entity, P2Entity);
            }
        } else {
            PixelmonBattleEndTask(bp1, bp2, P1Entity, P2Entity);
        }

    }

    private void PixelmonBattleEndTask(BattleParticipant bp1, BattleParticipant bp2, EntityLivingBase P1Entity, EntityLivingBase P2Entity) {


        if (bp1 instanceof PlayerParticipant && P1Entity != null) {
            final String key1 = "" + P1Entity.getUniqueID();
            PlayerData pd1 = PRank.getInstance().PlayerData.get(key1);

            if (PRank.getInstance().getSettings().getGui().getBossbar()) {
                Tools.UpdateEXPBar(P1Entity, pd1);
            }

            if (PRank.getInstance().getSettings().getGui().getActionbar()) {
                Tools.UpdateActionBar(Tools.GetSpongePlayer(P1Entity), pd1);
            }
        }

        if (bp2 instanceof PlayerParticipant && P2Entity != null) {
            final String key2 = "" + P2Entity.getUniqueID();
            PlayerData pd2 = PRank.getInstance().PlayerData.get(key2);

            if (PRank.getInstance().getSettings().getGui().getBossbar()) {
                Tools.UpdateEXPBar(P2Entity, pd2);
            }

            if (PRank.getInstance().getSettings().getGui().getActionbar()) {
                Tools.UpdateActionBar(Tools.GetSpongePlayer(P2Entity), pd2);
            }
        }
    }

/*    @SubscribeEvent
    public void DamageEvent(AttackEvents.DamageEvent event) {
        if (event.willBeFatal()) {
            try {
                BattleParticipant bp1 = event.user.getParticipant();
                PixelmonWrapper pw1 = event.target;
                if (bp1 instanceof PlayerParticipant) {
                    EntityLivingBase P1Entity = bp1.getEntity();
                    final String st_uuid1 = "" + P1Entity.getUniqueID();
                    PlayerData pd1 = PRank.getInstance().PlayerData.get(st_uuid1);
                    Tools.UpdateScore("kill", st_uuid1, pd1, pw1.entity.getPokemonData());
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }*/

    @SubscribeEvent
    public void PixelmonTurnEnd(TurnEndEvent event) {
        BattleParticipant bp1 = event.bcb.participants.get(0);
        BattleParticipant bp2 = event.bcb.participants.get(1);

        EntityPlayerMP P1Entity = null;
        EntityPlayerMP P2Entity = null;

        int dim = 0;

        if (bp1.getEntity() instanceof EntityPlayerMP) {
            P1Entity = (EntityPlayerMP)bp1.getEntity();
            dim = P1Entity.dimension;
        }

        if (bp2.getEntity() instanceof EntityPlayerMP) {
            P2Entity = (EntityPlayerMP) bp2.getEntity();
            dim = P2Entity.dimension;
        }

        if (PRank.getInstance().getSettings().getDimension().getEnable()) {
            if (PRank.getInstance().getSettings().getDimension().getDimension().contains(dim)) {
                PixelmonTurnEndTask(bp1, bp2, P1Entity, P2Entity);
            }
        } else {
            PixelmonTurnEndTask(bp1, bp2, P1Entity, P2Entity);
        }
    }

    private void PixelmonTurnEndTask(BattleParticipant bp1, BattleParticipant bp2, EntityLivingBase P1Entity, EntityLivingBase P2Entity) {

        try {
            PixelmonWrapper pw1 = null;

            if (bp1.getFaintedPokemon() != null) {
                pw1 = bp1.getFaintedPokemon();
                if (bp2 instanceof PlayerParticipant && P2Entity != null) {
                    final String st_uuid1 = "" + P2Entity.getUniqueID();
                    PlayerData pd1 = PRank.getInstance().PlayerData.get(st_uuid1);
                    Tools.UpdateScore("kill", st_uuid1, pd1, pw1.entity.getPokemonData());
                }
            }

        } catch (Exception ex) {
            PRank.getInstance().getLogger().info(ex.getMessage());
            ex.printStackTrace();
        }

        try {
            PixelmonWrapper pw2 = null;
            if (bp2.getFaintedPokemon() != null) {
                pw2 = bp2.getFaintedPokemon();
                if (bp1 instanceof PlayerParticipant && P1Entity != null) {
                    final String st_uuid2 = "" + P1Entity.getUniqueID();
                    PlayerData pd2 = PRank.getInstance().PlayerData.get(st_uuid2);
                    Tools.UpdateScore("kill", st_uuid2, pd2, pw2.entity.getPokemonData());
                }
            }


        } catch (Exception ex) {
            PRank.getInstance().getLogger().info(ex.getMessage());
            ex.printStackTrace();
        }
    }
}


