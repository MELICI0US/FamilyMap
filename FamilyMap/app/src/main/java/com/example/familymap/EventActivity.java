package com.example.familymap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Event;
import Models.Person;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private DataCache cache = new DataCache();
    View view;
    List<Polyline> lines = new ArrayList<>();
    private Settings settings = new Settings();
    private Event selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        selected = cache.getEvent(intent.getStringExtra(PersonActivity.EVENT_ID_KEY));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        drawEvents();
        drawLines(selected);
        fillBox(selected);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Event event = (Event) marker.getTag();
                TextView eventDetailsBox = findViewById(R.id.mapTextView);
                Drawable genderIcon;
                ImageView genderImageView = findViewById(R.id.genderImage);

                String eventDetails = cache.getPersons().get(event.getPersonID()).getFirstName() + " " + cache.getPersons().get(event.getPersonID()).getLastName() + "'s " + event.getEventType() + "\n";
                eventDetails += event.getCity() + ", " + event.getCountry() + "\n";
                eventDetails += event.getYear();

                eventDetailsBox.setText(eventDetails);

                if (cache.getPersons().get(event.getPersonID()).getGender().equals("f")) {
                    genderIcon = new IconDrawable(EventActivity.this, FontAwesomeIcons.fa_female).
                            colorRes(R.color.female_icon).sizeDp(40);
                } else {
                    genderIcon = new IconDrawable(EventActivity.this, FontAwesomeIcons.fa_male).
                            colorRes(R.color.male_icon).sizeDp(40);
                }

                genderImageView.setImageDrawable(genderIcon);

                for (Polyline line : lines) {
                    line.remove();
                }

                drawLines(event);
                selected = event;

                LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
                map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));

                return true;
            }
        });

        LinearLayout eventInfo = findViewById(R.id.eventInfoDisplay);
        eventInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_ID_KEY, selected.getPersonID());
                startActivity(intent);
            }
        });
    }

    private void fillBox(Event event) {
        TextView eventDetailsBox = findViewById(R.id.mapTextView);
        Drawable genderIcon;
        ImageView genderImageView = findViewById(R.id.genderImage);

        String eventDetails = cache.getPersons().get(event.getPersonID()).getFirstName() + " " + cache.getPersons().get(event.getPersonID()).getLastName() + "'s " + event.getEventType() + "\n";
        eventDetails += event.getCity() + ", " + event.getCountry() + "\n";
        eventDetails += event.getYear();

        eventDetailsBox.setText(eventDetails);

        if (cache.getPersons().get(event.getPersonID()).getGender().equals("f")) {
            genderIcon = new IconDrawable(EventActivity.this, FontAwesomeIcons.fa_female).
                    colorRes(R.color.female_icon).sizeDp(40);
        } else {
            genderIcon = new IconDrawable(EventActivity.this, FontAwesomeIcons.fa_male).
                    colorRes(R.color.male_icon).sizeDp(40);
        }

        genderImageView.setImageDrawable(genderIcon);
    }

    private void drawEvents() {
        Map colors = new HashMap<String, Float>();
        Float[] colorChoices = {BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_ROSE, BitmapDescriptorFactory.HUE_VIOLET};
        int colorCounter = 0;

        map.clear();

        for (Event event : cache.getFilteredEvents().values()) {
            if (!settings.isMaleEvents() && cache.getPerson(event.getPersonID()).getGender().equals("m")) {
                continue;
            }
            if (!settings.isFemaleEvents() && cache.getPerson(event.getPersonID()).getGender().equals("f")) {
                continue;
            }
            Float googleColor;
            if (colors.containsKey(event.getEventType().toLowerCase())) {
                googleColor = (Float) colors.get(event.getEventType().toLowerCase());
            } else {
                googleColor = colorChoices[colorCounter];
                colors.put(event.getEventType().toLowerCase(), googleColor);
                if (colorCounter < colorChoices.length - 1) {
                    colorCounter++;
                } else {
                    colorCounter = 0;
                }
            }
            LatLng eventLocation = new LatLng(event.getLatitude(), event.getLongitude());
            Marker marker = map.addMarker(new MarkerOptions().position(eventLocation).title(cache.getPersons().get(event.getPersonID()).getFirstName() + " " + cache.getPersons().get(event.getPersonID()).getLastName() + ": " + event.getEventType().toUpperCase()).icon(BitmapDescriptorFactory.defaultMarker(googleColor)));
            marker.setTag(event);

        }
        LatLng eventLocation = new LatLng(selected.getLatitude(), selected.getLongitude());

        map.animateCamera(CameraUpdateFactory.newLatLng(eventLocation));
    }

    private void drawLines(Event selectedEvent) {
        if (settings.isSpouseLines()) {
            drawSpouseLines(selectedEvent);
        }
        if (settings.isFamilyTreeLines()) {
            drawFamilyTreeLines(selectedEvent);
        }
        if (settings.isLifeStoryLines()) {
            drawLifeStoryLines(selectedEvent);
        }
    }

    private void drawSpouseLines(Event selectedEvent) {
        Person A = cache.getPerson(selectedEvent.getPersonID());
        Person B = cache.getPerson(A.getSpouseID());

        if (B == null) {
            return;
        }

        Event evB = cache.getEventsByPerson().get(B.getPersonID()).get(0);
        if (cache.getFilteredEvents().containsKey(evB.getEventID())) {
            LatLng start = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            LatLng stop = new LatLng(evB.getLatitude(), evB.getLongitude());

            PolylineOptions options = new PolylineOptions().add(start).add(stop).color(Color.BLUE).width(5);
            Polyline line = map.addPolyline(options);
            lines.add(line);
        }
    }

    private void drawFamilyTreeLines(Event selectedEvent) {
        drawToParents(selectedEvent, 20);
    }

    private void drawToParents(Event selectedEvent, float width) {
        Person person = cache.getPerson(selectedEvent.getPersonID());
        Map<String, List<Event>> eventsByPerson = cache.getEventsByPerson();

        if (person == null) {
            return;
        }

        LatLng personLocation = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());

        String mom = person.getMotherID();
        String dad = person.getFatherID();

        if (eventsByPerson.containsKey(mom)) {
            List<Event> momEvents = eventsByPerson.get(mom);
            Event momEvent = momEvents.get(0);
            if (cache.getFilteredEvents().containsKey(momEvent.getEventID())) {
                LatLng momLocation = new LatLng(momEvent.getLatitude(), momEvent.getLongitude());

                PolylineOptions options = new PolylineOptions().add(personLocation).add(momLocation).color(Color.RED).width(width);
                Polyline line = map.addPolyline(options);
                lines.add(line);

                drawToParents(momEvent, width - (width * .5f));
            }
        }

        if (eventsByPerson.containsKey(dad)) {
            List<Event> dadEvents = eventsByPerson.get(dad);
            Event dadEvent = dadEvents.get(0);
            if (cache.getFilteredEvents().containsKey(dadEvent.getEventID())) {
                LatLng dadLocation = new LatLng(dadEvent.getLatitude(), dadEvent.getLongitude());

                PolylineOptions options = new PolylineOptions().add(personLocation).add(dadLocation).color(Color.RED).width(width);
                Polyline line = map.addPolyline(options);
                lines.add(line);

                drawToParents(dadEvent, width - (width * .5f));
            }
        }
    }

    private void drawLifeStoryLines(Event selectedEvent) {
        Map<String, List<Event>> eventsByPerson = cache.getEventsByPerson();
        List<Event> eventList = eventsByPerson.get(selectedEvent.getPersonID());

        if (eventList.size() < 2) {
            return;
        }

        Event previousEvent = eventList.get(0);
        for (Event event : eventList) {
            LatLng start = new LatLng(previousEvent.getLatitude(), previousEvent.getLongitude());
            LatLng stop = new LatLng(event.getLatitude(), event.getLongitude());

            previousEvent = event;

            PolylineOptions options = new PolylineOptions().add(start).add(stop).color(Color.MAGENTA).width(5);
            Polyline line = map.addPolyline(options);
            lines.add(line);
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

    @Override
    public void onResume() {
        super.onResume();

        if (map != null) {
            drawEvents();
        }
    }
}
