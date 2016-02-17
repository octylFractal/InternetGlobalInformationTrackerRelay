package me.kenzierocks.igitr.firebase;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.firebase.security.token.TokenGenerator;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

/**
 * Generates a JWT. Simple and clean.
 */
public class JWTGen {

    public static void main(String[] args) {
        try (
                Scanner s = new Scanner(System.in)) {
            System.err.print("Hey! What's the secret?? ");
            TokenGenerator gen = new TokenGenerator(s.nextLine());
            System.out.println(gen.createToken(createDataMap(s)));
        }
    }

    @SuppressWarnings("serial")
    private static Map<String, Object> createDataMap(Scanner source) {
        Map<String, Object> map = new HashMap<>();
        System.err.print("Ok, what's the ID? ");
        map.put("uid", source.nextLine());
        System.err.print("Any last words? ");
        String json = source.nextLine();
        if (!Strings.isNullOrEmpty(json)) {
            map.putAll(new Gson().fromJson(json,
                    new TypeToken<Map<String, Object>>() {
                    }.getType()));
        }
        return map;
    }

}
