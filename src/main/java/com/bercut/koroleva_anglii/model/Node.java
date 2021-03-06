package com.bercut.koroleva_anglii.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Node {
    private String id;
    private List<Transition> transitions = new LinkedList<>();
    private BlockType stepChooser;
    private String welcomeMessage;
    private Group group;

    public Node(String id, String welcomeMessage, BlockType stepChooser) {
        this.id = id;
        this.stepChooser = stepChooser;
        this.welcomeMessage = welcomeMessage;
    }

    public List<Step> getSteps() {
        return group.getSteps();
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public BlockType getBlockType() {
        return stepChooser;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    private Random random = new Random();

    public Step getNextStep(Step currentStep) {
        if (getBlockType() == BlockType.SEQUENTAL) {
            if (currentStep != null) {
                int nextIndex = group.getSteps().indexOf(currentStep) + 1;
                if (nextIndex > group.getSteps().size() - 1) {
                    return null;
                }
                return group.getSteps().get(nextIndex);
            } else {
                return group.getSteps().get(0);
            }
        } else if (getBlockType() == BlockType.RANDOM) {
            if (currentStep != null) {
                int currIndex = group.getSteps().indexOf(currentStep);
                int nextIndex = random.nextInt(group.getSteps().size());
                while (nextIndex == currIndex) {
                    nextIndex = random.nextInt(group.getSteps().size());
                }
                return group.getSteps().get(nextIndex);
            } else {
                return group.getSteps().get(0);
            }
        }
        return null;
    }

    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Transition getTransition(Group group, List<Answer> currentAnswers) {
        for (Transition transition : transitions) {
            if (transition.handle(group, currentAnswers)) {
                return transition;
            }
        }
        return null;
    }

    public Transition getTransition(String message) {
        for (Transition transition : transitions) {
            if (transition.handle(message)) {
                return transition;
            }
        }
        return null;
    }

    public Group getGroup() {
        return group;
    }
}
