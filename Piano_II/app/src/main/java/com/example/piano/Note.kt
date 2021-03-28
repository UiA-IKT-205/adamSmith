package com.example.piano

class Note(val note: String?, val keyPressStart: String, val keyPressEnd: String) {
    override fun toString(): String {
        return ("$note, $keyPressStart, $keyPressEnd \n")
    }
}