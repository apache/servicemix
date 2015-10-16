package org.apache.servicemix.examples.drools.camel.blueprint.osgi;

import java.util.List;

import org.kie.api.task.UserGroupCallback;

public class EmptyUserGroupCallback implements UserGroupCallback {
    /**
     * 
     */
    @Override
    public boolean existsUser(String userId) {
        return true;
    }

    /**
     * 
     */
    @Override
    public boolean existsGroup(String groupId) {
        return true;
    }

    /**
     * 
     */
    @Override
    public List<String> getGroupsForUser(String userId, List<String> groupIds,
            List<String> allExistingGroupIds) {
        return null;
    }

}
