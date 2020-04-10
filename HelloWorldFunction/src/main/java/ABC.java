import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ABC {


    public static void main(String[] args) {

        final List<String> integers = new ArrayList<String>();
        integers.add("A");
        integers.add("B");
        integers.add("C");
        integers.add("D");
        for (int i = 0; !integers.isEmpty();) {
            integers.remove(integers.get(i));
        }


    }


}


