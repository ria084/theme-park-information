package com.ria084.themeparkinformation.batch.util

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

@SpringBootTest
class UtilFileTest extends Specification {

    public static final String baseDirectory = System.getProperty("user.home");

    def "GenerateFile_正常系"() {
        given:
        String contents = "test";
        String fileName = "testFile"
        Path filePath = Path.of(baseDirectory, fileName + ".json")

        when:
        UtilFile.generateFile(contents, fileName)

        then:
        filePath.toFile().exists()

        cleanup:
        Files.delete(filePath)
    }
}
