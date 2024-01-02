package com.test.examplekmp.presentation.ui.detail

import com.test.examplekmp.domain.entity.DetailItem
import com.test.examplekmp.domain.entity.SendDetail
import com.test.examplekmp.domain.usecase.DetailUseCase
import com.test.examplekmp.presentation.base.BaseViewModel
import com.test.examplekmp.presentation.extension.getThumbnailUrl
import com.test.examplekmp.presentation.model.Marker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class DetailViewModel(
    private val detailUseCase: DetailUseCase,
    val contentId: String,
    val contentTypeId: String
): BaseViewModel() {

    override fun onCleared() {
        defaultJob {
            _detailItem.emit(null)
            _selectMarker.emit(null)
        }
        super.onCleared()
    }

    private val _selectMarker = MutableStateFlow<Marker?>(null)

    val selectMarker: StateFlow<Marker?>
        get() = _selectMarker.asStateFlow()

    private val _detailItem = MutableStateFlow<DetailItem?>(null)

    val detailItem: StateFlow<DetailItem?>
        get() = _detailItem.asStateFlow()

    fun getDetail(contentId: String, contentTypeId: String) {
        ioJob {
            val param = SendDetail(
                contentId = contentId,
                contentTypeId = contentTypeId,
                defaultYN = "Y",
                firstImageYN = "Y",
                addrinfoYN = "Y",
                mapinfoYN = "Y",
                overviewYN = "Y"
            )
            detailUseCase.getDetail(param)
                .onStart {
                    setLoading(true)
                }.onCompletion {
                    setLoading(false)
                }.collectLatest {
                    if(it.isEmpty()) return@collectLatest
                    with(it[0]) {
                        _detailItem.emit(this)
                        _selectMarker.emit(Marker.PlaceMarker(
                            name = title,
                            address = addr1,
                            lat = mapy.toDouble(),
                            lng = mapx.toDouble(),
                            contentId = contentid,
                            contentTypeId = contenttypeid,
                            select = true,
                            thumbnailUrl = firstimage.getThumbnailUrl()
                        ))
                    }
                }
        }
    }
}