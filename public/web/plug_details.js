function plugAction(name, action) {
	var url = "../api/plugs/" + name + "?action=" + action;
	console.info("PlugDetails: request " + url);
	fetch(url);
}

/**
 * This is a stateless view showing details of one plug.
 */
window.PlugDetails = function (props) {
	var plug = props.plugSelected;
	if (plug == null)
		return (<div>Please select a plug.</div>);

	return (


		<div className="container-fluid">
			<h4>Selected Plug : <b>{plug.name}</b></h4>
			<div className="container-fluid">
				<h5>Plug Details</h5>
				<div className="container-fluid">
					State : <b>{plug.state}</b> <br />
					Power : <b>{plug.power}</b>
				</div>
			</div>

			<div className="container-fluid m-2">
				<h5>Actions</h5>
				<div className="d-flex justify-content-between">

					<button
						onClick={() => plugAction(plug.name, "on")}
						className={`btn btn-outline-secondary d-flex m-1 w-100 align-items-center`}
					>
						<i className="fa-solid fa-plug-circle-check"></i>
						<span className="flex-fill text-center">Switch On</span>
					</button>

					<button
						onClick={() => plugAction(plug.name, "off")}
						className={`btn btn-outline-secondary d-flex m-1 w-100 align-items-center`}
					>
						<i className="fa-solid fa-plug-circle-xmark"></i>
						<span className="flex-fill text-center">Switch Off</span>
					</button>

					<button
						onClick={() => plugAction(plug.name, "toggle")}
						className={`btn btn-outline-secondary d-flex m-1 w-100 align-items-center`}
					>
						<i className="fa-solid fa-toggle-on"></i>
						<span className="flex-fill text-center">Toggle</span>
					</button>
				</div>
			</div>
		</div>);
}
