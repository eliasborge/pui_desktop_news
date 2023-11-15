
package application;


import application.news.Article;
import application.news.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import serverConection.ConnectionManager;

import java.io.IOException;
import java.util.WeakHashMap;

/**
 * @author ÁngelLucas
 *
 */
public class ArticleDetailsController {
	//TODO add attributes and methods as needed
		/**
		 * Registered user
		 */
	    private User usr;
			private ConnectionManager connection;
	    
	    /**
	     * Article to be shown
	     */
	    private Article article;

			@FXML
			private WebView articleField;
			@FXML
			private Label title;
	  	@FXML
			private Label subtitle;
	  	@FXML
			private Label category;
			@FXML
			private ImageView image;
			@FXML
			private Button buttonBack;
			@FXML
			private Button buttonAbstractBody;
			@FXML
			private Label userLabel;

			private boolean body = true;




			public void goBack(){
				try {
					Stage stage = (Stage) title.getScene().getWindow();
					FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.READER.getFxmlFile()));
					Pane root = loader.load();
					NewsReaderController controller = loader.getController();
					controller.setConnectionManager(connection);
					controller.setUser(usr);


					Scene scene = new Scene(root);
					stage.setScene(scene);

				} catch (IOException e) {
					e.printStackTrace();
					// Handle the exception if there's an issue loading the READER page.
				}


			}

			public void showAbstractBody(){
				if(body){
					articleField.getEngine().loadContent(article.getAbstractText());
					body = false;
				}
				else{
					articleField.getEngine().loadContent(article.getBodyText());
					body=true;
				}

			}

		/**
		 * @param usr the usr to set
		 */
		void setUsr(User usr) {
			this.usr = usr;
			if (usr == null) {
				return; //Not logged user
			}
			this.userLabel.setText("News online for: " +usr.getLogin());
		}

		void setConnectionManager(ConnectionManager connection){
			this.connection = connection;
		}
		/**
		 * @param article the article to set
		 */
		void setArticle(Article article) {
			this.article = article;
			this.title.setText("Title: " + article.getTitle());
			this.subtitle.setText("Subtitle: " + article.getSubtitle());
			this.category.setText("Category: " + article.getCategory());
			this.image.setImage(article.getImageData());
			this.articleField.getEngine().loadContent(article.getBodyText());
		}
}
