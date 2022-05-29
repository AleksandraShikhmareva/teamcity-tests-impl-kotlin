package com.shikhmareva.listeners

import com.shikhmareva.utils.CommonUtils
import org.openqa.selenium.WebDriver
import org.slf4j.LoggerFactory
import org.testng.ITestContext
import org.testng.ITestResult
import org.testng.internal.IResultListener

class BaseTestListener : IResultListener {

    companion object {
        private val logger = LoggerFactory.getLogger(BaseTestListener::class.java)
        fun logErrorMessage(throwable: Throwable?) {
            logger.error("Test Failure: ", throwable)
        }
    }

    override fun onConfigurationSuccess(iTestResult: ITestResult) {}
    override fun onConfigurationFailure(iTestResult: ITestResult) {}
    override fun onConfigurationSkip(iTestResult: ITestResult) {}
    override fun onTestSkipped(iTestResult: ITestResult) {}
    override fun onTestFailedButWithinSuccessPercentage(iTestResult: ITestResult) {}
    override fun onStart(iTestContext: ITestContext) {}
    override fun onFinish(iTestContext: ITestContext) {}

    override fun onTestStart(iTestResult: ITestResult) {
        logger.info("============================")
        logger.info(getTestCaseName(iTestResult) + " started" + System.lineSeparator())
    }

    override fun onTestSuccess(iTestResult: ITestResult) {
        logger.info(getTestCaseName(iTestResult) + " passed" + System.lineSeparator())
        logger.info("============================")
    }

    override fun onTestFailure(iTestResult: ITestResult) {
        if (iTestResult.getThrowable() != null) {
            logErrorMessage(iTestResult.getThrowable())
        }
        logger.error("Test case " + getTestCaseName(iTestResult) + " failed " + iTestResult.getMethod().getTestClass())
        logger.info("============================")
        val context: ITestContext = iTestResult.getTestContext()
        val driver: WebDriver = context.getAttribute("driver") as WebDriver
        CommonUtils.saveScreenShot(driver)
    }

    protected fun getTestCaseName(iTestResult: ITestResult): String {
        return if (iTestResult.getName() != null) iTestResult.getName() else iTestResult.getMethod().getMethodName()
    }

}