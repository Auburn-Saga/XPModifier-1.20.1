package net.peanutgallery66.cacmod.mixin;

import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.client.gui.DrawContext;

@Mixin(EnchantmentScreen.class)
public interface EnchantmentScreenAccessor {
    @Invoker("drawBook")
    void invokeDrawBook(DrawContext context, int x, int y, float delta);
}