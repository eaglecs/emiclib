package basecode.com.ui.base.executor

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class AndroidTaskLifoSchedulerProvider : SchedulerProvider {
    override fun createScheduler(): Scheduler = Schedulers.from(TaskExecutor.getLifo())
}