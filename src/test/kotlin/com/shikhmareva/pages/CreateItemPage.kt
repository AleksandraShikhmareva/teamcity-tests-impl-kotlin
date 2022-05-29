package com.shikhmareva.pages

import com.shikhmareva.utils.SeleniumUtils
import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

class CreateItemPage(val driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(id = "url")
    private lateinit var urlInput: WebElement

    @FindBy(xpath = "//table[@class='parametersTable']")
    private lateinit var parametersTable: WebElement

    @FindBy(xpath = "//input[@value = 'Proceed']")
    private lateinit var proceedBtn: WebElement

    @FindBy(xpath = "//a[@href='#createFromUrl']")
    private lateinit var createFromUrl: WebElement

    @FindBy(xpath = "//a[@href='#createManually']")
    private lateinit var createManually: WebElement

    @FindBy(id = "name")
    private lateinit var nameInput: WebElement

    @FindBy(id = "externalId")
    private lateinit var projectIdInput: WebElement

    @FindBy(id = "createProject")
    private lateinit var createBtn: WebElement

    @FindBy(xpath = "//a[@name='projectCreated']/following-sibling::div[@class='successMessage']")
    private lateinit var successMessage: WebElement

    @FindBy(xpath = "//div[@class='saveButtonsBlock']/input[@value='Save']")
    private lateinit var saveBtn: WebElement

    @Step("Create project on \"From a repository URL\" tab with GitHub public repository URL = {projectUrl}")
    fun addProjectFromRemoteRepository(projectUrl: String) {
        createFromUrl.click()
        SeleniumUtils.waitVisibilityOfElement(driver, urlInput, 5)
        urlInput.sendKeys(projectUrl)
        proceedBtn.click()
    }

    @Step("Create project \"{projectName}\" manually")
    fun createProjectManually(projectName: String) {
        createManually.click()
        SeleniumUtils.waitVisibilityOfElement(driver, nameInput, 5)
        nameInput.sendKeys(projectName)
        createBtn.click()
        SeleniumUtils.waitVisibilityOfElement(driver, successMessage, 5)
        saveBtn.click()
    }

}