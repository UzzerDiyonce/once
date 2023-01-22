package com.example.once

data class FeedDTO (
    var userId: String? = null, //이메일
    var uid: String? = null, //uid
    var profileImageUrl: String? = null, //프로필이미지
    var timestamp: Long? = null, //timestamp
    var title: String? = null, //작성제목
    var contents: String? = null, //작성내용
    var imageUrl: String? = null, //업로드이미지
    //세부내용 피드
    var weather_kind: Int? = null, //날씨종류 0: 맑음 | 1: 흐림 | 2: 비 | 3: 눈
) {
    data class Comment(
        var userId: String? = null, //이메일
        var uid: String? = null, //uid
        var comment: String? = null, //댓글
        var timestamp: Long? = null //timestamp
    )
}
