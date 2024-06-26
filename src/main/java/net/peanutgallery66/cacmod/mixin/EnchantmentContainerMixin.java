package net.peanutgallery66.cacmod.mixin;

import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.screen.slot.Slot;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

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
            case 0:
                requiredLapis = 16;
                break;
            case 1:
                requiredLapis = 32;
                break;
            default:
                requiredLapis = 48;
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
                // Calculate the enchantment level based on surrounding bookshelves
                World world = player.getWorld();
                BlockPos pos = player.getBlockPos();
                int bookshelfCount = 0;
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dy = -2; dy <= 2; dy++) {
                        for (int dz = -2; dz <= 2; dz++) {
                            if (Math.abs(dx) == 2 || Math.abs(dz) == 2 || (Math.abs(dx) == 1 && Math.abs(dz) == 1)) {
                                if (world.getBlockState(pos.add(dx, dy, dz)).getBlock() == net.minecraft.block.Blocks.BOOKSHELF) {
                                    bookshelfCount++;
                                }
                            }
                        }
                    }
                }
                int maxBookshelves = 15; // Maximum number of effective bookshelves
                int effectiveBookshelves = Math.min(bookshelfCount, maxBookshelves);
                int enchantmentLevel = id; // 0, 1, or 2 for the three slots

                // Calculate the actual level based on the bookshelves and button clicked
                int level = EnchantmentHelper.calculateRequiredExperienceLevel(player.getRandom(), enchantmentLevel, effectiveBookshelves, itemStack);

                // Get the list of possible enchantments
                List<EnchantmentLevelEntry> enchantments = EnchantmentHelper.getPossibleEntries(level, itemStack, true);
                if (!enchantments.isEmpty()) {
                    // Apply the chosen enchantment
                    EnchantmentHelper.enchant(player.getRandom(), itemStack, level, true);
                    // Indicate success
                    cir.setReturnValue(true);
                } else {
                    // No valid enchantments found
                    cir.setReturnValue(false);
                }
            } else {
                // No item in the enchantment slot
                cir.setReturnValue(false);
            }
        } else {
            // Not enough lapis, return false to cancel the enchantment
            cir.setReturnValue(false);
        }
    }
}