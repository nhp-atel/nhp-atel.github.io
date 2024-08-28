function groupAction(members, action) {
    for (var member of members) {
        // console.log(`member is : ${member} , Member Name: ${member.name} and state is ${action}`)
        var url = "../api/plugs/" + member.name + "/?action=" + action;
        fetch(url);
    }
}

/**
 * This is a stateless view showing details of one plug.
 */
window.GroupDetails = function (props) {
    var group = props.groupSelected;
    if (group == null)
        return (<div>Please select a Group.</div>);


    var deleteGroup = groupName => {
        console.info("RESTful: delete group " + groupName);

        var delReq = {
            method: "DELETE"
        };
        fetch("api/groups/" + groupName, delReq)
            .then(rsp => {
                this.getGroups();
            })
            .catch(err => console.error("Members: deleteGroup", err));
    }



    var rows = group.members.map(function (member) {
        return (
            <PlugRow key={member.name}
                plug={member}
                plugSelected={member}
                selectPlug={() => {}}
            />
        )
    })
    return (
        <div className="container-fluid">
            <h4>Selected Group : <b>{group.name}</b></h4>
            <div className="container-fluid">
                <h5>Group Members</h5>
                <div className="d-flex justify-content-between">
                    {rows}
                </div>
            </div>
            <div className="container-fluid m-2">                    <h5>Actions</h5>
                <div className="d-flex justify-content-between">

                    <button
                        onClick={() => groupAction(group.members, "on")}
                        className={`btn btn-outline-secondary d-flex m-1 w-100 align-items-center`}
                    >
                        <i className="fa-solid fa-plug-circle-check"></i>
                        <span className="flex-fill text-center">Switch On</span>
                    </button>

                    <button
                        onClick={() => groupAction(group.members, "off")}
                        className={`btn btn-outline-secondary d-flex m-1 w-100 align-items-center`}
                    >
                        <i className="fa-solid fa-plug-circle-xmark"></i>
                        <span className="flex-fill text-center">Switch Off</span>
                    </button>

                    <button
                        onClick={() => groupAction(group.members, "toggle")}
                        className={`btn btn-outline-secondary d-flex m-1 w-100 align-items-center`}
                    >
                        <i className="fa-solid fa-toggle-on"></i>
                        <span className="flex-fill text-center">Toggle</span>
                    </button>
                    <button
                        onClick={() => deleteGroup(group.name)}
                        className={`btn btn-danger d-flex m-1 w-100 align-items-center`}
                    >
                        <i className="fa-solid fa-trash"></i>
                        <span className="flex-fill text-center">Delete Group</span>
                    </button>

                </div>
            </div>
        </div>);
}
