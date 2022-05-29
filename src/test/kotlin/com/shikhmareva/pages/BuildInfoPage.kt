package com.shikhmareva.pages

import com.shikhmareva.utils.SeleniumUtils
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import java.util.*

class BuildInfoPage(val driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//td[text()='Result:']/following-sibling::td[@class='st']")
    private lateinit var statusIconElement: WebElement

    @FindBy(id = "buildLog_Tab")
    private lateinit var buildLogTab: WebElement

    @FindBy(id = "buildParameters_Tab")
    private lateinit var  buildParametersTab: WebElement

    fun openBuildLogTab() {
        buildLogTab.click()
    }

    fun openParametersTab() {
        buildParametersTab.click()
    }

    fun hasExpectedResult(expectedResult: String): Boolean {
        SeleniumUtils.waitElementToBeClickable(driver, statusIconElement, 20)
        return statusIconElement.text.lowercase(Locale.getDefault()).contains(expectedResult.lowercase(Locale.getDefault()))
    }

    fun hasParameterWithValue(parameter: String, value: String): Boolean {
        return driver.findElement(By.xpath("//a[@name='ActualParametersOnAgent']/parent::h2/following-sibling::div//td[text()='$parameter']/following-sibling::td/span"))
            .text.contains(value)
    }

    fun hasStringInLog(str: String): Boolean {
        try {
            SeleniumUtils.waitForElementPresent(driver, By.xpath("//*[contains(text(),'$str')]"), 5)
        } catch (e: NoSuchElementException) {
            return false
        }
        return true
    }

}