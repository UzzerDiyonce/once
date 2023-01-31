package com.example.once

data class FeedDTO (
    var userId: String? = null, //이메일
    var uid: String? = null, //uid
    var alarmSet: Boolean? = null,
    var nickname: String? = null, //닉네임
    var profileImageUrl: String? = null, //프로필이미지
    var profInfo: String? = null,
    var timestamp: Long? = null, //timestamp
    var title: String? = null, //작성제목
    var contents: String? = null, //작성내용
    var imageUrl: String? = null, //업로드이미지
    var likeCount: Int = 0, //좋아요(도장)
    var likers: MutableMap<String, Boolean> = HashMap(), //좋아요 누른사람
    var friends: ArrayList<String> = arrayListOf(), //함께한 친구
    //세부내용 날씨 종류
    var weather_kind: Int? = null, //날씨종류 0: 맑음 | 1: 흐림 | 2: 비 | 3: 눈
    //피드 종류
    var feed_kind: Int? = null //피드종류 0: 기본일기 | 1: 타임캡슐
) {
    data class Comment(
        var userId: String? = null, //이메일
        var uid: String? = null, //uid
        var userProfile: String? = null, //프로필
        var comment: String? = null, //댓글
        var timestamp: Long? = null //timestamp
    )
}
