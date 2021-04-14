package com.example.todolistapp.activitys

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolistapp.*
import com.example.todolistapp.adapter.OtherItemListAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_item_detail.*
import org.json.JSONObject

class ItemDetailActivity : AppCompatActivity() {
    var selectedItemID: String = ""
    val allOtherItem = ArrayList<ChildItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        selectedItemID = intent.getStringExtra("ID") ?: ""
        tvTitle.text = intent.getStringExtra("Title") ?: ""

        getItemDetail(selectedItemID)
        fbAddDetailItem.setOnClickListener {
            showPasswordDialog()
        }

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
        val itemReference =
            FirebaseDatabase.getInstance().getReference("SuperList").child(selectedItemID)
        itemReference.push().setValue(DetailItem(title, false))
        Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT).show()
    }

    private fun getItemDetail(ItemID: String) {
        getAllItemDetailFromFirebaseDb(ItemID, object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allOtherItem.clear()
                for (item in dataSnapshot.children) {
                    if (item.key != "title") {
                        val jsonObject = JSONObject(item.value.toString())
                        allOtherItem.add(
                            ChildItem(
                                item.key ?: "",
                                jsonObject.getString("title"),
                                jsonObject.getBoolean("checked")
                            )
                        )
                    }
                }
                progressbar.progress =
                    if (allOtherItem.isNotEmpty()) {
                        (allOtherItem.filter { it.isChecked }.size * 100) / allOtherItem.size
                    } else 100
                Log.d("selected", allOtherItem.size.toString())
                rcvItemDetail.adapter =
                    OtherItemListAdapter(
                        allOtherItem,
                        ::deleteItemFromFirebase,
                        ::updateItemFromFirebase
                    )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Something Wrong with Detail")
            }

        })
    }

    fun deleteItemFromFirebase(id: String) {
        dbReference.child("SuperList").child(selectedItemID).child(id).removeValue()
        Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show()
    }

    private fun updateItemFromFirebase(id: String, title: String, isChecked: Boolean) {
        dbReference.child("SuperList").child(selectedItemID).child(id)
            .setValue(DetailItem(title, isChecked))
        Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
    }

}