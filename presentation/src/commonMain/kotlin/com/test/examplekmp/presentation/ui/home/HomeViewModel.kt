package com.test.examplekmp.presentation.ui.home

import com.test.examplekmp.domain.entity.HomeItem
import com.test.examplekmp.domain.entity.SendEvent
import com.test.examplekmp.domain.entity.SendHotel
import com.test.examplekmp.domain.entity.SendKeyword
import com.test.examplekmp.domain.entity.base.ArrangeType
import com.test.examplekmp.domain.entity.base.ContentType
import com.test.examplekmp.domain.usecase.HomeUseCase
import com.test.examplekmp.presentation.base.BaseViewModel
import com.test.examplekmp.presentation.util.endMonthDate
import com.test.examplekmp.presentation.util.todayDate
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest

class HomeViewModel(
    private val homeUseCase: HomeUseCase
): BaseViewModel() {

    companion object {
        private const val DEFAULT_ROWS = "6"
    }

    private val _keywordList = MutableStateFlow<List<HomeItem>>(emptyList())
    val keywordList: StateFlow<List<HomeItem>>
        get() = _keywordList.asStateFlow()

    init {
        callList()
    }

    private fun callList() = ioJob {
            setLoading(true)
            async {  getKeywordList() }.await()
            async { getEventList() }.await()
            async { getHotelList() }.await()
            setLoading(false)
        }

    private suspend fun getKeywordList() {
        val param = SendKeyword(
            arrange = ArrangeType.MODIFIED.type,
            keyword = "서울",
            contentTypeId = ContentType.TOURIST_DESTINATION.contentId,
            areaCode = "1",
            numOfRows = DEFAULT_ROWS
        )
        homeUseCase.getKeywordList(param).collectLatest {
            _keywordList.value += it
        }
    }

    private suspend fun getEventList() {
        val param = SendEvent(
            arrange = ArrangeType.CREATED.type,
            numOfRows = "4",
            eventStartDate = todayDate(),
            eventEndDate = endMonthDate()
        )
        homeUseCase.getEventList(param).collectLatest {
            _keywordList.value += it
        }
    }

    private suspend fun getHotelList() {
        val param = SendHotel(
            arrange = ArrangeType.IMAGE_CREATED.type,
            numOfRows = DEFAULT_ROWS
        )
        homeUseCase.getHotelList(param).collectLatest {
            _keywordList.value += it
        }
    }
}