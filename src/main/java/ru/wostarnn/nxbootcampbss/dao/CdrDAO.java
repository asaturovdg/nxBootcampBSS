package ru.wostarnn.nxbootcampbss.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class CdrDAO {
    @Value("${cdr.path}")
    private String cdrPath;
    public List<String> read() {

        File file = new File(cdrPath);
        FileReader fileReader;
        String line;
        List<String> strings = new ArrayList<>();
        try {
            fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            while (reader.ready()) {
                line = reader.readLine();
                strings.add(line);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return strings;
    }
}

