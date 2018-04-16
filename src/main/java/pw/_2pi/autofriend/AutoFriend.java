package pw._2pi.autofriend;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.event.*;
import cc.hyperium.internal.addons.IAddon;
import me.conortheoreo.autofriendport.update.VersionChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class AutoFriend implements IAddon {
    public static final String MODID = "AutoFriend";
    public static final String VERSION = "1.2";
    public static Minecraft mc;
    public static boolean hypixel;
    public static boolean toggle;
    public static boolean messages;
    public static List<String> blacklist;
    public static List<String> recent;
    VersionChecker vc = new VersionChecker();

    static {
        AutoFriend.mc = Minecraft.getMinecraft();
        AutoFriend.hypixel = false;
        AutoFriend.toggle = true;
        AutoFriend.messages = true;
        AutoFriend.blacklist = new ArrayList<String>();
        AutoFriend.recent = new ArrayList<String>();
    }

    private Pattern friend;

    public AutoFriend() {
        this.friend = Pattern.compile("§m----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*");
    }

    @InvokeEvent(priority = Priority.LOW)
    private void init(final InitializationEvent event) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand((BaseCommand) new FriendCommand());
        System.out.println(AutoFriend.blacklist.toString());
        try {
            BlacklistUtils.getBlacklist();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLoad() {
        System.out.println("AutoFriend loaded!");
        EventBus.INSTANCE.register((Object) this);
        vc.run();
    }

    public void onClose() {
        /* should probs put smthn here... */
    }

    @InvokeEvent
    public void playerLoggedIn(final JoinHypixelEvent event) {
        AutoFriend.hypixel = true;

        if(vc.getLatestVersion() != VERSION) {
            mc.thePlayer.addChatMessage(ChatUtils.of("AutoFriend Needs an update! Version: " + vc.getLatestVersion() + "." + " Go to: https://github.com/ConorTheOreo/AutoFriend-Addon-Hyperium/releases to download the latest release.").setColor(EnumChatFormatting.RED).setBold(true).build());
        }
    }

    @InvokeEvent
    public void friendRequestEvent(final HypixelFriendRequestEvent event) {
        String name = event.getFrom();
        if (!AutoFriend.blacklist.stream().anyMatch((Predicate<? super String >) name::equalsIgnoreCase)) {
            if (!name.equalsIgnoreCase(AutoFriend.blacklist.toString())) {
                System.out.println("Friending " + name);
            }
            AutoFriend.mc.thePlayer.sendChatMessage("/friend accept " + name);
            AutoFriend.recent.add(name);
        }
    }
}
