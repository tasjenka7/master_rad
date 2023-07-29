package com.example.remotecontrolapp;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
    public static final String PREFS_NAME = "true_tv_remote_prefs";
    public static final String kSAVED_SERVER = "saved_server";

    public static final String COMMAND_OK = "ok";
    public static final String COMMAND_LEFT = "left";
    public static final String COMMAND_RIGHT = "right";
    public static final String COMMAND_UP = "up";
    public static final String COMMAND_DOWN = "down";
    public static final String COMMAND_BACK = "back";
    public static final String COMMAND_ONE = "one";
    public static final String COMMAND_TWO = "two";
    public static final String COMMAND_THREE = "three";
    public static final String COMMAND_FOUR = "four";
    public static final String COMMAND_FIVE = "five";
    public static final String COMMAND_SIX = "six";
    public static final String COMMAND_SEVEN = "seven";
    public static final String COMMAND_EIGHT = "eight";
    public static final String COMMAND_NINE = "nine";
    public static final String COMMAND_ZERO = "zero";
    public static final String COMMAND_GUIDE = "guide";
    public static final String COMMAND_MOVIE = "movie";
    public static final String COMMAND_LIVETV = "live_tv";
    public static final String COMMAND_HOME = "home";
    public static final String COMMAND_VOLUME_DOWN = "volume_down";
    public static final String COMMAND_VOLUME_MUTE = "volume_mute";
    public static final String COMMAND_VOLUME_UP = "volume_up";
    public static final String COMMAND_CHANNEL_UP = "channel_up";
    public static final String COMMAND_CHANNEL_DOWN = "channel_down";
    public static final String COMMAND_POWER = "power";

    public final static int OPEN_VOD_SCENE = 430;

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int NINE = 9;
    public static final int ZERO = 0;

    // List of supported recording commands
    public static final ArrayList<String> vpower =
            new ArrayList<>(Arrays.asList("power", "power on", "on", "power off", "off", "turn on", "turn off", "turn on tv", "turn off tv", "sleep",
                    "uključi", "ugasi", "uključi se", "ugasi se", "uključi tv", "ugasi tv"));

    public static final ArrayList<String> vguide =
            new ArrayList<>(Arrays.asList("guide", "show guide", "all channels", "show channels",
                    "prikaži kanale", "svi kanali", "kanali", "prikaži sve kanale"));

    public static final ArrayList<String> vmovie =
            new ArrayList<>(Arrays.asList("movie", "movies", "show movie", "show movies",
                    "film", "filmovi", "svi filmovi", "lista filmova", "prikaži filmove", "prikaži listu filmova", "prikaži sve filmove"));

    public static final ArrayList<String> vlive =
            new ArrayList<>(Arrays.asList("tv", "live tv", "play live", "play live tv",
                    "uživo", "tv uživo", "pusti tv", "pusti uživo", "prikaži uživo"));

    public static final ArrayList<String> vok = new ArrayList<>(Arrays.asList("ok", "okay", "okey",
            "okej", "uredu", "u redu"));

    public static final ArrayList<String> vback =
            new ArrayList<>(Arrays.asList("back", "return", "go back",
                    "nazad", "vrati", "vrati se"));

    public static final ArrayList<String> vhome =
            new ArrayList<>(Arrays.asList("home", "home screen", "show home", "go to home", "go to home screen",
                    "početni ekran", "prvi ekran", "početak"));

    public static final ArrayList<String> vvolup =
            new ArrayList<>(Arrays.asList("volume up", "louder", "up volume",
                    "pojačaj", "glasnije", "jače", "pojačaj ton"));

    public static final ArrayList<String> vvoldown =
            new ArrayList<>(Arrays.asList("volume down", "quieter", "down volume",
                    "utišaj", "tiše", "smanji", "smanji ton", "snizi ton"));

    public static final ArrayList<String> vmute =
            new ArrayList<>(Arrays.asList("mute", "silent", "tišina", "ugasi zvuk"));

    public static final ArrayList<String> vchup =
            new ArrayList<>(Arrays.asList("up", "channel up", "next", "next channel",
                    "sledeći kanal", "sledeći", "gore", "pusti sledeći", "pusti sledeći kanal"));

    public static final ArrayList<String> vchdown =
            new ArrayList<>(Arrays.asList("down", "channel down", "previous", "previous channel",
                    "prethodni kanal", "prošli kanal", "pusti prethodni", "pusti prethodni kanal"));

    public static final String permissionMsg = "Da biste bili u mogućnosti da koristite mogućnost upravljanja glasom potrebno je da dozvolite upotrebu mikrofona. Odluku možete bilo kad promeniti u podešavanjima telefona.";
    public static final String noDevices = "Nismo u mogućnosti da pronađemo nijedan uređaj na mreži. ";
    public static final String searchAgain = "Traži ponovo";
    public static final String closeApp = "Zatvori aplikaciju";
    public static final String lookingForDevices = "Pretraga uređaja";
}
