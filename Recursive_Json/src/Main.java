import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    static void remove_child(JSONObject parent, JSONObject child, String s) {
        if(!parent.containsKey(s)) {
            parent.put(s, child.get(s));
            child.remove(s);
            return;
        }

        JSONArray arr = new JSONArray();
        arr.add(parent.get(s));
        arr.add(child.get(s));
        parent.put(s, arr);
    }
    static void solve(JSONObject curr, JSONObject parent, JSONObject grandParent) {
        if(curr == null) return;

        JSONObject temp = (JSONObject) curr.clone();
        Set<String> keys = temp.keySet();

        for(String s : keys) {
            if(String.valueOf(curr.get(s).getClass()).equals("class org.json.simple.JSONObject")) {
                JSONObject obj = (JSONObject) curr.get(s);
                solve(obj, curr, parent);
            }
        }

        Set<String> childkeys = temp.keySet();
        if(parent != null) {
            Set<String> parentkeys = parent.keySet();
            for(String child : childkeys) {
                for(String str : parentkeys) {
                    if(str.indexOf(child) != -1) {
                        remove_child(parent, curr, child);
                    }
                }
            }
        }

        if(grandParent != null) {
            Set<String> parentkeys = grandParent.keySet();
            for (String child : childkeys) {
                for(String str : parentkeys) {
                    if(str.indexOf(child) != -1){
                        remove_child(grandParent, curr, child);
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        JSONObject arr = new JSONObject();
        Object obj;

        try {
            obj = parser.parse(new FileReader("simple.json"));
            arr = (JSONObject) obj;
        } catch (FileNotFoundException error) {
            throw new RuntimeException(error);
        } catch (IOException error) {
            throw new RuntimeException(error);
        } catch (ParseException error) {
            obj = new Object();
            System.out.println("Empty file");
        }

        solve(arr, null, null);
        System.out.println(arr);
    }
}