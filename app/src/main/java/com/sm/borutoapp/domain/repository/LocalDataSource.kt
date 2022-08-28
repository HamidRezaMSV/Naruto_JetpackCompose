package com.sm.borutoapp.domain.repository

import com.sm.borutoapp.domain.model.Hero

interface LocalDataSource {

    suspend fun getSelectedHero(heroId : Int) : Hero

}