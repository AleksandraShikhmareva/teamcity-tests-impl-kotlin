package com.shikhmareva.utils

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory
import java.time.Duration

object SeleniumUtils {

    private val logger = LoggerFactory.getLogger(SeleniumUtils::class.java)

    fun waitVisibilityOfElement(driver: WebDriver, element: WebElement, timeOutInSeconds: Long) {
        WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds))
            .until(ExpectedConditions.visibilityOf(element))
    }

    fun waitElementToBeClickable(driver: WebDriver, element: WebElement, timeOutInSeconds: Long) {
        try {
            Thread.sleep(500)
            WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(element))
        } catch (e: Exception) {
            logger.error("Wait element exception", e)
        }
    }

    fun waitForElementPresent(driver: WebDriver, by: By, timeOutInSeconds: Long) {
        try {
            Thread.sleep(500)
            WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(by))
        } catch (e: Exception) {
            logger.error("Wait element exception", e)
        }
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