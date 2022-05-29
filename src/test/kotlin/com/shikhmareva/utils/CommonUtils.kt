package com.shikhmareva.utils

import io.qameta.allure.Attachment
import org.openqa.selenium.WebDriver
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.OutputType

class CommonUtils {

    companion object {
        @Attachment
        fun saveScreenShot(driver: WebDriver): ByteArray {
            return (driver as TakesScreenshot).getScreenshotAs(OutputType.BYTES)
        }
    }

}