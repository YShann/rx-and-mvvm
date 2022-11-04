package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.app.Activity.RESULT_CANCELED
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import android.view.ViewGroup
import android.widget.*
import android.widget.DatePicker.OnDateChangedListener
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
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
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateDietRecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateDietRecordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var editName: EditText
    private lateinit var editDateTime: EditText
    private lateinit var spinnerPortionSize: Spinner
    private lateinit var editNote: EditText
    private lateinit var checkBoxGrains: CheckBox
    private lateinit var checkBoxMeatsAndProtein: CheckBox
    private lateinit var checkBoxMilkAndDairy: CheckBox
    private lateinit var checkBoxFats: CheckBox
    private lateinit var checkBoxVegetables: CheckBox
    private lateinit var checkBoxFruits: CheckBox
    private lateinit var editEnergy: EditText
    private lateinit var editCarbohydrate: EditText
    private lateinit var editProtein: EditText
    private lateinit var editSaturatedFat: EditText
    private lateinit var editFat: EditText
    private lateinit var createDietRecordArrow: ImageButton
    private lateinit var createDietRecordFinish: ImageButton
    private lateinit var createRecordHeadBack: CardView
    private lateinit var createRecordHeadImage: ImageView
    private lateinit var createRecordHeadText: TextView
    private var pathUri: String? = null
    private val viewModel: DietRecordViewModel by viewModel()
    private val calender: Calendar = Calendar.getInstance()
    private val IMAGE_DIRECTORY = "/encoded_image"
    private val GALLERY = 1
    private val CAMERA = 2

    private lateinit var cameraBtn: Button
    private lateinit var albumBtn: Button
    private lateinit var foodphotoView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_diet_record, container, false)
        editName = view.findViewById(R.id.edit_create_record_name)
        editDateTime = view.findViewById(R.id.edit_create_record_dateTime)
        spinnerPortionSize = view.findViewById(R.id.create_record_portionSize_spinner)
        editNote = view.findViewById(R.id.edit_create_record_note)
        checkBoxGrains = view.findViewById(R.id.checkBox_create_record_grains)
        checkBoxMeatsAndProtein = view.findViewById(R.id.checkBox_create_record_meatsAndProtein)
        checkBoxMilkAndDairy = view.findViewById(R.id.checkBox_create_record_milkAndDairy)
        checkBoxFats = view.findViewById(R.id.checkBox_create_record_fats)
        checkBoxVegetables = view.findViewById(R.id.checkBox_create_record_vegetables)
        checkBoxFruits = view.findViewById(R.id.checkBox_create_record_fruits)
        editEnergy = view.findViewById(R.id.edit_create_record_energy)
        editCarbohydrate = view.findViewById(R.id.edit_create_record_carbohydrate)
        editProtein = view.findViewById(R.id.edit_create_record_protein)
        editSaturatedFat = view.findViewById(R.id.edit_create_record_saturatedFat)
        editFat = view.findViewById(R.id.edit_create_record_fat)
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

        val adapter = activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.create_record_portionSize_spinner_list,
                android.R.layout.simple_spinner_item
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPortionSize.adapter = adapter
        spinnerPortionSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val type = parent?.getItemAtPosition(position).toString()
                println(type)
            }

        }

        createDietRecordArrow.setOnClickListener {
            requireActivity().run{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
        createDietRecordFinish.setOnClickListener {
            this.creatDietRecord()
        }

        return view
    }

//    //把圖片設定到foodPhoto
//    private fun handleCameraImage(bitmap: Bitmap) {
//        foodphotoView.setImageBitmap(bitmap)
//    }


    private fun creatDietRecord() {
        val one = "1"
        val zero = "0"
        val dateTime = editDateTime.text.toString()
        val seconds = calender.get(Calendar.SECOND)
        val foodName = editName.text.toString()
        val portionSize = (spinnerPortionSize.selectedItemPosition - 1).toString()
        val mealTime = "$dateTime:$seconds"
        val note = editNote.text.toString()
        val energy = editEnergy.text.toString()
        val fat = editFat.text.toString()
        val saturatedFat = editSaturatedFat.text.toString()
        val carbohydrate = editCarbohydrate.text.toString()
        val protein = editProtein.text.toString()
        val grains = if (checkBoxGrains.isChecked) one else zero
        val vegetables = if (checkBoxVegetables.isChecked) one else zero
        val meatsAndProtein = if (checkBoxMeatsAndProtein.isChecked) one else zero
        val milkAndDairy = if (checkBoxMilkAndDairy.isChecked) one else zero
        val fruits = if (checkBoxFruits.isChecked) one else zero
        val fats = if (checkBoxFats.isChecked) one else zero


        val hashMap: HashMap<String, RequestBody> = HashMap<String, RequestBody>()
        hashMap["foodName"] = foodName.toRequestBody(MultipartBody.FORM)
        hashMap["portionSize"] = portionSize.toRequestBody(MultipartBody.FORM)
        hashMap["mealTime"] = mealTime.toRequestBody(MultipartBody.FORM)
        hashMap["note"] = note.toRequestBody(MultipartBody.FORM)
        hashMap["energy"] = energy.toRequestBody(MultipartBody.FORM)
        hashMap["fat"] = fat.toRequestBody(MultipartBody.FORM)
        hashMap["saturatedFat"] = saturatedFat.toRequestBody(MultipartBody.FORM)
        hashMap["carbohydrate"] = carbohydrate.toRequestBody(MultipartBody.FORM)
        hashMap["protein"] = protein.toRequestBody(MultipartBody.FORM)
        hashMap["grains"] = grains.toRequestBody(MultipartBody.FORM)
        hashMap["vegetables"] = vegetables.toRequestBody(MultipartBody.FORM)
        hashMap["meatsAndProtein"] = meatsAndProtein.toRequestBody(MultipartBody.FORM)
        hashMap["milkAndDairy"] = milkAndDairy.toRequestBody(MultipartBody.FORM)
        hashMap["fruits"] = fruits.toRequestBody(MultipartBody.FORM)
        hashMap["fats"] = fats.toRequestBody(MultipartBody.FORM)


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
                    requireActivity().run {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
                }
            })
    }

    private fun datePicker() {
        val picker = context?.let {
            DatePickerDialog(
                it,
                dateListener,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DATE)
            )
        }
        picker?.setCancelable(true)
        picker?.setCanceledOnTouchOutside(true)
        picker?.setButton(
            DialogInterface.BUTTON_POSITIVE, "確定"
        ) { date, which ->
            this.timePicker()
        }
        picker?.setButton(
            DialogInterface.BUTTON_NEGATIVE, "取消"
        ) { _, _ -> Log.d("Picker", "Cancel!") }
        picker?.show()
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

    private val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calender.set(year, month, day)
    }

    private val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        format("yyyy/MM/dd HH:mm", editDateTime)
    }

    private fun format(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        (view as EditText).setText(time.format(calender.time))
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
