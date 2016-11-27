package com.example.zl.ihour;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ConServer {

    public static XMPPConnection connection;
    public static String useraccount;
    public static String userpassword;
    private static ConnectionConfiguration config = new ConnectionConfiguration("112.74.60.143", 5222);




    private ConServer() {
        connect();
    }

    private static ConServer instance = new ConServer();

    public static ConServer getInstance() {
        return instance;
    }

    private void connect() {
        config.setServiceName("IMQQ");
//        /** 是否启用安全验证 */
//        config.setSASLAuthenticationEnabled(false);
//        /** 是否启用调试 */
//         config.setDebuggerEnabled(true);
        /** 创建connection链接 */
        if (connection == null || !connection.isConnected()) {
            connection = new XMPPConnection(config);
            try {
                /** 建立连接 */
                connection.connect();

            } catch (XMPPException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<RosterEntry> getAllEntries() {
        try {
            if (!ConServer.connection.isConnected()) {
                ConServer.connection.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Roster roster = ConServer.connection.getRoster();
        List<RosterEntry> EntriesList = new ArrayList<RosterEntry>();
        Collection<RosterEntry> collection = roster.getEntries();
        Iterator<RosterEntry> i = collection.iterator();
        while (i.hasNext()) {
            EntriesList.add(i.next());
        }
        return EntriesList;
    }



}

