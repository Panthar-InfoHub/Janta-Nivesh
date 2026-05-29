package org.velvetinvesting.jantanivesh.app.core.utils.deviceinfo

import platform.UIKit.UIDevice

class DeviceInfoRetrieverIos : DeviceInfoRetriever {
    override fun getDeviceInfo(): DeviceInfo {
        val device = UIDevice.currentDevice

        return DeviceInfo(
            deviceType = "I",
            deviceVersion = device.systemVersion,
            deviceBuildNumber = device.systemVersion,
            deviceId = device.identifierForVendor?.UUIDString ?: "unknown"
        )
    }
}
