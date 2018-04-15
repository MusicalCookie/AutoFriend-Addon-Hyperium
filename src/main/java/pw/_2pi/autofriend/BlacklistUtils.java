package pw._2pi.autofriend;

import java.nio.file.*;
import java.io.*;
import java.util.*;

public class BlacklistUtils
{
    public static void getBlacklist() throws IOException {
        final File blacklistFile = new File("config/autofriend.cfg");
        if (blacklistFile.exists()) {
            AutoFriend.blacklist = Files.readAllLines(Paths.get(blacklistFile.toURI()));
            if (AutoFriend.blacklist.get(0).equals("true") || AutoFriend.blacklist.get(0).equals("false")) {
                AutoFriend.messages = Boolean.parseBoolean(AutoFriend.blacklist.get(0));
                AutoFriend.blacklist.remove(0);
            }
            else {
                writeBlacklist();
            }
        }
        else {
            writeBlacklist();
        }
    }
    
    public static boolean writeBlacklist() {
        try {
            final File blacklistFile = new File("config/autofriend.cfg");
            final FileWriter writer = new FileWriter(blacklistFile);
            writer.write(Boolean.toString(AutoFriend.messages));
            for (final String str : AutoFriend.blacklist) {
                writer.write(System.lineSeparator() + str);
            }
            writer.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
