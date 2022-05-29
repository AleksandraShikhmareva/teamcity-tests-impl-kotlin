package com.shikhmareva.utils

import com.shikhmareva.dataProvider.ConfigFileReader
import io.qameta.allure.Step
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.response.Response
import org.slf4j.LoggerFactory

class ApiHelper {

    companion object {
        private val logger = LoggerFactory.getLogger(ApiHelper::class.java)
    }

    private val configFileReader: ConfigFileReader = ConfigFileReader()

    init {
        RestAssured.baseURI = configFileReader.getProperty("url")
    }

    fun getStatusAfterBuildComplete(buildId: Int): String? {
        var pollingEfforts = 0
        var lastBuildId = getLastBuildId()
        while (pollingEfforts < 5) {
            if (buildId != lastBuildId) {
                try {
                    Thread.sleep(15000)
                    logger.info("Requesting status of build with id $buildId")
                    lastBuildId = getLastBuildId()
                    pollingEfforts++
                } catch (e: InterruptedException) {
                    logger.error(e.toString())
                }
            } else break
            // throw new Exception("Build not found");?
        }
        return getStatus(buildId)
    }

    @Step("Run build")
    fun runBuild(buildConfId: String): Int {
        logger.info("Sending request to run build with configuration ID = $buildConfId to ${RestAssured.baseURI}")
        val requestBody = // language=JSON
            """{
                "buildType": {
                              "id": "$buildConfId"
                              }
            }""".trimIndent()
        val response: Response = RestAssured.given()
            .auth()
            .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(requestBody)
            .`when`()
            .post("app/rest/buildQueue")
            .then()
            .extract().response()
        logger.info("Build with Id ${response.jsonPath().getInt("id")} was run")
        return response.jsonPath().getInt("id")
    }

    fun getLastBuildId(): Int {
        val response: Response = RestAssured.given()
            .auth()
            .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .get("app/rest/builds")
            .then()
            .extract().response()
        return response.jsonPath().getInt("build.id[0]")
    }


    fun getStatus(buildId: Int): String? {
        var status: String? = null
        logger.info("Sending request to get status of build with id=" + buildId + " to " + RestAssured.baseURI)
        val response: Response = RestAssured.given()
            .auth()
            .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .`when`()
            .get("app/rest/builds?locator=id:$buildId")
            .then()
            .extract().response()
        status = response.jsonPath().getString("build.status[0]")
        logger.info("Build  status is $status")
        return status
    }

    fun deleteProject(vararg projectNames: String) {
        for (projectName in projectNames) {
            logger.info("Sending request to delete project with name=$projectName to " + RestAssured.baseURI)
            val response: Response = RestAssured.given()
                .auth()
                .basic(configFileReader.getProperty("username"), configFileReader.getProperty("userpassword"))
                .`when`()
                .delete("httpAuth/app/rest/projects/$projectName")
                .then()
                .extract().response()
            if (response.statusCode == 204) {
                logger.info("Project was successfully deleted")
            }
            response.statusCode
        }
    }

}