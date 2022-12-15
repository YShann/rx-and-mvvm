package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.FoodItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.FoodViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateDietRecordFragment() : Fragment() {
    private lateinit var editName: AutoCompleteTextView
    private lateinit var createRecordRecyclerInvisibility: RecyclerView
    private lateinit var editDateTime: EditText
    private lateinit var editContent: EditText
    private lateinit var editNote: EditText
    private lateinit var checkBoxGrains: CheckBox
    private lateinit var checkBoxMeatsAndProtein: CheckBox
    private lateinit var checkBoxMilkAndDairy: CheckBox
    private lateinit var checkBoxFats: CheckBox
    private lateinit var checkBoxVegetables: CheckBox
    private lateinit var checkBoxFruits: CheckBox
    private lateinit var createDietRecordArrow: ImageButton
    private lateinit var createDietRecordFinish: ImageButton
    private lateinit var createRecordHeadBack: CardView
    private lateinit var createRecordHeadImage: ImageView
    private lateinit var createRecordHeadText: TextView
    private var pathUri: String? = null
    private val viewModel: DietRecordViewModel by viewModel()
    private val foodViewModel: FoodViewModel by viewModel()
    private val calender: Calendar = Calendar.getInstance()
    private val IMAGE_DIRECTORY = "/encoded_image"
    private val GALLERY = 1
    private val CAMERA = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_diet_record, container, false)
        editName = view.findViewById(R.id.edit_create_record_name)
        createRecordRecyclerInvisibility =
            view.findViewById(R.id.create_record_recycler_invisibility)
        editDateTime = view.findViewById(R.id.edit_create_record_dateTime)
        editNote = view.findViewById(R.id.edit_create_record_note)
        editContent = view.findViewById(R.id.edit_create_record_content)
        checkBoxGrains = view.findViewById(R.id.checkBox_create_record_grains)
        checkBoxMeatsAndProtein = view.findViewById(R.id.checkBox_create_record_meatsAndProtein)
        checkBoxMilkAndDairy = view.findViewById(R.id.checkBox_create_record_milkAndDairy)
        checkBoxFats = view.findViewById(R.id.checkBox_create_record_fats)
        checkBoxVegetables = view.findViewById(R.id.checkBox_create_record_vegetables)
        checkBoxFruits = view.findViewById(R.id.checkBox_create_record_fruits)
        createDietRecordArrow = view.findViewById(R.id.create_diet_record_arrow)
        createDietRecordFinish = view.findViewById(R.id.create_diet_record_finish)
        createRecordHeadBack = view.findViewById(R.id.create_record_headBack)
        createRecordHeadImage = view.findViewById(R.id.image_create_record_takePhoto)
        createRecordHeadText = view.findViewById(R.id.textView_create_record_takePhoto)

        createRecordHeadBack.setOnClickListener {
            this.showPictureDialog()
        }

        this.getNowDateTime()
        editDateTime.setOnClickListener {
            this.datePicker()
        }

        createDietRecordArrow.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        createDietRecordFinish.setOnClickListener {
            this.creatDietRecord()
        }


        createRecordRecyclerInvisibility.visibility = View.INVISIBLE

        val searchList: ArrayList<String> = ArrayList()
        val searchAdapter = ObservableAdapter(
            foodViewModel.searchAll()
                .mapSourceState {
                    it.data.map { food ->
                        activity?.runOnUiThread(java.lang.Runnable {
                            searchList.add(food.name!!.plus(" ${food.energy}kcal"))
                        })
                    }
                    it.data.map { food ->
                        FoodItem(food)
                    }
                }
        )
        searchAdapter.attachToRecyclerView(createRecordRecyclerInvisibility)
        val arrayAdapter =
            ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_dropdown_item_1line,
                searchList
            )
        editName.setAdapter(arrayAdapter)
        editName.threshold = 1
        editName.onFocusChangeListener = OnFocusChangeListener { _, _ ->
            editName.setAdapter(arrayAdapter)
            editName.threshold = 1
        }

        editName.setOnItemClickListener { parent, view, position, id ->
            val searchName = editName.text.split(" ")[0]
            val searchNameAdapter = ObservableAdapter(
                foodViewModel.getFood(searchName)
                    .mapSourceState {
                        it.data.map { food ->
                            activity?.runOnUiThread(java.lang.Runnable {
                                checkBoxGrains.isChecked = food.grains == "1"
                                checkBoxVegetables.isChecked = food.vegetables == "1"
                                checkBoxMeatsAndProtein.isChecked = food.meatsAndProtein == "1"
                                checkBoxMilkAndDairy.isChecked = food.milkAndDairy == "1"
                                checkBoxFruits.isChecked = food.fruits == "1"
                                checkBoxFats.isChecked = food.fats == "1"
                            })
                        }
                        it.data.map { food ->
                            FoodItem(food)
                        }
                    }
            )
            searchNameAdapter.attachToRecyclerView(createRecordRecyclerInvisibility)
        }

        return view
    }


    private fun creatDietRecord() {
        val sharedPreference = this.activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val account = sharedPreference?.getString("account", "defaultAccount")
        val one = "1"
        val zero = "0"
        val mealDate = editDateTime.text.toString().substring(0, 10)
        val seconds = calender.get(Calendar.SECOND)
        val foodName = editName.text.toString()
        val foodContent =
            if (editContent.text.toString().isNotEmpty()) editContent.text.toString() else "未填寫"
        val mealTime = "${editDateTime.text.toString().substring(11, 16)}:$seconds"
        val note = if (editNote.text.toString().isNotEmpty()) editNote.text.toString() else "未填寫"
        val grains = if (checkBoxGrains.isChecked) one else zero
        val vegetables = if (checkBoxVegetables.isChecked) one else zero
        val meatsAndProtein = if (checkBoxMeatsAndProtein.isChecked) one else zero
        val milkAndDairy = if (checkBoxMilkAndDairy.isChecked) one else zero
        val fruits = if (checkBoxFruits.isChecked) one else zero
        val fats = if (checkBoxFats.isChecked) one else zero

        val hashMap: HashMap<String, RequestBody> = HashMap<String, RequestBody>()
        hashMap["foodName"] = foodName.toRequestBody(MultipartBody.FORM)
        hashMap["foodContent"] = foodContent.toRequestBody(MultipartBody.FORM)
        hashMap["mealTime"] = mealTime.toRequestBody(MultipartBody.FORM)
        hashMap["mealDate"] = mealDate.toRequestBody(MultipartBody.FORM)
        hashMap["note"] = note.toRequestBody(MultipartBody.FORM)
        hashMap["grains"] = grains.toRequestBody(MultipartBody.FORM)
        hashMap["vegetables"] = vegetables.toRequestBody(MultipartBody.FORM)
        hashMap["meatsAndProtein"] = meatsAndProtein.toRequestBody(MultipartBody.FORM)
        hashMap["milkAndDairy"] = milkAndDairy.toRequestBody(MultipartBody.FORM)
        hashMap["fruits"] = fruits.toRequestBody(MultipartBody.FORM)
        hashMap["fats"] = fats.toRequestBody(MultipartBody.FORM)
        hashMap["account"] = account!!.toRequestBody(MultipartBody.FORM)


        val file: File = File(pathUri)
        val fileRequestBody: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("imageFile", file.name, fileRequestBody)

        viewModel.createDietRecord(hashMap, filePart)
            .enqueue(object : Callback<ResponseBody<DietRecord>> {
                override fun onResponse(
                    call: Call<ResponseBody<DietRecord>>,
                    response: Response<ResponseBody<DietRecord>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("Retrofit", "Success")

                    }
                }

                override fun onFailure(call: Call<ResponseBody<DietRecord>>, t: Throwable) {
                    Log.d("Retrofit", t.stackTraceToString())
                    Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT).show()
                    requireActivity().run {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                    editName.setText("")
                    editContent.setText("")
                    editNote.setText("")
                    checkBoxGrains.isChecked = false
                    checkBoxMeatsAndProtein.isChecked = false
                    checkBoxMilkAndDairy.isChecked = false
                    checkBoxFats.isChecked = false
                    checkBoxVegetables.isChecked = false
                    checkBoxFruits.isChecked = false
                }
            })
        this.getNowDateTime()

    }


    private fun datePicker() {
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        var plusZero = "0"
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, yearPick, monthOfYear, dayOfMonth ->
                plusZero = if (dayOfMonth < 10) {
                    plusZero.plus(dayOfMonth)
                } else {
                    dayOfMonth.toString()
                }
                val dat = (yearPick.toString() + "/" + (monthOfYear + 1) + "/" + plusZero)
                editDateTime.setText(dat)
                this.timePicker()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    private fun timePicker() {
        val timePicker = context?.let {
            TimePickerDialog(
                it,
                timeListener,  // instead of a listener
                calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), true
            )
        }
        timePicker?.setCancelable(true)
        timePicker?.setCanceledOnTouchOutside(true)
        timePicker?.show()
    }


    private val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        format("yyyy/MM/dd HH:mm", editDateTime)
    }

    @SuppressLint("SetTextI18n")
    private fun format(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        val text = editDateTime.text.toString()
        (view as EditText).setText(text + " " + time.format(calender.time).substring(11, 16))
    }

    private fun getNowDateTime() {
        val time = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN)
        editDateTime.setText(time.format(Date()))
    }

    private fun showPictureDialog() {
        val pictureDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        pictureDialog.setTitle("選取相片")
        val pictureDialogItems = arrayOf(
            "從圖庫選取",
            "使用相機"
        )
        pictureDialog.setItems(pictureDialogItems,
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    0 -> choosePhotoFromGallary()
                    1 -> takePhotoFromCamera()
                }
            })
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(context?.contentResolver, contentURI)
                    val path = this.saveImage(bitmap)
                    pathUri = data?.data?.let { getPathFromUri(it) }
                    createRecordHeadImage.setImageBitmap(bitmap)
                    createRecordHeadText.text = ""
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        "失敗",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!["data"] as Bitmap?
            createRecordHeadImage.setImageBitmap(thumbnail)
            createRecordHeadText.text = ""
            saveImage(thumbnail)
        }
    }

    private fun saveImage(myBitmap: Bitmap?): String {
        val bytes = ByteArrayOutputStream()
        myBitmap?.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory =
            File(Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY)
        if (!wallpaperDirectory.exists()) {  // have the object build the directory structure, if needed.
            wallpaperDirectory.mkdirs()
        }
        try {
            val f =
                File(wallpaperDirectory, Calendar.getInstance().timeInMillis.toString() + ".jpg")
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context, arrayOf(f.path), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::---&gt;" + f.absolutePath)
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA) ?: null
        cursor?.moveToFirst()
        val result: String = column_index?.let { cursor?.getString(it) } ?: ""
        cursor?.close()
        return result
    }


}
