package com.example.onechamp.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.onechamp.R
import com.example.onechamp.databinding.ActivityMainBinding
import com.example.onechamp.domain.model.DataDTO
import com.example.onechamp.presentation.components.CardImageView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    var mMediaPlayer: MediaPlayer? = null
    var isClicked = false

    lateinit var binding: ActivityMainBinding
    val viewModel:MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setClickListeners()

        viewModel.getAnswerCount().observe(this){
            if (it == 0)
                viewModel.onEvent(Events.ShowAlert("Be a Champ","Press Start button.","Start"))
            else if (it == 9){
                viewModel.onEvent(Events.ShowAlert("You are a Champion","Task completed","Ok" ))
            }
        }

        lifecycleScope.launch {
            viewModel.eventFlow.collectLatest {event->
                when(event){
                    is MainViewModel.UiEvent.ShowAlert -> setAlert(event.title,event.message,event.buttonText)
                    is MainViewModel.UiEvent.slideUpEvent -> {
                        isClicked = false
                        for (i in 0 until event.data.size) {
                            val view = getHoleView(event.data[i].holeNum)
                            slideUp(view!!, event.data[i].dto)
                        }
                        playSound(event.data[0].dto.audio)
                        delay(4000)
                        Log.e("click",isClicked.toString())
                        if (!isClicked) {
                            viewModel.onEvent(Events.SlideDownEvent(false,event.data))
                        }
                    }

                    is MainViewModel.UiEvent.slideDownEvent -> {
                        for (i in 0 until event.data.size) {
                            val view = getHoleView(event.data[i].holeNum)
                            slideDown(view!!)
                        }
                            viewModel.callSlideUpEvent()
                    }

                    is MainViewModel.UiEvent.holeClickedEvent -> {
                        isClicked = true
                        if (event.holeNum == viewModel.cardStateList[0].holeNum){
                            showTick()
                        }else{
                            viewModel.onEvent(Events.SlideDownEvent(false,viewModel.cardStateList))
                        }
                    }
                }

            }
        }
//        setAlert("Be a Champ","Press Start button.","Start")

    }

    private fun showTick() {
        lifecycleScope.launch {
            val view = getHoleView(viewModel.cardStateList[0].holeNum)
            playSound(viewModel.cardStateList[0].dto.audio)
            delay(1000)
            view!!.setImage(R.drawable.tick)
            playSound(R.raw.correct)
            delay(1000)
            viewModel.onEvent(Events.SlideDownEvent(true,viewModel.cardStateList))
        }

    }

    private fun setClickListeners() {
        binding.cvHole1.setOnClickListener { viewModel.onEvent(Events.HoleClicked(0)) }
        binding.cvHole2.setOnClickListener { viewModel.onEvent(Events.HoleClicked(1)) }
        binding.cvHole3.setOnClickListener { viewModel.onEvent(Events.HoleClicked(2)) }
        binding.cvHole4.setOnClickListener { viewModel.onEvent(Events.HoleClicked(3)) }
        binding.cvHole5.setOnClickListener { viewModel.onEvent(Events.HoleClicked(4)) }
    }

    private fun getHoleView(holeNum: Int): CardImageView? {
        var view:CardImageView? = null
        when(holeNum){
            0-> view = binding.cvHole1
            1-> view = binding.cvHole2
            2-> view = binding.cvHole3
            3-> view = binding.cvHole4
            4-> view = binding.cvHole5
        }
        return view
    }

    private fun slideUp(view: CardImageView, data:DataDTO) {
        view.setImage(data.drawable)
        view.visibility = View.VISIBLE
        val animate = TranslateAnimation(
            0f, 0f,
            view.height.toFloat(), 0f
        )
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)


    }

    private fun slideDown(view: CardImageView) {
        view.visibility = View.INVISIBLE
        val animate = TranslateAnimation(
            0f, 0f,
            0f, view.height.toFloat()
        )
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)


    }

    private fun setAlert(title:String,message:String, buttonText:String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(title)
        builder.setMessage(
            message
        )
        builder.setPositiveButton(buttonText,
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                if (viewModel.answerCountLiveData.value == 0)
                    viewModel.callSlideUpEvent()
            })
        val diag: AlertDialog = builder.create()
        diag.show()
    }
    fun playSound(audio:Int) {

        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, audio)
            mMediaPlayer!!.isLooping = false
            mMediaPlayer!!.start()
        }
        mMediaPlayer?.setOnCompletionListener {
            it.release()
            mMediaPlayer = null
        }
    }
}