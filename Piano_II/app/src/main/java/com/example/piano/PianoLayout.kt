package com.example.piano

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.*
import kotlinx.android.synthetic.main.fragment_piano.view.*
import java.io.*


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2")
    private val halfTones = listOf("C#", "D#", "E#", "F#", "G#", "A#", "B#", "C2#", "D2#", "E2#", "F2#")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root
        val fm = childFragmentManager
        val ft = fm.beginTransaction()
        var song = arrayListOf<String>()
        var nameOfSong = ""
        var startingTime:Long = 0
        var startButtonClicked = false

        view.startSongButton.setOnClickListener() {
            startingTime = System.currentTimeMillis()
            startButtonClicked = true
        }

            for (i in fullTones.indices) {
                val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(fullTones[i])
                var keyPressStart: Long = System.currentTimeMillis()

                fullTonePianoKey.onFullToneKeyDown = {
                    println("Piano key pressed: $it")
                    keyPressStart = System.currentTimeMillis() - startingTime
                }

                fullTonePianoKey.onFullToneKeyUp = {
                    println("Piano key released: $it")
                    val keyPressEnd: Long = System.currentTimeMillis() - startingTime
                    val note = Note(it, keyPressStart.toString(), keyPressEnd.toString())
                    if (startButtonClicked){
                        song.add(note.toString())
                    }
                }

                ft.add(view.FullTonePianoKeyLinearLayout.id, fullTonePianoKey, "note_$fullTonePianoKey")

            }


            for (i in halfTones.indices) {
                val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(halfTones[i])
                var keyPressStart: Long = System.currentTimeMillis()

                halfTonePianoKey.onHalfToneKeyDown = {
                    println("Piano key pressed: $it")
                    keyPressStart = System.currentTimeMillis() - startingTime

                }

                halfTonePianoKey.onHalfToneKeyUp = {
                    println("Piano key released: $it")
                    val keyPressEnd: Long = System.currentTimeMillis() - startingTime
                    val note = Note(it, keyPressStart.toString(), keyPressEnd.toString())
                    if (startButtonClicked){
                        song.add(note.toString())
                    }
                }

                ft.add(view.HalfTonePianoKeyLinearLayout.id, halfTonePianoKey, "note_$halfTonePianoKey")
            }
            ft.commit()


        view.NameOfSong.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(s: CharSequence, start:Int, count: Int, after: Int){

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
                nameOfSong = NameOfSong.text.toString()
            }
        })

        view.saveSongButton.setOnClickListener(){
            val path = this.context?.filesDir
            val fileExists = File(path, "$nameOfSong.song").exists()

            if (fileExists){
                println("File name already exists.")
            }
            else if (nameOfSong.isBlank()){
                println("Give a name to the song")
            }
            else if (song.count() == 0){
                println("The song has no notes")
            }
            else if (path == null){
                println("There is no storage path")
            }
            else {
                FileOutputStream(File(path, "$nameOfSong.song"), true).bufferedWriter().use { out ->
                    song.forEach {
                        out.write(it)
                    }
                    println("Your song has been saved.")
                    nameOfSong = ""
                    NameOfSong.text.clear()
                    song.clear()
                    startButtonClicked = false
                }
            }
        }


        view.resetSongButton.setOnClickListener(){
            song.clear()
            startButtonClicked = false
        }

        return view
    }

}