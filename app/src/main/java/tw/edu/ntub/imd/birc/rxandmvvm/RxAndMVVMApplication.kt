package tw.edu.ntub.imd.birc.rxandmvvm

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.create
import tw.edu.ntub.imd.birc.rxandmvvm.constant.UrlConstant
import tw.edu.ntub.imd.birc.rxandmvvm.model.*
import tw.edu.ntub.imd.birc.rxandmvvm.source.api.*
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.*

class RxAndMVVMApplication : Application() {

//    @ExperimentalSerializationApi
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@RxAndMVVMApplication)

            val retrofitModule = module {
                single {
                    Retrofit.Builder()
                        .baseUrl(UrlConstant.BASE_URL)
                        .addConverterFactory(
                            Json(builderAction = {ignoreUnknownKeys = true})
                                .asConverterFactory("application/json; charset=UTF-8".toMediaTypeOrNull()!!)
                        )
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build()
                }
                single<UserAPI> {
                    get<Retrofit>().create()
                }
                single<DietRecordAPI> {
                    get<Retrofit>().create()
                }
                single<WaterRecordAPI> {
                    get<Retrofit>().create()
                }
                single<PoopRecordAPI> {
                    get<Retrofit>().create()
                }
                single<FoodAPI> {
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
                single {
                    DietRecordAPISource(get())
                }
                single {
                    WaterRecordAPISource(get())
                }
                single {
                    PoopRecordAPISource(get())
                }
                single {
                    FoodAPISource(get())
                }
            }
            val modelModule = module {
                single {
                    UserModel(get<UserAPISource>())
                }
                single {
                    DietRecordModel(get<DietRecordAPISource>())
                }
                single {
                    WaterRecordModel(get<WaterRecordAPISource>())
                }
                single {
                    PoopRecordModel(get<PoopRecordAPISource>())
                }
                single {
                    FoodModel(get<FoodAPISource>())
                }
            }
            val viewModelModule = module {
                viewModel {
                    MainViewModel(get())
                }
                viewModel {
                    DietRecordViewModel(get())
                }
                viewModel {
                    WaterRecordViewModel(get())
                }
                viewModel {
                    PoopRecordViewModel(get())
                }
                viewModel {
                    FoodViewModel(get())
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
