package com.example.onechamp.presentation

import com.example.onechamp.domain.model.DataDTO

sealed class Events{
    data class SlideUpEvent(val data:ArrayList<CardStateModel>):Events()
    data class SlideDownEvent(val isRight:Boolean,val data:ArrayList<CardStateModel>):Events()
    data class HoleClicked(val hole_number:Int):Events()
    data class ShowAlert(val title:String,val message:String,val buttonText:String):Events()
}