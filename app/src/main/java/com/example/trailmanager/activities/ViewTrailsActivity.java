package com.example.trailmanager.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trailmanager.R;
import com.example.trailmanager.adapters.TrailAdapter;
import com.example.trailmanager.database.TrailDAO;
import com.example.trailmanager.models.Trail;

import java.util.Calendar;
import java.util.List;

public class ViewTrailsActivity extends AppCompatActivity implements TrailAdapter.OnTrailClickListener {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private TrailAdapter adapter;
    private TrailDAO trailDAO;
    private List<Trail> trails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trails);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Minhas Trilhas");
        }

        trailDAO = new TrailDAO(this);

        initViews();
        setupRecyclerView();
        loadTrails();
    }

    @SuppressLint("WrongViewCast")
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewTrails);
        tvEmpty = findViewById(R.id.tvEmpty);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrailAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void loadTrails() {
        trails = trailDAO.getAllTrails();
        adapter.setTrails(trails);

        if (trails.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTrailClick(Trail trail) {
        // Visualizar detalhes da trilha
        Intent intent = new Intent(this, TrailDetailActivity.class);
        intent.putExtra("trail_id", trail.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Trail trail) {
        showEditDialog(trail);
    }

    @Override
    public void onDeleteClick(Trail trail) {
        showDeleteDialog(trail);
    }

    @Override
    public void onShareClick(Trail trail) {
        shareTrail(trail);
    }

    private void showEditDialog(Trail trail) {
        android.widget.EditText input = new android.widget.EditText(this);
        input.setText(trail.getName());
        input.setSelectAllOnFocus(true);

        new AlertDialog.Builder(this)
                .setTitle("Editar Nome da Trilha")
                .setView(input)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String newName = input.getText().toString().trim();
                    if (!newName.isEmpty()) {
                        trailDAO.updateTrailName(trail.getId(), newName);
                        loadTrails();
                        Toast.makeText(this, "Nome atualizado!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showDeleteDialog(Trail trail) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Trilha")
                .setMessage("Deseja realmente excluir esta trilha?")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    trailDAO.deleteTrail(trail.getId());
                    loadTrails();
                    Toast.makeText(this, "Trilha excluída!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void shareTrail(Trail trail) {
        // Carregar trilha completa com pontos
        Trail fullTrail = trailDAO.getTrailById(trail.getId());

        if (fullTrail == null || fullTrail.getPoints().isEmpty()) {
            Toast.makeText(this, "Trilha não possui dados para compartilhar",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar menu de opções de compartilhamento
        String[] options = {"GPX", "KML", "JSON", "CSV", "Texto Simples"};

        new AlertDialog.Builder(this)
                .setTitle("Escolha o formato")
                .setItems(options, (dialog, which) -> {
                    String format = options[which];
                    shareTrailInFormat(fullTrail, format);
                })
                .show();
    }

    private void shareTrailInFormat(Trail trail, String format) {
        String content = "";
        String mimeType = "text/plain";

        switch (format) {
            case "GPX":
                content = com.example.trailmanager.utils.ExportHelper.toGPX(trail);
                mimeType = "application/gpx+xml";
                break;
            case "KML":
                content = com.example.trailmanager.utils.ExportHelper.toKML(trail);
                mimeType = "application/vnd.google-earth.kml+xml";
                break;
            case "JSON":
                content = com.example.trailmanager.utils.ExportHelper.toJSON(trail);
                mimeType = "application/json";
                break;
            case "CSV":
                content = com.example.trailmanager.utils.ExportHelper.toCSV(trail);
                mimeType = "text/csv";
                break;
            case "Texto Simples":
                content = com.example.trailmanager.utils.ExportHelper.toText(trail);
                break;
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType(mimeType);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Trilha: " + trail.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);

        startActivity(Intent.createChooser(shareIntent, "Compartilhar trilha via"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_trails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            showDeleteAllDialog();
            return true;
        } else if (id == R.id.action_delete_range) {
            showDeleteRangeDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Todas as Trilhas")
                .setMessage("Deseja realmente excluir TODAS as trilhas? Esta ação não pode ser desfeita.")
                .setPositiveButton("Excluir Todas", (dialog, which) -> {
                    trailDAO.deleteAllTrails();
                    loadTrails();
                    Toast.makeText(this, "Todas as trilhas foram excluídas!",
                            Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showDeleteRangeDialog() {
        final Calendar startDate = Calendar.getInstance();
        final Calendar endDate = Calendar.getInstance();

        // Primeiro, selecionar data inicial
        DatePickerDialog startPicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    startDate.set(year, month, dayOfMonth, 0, 0, 0);

                    // Depois, selecionar data final
                    DatePickerDialog endPicker = new DatePickerDialog(
                            this,
                            (view2, year2, month2, dayOfMonth2) -> {
                                endDate.set(year2, month2, dayOfMonth2, 23, 59, 59);

                                // Confirmar exclusão
                                new AlertDialog.Builder(this)
                                        .setTitle("Confirmar Exclusão")
                                        .setMessage("Excluir trilhas entre " +
                                                android.text.format.DateFormat.getDateFormat(this).format(startDate.getTime()) +
                                                " e " +
                                                android.text.format.DateFormat.getDateFormat(this).format(endDate.getTime()) + "?")
                                        .setPositiveButton("Excluir", (dialog, which) -> {
                                            int count = trailDAO.deleteTrailsByDateRange(
                                                    startDate.getTimeInMillis(),
                                                    endDate.getTimeInMillis()
                                            );
                                            loadTrails();
                                            Toast.makeText(this, count + " trilha(s) excluída(s)!",
                                                    Toast.LENGTH_SHORT).show();
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .show();
                            },
                            endDate.get(Calendar.YEAR),
                            endDate.get(Calendar.MONTH),
                            endDate.get(Calendar.DAY_OF_MONTH)
                    );
                    endPicker.show();
                },
                startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH)
        );
        startPicker.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTrails();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}