package me.jacktym.aiomacro.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.jacktym.aiomacro.config.AIOMVigilanceConfig;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin {
    //Cancel Outgoing
    @Inject(at = @At(value = "HEAD"), method = "sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void cancelOutgoing(Packet<?> packet, CallbackInfo info) {
        if (AIOMVigilanceConfig.cancelPackets) {
            if (packet instanceof C0APacketAnimation && AIOMVigilanceConfig.C0APacketAnimation && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C0BPacketEntityAction && AIOMVigilanceConfig.C0BPacketEntityAction && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C0CPacketInput && AIOMVigilanceConfig.C0CPacketInput && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C0DPacketCloseWindow && AIOMVigilanceConfig.C0DPacketCloseWindow && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C0EPacketClickWindow && AIOMVigilanceConfig.C0EPacketClickWindow && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C0FPacketConfirmTransaction && AIOMVigilanceConfig.C0FPacketConfirmTransaction && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C00PacketKeepAlive && AIOMVigilanceConfig.C00PacketKeepAlive && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C01PacketChatMessage && AIOMVigilanceConfig.C01PacketChatMessage && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C02PacketUseEntity && AIOMVigilanceConfig.C02PacketUseEntity && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C03PacketPlayer && AIOMVigilanceConfig.C03PacketPlayer && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C07PacketPlayerDigging && AIOMVigilanceConfig.C07PacketPlayerDigging && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C08PacketPlayerBlockPlacement && AIOMVigilanceConfig.C08PacketPlayerBlockPlacement && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C09PacketHeldItemChange && AIOMVigilanceConfig.C09PacketHeldItemChange && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C10PacketCreativeInventoryAction && AIOMVigilanceConfig.C10PacketCreativeInventoryAction && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C11PacketEnchantItem && AIOMVigilanceConfig.C11PacketEnchantItem && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C12PacketUpdateSign && AIOMVigilanceConfig.C12PacketUpdateSign && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C13PacketPlayerAbilities && AIOMVigilanceConfig.C13PacketPlayerAbilities && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C14PacketTabComplete && AIOMVigilanceConfig.C14PacketTabComplete && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C15PacketClientSettings && AIOMVigilanceConfig.C15PacketClientSettings && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C16PacketClientStatus && AIOMVigilanceConfig.C16PacketClientStatus && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C17PacketCustomPayload && AIOMVigilanceConfig.C17PacketCustomPayload && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C18PacketSpectate && AIOMVigilanceConfig.C18PacketSpectate && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof C19PacketResourcePackStatus && AIOMVigilanceConfig.C19PacketResourcePackStatus && info.isCancellable()) {
                info.cancel();
            }
        }
    }

    //Cancel Incoming
    @Inject(at = @At(value = "HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V")
    private void cancelIncoming(ChannelHandlerContext channel, Packet packet, CallbackInfo info) {
        if (AIOMVigilanceConfig.cancelPackets) {
            if (packet instanceof S0APacketUseBed && AIOMVigilanceConfig.S0APacketUseBed && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S0BPacketAnimation && AIOMVigilanceConfig.S0BPacketAnimation && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S0CPacketSpawnPlayer && AIOMVigilanceConfig.S0CPacketSpawnPlayer && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S0DPacketCollectItem && AIOMVigilanceConfig.S0DPacketCollectItem && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S0EPacketSpawnObject && AIOMVigilanceConfig.S0EPacketSpawnObject && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S0FPacketSpawnMob && AIOMVigilanceConfig.S0FPacketSpawnMob && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S00PacketKeepAlive && AIOMVigilanceConfig.S00PacketKeepAlive && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S1BPacketEntityAttach && AIOMVigilanceConfig.S1BPacketEntityAttach && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S1CPacketEntityMetadata && AIOMVigilanceConfig.S1CPacketEntityMetadata && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S1DPacketEntityEffect && AIOMVigilanceConfig.S1DPacketEntityEffect && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S1EPacketRemoveEntityEffect && AIOMVigilanceConfig.S1EPacketRemoveEntityEffect && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S1FPacketSetExperience && AIOMVigilanceConfig.S1FPacketSetExperience && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S01PacketJoinGame && AIOMVigilanceConfig.S01PacketJoinGame && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S2APacketParticles && AIOMVigilanceConfig.S2APacketParticles && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S2BPacketChangeGameState && AIOMVigilanceConfig.S2BPacketChangeGameState && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S2CPacketSpawnGlobalEntity && AIOMVigilanceConfig.S2CPacketSpawnGlobalEntity && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S2DPacketOpenWindow && AIOMVigilanceConfig.S2DPacketOpenWindow && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S2EPacketCloseWindow && AIOMVigilanceConfig.S2EPacketCloseWindow && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S2FPacketSetSlot && AIOMVigilanceConfig.S2FPacketSetSlot && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S02PacketChat && AIOMVigilanceConfig.S02PacketChat && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S3APacketTabComplete && AIOMVigilanceConfig.S3APacketTabComplete && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S3BPacketScoreboardObjective && AIOMVigilanceConfig.S3BPacketScoreboardObjective && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S3CPacketUpdateScore && AIOMVigilanceConfig.S3CPacketUpdateScore && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S3DPacketDisplayScoreboard && AIOMVigilanceConfig.S3DPacketDisplayScoreboard && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S3EPacketTeams && AIOMVigilanceConfig.S3EPacketTeams && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S3FPacketCustomPayload && AIOMVigilanceConfig.S3FPacketCustomPayload && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S03PacketTimeUpdate && AIOMVigilanceConfig.S03PacketTimeUpdate && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S04PacketEntityEquipment && AIOMVigilanceConfig.S04PacketEntityEquipment && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S05PacketSpawnPosition && AIOMVigilanceConfig.S05PacketSpawnPosition && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S06PacketUpdateHealth && AIOMVigilanceConfig.S06PacketUpdateHealth && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S07PacketRespawn && AIOMVigilanceConfig.S07PacketRespawn && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S08PacketPlayerPosLook && AIOMVigilanceConfig.S08PacketPlayerPosLook && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S09PacketHeldItemChange && AIOMVigilanceConfig.S09PacketHeldItemChange && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S10PacketSpawnPainting && AIOMVigilanceConfig.S10PacketSpawnPainting && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S11PacketSpawnExperienceOrb && AIOMVigilanceConfig.S11PacketSpawnExperienceOrb && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S12PacketEntityVelocity && AIOMVigilanceConfig.S12PacketEntityVelocity && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S13PacketDestroyEntities && AIOMVigilanceConfig.S13PacketDestroyEntities && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S14PacketEntity && AIOMVigilanceConfig.S14PacketEntity && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S18PacketEntityTeleport && AIOMVigilanceConfig.S18PacketEntityTeleport && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S19PacketEntityHeadLook && AIOMVigilanceConfig.S19PacketEntityHeadLook && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S19PacketEntityStatus && AIOMVigilanceConfig.S19PacketEntityStatus && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S20PacketEntityProperties && AIOMVigilanceConfig.S20PacketEntityProperties && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S21PacketChunkData && AIOMVigilanceConfig.S21PacketChunkData && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S22PacketMultiBlockChange && AIOMVigilanceConfig.S22PacketMultiBlockChange && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S23PacketBlockChange && AIOMVigilanceConfig.S23PacketBlockChange && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S24PacketBlockAction && AIOMVigilanceConfig.S24PacketBlockAction && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S25PacketBlockBreakAnim && AIOMVigilanceConfig.S25PacketBlockBreakAnim && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S26PacketMapChunkBulk && AIOMVigilanceConfig.S26PacketMapChunkBulk && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S27PacketExplosion && AIOMVigilanceConfig.S27PacketExplosion && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S28PacketEffect && AIOMVigilanceConfig.S28PacketEffect && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S29PacketSoundEffect && AIOMVigilanceConfig.S29PacketSoundEffect && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S30PacketWindowItems && AIOMVigilanceConfig.S30PacketWindowItems && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S31PacketWindowProperty && AIOMVigilanceConfig.S31PacketWindowProperty && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S32PacketConfirmTransaction && AIOMVigilanceConfig.S32PacketConfirmTransaction && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S33PacketUpdateSign && AIOMVigilanceConfig.S33PacketUpdateSign && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S34PacketMaps && AIOMVigilanceConfig.S34PacketMaps && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S35PacketUpdateTileEntity && AIOMVigilanceConfig.S35PacketUpdateTileEntity && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S36PacketSignEditorOpen && AIOMVigilanceConfig.S36PacketSignEditorOpen && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S37PacketStatistics && AIOMVigilanceConfig.S37PacketStatistics && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S38PacketPlayerListItem && AIOMVigilanceConfig.S38PacketPlayerListItem && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S39PacketPlayerAbilities && AIOMVigilanceConfig.S39PacketPlayerAbilities && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S40PacketDisconnect && AIOMVigilanceConfig.S40PacketDisconnect && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S41PacketServerDifficulty && AIOMVigilanceConfig.S41PacketServerDifficulty && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S42PacketCombatEvent && AIOMVigilanceConfig.S42PacketCombatEvent && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S43PacketCamera && AIOMVigilanceConfig.S43PacketCamera && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S44PacketWorldBorder && AIOMVigilanceConfig.S44PacketWorldBorder && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S45PacketTitle && AIOMVigilanceConfig.S45PacketTitle && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S46PacketSetCompressionLevel && AIOMVigilanceConfig.S46PacketSetCompressionLevel && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S47PacketPlayerListHeaderFooter && AIOMVigilanceConfig.S47PacketPlayerListHeaderFooter && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S48PacketResourcePackSend && AIOMVigilanceConfig.S48PacketResourcePackSend && info.isCancellable()) {
                info.cancel();
            }
            if (packet instanceof S49PacketUpdateEntityNBT && AIOMVigilanceConfig.S49PacketUpdateEntityNBT && info.isCancellable()) {
                info.cancel();
            }
        }
    }

    //Edit Outgoing
    @ModifyVariable(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At(value = "HEAD"), argsOnly = true)
    private Packet<?> editOutgoing(Packet<?> packet) {
        return packet;
    }

    //Edit Incoming
    @ModifyVariable(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At(value = "HEAD"), argsOnly = true)
    private Packet<?> editIncoming(Packet<?> packet) {
        /*if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packetIn = (S12PacketEntityVelocity) packet;
            if (AntiKB.entityVelocity(packetIn)) {
                S12PacketEntityVelocity newPacket;

                if (!Main.mcPlayer.getHeldItem().getDisplayName().contains("Jerry-chine Gun")) {
                    newPacket = new S12PacketEntityVelocity(packetIn.getEntityID(), 0, 0, 0);
                } else {
                    newPacket = new S12PacketEntityVelocity(packetIn.getEntityID(), 0, packetIn.getMotionY()/8000.0, 0);
                }
                return newPacket;
            }
        }*/
        return packet;
    }
}