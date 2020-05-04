package com.bercut.koroleva_anglii;

import com.bercut.koroleva_anglii.model.*;
import com.bercut.koroleva_anglii.progress.ModelExecutor;
import com.bercut.koroleva_anglii.upload.JsonEdge;
import com.bercut.koroleva_anglii.upload.JsonModel;
import com.bercut.koroleva_anglii.upload.JsonNode;
import com.bercut.koroleva_anglii.upload.JsonQuestion;
import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
    public String upload(@RequestBody Map<String, Object> request) throws IOException {
        String jsonStr = Files.readString(Paths.get("./graph_model/multiplication_quest2.json"));
        Gson gson = new Gson();
        JsonModel jsonModel = gson.fromJson(jsonStr, JsonModel.class);

        Map<String, Group> groups = new HashMap<>();
        for (Map.Entry<String, List<JsonQuestion>> questionGroupEntry : jsonModel.getGraph().getMetadata().getQuestionGroups().entrySet()) {
            Group group = new Group();
            List<Step> steps = new LinkedList<>();
            for (JsonQuestion jsonQuestion : questionGroupEntry.getValue()) {
                Step step = new Step(jsonQuestion.getText());
                List<Answer> answers = new LinkedList<>();
                answers.add(new Answer(GOOD, message -> jsonQuestion.getAnswer().equalsIgnoreCase(message)));
                answers.add(new Answer(BAD, message -> true));
                step.setAnswers(answers);
            }
            group.setSteps(steps);
            groups.put(questionGroupEntry.getKey(), group);
        }

        Map<String, Node> nodes = new HashMap<>();
        for (Map.Entry<String, JsonNode> nodeEntry : jsonModel.getGraph().getNodes().entrySet()) {
            JsonNode jsonNode = nodeEntry.getValue();
            Node node = new Node(nodeEntry.getKey(), jsonNode.getMetadata().getNodeData().getText(),
                    BlockType.getByName(jsonNode.getMetadata().getNodeData().getType()));
            node.setGroup(groups.get(jsonNode.getMetadata().getNodeData().getQuestionGroup()));
            nodes.put(nodeEntry.getKey(), node);
        }

        for (JsonEdge jsonEdge : jsonModel.getGraph().getEdges()) {
            Node source = nodes.get(jsonEdge.getSource());
            Node target = nodes.get(jsonEdge.getTarget());
            if (jsonEdge.getMetadata().getTransitionProperties().getCanGo() != null) {
                source.getTransitions().add(new Transition(target,
                        transitionCallbackMap.get(jsonEdge.getMetadata().getTransitionProperties().getCanGo())));
            } else if (jsonEdge.getMetadata().getTransitionProperties().getEquals() != null) {
                source.getTransitions().add(new Transition(target, new TransitionCallback() {
                    @Override
                    public boolean handle(Group group, List<Answer> message) {
                        return false;
                    }

                    @Override
                    public boolean handle(String message) {
                        return jsonEdge.getMetadata().getTransitionProperties().getEquals().contains(message);
                    }
                }));
            }
        }
        modelExecutor.setModel(new Model(nodes.values().iterator().next()));
        return "ok";
    }

    private static TransitionCallback allAnswersSuccess = new TransitionCallback() {
        @Override
        public boolean handle(Group group, List<Answer> message) {
            int goodCounter = 0;
            for (Answer answer : message) {
                if (answer.getAnswerType() == GOOD) {
                    goodCounter++;
                }
            }
            return goodCounter == group.getSteps().size();
        }

        @Override
        public boolean handle(String message) {
            return false;
        }
    };

    private static TransitionCallback fail3InRow = new TransitionCallback() {
        @Override
        public boolean handle(Group group, List<Answer> message) {
            int badCounter = 0;
            for (Answer answer : message) {
                if (answer.getAnswerType() == BAD) {
                    badCounter++;
                } else if (answer.getAnswerType() == GOOD) {
                    badCounter++;
                }
            }
            return badCounter >= 3;
        }

        @Override
        public boolean handle(String message) {
            return false;
        }
    };

    private static final Map<String, TransitionCallback> transitionCallbackMap = new HashMap<>();

    static {
        transitionCallbackMap.put("allAnswersInGroupAreCorrect", allAnswersSuccess);
        transitionCallbackMap.put("serialIncorrectAnswers3", fail3InRow);
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


        List<Transition> transitions1 = new LinkedList<>();
        transitions1.add(new Transition(block2, allAnswersSuccess));
        transitions1.add(new Transition(block3, allAnswersSuccess));
        block1.setTransitions(transitions1);

        List<Transition> transitions2 = new LinkedList<>();
        transitions2.add(new Transition(block1, allAnswersSuccess));
        transitions2.add(new Transition(block3, allAnswersSuccess));
        block2.setTransitions(transitions2);

        List<Transition> transitions3 = new LinkedList<>();
        transitions3.add(new Transition(block1, allAnswersSuccess));
        transitions3.add(new Transition(block2, allAnswersSuccess));
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
        return new Node(name, String.format("Вход в блок %s, ты готов?", name), BlockType.SEQUENTAL);
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
