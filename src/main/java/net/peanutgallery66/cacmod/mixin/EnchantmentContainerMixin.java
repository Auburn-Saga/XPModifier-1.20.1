package net.peanutgallery66.cacmod.mixin;

import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.screen.slot.Slot;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentContainerMixin {

    @Inject(method = "onButtonClick", at = @At("HEAD"), cancellable = true)
    private void onButtonClick(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        EnchantmentScreenHandler handler = (EnchantmentScreenHandler)(Object) this;
        Slot lapisSlot = handler.getSlot(1);
        int requiredLapis;
        
        // Allow enchantment without lapis in creative mode
        boolean isCreativeMode = player.isCreative();
        
        switch (id) {
            case 1:
                requiredLapis = 32;
                break;
            case 2:
                requiredLapis = 48;
                break;
            default:
                requiredLapis = 16;
                break;
        }
        if (isCreativeMode || (lapisSlot.hasStack() && lapisSlot.getStack().getCount() >= requiredLapis)) {
            // Decrement lapis count if not in creative mode
            if (!isCreativeMode) {
                lapisSlot.getStack().decrement(requiredLapis);
            }
            // Retrieve the item stack from the enchantment slot
            ItemStack itemStack = handler.getSlot(0).getStack();
            if (!itemStack.isEmpty()) {
                int level = 1;
                EnchantmentHelper.enchant(player.getRandom(), itemStack, level, false);
                // Indicate success
                cir.setReturnValue(true);
            }
        } else {
            // Not enough lapis, return false to cancel the enchantment
            cir.setReturnValue(false);
        }
        
    }
}