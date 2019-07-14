package basecode.com.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import basecode.com.domain.model.epub.SkySetting;
import basecode.com.ui.features.readbook.SkyDatabase;

public class SettingActivity extends Activity {
    public CheckBox doublePagedCheckBox;
    public CheckBox lockRotationCheckBox;
    public CheckBox globalPaginationCheckBox;

    public Button themeWhiteButton;
    public Button themeBrownButton;
    public Button themeBlackButton;

    public ImageView themeWhiteImageView;
    public ImageView themeBrownImageView;
    public ImageView themeBlackImageView;

    public RadioGroup pageTransitionGroup;

    public CheckBox mediaOverlayCheckBox;
    public CheckBox ttsCheckBox;
    public CheckBox autoStartPlayingCheckBox;
    public CheckBox autoLoadNewChapterCheckBox;
    public CheckBox highlightTextToVoiceCheckBox;
    public AppCompatImageView ivBackSetting;


    public TextView SkyEpub;
    public SkyDatabase sd;
    public SkySetting setting;

    public void showMessageBox(String title, String messgage) {
        AlertDialog ad = new AlertDialog.Builder(this).create();
        ad.setCancelable(false); // This blocks the 'BACK' button
        ad.setTitle(title);
        ad.setMessage(messgage);
        ad.setButton(getString(R.string.ACTION_OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        ad.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sd = new SkyDatabase(this);
        setting = sd.fetchSetting();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        doublePagedCheckBox = (CheckBox) this.findViewById(R.id.doublePagedCheckBox);
        lockRotationCheckBox = (CheckBox) this.findViewById(R.id.lockRotationCheckBox);
        globalPaginationCheckBox = (CheckBox) this.findViewById(R.id.globalPaginationCheckBox);

        mediaOverlayCheckBox = (CheckBox) this.findViewById(R.id.mediaOverlayCheckBox);
        ttsCheckBox = (CheckBox) this.findViewById(R.id.ttsCheckBox);
        autoStartPlayingCheckBox = (CheckBox) this.findViewById(R.id.autoStartPlayingCheckBox);
        autoLoadNewChapterCheckBox = (CheckBox) this.findViewById(R.id.autoLoadNewChapterCheckBox);
        highlightTextToVoiceCheckBox = (CheckBox) this.findViewById(R.id.highlightTextToVoiceCheckBox);
        ivBackSetting = this.findViewById(R.id.ivBackSetting);


        globalPaginationCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {
                    showMessageBox(getString(R.string.warning), getString(R.string.globalpaginationwarning));
                }
            }
        });

        themeWhiteButton = (Button) this.findViewById(R.id.themeWhiteButton);
        themeWhiteButton.setOnClickListener(onClickListener);
        themeBrownButton = (Button) this.findViewById(R.id.themeBrownButton);
        themeBrownButton.setOnClickListener(onClickListener);
        themeBlackButton = (Button) this.findViewById(R.id.themeBlackButton);
        themeBlackButton.setOnClickListener(onClickListener);

        themeWhiteImageView = (ImageView) this.findViewById(R.id.themeWhiteImageView);
        themeWhiteImageView.setScaleType(ScaleType.FIT_CENTER);
        themeWhiteImageView.setAdjustViewBounds(true);

        themeBrownImageView = (ImageView) this.findViewById(R.id.themeBrownImageView);
        themeBrownImageView.setScaleType(ScaleType.FIT_CENTER);
        themeBrownImageView.setAdjustViewBounds(true);

        themeBlackImageView = (ImageView) this.findViewById(R.id.themeBlackImageView);
        themeBlackImageView.setScaleType(ScaleType.FIT_CENTER);
        themeBlackImageView.setAdjustViewBounds(true);

        ivBackSetting.setOnClickListener(onClickListener);

        SkyEpub = (TextView) this.findViewById(R.id.skyepubTextView);
        SkyEpub.setOnClickListener(onClickListener);


        pageTransitionGroup = (RadioGroup) this.findViewById(R.id.pageTransitionGroup);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void markTheme(int themeIndex) {
        int markColor = 0xAABFFF00;
        themeWhiteImageView.setBackgroundColor(Color.TRANSPARENT);
        themeBrownImageView.setBackgroundColor(Color.TRANSPARENT);
        themeBlackImageView.setBackgroundColor(Color.TRANSPARENT);

        if (themeIndex == 0) {
            themeWhiteImageView.setBackgroundColor(markColor);
        } else if (themeIndex == 1) {
            themeBrownImageView.setBackgroundColor(markColor);
        } else {
            themeBlackImageView.setBackgroundColor(markColor);
        }
    }

    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(View arg) {
            if (arg == SkyEpub) {
                String url = "http://skyepub.net/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                return;
            }

            if(arg == themeWhiteButton){
                setting.theme = 0;
                markTheme(setting.theme);
                return;
            }

            if(arg == themeBrownButton){
                setting.theme = 1;
                markTheme(setting.theme);
                return;
            }

            if(arg == themeBlackButton){
                setting.theme = 2;
                markTheme(setting.theme);
                return;
            }

            if (arg == ivBackSetting) {
                finish();
            }
        }
    };

    public void loadValues() {
        doublePagedCheckBox.setChecked(setting.doublePaged);
        lockRotationCheckBox.setChecked(setting.lockRotation);
        globalPaginationCheckBox.setChecked(setting.globalPagination);

        mediaOverlayCheckBox.setChecked(setting.mediaOverlay);
        ttsCheckBox.setChecked(setting.tts);
        autoStartPlayingCheckBox.setChecked(setting.autoStartPlaying);
        autoLoadNewChapterCheckBox.setChecked(setting.autoLoadNewChapter);
        highlightTextToVoiceCheckBox.setChecked(setting.highlightTextToVoice);

        int index = setting.transitionType;
        if (index == 0) pageTransitionGroup.check(R.id.noneRadio);
        else if (index == 1) pageTransitionGroup.check(R.id.slideRadio);
        else pageTransitionGroup.check(R.id.curlRadio);

        markTheme(setting.theme);
    }

    public void saveValues() {
        setting.doublePaged = doublePagedCheckBox.isChecked();
        setting.lockRotation = lockRotationCheckBox.isChecked();
        setting.globalPagination = globalPaginationCheckBox.isChecked();

        setting.mediaOverlay = mediaOverlayCheckBox.isChecked();
        setting.tts = ttsCheckBox.isChecked();
        setting.autoStartPlaying = autoStartPlayingCheckBox.isChecked();
        setting.autoLoadNewChapter = autoLoadNewChapterCheckBox.isChecked();
        setting.highlightTextToVoice = highlightTextToVoiceCheckBox.isChecked();

        int index = pageTransitionGroup.indexOfChild(findViewById(pageTransitionGroup.getCheckedRadioButtonId()));
        setting.transitionType = index;
    }

    @Override
    public void onResume() {
        super.onResume();
        setting = sd.fetchSetting();
        loadValues();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveValues();
        sd.updateSetting(this.setting);
    }

}
