package encryptdecrypt;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        // setup, variables
        String result = "";
        String alg = "";
        boolean defaultAlg = true;
        String mode = "";
        boolean defaultMode = true;
        int key = 0;
        boolean defaultKey = true;
        String data = "";
        boolean defaultData = true;
        String in = "";
        boolean isIn = false;
        boolean takeIn = true;
        String out = "";
        boolean isOut = false;

        // looking for parameters
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-key")) {
                key = Integer.parseInt(args[i + 1]);
                defaultKey = false;
            }
            if (args[i].equals("-data")) {
                data = args[i + 1];
                defaultData = false;
                takeIn = false;
            }
            if (args[i].equals("-mode")) {
                mode = args[i + 1];
                defaultMode = false;
            }
            if (args[i].equals("-in")) {
                isIn = true;
                in = args[i + 1];
            }
            if (args[i].equals("-out")) {
                out = args[i + 1];
                if (!out.equals("")) {
                    isOut = true;
                }
            }
            if (args[i].equals("-alg")) {
                alg = args[i + 1];
                defaultAlg = false;
            }
        }
        // default parameters check
        if (defaultKey) {
            key = 0;
        }
        if (defaultMode) {
            mode = "enc";
        }
        if (defaultData) {
            data = "";
        }
        if (defaultAlg) {
            alg = "shift";
        }
        /* PROGRAM LOGIC */

        // taking -data over -in
        if (takeIn && isIn) {
            File file = new File(in);
            try {
                Scanner scanner = new Scanner(file);
                data = scanner.nextLine();
                scanner.close();
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
        // miracle
        if (alg.equals("shift")) {
            Shifting shifted = new Shifting(key, data, mode);
            result = shifted.mainMethod();
        } else {
            Unicode unicoded = new Unicode(key, data, mode);
            result = unicoded.mainMethod();
        }

        // output
        if (isOut) {
            File file = new File(out);
            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.println(result);
            } catch (IOException e) {
                System.out.println("Error");
            }
        } else {
            System.out.println(result);
        }

    }
}


/* ABSTRACT CLASS FOR ALGS */

abstract class algs {

    int shiftKey;
    String type;
    String target;
    int signPosition;
    int changeIterator;
    char[] signsToMove;
    String result = "";

    public algs(int shiftKey, String target, String type) {
        this.shiftKey = shiftKey;
        this.target = target;
        this.type = type;
        this.changeIterator = target.length();
        this.signsToMove = target.toCharArray();
    }

    abstract String mainMethod();

    abstract String encrypt();

    abstract String decrypt();
}

/* SHIFT CLASS */

class Shifting extends algs {

    protected String base = "abcdefghijklmnopqrstuvwxyz";
    protected char[] baseSigns = base.toCharArray();

    public Shifting(int shiftKey, String target, String type) {
        super(shiftKey, target, type);
    }

    public String mainMethod() {
        if(type.equals("enc"))
            return encrypt();
        else
            return decrypt();
    }

    public String encrypt() {
        for(int i = 0; i < changeIterator; i++) {
            signPosition = base.indexOf(signsToMove[i]);
            if(signPosition == -1)
                result = result + signsToMove[i];
            else
                result = result + baseSigns[(signPosition + shiftKey) % baseSigns.length];
        }
        return result;
    }

    public String decrypt() {
        int index = 0;
        for(int i = 0; i < changeIterator; i++) {
            signPosition = base.indexOf(signsToMove[i]);
            if(signPosition == -1) {
                result = result + signsToMove[i];
            }
            else if(signPosition - shiftKey < 0 ) {
                index = Math.abs(signPosition - shiftKey) % base.length();
                index = base.length() - index;
            }
            else{
                index = signPosition - shiftKey;
            }
            result = result + baseSigns[index];
        }
        return result;
    }
}

class Unicode extends algs {

    protected int move = 0;

    public Unicode(int shiftKey, String target, String type) {
        super(shiftKey, target, type);
    }

    public String mainMethod() {
        if(type.equals("enc"))
            return encrypt();
        else
            return decrypt();
    }

    public String encrypt() {
        for(int i = 0; i < changeIterator; i++) {
            move = (int) signsToMove[i] + shiftKey;
            result = result + (char) move;
        }
        return result;
    }

    public String decrypt() {
        for(int i = 0; i < changeIterator; i++) {
            move = (int) signsToMove[i] - shiftKey;
            result = result + (char) move;
        }
        return result;
    }
}
