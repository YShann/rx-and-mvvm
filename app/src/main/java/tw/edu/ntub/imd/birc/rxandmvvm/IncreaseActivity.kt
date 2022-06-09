package tw.edu.ntub.imd.birc.rxandmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import tw.edu.ntub.imd.birc.rxandmvvm.model.UserData
import tw.edu.ntub.imd.birc.rxandmvvm.view.UserAdapter

class IncreaseActivity : AppCompatActivity() {
    private lateinit var addsBtn: FloatingActionButton
    private lateinit var recv: RecyclerView
    private lateinit var userList: ArrayList<UserData>
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_increase)
        /** set List */
        userList =ArrayList()
        /** set find Id */
        addsBtn = findViewById(R.id.addingBtn)
        recv = findViewById(R.id.mRecycler)
        /** set Adapter*/
        userAdapter = UserAdapter(this,userList)
        /**setRecycler view Adapter*/
        recv.layoutManager = LinearLayoutManager(this)
        recv.adapter = userAdapter
        /** set Dialog */
        addsBtn.setOnClickListener {
            addInfo()
        }
    }
    private fun addInfo() {
        val inflater = LayoutInflater.from(this)
        val v = inflater.inflate(R.layout.add_item,null)
        /** set view */
        val userName = v.findViewById<EditText>(R.id.userName)
        val Remark = v.findViewById<EditText>(R.id.Remark)

        val addDialog = AlertDialog.Builder(this)

        addDialog.setView(v)
        addDialog.setPositiveButton("新增"){
                dialog,_->
            val data = userName.text.toString()
            val text = Remark.text.toString()
            userList.add(UserData("$data","$text"))
            userAdapter.notifyDataSetChanged()
            Toast.makeText(this,"新增成功", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("取消"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(this,"取消", Toast.LENGTH_SHORT).show()
        }

        addDialog.create()
        addDialog.show()
    }
    /** ok now run this */

}