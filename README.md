# Retrofit Exemplo - Mars Photos 🌌

Este projeto é um exemplo prático de consumo de API REST desenvolvido como parte da disciplina de **Programação para Dispositivos Móveis 1** do curso de **Sistemas e Mídias Digitais (SMD)** da **Universidade Federal do Ceará (UFC)**.

O aplicativo demonstra como buscar dados da internet (neste caso, fotos reais da superfície de Marte capturadas pelos rovers da NASA) e exibi-las em uma interface construída com Jetpack Compose.

## 📚 Referência

Este projeto foi baseado no Codelab oficial: [Android Basics with Compose - Getting data from the internet](https://developer.android.com/codelabs/basic-android-kotlin-compose-getting-data-internet?hl=pt-br), com pequenas alterações e adaptações arquiteturais para fins didáticos.

## 🛠️ Tecnologias Utilizadas

* **Kotlin** & **Jetpack Compose**: Para lógica e UI.
* **Retrofit**: Para realizar as requisições HTTP.
* **Kotlinx Serialization**: Para converter o JSON da resposta em objetos Kotlin.
* **Coil**: Para carregamento assíncrono de imagens via URL.

---

## 🏗️ Estrutura da Implementação do Retrofit

Para entender como a comunicação com a internet funciona neste projeto, destacamos abaixo os arquivos principais e suas responsabilidades:

### 1. Dependências (`build.gradle.kts`)

No arquivo de build do módulo (app) e do projeto, adicionamos as bibliotecas necessárias para o Retrofit funcionar e conversar com o serializador JSON.

```kotlin
// build.gradle.kts (Module: app)
dependencies {
    // ... outras dependências ...

    // 1. Retrofit (O cliente HTTP)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // 2. Conversor para Kotlin Serialization (O Tradutor JSON -> Objeto)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // 3. Ponte entre Retrofit e Serialization
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // 4. OkHttp (O motor de conexão e logs)
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
}

// build.gradle.kts (Module: app)
plugins {
    // Plugin necessário para a serialização funcionar
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

```

### 2. O Modelo de Dados (`MarsPhoto.kt`)

Define a estrutura dos dados que receberemos. Usamos a anotação `@Serializable` para indicar que esta classe pode ser convertida de/para JSON.

* **Arquivo:** `com.smd.retrofitexemplo.model.MarsPhoto`
* **Destaque:** O uso de `@SerialName` permite mapear o campo `img_src` (como vem do JSON da API) para `imgSrc` (padrão camelCase do Kotlin).

```kotlin
@Serializable
data class MarsPhoto(
    val id: String,
    @SerialName(value = "img_src") // Mapeia "img_src" do JSON para a variável abaixo
    val imgSrc: String
)

```

### 3. A Interface da API (`ApiService.kt`)

Este arquivo define **o que** o aplicativo pode pedir para a internet. Ele atua como um contrato, listando os endpoints disponíveis.

* **Arquivo:** `com.smd.retrofitexemplo.network.ApiService`
* **Uso:** Define os métodos HTTP (`GET`, `POST`, `PUT`, `DELETE`). Note o uso de `suspend`, pois chamadas de rede devem rodar em corrotinas para não travar a interface do usuário.

```kotlin
interface ApiService {
    // Busca a lista de fotos no endpoint "photos"
    @GET("photos")
    suspend fun getPhotos(): List<MarsPhoto>

    // Exemplos de outras operações REST (CRUD):
    @POST("photos")
    suspend fun createPhoto(@Body photo: MarsPhoto): MarsPhoto

    @PUT("photos/{id}")
    suspend fun updatePhoto(@Path("id") photoId: String, @Body photo: MarsPhoto): MarsPhoto

    @DELETE("photos/{id}")
    suspend fun deletePhoto(@Path("id") photoId: String): Unit
}

```

### 4. O Cliente Retrofit (`RetrofitClient.kt`)

Este é o objeto Singleton (criado uma única vez) que configura e cria a instância do serviço. Ele junta a URL base, o conversor de JSON e a Interface definida acima.

* **Arquivo:** `com.smd.retrofitexemplo.network.RetrofitClient`
* **Destaque:** O uso de `by lazy` garante que a inicialização (que é custosa) só ocorra quando o objeto for acessado pela primeira vez.

```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com"

    // Configura o parser de JSON para ignorar chaves desconhecidas (evita erros se a API mudar)
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    // Cria a instância do ApiService
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Adiciona o conversor para transformar JSON em objetos MarsPhoto automaticamente
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ApiService::class.java)
    }
}

```

---

## ▶️ Como executar

1. Clone este repositório.
2. Abra o projeto no **Android Studio**.
3. Aguarde a sincronização do Gradle.
4. Execute o app em um emulador ou dispositivo físico (Mínimo SDK 31 / Android 12).

## 📄 Licença

Este projeto é para fins educacionais.
Mars Images: NASA/JPL-Caltech
