import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileNameCsv  = "data.csv";
        String fileNameJson = "data.json";
        writerCSV(fileNameCsv);
        writeString(listToJson(parseCSV(columnMapping, fileNameCsv)),fileNameJson);

    }

    public static void writerCSV(String fileName) {
        String[] employee  = "1,David,Miller,Australia,30".split(",");
        String[] employee2 = "2,Piter,D,German,40".split(",");
        String[] employee3 = "3,Alex,Petrov,Russia,44".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(employee);
            writer.writeNext(employee2);
            writer.writeNext(employee3);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readerCSV(String fileName) {

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            try {
                List<String[]> allRows = reader.readAll();
                for (String[] row : allRows) {
                    System.out.println(Arrays.toString(row));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Employee> parseCSV(String[] columnMapping, String fileName) throws FileNotFoundException {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader).withMappingStrategy(strategy).build();
            List<Employee> list = csv.parse();
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public  static void writeString(String strJson, String fileName ){
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(strJson);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
