package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Category;
import model.Item;
import model.Method;
import model.Node;

/*
 * Manages the reading of data from json files; contains the path to the source json file
 * Note: inspired by the JsonSerializationDemo, link found below
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master
 */
public class JsonReader {
    private String path;

    // EFFECTS: initializes reader to read from source file
    public JsonReader(String path) {
        this.path = path;
    }

    // EFFECTS: reads all the root nodes from file and returns it;
    // throws IOException if an error occurs reading data from file
    public List<Node> read() throws IOException {
        String jsonData = readFile(path);
        JSONArray jsonArray = new JSONArray(jsonData);
        return parseTrees(jsonArray);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses all the root nodes from JSON array and returns them
    private List<Node> parseTrees(JSONArray jsonArray) {
        List<Node> savedTreeList = new ArrayList<Node>();
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            savedTreeList.add(parseNode(json));
        }
        return savedTreeList;
    }

    // EFFECTS: parses JSON object into node and returns it
    private Node parseNode(JSONObject jsonObject) {
        Node newNode = new Node(parseItem(jsonObject.getJSONObject("item")));
        JSONArray nodeChildren = jsonObject.getJSONArray("children");
        if (nodeChildren.length() != 0) {
            addChildren(newNode, nodeChildren);
        }
        return newNode;
    }

    // EFFECTS: parses JSON object into item and returns it
    private Item parseItem(JSONObject item) {
        String name = "";
        Category category = Category.COMPONENT;
        Method method = Method.COOK;
        int amount = 3;
        int cost = 100;

        name = item.getString("name");
        category = Category.valueOf(item.getString("category"));
        method = Method.valueOf(item.getString("method"));
        amount = item.getInt("amount");
        cost = item.getInt("cost");

        return new Item(name, category, method, amount, cost);
    }

    // MODIFIES: node
    // EFFECTS: parses child nodes from JSON array and adds them to the node
    private void addChildren(Node node, JSONArray jsonArray) {
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) object;
            JSONArray nodeChildren = json.getJSONArray("children");
            Node newNode = node.addChild(parseItem(json.getJSONObject("item")));
            if (nodeChildren.length() != 0) {
                addChildren(newNode, nodeChildren);
            }
        }
    }
}