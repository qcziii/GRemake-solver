package org.qczi;

import org.qczi.io.InteractiveConfigWizard;
import org.qczi.io.InputParser;
import org.qczi.model.PuzzleConfig;
import org.qczi.solver.BfsSolver;
import org.qczi.solver.SolutionFormatter;
import org.qczi.solver.SolveResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.util.Scanner;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        PuzzleConfig config;

        if (args.length > 1) {
            System.out.println("Uzycie: mvn exec:java \"-Dexec.args=<sciezka-do-pliku-json>\"");
            System.out.println("Lub bez argumentow, aby wpisac konfiguracje krok po kroku w konsoli.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        try {
            if (args.length == 1) {
                Path configPath = Path.of(args[0]);
                config = InputParser.parse(configPath);
            } else {
                config = InteractiveConfigWizard.readFromConsole(scanner);
            }

            BfsSolver solver = new BfsSolver();
            SolveResult result = solver.solve(config);

            System.out.println(SolutionFormatter.format(result));
            System.out.println(result);
        } catch (Exception e) {
            System.err.println("Blad: " + e.getMessage());
        }
    }
}