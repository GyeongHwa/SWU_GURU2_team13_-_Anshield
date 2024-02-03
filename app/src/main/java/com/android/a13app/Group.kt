package com.android.a13app

class Group(groupName: String, members: String, token: String) {
    var groupName: String = groupName //모임 이름
        private set
    var members: String = members //모임의 멤버("멤버1, 멤버2, 멤버3, ..." 형식의 문자열로 저장됨)
        private set
    var token: String = token //모임 토큰
        private set
}