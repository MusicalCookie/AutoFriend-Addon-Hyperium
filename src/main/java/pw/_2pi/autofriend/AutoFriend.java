package pw._2pi.autofriend;

import cc.hyperium.internal.addons.*;
import cc.hyperium.*;
import pw._2pi.autofriend.comamnds.*;
import cc.hyperium.commands.*;
import java.io.*;
import cc.hyperium.event.*;
import java.util.*;

public class AutoFriend implements IAddon
{
    public static final String MODID = "AutoFriend";
    public static final String VERSION = "1.1";
    public static ave mc;
    public static boolean hypixel;
    public static boolean toggle;
    public static boolean messages;
    public static List<String> blacklist;
    public static List<String> recent;
    
    @InvokeEvent(priority = Priority.LOW)
    private void init(final InitializationEvent event) {
        Hyperium.INSTANCE.getHandlers().getHyperiumCommandHandler().registerCommand((BaseCommand)new FriendCommand());
        System.out.println(AutoFriend.blacklist.toString());
        try {
            BlacklistUtils.getBlacklist();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void onLoad() {
        System.out.println("AutoFriend loaded!");
        EventBus.INSTANCE.register((Object)this);
    }
    
    public void onClose() {
    }
    
    @InvokeEvent
    public void playerLoggedIn(final JoinHypixelEvent event) {
        AutoFriend.hypixel = true;
    }
    
    @InvokeEvent
    public void friendRequestEvent(final HypixelFriendRequestEvent event) {
        if (AutoFriend.hypixel && AutoFriend.toggle) {
            final String name = event.getFrom();
            if (!name.equalsIgnoreCase(AutoFriend.blacklist.toString())) {
                System.out.println("Friending " + name);
            }
            AutoFriend.mc.h.e("/friend accept " + name);
            AutoFriend.recent.add(name);
        }
    }
    
    static {
        AutoFriend.mc = ave.A();
        AutoFriend.hypixel = false;
        AutoFriend.toggle = true;
        AutoFriend.messages = true;
        AutoFriend.blacklist = new ArrayList<String>();
        AutoFriend.recent = new ArrayList<String>();
    }
}
