package com.kevinj1008.testviewpager2sample.handler

import android.os.Handler
import android.os.Message
import android.util.SparseIntArray


class TimerHandler(
    private var listener: TimerHandlerListener?,
    private val interval: Long
) : Handler() {

    var specialInterval: SparseIntArray? = null
    var isStopped = true

    interface TimerHandlerListener {
        val nextItem: Int

        fun callBack()
    }

    override fun handleMessage(msg: Message) {
        if (MSG_TIMER_ID == msg.what) {
            listener?.apply {
                val nextIndex = this.nextItem
                this.callBack()
                tick(nextIndex)
            }
        }
    }

    fun tick(index: Int) {
        sendEmptyMessageDelayed(MSG_TIMER_ID, getNextInterval(index))
    }

    private fun getNextInterval(index: Int): Long {
        var next = interval
        specialInterval?.apply {
            val has = this[index, -1].toLong()
            if (has > 0) {
                next = has
            }
        }
        return next
    }

    companion object {
        const val MSG_TIMER_ID = 87108
    }
}