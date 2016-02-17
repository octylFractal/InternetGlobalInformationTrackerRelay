package me.kenzierocks.igitr;

import org.kitteh.irc.client.library.event.channel.ChannelMessageEvent;
import org.kitteh.irc.lib.net.engio.mbassy.listener.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.Firebase.AuthResultHandler;
import com.firebase.client.FirebaseError;

/**
 * The class that does the magic.
 */
public class GitCommandTracker {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GitCommandTracker.class);

    private final Firebase firebase =
            new Firebase(IGITR.CONFIG.getFirebaseUrl());
    {
        this.firebase.authWithCustomToken(IGITR.CONFIG.getFirebaseAccessToken(),
                new AuthResultHandler() {

                    @Override
                    public void onAuthenticationError(FirebaseError arg0) {
                        LOGGER.error("Error authenticating",
                                arg0.toException());
                    }

                    @Override
                    public void onAuthenticated(AuthData arg0) {
                        LOGGER.info("Authenticated, welcome " + arg0.getUid());
                    }

                });
    }
    private final Firebase gitCommands = this.firebase.child("git-commands");

    @Handler
    public void onChannelMessage(ChannelMessageEvent event) {

    }

}
