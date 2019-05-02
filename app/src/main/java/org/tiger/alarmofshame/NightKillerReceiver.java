package org.tiger.alarmofshame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * Created by the-scrabi on 26.09.15.
 *
 * Copyright (C) Christian Schabesberger 2015 <chris.schabesberger@mailbox.org>
 * NightKillerReceiver.java is part of Terminightor.
 *
 * Terminightor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terminightor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terminightor.  If not, see <http://www.gnu.org/licenses/>.
 */

public class NightKillerReceiver extends BroadcastReceiver {
    private static final String TAG = NightKillerReceiver.class.toString();

    public static final String ACTION_FIRE_ALARM = "org.tiger.Terminightor.NightKillerReceiver.ACTION_FIRE_ALARM";

    static String consumer_token = "";
    static String consumer_secret = "";

    static String oauth_token = "";
    static String oauth_token_secret = "";

    //remove network restriction


    @Override
    public void onReceive(Context context, Intent intent) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if(intent.getAction().equals(ACTION_FIRE_ALARM)) {

            Log.d(TAG, "Gonna kill your night");


            //twitter message
            Twitter twitter = new TwitterFactory().getInstance();
            AccessToken a = new AccessToken(oauth_token, oauth_token_secret);
            twitter.setOAuthConsumer(consumer_token, consumer_secret);
            twitter.setOAuthAccessToken(a);

            try {
                twitter.updateStatus("If you're reading this on Twitter, it worked!");
            } catch (TwitterException e) {
                e.printStackTrace();
            }


            long id = intent.getLongExtra(Alarm.ID, -1);

            AlarmSetupManager.setupNextAlarm(context);

            Intent alarmServiceIntent = new Intent();
            alarmServiceIntent.putExtra(Alarm.ID, id);
            alarmServiceIntent.setClass(context, NightKillerService.class);
            context.startService(alarmServiceIntent);

        }
    }
}
