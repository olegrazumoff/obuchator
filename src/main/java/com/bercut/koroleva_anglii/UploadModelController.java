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
                steps.add(step);
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
            if (jsonEdge.getMetadata().getTransitionData().getCanGo() != null) {
                source.getTransitions().add(new Transition(target,
                        transitionCallbackMap.get(jsonEdge.getMetadata().getTransitionData().getCanGo())));
            } else if (jsonEdge.getMetadata().getTransitionData().getEquals() != null) {
                source.getTransitions().add(new Transition(target, new TransitionCallback() {
                    @Override
                    public boolean handle(Group group, List<Answer> message) {
                        return false;
                    }

                    @Override
                    public boolean handle(String message) {
                        return jsonEdge.getMetadata().getTransitionData().getEquals().contains(message);
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
                    badCounter = 0;
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
    private void init() throws IOException {
        upload(null);
    }
}
