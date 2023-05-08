package com.example.himasha.workhub

/**
 * Created by Himasha on 9/6/2017.
 */
class Review {
    var reviewingUser: String? = null
    var reviewedUser: String? = null
    var reviewedUserName: String? = null
    var review: String? = null

    constructor() {}
    constructor(
        reviewingUser: String?,
        reviewedUser: String?,
        reviewedUserName: String?,
        review: String?
    ) {
        this.reviewingUser = reviewingUser
        this.reviewedUser = reviewedUser
        this.reviewedUserName = reviewedUserName
        this.review = review
    }
}