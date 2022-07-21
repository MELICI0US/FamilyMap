package com.example.familymap;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Models.Event;
import Models.Person;
import Models.User;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Responses.EventResponse;
import Responses.LoginResponse;
import Responses.PersonResponse;
import Responses.RegisterResponse;


public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private String usernameValue;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
        if (savedInstanceState != null) {
            usernameValue = savedInstanceState.getString("username", null);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", usernameValue);
    }

    //    private LoginViewModel getViewModel() {
//        return new ViewModelProvider(this).get(LoginViewModel.class);
//    }

    private Listener listener;

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    private static final String LOG_TAG = "MainActivity";
    private static final String SUCCESS_KEY = "SuccessKey";
    private static final String MESSAGE_KEY = "MessageKey";

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        final Button loginButton = view.findViewById(R.id.login);
        final Button registerButton = view.findViewById(R.id.register);
        final EditText host = view.findViewById(R.id.editServerHost);
        final EditText port = view.findViewById(R.id.editserverPort);
        final EditText username = view.findViewById(R.id.editUsername);
        final EditText password = view.findViewById(R.id.editPassword);
        final EditText firstName = view.findViewById(R.id.editFirstname);
        final EditText lastName = view.findViewById(R.id.editLastname);
        final EditText email = view.findViewById(R.id.editEmail);
        final RadioButton female = view.findViewById(R.id.fButton);
        final RadioButton male = view.findViewById(R.id.mButton);

//        username.setText(mViewModel.getUsername());
//        if (username != null) {
//            username.setText(usernameValue);
//        }

        username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    loginButton.setEnabled(true);
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                } else {
                    loginButton.setEnabled(false);
                }

                mViewModel.setUsername(username.getText().toString());
                usernameValue = username.getText().toString();

                return false;
            }
        });

        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    loginButton.setEnabled(true);
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                } else {
                    loginButton.setEnabled(false);
                }
                return false;
            }
        });

        port.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    loginButton.setEnabled(true);
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                } else {
                    loginButton.setEnabled(false);
                }
                return false;
            }
        });

        host.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    loginButton.setEnabled(true);
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                }
                return false;
            }
        });

        firstName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                }
                return false;
            }
        });

        lastName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                }
                return false;
            }
        });

        email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                }
                return false;
            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                }
            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!(username.getText().toString().equals("") || password.getText().toString().equals("") || host.getText().toString().equals("") || port.getText().toString().equals(""))) {
                    if (!(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("") || !(female.isChecked() || male.isChecked()))) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    // Set up a handler that will process messages from the task and make updates on the UI thread
                    @SuppressLint("HandlerLeak")
                    Handler uiThreadMessageHandler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            System.out.println("Handling message...");

                            Bundle bundle = message.getData();
                            boolean success = bundle.getBoolean(SUCCESS_KEY);
                            String mess = bundle.getString(MESSAGE_KEY);

                            progressBar.setIndeterminate(false);
                            if (success) {
                                Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
                                listener.notifyDone();
                            } else {
                                Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    progressBar.setIndeterminate(true);
                    // Create and execute the download task on a separate thread
                    //10.0.2.2
                    String url = "http://" + host.getText().toString() + ":" + port.getText().toString() + "/user/login";
                    //System.out.println("URL: " + url);

                    LoginRequest request = new LoginRequest(username.getText().toString(), password.getText().toString());

                    LoginTask task = new LoginTask(uiThreadMessageHandler, new URL(url), request);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    Toast.makeText(getActivity(), "Bad Address", Toast.LENGTH_SHORT).show();
                }
//                if(listener != null) {
//                    listener.notifyDone();
//                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    // Set up a handler that will process messages from the task and make updates on the UI thread
                    @SuppressLint("HandlerLeak")
                    Handler uiThreadMessageHandler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            System.out.println("Handling message...");

                            Bundle bundle = message.getData();
                            boolean success = bundle.getBoolean(SUCCESS_KEY);
                            String mess = bundle.getString(MESSAGE_KEY);

                            progressBar.setIndeterminate(false);
                            if (success) {
                                Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
                                listener.notifyDone();
                            } else {
                                Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    progressBar.setIndeterminate(true);
                    // Create and execute the download task on a separate thread
                    //10.0.2.2
                    String url = "http://" + host.getText().toString() + ":" + port.getText().toString() + "/user/register";
                    //System.out.println("URL: " + url);
                    String gender;
                    if (female.isChecked()) {
                        gender = "f";
                    } else {
                        gender = "m";
                    }

                    RegisterRequest request = new RegisterRequest(username.getText().toString(), password.getText().toString(), email.getText().toString(), firstName.getText().toString(), lastName.getText().toString(), gender);
                    RegisterTask task = new RegisterTask(uiThreadMessageHandler, new URL(url), request);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
//                if(listener != null) {
//                    listener.notifyDone();
//                }
            }
        });

        return view;
    }


    private static class LoginTask implements Runnable {

        private final Handler messageHandler;

        private final URL url;

        private final LoginRequest request;

        public LoginTask(Handler messageHandler, URL url, LoginRequest request) {
            this.messageHandler =
                    messageHandler;
            this.url = url;
            this.request = request;
        }

        @Override
        public void run() {
            System.out.println("Running...");

            ServerProxy serverProxy = new ServerProxy();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(request, LoginRequest.class);

            String urlContent = serverProxy.postUrl(url, json);
            if (urlContent != null) {
                gson = new Gson();
                LoginResponse response = gson.fromJson(urlContent, LoginResponse.class);
                if (response.getSuccess()) {
                    DataCache cache = new DataCache();
                    String userId = response.getPersonID();
                    cache.setUserId(userId);

                    String urlString = url.toString();
                    String baseUrl = urlString.substring(0, urlString.length() - 11);
                    DataTask task = new DataTask(messageHandler, baseUrl, response.getAuthtoken());
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                } else {
                    sendMessage(false, response.getMessage());
                }
            } else {
                sendMessage(false, "No URL content");
            }

        }

        private void sendMessage(boolean success, String mess) {
            System.out.println("Sending message...");
            System.out.println("message: " + mess);
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(SUCCESS_KEY, success);
            messageBundle.putString(MESSAGE_KEY, mess);

            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }

    private static class RegisterTask implements Runnable {

        private final Handler messageHandler;

        private final URL url;

        private final RegisterRequest request;

        public RegisterTask(Handler messageHandler, URL url, RegisterRequest request) {
            this.messageHandler =
                    messageHandler;
            this.url = url;
            this.request = request;
        }

        @Override
        public void run() {
            System.out.println("Running...");

            ServerProxy serverProxy = new ServerProxy();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(request, RegisterRequest.class);

            String urlContent = serverProxy.postUrl(url, json);
            if (urlContent != null) {
                gson = new Gson();
                RegisterResponse response = gson.fromJson(urlContent, RegisterResponse.class);
                if (response.getSuccess()) {
                    DataCache cache = new DataCache();
                    String userId = response.getPersonID();
                    cache.setUserId(userId);

                    String urlString = url.toString();
                    String baseUrl = urlString.substring(0, urlString.length() - 14);
                    DataTask task = new DataTask(messageHandler, baseUrl, response.getAuthtoken());
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                } else {
                    sendMessage(false, response.getMessage());
                }
            } else {
                sendMessage(false, "No URL content");
            }

        }

        private void sendMessage(boolean success, String mess) {
            System.out.println("Sending message...");
            System.out.println("message: " + mess);
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(SUCCESS_KEY, success);
            messageBundle.putString(MESSAGE_KEY, mess);

            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }

    private static class DataTask implements Runnable {
        private final Handler messageHandler;

        private final String baseUrl;

        private final String authToken;

        public DataTask(Handler messageHandler, String baseUrl, String authToken) {
            this.messageHandler = messageHandler;
            this.baseUrl = baseUrl;
            this.authToken = authToken;
        }

        @Override
        public void run() {
            System.out.println("Running...");
            System.out.println("BaseURL: " + baseUrl);

            ServerProxy serverProxy = new ServerProxy();
            DataCache cache = new DataCache();

            Gson gson = new Gson();
            try {
                URL url = new URL(baseUrl + "/person");

                String urlContent = serverProxy.getData(url, authToken);
                if (urlContent != null) {
                    PersonResponse response = gson.fromJson(urlContent, PersonResponse.class);
                    if (response.getSuccess()) {
                        System.out.println("Getting persons...");
                        ArrayList<Person> personList = response.getData();
                        Map<String, Person> persons = new HashMap<>();

                        for (Person person : personList) {
                            persons.put(person.getPersonID(), person);
                        }

                        cache.setPersons(persons);
                    } else {
                        sendMessage(false, response.getMessage());
                    }
                } else {
                    sendMessage(false, "No URL content");
                }

                url = new URL(baseUrl + "/event");

                urlContent = serverProxy.getData(url, authToken);
                if (urlContent != null) {
                    EventResponse response = gson.fromJson(urlContent, EventResponse.class);
                    if (response.getSuccess()) {
                        System.out.println("Getting events...");
                        ArrayList<Event> eventList = response.getData();
                        Map<String, Event> events = new HashMap<>();

                        for (Event event : eventList) {
                            events.put(event.getEventID(), event);
                        }

                        cache.setEvents(events);
                        Person user = cache.getPerson(cache.getUserId());
                        sendMessage(true, "Welcome " + user.getFirstName() + " " + user.getLastName());
                    } else {
                        sendMessage(false, response.getMessage());
                    }
                } else {
                    sendMessage(false, "No URL content");
                }
            } catch (MalformedURLException ex) {
                sendMessage(false, "Malformed URL");
            }

        }

        private void sendMessage(boolean success, String mess) {
            System.out.println("Sending message...");
            System.out.println("message: " + mess);
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(SUCCESS_KEY, success);
            messageBundle.putString(MESSAGE_KEY, mess);

            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }

}

