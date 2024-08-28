/**
 * This is a stateless view showing one plug.
 */
function PlugRow(props) {
	var btnClass = (props.plug.state == "on") ? "btn-info" : "btn-outline-secondary";

	return (

		<button
			onClick={() => props.selectPlug(props.plug)}
			className={`btn ${btnClass} d-flex m-1 w-100 align-items-center`}
		>
			<i className="fa-solid fa-plug-circle-bolt"></i>
			<span className="flex-fill text-center">{props.plug.name} is {props.plug.state}</span>
		</button>
	);
}

/**
 * This is a stateless view listing all plugs.
 */
window.PlugsView = function (props) {
	if (props.plugs.length == 0)
		return (<div>There are no plugs.</div>);

	var rows = props.plugs.map(function (plug) {
		return (
			<PlugRow key={plug.name}
				plug={plug}
				plugSelected={props.plugSelected}
				selectPlug={props.selectPlug} />);
	});

	return (
		<div className="d-flex justify-content-between">
			{rows}
		</div>
	);
}
