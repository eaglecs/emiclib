package basecode.com.data.di

import basecode.com.data.BuildConfig
import basecode.com.data.remote.ApiService
import basecode.com.data.remote.HeaderInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object DataKoinModules {
    @JvmStatic
    fun modules(): List<Module> {
        return listOf(remoteModule)
    }

    private val remoteModule = module {
        fun createApiService(): ApiService {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(BuildConfig.END_POINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(get())
                    .build()
            return retrofit.create(ApiService::class.java)
        }

        fun provideOkClient(): OkHttpClient {
            return OkHttpClient.Builder()
                    .addNetworkInterceptor(StethoInterceptor())
                    .retryOnConnectionFailure(false)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(get())
                    .build()
        }

        single {
            HeaderInterceptor(context = get())
        }

        single {
            createApiService()
        }

        single {
            provideOkClient()
        }
    }
}