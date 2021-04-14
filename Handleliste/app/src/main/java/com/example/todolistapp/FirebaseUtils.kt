package com.example.todolistapp

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var dbReference = FirebaseDatabase.getInstance().reference

fun getAllItemsFromFirebaseDb(listener: ValueEventListener) {
    val countryRef = FirebaseDatabase.getInstance().getReference("SuperList")
    countryRef.keepSynced(true)
    countryRef.addValueEventListener(listener)
}

fun getAllItemDetailFromFirebaseDb(ItemID: String, listener: ValueEventListener) {
    val countryRef = FirebaseDatabase.getInstance().getReference("SuperList").child(ItemID)
    countryRef.keepSynced(true)
    countryRef.addValueEventListener(listener)
}

data class SuperListItem(
    var id: String,
    var heading: String,
    var detailItem: ArrayList<ChildItem>
)

data class ChildItem(
    var id: String,
    var title: String,
    var isChecked: Boolean
)

data class DetailItem(
    var title: String,
    var isChecked: Boolean
)

data class TitleItem(
    var title: String
)


