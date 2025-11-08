plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mesapronta.app"
    // Mantendo a SDK de destino
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mesapronta.app"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // --- Configurações de Compilação e Desugaring ---
    compileOptions {
        // Habilita o desugaring para java.time
        val coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8 // Usar Java 8 para compatibilidade com Desugaring
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        // Alinhar com Java 8
        jvmTarget = "1.8"
    }
    // ------------------------------------------------

    buildFeatures { compose = true }

    composeOptions {
        // Versão do plugin Kotlin Compiler Extension
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

dependencies {
    // --- 1. DESUGARING (CORREÇÃO PARA JAVA.TIME) ---
    // Esta deve ser uma configuração separada para incluir a biblioteca.
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.1.5")
    // ------------------------------------------------

    // --- 2. DEPENDÊNCIAS DE INFRAESTRUTURA/CORE ---
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.activity:activity-compose:1.9.1")

    // --- 3. DEPENDÊNCIAS DE COROUTINES ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // --- 4. DEPENDÊNCIAS DO COMPOSE E MATERIAL ---
    // BOM: Define versões estáveis e consistentes
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")

    // --- 5. DEPENDÊNCIAS DE LIFECYCLE E OBSERVAÇÃO ---
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")

    // Suporte para LiveData (observeAsState)
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")

    // Suporte para StateFlow (collectAsStateWithLifecycle) - IMPORTANTE
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

    // --- 6. DEPENDÊNCIAS DE NAVEGAÇÃO E IMAGENS ---
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.coil-kt:coil-compose:2.6.0")

    // --- 7. TESTES ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}