package com.haldny.testintent

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log

class MyJobService: JobService() {

    private var isWorking = false

    override fun onStartJob(jobParameters: JobParameters?): Boolean {
        isWorking = true

        startJobOnOtherThread(jobParameters)

        return isWorking
    }

    override fun onStopJob(jobParameters: JobParameters?): Boolean {
        return isWorking
    }

    private fun startJobOnOtherThread(jobParameters: JobParameters?) {
        Thread {
            doWork(jobParameters)
        }.start()
    }

    private fun doWork(jobParameters: JobParameters?) {
        Thread.sleep(10*1000)

        isWorking = false

        Log.d("MyJobService", "doWork")
        jobFinished(jobParameters, false)
    }

}