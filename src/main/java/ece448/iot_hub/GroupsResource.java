package ece448.iot_hub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupsResource {

	private final GroupsModel groups;
	private MqttController mqttController;

	public GroupsResource(GroupsModel groups, MqttController mqttController) {
		this.groups = groups;
		this.mqttController = mqttController;
	}

	@GetMapping("/api/groups")
	public Collection<Object> getGroups() throws Exception {
		ArrayList<Object> ret = new ArrayList<>();
		for (String group : groups.getGroups()) {
			ret.add(makeGroup(group));
		}
		logger.info("Groups: {}", ret);
		return ret;
	}

	@GetMapping("/api/groups/{groupName}")
	public Object getGroup(
			@PathVariable("groupName") String group,
			@RequestParam(value = "action", required = false) String action) {
		if (action == null) {
			Object ret = makeGroup(group);
			logger.info("Group {}: {}", group, ret);
			return ret;
		}

		// modify code below to control plugs by publishing messages to MQTT broker
		List<String> members = groups.getGroupMembers(group);
		Map<String, String> state = mqttController.getStates();
		// List<String> states = mqttController.getStates(group);
		for (String member : members) {
			try {
				mqttController.publishAction(member, action);
				logger.info("Action {} published to {}", member, action);
			} catch (Exception e) {
				logger.info("Failed to publish action {} to {} {}", action, member, e.getMessage());
			}
		}
		logger.info("Group {}: action {}, {}", group, action, members);
		return null;
	}

	@PostMapping("/api/groups/{groupName}")
	public void createGroup(
			@PathVariable("groupName") String group,
			@RequestBody List<String> members) throws Exception {
		groups.setGroupMembers(group, members);
		logger.info("Group {}: created {}", group, members);
	}

	@DeleteMapping("/api/groups/{groupName}")
	public void removeGroup(
			@PathVariable("groupName") String group) {
		groups.removeGroup(group);
		logger.info("Group {}: removed", group);
	}

	
	protected Object makeGroup(String group) {
		HashMap<String, Object> ret = new HashMap<>();
		ArrayList<Map<String, String>> membersInfo = new ArrayList<>();
	
		// Fetch the members of the specified group
		List<String> members = groups.getGroupMembers(group);
	
		// Iterate over each member to get its state and power
		for (String memberName : members) {
			Map<String, String> memberInfo = new HashMap<>();
			memberInfo.put("name", memberName);
			memberInfo.put("state", mqttController.getState(memberName)); // Retrieve the state of the member
			memberInfo.put("power", mqttController.getPower(memberName)); // Retrieve the power usage of the member
			
			membersInfo.add(memberInfo); // Add the member's info to the list
		}
	
		// Construct the return object with group name and members info
		ret.put("name", group);
		ret.put("members", membersInfo); // Use the list of members with their states and power
	
		return ret; // Return the constructed object
	}
	private static final Logger logger = LoggerFactory.getLogger(GroupsResource.class);

}
