/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.kevinj1008.testviewpager2sample.customview;

import android.os.Handler;
import android.os.Message;

class TimerHandler extends Handler {
    private static final int MSG_TIMER_ID = 1000;
    private long interval;
    private boolean isStopped = true;
    private ITimerCallback listener;

    TimerHandler(ITimerCallback listener, long interval) {
        this.listener = listener;
        this.interval = interval;
    }

    @Override
    public void handleMessage(Message msg) {
        if (MSG_TIMER_ID == msg.what) {
            if (listener != null) {
                listener.callBack();
            }
            sendEmptyMessageDelayed(MSG_TIMER_ID, interval);
        }
    }

    public void setListener(ITimerCallback listener) {
        this.listener = listener;
    }

    public void trigger() {
        if (!isStopped) {
            return;
        }
        removeCallbacksAndMessages(null);
        sendEmptyMessageDelayed(TimerHandler.MSG_TIMER_ID, interval);
        isStopped = false;
    }

    public void stop() {
        if (isStopped) {
            return;
        }
        removeCallbacksAndMessages(null);
        listener = null;
        isStopped = true;
    }

    interface ITimerCallback {
        void callBack();
    }
}
