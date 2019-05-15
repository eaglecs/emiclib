package basecode.com.ui.base.executor

import basecode.com.domain.usecase.base.UseCaseExecution

class AndroidUseCaseExecution : UseCaseExecution(AndroidTaskSchedulerProvider().createScheduler(), AndroidPostTaskSchedulerProvider().createScheduler())