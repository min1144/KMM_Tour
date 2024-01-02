package com.test.examplekmp.domain.usecase

import com.test.examplekmp.domain.entity.HomeHeader
import com.test.examplekmp.domain.entity.HomeItem
import com.test.examplekmp.domain.entity.HomeItemViewType
import com.test.examplekmp.domain.entity.SendEvent
import com.test.examplekmp.domain.entity.SendHotel
import com.test.examplekmp.domain.entity.SendKeyword
import com.test.examplekmp.domain.repository.HomeListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

class HomeUseCase constructor(
    private val homeListRepository: HomeListRepository
) {

    fun getKeywordList(param: SendKeyword): Flow<List<HomeItem>> {
        val homeItemList = ArrayList<HomeItem>().apply {
            add(HomeItem(viewType = HomeItemViewType.HEADER,
                homeHeader = HomeHeader(title = "이달의 추천 장소")
            ))
        }

        return homeListRepository.getKeywordList(param).map {
            it.response.body.items?.item ?: emptyList()
        }.transform { keyword ->
            homeItemList.add(HomeItem(viewType = HomeItemViewType.PAGER, keyword = keyword))
            emit(homeItemList)
        }
    }

    fun getEventList(param: SendEvent): Flow<List<HomeItem>> {
        val homeItemList = ArrayList<HomeItem>().apply {
            add(HomeItem(viewType = HomeItemViewType.HEADER,
                homeHeader = HomeHeader(title = "행사 정보")
            ))
        }

        return homeListRepository.getEventList(param).map {
            it.response.body.items?.item ?: emptyList()
        }.transform { event ->
            homeItemList.add(HomeItem(viewType = HomeItemViewType.LIST, event = event))
            emit(homeItemList)
        }
    }

    fun getHotelList(param: SendHotel): Flow<List<HomeItem>> {
        val homeItemList = ArrayList<HomeItem>().apply {
            add(HomeItem(viewType = HomeItemViewType.HEADER,
                homeHeader = HomeHeader(title = "숙박 정보")
            ))
        }

        return homeListRepository.getHotelList(param).map {
            it.response.body.items?.item ?: emptyList()
        }.transform {hotel ->
            homeItemList.add(HomeItem(viewType = HomeItemViewType.GRID, hotel = hotel))
            emit(homeItemList)
        }
    }
}