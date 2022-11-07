package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.constant.UrlConstant
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.DietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DietRecordDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DietRecordDetailFragment(val dietRecord: DietRecord?) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dietRecordDetailArrow: ImageButton
    private lateinit var imageDietRecordDetail: ImageView
    private lateinit var detailName: TextView
    private lateinit var detailDateTime: TextView
    private lateinit var label: TextView
    private lateinit var foodKind: TextView
    private lateinit var note: TextView
    private lateinit var energy: TextView
    private lateinit var carbohydrate: TextView
    private lateinit var protein: TextView
    private lateinit var saturatedFat: TextView
    private lateinit var fat: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diet_record_detail, container, false)
        dietRecordDetailArrow = view.findViewById(R.id.diet_record_detail_arrow)
        imageDietRecordDetail = view.findViewById(R.id.image_diet_record_detail)
        detailName = view.findViewById(R.id.text_diet_record_detail_name)
        detailDateTime = view.findViewById(R.id.text_diet_record_detail_dateTime)
        label = view.findViewById(R.id.label_diet_record_detail)
        foodKind = view.findViewById(R.id.kind_diet_record_detail)
        note = view.findViewById(R.id.text_diet_record_detail_note)
        energy = view.findViewById(R.id.text_diet_record_detail_energy)
        carbohydrate = view.findViewById(R.id.text_diet_record_detail_carbohydrate)
        protein = view.findViewById(R.id.text_diet_record_detail_protein)
        saturatedFat = view.findViewById(R.id.text_diet_record_detail_saturatedFat)
        fat = view.findViewById(R.id.text_diet_record_detail_fat)

        detailName.text= dietRecord?.foodName
        detailDateTime.text = dietRecord?.mealTime?.substring(0 until 16)
        note.text = dietRecord?.note
        energy.text = dietRecord?.energy.toString()+" Cal"
        carbohydrate.text = dietRecord?.carbohydrate.toString()+" g"
        protein.text = dietRecord?.protein.toString()+" g"
        saturatedFat.text = dietRecord?.saturatedFat.toString()+" g"
        fat.text = dietRecord?.fat.toString()+" g"
        if(dietRecord?.portionSize!! <5){
            label.text = dietRecord.portionSize.toString()
            label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_red)
        }else if(dietRecord.portionSize!! in 5..6){
            label.text = dietRecord.portionSize.toString()
            label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_yellow)
        }else if(dietRecord.portionSize!! in 7..8){
            label.text = dietRecord.portionSize.toString()
            label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_green)
        }else{
            label.text = dietRecord.portionSize.toString()
            label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_red)
        }
//        when (dietRecord?.portionSize) {
//            "0" -> {
//                label.text = "少量"
//                label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_yellow)
//            }
//            "1" -> {
//                label.text = "適中"
//                label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_green)
//            }
//            "2" -> {
//                label.text = "飽食"
//                label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_yellow)
//            }
//            else -> {
//                label.text = "過量"
//                label.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_red)
//            }
//        }

        val grains = "全榖雜糧類"
        val meatsAndProtein = "蛋豆魚肉類"
        val vegetables = "蔬菜類"
        val fruits = "水果類"
        val milkAndDairy = "乳品類"
        val fats = "油脂與堅果種子類"
        var foodKindTextList: MutableList<String> = mutableListOf<String>()

        if (dietRecord?.grains == "1") {
            foodKindTextList.add(grains)
        }
        if (dietRecord?.meatsAndProtein == "1") {
            foodKindTextList.add(meatsAndProtein)
        }
        if (dietRecord?.vegetables == "1") {
            foodKindTextList.add(vegetables)

        }
        if (dietRecord?.fruits == "1") {
            foodKindTextList.add(fruits)

        }
        if (dietRecord?.milkAndDairy == "1") {
            foodKindTextList.add(milkAndDairy)

        }
        if (dietRecord?.fats == "1") {
            foodKindTextList.add(fats)

        }
        foodKind.text=foodKindTextList.toString().replace(",","、").replace("[","").replace("]","")

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var imageBitmap: Bitmap? = null
        executor.execute {
            val imageURL = dietRecord?.imageUrl!!.replace("http://localhost:8080/", UrlConstant.BASE_URL)

            try {
                val `in` = java.net.URL(imageURL).openStream()
                imageBitmap = BitmapFactory.decodeStream(`in`)

                handler.post {
                    imageDietRecordDetail.setImageBitmap(imageBitmap)
                }
            }

            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dietRecordDetailArrow.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, HomeActivity.recordFragment)
                ?.commit()
        }


        return view
    }


}