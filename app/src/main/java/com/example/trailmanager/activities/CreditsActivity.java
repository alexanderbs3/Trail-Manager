package com.example.trailmanager.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trailmanager.R;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Créditos");
        }

        initViews();
    }

    private void initViews() {
        ImageView imgLogo = findViewById(R.id.imgCreditsLogo);
        TextView tvAppName = findViewById(R.id.tvCreditsAppName);
        TextView tvVersion = findViewById(R.id.tvCreditsVersion);
        TextView tvDescription = findViewById(R.id.tvCreditsDescription);
        TextView tvDeveloper = findViewById(R.id.tvCreditsDeveloper);
        TextView tvFeatures = findViewById(R.id.tvCreditsFeatures);
        TextView tvTechnologies = findViewById(R.id.tvCreditsTechnologies);

        // Informações básicas
        tvAppName.setText(R.string.app_name);
        tvVersion.setText("Versão 1.0");

        // Descrição
        tvDescription.setText(
                "Trail Manager é um aplicativo completo para registro e gerenciamento " +
                        "de trilhas e caminhadas. Acompanhe seu progresso, analise suas estatísticas " +
                        "e compartilhe suas aventuras!"
        );

        // Desenvolvedor (ALTERE COM SEUS DADOS)
        tvDeveloper.setText(
                "Desenvolvido por: [Alexander Costa]\n" +
                        "Curso: [Analise de desenvolvimento de sistemas]\n" +
                        "Instituição: [Catolica]\n" +
                        "Ano: 2025"
        );

        // Funcionalidades
        tvFeatures.setText(
                "✓ Rastreamento GPS em tempo real\n" +
                        "✓ Cálculo automático de distância e velocidade\n" +
                        "✓ Estimativa de gasto calórico\n" +
                        "✓ Visualização de trilhas em mapa\n" +
                        "✓ Suporte a mapas vetoriais e satélite\n" +
                        "✓ Modos de navegação North Up e Course Up\n" +
                        "✓ Exportação em múltiplos formatos (GPX, KML, JSON, CSV)\n" +
                        "✓ Compartilhamento via WhatsApp, Email, etc.\n" +
                        "✓ Banco de dados SQLite local\n" +
                        "✓ Interface Material Design 3"
        );

        // Tecnologias
        tvTechnologies.setText(
                "• Java + Android SDK\n" +
                        "• Google Maps SDK\n" +
                        "• Google Play Services Location\n" +
                        "• SQLite Database\n" +
                        "• Material Components\n" +
                        "• RecyclerView\n" +
                        "• Foreground Services"
        );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}