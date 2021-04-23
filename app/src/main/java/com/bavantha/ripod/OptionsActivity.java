package com.bavantha.ripod;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.uiwidgets.conversation.stt.KmSpeechToText;
import com.applozic.mobicomkit.uiwidgets.kommunicate.KmPrefSettings;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.imaginativeworld.whynotimagecarousel.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener;
import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.jetbrains.annotations.NotNull;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;
import java.util.List;

import abak.tr.com.boxedverticalseekbar.BoxedVertical;
import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KmCallback;
import pl.droidsonroids.gif.GifImageView;

public class OptionsActivity extends RosActivity implements View.OnClickListener {
    private Button auto, manual, load, save;
    private BoxedVertical light, pan, tilt;
    private ImageView up_click, down_click, setting;
    private RosImageView rec, play, stop, web;
    private ModeTalker controlTalker;
    static int light_val, height_val, pan_val, tilt_val;
    static String mode_val;
    static int preset_no;
    static boolean play_clicked, rec_clicked, load_clicked, save_clicked, stop_clicked, up_flag, down_flag;
    ImagePopup imagePopup;
    GifImageView assistant_btn1;
    Bitmap bitmap;
    Drawable d;

    public OptionsActivity() {
        super("RiPod ROS Client", "RiPod ROS Client");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options);

        play_clicked = false;
        rec_clicked = false;
        stop_clicked = true;
        load_clicked = false;
        save_clicked = false;
        up_flag = false;
        down_flag= false;
        preset_no = 1;
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("URL", "http://121.0.0.1:8080").commit();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auto = findViewById(R.id.auto_btn);
        auto.setOnClickListener(this);

        manual = findViewById(R.id.manual_btn);
        manual.setOnClickListener(this);

        load = findViewById(R.id.load_btn);
        load.setOnClickListener(this);

        save = findViewById(R.id.save_btn);
        save.setOnClickListener(this);

        setting = findViewById(R.id.settings);
        setting.setOnClickListener(this);

        assistant_btn1 = findViewById(R.id.assist_giff1);
        assistant_btn1.setOnClickListener(this);

        up_click = findViewById(R.id.up_button);
        up_click.setOnClickListener(this);

        down_click = findViewById(R.id.down_button);
        down_click.setOnClickListener(this);

        web = findViewById(R.id.web_control);

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopup = new ImagePopup(OptionsActivity.this);
                String url = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("URL", null);
//                Toast.makeText(OptionsActivity.this, url, Toast.LENGTH_SHORT).show();
                try {
                    bitmap = textToImage(url, 500, 500);
                    d = new BitmapDrawable(getResources(), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                imagePopup.initiatePopup(d);
                imagePopup.setWindowHeight(600);
                imagePopup.setWindowWidth(600);
                imagePopup.viewPopup();
            }
        });


        stop = findViewById(R.id.stop_button);
        stop.setOnClickListener(this);
        stop.setBackgroundResource(R.drawable.stop);

        rec = findViewById(R.id.record_button);
        rec.setOnClickListener(this);
        rec.setBackgroundResource(R.drawable.rec);

        play = findViewById(R.id.play_button);
        play.setOnClickListener(this);
        play.setBackgroundResource(R.drawable.play);

        light = findViewById(R.id.Light);
        tilt = findViewById(R.id.Tilt);
        pan = findViewById(R.id.Pan);

        auto.setBackgroundResource(R.drawable.mybuttonenabled);
        manual.setBackgroundResource(R.drawable.mybuttondisabled);
        mode_val="auto";

        ImageCarousel carousel = findViewById(R.id.carousel);
        List<CarouselItem> list = new ArrayList<>();


        list.add(new CarouselItem("https://i.pinimg.com/originals/fc/ca/fa/fccafa6ce178ac8c1499abff6483a131.gif", "Preset 1" ) );
        list.add(new CarouselItem("https://steamuserimages-a.akamaihd.net/ugc/940560616858383897/06F769F83AF4D2C6822A50F46A19078D15F7C8A2/", "Preset 2" ) );
        list.add(new CarouselItem("https://cdn.dribbble.com/users/271641/screenshots/1531994/num3.gif", "Preset 3" ) );
        list.add(new CarouselItem("https://i.pinimg.com/originals/12/fc/a7/12fca7e81a560b6295e0e8410cf31dc9.gif", "Preset 4" ) );
        carousel.addData(list);

        // Scroll listener
        carousel.setOnScrollListener(new CarouselOnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                // ...
            }

            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState, int position, @Nullable CarouselItem carouselItem) {
                preset_no = position+1;
                save_clicked = false;
                load_clicked = false;
            }
        });

        light.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
            @Override
            public void onPointsChanged(BoxedVertical boxedPoints, final int value) {
                light_val = value;
            }

            @Override
            public void onStartTrackingTouch(BoxedVertical boxedPoints) {

            }

            @Override
            public void onStopTrackingTouch(BoxedVertical boxedPoints) {

            }

        });

        tilt.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
            @Override
            public void onPointsChanged(BoxedVertical boxedPoints, final int value) {
                tilt_val = value;
            }

            @Override
            public void onStartTrackingTouch(BoxedVertical boxedPoints) {
//                Toast.makeText(OptionsActivity.this, "Tracking Tilt", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(BoxedVertical boxedPoints) {
//                Toast.makeText(OptionsActivity.this, "Stopped Tracking Tilt", Toast.LENGTH_SHORT).show();
            }
        });

        pan.setOnBoxedPointsChangeListener(new BoxedVertical.OnValuesChangeListener() {
            @Override
            public void onPointsChanged(BoxedVertical boxedPoints, final int value) {
                pan_val = value;
            }

            @Override
            public void onStartTrackingTouch(BoxedVertical boxedPoints) {
//                Toast.makeText(OptionsActivity.this, "Tracking Tilt", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(BoxedVertical boxedPoints) {
//                Toast.makeText(OptionsActivity.this, "onStopTrackingTouch", Toast.LENGTH_SHORT).show();
            }
        });

        Kommunicate.init(this, "41f1f1dfc1fafb6145f2a4199c5f8591");
        KmPrefSettings.getInstance(OptionsActivity.this)
                .setFaqOptionEnabled(false)
                .enableSpeechToText(true) // if true, enables speech to text feature in the SDK.
                .enableTextToSpeech(true) // if true, enables text to speech feature. All the messages received will be spoken when the chat screen is open.
                .setSendMessageOnSpeechEnd(true); //if true, the speech will automatically be sent as a message without clicking the send message button.

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
            startActivity(new Intent(OptionsActivity.this, MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onClick(View V) {

        if (V.getId() == R.id.manual_btn) {
            mode_val="manual";
            auto.setBackgroundResource(R.drawable.mybuttondisabled);
            manual.setBackgroundResource(R.drawable.mybuttonenabled);
        }


        if (V.getId() == R.id.auto_btn) {
            mode_val="auto";
            auto.setBackgroundResource(R.drawable.mybuttonenabled);
            manual.setBackgroundResource(R.drawable.mybuttondisabled);
        }

        if (V.getId() == R.id.stop_button) {
            play_clicked = false;
            rec_clicked = false;
            stop_clicked = true;
        }

        if (V.getId() == R.id.play_button) {
            play_clicked = true;
            rec_clicked = false;
            stop_clicked = false;
        }

        if (V.getId() == R.id.record_button) {
            play_clicked = false;
            rec_clicked = true;
            stop_clicked = false;
        }

        if(V.getId() == R.id.load_btn){
            load_clicked = true;
            save_clicked= false;
        }

        if(V.getId() == R.id.save_btn){
            load_clicked=false;
            save_clicked=true;
        }

        if (V.getId() == R.id.up_button) {
            if(!up_flag) {
                height_val = 1;
                DrawableCompat.setTint(up_click.getDrawable(), ContextCompat.getColor(OptionsActivity.this, R.color.blue));
                DrawableCompat.setTint(down_click.getDrawable(), ContextCompat.getColor(OptionsActivity.this, R.color.gray));
                up_flag = true;
                down_flag = false;
            }else{
                height_val = 0;
                DrawableCompat.setTint(up_click.getDrawable(), ContextCompat.getColor(OptionsActivity.this, R.color.gray1));
                up_flag=false;
            }
        }

        if (V.getId() == R.id.down_button) {
            if(!down_flag) {
                height_val = -1;
                DrawableCompat.setTint(down_click.getDrawable(), ContextCompat.getColor(OptionsActivity.this, R.color.blue));
                DrawableCompat.setTint(up_click.getDrawable(), ContextCompat.getColor(OptionsActivity.this, R.color.gray));
                down_flag = true;
                up_flag=false;
            }else{
                height_val = 0;
                DrawableCompat.setTint(down_click.getDrawable(), ContextCompat.getColor(OptionsActivity.this, R.color.gray1));
                down_flag = false;
            }
        }

        if(V.getId()==R.id.settings){
            QRChange.getInstance().alertQRChange(OptionsActivity.this);
        }

        if(V.getId()==R.id.assist_giff1){
            List<String> botList = new ArrayList();
            botList.add("ripod-a9nsw"); //enter your integrated bot Ids

            new KmConversationBuilder(OptionsActivity.this)
                    .setConversationTitle("RiPod: Smart Agent")
                    .setSingleConversation(false) // Pass false if you would like to create new conversation every time user starts a conversation. This is true by default which means only one conversation will open for the user every time the user starts a conversation.
                    .launchConversation(new KmCallback() {
                        @Override
                        public void onSuccess(Object message) {
                            String conversationId = message.toString();
                        }

                        @Override
                        public void onFailure(Object error) {
                            Log.d("ConversationTest", "Error : " + error);
                        }
                    });

        }
    }

    @Override
    protected void init(NodeMainExecutor nodeMainExecutor) {
        //light
        controlTalker = new ModeTalker("android/control_val");
        NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
        nodeConfiguration.setMasterUri(getMasterUri());
        nodeMainExecutor.execute(controlTalker, nodeConfiguration);

    }

    public static int getLight() {
        return light_val;
    }

    public static int getHeight() {
        return height_val;
    }

    public static int getPan() {
        return pan_val;
    }

    public static int getTilt() {
        return tilt_val;
    }

    public static String getMode() {
        return mode_val;
    }

    public static String getVideoState() { return play_clicked + ":" + rec_clicked + ":" + stop_clicked; }

    public static String getPreset(){
        Boolean old_load = load_clicked;
        Boolean old_save = save_clicked;
        load_clicked = false;
        save_clicked= false;
        return old_load + ":" + old_save + ":" + preset_no;}

    private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
}