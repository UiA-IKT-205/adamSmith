package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentHalfTonePianoKeyBinding
import kotlinx.android.synthetic.main.fragment_half_tone_piano_key.view.*

class HalfTonePianoKeyFragment : Fragment() {
    private var _binding:FragmentHalfTonePianoKeyBinding? = null
    private val binding get() = _binding!!
    private lateinit var note:String

    var onHalfToneKeyDown:((note:String) -> Unit)? = null
    var onHalfToneKeyUp:((note:String) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = if (it.getString("NOTE") != null){
                it.getString("NOTE").toString()
            } else{
                "feil"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHalfTonePianoKeyBinding.inflate(inflater)
        val br = binding.root

        br.halfToneKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@HalfTonePianoKeyFragment.onHalfToneKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@HalfTonePianoKeyFragment.onHalfToneKeyUp?.invoke(note)
                    MotionEvent.ACTION_CANCEL -> this@HalfTonePianoKeyFragment.onHalfToneKeyUp?.invoke(note)
                }
                return true
            }
        })
        return br
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            HalfTonePianoKeyFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}