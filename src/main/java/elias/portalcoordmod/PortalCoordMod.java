package elias.portalcoordmod;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PortalCoordMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerCommands();
    }

    static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(ClientCommandManager.literal("netherportal")
                    .then(ClientCommandManager.literal("help")
                            .executes(context -> {
                                sendHelpMessage(context.getSource());
                                return 1;
                            }))
                    .then(ClientCommandManager.literal("overworld")
                            .then(ClientCommandManager.argument("x", IntegerArgumentType.integer(0))
                                    .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                                            .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                                                    .executes(context -> executeNetherPortalCommand(
                                                            context.getSource(),
                                                            IntegerArgumentType.getInteger(context, "x"),
                                                            IntegerArgumentType.getInteger(context, "y"),
                                                            IntegerArgumentType.getInteger(context, "z"),
                                                            true
                                                    ))
                                            )
                                    )
                                    .executes(context -> executeNetherPortalCommand(
                                            context.getSource(),
                                            (int) context.getSource().getPosition().getX(),
                                            (int) context.getSource().getPosition().getY(),
                                            (int) context.getSource().getPosition().getZ(),
                                            true
                                    ))
                            )
                    )
                    .then(ClientCommandManager.argument("x", IntegerArgumentType.integer(0))
                            .then(ClientCommandManager.argument("y", IntegerArgumentType.integer())
                                    .then(ClientCommandManager.argument("z", IntegerArgumentType.integer())
                                            .executes(context -> executeNetherPortalCommand(
                                                    context.getSource(),
                                                    IntegerArgumentType.getInteger(context, "x"),
                                                    IntegerArgumentType.getInteger(context, "y"),
                                                    IntegerArgumentType.getInteger(context, "z"),
                                                    false
                                            ))
                                    )
                            )
                    )
                    .executes(context -> executeNetherPortalCommand(
                            context.getSource(),
                            (int) context.getSource().getPosition().getX(),
                            (int) context.getSource().getPosition().getY(),
                            (int) context.getSource().getPosition().getZ(),
                            false
                    ))
            );
        });
    }

    private static void sendHelpMessage(FabricClientCommandSource source) {
        // Create a help message
        Text helpMessage = Text.literal("")
                .append(Text.literal("Nether Portal Coords v0.0.1\n").formatted(Formatting.BOLD, Formatting.RED))
                .append(Text.literal("/netherportal ").formatted(Formatting.GOLD))
                .append(Text.literal("Calculates Nether coordinates using player position.\n").formatted(Formatting.AQUA))
                .append(Text.literal("-------------------------\n").formatted(Formatting.GRAY))
                .append(Text.literal("/netherportal ").formatted(Formatting.GOLD))
                .append(Text.literal("[").formatted(Formatting.BLUE))
                .append(Text.literal("x").formatted(Formatting.BLUE))
                .append(Text.literal("] ").formatted(Formatting.BLUE))
                .append(Text.literal("[").formatted(Formatting.BLUE))
                .append(Text.literal("y").formatted(Formatting.BLUE))
                .append(Text.literal("] ").formatted(Formatting.BLUE))
                .append(Text.literal("[").formatted(Formatting.BLUE))
                .append(Text.literal("z").formatted(Formatting.BLUE))
                .append(Text.literal("] ").formatted(Formatting.BLUE))
                .append(Text.literal("Calculates Nether coordinates using custom coordinates.\n").formatted(Formatting.AQUA))
                .append(Text.literal("-------------------------\n").formatted(Formatting.GRAY))
                .append(Text.literal("/netherportal ").formatted(Formatting.GOLD))
                .append(Text.literal("[").formatted(Formatting.GREEN))
                .append(Text.literal("overworld").formatted(Formatting.GREEN))
                .append(Text.literal("] ").formatted(Formatting.GREEN))
                .append(Text.literal("[").formatted(Formatting.BLUE))
                .append(Text.literal("x").formatted(Formatting.BLUE))
                .append(Text.literal("] ").formatted(Formatting.BLUE))
                .append(Text.literal("[").formatted(Formatting.BLUE))
                .append(Text.literal("y").formatted(Formatting.BLUE))
                .append(Text.literal("] ").formatted(Formatting.BLUE))
                .append(Text.literal("[").formatted(Formatting.BLUE))
                .append(Text.literal("z").formatted(Formatting.BLUE))
                .append(Text.literal("] ").formatted(Formatting.BLUE))
                .append(Text.literal("Calculates Overworld coordinates using custom coordinates.\n").formatted(Formatting.AQUA))
                .append(Text.literal("-------------------------\n").formatted(Formatting.GRAY))
                .append(Text.literal("/netherportal help ").formatted(Formatting.GOLD))
                .append(Text.literal("Shows this help message.\n").formatted(Formatting.AQUA));

        source.sendFeedback(helpMessage);
    }

    private static int executeNetherPortalCommand(FabricClientCommandSource source, int x, int y, int z, boolean toOverworld) {
        if (toOverworld) {
            // Calculate Overworld coordinates
            int overworldX = x * 8;
            int overworldY = y;
            int overworldZ = z * 8;

            // Create a Text object with the Overworld coordinates
            Text message = Text.literal("Overworld Coordinates: ")
                    .append(Text.literal(String.format("%d, %d, %d", overworldX, overworldY, overworldZ)).formatted(Formatting.GREEN));

            // Send the message to the player
            source.sendFeedback(message);
        } else {
            // Calculate Nether coordinates
            int netherX = x / 8;
            int netherY = y;
            int netherZ = z / 8;

            // Create a Text object with the Nether coordinates
            Text message = Text.literal("Nether Portal Coordinates: ")
                    .append(Text.literal(String.format("%d, %d, %d", netherX, netherY, netherZ)).formatted(Formatting.GREEN));

            // Send the message to the player
            source.sendFeedback(message);
        }

        return 1;
    }
}
