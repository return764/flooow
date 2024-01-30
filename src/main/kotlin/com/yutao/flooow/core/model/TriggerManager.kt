package com.yutao.flooow.core.model

import com.yutao.flooow.dsl.TriggerDSL
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.support.CronTrigger

class TriggerManager(
    val triggerDSL: TriggerDSL
) {
    fun getTrigger(): Trigger {
        // TODO support more trigger
        return CronTrigger(triggerDSL.cron!!)
    }
}
