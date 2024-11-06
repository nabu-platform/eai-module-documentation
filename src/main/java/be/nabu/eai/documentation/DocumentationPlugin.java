/*
* Copyright (C) 2021 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.eai.documentation;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.api.DeveloperPlugin;
import be.nabu.eai.module.web.application.WebApplication;
import be.nabu.eai.module.web.application.resource.WebBrowser;
import be.nabu.libs.artifacts.api.Artifact;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class DocumentationPlugin implements DeveloperPlugin {

	@Override
	public void initialize(MainController controller) {
		MenuItem item = new MenuItem("Documentation");
		// F1 in blox is used to enable/disable steps...
//		item.setAccelerator(new KeyCodeCombination(KeyCode.F1));
		controller.getMnuHelp().getItems().add(item);
		item.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Artifact resolve = controller.getRepository().resolve("nabu.documentation.wiki.artifacts.application");
				if (resolve instanceof WebApplication) {
					try {
						Tab newTab = controller.newTab("Documentation");
						WebBrowser webBrowser = new WebBrowser((WebApplication) resolve);
						String scheme = controller.getServer().getRepositoryRoot().getScheme();
						if (scheme.equals("remote") || scheme.equals("remotes")) {
							String harcodedUrl = scheme.replace("remote", "http") + "://" + controller.getServer().getHost() + ":" + controller.getServer().getPort() + "/wiki"; 
							webBrowser.setHardcodedUrl(harcodedUrl);
						}
						System.out.println("Connecting to wiki: " + webBrowser.getExternalUrl());
						AnchorPane asPane = webBrowser.asPane();
						Node lookup = asPane.lookup(".buttons");
						if (lookup != null) {
							lookup.setManaged(false);
							lookup.setVisible(false);
						}
						newTab.setContent(asPane);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

}
