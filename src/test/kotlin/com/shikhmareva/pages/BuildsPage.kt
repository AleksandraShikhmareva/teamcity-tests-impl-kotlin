package com.shikhmareva.pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import com.shikhmareva.pages.BuildsPage
import io.qameta.allure.Step
import org.openqa.selenium.support.PageFactory
import org.slf4j.LoggerFactory
import java.util.*

class BuildsPage(private val driver: WebDriver) {

    companion object {
        private val logger = LoggerFactory.getLogger(BuildsPage::class.java)
    }

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//div//button[text()='Run']")
    private lateinit var runBtn: WebElement

    @FindBy(xpath = "//td[@class=' status']/span/span")
    private lateinit var statusIconElement: WebElement

    @Step("Run build")
    fun runBuild() {
        runBtn.click()
    }

    @Step("Check last build for {expectedStatus} status in UI")
    fun hasExpectedStatus(expectedStatus: String): Boolean {
        return statusIconElement.getAttribute("class").contains(expectedStatus.lowercase(Locale.getDefault()))
    }

    @Step("Open information about build with id {buildId}")
    fun openBuildInfo(buildId: Int) {
        driver.findElement(By.xpath("//div[@data-build-id = '$buildId']//*[contains(@class, 'MiddleEllipsis__searchable')]"))
            .click()
    }

}