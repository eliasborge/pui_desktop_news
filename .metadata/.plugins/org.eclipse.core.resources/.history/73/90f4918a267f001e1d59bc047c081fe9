/**
 * 
 */
package application;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.BufferedWriter;
import java.io.File;

// for writing in a file
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.function.Predicate;

// for reading from a file
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;


import application.news.Article;
import application.news.Categories;
import application.news.User;
import application.utils.JsonArticle;
import application.utils.exceptions.ErrorMalFormedArticle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;
import serverConection.ConnectionManager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import application.news.Article;
import application.news.User;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * @author ÁngelLucas
 *
 */
public class NewsReaderController {

    private User currentUser;
    
    @FXML
    private AnchorPane imagePane; // The container for the ImageView
    @FXML
    private ImageView articleImageView; // The ImageView to display the article image
    @FXML
    private TextField searchField;
    @FXML
    private ListView<Article> articlesListView;
    @FXML
    private Text articleTitle;
    @FXML
    private Text articleAbstract;
    @FXML
    private ImageView articleImage;
    
    @FXML
    private Button newArticleButton;
    
	@FXML
	private Button articleNewButton;
	 
    @FXML
    private Button articleEditButton;
    
    @FXML
    private Button articleDeleteButton;
    
    @FXML
    private Button LoadArticleFromFile;
    
    
    
    @FXML
    private ListView<Article> headlinesListView; // Assuming your Article class has a category field

    @FXML
    private MenuItem filterAll;
    @FXML
    private MenuItem filterSport;
    @FXML
    private MenuItem filterPolitics;
    @FXML
    private MenuItem filterInternational;
    @FXML
    private MenuItem filterNational;
    @FXML
    private MenuItem filterTechnology;

    // Assuming you have a method to get all articles
    private List<Article> allArticles;
    
    private User loggedInUser; // This should be set when a user logs in
    private Article selectedArticle; // This should be set when an article is selected

    
	private NewsReaderModel newsReaderModel = new NewsReaderModel();
	private User usr;

	//TODO add attributes and methods as needed

	public NewsReaderController() {
		//TODO
		//Uncomment next sentence to use data from server instead dummy data
		newsReaderModel.setDummyData(false);
		//Get text Label
		
	}
	
	// Method to handle the selection of an article headline
    public void onArticleSelected(Article selectedArticle) {
        // Assuming Article has a method to get the image URL or Image object
        Image articleImage = selectedArticle.getImage();
        articleImageView.setImage(articleImage);
    }
    
    // This method is called by the LoginController after a successful login
    public void setUser(User user) {
        this.currentUser = user;
        updateUIForUser();
    }

    private void updateUIForUser() {
        if (currentUser != null) {
            // Update the UI based on the user's permissions
        	articleEditButton.setDisable(!currentUser.isAdmin());
        	articleDeleteButton.setDisable(!currentUser.isAdmin());
        	articleNewButton.setDisable(!currentUser.isAdmin());
            // ... other UI updates based on the user
        }
    }

    // Call this method when an article is selected from the list
    public void setSelectedArticle(Article article) {
        this.selectedArticle = article;
        updateArticleButtons();
    }

    // This method updates the state of the edit and delete buttons
    private void updateArticleButtons() {
        boolean isLoggedIn = loggedInUser != null;
        boolean isUsersArticle = isLoggedIn && selectedArticle != null && selectedArticle.getUserID() == loggedInUser.getId();

        articleEditButton.setDisable(!isUsersArticle);
        articleDeleteButton.setDisable(!isUsersArticle);
    }
	
    @FXML
    private void filterHeadlines(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        String category = source.getText(); // Get the text of the MenuItem clicked

        List<Article> filteredArticles;
        if ("All".equals(category)) {
            filteredArticles = new ArrayList<>(allArticles);
        } else {
            filteredArticles = allArticles.stream()
                    .filter(article -> category.equals(article.getCategory()))
                    .collect(Collectors.toList());
        }

        headlinesListView.getItems().setAll(filteredArticles);
    }

    private void updateHeadlinesListView(String category) {
        List<Article> filteredArticles;

        if ("ALL".equals(category)) {
            filteredArticles = new ArrayList<>(allArticles);
        } else {
            filteredArticles = allArticles.stream()
                    .filter(article -> category.equals(article.getCategory()))
                    .collect(Collectors.toList());
        }

        headlinesListView.getItems().setAll(filteredArticles);
    }

    
	@FXML
    void createNewArticle(ActionEvent event) {
        if (usr != null) {
            // User is logged in, open article creation dialog
            openArticleCreationDialog();
        } else {
            // User is not logged in, save article to file
            saveArticleToFile();
        }
    }
	
	 private void openArticleCreationDialog() {
	        // TODO: Implement the logic to open a dialog for article creation
	        // This could involve opening a new window with text fields for the article's title, content, etc.
	    }

	    public void saveArticleToFile(Article article, String fileName) {
	        JsonObject jsonArticle = JsonArticle.articleToJson(article);
	        try (FileWriter file = new FileWriter(fileName)) {
	            file.write(jsonArticle.toString());
	            file.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
		
// load and read articles from files -----------

	  public Article loadArticleFromFile(String fileName) {
	        JsonObject jsonArticle = JsonArticle.readFile(fileName);
	        try {
	            return JsonArticle.jsonToArticle(jsonArticle);
	        } catch (ErrorMalFormedArticle e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	 private void readArticleFromFile(File file) {
		    try {
		        String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
		        // Assuming you have a method to parse the JSON content to an Article object
		        Article article = new Gson().fromJson(content, Article.class);
		        displayArticle(article);
		    } catch (IOException ex) {
		        ex.printStackTrace();
		        // Show error message to the user, e.g., using an Alert
		        showAlert("Error", "Could not read file: " + file.getName());
		    }
		}
	 
	private void getData() {
		//TODO retrieve data and update UI
		//The method newsReaderModel.retrieveData() can be used to retrieve data  
		newsReaderModel.retrieveData();
		this.allArticles=newsReaderModel.getArticles();
	}



	void setConnectionManager (ConnectionManager connection){
		this.newsReaderModel.setDummyData(false); //System is connected so dummy data are not needed
		this.newsReaderModel.setConnectionManager(connection);
		this.getData();
	}
	

	
    @FXML
    private ListView<Article> headlinesListView; // Make sure the ListView is of type Article if you're using custom objects

    private NewsReaderModel newsReaderModel = new NewsReaderModel();

	@FXML
	void initialize() {
		
		// fetch the articles from the server
		newsReaderModel.retrieveData();
		
		// Initialize your ListView with article headlines
        headlinesListView.setItems(newsReaderModel.getArticles());
        
        // each Article object should be displayed as a ListCell
 
	
        // Add a listener to your ListView selection model
        SelectionModel<Article> selectionModel = headlinesListView.getSelectionModel();
        selectionModel.selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            setSelectedArticle(newSelection);
        });

        // Initially disable the buttons until an article is selected and user is logged in
        articleEditButton.setDisable(true);
        articleDeleteButton.setDisable(true);
    }
		
	
	/**
	 * @param usr the usr to set
	 */
	void setUsr(User usr) {
		
		this.usr = usr;
		//Reload articles
		this.getData();
		//TODO Update UI
	}
	
	/**
	 * @return the usr
	 */
	User getUsr() {
		return usr;
	}

	// called when a user logs in
    void setUsr(User usr) {
        this.usr = usr;
        updateLoginStatus();
        // ... other code to handle setting the user
    }

    /**
     * Updates the UI based on the user's login status.
     */
    private void updateLoginStatus() {
        boolean isLoggedIn = (usr != null);
        articleEditButton.setDisable(!isLoggedIn);
        articleDeleteButton.setDisable(!isLoggedIn);
        
        // ... additional UI updates based on login status
    }
