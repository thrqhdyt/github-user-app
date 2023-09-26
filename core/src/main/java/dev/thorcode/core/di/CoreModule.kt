package dev.thorcode.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import dev.thorcode.core.BuildConfig
import dev.thorcode.core.data.ThemeRepositoryImpl
import dev.thorcode.core.data.UserRepository
import dev.thorcode.core.data.source.local.LocalDataSource
import dev.thorcode.core.data.source.local.SettingPreferences
import dev.thorcode.core.data.source.local.room.FavoriteUserDatabase
import dev.thorcode.core.data.source.remote.RemoteDataSource
import dev.thorcode.core.data.source.remote.network.ApiService
import dev.thorcode.core.domain.repository.IThemeRepository
import dev.thorcode.core.domain.repository.IUserRepository
import dev.thorcode.core.utils.AppExecutors
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import okhttp3.CertificatePinner
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val APP_PACKAGE_NAME = "dev.thorcode.preferences"
private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    APP_PACKAGE_NAME
)
private const val GITHUB_TOKEN = BuildConfig.API_KEY

val databaseModule = module {
    factory { get<FavoriteUserDatabase>().favoriteUserDao() }
    single {
        val passphrase: ByteArray = SQLiteDatabase.getBytes("thorcode".toCharArray())
        val factory = SupportFactory(passphrase)
        Room.databaseBuilder(
            androidContext(),
            FavoriteUserDatabase::class.java, "favoriteuser_database"
        ).fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }
}

val networkModule = module {
    single {
        val hostname = "api.github.com"
        val certificatePinner = CertificatePinner.Builder()
            .add(hostname, "sha256/Jg78dOE+fydIGk19swWwiypUSR6HWZybfnJG/8G7pyM=")
            .add(hostname, "sha256/e0IRz5Tio3GA1Xs4fUVWmH1xHDiH2dMbVtCBSkOIdqM=")
            .add(hostname, "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=")
            .build()
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .addHeader("Authorization", "Bearer $GITHUB_TOKEN")
                .build()
            chain.proceed(requestHeaders)
        }
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(authInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val dataStoreModule = module {
    single {
        androidContext().preferencesDataStore
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single { SettingPreferences(get()) }
    factory { AppExecutors() }
    single<IThemeRepository> { ThemeRepositoryImpl(get()) }
    single<IUserRepository> { UserRepository(get(), get(), get()) }
}