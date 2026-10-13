package org.qczi.service;

import org.qczi.io.JsonConfigWizard;
import org.qczi.model.PuzzleConfig;
import org.qczi.request.AppRequest;
import org.qczi.solver.BfsSolver;
import org.qczi.solver.SolutionFormatter;
import org.qczi.solver.SolveResult;
import org.springframework.stereotype.Service;

@Service
public class AppService {


    private final BfsSolver bfsSolver;
    private final JsonConfigWizard jsonConfigWizard;
    private final SolutionFormatter solutionFormatter;


    public AppService(BfsSolver bfsSolver, JsonConfigWizard jsonConfigWizard, SolutionFormatter solutionFormatter) {
        this.bfsSolver = bfsSolver;
        this.jsonConfigWizard = jsonConfigWizard;
        this.solutionFormatter = solutionFormatter;
    }

    public SolveResult solve(AppRequest appRequest){
        SolveResult solveResult=  bfsSolver.solve(jsonConfigWizard.readFromJson(appRequest));
        return solutionFormatter.formatDirection(solveResult);
    }

}
