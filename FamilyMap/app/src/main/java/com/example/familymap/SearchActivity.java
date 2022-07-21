package com.example.familymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import Models.Event;
import Models.Person;

public class SearchActivity extends AppCompatActivity {

    private static final int PERSON_RESULT_VIEW_TYPE = 0;
    private static final int EVENT_RESULT_VIEW_TYPE = 1;
    private DataCache cache = new DataCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));


        final SearchView search = findViewById(R.id.searchView);
        search.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        List<List<String>> searchResults = cache.search(s);
                        List<String> personResults = searchResults.get(0);
                        List<String> eventResults =  searchResults.get(1);

                        SearchAdapter adapter = new SearchAdapter(personResults, eventResults);
                        recyclerView.setAdapter(adapter);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                }
        );
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<String> personResults;
        private final List<String> eventResults;

        SearchAdapter(List<String> personResults, List<String> eventResults) {
            this.personResults = personResults;
            this.eventResults = eventResults;
        }

        @Override
        public int getItemViewType(int position) {
            return position < personResults.size() ? PERSON_RESULT_VIEW_TYPE : EVENT_RESULT_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            view = getLayoutInflater().inflate(R.layout.list_item, parent, false);

            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < personResults.size()) {
                holder.bindPerson(personResults.get(position));
            } else {
                holder.bindEvent(eventResults.get(position - personResults.size()));
            }
        }

        @Override
        public int getItemCount() {
            return personResults.size() + eventResults.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView info;
        private final TextView description;
        private final ImageView icon;

        private final int viewType;
        private String person;
        private String event;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            info = itemView.findViewById(R.id.info);
            description = itemView.findViewById(R.id.description);
            icon = itemView.findViewById(R.id.genderImage);
        }

        private void bindPerson(String person) {
            this.person = person;
            Person personObj = cache.getPerson(person);
            info.setText(personObj.getFirstName() + " " + personObj.getLastName());

            Drawable genderIcon;
            if (personObj.getGender().equals("f")) {
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(40);
            } else {
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(40);
            }
            icon.setImageDrawable(genderIcon);
        }

        private void bindEvent(String event) {
            this.event = event;
            Event eventObj = cache.getEvent(event);
            String eventTitle = cache.getPerson(eventObj.getPersonID()).getFirstName() + " " + cache.getPerson(eventObj.getPersonID()).getLastName() + ": " + eventObj.getEventType().toUpperCase();
            info.setText(eventTitle);

            String eventDetails = eventObj.getCity() + ", " + eventObj.getCountry() + " ";
            eventDetails += eventObj.getYear();
            description.setText(eventDetails);

            Drawable image = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.blackColor).sizeDp(40);
            icon.setImageDrawable(image);
        }

        @Override
        public void onClick(View view) {
            if (viewType == EVENT_RESULT_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(PersonActivity.EVENT_ID_KEY, event);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(PersonActivity.PERSON_ID_KEY, person);
                startActivity(intent);
            }
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
