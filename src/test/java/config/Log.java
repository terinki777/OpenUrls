package config;

import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Log {

    public static Logger getLOGGER(String clazz) {
        try(FileInputStream ins = new FileInputStream("src/test/resources/logging.properties")){
            LogManager.getLogManager().readConfiguration(ins);
            return Logger.getLogger(clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}