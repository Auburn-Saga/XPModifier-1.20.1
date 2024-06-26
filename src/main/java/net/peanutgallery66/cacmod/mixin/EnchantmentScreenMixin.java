package net.peanutgallery66.cacmod.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin {

    private static final Identifier ENCHANTITEM_TEXTURE = new Identifier("minecraft", "textures/item/diamond.png");

    @Inject(method = "init", at = @At("HEAD"))
    public void init(CallbackInfo ci) {
        EnchantmentScreen screen = (EnchantmentScreen) (Object) this;
        ((HandledScreenAccessor) screen).setBackgroundWidth(176);
        ((HandledScreenAccessor) screen).setBackgroundHeight(166);
    }

    @Inject(method = "drawBackground", at = @At("HEAD"), cancellable = true)
    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        EnchantmentScreen screen = (EnchantmentScreen) (Object) this;
        EnchantmentScreenHandler handler = screen.getScreenHandler();
        EnchantmentScreenAccessor enchantmentScreenAccessor = (EnchantmentScreenAccessor) screen;

        int backgroundWidth = ((HandledScreenAccessor) screen).getBackgroundWidth();
        int backgroundHeight = ((HandledScreenAccessor) screen).getBackgroundHeight();

        int i = (screen.width - backgroundWidth) / 2;
        int j = (screen.height - backgroundHeight) / 2;
        context.drawTexture(new Identifier("textures/gui/container/enchanting_table.png"), i, j, 0, 0, backgroundWidth, backgroundHeight);

        int lapisCount = handler.getLapisCount();
        int[] requiredLapis = {16, 32, 48};

        enchantmentScreenAccessor.invokeDrawBook(context, i, j, delta);

        for (int l = 0; l < 3; ++l) {
            int m = i + 60;
            int n = m + 20;
            int enchantmentPower = handler.enchantmentPower[l];
            if (enchantmentPower == 0) {
                context.drawTexture(new Identifier("textures/gui/container/enchanting_table.png"), m, j + 14 + 19 * l, 0, 185, 108, 19);
                continue;
            }
            int required = requiredLapis[l];
            boolean canEnchant = lapisCount >= required;
            int color = canEnchant ? 0xFFFFFF : 0x808080; // white if enough lapis, grey otherwise

            // Draw the glyphs
            context.drawTexture(new Identifier("textures/gui/container/enchanting_table.png"), m, j + 14 + 19 * l, 0, 166, 108, 19);
            context.drawTexture(new Identifier("textures/gui/container/enchanting_table.png"), m + 1, j + 15 + 19 * l, 16 * l, canEnchant ? 223 : 239, 16, 16);

            TextRenderer textRenderer = ((ScreenAccessor) screen).getTextRenderer();
            // Draw the enchantment level
            String enchantmentLevelText = String.valueOf(enchantmentPower);
            int textWidth = textRenderer.getWidth(enchantmentLevelText);
            context.drawTextWithShadow(textRenderer, enchantmentLevelText, n + 86 - textWidth, j + 16 + 19 * l + 7, color);

            // Draw the lapis cost
            String lapisCostText = String.valueOf(required);
            int lapisTextWidth = textRenderer.getWidth(lapisCostText);
            context.drawTextWithShadow(textRenderer, lapisCostText, m + 86 - 8 - lapisTextWidth, j + 16 + 19 * l + 7, color);

            // Draw the lapis icon
            context.drawTexture(ENCHANTITEM_TEXTURE, m + 86 - 8, j + 16 + 19 * l + 7, 0, 0, 8, 8);
        }

        ci.cancel();
    }
}