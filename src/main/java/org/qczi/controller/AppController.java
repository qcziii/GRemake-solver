package org.qczi.controller;

import org.qczi.request.AppRequest;
import org.qczi.service.AppService;
import org.qczi.solver.SolveResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @PostMapping
    public ResponseEntity<SolveResult> solve (@RequestBody AppRequest appRequest) {
        return new ResponseEntity<>(appService.solve(appRequest), HttpStatus.ACCEPTED);
    }


}
