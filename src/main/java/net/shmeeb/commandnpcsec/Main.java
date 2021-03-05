package net.shmeeb.commandnpcsec;

import com.google.common.reflect.TypeToken;
import net.shmeeb.commandnpcsec.data.Settings;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.*;

@Plugin(id = "commandnpcsec", name = "CommandNPCSEC", version = "1.0")
public class Main {
    private static Main instance;
    public static List<UUID> removing = new ArrayList<>();
    public static Map<UUID, String> setting = new HashMap<>();
    public static List<UUID> list = new ArrayList<>();
    public static Key<Value<String>> CMD;

    @Listener
    public void init(GameInitializationEvent e) {
        instance = this;
        registerCommands();

        DataRegistration.builder()
                .dataClass(Settings.class)
                .immutableClass(Settings.Immutable.class)
                .builder(new Settings.Builder())
                .manipulatorId("cmd")
                .dataName("CommandNPC Data")
                .buildAndRegister(Sponge.getPluginManager().getPlugin("commandnpcsec").get());
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent e) {
        CMD = Key.builder()
                .type(new TypeToken<Value<String>>() {
                })
                .query(DataQuery.of("Cmd"))
                .id("commandnpcsec:cmd")
                .name("Cmd")
                .build();
    }

    @Listener
    public void entityInteract(InteractEntityEvent.Secondary.MainHand ev, @First Player player) {
        if (setting.containsKey(player.getUniqueId())) {
            ev.getTargetEntity().offer(new Settings(setting.get(player.getUniqueId())));
            setting.remove(player.getUniqueId());
            sendMessage(player, "&aSuccessfully set command");
            return;
        }

        if (removing.contains(player.getUniqueId())) {
            ev.getTargetEntity().remove(Settings.class);
            removing.remove(player.getUniqueId());
            sendMessage(player, "&aSuccessfully removed all commands");
            return;
        }

        if (list.contains(player.getUniqueId())) {
            if (ev.getTargetEntity().get(Settings.class).isPresent()) {
                String cmd = ev.getTargetEntity().get(Settings.class).get().getCmd();
                List<String> commands = new ArrayList<>();

                if (cmd.contains("#")) {
                    commands = Arrays.asList(cmd.split("#"));
                } else {
                    commands.add(cmd);
                }

                sendMessage(player, "&a&lAttached commands:");
                for (int i = 0; i < commands.size(); i++) {
                    sendMessage(player, "&7#" + (i + 1) + " &a" + commands.get(i));
                }
            } else {
                sendMessage(player, "&cThis entity doesn't have any command attached!");
            }
            list.remove(player.getUniqueId());
            return;
        }


        if (ev.getTargetEntity().get(Settings.class).isPresent()) {
            ev.setCancelled(true);
            String cmd = ev.getTargetEntity().get(Settings.class).get().getCmd().replaceAll("@p", player.getName());
            List<String> commands = new ArrayList<>();

            if (cmd.contains("#")) {
                commands = Arrays.asList(cmd.split("#"));
            } else {
                commands.add(cmd);
            }

            for (String command : commands) {
                if (command.startsWith("c:")) {
                    Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command.replaceAll("c:", ""));
                } else {
                    Sponge.getCommandManager().process(player, command);
                }
            }
        }
    }

    @Listener
    public void onRegister(GameRegistryEvent.Register<Key<?>> e) {
        e.register(CMD);
    }

    private void registerCommands() {
        CommandSpec setcommand = CommandSpec.builder().description(Text.of("Sets a command to a npc"))
                .permission("commandnpcsec.set")
                .arguments(GenericArguments.remainingJoinedStrings(Text.of("command")))
                .executor((commandSource, commandContext) -> {
                    if (!(commandSource instanceof Player)) return CommandResult.empty();
                    Player player = (Player) commandSource;

                    setting.put(player.getUniqueId(), commandContext.<String>getOne("command").get());
                    sendMessage(player, "&aClick an entity you want to make a command NPC");

                    return CommandResult.success();
                }).build();

        CommandSpec removecommand = CommandSpec.builder().description(Text.of("Removes all commands from a npc"))
                .permission("commandnpcsec.remove")
                .executor((commandSource, commandContext) -> {
                    if (!(commandSource instanceof Player)) return CommandResult.empty();
                    Player player = (Player) commandSource;

                    removing.add(player.getUniqueId());
                    sendMessage(player, "&aClick an entity to remove all commands from");

                    return CommandResult.success();
                }).build();

        CommandSpec listcommand = CommandSpec.builder().description(Text.of("List of all commands of a npc"))
                .permission("commandnpcsec.list")
                .executor((commandSource, commandContext) -> {
                    if (!(commandSource instanceof Player)) return CommandResult.empty();
                    Player player = (Player) commandSource;

                    list.add(player.getUniqueId());
                    sendMessage(player, "&aClick an entity to see all commands it has attached");

                    return CommandResult.success();
                }).build();

        Sponge.getCommandManager().register(getInstance(), setcommand, "setcommandnpc");
        Sponge.getCommandManager().register(getInstance(), removecommand, "removecommandnpc");
        Sponge.getCommandManager().register(getInstance(), listcommand, "listcommandnpc");
    }

    public static String color(String string) {
        return TextSerializers.FORMATTING_CODE.serialize(Text.of(string));
    }

    public static void sendMessage(CommandSource sender, String message) {
        if (sender == null) {
            return;
        }
        sender.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(color(message)));
    }

    public static Text getText(String message) {
        return TextSerializers.FORMATTING_CODE.deserialize(color(message));
    }

    public static Main getInstance() {
        return instance;
    }
}