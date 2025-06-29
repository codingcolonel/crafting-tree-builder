package persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Item;
import model.Node;
import model.TreeManager;

/*
 * Manages the writing of data to json files; contains a TAB amount, writer and destination of the json file
 * Note: inspired by the JsonSerializationDemo, link found below
 * https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo/tree/master
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String path;

    // EFFECTS: creates writer with a path to the destination file
    public JsonWriter(String path) {
        this.path = path;
    }

    // MODIFIES: this
    // EFFECTS: opens writer or throws FileNotFoundException if destination file cannot be opened 
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(path));
    }

    // MODIFIES: this
    // EFFECTS: writes a JSON representation of all the saved trees in TreeManager
    public void write(TreeManager treeManager) {
        JSONArray json = savedTreesToJson(treeManager);
        saveToFile(json.toString(TAB));
    }

    // EFFECTS: returns a JSON array of all savedTree Nodes converted to JSON objects
    private JSONArray savedTreesToJson(TreeManager treeManager) {
        JSONArray jsonArray = new JSONArray();
        for (Node node : treeManager.getSavedTrees()) {
            jsonArray.put(nodeToJson(node));
        }
        return jsonArray;
    }

    // EFFECTS: returns an node converted to a JSON object
    private JSONObject nodeToJson(Node node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("item", itemToJson(node.getItemData()));

        List<Node> childNodes = node.getChildren();
        JSONArray jsonArray = new JSONArray();

        if (childNodes.size() > 0) {
            for (Node childNode : childNodes) {
                jsonArray.put(nodeToJson(childNode));
            }
        } 
        jsonObject.put("children", jsonArray);

        return jsonObject;
    }

    // EFFECTS: returns an Item converted to a JSON object
    private JSONObject itemToJson(Item item) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", item.getName());
        jsonObject.put("category", item.getCategory());
        jsonObject.put("method", item.getMethod());
        jsonObject.put("amount", item.getAmount());
        jsonObject.put("cost", item.getCost());

        return jsonObject;
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}