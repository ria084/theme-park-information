package com.ria084.themeparkinformation.batch.util;

import com.ria084.themeparkinformation.batch.exception.ThemeParkInformationException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class UtilFile {

    public static final String baseDirectory = System.getProperty("user.home");

    public static void generateFile(String contents, String fileName) throws ThemeParkInformationException {
        Path filePath = Path.of(baseDirectory, fileName + ".json");
        try {
            Files.writeString(filePath, contents);
        } catch (IOException e) {
            throw new ThemeParkInformationException("ファイルの書き込みに失敗しました", e);
        }
    }
}
