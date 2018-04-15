package pw._2pi.autofriend;

import cc.hyperium.Hyperium;
import cc.hyperium.commands.BaseCommand;
import cc.hyperium.event.*;
import cc.hyperium.internal.addons.IAddon;
import net.minecraft.client.Minecraft;
import pw._2pi.autofriend.commands.FriendCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutoFriend implements IAddon {
    public static final String MODID = "AutoFriend";
    public static final String VERSION = "1.1";
    public static Minecraft mc;
    public static boolean hypixel;
    public static boolean toggle;
    public static boolean messages;
    public static List<String> blacklist;
    public static List<String> recent;

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
    }

    public void onClose() {
        /* should probs put smthn here... */
    }

    @InvokeEvent
    public void playerLoggedIn(final JoinHypixelEvent event) {
        AutoFriend.hypixel = true;
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
