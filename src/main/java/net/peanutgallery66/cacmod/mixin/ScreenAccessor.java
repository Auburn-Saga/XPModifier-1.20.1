package net.peanutgallery66.cacmod.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("textRenderer")
    TextRenderer getTextRenderer();
}