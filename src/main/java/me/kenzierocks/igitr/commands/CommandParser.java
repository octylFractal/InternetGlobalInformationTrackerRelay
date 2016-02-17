package me.kenzierocks.igitr.commands;

import java.util.List;

public interface CommandParser {
    
    List<String> parse(String raw);

}
