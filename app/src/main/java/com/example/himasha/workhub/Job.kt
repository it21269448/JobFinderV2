package com.example.himasha.workhub

/**
 * Created by Himasha on 9/5/2017.
 */
class Job {
    var jobName: String? = null
    var jobDesc: String? = null
    var jobBudget: String? = null
    var jobLocationName: String? = null
    var jobPostedDate: String? = null
    var jobPostedUserId: String? = null
    var jobPostedUserName: String? = null
    var jobStatus: String? = null
    var jobKeyWord: String? = null
    var jobLocationLong: Double? = null
    var jobLocationLat: Double? = null
    var isSaved: String? = null

    constructor() {}
    constructor(
        jobName: String?,
        jobDesc: String?,
        jobBudget: String?,
        jobLocationName: String?,
        jobPostedDate: String?,
        jobPostedUserId: String?,
        jobPostedUserName: String?,
        jobStatus: String?,
        jobKeyWord: String?,
        jobLocationLong: Double?,
        jobLocationLat: Double?,
        isSaved: String?
    ) {
        this.jobName = jobName
        this.jobDesc = jobDesc
        this.jobBudget = jobBudget
        this.jobLocationName = jobLocationName
        this.jobPostedDate = jobPostedDate
        this.jobPostedUserId = jobPostedUserId
        this.jobPostedUserName = jobPostedUserName
        this.jobStatus = jobStatus
        this.jobKeyWord = jobKeyWord
        this.jobLocationLong = jobLocationLong
        this.jobLocationLat = jobLocationLat
        this.isSaved = isSaved
    }

    constructor(
        jobName: String?,
        jobDesc: String?,
        jobBudget: String?,
        jobLocationName: String?,
        jobPostedDate: String?,
        jobPostedUserId: String?,
        jobPostedUserName: String?,
        jobStatus: String?,
        jobKeyWord: String?,
        jobLocationLong: Double?,
        jobLocationLat: Double?
    ) {
        this.jobName = jobName
        this.jobDesc = jobDesc
        this.jobBudget = jobBudget
        this.jobLocationName = jobLocationName
        this.jobPostedDate = jobPostedDate
        this.jobPostedUserId = jobPostedUserId
        this.jobPostedUserName = jobPostedUserName
        this.jobStatus = jobStatus
        this.jobKeyWord = jobKeyWord
        this.jobLocationLong = jobLocationLong
        this.jobLocationLat = jobLocationLat
    }
}