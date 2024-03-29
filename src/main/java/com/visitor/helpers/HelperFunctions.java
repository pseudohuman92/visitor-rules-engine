package com.visitor.helpers;

import com.visitor.game.Card;
import com.visitor.game.parts.Game;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static java.lang.Class.forName;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class HelperFunctions {

    public static void runIfNotNull(Object object, Runnable runnable, Runnable elseRun) {
        if (object != null) {
            runnable.run();
        } else {
            System.out.println("Trying to run following on null\n R: " + runnable.toString());
            elseRun.run();
        }
    }

    public static <T> T runIfNotNull(Object object, Supplier<T> supplier, Supplier<T> elseRun) {
        if (object != null) {
            return supplier.get();
        } else {
            System.out.println("Trying to run following on null\n R: " + supplier.toString());
            return elseRun.get();
        }
    }

    public static void runIfNotNull(Object object, Runnable runnable) {
        runIfNotNull(object, runnable, () -> {
        });
    }

    public static <T> T runIfNotNull(Object object, Supplier<T> supplier) {
        return runIfNotNull(object, supplier, () -> null);
    }

    // Taken from stackoverflow
    public static List<String> getClassesInPackage(String packageName) {
        List<String> classes = new ArrayList<String>();
        URL root = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "/"));

        // Filter .class files.
        File[] files = new File[0];
        try {
            files = new File(URLDecoder.decode(root.getFile(), "UTF-8")).listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".class");
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

// Find classes implementing ICommand.
        for (File file : files) {
            classes.add(file.getName().replaceAll(".class$", ""));

        }
        return classes;
    }

    public static Card createCard(Game game, UUID playerId, String cardName) {
        try {
            Class<?> cardClass = forName("com.visitor.sets." + cardName);
            Constructor<?> cardConstructor = cardClass.getConstructor(Game.class, UUID.class);
            Object card = cardConstructor.newInstance(game, playerId);
            return ((Card) card);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassNotFoundException ex) {
            getLogger(HelperFunctions.class.getName()).log(SEVERE, null, ex);
        }
        return null;
    }

}
