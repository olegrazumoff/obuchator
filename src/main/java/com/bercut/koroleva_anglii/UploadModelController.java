package com.bercut.koroleva_anglii;

import com.bercut.koroleva_anglii.model.*;
import com.bercut.koroleva_anglii.progress.ModelExecutor;
import com.bercut.koroleva_anglii.upload.JsonGraph;
import com.bercut.koroleva_anglii.upload.JsonModel;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.bercut.koroleva_anglii.model.AnswerType.BAD;
import static com.bercut.koroleva_anglii.model.AnswerType.GOOD;

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
    public Map<String, Object> upload(@RequestBody Map<String, Object> request) throws IOException {
        String jsonStr = Files.readString(Paths.get("./graph_model/multiplication_quest.json"));
        Gson gson = new Gson();
        JsonModel jsonModel = gson.fromJson(jsonStr, JsonModel.class);
        return request;
    }

    @PostConstruct
    private void init() {
        Group group1 = newGroup(Arrays.asList("1", "2"));
        Group group2 = newGroup(Arrays.asList("пять", "шесть"));

        Node block1 = newNode("1");
        Node block2 = newNode("2");
        Node block3 = newNode("3");

        block1.setGroup(group1);
        block2.setGroup(group2);
        block3.setGroup(group2);


        TransitionCallback transitionCallback = (group, message) -> {
            int goodCounter = 0;
            for (Answer answer : message) {
                if (answer.getAnswerType() == GOOD) {
                    goodCounter++;
                }
            }
            return goodCounter == group.getSteps().size();
        };

        List<Transition> transitions1 = new LinkedList<>();
        transitions1.add(new Transition(block2, transitionCallback));
        transitions1.add(new Transition(block3, transitionCallback));
        block1.setTransitions(transitions1);

        List<Transition> transitions2 = new LinkedList<>();
        transitions2.add(new Transition(block1, transitionCallback));
        transitions2.add(new Transition(block3, transitionCallback));
        block2.setTransitions(transitions2);

        List<Transition> transitions3 = new LinkedList<>();
        transitions3.add(new Transition(block1, transitionCallback));
        transitions3.add(new Transition(block2, transitionCallback));
        block3.setTransitions(transitions3);

        Model model = new Model(block1);
        modelExecutor.setModel(model);
    }

    private Group newGroup(List<String> answers) {
        List<Step> steps = new LinkedList<>();
        for (String answer : answers) {
            steps.add(newStep(String.format("Вопрос, напиши ответ '%s'?", answer), answer));
        }
        Group group = new Group();
        group.setSteps(steps);
        return group;
    }

    private Node newNode(String name) {
        return new Node(String.format("Вход в блок %s, ты готов?", name), StepChooser.SEQUENTAL);
    }

    private Step newStep(String message, String answer) {
        Step step = new Step(message);
        List<Answer> answerHandlers = new LinkedList<>();
        answerHandlers.add(new Answer(GOOD, answer::equals));
        answerHandlers.add(new Answer(BAD, msg -> true));
        step.setAnswers(answerHandlers);
        return step;
    }
}
