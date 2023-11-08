/**
 * 
 */
package application;


import application.news.Article;
import application.news.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;

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
				//TODO REROUTE BACK TO THE MAIN PAGE.


			}

			public void showAbstractBody(){
				//TODO DUNNO YET
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
