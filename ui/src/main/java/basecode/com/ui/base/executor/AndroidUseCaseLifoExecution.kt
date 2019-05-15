package basecode.com.ui.base.executor

import basecode.com.domain.usecase.base.UseCaseExecution


class AndroidUseCaseLifoExecution : UseCaseExecution(AndroidTaskLifoSchedulerProvider().createScheduler(), AndroidPostTaskSchedulerProvider().createScheduler())