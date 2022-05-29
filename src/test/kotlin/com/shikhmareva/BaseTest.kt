package com.shikhmareva

import com.shikhmareva.dataProvider.ConfigFileReader
import com.shikhmareva.listeners.BaseTestListener
import com.shikhmareva.utils.ApiHelper
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.LoggerFactory
import org.testng.ITestContext
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite
import org.testng.annotations.Listeners
import java.time.Duration

@Listeners(BaseTestListener::class)
abstract class BaseTest {

    protected lateinit var driver: WebDriver
    var configFileReader: ConfigFileReader = ConfigFileReader()
    protected var apiHelper: ApiHelper = ApiHelper()

    companion object {
        private val logger = LoggerFactory.getLogger(BaseTest::class.java)
    }

    @BeforeSuite
    fun setUp(iTestContext: ITestContext) {
        logger.info("Starting chrome driver")
        System.setProperty("webdriver.chrome.driver", configFileReader.getProperty("pathToDriver"))
        if (configFileReader.getProperty("mode").equals("remoteRun")) {
            val options = ChromeOptions()
            options.addArguments(
                "--headless",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "start-maximized",
                "disable-infobars",
                "--disable-extensions"
            )
            driver = ChromeDriver(options)
        } else if (configFileReader.getProperty("mode").equals("localRun")) {
            driver = ChromeDriver()
            driver.manage().window().fullscreen()
            driver.manage().window().maximize()
        }
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5))
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configFileReader.getProperty("defaultImplicitlyWait").toLong()))
        driver.get(configFileReader.getProperty("url"))
        logger.info("Opening " + configFileReader.getProperty("url"))
        iTestContext.setAttribute("driver", driver)
    }

    @AfterSuite
    fun close() {
        driver.close()
        logger.info("Closing driver")
        driver.quit()
        logger.info("Closing browser")
    }
}