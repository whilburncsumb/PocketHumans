package com.example.pockethumans;

import android.content.Context;
import android.content.Intent;
import androidx.test.platform.app.InstrumentationRegistry;

import junit.framework.TestCase;

import org.junit.Test;

public class IntentTest extends TestCase {

    @Test
    public void testLandingIntent() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int userId = 1;
        Intent intent = LandingActivity.getIntent(context,userId);
        assertNotNull(intent);
        assertEquals(LandingActivity.class.getName(), intent.getComponent().getClassName());
        assertEquals(userId, intent.getIntExtra("USERID", -1));
    }

    @Test
    public void testMainIntent() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = MainActivity.getIntent(context);
        assertNotNull(intent);
        assertEquals(MainActivity.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void testBattleIntent() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int userId = 1;
        Intent intent = BattleHumanActivity.getIntent(context,userId);
        assertNotNull(intent);
        assertEquals(BattleHumanActivity.class.getName(), intent.getComponent().getClassName());
        assertEquals(userId, intent.getIntExtra("USERID", -1));
    }
    @Test
    public void testEditIntent() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        int userId = 1;
        Intent intent = EditHumanActivity.getIntent(context,userId,false);
        assertNotNull(intent);
        assertEquals(EditHumanActivity.class.getName(), intent.getComponent().getClassName());
        assertEquals(userId, intent.getIntExtra("USERID", -1));
    }
    @Test
    public void testRegisterIntent() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Intent intent = RegisterActivity.getIntent(context);
        assertNotNull(intent);
        assertEquals(RegisterActivity.class.getName(), intent.getComponent().getClassName());
    }
}