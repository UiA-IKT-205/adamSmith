package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentFullTonePianoKeyBinding
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.*

class FullTonePianoKeyFragment : Fragment() {
    private var _binding:FragmentFullTonePianoKeyBinding? = null
    private val binding get() = _binding!!
    private lateinit var note:String

    var onFullToneKeyDown:((note:String) -> Unit)? = null
    var onFullToneKeyUp:((note:String) -> Unit)? = null


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

        _binding = FragmentFullTonePianoKeyBinding.inflate(inflater)
        val br = binding.root

        br.fullToneKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@FullTonePianoKeyFragment.onFullToneKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@FullTonePianoKeyFragment.onFullToneKeyUp?.invoke(note)
                    MotionEvent.ACTION_CANCEL -> this@FullTonePianoKeyFragment.onFullToneKeyUp?.invoke(note)
                }
                return true
            }

        })
        return br
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            FullTonePianoKeyFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}