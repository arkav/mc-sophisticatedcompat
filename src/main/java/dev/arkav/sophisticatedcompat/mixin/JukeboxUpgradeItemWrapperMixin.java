package dev.arkav.sophisticatedcompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.arkav.sophisticatedcompat.SophisticatedCompat;
import gg.moonflower.etched.common.network.EtchedMessages;
import gg.moonflower.etched.common.network.play.ClientboundPlayEntityMusicPacket;
import gg.moonflower.etched.core.registry.EtchedItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeItem;

@Mixin(JukeboxUpgradeItem.Wrapper.class)
abstract class JukeboxUpgradeItemWrapperMixin {
	@Shadow(remap = false)
	public abstract ItemStack getDisc();

	private boolean isEtchedDisk() {
		var item = getDisc().copy();
		return item.is(EtchedItems.ALBUM_COVER.get()) || item.is(EtchedItems.ETCHED_MUSIC_DISC.get());
	}

	// @Inject(at = @At("HEAD"), method =
	// "play(Lnet/minecraft/world/Level;Lnet/minecraft/core/BlockPos;)Z", remap =
	// false, cancellable = true)
	// public void play(Level world, BlockPos pos, CallbackInfo ci) {
	// if (!(world instanceof ServerLevel) || this.getDisc().isEmpty()) {
	// return;
	// }
	//
	// if (this.isEtchedDisk()) {
	// EtchedMessages.PLAY.send(
	// PacketDistributor.NEAR
	// .with(() -> new PacketDistributor.TargetPoint(pos.getX() + 0.5,
	// pos.getY() + 0.5, pos.getZ() + 0.5, 64,
	// world.dimension())),
	// new ClientboundPlayMusicPacket(this.getDisc().copy(), pos));
	// ci.cancel();
	// }
	// }

	@Inject(at = @At("HEAD"), method = "play(Lnet/minecraft/world/entity/LivingEntity;)V", remap = false, cancellable = true)
	public void play(LivingEntity entity, CallbackInfo ci) {
		if (!(entity.level() instanceof ServerLevel) || this.getDisc().isEmpty()) {
			SophisticatedCompat.LOGGER.debug("Play callled for entity on client");
			return;
		}

		if (this.isEtchedDisk()) {
			SophisticatedCompat.LOGGER.debug("Sending play to nearby clients");
			var pos = entity.blockPosition();
			EtchedMessages.PLAY.send(
					PacketDistributor.NEAR
							.with(() -> new PacketDistributor.TargetPoint(pos.getX() + 0.5,
									pos.getY() + 0.5, pos.getZ() + 0.5, 64,
									entity.level().dimension())),
					new ClientboundPlayEntityMusicPacket(this.getDisc().copy(), entity, false));
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "stop(Lnet/minecraft/world/entity/LivingEntity;)V", remap = false, cancellable = true)
	public void stop(LivingEntity entity, CallbackInfo ci) {
		if (!(entity.level() instanceof ServerLevel) || this.getDisc().isEmpty()) {
			SophisticatedCompat.LOGGER.debug("Stop callled for entity on client");
			return;
		}

		SophisticatedCompat.LOGGER.debug("Sending stop to nearby clients");
		var pos = entity.blockPosition();
		EtchedMessages.PLAY.send(
				PacketDistributor.NEAR
						.with(() -> new PacketDistributor.TargetPoint(pos.getX() + 0.5,
								pos.getY() + 0.5, pos.getZ() + 0.5, 64,
								entity.level().dimension())),
				new ClientboundPlayEntityMusicPacket(entity));
	}
}
