package com.example.onechamp.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onechamp.R
import com.example.onechamp.data.DataRepositoryImpl
import com.example.onechamp.domain.model.DataDTO
import com.example.onechamp.domain.repository.DataRepository
import com.example.onechamp.domain.use_case.GetDataUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Random

class MainViewModel :ViewModel(){
    val repository:DataRepository = DataRepositoryImpl()
    val getDataUseCase = GetDataUseCase(repository)

    val randomGenerator = Random()

    val answerCountLiveData:MutableLiveData<Int> = MutableLiveData(0)
    fun getAnswerCount():LiveData<Int> = answerCountLiveData


    var dataList = ArrayList<DataDTO>()
    var cardStateList = ArrayList<CardStateModel>()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        dataList.addAll(getDataUseCase.invoke())
        Log.e("id",dataList[2].id.toString())
    }

    fun onEvent(events: Events){
        when(events){
            is Events.ShowAlert -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.ShowAlert(events.title,events.message,events.buttonText))
                }
            }
            is Events.SlideUpEvent -> {
                viewModelScope.launch {
                    cardStateList = events.data
                    _eventFlow.emit(UiEvent.slideUpEvent(events.data))
                }
            }

            is Events.SlideDownEvent -> {
                viewModelScope.launch {
                    if (events.isRight) {
                        incrementAnswerCount()
                        Log.e("increment","true")
                    }
                    if (answerCountLiveData.value!! < 9)
                        _eventFlow.emit(UiEvent.slideDownEvent(events.isRight,events.data))
                }
            }

            is Events.HoleClicked -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.holeClickedEvent(events.hole_number))
                }
            }
        }
    }

    fun callSlideUpEvent(){
        val item = chooseRandomItem(dataList)
        Log.e("itemSize",item.size.toString())
        onEvent(Events.SlideUpEvent(item))
    }

    private fun incrementAnswerCount() {
        val i = answerCountLiveData.value
        answerCountLiveData.value = i?.plus(1)
    }

    fun chooseRandomItem(list:ArrayList<DataDTO>):ArrayList<CardStateModel>{
        val mlist = ArrayList<CardStateModel>()
        var n = answerCountLiveData.value!!/3
        val selectedHoles = ArrayList<Int>()
        val selectedDTO = ArrayList<DataDTO>()
        Log.e("hg",answerCountLiveData.value.toString())
        do {
            val index: Int = randomGenerator.nextInt(list.size)
            val holeNum = chooseRandomHoleNumber()
            if (!selectedDTO.contains(list[index])&&!selectedHoles.contains(holeNum)) {
                mlist.add(CardStateModel(holeNum, list[index]))
                selectedDTO.add(list[index])
                selectedHoles.add(holeNum)
                n -= 1
            }

        }while (n >= 0)
        return mlist
    }

    fun chooseRandomHoleNumber():Int{
        return randomGenerator.nextInt(4)
    }

    sealed class UiEvent{
        data class slideUpEvent(val data:ArrayList<CardStateModel>):UiEvent()

        data class slideDownEvent(val isright:Boolean,val data:List<CardStateModel>):UiEvent()

        data class holeClickedEvent(val holeNum: Int):UiEvent()

        data class ShowAlert(val title:String,val message:String,val buttonText:String):UiEvent()
    }


}