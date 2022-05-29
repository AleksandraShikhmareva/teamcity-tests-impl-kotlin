package com.shikhmareva.dataProvider

import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

class ConfigFileReader {

    companion object {
        private val logger = LoggerFactory.getLogger(ConfigFileReader::class.java)
    }

    private lateinit var properties: Properties

    init {
        val propertyFilePath = "src//main//resources//project.properties"
        try {
            BufferedReader(FileReader(propertyFilePath)).use { bufferedReader ->  //try with resources
                properties = Properties()
                properties.load(bufferedReader)
            }
        } catch (e: IOException) {
            logger.error("Config FileReader Initialization error", e);
            throw RuntimeException("Configuration.properties not found at $propertyFilePath", e)
        }
    }

    fun getProperty(name: String): String {
        return properties.getProperty(name)
    }
}