package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class FileParser {

    public static Vector<Tweet> readFoot(int n) {
        File file = new File("src/REPORT/Foot.txt");

        Vector<Tweet> vector = new Vector<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        random.nextInt(100);
        int i = 0;
        for (; i < n; ) {
            if (random.nextInt(100) < 10) {
                scanner.nextLine();
                continue;
            }
            Tweet tweet = Tweet.fromString(scanner.nextLine());
            if (tweet != null)
                vector.add(tweet);
            i++;
        }
        return vector;
    }

    public static ObservableList<Tweet> tweets;

    public static ObservableList<Tweet> getTweets() {

        return tweets;
    }

    public static Vector<Tweet> readClimat(int n) {
        File file = new File("src/REPORT/climat.txt");

        Vector<Tweet> vector = new Vector<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        random.nextInt(100);
        int i = 0;
        for (; i < n; ) {
            if (random.nextInt(100) < 10) {
                scanner.nextLine();
                continue;
            }
            Tweet tweet = Tweet.fromString(scanner.nextLine());
            if (tweet != null)
                vector.add(tweet);
            i++;
        }
        return vector;
    }

    public static void load(int climat, int foot) {
        if (tweets == null) {
            tweets = FXCollections.observableArrayList(FileParser.readClimat(climat));
            tweets.addAll(FileParser.readFoot(foot));
        }
    }
}
