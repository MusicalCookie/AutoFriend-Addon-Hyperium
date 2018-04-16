package pw._2pi.autofriend;

import cc.hyperium.commands.BaseCommand;
import cc.hyperium.commands.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static net.minecraft.command.CommandBase.getListOfStringsMatchingLastWord;

public class FriendCommand implements BaseCommand {
    private Pattern username;

    public FriendCommand() {
        this.username = Pattern.compile("\\w{1,16}");
    }

    public void throwError(final String error) {
        AutoFriend.mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentText(EnumChatFormatting.RED + "Error: " + error));
    }

    public void sendMessage(final String message) {
        AutoFriend.mc.thePlayer.addChatMessage((IChatComponent) new ChatComponentText(message));
    }

    public String getName() {
        return "autofriend";
    }

    public String getUsage() {
        return "/autofriend <toggle, messages, recent, blacklist, info>";
    }

    public void onExecute(final String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equals("toggle")) {
                AutoFriend.toggle = !AutoFriend.toggle;
                this.sendMessage(EnumChatFormatting.BLUE + "AutoFriend: " + (AutoFriend.toggle ? (EnumChatFormatting.GREEN + "On") : (EnumChatFormatting.RED + "Off")));
            } else if (args[0].equals("messages")) {
                AutoFriend.messages = !AutoFriend.messages;
                BlacklistUtils.writeBlacklist();
                if (!AutoFriend.messages) {
                    AutoFriend.mc.thePlayer.addChatMessage(ChatUtils.of("NOTE: This will also remove divider messages which may affect other messages on Hypixel. Hover on this message to see what they look like.").setColor(EnumChatFormatting.RED).setBold(true).setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils.of("-----------------------------------------------------").setColor(EnumChatFormatting.WHITE).build()).build());
                }
                this.sendMessage(EnumChatFormatting.BLUE + "Friend Messages: " + (AutoFriend.messages ? (EnumChatFormatting.GREEN + "On") : (EnumChatFormatting.RED + "Off")));
            } else if (args[0].equals("recent")) {
                if (AutoFriend.recent.size() > 0) {
                    int page = 1;
                    final int pages = (int) Math.ceil(AutoFriend.recent.size() / 7.0);
                    if (args.length > 1) {
                        try {
                            page = Integer.parseInt(args[1]);
                        } catch (NumberFormatException ignored) {
                            page = -1;
                        }
                    }
                    if (page < 1 || page > pages) {
                        this.throwError("Invalid page number");
                    } else {
                        this.sendMessage(EnumChatFormatting.GRAY + "----------------------");
                        this.sendMessage(EnumChatFormatting.BLUE + "Friend History " + EnumChatFormatting.DARK_AQUA + "(Page " + page + " of " + pages + ")");
                        AutoFriend.recent.stream().skip((page - 1) * 7).limit(7L).forEach(name -> AutoFriend.mc.thePlayer.sendChatMessage(String.valueOf(ChatUtils.of(EnumChatFormatting.BLUE + name + EnumChatFormatting.GRAY + " - ").append("[REMOVE]").setColor(EnumChatFormatting.RED).setClickEvent(ClickEvent.Action.RUN_COMMAND, "/f remove " + name).setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils.of("Remove " + name).setColor(EnumChatFormatting.RED).build()).append(" - ").setColor(EnumChatFormatting.GRAY).append("[BLACKLIST]").setColor(EnumChatFormatting.DARK_GRAY).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/autofriend blacklist add " + name).setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils.of("Blacklist " + name).setColor(EnumChatFormatting.RED).build()).build())));
                        this.sendMessage(EnumChatFormatting.GRAY + "----------------------");
                    }
                } else {
                    this.throwError("You haven't friended anyone");
                }
            } else if (args[0].equals("blacklist")) {
                if (args.length == 1 || (!args[1].equals("add") && !args[1].equals("remove"))) {
                    if (AutoFriend.blacklist.size() > 0) {
                        int page = 1;
                        final int pages = (int) Math.ceil(AutoFriend.blacklist.size() / 7.0);
                        if (args.length > 1) {
                            try {
                                page = Integer.parseInt(args[1]);
                            } catch (NumberFormatException ignored) {
                                page = -1;
                            }
                        }
                        if (page < 1 || page > pages) {
                            this.throwError("Invalid page number");
                        } else {
                            this.sendMessage(EnumChatFormatting.GRAY + "----------------------");
                            this.sendMessage(EnumChatFormatting.BLUE + "Blacklist " + EnumChatFormatting.DARK_AQUA + "(Page " + page + " of " + pages + ")");
                            AutoFriend.blacklist.stream().skip((page - 1) * 7).limit(7L).forEach(name -> this.sendMessage(String.valueOf(ChatUtils.of(EnumChatFormatting.BLUE + name + EnumChatFormatting.GRAY + " - ").append("[REMOVE]").setColor(EnumChatFormatting.RED).setClickEvent(ClickEvent.Action.RUN_COMMAND, "/f remove " + name).setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils.of("Remove " + name).setColor(EnumChatFormatting.RED).build()).append(" - ").setColor(EnumChatFormatting.GRAY).append("[UNBLACKLIST]").setColor(EnumChatFormatting.GREEN).setClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/autofriend blacklist remove " + name).setHoverEvent(HoverEvent.Action.SHOW_TEXT, ChatUtils.of("Blacklist " + name).setColor(EnumChatFormatting.RED).build()).build())));
                            this.sendMessage(EnumChatFormatting.GRAY + "----------------------");
                        }
                    } else {
                        this.throwError("You haven't blacklisted anyone");
                    }
                } else if (args[1].equals("add") || args[1].equals("remove")) {
                    if (args.length > 2 && this.username.matcher(args[2]).matches()) {
                        if (args[1].equals("add")) {
                            AutoFriend.blacklist.add(args[2]);
                        } else {
                            AutoFriend.blacklist.remove(args[2]);
                        }
                        this.sendMessage(EnumChatFormatting.BLUE + (args[1].equals("add") ? ("Added " + EnumChatFormatting.RED + args[2] + EnumChatFormatting.BLUE + " to") : ("Removed " + EnumChatFormatting.GREEN + args[2] + EnumChatFormatting.BLUE + " from")) + " your blacklist.");
                        BlacklistUtils.writeBlacklist();
                    } else {
                        this.throwError("Invalid username. Usage: /autofriend blacklist add/remove <username>");
                    }
                }
            } else if (args[0].equals("info")) {
                sendMessage(EnumChatFormatting.BLUE + "Forge Version created by" + EnumChatFormatting.RED + " 2PI" + " Ported by" + EnumChatFormatting.RED + " ConorTheOreo");
            }
        } else {
            this.throwError("Unknown Usage, Usage: /autofriend <toggle, messages, recent, blacklist, info>");
        }
    }

    public List<String> addTabCompletionOptions(final ICommandSender sender, final String[] args, final BlockPos pos) {
        switch (args.length) {
            case 1: {
                return (List<String>) getListOfStringsMatchingLastWord(args, new String[]{"toggle", "messages", "recent", "blacklist"});
            }
            case 2: {
                if (args[0].equalsIgnoreCase("blacklist")) {
                    return (List<String>) getListOfStringsMatchingLastWord(args, new String[]{"add", "remove"});
                }
                break;
            }
        }
        return Collections.emptyList();
    }
}
