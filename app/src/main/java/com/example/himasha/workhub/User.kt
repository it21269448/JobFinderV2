package com.example.himasha.workhub

/**
 * Created by Himasha on 9/5/2017.
 */
class User {
    var userName: String? = null
    var userEmail: String? = null
    var userTelephone: String? = null
    var userWebsite: String? = null
    var userAddress: String? = null
    var userBio: String? = null

    constructor() {}
    constructor(
        userName: String?,
        userEmail: String?,
        userTelephone: String?,
        userWebsite: String?,
        userAddress: String?,
        userBio: String?
    ) {
        this.userName = userName
        this.userEmail = userEmail
        this.userTelephone = userTelephone
        this.userWebsite = userWebsite
        this.userAddress = userAddress
        this.userBio = userBio
    }
}