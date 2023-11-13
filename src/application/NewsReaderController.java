package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import application.news.Article;
import application.news.User;
import javafx.collections.ObservableList;
import serverConection.ConnectionManager;
import application.news.Article; 
import javafx.scene.web.WebView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class NewsReaderController {

    // FXML injected UI components
    @FXML private AnchorPane imagePane;
    @FXML private ImageView articleImageView;
    @FXML private TextField searchField;
    @FXML private ListView<Article> articlesListView;
    @FXML private Text articleTitle;
    @FXML private Text articleAbstract;
    @FXML private Button newArticleButton;
    @FXML private Button articleEditButton;
    @FXML private Button articleDeleteButton;
    @FXML private ListView<Article> headlinesListView;
    @FXML private ListView<String> headlinesListView;
    @FXML private Label NewsforID;
    @FXML private WebView webView;
    @FXML private Button readMoreButton;
    

    // Model and user information
    private NewsReaderModel newsReaderModel = new NewsReaderModel();
    private User usr;
    private ObservableList<Article> allArticles;
    private Article selectedArticle;
    private Article article; 
    private ConnectionManager connectionManager;
    private ObservableList<Article> allArticles = FXCollections.observableArrayList();

    // Dependency injection method, so that NewsReader can fetch UserID from ConnectionManager class
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    

    
	// Method to update the image in the ImageView, this method has to be called when a new article is selected
    public void updateArticleImage(Article article) {
        Image image = article.getImageData();
        if (image != null) {
            articleImageView.setImage(image);
        }
    }
    
	 // Method to display the abstract of an article in the WebView, this method has to be called when a new article is selected
	    public void displayArticleAbstract(Article article) {
	        String abstractText = article.getAbstractText();
	        if (abstractText != null && !abstractText.isEmpty()) {
	            webView.getEngine().loadContent(abstractText, "text/html");
	        } else {
	            webView.getEngine().loadContent("<p>No abstract available.</p>", "text/html");
	        }
	    }
	
    
    // Initialize the controller and fetch articles from the server
    @FXML
    void initialize() {
        newsReaderModel.retrieveData();
        allArticles = newsReaderModel.getArticles();
        System.out.println(allArticles);
        articlesListView = new ListView<>();
        articlesListView.setItems(allArticles);
        articlesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            setSelectedArticle(newSelection);
        });
        updateArticleButtons(); // Edit & Delete only possible if article belongs to logged user
        getData();
        updateArticleImage(Article article);
        // By default, show all categories
        updateHeadlinesListView("ALL")
        
        // Label will display the current User's ID
        if (connectionManager != null) {
            String userID = connectionManager.getIdUser();
            NewsforID.setText("News Online for " + userID);
        }
        
       
    }

    // Set the current user and update the UI accordingly
    public void setUser(User user) {
        this.usr = user;
        updateLoginStatus();
        getData();
    }

    // Fetch data from the model and update the list view for category
    private void getData() {
        newsReaderModel.retrieveData();
        allArticles = newsReaderModel.getArticles();
        updateHeadlinesListView("ALL");
    }

    // Update the UI when an article is selected
    private void setSelectedArticle(Article article) {
        this.selectedArticle = article;
        if (article != null) {
            articleTitle.setText(article.getTitle());
            articleAbstract.setText(article.getAbstractText());
            articleImageView.setImage(article.getImageData());
        }
        updateArticleButtons();
    }

    // Enable or disable article edit and delete buttons based on the selected article and user
    private void updateArticleButtons() {
        boolean isLoggedIn = usr != null;
        boolean isUsersArticle = isLoggedIn && selectedArticle != null && selectedArticle.getIdUser() == usr.getIdUser();
        articleEditButton.setDisable(!isUsersArticle);
        articleDeleteButton.setDisable(!isUsersArticle);
        // TODO: disable NewButton, if no user is logged in
        articleNewButton.setDisable(!isLoggedIn);
    }


    
    // Filter headlines based on the selected category
//    @FXML
//    private void filterHeadlines(ActionEvent event) {
//        MenuItem source = (MenuItem) event.getSource();
//        updateHeadlinesListView(source.getText());
//    }
    
    // each Category button has its own handler method
//    @FXML
//    private void handleECONOMY(ActionEvent event) {
//        updateHeadlinesListView("ECONOMY");
//    }
//
//    @FXML
//    private void handleINTERNATIONAL(ActionEvent event) {
//        updateHeadlinesListView("INTERNATIONAL");
//    }
//    
//    @FXML
//    private void handleNATIONAL(ActionEvent event) {
//        updateHeadlinesListView("NATIONAL");
//    }
//
//    @FXML
//    private void handleSPORTS(ActionEvent event) {
//        updateHeadlinesListView("SPORTS");
//    }
//    
//    @FXML
//    private void handleTECHNOLOGY(ActionEvent event) {
//        updateHeadlinesListView("TECHNOLOGY");
//    }

    // takes one item from the possible categories and checks if the currently selected article belongs to the selected category, from other group
//    @FXML
//    public void handleCategorySelection(ActionEvent event) {
//    	MenuItem selectedCategory = (MenuItem) event.getTarget();
//    	categoryMenu.setText(categoryText);
//    }
    
    //  This method will be triggered when a category is selected.
    @FXML
    private void handleCategorySelection(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        String selectedCategory = source.getText();
        updateHeadlinesListView(selectedCategory);
    }

  
    
    // Update the headlines list view with filtered articles based on the category
//    private void updateHeadlinesListView(String category) {
//        ObservableList<Article> filteredArticles = "All".equalsIgnoreCase(category) ? allArticles : allArticles.filtered(article -> category.equalsIgnoreCase(article.getCategory()));
//        articlesListView.setItems(filteredArticles);
//    }
    
    private void updateHeadlinesListView(String category) {
        ObservableList<String> filteredHeadlines = FXCollections.observableArrayList();
        // If "ALL" is selected, add all article titles to filteredHeadlines.
        if ("ALL".equalsIgnoreCase(category)) {
            allArticles.forEach(article -> filteredHeadlines.add(article.getTitle()));
        } else {
            // Otherwise, filter allArticles based on the selected category and add their titles to filteredHeadlines.
        	allArticles.stream()
                .filter(article -> category.equalsIgnoreCase(article.getCategory()))
                .forEach(article -> filteredHeadlines.add(article.getTitle()));
        }

        // set the filteredHeadlines as the items of headlinesListView
        headlinesListView.setItems(filteredHeadlines);
    }

    // Set the connection manager for the model
    void setConnectionManager(ConnectionManager connection) {
        newsReaderModel.setConnectionManager(connection);
        getData();
    }

    // Update the UI based on the user's login status
    private void updateLoginStatus() {
        boolean isLoggedIn = (usr != null);
        newArticleButton.setDisable(!isLoggedIn);
        if (selectedArticle != null) {
            updateArticleButtons();
        }
    }

  
    // Actions, when buttons are clicked in the order "Load article from file", "Login", "New", "Edit", "Delete", "Exit" -------------------------------------

    // Load article from file TODO
    @FXML
    private void handleLoadArticle(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Handle the file (e.g., read and load the article)
            // You can read the file and process it as needed
            try {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                // Process the content as needed
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception
            }
        }
    }
    
    // Login Button
    @FXML
    private void handleLoginButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Stage stage = (Stage) LoginButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }
    }
    
    // This method will be triggered when the "New" button is clicked.
    @FXML
    private void handleNewArticle(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleEdit.fxml"));
            Stage stage = (Stage) articleNewButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }
    }
    
    // Handle the editing of an existing article 
    @FXML
    private void handleEditArticle(ActionEvent event) {
        try {
            // Assuming you have a method getSelectedArticle() that returns the currently selected article
            Article selectedArticle = getIDArticle();
            if (selectedArticle == null) {
            	JOptionPane.showMessageDialog(null, "No article was selected"); // If no article was selected
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleEdit.fxml"));
            Stage stage = (Stage) articleEditButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());
            
            // Get the controller and pass the article to the ArticleEditController
            ArticleEditController controller = loader.getController();
            controller.setArticle(selectedArticle); // Pass the selected article to the edit controller

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }

        
    // Handle the deletion of an article 
    @FXML
    void handleDeleteArticle(ActionEvent event) {
        if (selectedArticle != null) {
            newsReaderModel.deleteArticle(selectedArticle);
            articlesListView.getItems().remove(selectedArticle);
            // Clear the selection and update the UI
            articlesListView.getSelectionModel().clearSelection();
            selectedArticle = null;
            updateArticleButtons();
        }
    }

    // Exit Button
    @FXML
    private void handleExitButton(ActionEvent event) {
        // Get the stage of the current scene
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    
    // If "ReadMoreButton" is clicked, switch to the next Scene 
    private void handleReadMore(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ArticleDetails.fxml"));
            Stage stage = (Stage) readMoreButton.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(loader.load());
            
         // Get the controller and pass the article to the ArticleDetailsController
            ArticleDetailsController controller = loader.getController();
            controller.setArticle(selectedArticle);

            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }
    }
    // Save an article to a file
    public void saveArticleToFile(Article article, String fileName) {
        // The JsonArticle class is assumed to have a method to convert an article to JSON
        // JsonObject jsonArticle = JsonArticle.articleToJson(article);
        // TODO: still todo
    }
}