package com.sm.borutoapp.data.repository

import com.sm.borutoapp.data.local.BorutoDatabase
import com.sm.borutoapp.domain.model.Hero
import com.sm.borutoapp.domain.repository.LocalDataSource

class LocalDataSourceImpl (borutoDatabase: BorutoDatabase) : LocalDataSource {

    private val heroDao = borutoDatabase.heroDao()

    override suspend fun getSelectedHero(heroId: Int): Hero {
        return heroDao.getSelectedHero(heroId = heroId)
    }

}