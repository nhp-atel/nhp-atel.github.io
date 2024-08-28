

class GroupCreator extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      groupName: '',
      members: []
    };
  }

  handleGroupNameChange = (event) => {
    this.setState({ groupName: event.target.value });
  };

  toggleMemberSelection = (member) => {
    const { members } = this.state;
    if (members.includes(member)) {
      this.setState({ members: members.filter(m => m !== member) });
    } else {
      this.setState({ members: [...members, member] });
    }
  };

  createGroup = (groupName, groupMembers) => {
    console.info("RESTful: create group " + groupName
      + " " + JSON.stringify(groupMembers));

    var postReq = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(groupMembers)
    };
    fetch("api/groups/" + groupName, postReq)
      .then(rsp => console.log("goup created successfully"))
      .catch(err => console.error("Members: createGroup", err));
  }

  onMemberChange = (memberName, groupName) => {
    var groupMembers = new Set(this.state.members.get_group_members(groupName));
    if (groupMembers.has(memberName))
      groupMembers.delete(memberName);
    else
      groupMembers.add(memberName);

    this.createGroup(groupName, Array.from(groupMembers));
  }

  onDeleteGroup = groupName => {
    this.deleteGroup(groupName);
  }

  onInputNameChange = value => {
    console.debug("Members: onInputNameChange", value);
    this.setState({ inputName: value });
  }

  onInputMembersChange = value => {
    console.debug("Members: onInputMembersChange", value);
    this.setState({ inputMembers: value });
  }


  handleSubmit = (event) => {
    event.preventDefault();
    console.log('Group Name:', this.state.groupName);
    console.log('Selected Members:', this.state.members);
    // You can add your logic to handle the new group here
    this.createGroup(this.state.groupName, this.state.members)
    this.state.members = []
    this.state.groupName = ""
  };

  render() {
    const { groupName, members } = this.state;
    const availableMembers = ['a', 'b.100', 'cc', 'dddd'];

    return (
      <div className="">      
        <form onSubmit={this.handleSubmit} className="">
          <div className="form-group">
            <h4>
            <i className="bi bi-file-earmark-plus"></i>
              Create New Group
            </h4>
            <label htmlFor="groupName" className="form-label">Group Name:</label>
            <input
              type="text"
              className="form-control"
              id="groupName"
              value={groupName}
              onChange={this.handleGroupNameChange}
            />
          </div>

          <div className="form-group">
            <label htmlFor="members" className="form-label">Select Members:</label>
            <div className="d-flex justify-content-between">
              {availableMembers.map(member => (
                <button
                  key={member}
                  type="button"
                  onClick={() => this.toggleMemberSelection(member)}
                  className={`btn ${members.includes(member) ? "btn-info" : "btn-outline-secondary"} d-flex m-1 w-100 align-items-center`}
                >
                  <i className="bi bi-plug-fill"></i>
                  <span className="flex-fill text-center">{member}</span>
                </button>
              ))}
            </div>
          </div>


          <div className="d-flex justify-content-end">
            <button type="submit" className="btn btn-primary">Create Group</button>
          </div>
        </form>

      </div>

    );
  }
}

window.GroupCreator = GroupCreator;
