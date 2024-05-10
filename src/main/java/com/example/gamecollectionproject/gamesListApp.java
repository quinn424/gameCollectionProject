package com.example.gamecollectionproject;
// **********************************************************************************
// Title: gameCollectionProject
// Author: Quinn Leeson
// Course Section: CMIS201-ONL1 (Seidel) Spring 2024
// File: gamesListApp.java
// Description: This program works as a game collection sorter. It includes options to record each item and its quality, the date acquired, etc.
// **********************************************************************************

import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;

public class gamesListApp extends Application {

    @Override
    public void start(Stage stage) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InterruptedException {
        File f = new File("gameCollection.csv");
        gamesList gameList = new gamesList(new ArrayList<Game>(gameGetter(f)));
        ArrayList<Game> gamesCollection = gameList.getGames();
        BorderPane view = new BorderPane();
        TreeView tree = new TreeView();
        Hashtable counter = new Hashtable();
        TilePane labels = new TilePane();
        GridPane grid = new GridPane();
        HBox options = new HBox();
        labels.setPadding(new Insets(3,3,3,3));
        VBox output = new VBox();
        TextArea out = new TextArea();
        Scene scene = new Scene(view, 800, 600);
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(3,3,3,3));
        out.setWrapText(true);
        out.setText("");
        int pos=out.caretPositionProperty().get();
        for(int i=0; i<gameList.getGames().size(); i++){
            out.appendText(gameList.getGames().get(i) + "\n\n");
        }
        out.positionCaret(pos);
        out.setEditable(false);
        output.getChildren().add(bar);
        output.getChildren().add(out);
        labels.setVgap(3);
        labels.setHgap(3);
        labels.setPrefColumns(2);
        labels.setStyle("-fx-background-color: FFFF00;");
        //Ideas: More sorts (such as lowest price), total price (into a box on the side),
        Label platform=new Label("Platform: ");
        TextField platformField=new TextField();
        Label category=new Label("Category: ");
        TextField categoryField=new TextField();
        Label userRecordType=new Label("Own status: ");
        TextField userRecordTypeField=new TextField();
        Label title=new Label("Title: ");
        TextField titleField = new TextField();
        Label country=new Label("Country: ");
        TextField countryField=new TextField();
        Label releaseType=new Label("Release Type: ");
        TextField releaseTypeField=new TextField();
        Label publisher=new Label("Publisher: ");
        TextField publisherField=new TextField();
        Label developer=new Label("Developer: ");
        TextField developerField=new TextField();
        Label createdAt=new Label("Created At: ");
        TextField createdAtField = new TextField();
        Label ownership=new Label("CIB status: ");
        ownership.setTooltip(new Tooltip("Is the case missing anything / is it in a box?"));
        TextField ownershipField=new TextField();
        Label priceLoose=new Label("Price Loose: ");
        TextField priceLooseField = new TextField();
        Label priceCIB=new Label("Price CIB: ");
        TextField priceCIBField=new TextField();
        Label priceNew=new Label("Price New: ");
        TextField priceNewField = new TextField();
        Label yourPrice=new Label("Your Price: ");
        TextField yourPriceField=new TextField();
        Label pricePaid=new Label("Price Paid: ");
        TextField pricePaidField = new TextField();
        Label itemCondition=new Label("Item Condition: ");
        TextField itemConditionField=new TextField();
        Label boxCondition=new Label("Box Condition: ");
        TextField boxConditionField = new TextField();
        Label manualCondition=new Label("Manual Condition: ");
        TextField manualConditionField=new TextField();
        Label notes=new Label("Notes: ");
        TextField notesField = new TextField();
        Label tags=new Label("Tags: ");
        TextField tagsField = new TextField();
        TextField totalTitles = new TextField("Titles: ");
        TextField totalValue = new TextField("Value: ");
        TextField totalYourPrice = new TextField("Your Total: ");
        double value=0.0; //New price, according to online
        double yourTotal=0.0; //Price paid as of acquired date
        int titleCount=gameList.getGames().size();
        for(int i=0; i<gameList.getGames().size();i++){
            if(!(gameList.getGames().get(i).getPriceNew().equalsIgnoreCase("-1.0") || gameList.getGames().get(i).getPriceNew().equalsIgnoreCase("?") || gameList.getGames().get(i).getPriceNew().matches(".*[a-z].*") || gameList.getGames().get(i).getPriceNew().equalsIgnoreCase("CIB")  || gameList.getGames().get(i).getPriceNew().equalsIgnoreCase(""))) {
                value = value + Double.parseDouble(gameList.getGames().get(i).getPriceNew());
            }
            if(!(gameList.getGames().get(i).getYourPrice().equalsIgnoreCase("-1.0") || gameList.getGames().get(i).getYourPrice().equalsIgnoreCase("?") || gameList.getGames().get(i).getYourPrice().matches(".*[a-z].*") || gameList.getGames().get(i).getYourPrice().equalsIgnoreCase("CIB") || gameList.getGames().get(i).getYourPrice().equalsIgnoreCase(""))) {
                yourTotal=yourTotal+Double.parseDouble(gameList.getGames().get(i).getYourPrice());
            }
        }
        totalYourPrice.setText("Your Total: $" + new DecimalFormat("###,###,###.00").format(yourTotal));
        totalValue.setText("Value: $" + new DecimalFormat("###,###,###.00").format(value));
        totalTitles.setText("Titles: " + titleCount);
        totalValue.setEditable(false);
        totalYourPrice.setEditable(false);
        totalTitles.setEditable(false);
        Button submitButton=new Button("Add");
        submitButton.setTooltip(new Tooltip("Adds any filled fields to the list, as long as there is a title and there is no existing exact match."));
        Button removeButton=new Button("Remove");
        removeButton.setTooltip(new Tooltip("Removes the first result matching all of the inputted fields."));
        Label searchLabel = new Label("Search List (Title): ");
        TextField searchBarField = new TextField();
        Button submitSearchTitleButton = new Button();
        submitSearchTitleButton.setTooltip(new Tooltip("Searches the list of games & displays any that contain the inputted text. Does not support treeview."));
        submitSearchTitleButton.setText("Submit");
        Button showAllButton=new Button("Show All");
        Button showDevelopersButton=new Button("Show Developers");
        Button showPublishersButton=new Button("Show Publishers");
        Button showCountriesButton=new Button("Show Countries");
        Button showTitlesButton=new Button("Show Titles");
        Button asTree=new Button("Show as Tree");
        Button sortByTitlesButton = new Button("Sort By Titles");
        Button sortByPublishers = new Button("Sort By Publishers");
        Button sortByCategory = new Button("Sort By Category");
        Button sortByPlatform = new Button("Sort By Platform");
        Button sortByCountry = new Button("Sort By Country");
        Button sortByDeveloper = new Button("Sort By Developers");
        Button sortByDate = new Button("Sort By Date");
        Button sortByPricePaid = new Button("Sort By Paid");
        Button sortByItemCondition = new Button("Sort by Item Condition");
        Button sortButton = new Button("Click to cycle through sorts for the full list display");
        Button createBackupButton=new Button("Create backup of current list");
        Button clearButton=new Button("Clear Games");
        Region left = new Region();
        bar.getChildren().addAll(searchLabel,searchBarField,submitSearchTitleButton);
        HBox.setHgrow(left,Priority.ALWAYS);
        GridPane.setConstraints(platform,0,0);
        GridPane.setConstraints(platformField,1,0);
        GridPane.setConstraints(category,0,1);
        GridPane.setConstraints(categoryField,1,1);
        GridPane.setConstraints(userRecordType,0,2);
        GridPane.setConstraints(userRecordTypeField,1,2);
        GridPane.setConstraints(title,0,3);
        GridPane.setConstraints(titleField,1,3);
        GridPane.setConstraints(country,0,4);
        GridPane.setConstraints(countryField,1,4);
        GridPane.setConstraints(releaseType,0,5);
        GridPane.setConstraints(releaseTypeField,1,5);
        GridPane.setConstraints(publisher,0,6);
        GridPane.setConstraints(publisherField,1,6);
        GridPane.setConstraints(developer,0,7);
        GridPane.setConstraints(developerField,1,7);
        GridPane.setConstraints(createdAt,0,8);
        GridPane.setConstraints(createdAtField,1,8);
        GridPane.setConstraints(ownership,0,9);
        GridPane.setConstraints(ownershipField,1,9);
        GridPane.setConstraints(priceLoose,0,10);
        GridPane.setConstraints(priceLooseField,1,10);
        GridPane.setConstraints(priceCIB,0,11);
        GridPane.setConstraints(priceCIBField,1,11);
        GridPane.setConstraints(priceNew,0,12);
        GridPane.setConstraints(priceNewField,1,12);
        GridPane.setConstraints(yourPrice,0,13);
        GridPane.setConstraints(yourPriceField,1,13);
        GridPane.setConstraints(pricePaid,0,14);
        GridPane.setConstraints(pricePaidField,1,14);
        GridPane.setConstraints(itemCondition,0,15);
        GridPane.setConstraints(itemConditionField,1,15);
        GridPane.setConstraints(boxCondition,0,16);
        GridPane.setConstraints(boxConditionField,1,16);
        GridPane.setConstraints(manualCondition,0,17);
        GridPane.setConstraints(manualConditionField,1,17);
        GridPane.setConstraints(notes,0,18);
        GridPane.setConstraints(notesField,1,18);
        GridPane.setConstraints(tags,0,19);
        GridPane.setConstraints(tagsField,1,19);
        GridPane.setConstraints(submitButton,0,20);
        GridPane.setConstraints(removeButton,1,20);
        HBox top = new HBox(createBackupButton,clearButton,left,totalTitles,totalValue,totalYourPrice);
        //top.getChildren().addAll(createBackupButton,clearButton);
        grid.getChildren().addAll(platform,platformField,category,categoryField,userRecordType,userRecordTypeField,title,titleField,country,countryField,releaseType,releaseTypeField,publisher,publisherField,developer,developerField,createdAt,createdAtField,ownership,ownershipField,priceLoose,priceLooseField,priceCIB,priceCIBField,priceNew,priceNewField,yourPrice,yourPriceField,pricePaid,pricePaidField,itemCondition,itemConditionField,boxCondition,boxConditionField,manualCondition,manualConditionField,notes,notesField,tags,tagsField,submitButton,removeButton);
        options.getChildren().addAll(showAllButton,showCountriesButton,showTitlesButton,showDevelopersButton,showPublishersButton);
        submitButton.setOnAction(actionEvent -> {
            int scrollPos = out.caretPositionProperty().get();
            gameList.add(platformField.getText(),categoryField.getText(),userRecordTypeField.getText(),titleField.getText(),countryField.getText(),releaseTypeField.getText(),publisherField.getText(),developerField.getText(),createdAtField.getText(),ownershipField.getText(),priceLooseField.getText(),priceCIBField.getText(),priceNewField.getText(),yourPriceField.getText(),pricePaidField.getText(),itemConditionField.getText(),boxConditionField.getText(),manualConditionField.getText(),notesField.getText(),tagsField.getText());
            out.setText("");
            for(int i=0; i<gameList.getGames().size(); i++){
                out.appendText(gameList.getGames().get(i).toString() + "\n\n");
            }
            out.positionCaret(scrollPos);
            tree.setRoot(createTree(gameList));
        });
        Queue<Method> sortQueue = new LinkedList<Method>();
        sortQueue.add((gameList.getClass().getMethod(("sortByCountry"))));
        sortQueue.add((gameList.getClass().getMethod(("sortByDeveloper"))));
        sortQueue.add((gameList.getClass().getMethod(("sortByTitles"))));
        sortQueue.add((gameList.getClass().getMethod(("sortByPlatform"))));
        sortQueue.add((gameList.getClass().getMethod(("sortByPublisher"))));
        sortButton.setOnAction(actionEvent ->{ //Project Part 2: Adding a stack which cycles through methods.
            try { //Might add way to consider current list display in the future when sorting instead of pasting the full list
                Method temp = sortQueue.peek();
                sortQueue.poll().invoke(gameList);
                sortQueue.add(temp);
                int scrollPos = out.caretPositionProperty().get();
                out.setText("");
                for(int i=0; i<gameList.getGames().size(); i++){
                    out.appendText(gameList.getGames().get(i).toString() + "\n\n");
                }
                sortButton.setText("Current sort: " + temp.getName().toString().replace("sortBy","") +  " -> Sort next: " + sortQueue.peek().getName().toString().replace("sortBy",""));
                out.positionCaret(scrollPos);
                if(tree.isVisible()){
                   tree.setRoot(createTree(gameList));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
        removeButton.setOnAction(actionEvent -> {
            int scrollPos = out.caretPositionProperty().get();
            gameList.remove(platformField.getText(),categoryField.getText(),userRecordTypeField.getText(),titleField.getText(),countryField.getText(),releaseTypeField.getText(),publisherField.getText(),developerField.getText(),createdAtField.getText(),ownershipField.getText(),priceLooseField.getText(),priceCIBField.getText(),priceNewField.getText(),yourPriceField.getText(),pricePaidField.getText(),itemConditionField.getText(),boxConditionField.getText(),manualConditionField.getText(),notesField.getText(),tagsField.getText());
            out.setText("");
            for(int i=0; i<gameList.getGames().size(); i++){
                out.appendText(gameList.getGames().get(i).toString() + "\n\n");
            }
            out.positionCaret(scrollPos);
            tree.setRoot(createTree(gameList));
        });
        showTitlesButton.setOnAction(actionEvent -> {
            out.setVisible(true);
            out.setManaged(true);
            tree.setManaged(false);
            tree.setVisible(false);
            int scrollPos = out.caretPositionProperty().get();
            out.setText("");
            gameList.sortByTitles();
            sortButton.setText("Current sort: By title  -> Next sort: " + sortQueue.peek().getName().toString().replace("sortBy",""));
            for(int i=0; i<gameList.getGames().size(); i++){
                out.appendText(gameList.getGames().get(i).getTitle() + "\n");
            }
            out.appendText("Size is: "+gameList.titleCount());
            out.positionCaret(scrollPos);
        });
        submitSearchTitleButton.setOnAction(actionEvent->{ //Part 4: Additional Improvement
            ArrayList<Game> temp = new ArrayList<>();
            out.clear();
            Thread t = new Thread(new Runnable() { //Part 4: Multithreading (1)
                @Override
                public void run() {
                    for(int i=0; i<gameList.getGames().size();i++){
                        Game g = gameList.getGames().get(i);
                        if(g.getTitle().toLowerCase().contains(searchBarField.getText().toLowerCase())){
                            temp.add(g);
                            out.appendText(g.toString() + "\n\n");
                        }
                    }
                    tree.setVisible(false);
                    tree.setManaged(false);
                    out.setVisible(true);
                    out.setManaged(true);
                }
            });
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        showAllButton.setOnAction(actionEvent -> {
            out.setVisible(true);
            out.setManaged(true);
            tree.setManaged(false);
            tree.setVisible(false);
            int scrollPos = out.caretPositionProperty().get();
            out.setText("");
            for(int i=0; i<gameList.getGames().size(); i++){
                out.appendText(gameList.getGames().get(i).toString() + "\n\n");
            }
            out.positionCaret(scrollPos);
        });
        showDevelopersButton.setOnAction(actionEvent ->{
            out.setVisible(true);
            out.setManaged(true);
            tree.setManaged(false);
            tree.setVisible(false);
            int scrollPos = out.caretPositionProperty().get();
            out.setText("");
            gameList.sortByDeveloper();
            sortButton.setText("Current sort: By developer -> Next sort: " + sortQueue.peek().getName().toString().replace("sortBy",""));
            String[][] temp = gameList.getDevelopers();
            for(int i=0; i<temp.length; i++){
                out.appendText(temp[i][0] + " | " + temp[i][1] +  "\n");
            }
            out.appendText("Size is: "+temp.length);
            out.positionCaret(scrollPos);
        });
        showPublishersButton.setOnAction(actionEvent ->{
            out.setVisible(true);
            out.setManaged(true);
            tree.setManaged(false);
            tree.setVisible(false);
            int scrollPos = out.caretPositionProperty().get();
            out.setText("");
            gameList.sortByPublisher();
            sortButton.setText("Current sort: By publisher  -> Next sort: " + sortQueue.peek().getName().toString().replace("sortBy",""));
            String[][] temp = gameList.getPublishers();
            for(int i=0; i<temp.length; i++){
                out.appendText(temp[i][0] + " | " + temp[i][1] +  "\n");
            }
            out.appendText("Size is: "+temp.length);
            out.positionCaret(scrollPos);
        });
        showCountriesButton.setOnAction(actionEvent ->{
            out.setVisible(true);
            out.setManaged(true);
            tree.setManaged(false);
            tree.setVisible(false);
            int scrollPos = out.caretPositionProperty().get();
            out.setText("");
            gameList.sortByCountry();
            sortButton.setText("Current sort: By country  -> Next sort: " + sortQueue.peek().getName().toString().replace("sortBy",""));
            String[][] temp = gameList.getCountries();
            for(int i=0; i<temp.length; i++){
                out.appendText(temp[i][0] + " | " + temp[i][1] +  "\n");
            }
            out.appendText("Size is: "+temp.length);
            out.positionCaret(scrollPos);
        });
        EventHandler backup = new EventHandler() {
            @Override
            public void handle(Event event) {
                    out.setVisible(true);
                    out.setManaged(true);
                    tree.setManaged(false);
                    tree.setVisible(false);
                    String name = "Backup"+(Math.random()*10)*(System.currentTimeMillis()/100)+".csv";
                    File g = new File((name));
                    try(FileWriter writer = new FileWriter(g)){
                        writer.write(gameList.parameters.toString() + "\n");
                        for(int i=0; i<gameList.getGames().size();i++){
                            writer.write(gamesCollection.get(i).toString() + "\n");
                        }
                        out.setText("Success, saved to "+name);
                    } catch (IOException e) {
                        out.setText("Failure");
                        e.printStackTrace();
                    }
            }
        };
        createBackupButton.setOnAction(backup);
        clearButton.setOnAction(actionEvent->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Clear games?",ButtonType.YES,ButtonType.NO);
            Alert alert2;
            alert.showAndWait();
            if(alert.getResult()==ButtonType.YES){
                out.setText("");
                alert2 = new Alert(Alert.AlertType.WARNING, "Create backup?",ButtonType.YES,ButtonType.NO);
                alert2.showAndWait();
                if(alert2.getResult()==ButtonType.YES){
                    createBackupButton.fire();
                }
                gameList.games=new ArrayList<Game>();
                try(FileWriter writer = new FileWriter(f)){
                    writer.write(gameList.parameters.toString() + "\n");
                    for(int i=0; i<gameList.getGames().size();i++){
                        writer.write(gamesCollection.get(i).toString() + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.appendText("\nSuccessfully cleared");
            }
        });
        asTree.setOnAction(actionEvent -> { //Project Part 3: Adding the tree
            if(out.isVisible()){
                out.setVisible(false);
                out.setManaged(false);
                tree.setManaged(true);
                tree.setVisible(true);
            }else{
                out.setVisible(true);
                out.setManaged(true);
                tree.setManaged(false);
                tree.setVisible(false);
            }
            if(asTree.getText().toLowerCase().contains("tree")){
                asTree.setText("Show as List");
            }else{
                asTree.setText("Show as Tree");
            }
            tree.setRoot(createTree(gameList));
        });
        grid.setStyle("-fx-background-color: #a6eded;");
        grid.setPadding(new Insets(5,5,5,5));
        output.setAlignment(Pos.CENTER);
        grid.setAlignment(Pos.CENTER_LEFT);
        options.setAlignment(Pos.CENTER_RIGHT);
        out.setMinHeight(300);
        output.setMinHeight(300);
        view.setPadding(new Insets(5,5,5,5));
        view.setLeft(grid);
        submitButton.setText("Submit");
        tree.setManaged(false);
        tree.setVisible(false);

        output.getChildren().addAll(tree,sortButton,asTree);
        view.setCenter(output);
        view.setRight(new VBox());
        view.setTop(top);
        view.setBottom(options);
        stage.setTitle("Gamelist App");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(Event ->{
            if(!gameList.getGames().isEmpty()) {
                try (FileWriter writer = new FileWriter(f)) {
                    writer.write(gameList.parameters.toString() + "\n");
                    for (int i = 0; i < gameList.getGames().size(); i++) {
                        writer.write(gamesCollection.get(i).toString() + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                File g = new File("tempBackup"+(int)(Math.random()*100)+".csv");
                try {
                    Files.copy(f.toPath(),g.toPath());

                    try (FileWriter writer = new FileWriter(f)) {
                        writer.write(gameList.parameters.toString() + "\n");
                        for (int i = 0; i < gameList.getGames().size(); i++) {
                            writer.write(gamesCollection.get(i).toString() + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            System.exit(0);
        });
    }
    public static void main(String[] args) {
        launch();
    }
    ArrayList<Game> gameGetter(File file) throws IOException, InterruptedException {
        ArrayList<Game> temp = new ArrayList<>(); //Part 4: Multithreading(2)
        Scanner reader = new Scanner(file);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                reader.nextLine();
                while (reader.hasNextLine()) {
                    Game game = new Game(reader.nextLine());
                    game.setTitle(game.getTitle().replaceAll(",",""));
                    game.setCategory(game.getCategory().replaceAll(",",""));
                    game.setCountry(game.getCountry().replaceAll(",",""));
                    game.setCreatedAt(game.getCreatedAt().replaceAll(",",""));
                    game.setPlatform(game.getPlatform().replaceAll(",",""));
                    game.setPublisher(game.getPublisher().replaceAll(",",""));
                    game.setDeveloper(game.getDeveloper().replaceAll(",",""));
                    game.setNotes(game.getNotes().replaceAll(",",""));
                    game.setUserRecordType(game.getUserRecordType().replaceAll(",",""));
                    temp.add(game);
                }
            }
        });
        thread.start();
        thread.join();
        if(temp.isEmpty()){
            //System.exit(1);
        }
        return temp;
    }
    TreeItem<String> createTree(gamesList games){ //Project part 3:
        TreeItem<String> rootItem = new TreeItem<String> ("Games", new VBox());
        TreeItem<String> item1;
        rootItem.setExpanded(true);
        for(int k=0; k<games.getGames().size();k++) {
            item1 = new TreeItem<String> (games.getGames().get(k).getTitle(), new VBox());
            for (int i = 0; i < games.parameterCounter(); i++) {
                if(!(games.getGames().get(k).getParameter(i).equalsIgnoreCase("") || games.getGames().get(k).getParameter(i).equalsIgnoreCase("Missing Field") || games.getGames().get(k).getParameter(i).equalsIgnoreCase("Null"))){
                    TreeItem<String> item = new TreeItem<String>(games.getGames().get(k).getParameters().get(i) + ": " + games.getGames().get(k).getParameter(i));
                    item1.getChildren().add(item);
                }
            }
            rootItem.getChildren().add(item1);
        }
        return rootItem;
    }
}
class gamesList extends Thread{
    ArrayList<Game> games;
    HashSet<String> lister;
    ArrayList<String> arrayLister;
    HashSet<String> parameters;
    Method e[];
    gamesList(){
        this.lister = new HashSet<String>();
        this.arrayLister = new ArrayList<>();
        this.e = Game.class.getMethods();
        this.parameters = new HashSet<>();
        for(int i=0; i<e.length; i++){
            parameters.add(e[i].getName());
        }
        this.games = new ArrayList<>();
    }
    public void run(){

    }
    public void publisherThread(){
        Thread publishSortThread = new Thread(this::sortByPublisher);
        publishSortThread.start();
    }
    public void developerThread(){
        Thread sortThread = new Thread(this::sortByDeveloper);
        sortThread.start();
    }
    public void countryThread(){
        Thread publishSortThread = new Thread(this::sortByCountry);
        publishSortThread.start();
    }
    public void platformThread(){
        Thread sortThread = new Thread(this::sortByPlatform);
        sortThread.start();
    }
    public void titlesThread(){
        System.out.println("test");
        Thread sortThread = new Thread(this::sortByTitles);
        sortThread.start();
    }
    public gamesList(ArrayList<Game> games){
        this.lister = new HashSet<String>();
        this.arrayLister = new ArrayList<>();
        this.e = Game.class.getMethods();
        this.parameters = new HashSet<>();
        for(int i=0; i<e.length; i++){
            this.parameters.add(e[i].getName());
        }
        this.games = games;
    }
    public ArrayList<Game> getGames() {
        return games;
    }
    public int parameterCounter(){
        return 20;
    }
    public String[][] getGamesString(){
        HashSet<String> e = new HashSet<String>();
        ArrayList<String> t = new ArrayList<>();
        int count=0;
        int index=0;
        for(int i=0; i<this.getGames().size();i++){
            if(this.games.get(i).getTitle().equalsIgnoreCase("")||this.games.get(i).getTitle().equalsIgnoreCase("Unknown")||this.games.get(i).getTitle().equalsIgnoreCase(",")){
                e.add("Unknown");
            }else {
                e.add(this.games.get(i).getTitle());
            }
        }
        String[][] temp = new String[e.size()][2];
        for(int i=0; i<e.size();i++){
            temp[i][0]= (String) e.toArray()[i];
            temp[i][1]="0";
        }
        int num=0;
        Iterator i=e.iterator();
        while(i.hasNext()){
            String tem = i.next().toString();
            for(int k=0; k<this.getGames().size(); k++){
                if(tem.equalsIgnoreCase(this.getGames().get(k).getTitle())){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }else if((tem.equalsIgnoreCase("Unknown")&&(this.getGames().get(k).getTitle().equalsIgnoreCase("")||this.getGames().get(k).getTitle().equalsIgnoreCase("Unknown")))){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }
            }
            num++;
        }
        return temp;
    }
    public String[][] getCountries(){
        HashSet<String> e = new HashSet<String>();
        ArrayList<String> t = new ArrayList<>();
        this.sortByCountry();
        int count=0;
        int index=0;
        for(int i=0; i<this.getGames().size();i++){
            if(this.games.get(i).getCountry().equalsIgnoreCase("")||this.games.get(i).getCountry().equalsIgnoreCase("Unknown")||this.games.get(i).getCountry().equalsIgnoreCase(",")){
                e.add("Unknown");
            }else {
                e.add(this.games.get(i).getCountry());
            }
        }
        String[][] temp = new String[e.size()][2];
        for(int i=0; i<e.size();i++){
            temp[i][0]= (String) e.toArray()[i];
            temp[i][1]="0";
        }
        int num=0;
        Iterator i=e.iterator();
        while(i.hasNext()){
            String tem = i.next().toString();
            for(int k=0; k<this.getGames().size(); k++){
                if(tem.equalsIgnoreCase(this.getGames().get(k).getCountry())){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }else if((tem.equalsIgnoreCase("Unknown")&&(this.getGames().get(k).getCountry().equalsIgnoreCase("")||this.getGames().get(k).getCountry().equalsIgnoreCase("Unknown")))){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }
            }
            num++;
        }
        return sort(temp);
    }
    public String[][] getPublishers(){
        HashSet<String> e = new HashSet<String>();
        ArrayList<String> t = new ArrayList<>();
        this.sortByPublisher();
        int count=0;
        int index=0;
        for(int i=0; i<this.getGames().size();i++){
            if(this.games.get(i).getPublisher().equalsIgnoreCase("")||this.games.get(i).getPublisher().equalsIgnoreCase("Unknown")||this.games.get(i).getPublisher().equalsIgnoreCase(",")){
                e.add("Unknown");
            }else {
                e.add(this.games.get(i).getPublisher());
            }
        }
        String[][] temp = new String[e.size()][2];
        for(int i=0; i<e.size();i++){
            temp[i][0]= (String) e.toArray()[i];
            temp[i][1]="0";
        }
        int num=0;
        Iterator i=e.iterator();
        while(i.hasNext()){
            String tem = i.next().toString();
            for(int k=0; k<this.getGames().size(); k++){
                if(tem.equalsIgnoreCase(this.getGames().get(k).getPublisher())){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }else if((tem.equalsIgnoreCase("Unknown")&&(this.getGames().get(k).getPublisher().equalsIgnoreCase("")||this.getGames().get(k).getPublisher().equalsIgnoreCase("Unknown")))){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }
            }
            num++;
        }
        return sort(temp);
    }
    public String[][] getDevelopers(){
        HashSet<String> e = new HashSet<String>();
        ArrayList<String> t = new ArrayList<>();
        this.sortByDeveloper();
        int count=0;
        int index=0;
        for(int i=0; i<this.getGames().size();i++){
            if(this.games.get(i).getDeveloper().equalsIgnoreCase("")||this.games.get(i).getDeveloper().equalsIgnoreCase("Unknown")||this.games.get(i).getDeveloper().equalsIgnoreCase(",")){
                e.add("Unknown");
            }else {
                e.add(this.games.get(i).getDeveloper());
            }
        }
        String[][] temp = new String[e.size()][2];
        for(int i=0; i<e.size();i++){
            temp[i][0]= (String) e.toArray()[i];
            temp[i][1]="0";
        }
        int num=0;
        Iterator i=e.iterator();
        while(i.hasNext()){
            String tem = i.next().toString();
            for(int k=0; k<this.getGames().size(); k++){
                if(tem.equalsIgnoreCase(this.getGames().get(k).getDeveloper())){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }else if((tem.equalsIgnoreCase("Unknown")&&(this.getGames().get(k).getDeveloper().equalsIgnoreCase("")||this.getGames().get(k).getDeveloper().equalsIgnoreCase("Unknown")))){
                    temp[num][1]=""+(Integer)(Integer.parseInt(temp[num][1])+1);
                }
            }
            num++;
        }
        return sort(temp);
    }
    public static String[][]sort(String[][] temp){
        String[] g = new String[temp.length];
        for(int v=0; v<g.length;v++){
            g[v]=temp[v][0];
        }
        Arrays.sort(g);
        String[][] temp2 = new String[temp.length][2];
        for(int v=0; v<g.length;v++){
            for(int k=0;k<temp.length;k++){
                if(g[v].equalsIgnoreCase(temp[k][0])){
                    temp2[v][0]=g[v];
                    temp2[v][1]=temp[k][1];
                }
            }
        }
        return temp2;
    }
    boolean remove(String title){
        ArrayList<Game> temp = this.games;
        for(int i=0; i<this.games.size(); i++){
            if(this.games.get(i).getTitle().equalsIgnoreCase(title)){
                this.games.remove(i);
                i=i-1;
                return true;
            }
        }
        return false;
    }
    public boolean remove(String platform, String category, String userRecord, String title, String country, String releaseType, String publisher, String developer, String createdAt, String ownership, String priceLoose, String priceCIB, String priceNew, String yourPrice, String pricePaid, String itemCondition, String boxCondition, String manualCondition, String notes, String tags) {
        boolean status=false;
        Game temp = new Game(platform, category, userRecord, title, country, releaseType, publisher, developer, createdAt, ownership, priceLoose, priceCIB, priceNew, yourPrice, pricePaid, itemCondition, boxCondition, manualCondition, notes, tags);
        for(int i=0; i<this.games.size();i++){
            if(this.games.get(i).getTitle().equalsIgnoreCase(title)) {
                status=true;
                Game temp2 = new Game(this.games.get(i).getGameExact());
                if(platform.isEmpty()){
                    temp2.setPlatform("");
                }
                if(category.isEmpty()){
                    temp2.setCategory("");
                }
                if(userRecord.isEmpty()){
                    temp2.setUserRecordType("");
                }
                if(country.isEmpty()){
                    temp2.setCountry("");
                }
                if(releaseType.isEmpty()){
                    temp2.setReleaseType("");
                }
                if(publisher.isEmpty()){
                    temp2.setPublisher("");
                }
                if(developer.isEmpty()){
                    temp2.setDeveloper("");
                }
                if(createdAt.isEmpty()){
                    temp2.setCreatedAt("");
                }
                if(ownership.isEmpty()){
                    temp2.setOwnership("");
                }
                if(priceLoose.isEmpty()){
                    temp2.setPriceLoose("");
                }
                if(priceCIB.isEmpty()){
                    temp2.setPriceCIB("");
                }
                if(priceNew.isEmpty()){
                    temp2.setPriceNew("");
                }
                if(yourPrice.isEmpty()){
                    temp2.setYourPrice("");
                }
                if(pricePaid.isEmpty()){
                    temp2.setPricePaid("");
                }
                if(itemCondition.isEmpty()){
                    temp2.setItemCondition("");
                }
                if(boxCondition.isEmpty()){
                    temp2.setBoxCondition("");
                }
                if(manualCondition.isEmpty()){
                    temp2.setManualCondition("");
                }
                if(notes.isEmpty()){
                    temp2.setNotes("");
                }
                if(tags.isEmpty()){
                    temp2.setTags("");
                }
                if(temp2.toString().equalsIgnoreCase(temp.toString())){
                    this.games.remove(this.games.get(i));
                }
            }
        }
        return status;
    }
    boolean removeAll(String parameterGroup){
        parameterGroup=parameterGroup.trim();
        boolean succeeded=false;
        for(int i=0; i<this.games.size();i++){
            if(this.games.get(i).toString().contains(parameterGroup)){
                this.games.remove(i);
                i=i-1;
                succeeded=true;
            }
        }
        return succeeded;
    }
    public boolean add(String game) {
        boolean valid=true;
        for(int i=0; i<this.games.size();i++){
            String[] temp = game.split("\",\"");
            if((this.games.get(i).toString().equalsIgnoreCase(game))){
                valid=false;
            }
        }
        if(valid){
            this.games.add(new Game(game));
        }
        return valid;
    }
    public boolean add(String platform, String category, String userRecordType, String title, String country, String releaseType, String publisher, String developer, String createdAt, String ownership, String priceLoose, String priceCIB, String priceNew, String yourPrice, String pricePaid, String itemCondition, String boxCondition, String manualCondition, String notes, String tags) {
        String game="\""+platform+"\", \"" +category+"\", \""+userRecordType+"\", \""+title+"\", \""+country+"\", \""+releaseType+"\", \""+publisher+"\", \""+developer+"\", \""+createdAt+"\", \""+ownership+"\", \""+priceLoose+"\", \""+priceCIB+"\", \""+priceNew+"\", \""+yourPrice+"\", \""+pricePaid+"\", \""+itemCondition+"\", \""+boxCondition+"\", \""+manualCondition+"\", \""+notes+"\", \""+tags+"\"";
        Game t = new Game(platform.replaceAll(",",""), category.replaceAll(",",""), userRecordType.replaceAll(",",""), title.replaceAll(",",""), country.replaceAll(",",""), releaseType.replaceAll(",",""),publisher.replaceAll(",",""),developer.replaceAll(",",""),createdAt.replaceAll(",",""),ownership.replaceAll(",",""),priceLoose.replaceAll(",",""),priceCIB.replaceAll(",",""),priceNew,yourPrice,pricePaid,itemCondition,boxCondition,manualCondition,notes.replaceAll(",",""),tags.replaceAll(",",""));
        boolean valid=true;
        if(!(title.isEmpty())) {
            for (int i = 0; i < this.games.size(); i++) {
                //String[] temp = game.split("\",\"");
                if (this.games.get(i).toString().equalsIgnoreCase(t.toString())) {
                    valid = false;
                }
            }
            if (valid) {
                this.games.add(new Game(t));
                this.sortByTitles();
            }
        }
        return valid;
    }
    public boolean edit(String title, String parameter,String change) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        parameter=parameter.toLowerCase();
        parameter=parameter.replace("set","");
        parameter=parameter.substring(0,1).toUpperCase() + parameter.substring(1);
        parameter="set"+parameter;
        if(this.games.size()>0) {
            if (parameters.contains(parameter)) {
                for(int i=0; i<this.games.size();i++){
                    if(this.games.get(i).getTitle().equalsIgnoreCase(title)){
                        Method e = this.games.get(i).getClass().getMethod(parameter,String.class);
                        e.invoke(this.games.get(i),change);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    HashSet<String> listerMethod(ArrayList<Game> gamesCollection, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        lister = new HashSet<>();
        type=type.toLowerCase();
        type=type.replace("get","");
        type=type.substring(0,1).toUpperCase() + type.substring(1);
        type="get"+type;
        this.games = gamesCollection;
        if(this.games.size()>0) {
            if (parameters.contains(type)) {
                for (int i = 0; i < gamesCollection.size(); i++) {
                    Method e = gamesCollection.get(i).getClass().getMethod(type);
                    if (!((e.invoke(gamesCollection.get(i))).toString().equalsIgnoreCase(""))) {
                        lister.add((e.invoke(gamesCollection.get(i))).toString());
                    }
                }
            }
        }
        return lister;
    }
    HashSet<String> listerMethod(String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        lister = new HashSet<>();
        type=type.toLowerCase();
        type=type.replace("get","");
        type=type.substring(0,1).toUpperCase() + type.substring(1);
        type="get"+type;
        if(this.games.size()>0) {
            if (parameters.contains(type)) {
                for (int i = 0; i < this.games.size(); i++) {
                    Method e = this.games.get(i).getClass().getMethod(type);
                    if (!((e.invoke(this.games.get(i))).toString().equalsIgnoreCase(""))) {
                        lister.add((e.invoke(this.games.get(i))).toString());
                    }
                }
            }
        }
        return lister;
    }
    ArrayList<String> arrayListerMethod(ArrayList<Game> gamesCollection, String type) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        arrayLister = new ArrayList<>();
        type=type.toLowerCase();
        type=type.replace("get","");
        type=type.substring(0,1).toUpperCase() + type.substring(1);
        type="get"+type;
        this.games = gamesCollection;
        if(this.games.size()>0) {
            if (parameters.contains(type)) {
                for (int i = 0; i < gamesCollection.size(); i++) {
                    Method e = gamesCollection.get(i).getClass().getMethod(type);
                    if (!((e.invoke(gamesCollection.get(i))).toString().equalsIgnoreCase(""))) {
                        arrayLister.add((e.invoke(gamesCollection.get(i))).toString());
                    }
                }
            }
        }
        return arrayLister;
    }
    ArrayList<String> arrayListerMethod(String type) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        arrayLister = new ArrayList<>();
        type=type.toLowerCase();
        type=type.replace("get","");
        type=type.substring(0,1).toUpperCase() + type.substring(1);
        type="get"+type;
        if(this.games.size()>0) {
            if (parameters.contains(type)) {
                for (int i = 0; i < this.games.size(); i++) {
                    Method e = this.games.get(i).getClass().getMethod(type);
                    if (!((e.invoke(this.games.get(i))).toString().equalsIgnoreCase(""))) {
                        arrayLister.add((e.invoke(this.games.get(i))).toString());
                    }
                }
            }
        }
        return arrayLister;
    }
    public void sortByTitles(){
        this.games.sort(new Comparator<Game>() {
            @Override
            public int compare(Game o1, Game o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });
    }
    public void sortByPlatform(){
        this.games.sort(new Comparator<Game>() {
            @Override
            public int compare(Game o1, Game o2) {
                return o1.getPlatform().compareToIgnoreCase(o2.getPlatform());
            }
        });
    }
    public void sortByPublisher(){
        this.games.sort(new Comparator<Game>() {
            @Override
            public int compare(Game o1, Game o2) {
                return o1.getPublisher().compareToIgnoreCase(o2.getPublisher());
            }
        });
    }
    public void sortByCountry(){
        this.games.sort(new Comparator<Game>() {
            @Override
            public int compare(Game o1, Game o2) {
                return o1.getCountry().compareToIgnoreCase(o2.getCountry());
            }
        });
    }
    public void sortByDeveloper(){
        this.games.sort(new Comparator<Game>() {
            @Override
            public int compare(Game o1, Game o2) {
                return o1.getDeveloper().compareToIgnoreCase(o2.getDeveloper());
            }
        });
    }
    public Integer titleCount(){
        return this.getGames().size();
    }
}

class Game{
    String platform;
    String category;
    String userRecordType;
    String title;
    String country;
    String releaseType;
    String publisher;
    String developer;
    String createdAt;
    String ownership;
    String priceLoose;
    String priceCIB;
    String priceNew;
    String yourPrice;
    String pricePaid;
    String itemCondition;
    String boxCondition;
    String manualCondition;
    String notes;
    String tags;
    String game;
    String[] parameterArray=new String[20];
    ArrayList<String> parameters = new ArrayList();

    public Game() {
        this.parameters.add("Platform");
        this.parameters.add("Category");
        this.parameters.add("userRecordType");
        this.parameters.add("title");
        this.parameters.add("country");
        this.parameters.add("releaseType");
        this.parameters.add("publisher");
        this.parameters.add("developer");
        this.parameters.add("createdAt");
        this.parameters.add("ownership");
        this.parameters.add("priceLoose");
        this.parameters.add("priceCIB");
        this.parameters.add("priceNew");
        this.parameters.add("yourPrice");
        this.parameters.add("pricePaid");
        this.parameters.add("itemCondition");
        this.parameters.add("boxCondition");
        this.parameters.add("manualCondition");
        this.parameters.add("notes");
        this.parameters.add("tags");
        this.platform="null";
        this.category="null";
        this.userRecordType="null";
        this.title="null";
        this.country="null";
        this.releaseType="null";
        this.publisher="null";
        this.developer="null";
        this.createdAt="null";
        this.ownership="null";
        this.priceLoose="null";
        this.priceCIB="null";
        this.priceNew="null";
        this.yourPrice="null";
        this.pricePaid="null";
        this.itemCondition="null";
        this.boxCondition="null";
        this.manualCondition="null";
        this.notes="null";
        this.tags="null";
        this.game="null";

    }
    public Game(String data) {
        this.game=data;
        //data = data.replaceAll(", ","");
        String[] s = data.split(",");
        for(int i=0; i<s.length; i++){
            System.out.println(s[i]);
            s[i]= s[i].replaceAll("\"","");
            s[i] = s[i].trim();
        }
        this.parameterArray=s;
        this.parameters.add("Platform");
        this.parameters.add("Category");
        this.parameters.add("userRecordType");
        this.parameters.add("title");
        this.parameters.add("country");
        this.parameters.add("releaseType");
        this.parameters.add("publisher");
        this.parameters.add("developer");
        this.parameters.add("createdAt");
        this.parameters.add("ownership");
        this.parameters.add("priceLoose");
        this.parameters.add("priceCIB");
        this.parameters.add("priceNew");
        this.parameters.add("yourPrice");
        this.parameters.add("pricePaid");
        this.parameters.add("itemCondition");
        this.parameters.add("boxCondition");
        this.parameters.add("manualCondition");
        this.parameters.add("notes");
        this.parameters.add("tags");

        this.platform=s[0].replaceAll(",","");
        this.category=s[1].replaceAll(",","");
        this.userRecordType=s[2].replaceAll(",","");;
        this.title=s[3].replaceAll(",","");;
        this.country=s[4].replaceAll(",","");;
        this.releaseType=s[5].replaceAll(",","");;
        this.publisher=s[6].replaceAll(",","");;
        this.developer=s[7].replaceAll(",","");;
        this.createdAt=s[8].replaceAll(",","");;
        this.ownership=s[9].replaceAll(",","");;
        this.priceLoose=s[10].replaceAll(",","");;
        this.priceCIB=s[11].replaceAll(",","");;
        this.priceNew=s[12].replaceAll(",","");;
        this.yourPrice=s[13].replaceAll(",","");;
        this.pricePaid=s[14].replaceAll(",","");;
        this.itemCondition=s[15].replaceAll(",","");
        this.boxCondition=s[16].replaceAll(",","");
        this.manualCondition=s[17].replaceAll(",","");
        this.notes=s[18].replaceAll(",","");;
        this.tags=s[19].replaceAll(",","");;
    }

    public Game(Game game) {
        this.parameters.add("Platform");
        this.parameters.add("Category");
        this.parameters.add("userRecordType");
        this.parameters.add("title");
        this.parameters.add("country");
        this.parameters.add("releaseType");
        this.parameters.add("publisher");
        this.parameters.add("developer");
        this.parameters.add("createdAt");
        this.parameters.add("ownership");
        this.parameters.add("priceLoose");
        this.parameters.add("priceCIB");
        this.parameters.add("priceNew");
        this.parameters.add("yourPrice");
        this.parameters.add("pricePaid");
        this.parameters.add("itemCondition");
        this.parameters.add("boxCondition");
        this.parameters.add("manualCondition");
        this.parameters.add("notes");
        this.parameters.add("tags");
        this.platform=game.getPlatform();
        this.category=game.getCategory();
        this.userRecordType=game.getUserRecordType();
        this.title=game.getTitle();
        this.country=game.getCountry();
        this.releaseType=game.getReleaseType();
        this.publisher=game.getPublisher();
        this.developer=game.getDeveloper();
        this.createdAt=game.getCreatedAt();
        this.ownership=game.getOwnership();
        this.priceLoose=game.getPriceLoose();
        this.priceCIB=game.getPriceCIB();
        this.priceNew=game.getPriceNew();
        this.yourPrice=game.getYourPrice();
        this.pricePaid=game.getPricePaid();
        this.itemCondition=game.getItemCondition();
        this.boxCondition=game.getBoxCondition();
        this.manualCondition=game.getManualCondition();
        this.notes=game.getNotes();
        this.tags=game.getTags();
        this.parameterArray[0]=this.platform;
        this.parameterArray[1]=this.category;
        this.parameterArray[2]=this.userRecordType;
        this.parameterArray[3]=this.title;
        this.parameterArray[4]=this.country;
        this.parameterArray[5]=this.releaseType;
        this.parameterArray[6]=this.publisher;
        this.parameterArray[7]=this.developer;
        this.parameterArray[8]=this.createdAt;
        this.parameterArray[9]=this.ownership;
        this.parameterArray[10]=this.priceLoose;
        this.parameterArray[11]=this.priceCIB;
        this.parameterArray[12]=this.priceNew;
        this.parameterArray[13]=this.yourPrice;
        this.parameterArray[14]=this.pricePaid;
        this.parameterArray[15]=this.itemCondition;
        this.parameterArray[16]=this.boxCondition;
        this.parameterArray[17]=this.manualCondition;
        this.parameterArray[18]=this.notes;
        this.parameterArray[19]=this.tags;
    }

    public Game(String platform, String category, String userRecord, String title, String country, String releaseType, String publisher, String developer, String createdAt, String ownership, String priceLoose, String priceCIB, String priceNew, String yourPrice, String pricePaid, String itemCondition, String boxCondition, String manualCondition, String notes, String tags) {
        this.platform=platform.replace(",","");
        this.category=category.replace(",","");
        this.userRecordType=userRecord.replace(",","");
        this.title=title.replace(",","");
        this.country=country.replace(",","");
        this.releaseType=releaseType.replace(",","");
        this.publisher=publisher.replace(",","");
        this.developer=developer.replace(",","");
        this.createdAt=createdAt.replace(",","");
        this.ownership=ownership.replace(",","");
        this.priceLoose=priceLoose.replace(",","");
        this.priceCIB=priceCIB.replace(",","");
        this.priceNew=priceNew.replace(",","");
        this.yourPrice=yourPrice.replace(",","");
        this.pricePaid=pricePaid.replace(",","");
        this.itemCondition=itemCondition.replace(",","");
        this.boxCondition=boxCondition.replace(",","");
        this.manualCondition=manualCondition.replace(",","");
        this.notes=notes.replace(",","");
        this.tags=tags.replace(",","");
        this.parameters.add("Platform");
        this.parameters.add("Category");
        this.parameters.add("userRecordType");
        this.parameters.add("title");
        this.parameters.add("country");
        this.parameters.add("releaseType");
        this.parameters.add("publisher");
        this.parameters.add("developer");
        this.parameters.add("createdAt");
        this.parameters.add("ownership");
        this.parameters.add("priceLoose");
        this.parameters.add("priceCIB");
        this.parameters.add("priceNew");
        this.parameters.add("yourPrice");
        this.parameters.add("pricePaid");
        this.parameters.add("itemCondition");
        this.parameters.add("boxCondition");
        this.parameters.add("manualCondition");
        this.parameters.add("notes");
        this.parameters.add("tags");
        this.parameterArray[0]=this.platform;
        this.parameterArray[1]=this.category;
        this.parameterArray[2]=this.userRecordType;
        this.parameterArray[3]=this.title;
        this.parameterArray[4]=this.country;
        this.parameterArray[5]=this.releaseType;
        this.parameterArray[6]=this.publisher;
        this.parameterArray[7]=this.developer;
        this.parameterArray[8]=this.createdAt;
        this.parameterArray[9]=this.ownership;
        this.parameterArray[10]=this.priceLoose;
        this.parameterArray[11]=this.priceCIB;
        this.parameterArray[12]=this.priceNew;
        this.parameterArray[13]=this.yourPrice;
        this.parameterArray[14]=this.pricePaid;
        this.parameterArray[15]=this.itemCondition;
        this.parameterArray[16]=this.boxCondition;
        this.parameterArray[17]=this.manualCondition;
        this.parameterArray[18]=this.notes;
        this.parameterArray[19]=this.tags;
    }

    public String getGame() {
        return this.toString();
    }

    public Game getGameExact(){
        return this;
    }

    @Override
    public String toString() {
        return "" +
                "\"" + this.platform + "\"," +
                "\"" + this.category + "\"," +
                "\"" + this.userRecordType + "\"," +
                "\"" + this.title + "\"," +
                "\"" + this.country + "\"," +
                "\"" + this.releaseType + "\"," +
                "\"" + this.publisher + "\"," +
                "\"" + this.developer + "\"," +
                "\"" + this.createdAt + "\"," +
                "\"" + this.ownership + "\"," +
                "\"" + this.priceLoose + "\"," +
                "\"" + this.priceCIB + "\"," +
                "\"" + this.priceNew + "\"," +
                "\"" + this.yourPrice + "\"," +
                "\"" + this.pricePaid + "\"," +
                "\"" + this.itemCondition + "\"," +
                "\"" + this.boxCondition + "\"," +
                "\"" + this.manualCondition + "\"," +
                "\"" + this.notes + "\"," +
                "\"" + this.tags + "\"";
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserRecordType() {
        return userRecordType;
    }

    public void setUserRecordType(String userRecordType) {
        this.userRecordType = userRecordType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getPriceLoose() {
        return priceLoose;
    }

    public void setPriceLoose(String priceLoose) {
        this.priceLoose = priceLoose;
    }

    public String getPriceCIB() {
        return priceCIB;
    }

    public void setPriceCIB(String priceCIB) {
        this.priceCIB = priceCIB;
    }

    public String getPriceNew() {
        return priceNew;
    }

    public void setPriceNew(String priceNew) {
        this.priceNew = priceNew;
    }

    public String getYourPrice() {
        return yourPrice;
    }

    public void setYourPrice(String yourPrice) {
        this.yourPrice = yourPrice;
    }

    public String getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(String pricePaid) {
        this.pricePaid = pricePaid;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public String getBoxCondition() {
        return boxCondition;
    }

    public void setBoxCondition(String boxCondition) {
        this.boxCondition = boxCondition;
    }

    public String getManualCondition() {
        return manualCondition;
    }

    public void setManualCondition(String manualCondition) {
        this.manualCondition = manualCondition;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getParameter(int i) {
        return parameterArray[i];
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }
}