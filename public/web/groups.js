/**
 * The Plugs controller holds the state of plugs.
 * It refreshes the state every 1 second (and notify the parent controller).
 * It creates its view in render().
 */
class Groups extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            groups: []
        };
    }

    updateGroups(groups) {
        if (!Array.isArray(groups)) {
            console.debug("Groups: cannot get Groups " + JSON.stringify(Groups));
            return;
        }

        console.debug("Groups: " + JSON.stringify(groups));
        this.setState({ groups: groups });

        if (this.props.groupSelected == null)
            return;

        // notify parent
        for (var i = 0; i < groups.length; ++i) {
            if (this.props.groupSelected.name == groups[i].name) {
                this.props.updateGroupSelected(groups[i]);
                return;
            }
        }
    }

    getGroups() {
        fetch("../api/groups")
            .then(rsp => rsp.json())
            // .then(data => console.log(data))
            .then(data => this.updateGroups(data))
            .catch(err => console.debug("Groups: error loading the groups" + JSON.stringify(err)));
    }

    componentDidMount() {
        this.getGroups();
        window.setInterval(() => this.getGroups(), 1000);
    }

    render() {
        return (<GroupsView
            groups={this.state.groups}
            groupSelected={this.props.groupSelected}
            selectGroup={this.props.updateGroupSelected} />);
    }
}

window.Groups = Groups;
