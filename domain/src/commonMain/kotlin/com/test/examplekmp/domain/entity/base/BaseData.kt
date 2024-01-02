package com.test.examplekmp.domain.entity.base

enum class ContentType(val contentId: Int) {
    TOURIST_DESTINATION(12), //관광지
    CULTURE(14), //문화시설
    FESTIVAL(15), //축제공연행사
    TRAVEL(25), //여행코스
    LEISURE_SPORTS(28), //레포츠
    HOTEL(32), //숙박
    SHOPPING(38), //쇼핑
    FOOD(39); //음식점

    companion object {
        fun fromIdToText(contentId: Int): String {
           return when(contentId) {
               ContentType.TOURIST_DESTINATION.contentId -> "관광지"
               ContentType.CULTURE.contentId -> "문화시설"
               ContentType.FESTIVAL.contentId -> "축제/공연/행사"
               ContentType.TRAVEL.contentId -> "여행코스"
               ContentType.LEISURE_SPORTS.contentId -> "레포츠"
               ContentType.HOTEL.contentId -> "호텔"
               ContentType.SHOPPING.contentId -> "쇼핑"
               ContentType.FOOD.contentId -> "음식점"
               else -> "정보없음"
           }
        }
    }
}


enum class ListType(val type: String) {
    GRID("Y"), //목록
    COUNT("N") //개수
}

enum class ArrangeType(val type: String) {
    TITLE("A"), //제목순
    MODIFIED("C"), //수정일순
    CREATED("D"), //생성일순
    IMAGE_TITLE("O"), //이미지 제목순
    IMAGE_MODIFIED("Q"), //이미지 수정일순
    IMAGE_CREATED("R"), //이미지 생성일순
}