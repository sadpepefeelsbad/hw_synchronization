import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        // Thread's logic
        Runnable logic = () -> {
            String route = generateRoute("RLRFR", 100);
            int count = 0;

            for (char letter : route.toCharArray()) {
                if (letter == 'R') count++;
            }

            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(count)) {
                    sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                } else {
                    sizeToFreq.put(count, 1);
                }
            }
        };

        // Threads' creation
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(logic);
            thread.start();
        }

        // Finding key for max freq
        int maxKey = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();

        // Summary
        System.out.printf("Самое частое количество повторений %d (встретилось %d раз) \n", maxKey, sizeToFreq.get(maxKey));
        System.out.println("Другие размеры: ");

        for (Integer size : sizeToFreq.keySet()) {
            System.out.printf("- %d (%d раз) \n", size, sizeToFreq.get(size));
        }

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}