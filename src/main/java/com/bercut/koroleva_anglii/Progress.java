package com.bercut.koroleva_anglii;

import java.util.LinkedList;
import java.util.List;

import static com.bercut.koroleva_anglii.StepChooser.SEQUENTAL;

public class Progress {

    private final Model model;
    private final List<AnswerHandler> currentAnswerHandlers = new LinkedList<>();

    private Step currentStep;
    private Block currentBlock;
    private MessageType waitingMessageType;

    public Progress(Model model) {
        this.model = model;
        this.waitingMessageType = MessageType.FIRST;
    }

    public String handleMessage(String message) {
        switch (waitingMessageType) {
            case FIRST -> {
                currentBlock = model.getBlock();
                waitingMessageType = MessageType.WELCOME;
                return currentBlock.getWelcomeMessage();
            }
            case WELCOME -> {
                currentStep = currentBlock.getNextStep(null);
                waitingMessageType = MessageType.STEP;
                return currentStep.getMessage();
            }
            case STEP -> {
                for (AnswerHandler handler : currentStep.getAnswerHandlers()) {
                    if (handler.handle(message)) {
                        currentAnswerHandlers.add(handler);
                        Block block = currentStep.getBlock();
                        if (block.canGo(currentAnswerHandlers)) {
                            currentAnswerHandlers.clear();
                            waitingMessageType = MessageType.CANGO;
                            return block.getCanGoMessage();
                        } else {
                            if (block.getStepChooser() == SEQUENTAL) {
                                currentStep = currentBlock.getNextStep(currentStep);
                                if (currentStep != null) {
                                    waitingMessageType = MessageType.STEP;
                                    return currentStep.getMessage();
                                }
                            }
                        }
                    }
                }
            }
            case CANGO -> {
                for (Transition transition : currentBlock.getTransitions()) {
                    if (transition.isConditionSuccess(message)) {
                        currentBlock = transition.getBlock();
                        waitingMessageType = MessageType.WELCOME;
                        return currentBlock.getWelcomeMessage();
                    }
                }
            }
        }
        return "invalid command";
    }
}
