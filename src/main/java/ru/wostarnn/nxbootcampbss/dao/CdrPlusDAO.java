package ru.wostarnn.nxbootcampbss.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CdrPlusDAO {
    @Value("${cdr.plus.path}")
    private String cdrPlusPath;
    public void write(List<String> strings) {

        try {
            File file = new File(cdrPlusPath);
            FileOutputStream fos = new FileOutputStream(file);
            for (String string : strings) {
                fos.write(string.getBytes());
                fos.write("\n".getBytes());
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> read() {

        File file = new File(cdrPlusPath);
        FileReader fileReader;
        String line;
        List<String> callsList = new ArrayList<>();
        try {
            fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            while (reader.ready()) {
                line = reader.readLine();
                callsList.add(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return callsList;
    }
}
