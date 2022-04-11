package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.View
import android.widget.TextView
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

class UserItem(private val user: User) : AbstractViewItem() {
    override val layoutId: Int = R.layout.recycler_item_user
    override fun bindView(adapter: Adapter, view: View) {
        val name = view.findViewById<TextView>(R.id.recycler_item_user_name)
        val email = view.findViewById<TextView>(R.id.recycler_item_user_email)
        val phone = view.findViewById<TextView>(R.id.recycler_item_user_phone)
        name.text = user.name
        email.text = user.email
        phone.text = user.phone
    }

    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

}