package tw.edu.ntub.imd.birc.rxandmvvm.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.model.UserData

class UserAdapter(val c: Context, val userList:ArrayList<UserData>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner  class UserViewHolder(val v: View):RecyclerView.ViewHolder(v){
        var name: TextView
        var mbNum:TextView
        var mMenus: ImageView

        init{

            name = v.findViewById<TextView>(R.id.mTitle)
            mbNum = v.findViewById<TextView>(R.id.mSubTitle)
            mMenus = v.findViewById(R.id.mMenus)
            mMenus.setOnClickListener{
                popupMenus(it)
            }
        }
        /** ok now set edit the item and delete item */
        private fun popupMenus(v: View) {
            val position = userList[adapterPosition]
            val popupMenus = PopupMenu(c,v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{
                        val v = LayoutInflater.from(c).inflate(R.layout.add_item,null)
                        val data = v.findViewById<EditText>(R.id.userName)
                        val text = v.findViewById<EditText>(R.id.Remark)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("新增"){
                                    dialog,_->
                                position.userName = data.text.toString()
                                position.userMb = text.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(c,"修改成功", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("取消"){
                                    dialog,_->
                                dialog.dismiss()

                            }
                            .create()
                            .show()

                        true
                    }
                    R.id.delete->{
                        /** set delete */
                        AlertDialog.Builder(c)
                            .setTitle("刪除")
                            . setIcon(R.drawable.ic_warning)
                            .setMessage("確定要刪除訊息")
                            .setPositiveButton("是"){
                                    dialog,_->
                                userList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c,"刪除成功", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("否"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item,parent,false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val newList = userList[position]
        holder.name.text = newList.userName
        holder.mbNum.text = newList.userMb
    }

    override fun getItemCount(): Int {
        return userList.size

    }



}