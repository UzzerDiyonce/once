package com.example.once

data class MypageDTO(
    var userId: String? = null, //이메일
    var uid: String? = null, //uid
    var nickname: String? = null, //닉네임
    var profileImageUrl: String? = null, //프로필이미지
    var timestamp: Long? = null, //timestamp
    var title: String? = null, //작성제목
    var contents: String? = null, //작성내용
    var imageUrl: String? = null //업로드이미지
)
