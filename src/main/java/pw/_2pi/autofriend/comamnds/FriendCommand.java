package pw._2pi.autofriend.comamnds;

import java.util.regex.*;
import pw._2pi.autofriend.*;
import cc.hyperium.commands.*;
import java.util.*;

public class FriendCommand implements BaseCommand
{
    private Pattern username;
    
    public FriendCommand() {
        this.username = Pattern.compile("\\w{1,16}");
    }
    
    public void sendMessage(final String message) {
        AutoFriend.mc.h.a((eu)new fa(message));
    }
    
    public void throwError(final String error) {
        this.sendMessage(a.m + "Error: " + error);
    }
    
    public String getName() {
        return "autofriend";
    }
    
    public String getUsage() {
        return "/autofriend <toggle, messages, recent, blacklist>";
    }
    
    public void onExecute(final String[] args) throws CommandException {
        if (args.length > 0) {
            if (args[0].equals("toggle")) {
                AutoFriend.toggle = !AutoFriend.toggle;
                this.sendMessage(a.j + "AutoFriend: " + (AutoFriend.toggle ? (a.k + "On") : (a.m + "Off")));
            }
            else if (args[0].equals("messages")) {
                AutoFriend.messages = !AutoFriend.messages;
                BlacklistUtils.writeBlacklist();
                if (!AutoFriend.messages) {
                    this.sendMessage(ChatUtils.of("NOTE: This will also remove divider messages which may affect other messages on Hypixel. Hover on this message to see what they look like.").setColor(a.m).setBold(true).setHoverEvent(ew.a.a, ChatUtils.of("-----------------------------------------------------").setColor(a.p).build()).build().toString());
                }
                this.sendMessage(a.j + "Friend Messages: " + (AutoFriend.messages ? (a.k + "On") : (a.m + "Off")));
            }
            else if (args[0].equals("recent")) {
                if (AutoFriend.recent.size() > 0) {
                    int page = 1;
                    final int pages = (int)Math.ceil(AutoFriend.recent.size() / 7.0);
                    if (args.length > 1) {
                        try {
                            page = Integer.parseInt(args[1]);
                        }
                        catch (NumberFormatException ignored) {
                            page = -1;
                        }
                    }
                    if (page < 1 || page > pages) {
                        this.throwError("Invalid page number, " + AutoFriend.mc.h.f_().toString());
                    }
                    else {
                        this.sendMessage(a.h + "----------------------" + AutoFriend.mc.h.f_().toString());
                        this.sendMessage(a.j + "Friend History " + a.d + "(Page " + page + " of " + pages + ")");
                        AutoFriend.recent.stream().skip((page - 1) * 7).limit(7L).forEach(name -> this.sendMessage(ChatUtils.of(a.j + name + a.h + " - ").append("[REMOVE]").setColor(a.m).setClickEvent(et.a.c, "/f remove " + name).setHoverEvent(ew.a.a, ChatUtils.of("Remove " + name).setColor(a.m).build()).append(" - ").setColor(a.h).append("[BLACKLIST]").setColor(a.i).setClickEvent(et.a.e, "/autofriend blacklist add " + name).setHoverEvent(ew.a.a, ChatUtils.of("Blacklist " + name).setColor(a.m).build()).build().toString()));
                        this.sendMessage(a.h + "----------------------" + AutoFriend.mc.h.f_().toString());
                    }
                }
                else {
                    this.throwError("You haven't friended anyone, " + AutoFriend.mc.h.f_().toString());
                }
            }
            else if (args[0].equals("blacklist")) {
                if (args.length == 1 || (!args[1].equals("add") && !args[1].equals("remove"))) {
                    if (AutoFriend.blacklist.size() > 0) {
                        int page = 1;
                        final int pages = (int)Math.ceil(AutoFriend.blacklist.size() / 7.0);
                        if (args.length > 1) {
                            try {
                                page = Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException ignored) {
                                page = -1;
                            }
                        }
                        if (page < 1 || page > pages) {
                            this.throwError("Invalid page number, " + AutoFriend.mc.h.f_().toString());
                        }
                        else {
                            this.sendMessage(a.h + "----------------------");
                            this.sendMessage(a.j + "Blacklist " + a.d + "(Page " + page + " of " + pages + ")");
                            AutoFriend.blacklist.stream().skip((page - 1) * 7).limit(7L).forEach(name -> this.sendMessage(ChatUtils.of(a.j + name + a.h + " - ").append("[REMOVE]").setColor(a.m).setClickEvent(et.a.c, "/f remove " + name).setHoverEvent(ew.a.a, ChatUtils.of("Remove " + name).setColor(a.m).build()).append(" - ").setColor(a.h).append("[UNBLACKLIST]").setColor(a.k).setClickEvent(et.a.e, "/autofriend blacklist remove " + name).setHoverEvent(ew.a.a, ChatUtils.of("Blacklist " + name).setColor(a.m).build()).build().toString()));
                            this.sendMessage(a.h + "----------------------");
                        }
                    }
                    else {
                        this.throwError("You haven't blacklisted anyone," + AutoFriend.mc.h.f_().toString());
                    }
                }
                else if (args[1].equals("add") || args[1].equals("remove")) {
                    if (args.length > 2 && this.username.matcher(args[2]).matches()) {
                        if (args[1].equals("add")) {
                            AutoFriend.blacklist.add(args[2]);
                        }
                        else {
                            AutoFriend.blacklist.remove(args[2]);
                        }
                        this.sendMessage(a.j + (args[1].equals("add") ? ("Added " + a.m + args[2] + a.j + " to") : ("Removed " + a.k + args[2] + a.j + " from")) + " your blacklist.");
                        BlacklistUtils.writeBlacklist();
                    }
                    else {
                        this.throwError("Invalid username. Usage: /autofriend blacklist add/remove <username>");
                    }
                }
                else {
                    this.throwError("/autofriend blacklist <add, remove, [number]>");
                }
            }
            else {
                this.throwError("Unknown Error Occured, please contact ConorTheOreo#0939 on Discord");
            }
        }
        else {
            this.throwError("Unknown Error Occured, please contact ConorTheOreo#0939 on Discord");
        }
    }
    
    public List<String> addTabCompletionOptions(final m sender, final String[] args, final cj pos) {
        switch (args.length) {
            case 1: {
                return (List<String>)i.a(args, new String[] { "toggle", "messages", "recent", "blacklist" });
            }
            case 2: {
                if (args[0].equalsIgnoreCase("blacklist")) {
                    return (List<String>)i.a(args, new String[] { "add", "remove" });
                }
                break;
            }
        }
        return Collections.emptyList();
    }
}
