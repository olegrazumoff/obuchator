package com.bercut.koroleva_anglii;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.bercut.koroleva_anglii.AnswerType.BAD;
import static com.bercut.koroleva_anglii.AnswerType.GOOD;

@Controller
@RequestMapping("/upload")
public class UploadModelController {

    private final ModelExecutor modelExecutor;

    public UploadModelController(ModelExecutor modelExecutor) {
        this.modelExecutor = modelExecutor;
    }

    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Map<String, Object> upload(@RequestBody Map<String, Object> request) {
        return request;
    }

    @PostConstruct
    private void init() {
        Block block1 = newBlock("1");
        Block block2 = newBlock("2");
        Block block3 = newBlock("3");

        List<Transition> transitions1 = new LinkedList<>();
        transitions1.add(new Transition(block2, "2"::equals));
        transitions1.add(new Transition(block3, "3"::equals));
        block1.setTransitions(transitions1);

        List<Transition> transitions2 = new LinkedList<>();
        transitions2.add(new Transition(block1, "1"::equals));
        transitions2.add(new Transition(block3, "3"::equals));
        block2.setTransitions(transitions2);

        List<Transition> transitions3 = new LinkedList<>();
        transitions3.add(new Transition(block1, "1"::equals));
        transitions3.add(new Transition(block2, "2"::equals));
        block3.setTransitions(transitions3);

        Model model = new Model(block1);
        modelExecutor.setModel(model);
    }

    private Block newBlock(String name) {
        Block block = new Block("Вход в блок " + name, StepChooser.SEQUENTAL);
        List<Step> steps = new LinkedList<>();
        steps.add(newStep(block, "Шаг 1 в блоке " + name, name + "1"));
        steps.add(newStep(block, "Шаг 2 в блоке " + name, name + "2"));
        block.setSteps(steps);
        return block;
    }

    private Step newStep(Block block, String message, String answer) {
        Step step = new Step(message, block);
        List<AnswerHandler> answerHandlers = new LinkedList<>();
        answerHandlers.add(new AnswerHandler(step, GOOD, answer::equals));
        answerHandlers.add(new AnswerHandler(step, BAD, msg -> true));
        step.setAnswerHandlers(answerHandlers);
        return step;
    }
}
