package com.my.targetDemoTests.tests

import com.my.targetDemoTests.helpers.DeviceHelper
import org.junit.Before

open class TestBase {
    val device = DeviceHelper()

    @Before
    open fun setUp() {
        device.clearLog()
    }
}