package com.example.onechamp.domain.use_case

import com.example.onechamp.domain.model.DataDTO
import com.example.onechamp.domain.repository.DataRepository

class GetDataUseCase(private val repository: DataRepository) {
    operator fun invoke():List<DataDTO>{
       return repository.getData()
    }
}