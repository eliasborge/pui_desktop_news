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
    @FXML private MenuItem filterAll;
    @FXML private MenuItem filterSport;
    @FXML private MenuItem filterPolitics;
    @FXML private MenuItem filterInternational;
    @FXML private MenuItem filterNational;
    @FXML private MenuItem filterTechnology;

    // Model and user information
    private NewsReaderModel newsReaderModel = new NewsReaderModel();
    private User usr;
    private ObservableList<Article> allArticles;
    private Article selectedArticle;

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
        updateArticleButtons();
    }

    // Set the current user and update the UI accordingly
    public void setUser(User user) {
        this.usr = user;
        updateLoginStatus();
        getData();
    }

    // Fetch data from the model and update the list view
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
    }

    // Filter headlines based on the selected category
    @FXML
    private void filterHeadlines(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        updateHeadlinesListView(source.getText());
    }

    // Update the headlines list view with filtered articles based on the category
    private void updateHeadlinesListView(String category) {
        ObservableList<Article> filteredArticles = "All".equalsIgnoreCase(category) ? allArticles : allArticles.filtered(article -> category.equalsIgnoreCase(article.getCategory()));
        articlesListView.setItems(filteredArticles);
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

    // Handle the creation of a new article
    @FXML
    void handleNewArticle(ActionEvent event) {
        // TODO: function call to create 
    }

    // Handle the editing of an existing article
    @FXML
    void handleEditArticle(ActionEvent event) {
        // TODO: function call to edit 
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

    // Save an article to a file
    public void saveArticleToFile(Article article, String fileName) {
        // The JsonArticle class is assumed to have a method to convert an article to JSON
        // JsonObject jsonArticle = JsonArticle.articleToJson(article);
        // TODO: still todo
    }
}
