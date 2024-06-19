package net.peanutgallery66.cacmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CacMod implements ModInitializer {
    private final Map<ServerPlayerEntity, String> displayNames = new HashMap<>();
    private final Map<ServerPlayerEntity, String> pronouns = new HashMap<>();
    public static final Logger LOGGER = LoggerFactory.getLogger("CogsandConstructsMod");

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> {
            dispatcher.register(CommandManager.literal("setdisplayname")
                .then(CommandManager.argument("name", StringArgumentType.greedyString())
                    .executes(context -> setDisplayName(context))));
            dispatcher.register(CommandManager.literal("setpronouns")
                .then(CommandManager.argument("pronouns", StringArgumentType.greedyString())
                    .executes(context -> setPronouns(context))));
        });
        LOGGER.info("Cogs and Constructs Mod initialized.");
    }

    private int setDisplayName(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            String name = StringArgumentType.getString(context, "name");
            displayNames.put(player, name);
            updatePlayerDisplay(player);
            player.sendMessage(Text.literal("Display name set to " + name).formatted(Formatting.GREEN), false);
        }
        return 1;
    }

    private int setPronouns(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player != null) {
            String pronoun = StringArgumentType.getString(context, "pronouns");
            pronouns.put(player, pronoun);
            updatePlayerDisplay(player);
            player.sendMessage(Text.literal("Pronouns set to " + pronoun).formatted(Formatting.GREEN), false);
        }
        return 1;
    }

    private void updatePlayerDisplay(ServerPlayerEntity player) {
        String displayName = displayNames.getOrDefault(player, player.getName().getString());
        String playerPronouns = pronouns.getOrDefault(player, "");
        player.setCustomName(Text.literal(displayName + " (" + playerPronouns + ")"));
        player.setCustomNameVisible(true);
    }
}