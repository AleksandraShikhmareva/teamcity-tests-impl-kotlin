package com.shikhmareva.pages

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.WebElement
import com.shikhmareva.utils.SeleniumUtils
import io.qameta.allure.Step
import org.openqa.selenium.By
import org.openqa.selenium.support.PageFactory

class MainPage(private val driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//a[@title='Administration']/span")
    private lateinit var administrationLink: WebElement

    @FindBy(xpath = "//a[@title='Projects']/span")
    private lateinit var projectPageLink: WebElement

    @FindBy(xpath = "//a[@title='Edit project settings']")
    private lateinit var editProjectSettingsLink: WebElement

    @Step("Open \"Projects\" page")
    fun openProjectsPage(): MainPage {
        SeleniumUtils.waitElementToBeClickable(driver, projectPageLink, 5)
        projectPageLink.click()
        return this
    }

    @Step("Edit Project Settings")
    fun editProjectSettings() {
        editProjectSettingsLink.click()
    }

    @Step("Open \"{projectName}\" Project")
    fun openProject(projectName: String): MainPage {
        driver.findElement(By.xpath("//a[text() = '$projectName']")).click()
        return this
    }

    @Step("Open Administration Link")
    fun openAdministrationLink() {
        SeleniumUtils.waitVisibilityOfElement(driver, administrationLink, 5)
        SeleniumUtils.waitElementToBeClickable(driver, administrationLink, 5)
        administrationLink.click()
    }

}