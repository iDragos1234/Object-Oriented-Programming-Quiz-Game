package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class AdminCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Activity activity;

    @FXML
    Button leaveButton;

    @FXML
    Button helpButton;

    @FXML
    TextArea rules;

    @FXML
    Button addQuestion;

    @FXML
    Button database;

    @FXML
    TextField idText;

    @FXML
    TextField imagePathText;

    @FXML
    TextField consumptionText;

    @FXML
    TextField sourceText;

    @FXML
    TextField titleText;

    @FXML
    Label title;

    @FXML
    Label source;

    @FXML
    Label id;

    @FXML
    Label imagePath;

    @FXML
    Label consumption;

    @FXML
    Label questionInfo;

    @FXML
    Button add;

    @FXML
    Button back;

    @FXML
    Label idLabel;

    @FXML
    Button edit;

    @FXML
    TextField idInput;

    @FXML
    Button delete;

    @FXML
    Button change;

    @FXML
    TableView<Activity> repoTable;

    @FXML
    TableColumn<Activity, String> id_col;

    @FXML
    TableColumn<Activity, Long> index_col;

    @FXML
    TableColumn<Activity, String> image_path_col;

    @FXML
    TableColumn<Activity, String> source_col;

    @FXML
    TableColumn<Activity, String> title_col;

    @FXML
    TableColumn<Activity, Long> consumption_col;

    @Inject
    public AdminCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * initialize admin scene
     * only shows appropriate buttons/labels... for the main admin page
     * and set all others to invisible
     * set the buttons rules as well
     */
    public void initialize() {
        repoTable.setVisible(false);
        leaveButton.setStyle("-fx-background-color: #ff0000;");
        rules.setWrapText(true);
        rules.setFont(Font.font(20));
        setRules();
        idText.setFocusTraversable(false);
        sourceText.setFocusTraversable(false);
        imagePathText.setFocusTraversable(false);
        titleText.setFocusTraversable(false);
        consumptionText.setFocusTraversable(false);
        unShowAll();
        change.setVisible(false);
        delete.setVisible(false);

    }

    /**
     * set the text for the help rules.
     */
    public void setRules() {
        rules.setText("As an admin you can change and edit questions.");
    }


    /**
     * return the main admin page then
     * goes back to the splash screen
     */
    public void toSplash() {
        back();
        mainCtrl.toSplash();
    }

    /**
     * show help rules
     * by moving mouse on it
     */
    public void clickHelp() {
        rules.setVisible(true);
    }

    /**
     * Hide rules
     * when mouse removed
     */
    public void releaseHelp() {
        rules.setVisible(false);
    }

    /**
     * enters the add question page
     * by showing appropriate content
     */
    public void addQuestion() {
        unShow();
        showAll();
        rules.setText("Enter the information for the question you want to add");
    }

    /**
     * hide the two buttons when entering a new page
     * will be used by some other methods
     */
    public void unShow() {
        addQuestion.setVisible(false);
        database.setVisible(false);
    }

    /**
     * shows the two buttons
     * needed from other methods
     */
    public void show() {
        addQuestion.setVisible(true);
        database.setVisible(true);
    }

    /**
     * used when entering the add question page
     * so all appropriate buttons/labels/text fields will be shown
     */
    public void showAll() {

        source.setVisible(true);

        sourceText.setVisible(true);
        idText.setVisible(true);
        id.setVisible(true);
        consumption.setVisible(true);
        consumptionText.setVisible(true);
        questionInfo.setVisible(true);
        title.setVisible(true);
        titleText.setVisible(true);
        imagePath.setVisible(true);
        imagePathText.setVisible(true);
        questionInfo.setVisible(true);
        back.setVisible(true);
        add.setVisible(true);
    }

    /**
     * for the add question and inspect database page,
     * all the buttons and labels on those pages will be
     * initiated within the admin scene,
     * unShowAll/showAll function is used to hide those and show them on when required.
     */
    public void unShowAll() {
        source.setVisible(false);
        sourceText.setVisible(false);
        idText.setVisible(false);
        id.setVisible(false);
        consumption.setVisible(false);
        consumptionText.setVisible(false);
        questionInfo.setVisible(false);
        title.setVisible(false);
        titleText.setVisible(false);
        imagePath.setVisible(false);
        imagePathText.setVisible(false);
        questionInfo.setVisible(false);
        back.setVisible(false);
        add.setVisible(false);
        idInput.setVisible(false);
        idLabel.setVisible(false);
        edit.setVisible(false);
    }

    /**
     * goes back to the "main" admin page
     * hide all content that is not needed
     * shows appropriate content
     * shows correct text for the help rules
     */
    public void back() {
        repoTable.setVisible(false);
        unShowAll();
        show();
        resetAddQuestion();
        setRules();
        delete.setVisible(false);
        change.setVisible(false);
    }

    /**
     * adds the question to the database
     * a new activity will get initialized using user's input
     * the index for this activity will be the size of the repository
     * repository will add the new activity
     * calls success dialog
     * then set all text fields to empty
     * now it checks if the id of new activity being added is already used in the database
     * if this is the case then index will not get updated, else the index will be the size of the repository
     */
    public void add() {

        String idString = idText.getText().strip();
        String imgString = imagePathText.getText().strip();
        String titleString = titleText.getText().strip();
        String consumeString = consumptionText.getText().strip();
        String sourceString = sourceText.getText().strip();
        Activity newActivity = new Activity(idString, imgString, titleString, consumeString, sourceString);
        List<Activity> activities = server.getAllActivities();
        List<Activity> matched = activities.stream().filter(x -> x.getId().equals(idString)).collect(Collectors.toList());
        if (!(matched.size() > 0)) {
            newActivity.setIndex(activities.size());
        } else {
            long index = matched.get(matched.size() - 1).getIndex();
            newActivity.setIndex(index);
        }

        server.addActivity(newActivity);
        mainCtrl.successDialog();
        resetAddQuestion();

    }

    /**
     * checks if user input are appropriate
     * if not appropriate alert will show
     * else call add
     */
    public void clickAdd() {
        if (inputChecks()) {
            add();
        }
    }

    /**
     * checks if a string is numeric
     *
     * @param strNum the string input
     * @return whether the statement is true
     */
    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Reset all text fields for add question page
     * making them empty
     */
    public void resetAddQuestion() {
        titleText.setText("");
        consumptionText.setText("");
        imagePathText.setText("");
        sourceText.setText("");
        idText.setText("");
    }

    /**
     * enters the inspecting database page
     * sets the font properly
     * returns all the activities in a readable form in the textarea
     * make appropriate things visible
     */
    public void showDatabase() {
        repoTable.setVisible(true);
        unShow();
        formatRepoTable();
        back.setVisible(true);
        idInput.setVisible(true);
        idLabel.setVisible(true);
        edit.setVisible(true);
        rules.setText("You can enter the unique id for the question you want to edit.\n");

    }


    /**
     * search for the id from the database
     * show the content of this activity
     * and user is able to change this question
     */
    public void search() {
        String id = idInput.getText();
        toEditScene(id);
    }

    /**
     * if the user has entered an ID that does not exist in the database
     * error message(alert window) will show
     * goes to the edit scene which is similar to the add question scene
     * though they have different buttons and functionalities
     * set all text field to the content of the activity matched
     * this is editable and will be saved to the repository
     *
     * @param id the id of the question going to be edited
     */
    public void toEditScene(String id) {
        try {

            activity = server.getActivity(id);
            back();
            unShow();
            idText.setText(activity.getId());
            titleText.setText(activity.getTitle());
            consumptionText.setText(Long.toString(activity.getConsumption_in_wh()));
            sourceText.setText(activity.getSource());
            imagePathText.setText(activity.getImage_path());

            showAll();
            add.setVisible(false);
            change.setVisible(true);
            delete.setVisible(true);
            rules.setText("You can edit the content of this question, or delete it completely");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText("ID does not exist");
            alert.setContentText("Please enter an existing ID from the database");
            alert.showAndWait();
        }

    }

    /**
     * delete this activity from the repository
     * couldn't figure out how to update the index of other activities so the index gets -1
     */
    public void delete() {
        String id = activity.getId();
        server.deleteById(id);
        mainCtrl.successDialog();
        back();
    }

    /**
     * checks if user has entered correct data types/ no null fields
     * if the user has invalid input, appropriate error alert will show
     * used for adding/changing question
     *
     * @return boolean whether the input is valid
     */
    public boolean inputChecks() {
        if (idText.getText().strip().equals("") || imagePathText.getText().strip().equals("") || titleText.getText().strip().equals("")
                || consumptionText.getText().strip().equals("") || sourceText.getText().strip().equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid input");
            alert.setHeaderText("Empty fields");
            alert.setContentText("Please do not leave any of the text fields empty");
            alert.showAndWait();
            return false;
        } else if (!isNumeric(consumptionText.getText())) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Invalid input");
            alert2.setHeaderText("Consumption value needs to be numeric!");
            alert2.setContentText("Please do not include any non-numeric value for the consumption");
            alert2.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * change the current activity from the repository based on user input
     * checks if id has been changed
     * if yes then the old one gets deleted and new one will be added
     * if not then this replaces the old one without changing the index
     */
    public void change() {
        if (inputChecks()) {
            if (activity.getId().equals(idText.getText())) {
                add();
                back();
            } else {
                String id = activity.getId();
                server.deleteById(id);
                add();
                back();
            }
        }

    }

    public ServerUtils getServer() {
        return server;
    }

    /**
     * initialize the table for the database
     * set the content to the activities from the database
     * set the cell size to 40
     */
    public void formatRepoTable() {
        id_col.setCellValueFactory(new PropertyValueFactory("id"));
        consumption_col.setCellValueFactory(new PropertyValueFactory("consumption_in_wh"));
        title_col.setCellValueFactory(new PropertyValueFactory("title"));
        source_col.setCellValueFactory(new PropertyValueFactory("source"));
        index_col.setCellValueFactory(new PropertyValueFactory("index"));
        image_path_col.setCellValueFactory(new PropertyValueFactory("image_path"));
        ObservableList<Activity> data = FXCollections.observableArrayList(
                server.getAllActivities()
        );
        setCell(id_col);
        setCell(title_col);
        setCell(source_col);
        setCell(image_path_col);
        repoTable.setFixedCellSize(40);
        repoTable.setItems(data);
    }

    /**
     * methods to format the column cells
     * for this column all the text in the cells will be wrapped
     *
     * @param column the column to be text-wrapped
     */
    public void setCell(TableColumn column) {
        column.setCellFactory(a -> {
            TableCell<Activity, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            text.wrappingWidthProperty().bind(column.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

    }
}
