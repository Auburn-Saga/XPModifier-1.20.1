package net.peanutgallery66.cacmod.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandledScreenAccessor {
    @Accessor("backgroundWidth")
    int getBackgroundWidth();
    
    @Accessor("backgroundWidth")
    void setBackgroundWidth(int backgroundWidth);
    
    @Accessor("backgroundHeight")
    int getBackgroundHeight();
    
    @Accessor("backgroundHeight")
    void setBackgroundHeight(int backgroundHeight);
}