/**
 * This is a stateless view showing one Group.
 */
function GroupRow(props) {
	var checkIfGroupMemberIsActive = props.group.members.some(member => {
		return member.state == "on"
	})
	var btnClass = (checkIfGroupMemberIsActive) ? "btn-info" : "btn-outline-secondary";

	return (

		<button
			onClick={() => props.selectGroup(props.group)}
			className={`btn ${btnClass} d-flex m-1 w-100 align-items-center`}
		>
			<i className="fa-solid fa-house-circle-check"></i>
			<span className="flex-fill text-center">{props.group.name}</span>
		</button>

	);
}

/**
 * This is a stateless view listing all plugs.
 */
window.GroupsView = function (props) {
	if (props.groups.length == 0)
		return (<div>There are no groups. <GroupCreator /></div>);

	var rows = props.groups.map(function (group) {
		return (
			<GroupRow key={group.name}
				group={group}
				members={group.members}
				groupSelected={props.groupSelected}
				selectGroup={props.selectGroup}>
			</GroupRow>);
	});

	return (
		<div>
			<GroupCreator />
				<h4>
					<i className="fa-solid fa-list-check"></i>
					<span> List of Groups </span>
				</h4>
				<div className="d-flex justify-content-between">
					{rows}
				</div>
		</div>
	);
}
