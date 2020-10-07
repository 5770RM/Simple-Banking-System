import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long K = scanner.nextLong();
        long N = scanner.nextLong();
        double M = scanner.nextDouble();

        int counter = 0;

        while (true) {
            Random random = new Random(K);
            for (int i = 0; i < N; i++) {
                double num = random.nextGaussian();
                if (num <= M) {
                    counter++;
                }
            }
            if (counter == N) {
                System.out.println(K);
                break;
            } else {
                K++;
                counter = 0;
            }
        }
        
        


    }
}