package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Create_the_list {
    public static String create() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(Students_Selector_app.backupPath));
        String line;
        List<String> txt = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            txt.add(line);
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(Students_Selector_app.filePath));
        for (int j = 0; j < txt.size(); j++) {
            writer.write(j + 1 + " " + txt.get(j));
            writer.newLine();
        }
        writer.close();
        return "List has been rewritten. ";
    }
}

