package com.shikhmareva.pages

import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory

class AdministrationPage(driver: WebDriver?) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(xpath = "//p[@class='createProject']/a")
    private lateinit var createProjectBtn: WebElement

    @Step("Create Project")
    fun createProject() {
        createProjectBtn.click()
    }

}