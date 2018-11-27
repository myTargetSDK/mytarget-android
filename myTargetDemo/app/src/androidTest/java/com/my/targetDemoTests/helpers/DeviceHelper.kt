package com.my.targetDemoTests.helpers

import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.BySelector
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until

class DeviceHelper {
    var device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    fun osVersion(): String {
        return device.executeShellCommand("getprop ro.build.version.release")
    }

    fun portrait() {
        device.unfreezeRotation()
        device.setOrientationNatural()
    }

    fun wait(element: BySelector, timeout: Long = 5000) {
        device.wait(Until.findObject(element), timeout)
    }

    fun wait(callback: String, timeout: Long = 5000): Boolean {
        val wait = System.currentTimeMillis() + timeout
        while (wait > System.currentTimeMillis()) {
            if (getLog().contains(callback)) { return true }
        }
        return false
    }

    fun landscape() {
        device.unfreezeRotation()
        device.setOrientationLeft()
    }

    fun isAppRunning(bundleId: String): Boolean {
        val result = device.executeShellCommand("shell ps | grep $bundleId")
        return !result.isEmpty()
    }

    fun closeApp(bundleId: String) {
        device.executeShellCommand("shell am force-stop$bundleId")
    }

    fun androidId(): String {
        return device.executeShellCommand("settings get secure android_id")
    }

    fun clearLog() {
        device.executeShellCommand("logcat -c")
    }

    fun getLog(): String {
        return device.executeShellCommand("logcat -d")
    }
}