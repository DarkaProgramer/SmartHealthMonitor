package mx.utng.smart_health_monitor.data.remote

import com.google.gson.GsonBuilder
import mx.utng.smart_health_monitor.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NeonClient {
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"

    val AUTH_HEADER = "Bearer ${BuildConfig.NEON_API_KEY}"
    val CONN_STRING = "postgresql://neondb_owner:npg_5f3S4XbtGZrV@ep-spring-union-a6c4m1yz-pooler.us-west-2.aws.neon.tech/neondb?sslmode=require"

    val api: NeonApiService by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(NeonApiService::class.java)
    }
}