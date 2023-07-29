package com.example.remotecontrolapp;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.Manifest;
import android.content.SharedPreferences;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.remotecontrolapp.controller.RemoteView;
import com.example.remotecontrolapp.stbs.ChooseConnection;
import com.example.remotecontrolapp.stbs.Stb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChooseConnectionInstrumentedTest {

    private ChooseConnection chooseConnection;


    @Before
    public void setUp() {
        ArrayList<Stb> stbList = new ArrayList<>();
        try {
            InetAddress address1 = InetAddress.getByName("192.168.1.1");
            InetAddress address2 = InetAddress.getByName("192.168.1.2");
            stbList.add(new Stb(1, "First STB", "Type", address1, 8080));
            stbList.add(new Stb(2, "Second STB", "Type", address2, 8080));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getSharedPreferences(Constants.PREFS_NAME, InstrumentationRegistry.getInstrumentation().getTargetContext().MODE_PRIVATE)
                .edit();
        editor.clear(); // Clear any saved server
        editor.apply();

        // Grant RECORD_AUDIO permission
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("pm grant " + InstrumentationRegistry.getInstrumentation().getTargetContext().getPackageName() + " " + permissions[0]);


        ActivityScenario<ChooseConnection> activityScenario = ActivityScenario.launch(ChooseConnection.class);
        activityScenario.onActivity(activity -> {
            chooseConnection = activity;
        });

        Intents.init();
    }

    @After
    public void tearDown() {
        SharedPreferences.Editor editor = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getSharedPreferences(Constants.PREFS_NAME, InstrumentationRegistry.getInstrumentation().getTargetContext().MODE_PRIVATE)
                .edit();
        editor.clear(); // Clear any saved server
        editor.apply();

        Intents.release();
    }

    @Test
    public void testStbListDisplayed() {
        onView(withId(R.id.rv_boxes_list))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void testEnterCodeContainerHidden() {
        onView(withId(R.id.rl_pairing_container)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testClickOnStbItem_EntersCode() {
        onView(withId(R.id.rv_boxes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(withId(R.id.rl_pairing_container)).check(matches(isDisplayed()));
    }

    @Test
    public void testCorrectCode_EntersRemoteView() {
        onView(withId(R.id.rv_boxes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(withId(R.id.et_pairing_code)).perform(ViewActions.typeText(String.valueOf(Singleton.getInstance().getCommandsHandler().getPairingCode())));
        onView(withId(R.id.btn_send_code)).perform(ViewActions.click());


        // Assert that the RemoteView activity is launched
        intended(hasComponent(RemoteView.class.getName()));
    }

    @Test
    public void testIncorrectCode_ShowsToast() {
        onView(withId(R.id.rv_boxes_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        onView(withId(R.id.et_pairing_code)).perform(ViewActions.typeText("123456"));
        onView(withId(R.id.btn_send_code)).perform(ViewActions.click());

        // Assert that a toast message with "Try again" is displayed
        onView(withText("Try again")).inRoot(withDecorView(not(chooseConnection.getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void testBackPressed_ExitActivity() {
        Espresso.pressBackUnconditionally();
        assertTrue(chooseConnection.isFinishing());
    }


    @Test
    public void testSaveServer_PreferenceUpdated() {
        String selectedHostAddress = "192.168.1.1";

        chooseConnection.saveServer(selectedHostAddress);

        SharedPreferences preferences = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getSharedPreferences(Constants.PREFS_NAME, InstrumentationRegistry.getInstrumentation().getTargetContext().MODE_PRIVATE);

        String savedHostAddress = preferences.getString(Constants.kSAVED_SERVER, null);
        assertEquals(selectedHostAddress, savedHostAddress);
    }

    @Test
    public void testIsServerInSharedPrefs_ServerExists_ReturnsTrue() {
        String savedHostAddress = "192.168.1.1";

        SharedPreferences.Editor editor = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getSharedPreferences(Constants.PREFS_NAME, InstrumentationRegistry.getInstrumentation().getTargetContext().MODE_PRIVATE)
                .edit();
        editor.putString(Constants.kSAVED_SERVER, savedHostAddress);
        editor.apply();

        boolean result = chooseConnection.isServerInSharedPrefs(savedHostAddress);
        assertTrue(result);
    }

    @Test
    public void testIsServerInSharedPrefs_ServerDoesNotExist_ReturnsFalse() {
        String savedHostAddress = "192.168.1.1";

        boolean result = chooseConnection.isServerInSharedPrefs(savedHostAddress);
        assertFalse(result);
    }
}

