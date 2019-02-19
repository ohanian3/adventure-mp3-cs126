
public class Source {

    public static void main(String[] args) {
        System.out.println("takes argument as source path or source url:");
        if (args.length < 1) {
            System.out.println("missing argument");
        } else {
            try {
                Main.main(args);
            } catch (Exception E) {
                System.out.println("invalid argument");
            }
        }
    }

}
