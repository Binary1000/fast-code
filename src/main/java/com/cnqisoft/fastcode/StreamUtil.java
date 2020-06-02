package com.cnqisoft.fastcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Binary on 2020/6/2
 */
public class StreamUtil {

    public static String readString(InputStream inputStream, String charsetName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charsetName);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
        ){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\r\n");
            }
            return stringBuilder.toString();
        }
    }

}
