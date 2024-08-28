package ece448.iot_hub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class GroupsModel {

	private GroupService groupService;
	
	public GroupsModel(){
		this.groupService = new GroupService();
	}

	synchronized public List<String> getGroups() {
		HashSet<Group> groups = groupService.getAllGroups();
		List<String> groupNames = new ArrayList<String>();
		groups.forEach(g -> groupNames.add(g.getName()) );
		return groupNames;
	}

	synchronized public List<String> getGroupMembers(String group) {
		Optional<Group> groupDetails = groupService.getGroup(group);
		List<String> members = new ArrayList<String>();
		if(groupDetails.isPresent()){
			groupDetails.get().getMembers().forEach(m -> members.add(m));;
		}
		return members;
	}

	synchronized public void setGroupMembers(String group, List<String> members) throws Exception {

		if(groupService.getGroup(group).isPresent()){
			groupService.updateGroupMembers(group, new HashSet<>(members));
		}
		else{
			Group groupDetails = new Group(group, new HashSet<>(members));		
			groupService.saveGroup(groupDetails);	
		}
	}

	synchronized public void removeGroup(String group) {
		groupService.deleteGroup(group);
	}
}
