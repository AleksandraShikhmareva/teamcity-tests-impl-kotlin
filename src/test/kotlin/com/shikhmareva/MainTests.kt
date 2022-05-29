package com.shikhmareva

import com.shikhmareva.dataProvider.ConfigFileReader
import com.shikhmareva.pages.*
import io.qameta.allure.Description
import org.testng.Assert
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

class MainTests : BaseTest() {

    companion object {
        private const val PROJECT_NAME_1 = "Calculator"
        private const val PROJECT_NAME_2 = "Calculator 2"
        private const val BRANCH_NAME = "refs/heads/main"
        private const val BUILD_CONF_ID = "Calculator_Build"
        private const val BUILD_CONF_ID_2 = "Calculator_RunTests"
        private const val BUILD_CONF_ID_3 = "Calculator2_Build2"
        private const val BUILD_NAME = "Build"
        private const val BUILD_NAME_2 = "Run Tests"
        private const val BUILD_NAME_3 = "Build 2"
        private const val SUCCESS_CONNECTION_MESSAGE = "The connection to the VCS repository has been verified"
        private const val PROJECT_URL_1 = "https://github.com/AleksandraShikhmareva/calculator.git"
    }

    private lateinit var mainPage: MainPage
    private lateinit var loginPage: LoginPage
    private lateinit var administrationPage: AdministrationPage
    private lateinit var createProjectPage: CreateItemPage
    private lateinit var createProjectFromURLPage: CreateFromURLPage
    private lateinit var buildConfigurationPage: BuildConfigurationPage
    private lateinit var buildsPage: BuildsPage
    private lateinit var projectSettingsPage: ProjectSettingsPage
    private lateinit var buildInfoPage: BuildInfoPage
    private lateinit var projectInfoPage: ProjectInfoPage

    @BeforeClass
    fun initiatePages() {
        mainPage = MainPage(driver)
        loginPage = LoginPage(driver)
        administrationPage = AdministrationPage(driver)
        createProjectPage = CreateItemPage(driver)
        createProjectFromURLPage = CreateFromURLPage(driver)
        buildConfigurationPage = BuildConfigurationPage(driver)
        buildsPage = BuildsPage(driver)
        projectSettingsPage = ProjectSettingsPage(driver)
        buildInfoPage = BuildInfoPage(driver)
        projectInfoPage = ProjectInfoPage(driver)
    }

    @BeforeClass
    fun login() {
        loginPage.login(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
    }

    @AfterClass
    fun deleteProject() {
        apiHelper.deleteProject(PROJECT_NAME_1, PROJECT_NAME_2)
    }

    @Description("Creating new project and build")
    @Test(testName = "Build project test", enabled = true, priority = 0)
    fun creatingBuildTest() {
        mainPage.openAdministrationLink()
        administrationPage.createProject()
        createProjectPage.addProjectFromRemoteRepository(PROJECT_URL_1)
        Assert.assertTrue(createProjectFromURLPage.checkSuccessConnectMessage().contains(SUCCESS_CONNECTION_MESSAGE))
        createProjectFromURLPage.addGeneralProjectProperties(BRANCH_NAME, PROJECT_NAME_1)
        buildConfigurationPage.openParametersTab()
            .addNewParameter("MAVEN_EXTRA_PARAMETERS", "-DskipTests=true")
            .addNewParameter("PROJECT_VERSION", "1.0")
            .addNewParameter("BUILD_VERSION", "%PROJECT_VERSION%.%build.number%")
            .openBuildStepsTab()
            .addNewBuildStep(
                "Maven",
                "Set project version",
                "org.codehaus.mojo:versions-maven-plugin:2.7:set -DnewVersion=%BUILD_VERSION%",
                ""
            )
            .addNewBuildStep("Maven", "Build", "clean package", "%MAVEN_EXTRA_PARAMETERS%")
            .goToConfigurationPage()
        val buildId = apiHelper.runBuild(BUILD_CONF_ID)
        Assert.assertEquals(apiHelper.getStatusAfterBuildComplete(buildId), "SUCCESS")
        buildsPage.openBuildInfo(buildId)
        Assert.assertTrue(buildInfoPage.hasExpectedResult("Success"))
        buildInfoPage.openBuildLogTab()
        Assert.assertTrue(buildInfoPage.hasStringInLog("Build finished"))
        buildInfoPage.openParametersTab()
        Assert.assertTrue(buildInfoPage.hasParameterWithValue("BUILD_VERSION", "1.0.1"))
    }

    @Description("Creating a Build Chain")
    @Test(testName = "Create build chain", enabled = true, priority = 1, dependsOnMethods = ["creatingBuildTest"])
    fun createBuildChainTest() {
        mainPage.openProjectsPage()
            .openProject(PROJECT_NAME_1)
            .editProjectSettings()
        projectSettingsPage.createBuildConfiguration()
        createProjectPage.addProjectFromRemoteRepository(PROJECT_URL_1)
        Assert.assertTrue(createProjectFromURLPage.checkSuccessConnectMessage().contains(SUCCESS_CONNECTION_MESSAGE))
        createProjectFromURLPage.addGeneralBuildProperties(BRANCH_NAME, BUILD_NAME_2)
            .selectUseThisInDialog()
        buildConfigurationPage.openBuildStepsTab()
            .addNewBuildStep("Maven", "Run Tests", "surefire:test", "")
            .openDependencyTab()
            .addNewSnapshotDependency(true, BUILD_NAME)
            .goToConfigurationPage()
        Assert.assertEquals(apiHelper.getStatusAfterBuildComplete(apiHelper.runBuild(BUILD_CONF_ID_2)), "SUCCESS")
        mainPage.openProjectsPage()
            .openProject(PROJECT_NAME_1)
        projectInfoPage.openBuildChainTab()
        Assert.assertTrue(projectInfoPage.hasAllBuildsInBuildChain())
    }

    @Description("Copy build configuration from project \"Calculator 1\" to project \"Calculator 2\"")
    @Test(testName = "Copy configuration", enabled = true, priority = 2, dependsOnMethods = ["creatingBuildTest"])
    fun copyConfigurationTest() {
        mainPage.openAdministrationLink()
        administrationPage.createProject()
        createProjectPage.createProjectManually(PROJECT_NAME_2)
        mainPage.openProjectsPage()
            .openProject(PROJECT_NAME_1)
            .editProjectSettings()
        projectSettingsPage.openBuildConfiguration(BUILD_NAME)
        buildConfigurationPage.copyConfiguration(PROJECT_NAME_2, BUILD_NAME_3)
        mainPage.openProjectsPage()
            .openProject(PROJECT_NAME_2)
        projectInfoPage.editProjectSettings()
        projectSettingsPage.openBuildConfiguration(BUILD_NAME_3)
        buildConfigurationPage.openParametersTab()
            .editParameter("MAVEN_EXTRA_PARAMETERS", "")
            .goToConfigurationPage()
        val buildId = apiHelper.runBuild(BUILD_CONF_ID_3)
        Assert.assertEquals(apiHelper.getStatusAfterBuildComplete(buildId), "SUCCESS")
        buildsPage.openBuildInfo(buildId)
        Assert.assertTrue(buildInfoPage.hasExpectedResult("Tests passed: 1"))
        buildInfoPage.openParametersTab()
        Assert.assertTrue(buildInfoPage.hasParameterWithValue("BUILD_VERSION", "1.0.3"))
    }

}