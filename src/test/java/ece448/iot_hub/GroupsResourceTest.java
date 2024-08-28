package ece448.iot_hub;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class GroupsResourceTest {

    private static final String broker = "tcp://127.0.0.1";
    private static final String clientId = "testClient";
    private static final String topicPrefix = "test";

    @Test
    public void testGetGroups() throws Exception {
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);
        
        // Assuming at least one group exists; this needs setup beforehand
        gm.setGroupMembers("testGroup", Arrays.asList("member1", "member2"));

        Object groups = gr.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testCreateGroup() throws Exception {
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);
        
        gr.createGroup("AdditionalGroup", Arrays.asList("member1", "member2"));

        // Verifying the group was created
        assertTrue(gm.getGroups().contains("AdditionalGroup"));
    }

    @Test
    public void testRemoveGroup() throws Exception {
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);
        
        // Preparing a group to remove
        String groupName = "fakeGroup";
        gm.setGroupMembers(groupName, Arrays.asList("member1"));
        gr.removeGroup(groupName);

        // Verifying the group was removed
        assertTrue(!gm.getGroups().contains(groupName));
    }

    @Test
    public void testGetGroupWithoutAction() throws Exception {
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);

        // Setting up a test group
        String groupName = "randomGroup";
        gm.setGroupMembers(groupName, Arrays.asList("member1", "member2"));

        Object groupDetails = gr.getGroup(groupName, null);
        assertNotNull(groupDetails);
    }

    @Test
    public void testGetNonExistingGroup() throws Exception{
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);

        String nonExistingGroup = "IITGroup";
        Object res = gr.getGroup(nonExistingGroup, null);

        assertNotNull("handle with care", res);
        
    }
    @Test
    public void testCreateGrupWithInvalidName() throws Exception{
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);

        String ing = "";
        try{
            gr.createGroup(ing, Arrays.asList("Candiate 1"));
            System.out.println("sorry did not work");
        } catch(Exception e){
            System.out.println("Nothing to look");
        }

    }
    @Test
    public void testCreateGroupWithInvalidMemberName() throws Exception{
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);

        String groupName = "NotSureName";
        List<String> fakeMembers = Arrays.asList("", " ", "wrongName", null);
        
        try{
            gr.createGroup(groupName, fakeMembers);
            System.out.println("The member name is not valid");
        }catch (Exception e){
            System.out.println("Nice try in fooling me");
        }
    }

    @Test
    public void testUpdateGroupMembers() throws Exception{
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);

        String groupName = "sameGroup";
        List<String> im = Arrays.asList("mem 1");
        gm.setGroupMembers(groupName, im);

        List<String> nm = Arrays.asList("mem 2", "mem 3");
        gr.createGroup(groupName, nm);

        List<String> rm = gm.getGroupMembers(groupName);
        assertTrue("group memebrs should be updated", rm.containsAll(nm));
        
    }
    @Test
    public void testRemoveNonexisteningPlug() throws Exception{
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);

        String non = "nonGroup";

        try{
            gr.removeGroup(non);
            assertFalse("Non existing plug should not be here ", gm.getGroups().contains(non));
        
        } catch (Exception e){
            System.out.println("Removing the non-existing plug");
        }
    }

    @Test
    public void testCreateGroupWithValidMemebers() throws Exception{
        MqttController c= new MqttController(broker, clientId, topicPrefix);
        GroupsModel gm = new GroupsModel();
        GroupsResource gr = new GroupsResource(gm, c);

        String groupName = "A";
        List<String> vm = Arrays.asList("Aa", "Bb");

        try{
            gr.createGroup(groupName,vm);
            assertTrue("Group should exist after creation", gm.getGroups().contains(groupName));
            assertTrue("Group should contain the correct memebers",gm.getGroupMembers(groupName).containsAll(vm));
        } catch(Exception e){
            System.out.println("There should not be any exception");
        }
    }
   
}
