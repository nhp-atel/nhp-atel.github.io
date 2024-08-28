/**
 * The App class is a controller holding the global state.
 * It creates all children controllers in render().
 */
class IoTHubApp extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			plugSelected: null,
			groupSelected: null
		};
	}

	updatePlugSelected(plug) {
		this.setState({ plugSelected: plug });
	}

	updateGroupSelected(group) {
		this.setState({ groupSelected: group });
	}


	render() {
		return (
			<div>
				<nav className="navbar navbar-expand-lg navbar-dark bg-dark">
					<div className="container-fluid">
						<a className="navbar-brand" href="#groups">Welcome to IoT Hub from ECE448/ECE528@IIT!</a>
					</div>
				</nav>

				<div className="container-fluid">
					<div className="row">
						<nav className="col-md-2 d-none d-md-block bg-light sidebar">
							<div className="position-sticky pt-3">
								<ul className="nav flex-column">
									<li className="nav-item">
										<a className="nav-link active" href="#plugs">
											Plugs
										</a>
									</li>
									<li className="nav-item">
										<a className="nav-link" href="#groups">
											Groups
										</a>
									</li>
								</ul>
							</div>
						</nav>

						<main className="col-md-9 ms-sm-auto col-lg-10 px-md-4">
							<section id="plugs" className="content-section">
								<div className="card m-2">
									<div className="card-body">
										<h2 className="card-title">
											Plugs
										</h2>
										<div className="card-text">
											<Plugs
												updatePlugSelected={plug => this.updatePlugSelected(plug)}
												plugSelected={this.state.plugSelected} />
											<PlugDetails
												plugSelected={this.state.plugSelected} />
										</div>
									</div>
								</div>

							</section>
							<section id="groups" className="content-section">
								<div className="card m-2">
									<div className="card-body">
										<h2 className="card-title">
											Groups
										</h2>
										<div className="card-text">
											<Groups
												updateGroupSelected={group => this.updateGroupSelected(group)}
												groupSelected={this.state.groupSelected} />

											<GroupDetails
												groupSelected={this.state.groupSelected} />
										</div>
									</div>
								</div>
							</section>
						</main>
					</div>
				</div>

			</div>
		);
	}
}

window.IoTHubApp = IoTHubApp;
