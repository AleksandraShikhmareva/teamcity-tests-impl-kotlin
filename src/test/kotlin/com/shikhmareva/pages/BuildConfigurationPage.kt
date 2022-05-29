package com.shikhmareva.pages

import com.shikhmareva.utils.SeleniumUtils
import io.qameta.allure.Step
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.Select

class BuildConfigurationPage(val driver: WebDriver) {

    init {
        PageFactory.initElements(driver, this)
    }

    /*Actions menu*/
    @FindBy(xpath = "//span[@id='sp_span_btActions']/button")
    private lateinit var actionsBtn: WebElement

    @FindBy(xpath = "//a[text()='Copy configuration...']")
    private lateinit var copyConfigurationItem: WebElement

    @FindBy(id = "newBuildTypeName")
    private lateinit var newBuildNameInput: WebElement

    @FindBy(id = "copyBuildTypeFormDialog")
    private lateinit var copyBuildTypeFormDialog: WebElement

    @FindBy(id = "-ufd-teamcity-ui-copyBuildTypeProjectId")
    private lateinit var copyToProjectInput: WebElement

    @FindBy(id = "copyBuildTypeButton")
    private lateinit var copyBtn: WebElement

    /* Dependencies Tab */
    @FindBy(xpath = "//li[@id='dependencies_Tab']/p/a")
    private lateinit var dependenciesTab: WebElement

    @FindBy(xpath = "//span[text()='Add new snapshot dependency']")
    private lateinit var addNewSnapshotDependencyBtn: WebElement

    @FindBy(id = "option:run-build-if-dependency-failed-to-start")
    private lateinit var runBuildIfDependencyFailedToStartSelect: WebElement

    @FindBy(xpath = "//div[@id='sourceDependenciesDialog']//input[@value='Save']")
    private lateinit var sourceDialogSaveBtn: WebElement

    @FindBy(id = "option:take-started-build-with-same-revisions")
    private lateinit var doNotRunNewCheckbox: WebElement

    /*Build Step Tab*/
    @FindBy(xpath = "//li[@id='runType_Tab']/p/a")
    private lateinit var buildStepsTab: WebElement

    @FindBy(xpath = "//div[@id = 'advancedSettingsToggle_editBuildTypeForm']/a")
    private lateinit var advancedOptionsLink: WebElement

    @FindBy(id = "runnerArgs")
    private lateinit var runnerArgsInput: WebElement

    @FindBy(xpath = "//span[contains(@class,'addNew')]")
    private lateinit var addNewBtn: WebElement

    @FindBy(xpath = "//a[text()='Go to build configuration page']")
    private lateinit var goToConfigurationPageBtn: WebElement

    @FindBy(id = "goals")
    private lateinit var goalsInput: WebElement

    @FindBy(id = "buildStepName")
    private lateinit var stepNameInput: WebElement

    @FindBy(id = "buildTypeSettingsContainer")
    private lateinit var buildTypeSettingsContainer: WebElement

    @FindBy(xpath = "//div[@id='mainContent']//input[@type='submit']")
    private lateinit var saveBuildStepBtn: WebElement

    /*Parameters tab*/
    @FindBy(xpath = "//li[@id='buildParams_Tab']/p/a")
    private lateinit var parametersTab: WebElement

    @FindBy(id = "parameterName")
    private lateinit var parameterNameInput: WebElement

    @FindBy(id = "parameterValue")
    private lateinit var parameterValueInput: WebElement

    @FindBy(xpath = "//div[@id='editParamFormDialog']//input[@type='submit']")
    private lateinit var submitBtn: WebElement

    @FindBy(id = "-ufd-teamcity-ui-runTypeInfoKey")
    private lateinit var optionInput: WebElement

    @FindBy(xpath = "//div//button[text()='Run']")
    private lateinit var runBtn: WebElement

    @FindBy(id = "footer")
    private lateinit var footer: WebElement

    fun goToConfigurationPage() {
        SeleniumUtils.waitElementToBeClickable(driver, goToConfigurationPageBtn, 5)
        goToConfigurationPageBtn.click()
        SeleniumUtils.waitElementToBeClickable(driver, runBtn, 5)
    }

    /*Build Actions*/
    @Step("Select copy configuration item in Action menu")
    fun copyConfiguration(projectName: String, buildName: String) {
        actionsBtn.click()
        copyConfigurationItem.click()
        SeleniumUtils.customSelect(driver, projectName, copyToProjectInput, copyBuildTypeFormDialog)
        newBuildNameInput.clear()
        newBuildNameInput.sendKeys(buildName)
        copyBtn.click()
    }

    /*Methods for Dependencies tab*/
    @Step("Open Dependency tab")
    fun openDependencyTab(): BuildConfigurationPage {
        SeleniumUtils.waitElementToBeClickable(driver, dependenciesTab, 5)
        dependenciesTab.click()
        return this
    }

    @Step("Add new snapshot dependency with depends on {buildName}")
    fun addNewSnapshotDependency(hasDoNotRunNewOption: Boolean, buildName: String): BuildConfigurationPage {
        SeleniumUtils.waitElementToBeClickable(driver, addNewSnapshotDependencyBtn, 10)
        addNewSnapshotDependencyBtn.click()
        driver.findElement(By.xpath("//span[text()='$buildName']/../parent::span/preceding-sibling::input[@class='ring-checkbox-input']"))
            .click()
        if (hasDoNotRunNewOption) {
            doNotRunNewCheckbox.click()
        }
        val select = Select(runBuildIfDependencyFailedToStartSelect)
        select.selectByValue("CANCEL")
        sourceDialogSaveBtn.click()
        return this
    }

    /*Methods for Parameter tab*/
    @Step("Open Parameters tab")
    fun openParametersTab(): BuildConfigurationPage {
        SeleniumUtils.waitElementToBeClickable(driver, parametersTab, 10)
        parametersTab.click()
        return this
    }

    @Step("Edit parameter {name}. Set value {value}")
    fun editParameter(name: String, value: String): BuildConfigurationPage {
        SeleniumUtils.waitElementToBeClickable(driver, addNewBtn, 10)
        driver.findElement(By.xpath("//a[@name='$name']/parent::td/parent::tr/child::td[@class='edit highlight']/a"))
            .click()
        parameterValueInput.clear()
        parameterValueInput.sendKeys(value)
        submitBtn.click()
        SeleniumUtils.waitForElementPresent(driver, By.xpath("//table[@id='confTable']//tr[@class='ownParam']"), 2)
        return this
    }

    @Step("Add parameter {name} with value {value}")
    fun addNewParameter(name: String, value: String): BuildConfigurationPage {
        SeleniumUtils.waitElementToBeClickable(driver, addNewBtn, 10)
        addNewBtn.click()
        parameterNameInput.sendKeys(name)
        parameterValueInput.sendKeys(value)
        submitBtn.click()
        SeleniumUtils.waitForElementPresent(driver, By.xpath("//table[@id='confTable']//tr[@class='ownParam']"), 2)
        return this
    }

    /*Methods for Build Steps Tab*/
    @Step("Add new build step \"{stepName}\" with runner type \"{runner}\"")
    fun addNewBuildStep(runner: String, stepName: String, goals: String, runnerArgs: String): BuildConfigurationPage {
        SeleniumUtils.waitElementToBeClickable(driver, addNewBtn, 10)
        addNewBtn.click()
        SeleniumUtils.customSelect(driver, runner, optionInput, buildTypeSettingsContainer)
        SeleniumUtils.waitElementToBeClickable(driver, stepNameInput, 5)
        stepNameInput.sendKeys(stepName)
        goalsInput.sendKeys(goals)
        SeleniumUtils.moveToElement(driver, footer)
        if (advancedOptionsLink.getText() == "Show advanced options") {
            advancedOptionsLink.click()
        }
        runnerArgsInput.sendKeys(runnerArgs)
        saveBuildStepBtn.click()
        return this
    }

    fun openBuildStepsTab(): BuildConfigurationPage {
        buildStepsTab.click()
        return this
    }

}