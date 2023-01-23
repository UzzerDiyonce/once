package com.example.once

data class AlarmDTO (
    var fromUid: String? = null, //알람 보낸 사람의 UID
    var fromId: String? = null, //알람 보낸 사람의 ID
    var profileImage: String? = null, //알람 보낸 사람의 프로필
    var kind: Int? = null,
    // 0: stamp 1: comment, 2: capsule, 3: friend
    var message: String? = null,
    var timestamp: Long? = null

//fromUid
//fromId
//profileImageUrl
//* KIND *
//0; likestamp //도장
//1; comment //댓글
//2; makecapsule //타임캡슐
//3; friend //친구
//timestamp
//message
)