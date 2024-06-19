package net.peanutgallery66.cacmod.mixin;

import net.minecraft.entity.player.PlayerEntity;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

@Mixin (PlayerEntity.class)
public abstract class PlayerEntityMixin {
    
    @Inject(method = "getNextLevelExperience", at = @At("HEAD"), cancellable = true)
    private void modifyXpPerLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(1000);
    }
}
