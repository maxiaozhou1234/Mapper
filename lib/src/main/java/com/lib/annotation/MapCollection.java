package com.lib.annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mxz on 2020/7/3.
 */
public class MapCollection {

    private final static HashMap<String, HashMap<String, Class>> collection = TypeMap$Data.map;

    public static Class findItem(String typeName) {
        return findItem(null, typeName, false);
    }

    public static Class findItem(String groupName, String typeName) {
        return findItem(groupName, typeName, true);
    }

    public static Class findItem(String groupName, String typeName, boolean findToEnd) {

        if (typeName == null || typeName.length() == 0) {
            return null;
        }

        String group;
        if (groupName == null) {
            group = Constant.defaultGroup;
        } else {
            group = groupName;
        }

        boolean flag = !Constant.defaultGroup.equals(group) && findToEnd;

        HashMap<String, Class> map = collection.get(group);
        Class item = map == null ? null : map.get(typeName);
        //if not found item,it will search from defaultGroup when findToEnd is true
        if (item == null && flag) {
            item = collection.get(Constant.defaultGroup).get(typeName);
        }

        return item;
    }

    public static String string() {
        StringBuilder sb = new StringBuilder();
        int i;
        for (Map.Entry<String, HashMap<String, Class>> item : collection.entrySet()) {
            i = 0;
            String group = item.getKey();
            sb.append("group >>> ").append(group).append(" : [ ");
            for (Map.Entry<String, Class> m : item.getValue().entrySet()) {
                if (i != 0) {
                    sb.append(",[");
                }
                sb.append(m.getKey()).append(" : ").append(m.getValue()).append(" ]");
                i++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
