package com.test.examplekmp.domain.usecase

import com.test.examplekmp.domain.entity.DetailItem
import com.test.examplekmp.domain.entity.SendDetail
import com.test.examplekmp.domain.repository.DetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DetailUseCase constructor(
    private val detailRepository: DetailRepository
) {

    fun getDetail(param: SendDetail): Flow<List<DetailItem>> {
        return detailRepository.getDetail(param).map {
            it.response.body.items?.item ?: emptyList()
        }
    }
}