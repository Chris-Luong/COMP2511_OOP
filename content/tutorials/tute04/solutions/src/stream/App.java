package stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<String>(Arrays.asList(new String[] {"1", "2", "3", "4", "5"}));
        strings.forEach(s -> System.out.println(s));

        List<String> strings2 = new ArrayList<String>(Arrays.asList(new String[] {"1", "2", "3", "4", "5"}));
        // Q3 List<Integer> ints = strings2.stream().map(i -> Integer.parseInt(i)).collect(Collectors.toList()); 
        List<Integer> ints = strings2.stream().map(Integer::parseInt).collect(Collectors.toList()); 
        System.out.println(ints);
    }


}
