package com.shikhmareva.pages

import com.shikhmareva.utils.SeleniumUtils
import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

class CreateFromURLPage(val driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//div[@class='connectionSuccessful']")
    private lateinit var successVcsConnectMessage: WebElement

    @FindBy(id = "projectName")
    private lateinit var projectNameInput: WebElement

    @FindBy(id = "branch")
    private lateinit var branchInput: WebElement

    @FindBy(id = "buildTypeName")
    private lateinit var buildTypeNameInput: WebElement

    @FindBy(xpath = "//input[@value = 'Proceed']")
    private lateinit var proceedBtn: WebElement

    @FindBy(xpath = "//input[@value='Use this']")
    private lateinit var useThisBtn: WebElement

    fun checkSuccessConnectMessage(): String {
        return successVcsConnectMessage.text
    }

    @Step("Add project \"{projectName}\". Set Default branch = {branch}")
    fun addGeneralProjectProperties(branch: String, projectName: String) {
        projectNameInput.clear()
        projectNameInput.sendKeys(projectName)
        branchInput.clear()
        branchInput.sendKeys(branch)
        proceedBtn.click()
    }

    @Step("Add build {buildName}. Set Default branch = {branch}")
    fun addGeneralBuildProperties(branch: String, buildName: String): CreateFromURLPage {
        buildTypeNameInput.clear()
        buildTypeNameInput.sendKeys(buildName)
        branchInput.clear()
        branchInput.sendKeys(branch)
        proceedBtn.click()
        return this
    }

    @Step("Select \"Use this\" in \"Duplicate VCS Roots\" dialog")
    fun selectUseThisInDialog() {
        SeleniumUtils.waitElementToBeClickable(driver, useThisBtn, 5)
        useThisBtn.click()
    }

}