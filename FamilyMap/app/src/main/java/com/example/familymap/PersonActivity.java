package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import Models.Event;
import Models.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersonActivity extends AppCompatActivity {

    public static final String PERSON_ID_KEY = "personKey";
    public static final String EVENT_ID_KEY = "eventKey";

    private DataCache cache = new DataCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        DataCache cache = new DataCache();

        Intent intent = getIntent();
        String personId = intent.getStringExtra(PERSON_ID_KEY);
        List<Event> lifeEventsUnfiltered = cache.getEventsByPerson().get(personId);
        List<Event> lifeEvents = new ArrayList<>();
        for (Event event: lifeEventsUnfiltered){
            if (cache.getFilteredEvents().containsKey(event.getEventID())){
                lifeEvents.add(event);
            }
        }

        Map<String, String> family = cache.getFamilyRelations(personId);

        TextView firstName = findViewById(R.id.firstName);
        TextView lastName = findViewById(R.id.lastName);
        TextView gender = findViewById(R.id.gender);

        Person person = cache.getPerson(personId);
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());

        if (person.getGender().equals("f")) {
            gender.setText("Female");
        } else {
            gender.setText("Male");
        }

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new ExpandableListAdapter(lifeEvents, family));
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int LIFE_EVENTS_POSITION = 0;
        private static final int FAMILY_POSITION = 1;

        private final List<Event> lifeEvents;
        private final List<String> family;
        private final Map<String, String> familyMapped;

        ExpandableListAdapter(List<Event> lifeEvents, Map<String, String> family) {
            this.lifeEvents = lifeEvents;
            List<String> keys = new ArrayList<>(family.keySet());
            this.family = keys;
            this.familyMapped = family;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    return lifeEvents.size();
                case FAMILY_POSITION:
                    return family.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    return getString(R.string.lifeEventsTitle);
                case FAMILY_POSITION:
                    return getString(R.string.familyTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_POSITION:
                    return family.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    titleView.setText(R.string.lifeEventsTitle);
                    break;
                case FAMILY_POSITION:
                    titleView.setText(R.string.familyTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case LIFE_EVENTS_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                    initializeLifeEventsView(itemView, childPosition);
                    break;
                case FAMILY_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeLifeEventsView(View lifeEventsView, final int childPosition) {
            ImageView imageView = lifeEventsView.findViewById(R.id.genderImage);
            Drawable icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.blackColor).sizeDp(40);
            imageView.setImageDrawable(icon);

            TextView info = lifeEventsView.findViewById(R.id.info);
            Event event = lifeEvents.get(childPosition);
            String eventTitle = cache.getPersons().get(event.getPersonID()).getFirstName() + " " + cache.getPersons().get(event.getPersonID()).getLastName() + ": " + event.getEventType().toUpperCase();
            info.setText(eventTitle);

            TextView description = lifeEventsView.findViewById(R.id.description);
            String eventDetails = event.getCity() + ", " + event.getCountry() + " ";
            eventDetails += event.getYear();
            description.setText(eventDetails);

            lifeEventsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra(PersonActivity.EVENT_ID_KEY, lifeEvents.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializeFamilyView(View familyView, final int childPosition) {
            final Person person = cache.getPerson(family.get(childPosition));

            ImageView imageView = familyView.findViewById(R.id.genderImage);
            Drawable genderIcon;
            if (person.getGender().equals("f")) {
                genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            } else {
                genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            }

            imageView.setImageDrawable(genderIcon);

            String personName = person.getFirstName() + " " + person.getLastName();

            TextView info = familyView.findViewById(R.id.info);
            info.setText(personName);

            TextView description = familyView.findViewById(R.id.description);
            String relation = familyMapped.get(family.get(childPosition));
            description.setText(relation);

            familyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra(PersonActivity.PERSON_ID_KEY, family.get(childPosition));
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
