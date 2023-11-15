/**
 * 
 */
package application;

import java.io.FileWriter;
import java.io.IOException;

import javax.json.JsonObject;

import application.news.Article;
import application.news.Categories;
import application.news.User;
import application.utils.JsonArticle;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import serverConection.ConnectionManager;
import serverConection.exceptions.ServerCommunicationError;

/**
 * @author Antonio Carpintero Castilla & Angel Lucas
 */
public class ArticleEditController {
	/**
	 * Connection used to send article to server after editing process
	 */
    private ConnectionManager connection;

    /**
     * Instance that represent an article when it is editing
     */
	private ArticleEditModel editingArticle;
	/**
	 * User whose is editing the article
	 */
	private User usr;

	@FXML
	private TextField titleField;
	@FXML
	private TextField subtitleField;
	@FXML
	private ChoiceBox<Categories> categoryField;
	@FXML
	private ImageView imageField;
	@FXML
	private WebView articleField;
	@FXML
	private Button buttonBack;
	@FXML
	private Button buttonAbstractBody;
	@FXML
	private Label userLabel;

	@FXML
	private void initialize() {
		titleField.textProperty().bind(editingArticle.titleProperty());
		subtitleField.textProperty().bind(editingArticle.subtitleProperty());
		categoryField.setValue(editingArticle.getCategory());
		articleField.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				String content = editingArticle.getBodyText();
				articleField.getEngine().executeScript("document.body.innerHTML = '" + content + "';");
			}
		});
	}

	@FXML
	void onImageClicked(MouseEvent event) {
		if (event.getClickCount() >= 2) {
			Scene parentScene = ((Node) event.getSource()).getScene();
			FXMLLoader loader = null;
			try {
				loader = new FXMLLoader(getClass().getResource(AppScenes.IMAGE_PICKER.getFxmlFile()));
				Pane root = loader.load();
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				Window parentStage = parentScene.getWindow();
				Stage stage = new Stage();
				stage.initOwner(parentStage);
				stage.setScene(scene);
				stage.initStyle(StageStyle.UNDECORATED);
				stage.initModality(Modality.WINDOW_MODAL);
				stage.showAndWait();
				ImagePickerController controller = loader.<ImagePickerController>getController();
				Image image = controller.getImage();
				if (image != null) {
					editingArticle.setImage(image);
					//TODO Update image on UI
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


	
	/**
	 * Send and article to server,
	 * Title and category must be defined and category must be different to ALL
	 * @return true if only if article has been correctly send
	 */
	@FXML
	private boolean send() {
		String titleText = titleField.getText();
		Categories category = categoryField.getValue();
		if (titleText == null || category == null || 
				titleText.equals("") || category == Categories.ALL) {
			Alert alert = new Alert(AlertType.ERROR, "Imposible send the article!! Title and categoy are mandatory", ButtonType.OK);
			alert.showAndWait();
			return false;
		}

		try {
			editingArticle.setCategory(categoryField.getValue());
			editingArticle.commit();
			connection.saveArticle(editingArticle.getArticleOriginal());
		} catch (ServerCommunicationError e) {
			Alert alert = new Alert(AlertType.ERROR, "Error sending the article to the server", ButtonType.OK);
			alert.showAndWait();
			return false;
		}
		return true;
	}
	
	/**
	 * This method is used to set the connection manager which is
	 * needed to save a news 
	 * @param connection connection manager
	 */
	void setConnectionMannager(ConnectionManager connection) {
		this.connection = connection;
		//TODO enable send and back button
	}

	/**
	 * 
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
		this.usr = usr;
		if (usr == null) {
			return; //Not logged user
		}
		this.userLabel.setText("News online for: " +usr.getLogin());
	}

	/**
	 * Get the article without changes since last commit 
	 * @return article without changes since last commit
	 */
	Article getArticle() {
		Article result = null;
		if (this.editingArticle != null) {
			result = this.editingArticle.getArticleOriginal();
		}
		return result;
	}

	/**
	 * PRE: User must be set
	 * 
	 * @param article
	 *            the article to set
	 */
	void setArticle(Article article) {
		this.editingArticle = (article != null) ? new ArticleEditModel(article) : new ArticleEditModel(usr);
		this.titleField.setText(article.getTitle());
		this.subtitleField.setText(article.getSubtitle());
		this.categoryField.setValue(Categories.valueOf(article.getCategory().toUpperCase()));
		this.imageField.setImage(article.getImageData());
		this.articleField.getEngine().loadContent(article.getBodyText());
	}
	
	/**
	 * Save an article to a file in a json format
	 * Article must have a title
	 */
	private void write() {
		this.editingArticle.commit();
		//Removes special characters not allowed for filenames
		String name = this.getArticle().getTitle().replaceAll("\\||/|\\\\|:|\\?","");
		String fileName ="saveNews//"+name+".news";
		JsonObject data = JsonArticle.articleToJson(this.getArticle());
		  try (FileWriter file = new FileWriter(fileName)) {
	            file.write(data.toString());
	            file.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	public void goBack() throws IOException {
		try {
			Stage stage = (Stage) titleField.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.READER.getFxmlFile()));
			Pane root = loader.load();

			Scene scene = new Scene(root);
			stage.setScene(scene);

		} catch (IOException e) {
			e.printStackTrace();
			// Handle the exception if there's an issue loading the READER page.
		}
	}
	public void save(ActionEvent actionEvent) {
		write();
	}

}
