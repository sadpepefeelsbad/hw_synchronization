import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        // Counting thread's logic
        Runnable counting = () -> {
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

                sizeToFreq.notify();
            }
        };

        // Freqlog thread's logic
        Runnable logging = () -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {

                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    // Finding key for max freq
                    int maxKey = Collections.max(sizeToFreq.entrySet(), Map.Entry.comparingByValue()).getKey();
                    System.out.printf("Самое частое количество повторений %d (встретилось %d раз) \n", maxKey, sizeToFreq.get(maxKey));
                }
            }
        };

        List<Thread> threads = new ArrayList<>();

        // Logging thread's creation
        Thread freqLog = new Thread(logging);
        freqLog.start();

        // Counting threads' creation
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(counting);
            threads.add(thread);
            thread.start();
        }

        // FreqLog thread's interruption
        for (Thread thread : threads) {
            thread.join();
        }
        freqLog.interrupt();


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