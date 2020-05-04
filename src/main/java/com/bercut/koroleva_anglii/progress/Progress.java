package com.bercut.koroleva_anglii.progress;

import com.bercut.koroleva_anglii.model.*;

import java.util.LinkedList;
import java.util.List;

import static com.bercut.koroleva_anglii.model.BlockType.SEQUENTAL;

public class Progress {

    private final Model model;
    private final List<Answer> currentAnswers = new LinkedList<>();

    private Step currentStep;
    private Node currentBlock;
    private MessageType waitingMessageType;

    public Progress(Model model) {
        this.model = model;
        this.waitingMessageType = MessageType.FIRST;
    }

    public String handleMessage(String message) {
        switch (waitingMessageType) {
            case FIRST -> {
                currentBlock = model.getBlock();
                if (currentBlock.getBlockType() == BlockType.INFO) {
                    waitingMessageType = MessageType.INFO;
                } else {
                    waitingMessageType = MessageType.START;
                }
                return currentBlock.getWelcomeMessage();
            }
            case INFO -> {
                Transition transition = currentBlock.getTransition(message);
                if (transition != null) {
                    currentAnswers.clear();
                    waitingMessageType = MessageType.START;
                    return transition.getMessage();
                } else {
                    return "Переход не найден";
                }
            }
            case START -> {
                currentStep = currentBlock.getNextStep(null);
                waitingMessageType = MessageType.STEP;
                return currentStep.getMessage();
            }
            case STEP -> {
                Answer answer = currentStep.getAnswer(message);
                if (answer == null) {
                    waitingMessageType = MessageType.STEP;
                    return "Неопознанный ответ";
                }
                currentAnswers.add(answer);
                Transition transition = currentBlock.getTransition(currentBlock.getGroup(), currentAnswers);
                if (transition != null) {
                    currentAnswers.clear();
                    waitingMessageType = MessageType.START;
                    return transition.getMessage();
                } else {
                    waitingMessageType = MessageType.STEP;
                    if (currentBlock.getBlockType() == SEQUENTAL) {
                        currentStep = currentBlock.getNextStep(currentStep);
                        if (currentStep == null) {
                            return "Не найден следующий шаг, но и условие перехода не сработало";
                        }
                        return currentStep.getMessage();
                    }
                }
            }
            case TRANSITION -> {
                return "Не поддерживаемый тип сообщения: transition";
                /*for (Transition transition : currentBlock.getTransitions()) {
                    if (transition.isConditionSuccess(message)) {
                        currentBlock = transition.getBlock();
                        waitingMessageType = MessageType.WELCOME;
                        return currentBlock.getWelcomeMessage();
                    }
                }*/
            }
        }
        return "invalid command";
    }
}
