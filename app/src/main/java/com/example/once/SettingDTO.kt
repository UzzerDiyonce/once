package com.example.once

data class SettingDTO(
    var userId: String? = null, //이메일
    var uid: String? = null, //uid
    var profileImageUrl: String? = null, //프로필이미지
    var profInfo: String? = null,     // 한줄소개
    var alarmSet: Boolean? = true   // 푸시알림 설정 여부
)
