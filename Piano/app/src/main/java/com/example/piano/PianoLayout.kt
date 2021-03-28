package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentPianoBinding
import kotlinx.android.synthetic.main.fragment_piano.view.*


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2")
    private val halfTones = listOf("C#", "D#", "E#", "F#", "G#", "A#", "B#", "C2#", "D2#", "E2#", "F2#")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()


        for (i in fullTones.indices){
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(fullTones[i])

            fullTonePianoKey.onFullToneKeyDown = {
                println("Piano key pressed: $it")
            }

            fullTonePianoKey.onFullToneKeyUp = {
                println("Piano key released: $it")
            }

            ft.add(view.FullTonePianoKeyLinearLayout.id, fullTonePianoKey,"note_$fullTonePianoKey")
        }


        for (i in halfTones.indices) {
            val halfTonePianoKey = HalfTonePianoKeyFragment.newInstance(halfTones[i])

            halfTonePianoKey.onHalfToneKeyDown = {
                println("Piano key pressed: $it")
            }

            halfTonePianoKey.onHalfToneKeyUp = {
                println("Piano key released: $it")
            }

            ft.add(view.HalfTonePianoKeyLinearLayout.id, halfTonePianoKey, "note_$halfTonePianoKey")
        }
        ft.commit()
        return view
    }

}