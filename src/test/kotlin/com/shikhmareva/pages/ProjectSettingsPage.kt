package com.shikhmareva.pages

import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.WebElement
import org.openqa.selenium.By
import org.openqa.selenium.support.PageFactory

class ProjectSettingsPage(var driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//span[text()='Create build configuration']/parent::a")
    private lateinit var createBuildConfigurationBtn: WebElement

    @FindBy(xpath = "//span[text()='Create build configuration']/parent::a")
    private lateinit var buildConfigurationLink: WebElement

    @Step("Create Build Configuration")
    fun createBuildConfiguration() {
        createBuildConfigurationBtn.click()
    }

    @Step("Open configuration of build with name \"{buildConfigurationName}\"")
    fun openBuildConfiguration(buildConfigurationName: String) {
        driver.findElement(By.xpath("//table[@id='configurations']//strong[text()='$buildConfigurationName']/parent::td"))
            .click()
    }
}