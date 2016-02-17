package me.kenzierocks.igitr;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.kenzierocks.igitr.autovaluegson.AutoValueAdapterFactory;

public class Configuration {

    private static final Gson dataHandler = new GsonBuilder()
            .setPrettyPrinting().serializeNulls().disableHtmlEscaping()
            .registerTypeAdapterFactory(new AutoValueAdapterFactory()).create();

    private static Path getConfigPath() {
        Path configPath =
                Paths.get(".").resolve("config").resolve("config.cfg");
        try {
            Files.createDirectories(configPath.getParent());
            if (Files.notExists(configPath)) {
                Files.createFile(configPath);
                new Configuration().saveConfig();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configPath;
    }

    public static Configuration loadConfig() throws IOException {
        Configuration config;
        try (
                Reader reader = Files.newBufferedReader(getConfigPath())) {
            config = dataHandler.fromJson(reader, Configuration.class);
        }
        config.saveConfig();
        return config;
    }

    public void saveConfig() throws IOException {
        try (
                Writer writer = Files.newBufferedWriter(getConfigPath())) {
            dataHandler.toJson(this, writer);
        }
    }

    private void saveConfigOrLogException() {
        try {
            saveConfig();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    private String ircNetwork;
    private int ircPort = 6667;
    private boolean useSsl = false;
    private String nickName = "IGITR";
    private String realName = "InternetGlobalInformationTrackerRelay";
    private String internalName = "TheIGITR";
    private boolean logOutput = false;
    private Set<String> channelsToJoinOnStartup = new HashSet<>();
    /**
     * If present, use depends on {@link #nickServUsername}. If the username is
     * empty, then this password will be used as a server password. Otherwise,
     * this will be used as a NickServ/SASL password.
     */
    private String password;
    private String nickServUsername;
    private String defaultPrefix = "`";
    private Map<String, ChannelData> channelDataMap = new HashMap<>();
    private String firebaseUrl = "https://igitr.firebaseio.com/";
    private String firebaseAccessToken;

    public String getIrcNetwork() {
        return this.ircNetwork;
    }

    public void setIrcNetwork(String ircNetwork) {
        this.ircNetwork = ircNetwork;
        saveConfigOrLogException();
    }

    public int getIrcPort() {
        return this.ircPort;
    }

    public void setIrcPort(int ircPort) {
        this.ircPort = ircPort;
        saveConfigOrLogException();
    }

    public boolean isUseSsl() {
        return this.useSsl;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
        saveConfigOrLogException();
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        saveConfigOrLogException();
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
        saveConfigOrLogException();
    }

    public String getInternalName() {
        return this.internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
        saveConfigOrLogException();
    }

    public boolean isLogOutput() {
        return this.logOutput;
    }

    public void setLogOutput(boolean logOutput) {
        this.logOutput = logOutput;
        saveConfigOrLogException();
    }

    public Set<String> getChannelsToJoinOnStartup() {
        return ImmutableSet.copyOf(this.channelsToJoinOnStartup);
    }

    public boolean addChannelToJoinOnStartup(String channel) {
        try {
            return this.channelsToJoinOnStartup.add(channel);
        } finally {
            saveConfigOrLogException();
        }
    }

    public boolean removeChannelToJoinOnStartup(String channel) {
        try {
            return this.channelsToJoinOnStartup.remove(channel);
        } finally {
            saveConfigOrLogException();
        }
    }

    public void
            setChannelsToJoinOnStartup(Set<String> channelsToJoinOnStartup) {
        this.channelsToJoinOnStartup = new HashSet<>(channelsToJoinOnStartup);
        saveConfigOrLogException();
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
        saveConfigOrLogException();
    }

    public String getNickServUsername() {
        return this.nickServUsername;
    }

    public void setNickServUsername(String nickServUsername) {
        this.nickServUsername = nickServUsername;
        saveConfigOrLogException();
    }

    public String getDefaultPrefix() {
        return this.defaultPrefix;
    }

    public void setDefaultPrefix(String defaultPrefix) {
        this.defaultPrefix = defaultPrefix;
        saveConfigOrLogException();
    }

    public Map<String, ChannelData> getChannelDataMap() {
        return ImmutableMap.copyOf(this.channelDataMap);
    }

    public ChannelData putChannelData(String key, ChannelData value) {
        try {
            return this.channelDataMap.put(key, value);
        } finally {
            saveConfigOrLogException();
        }
    }

    public ChannelData removeChannelData(String key) {
        try {
            return this.channelDataMap.remove(key);
        } finally {
            saveConfigOrLogException();
        }
    }

    public void mapChannelData(String key,
            Function<ChannelData, ChannelData> transform) {
        this.channelDataMap.compute(key, (k, v) -> transform.apply(v));
        saveConfigOrLogException();
    }

    public void setChannelDataMap(Map<String, ChannelData> channelDataMap) {
        this.channelDataMap = channelDataMap;
        saveConfigOrLogException();
    }

    public String getFirebaseUrl() {
        return this.firebaseUrl;
    }

    public void setFirebaseUrl(String firebaseUrl) {
        this.firebaseUrl = firebaseUrl;
        saveConfigOrLogException();
    }

    public String getFirebaseAccessToken() {
        return this.firebaseAccessToken;
    }

    public void setFirebaseAccessToken(String firebaseAccessToken) {
        this.firebaseAccessToken = firebaseAccessToken;
        saveConfigOrLogException();
    }

}
