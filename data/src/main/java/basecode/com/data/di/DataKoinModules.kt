package basecode.com.data.di

import basecode.com.data.BuildConfig
import basecode.com.data.cache.dbflow.BookDataServiceImpl
import basecode.com.data.remote.ApiService
import basecode.com.data.remote.HeaderInterceptor
import basecode.com.data.remote.NullOnEmptyConverterFactory
import basecode.com.data.repositoryiml.ApiServiceImpl
import basecode.com.data.repositoryiml.CacheServiceImpl
import basecode.com.domain.repository.CacheRespository
import basecode.com.domain.repository.dbflow.BookDataService
import basecode.com.domain.repository.network.AppRepository
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager
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
        return listOf(remoteModule, dbflowModule, databaseModule)
    }

    private val dbflowModule = module {
        single {
            BookDataServiceImpl() as BookDataService
        }
    }

    private val databaseModule = module {
        FlowManager.init(FlowConfig.Builder(get()).build())
    }

    private val remoteModule = module {
        fun createApiService(): ApiService {
            val retrofit: Retrofit = Retrofit.Builder().baseUrl(BuildConfig.END_POINT)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(NullOnEmptyConverterFactory())
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
                .addInterceptor(OkHttpProfilerInterceptor())
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