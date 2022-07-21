package com.example.familymap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.zip.Inflater;

public class SettingsActivity extends AppCompatActivity {

    public static final String lifeStoryLines_KEY = "lifeStoryLinesKey";
    public static final String familyTreeLines_KEY = "familyTreeLinesKey";
    public static final String spouseLines_KEY = "spouseLinesKey";
    public static final String fathersSide_KEY = "fathersSideKey";
    public static final String mothersSide_KEY = "mothersSideKey";
    public static final String maleEvents_KEY = "maleEventsKey";
    public static final String femaleEvents_KEY = "femaleEventsKey";
    public static final String CHANGED_KEY = "changedKey";

    private boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Settings settings = new Settings();

        final Switch lifeStoryLinesSwitch = findViewById(R.id.lifeStoryLinesSwitch);
        final Switch familyTreeLinesSwitch = findViewById(R.id.familyTreeLinesSwitch);
        final Switch spouseLinesSwitch = findViewById(R.id.spouseLinesSwitch);
        final Switch fathersSideSwitch = findViewById(R.id.fathersSideSwitch);
        final Switch mothersSideSwitch = findViewById(R.id.mothersSideSwitch);
        final Switch maleEventsSwitch = findViewById(R.id.maleEventsSwitch);
        final Switch femaleEventsSwitch = findViewById(R.id.femaleEventsSwitch);

        Intent intent = new Intent(this, MainActivity.class);

        lifeStoryLinesSwitch.setChecked(settings.isLifeStoryLines());
        familyTreeLinesSwitch.setChecked(settings.isFamilyTreeLines());
        spouseLinesSwitch.setChecked(settings.isSpouseLines());
        fathersSideSwitch.setChecked(settings.isFathersSide());
        mothersSideSwitch.setChecked(settings.isMothersSide());
        maleEventsSwitch.setChecked(settings.isMaleEvents());
        femaleEventsSwitch.setChecked(settings.isFemaleEvents());

        lifeStoryLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setLifeStoryLines(lifeStoryLinesSwitch.isChecked());
                changed = true;
            }
        });

        familyTreeLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setFamilyTreeLines(familyTreeLinesSwitch.isChecked());
                changed = true;

            }
        });

        spouseLinesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setSpouseLines(spouseLinesSwitch.isChecked());
                changed = true;
            }
        });

        fathersSideSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setFathersSide(fathersSideSwitch.isChecked());
                changed = true;
            }
        });


        mothersSideSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setMothersSide(mothersSideSwitch.isChecked());
                changed = true;
            }
        });

        maleEventsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setMaleEvents(maleEventsSwitch.isChecked());
                changed = true;
            }
        });

        femaleEventsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setFemaleEvents(femaleEventsSwitch.isChecked());
                changed = true;
            }
        });

        LinearLayout logout = findViewById(R.id.logoutBox);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);

                DataCache cache = new DataCache();
                cache.clear();

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            intent.putExtra(CHANGED_KEY, changed);
            startActivity(intent);
        }
        return true;
    }

}