package dev.arkav.sophisticatedcompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.item.ItemStack;

@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeItem$Wrapper$1", remap = false)
abstract class JukeboxUpgradeItemWrapperItemStackHandlerMixin {
	@Inject(method = "isItemValid(ILnet/minecraft/world/item/ItemStack;)Z", at = @At("RETURN"), cancellable = true)
	public void isItemValid(int slot, ItemStack item, CallbackInfoReturnable<Boolean> cir) {
		// TODO: Actually validate the item :)
		cir.setReturnValue(true);
	}
}
