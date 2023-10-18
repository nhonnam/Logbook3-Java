package com.example.logbook3.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logbook3.Database.AppDatabase;
import com.example.logbook3.Models.User;
import com.example.logbook3.R;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    Button saveButton, viewButton;
    EditText nameText, emailText;
    RadioGroup imageRadio;
    TextView dobText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        dobText = findViewById(R.id.date_picker);
        imageRadio = findViewById(R.id.radioGroup);
        saveButton = findViewById(R.id.saveDetailsButton);
        viewButton = findViewById(R.id.viewDetailsButton);

        dobText.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "datePicker");
        });
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "users")
                .allowMainThreadQueries()
                .build();
        saveButton.setOnClickListener(view -> saveUsers());
        viewButton.setOnClickListener(view -> viewUsers());
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int day = d.getDayOfMonth();
            int month = d.getMonthValue();
            int year = d.getYear();
            return new DatePickerDialog(getActivity(), this, year, --month, day);}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate dob = LocalDate.of(year, ++month, day);
            ((MainActivity)getActivity()).updateDateOfBirth(dob);
        }
    }

    public void updateDateOfBirth(LocalDate dob){
        dobText.setText(dob.toString());
    }

    private void viewUsers(){
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    private void saveUsers() {
        String name = nameText.getText().toString().trim();
        String dob = dobText.getText().toString().trim();
        String email = emailText.getText().toString().trim();
        int selectedRadioButtonId = imageRadio.getCheckedRadioButtonId();

        if (name.isEmpty() || dob.isEmpty() || email.isEmpty() || selectedRadioButtonId == -1){
            Toast.makeText(this, "Please fill all fields.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        if (!email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")){
            Toast.makeText(this, "Please enter a valid email.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String imageTag = (String) selectedRadioButton.getTag();
        String packageName = getPackageName();
        int image = getResources().getIdentifier(imageTag, "drawable", packageName);

        User user = new User();
        user.name = name;
        user.dob = dob;
        user.email = email;
        user.image = image;

        appDatabase.userDao().insertUser(user);
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("New Contact is saved")
                .setNeutralButton("Ok", (dialogInterface, i) -> {
                })
                .show();
    }
}