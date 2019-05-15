package basecode.com.ui.base.executor

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun createScheduler(): Scheduler
}