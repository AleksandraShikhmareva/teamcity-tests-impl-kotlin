package com.shikhmareva.pages

import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.support.PageFactory
import java.util.NoSuchElementException

class ProjectInfoPage(var driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//a[text()='Build Chains']")
    private lateinit var buildChainTab: WebElement

    @FindBy(xpath = "//a[@title='Edit project settings']")
    private lateinit var editProjectSettings: WebElement

    @Step("Open Build Chain Tab")
    fun openBuildChainTab() {
        buildChainTab.click()
    }

    @Step("Open Project Settings")
    fun editProjectSettings() {
        editProjectSettings.click()
    }

    fun hasAllBuildsInBuildChain(vararg builds: String): Boolean {
        for (build in builds) {
            try {
                driver.findElement(By.xpath("//div[@class='buildChainBuild']//a[text()=' $build']"))
            } catch (e: NoSuchElementException) {
                return false
            }
        }
        return true
    }
}