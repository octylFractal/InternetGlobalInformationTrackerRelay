package me.kenzierocks.igitr;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;

public class IGITR {

    public static void main(String[] args) throws Exception {
        // Client client =
        // Client.builder().nick("kenzierocks").realName("HECOMES")
        // .name("InternalName").serverHost("localhost").serverPort(13420)
        // // trust everyone!
        // .secureTrustManagerFactory(new AcceptingTrustManagerFactory())
        // .secure(false)
        // .listenException(Exception::printStackTrace)
        // .listenInput(s -> System.err.println("[I] " + s)).build();
        // client.setOutputListener(s -> System.err.println("[O] " + s));
        // System.err.println("Ready to go!");
        // client.getEventManager().registerEventListener(new IGITR());
    }

    @Handler
    public void onConnect(ClientConnectedEvent event) throws Exception {
    }

    @Handler
    public void onChanMessage(ChannelMessageEvent event) {
    }

    @Handler
    public void onPM(PrivateMessageEvent event) {
    }

}
