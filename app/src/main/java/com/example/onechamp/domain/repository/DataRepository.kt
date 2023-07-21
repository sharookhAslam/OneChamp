package com.example.onechamp.domain.repository

import com.example.onechamp.domain.model.DataDTO

interface DataRepository {
    fun getData():List<DataDTO>
}