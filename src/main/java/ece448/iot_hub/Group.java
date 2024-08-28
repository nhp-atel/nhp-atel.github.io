package ece448.iot_hub;

import java.util.HashSet;

public class Group {
    private String name;
    private HashSet<String> members;

    // Constructors
    public Group() {
        this.members = new HashSet<>();
    }

    public Group(String name, HashSet<String> members) {
        this.name = name;
        this.members = members;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<String> getMembers() {
        return members;
    }

    public void setMembers(HashSet<String> members) {
        this.members = members;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Group{" +
               "name='" + name + '\'' +
               ", members=" + members +
               '}';
    }
}
