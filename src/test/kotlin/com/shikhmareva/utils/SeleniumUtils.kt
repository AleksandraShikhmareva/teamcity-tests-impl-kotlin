package com.shikhmareva.utils

import com.shikhmareva.dataProvider.ConfigFileReader
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object SeleniumUtils {

    private val logger = LoggerFactory.getLogger(SeleniumUtils::class.java)

    fun waitVisibilityOfElement(driver: WebDriver, element: WebElement, timeOutInSeconds: Long) {
        WebDriverWait(driver, timeOutInSeconds)
            .until(ExpectedConditions.visibilityOf(element))
    }

    fun waitElementToBeClickable(driver: WebDriver, element: WebElement, timeOutInSeconds: Long) {
        val configFileReader = ConfigFileReader()
        try {
            Thread.sleep(1000)
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS) //nullify implicitlyWait()
            WebDriverWait(driver, timeOutInSeconds)
                .until(ExpectedConditions.elementToBeClickable(element))
        } catch (e: Exception) {
            logger.error("Wait element exception", e)
        } finally {
            driver.manage().timeouts().implicitlyWait(
                configFileReader.getProperty("defaultImplicitlyWait").toLong(),
                TimeUnit.SECONDS
            ) //reset implicitlyWait
        }
    }

    fun waitForElementPresent(driver: WebDriver, by: By, timeOutInSeconds: Long): WebElement? {
        val element: WebElement
        val configFileReader = ConfigFileReader()
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS) //nullify implicitlyWait()
            Thread.sleep(1000)
            val wait = WebDriverWait(driver, timeOutInSeconds)
            element = wait.until(ExpectedConditions.presenceOfElementLocated(by))
            driver.manage().timeouts().implicitlyWait(
                configFileReader.getProperty("defaultImplicitlyWait").toLong(),
                TimeUnit.SECONDS
            ) //reset implicitlyWait
            return element
        } catch (e: Exception) {
            logger.error("Wait element exception", e)
        }
        return null
    }

    fun moveToElement(driver: WebDriver, element: WebElement) {
        val actions = Actions(driver)
        actions.moveToElement(element)
        actions.perform()
    }

    fun customSelect(driver: WebDriver, value: String, select: WebElement, form: WebElement) {
        select.clear()
        select.sendKeys(value)
        val selectedValue = driver.findElement(By.xpath("//li[@data-title='$value']"))
        waitVisibilityOfElement(driver, selectedValue, 10)
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            logger.error("Wait element exception", e)
        }
        form.click()
    }
}