package com.bercut.koroleva_anglii;

import java.util.List;

public class Block {
    private List<Step> steps;
    private List<Transition> transitions;
    private StepChooser stepChooser;
    private String welcomeMessage;
    private String canGoMessage;

    public Block(String welcomeMessage, StepChooser stepChooser) {
        this.stepChooser = stepChooser;
        this.welcomeMessage = welcomeMessage;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public boolean canGo(List<AnswerHandler> currentAnswerHandlers) {
        //TODO логика из модели
        return false;
    }

    public StepChooser getStepChooser() {
        return stepChooser;
    }

    public String getCanGoMessage() {
        return canGoMessage;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public Step getNextStep(Step currentStep) {
        if (currentStep != null) {
            return steps.get(steps.indexOf(currentStep) + 1);
        } else {
            return steps.get(0);
        }
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }
}
