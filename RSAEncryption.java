import java.util.HashSet;
import java.util.Random;

public class RSAEncryption {

    private static HashSet<Integer> prime = new HashSet<>();
    private static Integer public_key;
    private static Integer private_key;
    private static Integer n;

    public static void main(String[] args) {
        primeFiller();
        setKeys();

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Enter the message: ");
        String message = scanner.nextLine();

        int[] coded = encoder(message);

        System.out.println("Initial message:");
        System.out.println(message);
        System.out.println("\n\nThe encoded message (encrypted by public key)\n");
        for (int p : coded) {
            System.out.print(p + " ");
        }

        System.out.println("\n\nThe decoded message (decrypted by private key)\n");
        String decoded = decoder(coded);
        System.out.println(decoded);
    }

    private static void primeFiller() {
        boolean[] sieve = new boolean[250];
        sieve[0] = false;
        sieve[1] = false;
        for (int i = 2; i < 250; i++) {
            sieve[i] = true;
        }

        for (int i = 2; i < sieve.length; i++) {
            if (sieve[i]) {
                prime.add(i);
                for (int j = i * 2; j < sieve.length; j += i) {
                    sieve[j] = false;
                }
            }
        }
    }

    private static int pickRandomPrime() {
        int k = new Random().nextInt(prime.size());
        int ret = 0;
        for (int num : prime) {
            if (k == 0) {
                ret = num;
                break;
            }
            k--;
        }
        prime.remove(ret);
        return ret;
    }

    private static void setKeys() {
        int prime1 = pickRandomPrime();
        int prime2 = pickRandomPrime();

        n = prime1 * prime2;
        int fi = (prime1 - 1) * (prime2 - 1);

        int e = 2;
        while (true) {
            if (gcd(e, fi) == 1) {
                break;
            }
            e++;
        }
        public_key = e;

        int d = 2;
        while (true) {
            if ((d * e) % fi == 1) {
                break;
            }
            d++;
        }
        private_key = d;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static int[] encoder(String message) {
        int[] encoded = new int[message.length()];
        for (int i = 0; i < message.length(); i++) {
            encoded[i] = encrypt((int) message.charAt(i));
        }
        return encoded;
    }

    private static String decoder(int[] encoded) {
        StringBuilder sb = new StringBuilder();
        for (int num : encoded) {
            sb.append((char) decrypt(num));
        }
        return sb.toString();
    }

    private static int encrypt(int message) {
        int e = public_key;
        int encryptedText = 1;
        while (e > 0) {
            encryptedText *= message;
            encryptedText %= n;
            e--;
        }
        return encryptedText;
    }

    private static int decrypt(int encryptedText) {
        int d = private_key;
        int decrypted = 1;
        while (d > 0) {
            decrypted *= encryptedText;
            decrypted %= n;
            d--;
        }
        return decrypted;
    }
}

