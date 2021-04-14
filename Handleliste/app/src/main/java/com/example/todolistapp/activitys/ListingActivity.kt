package com.example.todolistapp.activitys

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistapp.*
import com.example.todolistapp.adapter.SuperListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_listing.*
import org.json.JSONObject


class ListingActivity : AppCompatActivity() {
    var allItemList = ArrayList<SuperListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)

        getAllItems()

        fbAddItem.setOnClickListener {
            showPasswordDialog()
        }

    }

    private fun getAllItems() {
        getAllItemsFromFirebaseDb(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allItemList.clear()
                for (item in dataSnapshot.children) {
                    val jsonObject = JSONObject(item.value.toString())
                    allItemList.add(
                        SuperListItem(
                            item.key ?: "",
                            jsonObject.getString("title"),
                            getAllChild(jsonObject)
                        )
                    )

                }
                rcvToDOList.adapter =
                    SuperListAdapter(allItemList, ::deleteItemFromFirebase, ::startNextActivity)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Something Wrong with Listing")
            }

        })
    }

    private fun getAllChild(jsonObject: JSONObject): ArrayList<ChildItem> {
        val otherChild = ArrayList<ChildItem>()
        for (key in jsonObject.keys()) {
            if (key != "title") {
                val otherItemData = jsonObject.getJSONObject(key)
                otherChild.add(
                    ChildItem(
                        key,
                        otherItemData.getString("title"),
                        otherItemData.getBoolean("checked")
                    )
                )
            }
        }
        return otherChild
    }

    private fun showPasswordDialog() {
        val dialog = Dialog(this, R.style.custom_dialog_theme)
        dialog.setContentView(R.layout.dialog_heading_input)
        val titleInputLayout = dialog.findViewById<TextInputLayout>(R.id.title_input_layout)
        val etTitle = dialog.findViewById<EditText>(R.id.et_title)

        dialog.findViewById<MaterialButton>(R.id.btn_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.findViewById<MaterialButton>(R.id.btn_done).setOnClickListener {
            if (etTitle.text.isNotBlank()) {
                addItemInDB(etTitle.text.toString())
                dialog.dismiss()
            } else {
                titleInputLayout.error = "Pls Enter Value"
            }
        }
        dialog.show()
    }

    private fun addItemInDB(title: String) {
        val listReference: DatabaseReference = dbReference.child("SuperList").push()
        listReference.setValue(TitleItem(title))
        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
    }

    fun deleteItemFromFirebase(id: String) {
        dbReference.child("SuperList").child(id).removeValue()
    }

    private fun startNextActivity(id: String, title: String) {
        startActivity(Intent(this, ItemDetailActivity::class.java).apply {
            putExtra("ID", id)
            putExtra("Title", title)
        })
    }

}