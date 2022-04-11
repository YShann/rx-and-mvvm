package tw.edu.ntub.imd.birc.rxandmvvm

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create
import tw.edu.ntub.imd.birc.rxandmvvm.constant.UrlConstant
import tw.edu.ntub.imd.birc.rxandmvvm.model.UserModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.api.UserAPI
import tw.edu.ntub.imd.birc.rxandmvvm.source.api.UserAPISource
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.MainViewModel

class RxAndMVVMApplication : Application() {

    @ExperimentalSerializationApi
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@RxAndMVVMApplication)

            val retrofitModule = module {
                single {
                    Retrofit.Builder()
                        .baseUrl(UrlConstant.BASE_URL)
                        .addConverterFactory(
                            Json(builderAction = {ignoreUnknownKeys = true})
                                .asConverterFactory(MediaType.parse("application/json; charset=UTF-8")!!)
                        )
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build()
                }
                single<UserAPI> {
                    get<Retrofit>().create()
                }
            }
            val databaseModule = module {
//                single {
//                    Room.databaseBuilder(androidContext(), Database::class.java, "RxAndMVVM")
//                        .fallbackToDestructiveMigration()
//                        .build()
//                }
            }
            val sourceModule = module {
                single {
                    UserAPISource(get())
                }
            }
            val modelModule = module {
                single {
                    UserModel(get<UserAPISource>())
                }
            }
            val viewModelModule = module {
                viewModel {
                    MainViewModel(get())
                }
            }
            modules(
                listOf(
                    retrofitModule,
                    databaseModule,
                    sourceModule,
                    modelModule,
                    viewModelModule
                )
            )
        }
    }
}
