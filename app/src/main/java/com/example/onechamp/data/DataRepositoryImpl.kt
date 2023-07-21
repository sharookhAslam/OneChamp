package com.example.onechamp.data

import com.example.onechamp.R
import com.example.onechamp.domain.model.DataDTO
import com.example.onechamp.domain.repository.DataRepository

class DataRepositoryImpl:DataRepository {
    override fun getData(): List<DataDTO> {
        return listOf(
            DataDTO(1, R.drawable.fc_apple,R.raw.fc_apple),
            DataDTO(2,R.drawable.fc_banana,R.raw.fc_banana),
            DataDTO(3,R.drawable.fc_bread,R.raw.fc_bread),
            DataDTO(4,R.drawable.fc_cake,R.raw.fc_cake),
            DataDTO(5,R.drawable.fc_egg,R.raw.fc_egg),
            DataDTO(6,R.drawable.fc_carrot,R.raw.fc_carrot),
            DataDTO(7,R.drawable.fc_orange,R.raw.fc_orange),
            DataDTO(8,R.drawable.fc_potato,R.raw.fc_potato),
            DataDTO(9,R.drawable.fc_tomato,R.raw.fc_tomato)
        )
    }


}