import java.util.stream.IntStream;

/* Please, do not rename it */
class Problem {

    public static void main(String[] args) {
        String operator = args[0];

        switch (operator) {
            case "MAX":
                int max = Integer.MIN_VALUE;
                for (int i = 1; i < args.length; i++) max = Math.max(Integer.parseInt(args[i]), max);
                System.out.println(max);
                break;

            case "MIN":
                int min = Integer.parseInt(args[1]);
                for (int i = 2; i < args.length; i++) min = Math.min(Integer.parseInt(args[i]), min);
                System.out.println(min);
                break;

            case "SUM":
                long sum = 0;
                for (int i = 1; i < args.length; i++) sum += Integer.parseInt(args[i]);
                System.out.println(sum);
                break;
        }
    }
}