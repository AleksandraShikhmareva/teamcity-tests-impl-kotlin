package com.shikhmareva.pages

import io.qameta.allure.Step
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.PageFactory

class LoginPage(driver: WebDriver?) {

    init {
        PageFactory.initElements(driver, this)
    }

    @FindBy(id = "username")
    private lateinit var userNameInput: WebElement

    @FindBy(id = "password")
    private lateinit var passwordInput: WebElement

    @FindBy(xpath = "//input[@class='btn loginButton']")
    private lateinit var logInButton: WebElement

    @Step("Login as {username}")
    fun login(username: String, password: String) {
        userNameInput.sendKeys(username)
        passwordInput.sendKeys(password)
        logInButton.click()
    }
}