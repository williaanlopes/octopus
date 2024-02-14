package com.gurpster.location.schedulers

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler

class TestSchedulerProvider : BaseSchedulerProvider {
    private val testScheduler = TestScheduler()
    override fun io(): Scheduler = testScheduler
    override fun computation(): Scheduler = testScheduler
    override fun ui(): Scheduler = testScheduler
}