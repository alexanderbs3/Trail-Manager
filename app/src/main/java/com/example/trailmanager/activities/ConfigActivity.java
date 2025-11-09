package com.example.trailmanager.activities;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trailmanager.R;
import com.example.trailmanager.models.UserConfig;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ConfigActivity extends AppCompatActivity {

    private EditText etWeight;
    private EditText etHeight;
    private RadioGroup rgGender;
    private MaterialRadioButton rbMale;
    private MaterialRadioButton rbFemale;
    private Button btnBirthdate;
    private RadioGroup rgMapType;
    private MaterialRadioButton rbMapNormal;
    private MaterialRadioButton rbMapSatellite;
    private RadioGroup rgNavMode;
    private MaterialRadioButton rbNavNorthUp;
    private MaterialRadioButton rbNavCourseUp;
    private Button btnSave;

    private UserConfig userConfig;
    private Calendar selectedBirthdate;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Configurações");
        }

        userConfig = new UserConfig(this);
        selectedBirthdate = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        initViews();
        loadSavedConfig();
        setupListeners();
    }

    private void initViews() {
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnBirthdate = findViewById(R.id.btnBirthdate);
        rgMapType = findViewById(R.id.rgMapType);
        rbMapNormal = findViewById(R.id.rbMapNormal);
        rbMapSatellite = findViewById(R.id.rbMapSatellite);
        rgNavMode = findViewById(R.id.rgNavMode);
        rbNavNorthUp = findViewById(R.id.rbNavNorthUp);
        rbNavCourseUp = findViewById(R.id.rbNavCourseUp);
        btnSave = findViewById(R.id.btnSave);
    }

    private void loadSavedConfig() {
        // Dados do usuário
        float weight = userConfig.getWeight();
        if (weight > 0) {
            etWeight.setText(String.valueOf(weight));
        }

        float height = userConfig.getHeight();
        if (height > 0) {
            etHeight.setText(String.valueOf(height));
        }

        String gender = userConfig.getGender();
        if (gender.equals(UserConfig.GENDER_MALE)) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }

        long birthdate = userConfig.getBirthdate();
        if (birthdate > 0) {
            selectedBirthdate.setTimeInMillis(birthdate);
            updateBirthdateButton();
        }

        // Tipo de mapa
        int mapType = userConfig.getMapType();
        if (mapType == UserConfig.MAP_TYPE_SATELLITE) {
            rbMapSatellite.setChecked(true);
        } else {
            rbMapNormal.setChecked(true);
        }

        // Modo de navegação
        String navMode = userConfig.getNavigationMode();
        if (navMode.equals(UserConfig.NAV_MODE_COURSE_UP)) {
            rbNavCourseUp.setChecked(true);
        } else {
            rbNavNorthUp.setChecked(true);
        }
    }

    private void setupListeners() {
        btnBirthdate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveConfiguration());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedBirthdate.set(Calendar.YEAR, year);
                    selectedBirthdate.set(Calendar.MONTH, month);
                    selectedBirthdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateBirthdateButton();
                },
                selectedBirthdate.get(Calendar.YEAR),
                selectedBirthdate.get(Calendar.MONTH),
                selectedBirthdate.get(Calendar.DAY_OF_MONTH)
        );

        // Limitar data máxima para hoje
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        // Limitar data mínima (120 anos atrás)
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.YEAR, -120);
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());

        datePickerDialog.show();
    }

    private void updateBirthdateButton() {
        String formattedDate = dateFormat.format(selectedBirthdate.getTime());
        btnBirthdate.setText(formattedDate);
    }

    private void saveConfiguration() {
        // Validar peso
        String weightStr = etWeight.getText().toString().trim();
        if (weightStr.isEmpty()) {
            Toast.makeText(this, "Por favor, informe seu peso", Toast.LENGTH_SHORT).show();
            etWeight.requestFocus();
            return;
        }

        float weight;
        try {
            weight = Float.parseFloat(weightStr);
            if (weight <= 0 || weight > 300) {
                Toast.makeText(this, "Peso inválido (1-300 kg)", Toast.LENGTH_SHORT).show();
                etWeight.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Peso inválido", Toast.LENGTH_SHORT).show();
            etWeight.requestFocus();
            return;
        }

        // Validar altura
        String heightStr = etHeight.getText().toString().trim();
        if (heightStr.isEmpty()) {
            Toast.makeText(this, "Por favor, informe sua altura", Toast.LENGTH_SHORT).show();
            etHeight.requestFocus();
            return;
        }

        float height;
        try {
            height = Float.parseFloat(heightStr);
            if (height <= 0 || height > 250) {
                Toast.makeText(this, "Altura inválida (1-250 cm)", Toast.LENGTH_SHORT).show();
                etHeight.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Altura inválida", Toast.LENGTH_SHORT).show();
            etHeight.requestFocus();
            return;
        }

        // Validar data de nascimento
        if (selectedBirthdate.getTimeInMillis() == 0 ||
                selectedBirthdate.getTimeInMillis() >= System.currentTimeMillis()) {
            Toast.makeText(this, "Por favor, selecione sua data de nascimento",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Salvar dados do usuário
        userConfig.setWeight(weight);
        userConfig.setHeight(height);

        String gender = rbMale.isChecked() ? UserConfig.GENDER_MALE : UserConfig.GENDER_FEMALE;
        userConfig.setGender(gender);

        userConfig.setBirthdate(selectedBirthdate.getTimeInMillis());

        // Salvar tipo de mapa
        int mapType = rbMapSatellite.isChecked() ?
                UserConfig.MAP_TYPE_SATELLITE : UserConfig.MAP_TYPE_NORMAL;
        userConfig.setMapType(mapType);

        // Salvar modo de navegação
        String navMode = rbNavCourseUp.isChecked() ?
                UserConfig.NAV_MODE_COURSE_UP : UserConfig.NAV_MODE_NORTH_UP;
        userConfig.setNavigationMode(navMode);

        Toast.makeText(this, "Configurações salvas com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
