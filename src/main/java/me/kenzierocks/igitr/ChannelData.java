package me.kenzierocks.igitr;

import com.google.auto.value.AutoValue;

import me.kenzierocks.igitr.autovaluegson.AutoGson;

@AutoValue
@AutoGson
public abstract class ChannelData {

    public static ChannelData create(String commandPrefix) {
        return new AutoValue_ChannelData(commandPrefix);
    }

    public static ChannelData getFromConfig(String channel) {
       Configuration config = IGITR.CONFIG;
       ChannelData data = config.getChannelDataMap().get(config);
       if (data == null){
           data = create(config.getDefaultPrefix());
           config.putChannelData(channel, data);
       }
       return data;
       
    }

    ChannelData() {
    }

    public abstract String getCommandPrefix();

}
