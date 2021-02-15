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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;

public class DocumentationPlugin implements DeveloperPlugin {

	@Override
	public void initialize(MainController controller) {
		MenuItem item = new MenuItem("Wiki");
		item.setAccelerator(new KeyCodeCombination(KeyCode.F1));
		controller.getMnuHelp().getItems().add(item);
		item.addEventHandler(ActionEvent.ANY, new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Artifact resolve = controller.getRepository().resolve("nabu.documentation.wiki.artifacts.application");
				if (resolve instanceof WebApplication) {
					Tab newTab = controller.newTab("Wiki");
					AnchorPane asPane = new WebBrowser((WebApplication) resolve).asPane();
					Node lookup = asPane.lookup(".buttons");
					if (lookup != null) {
						lookup.setManaged(false);
						lookup.setVisible(false);
					}
					newTab.setContent(asPane);
				}
			}
		});
	}

}
