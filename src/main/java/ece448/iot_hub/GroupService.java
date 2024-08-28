package ece448.iot_hub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

@Service
public class GroupService {
    private static final String FILE_PATH = "groups.json";
    private static HashSet<String> groupNames;
    private static HashSet<Group> groups;
    private final ObjectMapper mapper;

    public GroupService() {
        this.mapper = new ObjectMapper();
        groups = new HashSet<Group>();
        groupNames = new HashSet<String>();
        this.loadData();
    }

    private HashSet<Group> initializeEmptyGroupsFile() {
        HashSet<Group> emptySet = new HashSet<>();
        try {
            // Create an empty JSON file if it does not exist
            mapper.writeValue(new File(FILE_PATH), emptySet);
            groups = emptySet;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emptySet;
    }

    private void loadData() {
        try {
            File file = new File(FILE_PATH);
            if (file.exists()) {
                // Read JSON file and convert it to a Set of Group objects
                groups = mapper.readValue(file, new TypeReference<HashSet<Group>>() {
                });
            } else {
                initializeEmptyGroupsFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashSet<Group> getAllGroups() {
        return groups;
    }

    public Optional<Group> getGroup(String name) {
        return groups.stream()
                .filter(group -> group.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public Group saveGroup(Group group) throws Exception {

        groups.add(group);
        try {
            // Write the Set of Group objects to a JSON file
            mapper.writeValue(new File(FILE_PATH), groups);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return group;
    }

    public Group updateGroupMembers(String name, HashSet<String> newMembers) throws Exception {
        Group newGroup = new Group(name, newMembers);
        Optional<Group> group = getGroup(name);

        try {
            if (group.isPresent()) {
                groups.remove(group.get());
                saveGroup(newGroup);
            } else {
                String message = String.format("GroupName %s does not exist", name);
                throw new Exception(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newGroup;
    }

    public boolean deleteGroup(String name) {
        boolean removed = groups.removeIf(group -> group.getName().equalsIgnoreCase(name));

        if (removed) {
            try {
                mapper.writeValue(new File(FILE_PATH), groups); // Update the JSON file
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return removed;
    }
}
