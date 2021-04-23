package com.bavantha.ripod;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MessageBuilder;
import com.applozic.mobicomkit.uiwidgets.conversation.stt.KmSpeechToText;
import com.applozic.mobicomkit.uiwidgets.kommunicate.KmPrefSettings;
import com.applozic.mobicommons.commons.core.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.kommunicate.KmChatBuilder;
import io.kommunicate.KmConversationBuilder;
import io.kommunicate.KmConversationHelper;
import io.kommunicate.KmException;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    VideoView videoView;
    ImageView menu_btn;
    GifImageView assistant_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        videoView = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ripod_video);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(mp -> mp.setLooping(true));

        menu_btn = (ImageView) findViewById(R.id.imageView);
        menu_btn.setOnClickListener(this);

        assistant_btn = (GifImageView) findViewById(R.id.assist_giff);
        assistant_btn.setOnClickListener(this);

        Kommunicate.init(this, "41f1f1dfc1fafb6145f2a4199c5f8591");
        KmPrefSettings.getInstance(MainActivity.this)
                .setFaqOptionEnabled(false)
                .enableSpeechToText(true) // if true, enables speech to text feature in the SDK.
                .enableTextToSpeech(true) // if true, enables text to speech feature. All the messages received will be spoken when the chat screen is open.
                .setSendMessageOnSpeechEnd(true); //if true, the speech will automatically be sent as a message without clicking the send message button.

    }

    @Override
    protected void onResume() {
        super.onResume();
        // to restart the video after coming from other activity like Sing up
        videoView.start();


    }


    public void onClick(View V) {
        if (V.getId() == R.id.imageView) {
            //Toast.makeText(this, "Click on the Button", Toast.LENGTH_SHORT).show();
            videoView.stopPlayback();
            this.finish();
            startActivity(new Intent(MainActivity.this, OptionsActivity.class));

        }

        if(V.getId()==R.id.assist_giff){
            List<String> botList = new ArrayList();
            botList.add("ripod-a9nsw"); //enter your integrated bot Ids

            final KmSpeechToText.KmTextListener kmTextListener = new KmSpeechToText.KmTextListener() {
                @Override
                public void onSpeechToTextResult(String text) {

                }

                @Override
                public void onSpeechToTextPartialResult(String text) {

                }

                @Override
                public void onSpeechEnd(int errorCode) {
                    Toast.makeText(MainActivity.this, "Done!", Toast.LENGTH_SHORT);

                };
            };

            new KmConversationBuilder(MainActivity.this)
                    .setConversationTitle("RiPod: Smart Agent")
                    .setSingleConversation(false) // Pass false if you would like to create new conversation every time user starts a conversation. This is true by default which means only one conversation will open for the user every time the user starts a conversation.
                    .launchConversation(new KmCallback() {
                        @Override
                        public void onSuccess(Object message) {
                            String conversationId = message.toString();
//                            new MessageBuilder(MainActivity.this)
//                                    .setMessage("Hello there")
//                                    .setGroupId(Integer.valueOf(conversationId)) //where 123456 is the conversationId.
//                                    .send();
                        }

                        @Override
                        public void onFailure(Object error) {
                            Log.d("ConversationTest", "Error : " + error);
                        }
                    });

        }

    }
}