package me.kenzierocks.igitr;

import org.kitteh.irc.client.library.element.User;
import org.kitteh.irc.client.library.event.helper.ActorEvent;

public class AdminDetection {

    public static boolean isAdmin(ActorEvent<User> event) {
        return event.getActor().getAccount().orElse("").equals("kenzierocks");
    }

}
