package basecode.com.data.di

import basecode.com.data.BuildConfig
import basecode.com.data.epub.SkyDatabase
import basecode.com.data.epub.SkyDatabaseImpl
import basecode.com.data.remote.ApiService
import basecode.com.data.remote.HeaderInterceptor
import basecode.com.data.repositoryiml.ApiServiceImpl
import basecode.com.data.repositoryiml.CacheServiceImpl
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.repository.epub.SkyDatabaseRepository
import basecode.com.domain.repository.network.AppRepository
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.skytree.epub.SkyKeyManager
import okhttp3.Interceptor
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
        return listOf(remoteModule, epubModule)
    }

    private val epubModule = module {
        single {
            SkyDatabase(get())
        }

        single {
            SkyKeyManager("A3UBZzJNCoXmXQlBWD4xNo", "zfZl40AQXu8xHTGKMRwG69")
        }

        single {
            SkyDatabaseImpl(sd = get()) as SkyDatabaseRepository
        }
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
            HeaderInterceptor(context = get()) as Interceptor
        }

        single {
            createApiService()
        }

        single {
            provideOkClient()
        }

        single {
            ApiServiceImpl(apiServiceApp = get()) as AppRepository
        }

        single {
            CacheServiceImpl() as CacheRespository
        }
    }
}