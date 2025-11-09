# 🗺️ Trail Manager

Aplicativo Android para gerenciamento e rastreamento de trilhas e caminhadas com GPS em tempo real.

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![Java](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-Educational-blue.svg)]()

---

## 📱 Sobre o Projeto

Trail Manager é um aplicativo completo de gerenciamento de trilhas que permite aos usuários registrar, visualizar e compartilhar suas caminhadas e trilhas. O app utiliza GPS em tempo real para rastreamento preciso, calcula estatísticas como distância, velocidade e calorias queimadas, e oferece visualização em mapas interativos.

### ✨ Funcionalidades Principais

- 📍 **Rastreamento GPS em Tempo Real** - Acompanhe sua posição durante a trilha
- 📊 **Estatísticas Completas** - Distância, velocidade, duração e calorias
- 🗺️ **Visualização em Mapa** - Mapas vetoriais e satélite do Google Maps
- 💾 **Armazenamento Local** - Banco de dados SQLite para persistência
- 📤 **Compartilhamento Múltiplo** - Exportação em GPX, KML, JSON, CSV e texto
- ⚙️ **Configurações Personalizadas** - Perfil do usuário e preferências de mapa
- 🧭 **Navegação Inteligente** - Modos North Up e Course Up
- 🔥 **Cálculo de Calorias** - Baseado em MET (Metabolic Equivalent of Task)

---

## 🎯 Requisitos do Projeto

Este projeto foi desenvolvido como parte da disciplina de **Desenvolvimento Mobile** e atende aos seguintes requisitos:

### Funcionalidades Obrigatórias

#### 1. Configuração
- [x] Dados do usuário (peso, altura, sexo, data de nascimento)
- [x] Tipo de mapa (vetorial/satélite)
- [x] Modo de navegação (North Up/Course Up)
- [x] Armazenamento em SharedPreferences

#### 2. Registrar Trilha
- [x] Mapa em tempo real
- [x] Velocidade instantânea e máxima
- [x] Cronômetro
- [x] Distância total percorrida
- [x] Gasto calórico calculado
- [x] Círculo de acurácia GPS
- [x] Desenho do caminho no mapa
- [x] Armazenamento no SQLite

#### 3. Consultar Trilhas
- [x] Listar todas as trilhas
- [x] Editar nome da trilha
- [x] Deletar trilha específica
- [x] Deletar por período
- [x] Deletar todas as trilhas
- [x] Visualizar detalhes no mapa
- [x] Compartilhar em múltiplos formatos

#### 4. Créditos
- [x] Informações do aplicativo
- [x] Dados do desenvolvedor
- [x] Funcionalidades
- [x] Tecnologias utilizadas
- [x] Imagem obrigatória

---

## 🛠️ Tecnologias Utilizadas

### Core
- **Linguagem**: Java
- **SDK**: Android SDK (API 24+)
- **IDE**: Android Studio
- **Build System**: Gradle

### Bibliotecas e APIs
- **Google Maps SDK** - Mapas interativos
- **Google Play Services Location** - Serviços de localização
- **Material Components 3** - Interface moderna
- **SQLite** - Banco de dados local
- **RecyclerView** - Listas otimizadas
- **Foreground Services** - Rastreamento em background

### Arquitetura
```
com.example.trailmanager/
├── activities/      # Telas do aplicativo
├── adapters/        # Adaptadores RecyclerView
├── database/        # Camada de dados SQLite
├── models/          # Modelos de dados
├── services/        # Serviços de background
└── utils/           # Utilitários e helpers
```

---

## 📋 Pré-requisitos

- **Android Studio**: Arctic Fox ou superior
- **JDK**: 8 ou superior
- **Android SDK**: API Level 24 (Android 7.0) ou superior
- **Google Maps API Key**: [Como obter](#-configuração-google-maps-api)
- **Dispositivo/Emulador**: Com Google Play Services

---

## 🚀 Instalação e Configuração

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/trail-manager.git
cd trail-manager
```

### 2. Abra no Android Studio

```
File → Open → Selecione a pasta do projeto
```

### 3. Configure a Google Maps API Key

#### 3.1. Obter API Key

1. Acesse: https://console.cloud.google.com/
2. Crie um novo projeto: "Trail Manager"
3. Ative as APIs:
   - Maps SDK for Android
   - Maps JavaScript API
4. Crie uma API Key em "Credentials"
5. Configure as restrições (Android apps + SHA-1)

#### 3.2. Adicionar no Projeto

Edite `app/src/main/AndroidManifest.xml`:

```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="SUA_API_KEY_AQUI" />
```

**Ou** (Recomendado para segurança):

Crie `local.properties` na raiz:
```properties
MAPS_API_KEY=SUA_API_KEY_AQUI
```

Edite `build.gradle (Module: app)`:
```gradle
android {
    defaultConfig {
        manifestPlaceholders = [MAPS_API_KEY: project.properties['MAPS_API_KEY']]
    }
}
```

#### 3.3. Obter SHA-1

No Android Studio:
```
Gradle → Tasks → android → signingReport (duplo clique)
```

Copie o SHA-1 e adicione nas restrições da API Key.

### 4. Sync e Build

```
File → Sync Project with Gradle Files
Build → Clean Project
Build → Rebuild Project
```

### 5. Execute o App

```
Run → Run 'app' (Shift + F10)
```

Selecione um dispositivo ou emulador e aguarde a instalação.

---

## 📖 Como Usar

### Primeiro Uso

1. **Configure seus Dados**
   - Abra "Configurações"
   - Preencha: peso, altura, sexo e data de nascimento
   - Escolha tipo de mapa e modo de navegação
   - Salve

2. **Conceda Permissões**
   - Permita acesso à localização (sempre ou durante uso)
   - Permita notificações (Android 13+)

### Registrar uma Trilha

1. Clique em **"Registrar Trilha"**
2. Aguarde o mapa carregar
3. Clique em **"Iniciar"**
4. Caminhe/corra normalmente
5. Acompanhe as estatísticas em tempo real:
   - Velocidade atual e máxima
   - Distância percorrida
   - Tempo decorrido
   - Calorias gastas
6. Clique em **"Finalizar"** quando terminar
7. Confirme para salvar a trilha

### Visualizar Trilhas

1. Clique em **"Consultar Trilhas"**
2. Veja a lista de todas as trilhas
3. Clique em uma trilha para ver detalhes
4. Opções disponíveis:
   - ✏️ **Editar** - Alterar nome
   - 🗑️ **Excluir** - Remover trilha
   - 📤 **Compartilhar** - Exportar dados

### Compartilhar Trilha

1. Selecione uma trilha
2. Clique no ícone de compartilhar
3. Escolha o formato:
   - **GPX** - Padrão GPS Exchange
   - **KML** - Google Earth
   - **JSON** - JavaScript Object Notation
   - **CSV** - Planilha
   - **Texto** - Resumo legível
4. Compartilhe via WhatsApp, Email, etc.

---

## 🗂️ Estrutura do Banco de Dados

### Tabela: trails
```sql
CREATE TABLE trails (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    start_time INTEGER NOT NULL,
    end_time INTEGER,
    total_distance REAL DEFAULT 0,
    max_speed REAL DEFAULT 0,
    avg_speed REAL DEFAULT 0,
    calories_burned REAL DEFAULT 0
)
```

### Tabela: trail_points
```sql
CREATE TABLE trail_points (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    trail_id INTEGER NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    altitude REAL DEFAULT 0,
    accuracy REAL DEFAULT 0,
    speed REAL DEFAULT 0,
    timestamp INTEGER NOT NULL,
    FOREIGN KEY(trail_id) REFERENCES trails(id) ON DELETE CASCADE
)
```

---

## 🧮 Cálculo de Calorias

O aplicativo calcula calorias baseado no **MET (Metabolic Equivalent of Task)**:

```
Calorias = MET × Peso(kg) × Tempo(horas)
```

### Tabela MET por Velocidade

| Velocidade | Atividade | MET |
|------------|-----------|-----|
| < 2 km/h | Parado | 2.0 |
| 2-4 km/h | Caminhada Lenta | 2.5 |
| 4-5 km/h | Caminhada Moderada | 3.5 |
| 5-6.5 km/h | Caminhada Rápida | 4.3 |
| 6.5-8 km/h | Caminhada Muito Rápida | 5.0 |
| 8-10 km/h | Corrida Leve | 8.0 |
| 10-12 km/h | Corrida Moderada | 10.0 |
| 12-14 km/h | Corrida Intensa | 12.0 |
| > 14 km/h | Corrida Muito Intensa | 14.0 |

**Ajuste por Gênero**: Mulheres têm redução de 5% no cálculo.

---

## 🎨 Interface do Usuário

### Telas Principais

1. **MainActivity** - Menu principal com 4 opções
2. **ConfigActivity** - Configurações do usuário
3. **RegisterTrailActivity** - Registro de trilha com mapa
4. **ViewTrailsActivity** - Lista de trilhas com RecyclerView
5. **TrailDetailActivity** - Detalhes e mapa da trilha
6. **CreditsActivity** - Informações e créditos

### Design
- **Material Design 3** - Interface moderna
- **Cores**: Azul (#1976D2) como cor primária
- **Cards** - Informações organizadas em cards
- **Ícones** - Material Icons para todas as ações
- **Responsivo** - Adaptável a diferentes tamanhos de tela

---

## 🔐 Permissões

O aplicativo requer as seguintes permissões:

```xml
<!-- Localização (Obrigatória) -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

<!-- Internet (Obrigatória - para mapas) -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Foreground Service (Obrigatória - rastreamento) -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_LOCATION" />

<!-- Notificações (Android 13+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

---

## 🐛 Troubleshooting

### Mapa não aparece

**Problema**: Tela cinza ou erro de autorização

**Solução**:
1. Verifique se a API Key está correta
2. Confirme que Maps SDK for Android está ativado
3. Verifique se o SHA-1 está configurado
4. Aguarde 5-10 minutos após mudanças
5. Limpe e reconstrua o projeto

### GPS não funciona

**Problema**: Localização não atualiza

**Solução**:
1. Verifique se GPS está ativado no dispositivo
2. Confirme permissões de localização
3. Teste ao ar livre (sinal melhor)
4. No emulador, configure localização manualmente

### App crasha ao salvar

**Problema**: Erro ao finalizar trilha

**Solução**:
1. Verifique logs no Logcat
2. Confirme que há pontos GPS registrados
3. Verifique se configurações do usuário estão preenchidas

### Banco de dados vazio

**Problema**: Trilhas não aparecem após salvar

**Solução**:
1. Verifique Database Inspector
2. Confirme que `trailDAO.insertTrail()` retorna ID > 0
3. Use `adb` para inspecionar o banco manualmente

---

## 📊 Estatísticas do Projeto

```
📁 Arquivos Java:        17 classes
📄 Layouts XML:          7 arquivos
🎨 Drawables:            13 ícones
📋 Recursos:             5 arquivos
📏 Linhas de Código:     ~3.500 linhas
⏱️ Tempo de Desenvolvimento: [SEU TEMPO]
```

---

## 👨‍💻 Desenvolvedor

**Nome**: [Seu Nome Completo]  
**Matrícula**: [Sua Matrícula]  
**Curso**: [Seu Curso]  
**Instituição**: [Sua Instituição]  
**Disciplina**: Desenvolvimento Mobile  
**Professor**: [Nome do Professor]  
**Ano**: 2025

---

## 📄 Licença

Este projeto foi desenvolvido para fins **educacionais** como parte da disciplina de Desenvolvimento Mobile.

Bibliotecas e APIs de terceiros utilizadas estão sujeitas às suas respectivas licenças:
- Google Maps SDK: [Google Maps Platform Terms of Service](https://cloud.google.com/maps-platform/terms)
- Material Components: [Apache License 2.0](https://github.com/material-components/material-components-android/blob/master/LICENSE)

---

## 🙏 Agradecimentos

- **Google Maps Platform** - Pela API de mapas
- **Material Design** - Pelas diretrizes de interface
- **Android Developers** - Pela documentação completa
- **Stack Overflow** - Pela comunidade de desenvolvedores
- **Professor [Nome]** - Pela orientação no projeto

---

## 📚 Referências

- [Android Developers Guide](https://developer.android.com/guide)
- [Google Maps Android SDK](https://developers.google.com/maps/documentation/android-sdk)
- [Material Design 3](https://m3.material.io/)
- [SQLite Documentation](https://www.sqlite.org/docs.html)
- [Location Services API](https://developer.android.com/training/location)

---

## 📞 Suporte

Para dúvidas ou problemas:

1. **Documentação**: Consulte este README primeiro
2. **Issues**: Abra uma issue no GitHub (se aplicável)
3. **Email**: [seu-email@exemplo.com]
4. **Professor**: Entre em contato com o orientador

---

## 🔄 Versões

### v1.0.0 (Novembro 2025)
- ✅ Release inicial
- ✅ Todas as funcionalidades obrigatórias implementadas
- ✅ Testes realizados em Android 7.0+
- ✅ Interface Material Design 3
- ✅ Documentação completa

---

## 🚀 Melhorias Futuras

Possíveis implementações para versões futuras:

- [ ] Importar trilhas de arquivos GPX/KML
- [ ] Gráficos de elevação
- [ ] Histórico de estatísticas mensais
- [ ] Integração com redes sociais
- [ ] Modo offline com mapas baixados
- [ ] Sistema de conquistas/badges
- [ ] Backup na nuvem (Firebase)
- [ ] Temas personalizáveis (claro/escuro)
- [ ] Suporte a múltiplos idiomas
- [ ] Widget para tela inicial

---

## 📸 Screenshots

> **Nota**: Adicione screenshots do aplicativo aqui após a implementação completa.

```
[Screenshot 1 - Tela Principal]
[Screenshot 2 - Registro de Trilha]
[Screenshot 3 - Mapa com Trajeto]
[Screenshot 4 - Lista de Trilhas]
[Screenshot 5 - Detalhes da Trilha]
```

---

## 🎓 Aprendizados

Este projeto proporcionou experiência prática em:

- Desenvolvimento Android nativo com Java
- Integração com Google Maps API
- Uso de SQLite para persistência local
- Serviços em foreground e notificações
- Gerenciamento de permissões runtime
- Location Services e GPS
- Material Design e UX
- Arquitetura de aplicativos móveis
- Versionamento com Git

---

<div align="center">

**Trail Manager** - Gerencie suas trilhas com tecnologia! 🗺️🚶‍♂️

Desenvolvido com ❤️ em Java para Android

⭐ Se este projeto foi útil, considere dar uma estrela!

</div>

---

**Última atualização**: Novembro 2025  
**Status**: ✅ Projeto Completo e Funcional
