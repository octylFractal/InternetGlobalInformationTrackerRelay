package me.kenzierocks.igitr;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.kitteh.irc.client.library.Client;
import org.kitteh.irc.client.library.ClientBuilder;
import org.kitteh.irc.client.library.IRCFormat;
import org.kitteh.irc.client.library.auth.protocol.NickServ;
import org.kitteh.irc.client.library.auth.protocol.SaslPlain;
import org.kitteh.irc.client.library.element.MessageReceiver;
import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.channel.ChannelJoinEvent;
import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.client.library.event.client.ClientConnectedEvent;
import org.kitteh.irc.client.library.event.helper.ActorEvent;
import org.kitteh.irc.client.library.event.helper.MessageEvent;
import org.kitteh.irc.client.library.event.user.PrivateMessageEvent;
import org.kitteh.irc.client.library.util.AcceptingTrustManagerFactory;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import me.kenzierocks.igitr.commands.CommandParser;

public class IGITR {

    private static final Logger LOGGER = LoggerFactory.getLogger(IGITR.class);

    public static final Configuration CONFIG;
    static {
        try {
            CONFIG = Configuration.loadConfig();
        } catch (IOException e) {
            // bad.
            throw Throwables.propagate(e);
        }
    }

    public static void main(String[] args) throws Exception {
        ClientBuilder clientBuilder =
                Client.builder().nick(CONFIG.getNickName())
                        .realName(CONFIG.getRealName())
                        .name(CONFIG.getInternalName())
                        .serverHost(CONFIG.getIrcNetwork())
                        .serverPort(CONFIG.getIrcPort())
                        // trust everyone!
                        .secureTrustManagerFactory(
                                new AcceptingTrustManagerFactory())
                        .secure(CONFIG.isUseSsl())
                        .listenException(Exception::printStackTrace)
                        .listenInput(s -> LOGGER.info("[I] " + s));
        if (CONFIG.isLogOutput()) {
            clientBuilder.listenOutput(s -> LOGGER.info("[O] " + s));
        }
        final Client client;
        if (!Strings.isNullOrEmpty(CONFIG.getPassword())) {
            if (!Strings.isNullOrEmpty(CONFIG.getNickServUsername())) {
                client = clientBuilder.build();
                client.getAuthManager().addProtocol(new SaslPlain(client,
                        CONFIG.getNickServUsername(), CONFIG.getPassword()));
                client.getAuthManager().addProtocol(new NickServ(client,
                        CONFIG.getNickServUsername(), CONFIG.getPassword()));
            } else {
                client = clientBuilder.serverPassword(CONFIG.getPassword())
                        .build();
            }
        } else {
            client = clientBuilder.build();
        }
        LOGGER.info("Ready to go!");

        client.getEventManager().registerEventListener(new IGITR());
        client.getEventManager().registerEventListener(new GitCommandTracker());
    }

    private CommandParser commandParser = Splitter.on(' ')::splitToList;

    @Handler
    public void onConnect(ClientConnectedEvent event) {
        CONFIG.getChannelsToJoinOnStartup()
                .forEach(event.getClient()::addChannel);
    }

    @Handler
    public void onJoin(ChannelJoinEvent event) {
        event.getChannel()
                .sendMessage("Hi! I'm converting your messages to git soon™!");
    }

    @Handler
    public void onChanMessage(ChannelMessageEvent event) {
        ChannelData data =
                ChannelData.getFromConfig(event.getChannel().getName());
        if (event.getMessage().startsWith(data.getCommandPrefix())) {
            handleCommand(event, event.getChannel(),
                    this.commandParser.parse(event.getMessage()
                            .substring(data.getCommandPrefix().length())
                            .trim()));
        }
    }

    private <E extends MessageEvent & ActorEvent<User>> void handleCommand(
            E context, MessageReceiver location, List<String> command) {
        if (command.isEmpty()) {
            return;
        }
        boolean isAdmin = AdminDetection.isAdmin(context);
        if (command.get(0).equals("exit")) {
            tryIfAuth(isAdmin, location, loc -> {
                String cause = context.getActor().getNick();
                loc.sendMessage("Exiting as requested by " + cause + ".");
                context.getClient()
                        .shutdown("git commit -m 'Killed by " + cause + "'");
            });
        }
    }

    private void tryIfAuth(boolean isAdmin, MessageReceiver location,
            Consumer<MessageReceiver> action) {
        if (isAdmin) {
            action.accept(location);
        } else {
            location.sendMessage(
                    IRCFormat.RED + "Woah there! You can't do that!");
        }
    }

    @Handler
    public void onPM(PrivateMessageEvent event) {
    }

}
