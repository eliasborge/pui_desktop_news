
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



			@FXML
			public void initialize(){

				//TODO MAKE THIS ACTUALLY RENDER AN ARTICLE BY ID

				this.article = new Article();
				NewsReaderModel model = new NewsReaderModel();
				model.setDummyData(true);
				model.retrieveData();
				ObservableList<Article> articles = model.getArticles();
				this.article = articles.get(0);

				setArticle(this.article);


			}

			public void goBack(){
				try {
					Stage stage = (Stage) title.getScene().getWindow();
					FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.READER.getFxmlFile()));
					Pane root = loader.load();
					NewsReaderController controller = loader.getController();


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
=======
/**
 * 
 */
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



			@FXML
			public void initialize(){

				//TODO MAKE THIS ACTUALLY RENDER AN ARTICLE BY ID

				this.article = new Article();
				NewsReaderModel model = new NewsReaderModel();
				model.setDummyData(true);
				model.retrieveData();
				ObservableList<Article> articles = model.getArticles();
				this.article = articles.get(0);

				setArticle(this.article);


			}

			public void goBack(){
				try {
					Stage stage = (Stage) title.getScene().getWindow();
					FXMLLoader loader = new FXMLLoader(getClass().getResource(AppScenes.READER.getFxmlFile()));
					Pane root = loader.load();
					NewsReaderController controller = loader.getController();


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

