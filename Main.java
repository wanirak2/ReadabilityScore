package readability;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        ScoreReadability scoreReadability = new ScoreReadability();
        scoreReadability.startScore(args[0]);
    }
}

