/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ProjectRoot;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Personal
 */
public class MainView extends Application {

    /**
     *
     */
    public static Stage stage;

    //this q maintains all downloads --- qwq
    //public static Queue<DownloadSerializable> dList;
    public static ArrayList<NewDownloader> dArray = new ArrayList<NewDownloader>();
    public static ObservableList<NewDownloader> data;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/mainStage.png")));
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        //creating a border pane
        BorderPane root = new BorderPane();

        VBox topContainer = new VBox();  //Creates a container to hold all Menu Objects.
        MenuBar mainMenu = new MenuBar();  //Creates our main menu to hold our Sub-Menus.
        ToolBar toolBar = new ToolBar();  //Creates our tool-bar to hold the buttons.

        //adding menu and toolbar to the top container
        topContainer.getChildren().add(mainMenu);
        topContainer.getChildren().add(toolBar);

        root.setTop(topContainer);

        //main menu starts
        //Declaring sub-menus and add to main menu.
        Menu tasks = new Menu("Tasks");
        Menu file = new Menu("File");
        Menu downloads = new Menu("Downloads");
        Menu help = new Menu("Help");

        //bulding the task menu
        MenuItem addUrl = new MenuItem("Add new download");
        MenuItem exit = new MenuItem("Exit");
        tasks.getItems().addAll(addUrl, exit);

        addUrl.setOnAction(mainHandler.addUrlHandler);

        exit.setOnAction((ActionEvent t) -> {
            stage.close();
        });

        //building the file menu
        MenuItem stop = new MenuItem("Stop");
        MenuItem remove = new MenuItem("Remove");
        MenuItem redownload = new MenuItem("Redownload");
        MenuItem deleteFile = new MenuItem("Delete downloaded file");
        file.getItems().addAll(stop, remove, redownload, deleteFile);

        //building the downloads menu
        MenuItem stopAll = new MenuItem("Stop all");
        MenuItem removeAllInactive = new MenuItem("Remove inactive downloads");
        downloads.getItems().addAll(stopAll, removeAllInactive);

        //building the help menu
        MenuItem contact = new MenuItem("Contact us");
        MenuItem about = new MenuItem("About IDM");
        help.getItems().addAll(contact, about);

        //add all to mainMenu
        mainMenu.getMenus().addAll(tasks, file, downloads, help);
        // root.getChildren().add(topContainer);

        //main Menu End
        //start making the toolbar
        Button addBtn = new Button();
        Button stopBtn = new Button();
        Button resumeBtn = new Button();
        Button stopAllBtn = new Button();
        Button redownloadBtn = new Button();
        Button clearBtn = new Button();
        Button clearAllBtn = new Button();
        Button deleteFileBtn = new Button();

        //Set the icon/graphic for the ToolBar Buttons.
        addBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/addUrl.png"), 40, 40, false, false)));
        addBtn.setContentDisplay(ContentDisplay.TOP);
        addBtn.setTooltip(new Tooltip("Add new download"));
        //button action

        stopBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/stop.png"), 40, 40, false, false)));
        stopBtn.setContentDisplay(ContentDisplay.TOP);
        stopBtn.setTooltip(new Tooltip("Stop"));

        resumeBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/resume.png"), 40, 40, false, false)));
        resumeBtn.setContentDisplay(ContentDisplay.TOP);
        resumeBtn.setTooltip(new Tooltip("Resume"));

        stopAllBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/stopAll.png"), 40, 40, false, false)));
        stopAllBtn.setContentDisplay(ContentDisplay.TOP);
        stopAllBtn.setTooltip(new Tooltip("Stop All"));

        redownloadBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/redownload.png"), 40, 40, false, false)));
        redownloadBtn.setContentDisplay(ContentDisplay.TOP);
        redownloadBtn.setTooltip(new Tooltip("Redownload"));

        clearBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/clear.png"), 40, 40, false, false)));
        clearBtn.setContentDisplay(ContentDisplay.TOP);
        clearBtn.setTooltip(new Tooltip("Clear"));

        clearAllBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/clearAll.png"), 40, 40, false, false)));
        clearAllBtn.setContentDisplay(ContentDisplay.TOP);
        clearAllBtn.setTooltip(new Tooltip("Clear inactive downloads"));

        deleteFileBtn.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/deleteFile.png"), 40, 40, false, false)));
        deleteFileBtn.setContentDisplay(ContentDisplay.TOP);
        deleteFileBtn.setTooltip(new Tooltip("Delete downloaded File"));

        //Add the Buttons to the ToolBar.
        toolBar.getItems().addAll(addBtn, stopBtn, resumeBtn, stopAllBtn, redownloadBtn, clearBtn, clearAllBtn, deleteFileBtn);

        //table of downloads
        TableView tv = new TableView();
        tv.setPrefSize(900, 600);

        TableColumn fileNameCol = new TableColumn();
        fileNameCol.setText("File Name");
        fileNameCol.setMinWidth(300);

        TableColumn statusCol = new TableColumn();
        statusCol.setText("Status");
        statusCol.setMinWidth(100);
        
        TableColumn percentCol = new TableColumn();
        percentCol.setText("Completed");
        percentCol.setMinWidth(100);

        TableColumn fullSizeCol = new TableColumn();
        fullSizeCol.setText("Full Size");
        fullSizeCol.setMinWidth(100);

        TableColumn downloadedSizeCol = new TableColumn();
        downloadedSizeCol.setText("Downloaded");
        downloadedSizeCol.setMinWidth(100);
        
        TableColumn urlLinkCol = new TableColumn();
        urlLinkCol.setText("Link used");
        urlLinkCol.setMinWidth(200);
        
        tv.getColumns().addAll(fileNameCol, statusCol, percentCol, fullSizeCol, downloadedSizeCol, urlLinkCol);

        data = FXCollections.observableArrayList(dArray);

        fileNameCol.setCellValueFactory(new PropertyValueFactory("fileName"));

        statusCol.setCellValueFactory(new PropertyValueFactory("status"));
        
        percentCol.setCellValueFactory(new PropertyValueFactory("percentage"));

        fullSizeCol.setCellValueFactory(new PropertyValueFactory("fullSize"));

        downloadedSizeCol.setCellValueFactory(new PropertyValueFactory("downloadedSize"));
        
        urlLinkCol.setCellValueFactory(new PropertyValueFactory("urlLink"));

        //set columns non-sortable
        fileNameCol.setSortable(false);
        statusCol.setSortable(false);
        percentCol.setSortable(false);
        fullSizeCol.setSortable(false);
        downloadedSizeCol.setSortable(false);
        urlLinkCol.setSortable(false);

        //tv.getColumns().addAll(fileNameCol);
        AnimationTimer at = new AnimationTimer() {
            @Override
            public void handle(long now) {

                tv.setItems(data);

                tv.refresh();
            }
        };
        at.start();

        topContainer.getChildren().add(tv);
        stage.setTitle("Internet Download Manager v1.0.2");

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);

        stage.show();

        stage.setOnCloseRequest((WindowEvent we) -> {
            saveBeforeExit();
        });

        //MenuItem actions
        stop.setOnAction(event -> {
            try {
                int idx = tv.getSelectionModel().getSelectedIndex();
                System.out.println(idx);
                NewDownloader n = dArray.get(idx);
                n.pause();
            } catch (Exception e) {

            }
        });
        remove.setOnAction((ActionEvent event) -> {
            try {
                int idx = tv.getSelectionModel().getSelectedIndex();
                System.out.println(idx);
                NewDownloader n = dArray.get(idx);

                //KAJ NA CHOLLE STAGE OFF THAKE -- TOKHON DELETE
                if (!n.HAS_A_STAGE) {
                    dArray.remove(n);
                    data = FXCollections.observableArrayList(dArray);
                }
            } catch (Exception e) {

            }
        });
        redownload.setOnAction((ActionEvent event) -> {
            try {
                int idx = tv.getSelectionModel().getSelectedIndex();
                System.out.println(idx);
                NewDownloader n = dArray.get(idx);
                n.redownload();
                if (!n.HAS_A_STAGE) {
                    DetailDownload detailDownload = new DetailDownload(n);
                    n.HAS_A_STAGE = true;
                }
            } catch (Exception e) {

            }
        });
        deleteFile.setOnAction((ActionEvent event) -> {
            try {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation of deletion");
                alert.setHeaderText("You are going to delete a file.");
                alert.setContentText("This will delete the downloaded file from your computer. Are you okay with this?");
                Stage st1 = (Stage) alert.getDialogPane().getScene().getWindow();
                st1.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/deleteFile.png")));

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    // ... user chose OK
                    int idx = tv.getSelectionModel().getSelectedIndex();
                    System.out.println(idx);
                    NewDownloader n = dArray.get(idx);
                    File f = new File(n.getPath());
                    //KAJ NA CHOLLE STAGE OFF THAKE -- TOKHON DELETE
                    if (!n.HAS_A_STAGE) {
                        dArray.remove(n);
                        data = FXCollections.observableArrayList(dArray);
                        f.delete();
                    }
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            } catch (Exception e) {

            }
        });
        stopAll.setOnAction((ActionEvent e) -> {
            dArray.forEach((n) -> {
                n.pause();
            });
        });
        removeAllInactive.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation of removal");
            alert.setHeaderText("You are going to clear the download list.");
            alert.setContentText("This will remove all inactive downloads from the list. Are you okay with this?");
            Stage st1 = (Stage) alert.getDialogPane().getScene().getWindow();
            st1.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/clearAll.png")));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                Iterator<NewDownloader> iter = dArray.iterator();

                while (iter.hasNext()) {
                    NewDownloader n = iter.next();

                    if (!n.HAS_A_STAGE) {
                        iter.remove();
                    }
                }
                data = FXCollections.observableArrayList(dArray);
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });
        contact.setOnAction((ActionEvent event) -> {
            try {
                Stage stageForInfo = new Stage();
                stageForInfo.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/info.png")));

                //make new stage as child of previous and prevent the parent from closing while the child is on
                stageForInfo.initModality(Modality.WINDOW_MODAL);
                stageForInfo.initOwner(MainView.stage);
                //make non-resizable
                stageForInfo.resizableProperty().setValue(Boolean.FALSE);
                Parent r = FXMLLoader.load(mainHandler.class.getClassLoader().getResource("ProjectRoot/TextContent/ContactUs.fxml"));
                Scene sc = new Scene(r);
                stageForInfo.setTitle("Contact us");
                stageForInfo.setScene(sc);
                stageForInfo.show();
            } catch (IOException ex) {
                //Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        about.setOnAction((ActionEvent event) -> {
            try {
                Stage stageForInfo = new Stage();
                stageForInfo.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/info.png")));

                //make new stage as child of previous and prevent the parent from closing while the child is on
                stageForInfo.initModality(Modality.WINDOW_MODAL);
                stageForInfo.initOwner(MainView.stage);
                //make non-resizable
                stageForInfo.resizableProperty().setValue(Boolean.FALSE);
                Parent r = FXMLLoader.load(mainHandler.class.getClassLoader().getResource("ProjectRoot/TextContent/AboutIDM.fxml"));
                Scene sc = new Scene(r);
                stageForInfo.setTitle("About Internet Download Manager");
                stageForInfo.setScene(sc);
                stageForInfo.show();
            } catch (IOException ex) {
                //Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //button actions
        addBtn.setOnAction(mainHandler.addUrlHandler);

        stopBtn.setOnAction(event -> {
            try {
                int idx = tv.getSelectionModel().getSelectedIndex();
                System.out.println(idx);
                NewDownloader n = dArray.get(idx);
                n.pause();
            } catch (Exception e) {

            }
        });
        resumeBtn.setOnAction((ActionEvent event) -> {
            try {
                int idx = tv.getSelectionModel().getSelectedIndex();
                System.out.println(idx);
                NewDownloader n = dArray.get(idx);
                n.resume();
                if (!n.HAS_A_STAGE) {
                    DetailDownload detailDownload = new DetailDownload(n);
                    n.HAS_A_STAGE = true;
                }
            } catch (Exception e) {

            }
        });
        stopAllBtn.setOnAction((ActionEvent e) -> {
            dArray.forEach((n) -> {
                n.pause();
            });
        });
        redownloadBtn.setOnAction((ActionEvent event) -> {
            try {
                int idx = tv.getSelectionModel().getSelectedIndex();
                System.out.println(idx);
                NewDownloader n = dArray.get(idx);
                n.redownload();
                if (!n.HAS_A_STAGE) {
                    DetailDownload detailDownload = new DetailDownload(n);
                    n.HAS_A_STAGE = true;
                }
            } catch (Exception e) {

            }
        });
        clearBtn.setOnAction((ActionEvent event) -> {
            try {
                int idx = tv.getSelectionModel().getSelectedIndex();
                System.out.println(idx);
                NewDownloader n = dArray.get(idx);

                //KAJ NA CHOLLE STAGE OFF THAKE -- TOKHON DELETE
                if (!n.HAS_A_STAGE) {
                    dArray.remove(n);
                    data = FXCollections.observableArrayList(dArray);
                }
            } catch (Exception e) {

            }
        });
        clearAllBtn.setOnAction((ActionEvent event) -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation of removal");
            alert.setHeaderText("You are going to clear the download list.");
            alert.setContentText("This will remove all inactive downloads from the list. Are you okay with this?");
            Stage st1 = (Stage) alert.getDialogPane().getScene().getWindow();
            st1.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/clearAll.png")));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                // ... user chose OK
                Iterator<NewDownloader> iter = dArray.iterator();

                while (iter.hasNext()) {
                    NewDownloader n = iter.next();

                    if (!n.HAS_A_STAGE) {
                        iter.remove();
                    }
                }
                data = FXCollections.observableArrayList(dArray);
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        });
        deleteFileBtn.setOnAction((ActionEvent event) -> {
            try {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation of deletion");
                alert.setHeaderText("You are going to delete a file.");
                alert.setContentText("This will delete the downloaded file from your computer. Are you okay with this?");
                Stage st1 = (Stage) alert.getDialogPane().getScene().getWindow();
                st1.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("ProjectRoot/BinaryContent/deleteFile.png")));

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    // ... user chose OK
                    int idx = tv.getSelectionModel().getSelectedIndex();
                    System.out.println(idx);
                    NewDownloader n = dArray.get(idx);
                    File f = new File(n.getPath());
                    //KAJ NA CHOLLE STAGE OFF THAKE -- TOKHON DELETE
                    if (!n.HAS_A_STAGE) {
                        dArray.remove(n);
                        data = FXCollections.observableArrayList(dArray);
                        f.delete();
                    }
                } else {
                    // ... user chose CANCEL or closed the dialog
                }

            } catch (Exception e) {

            }
        });
        //disable buttons when no row selected
        stopBtn.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        resumeBtn.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        redownloadBtn.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        clearBtn.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        deleteFileBtn.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        //disable menu items when no row selected
        stop.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        remove.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        redownload.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
        deleteFile.disableProperty().bind(Bindings.isEmpty(tv.getSelectionModel().getSelectedItems()));
//        //disable buttons when there is no element in the table
//        stopAllBtn.disableProperty().bind(Bindings.size(data).isEqualTo(0));
//        clearAllBtn.disableProperty().bind(Bindings.size(data).isEqualTo(0));
//        //disable menu items when there is no element in the table
//        stopAll.disableProperty().bind(Bindings.size(data).isEqualTo(0));
//        removeAllInactive.disableProperty().bind(Bindings.size(data).isEqualTo(0));

    }

    public static void saveBeforeExit() {
        //serialize the List
        //note the use of abstract base class references
        try {
            //use buffering
            OutputStream file = new FileOutputStream("D:\\IDM_data\\DownloadList.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            try {
                output.writeObject(dArray);
            } finally {
                output.close();
            }
        } catch (IOException ex) {
            //fLogger.log(Level.SEVERE, "Cannot perform output.", ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //deserialize the DownloadList.ser file
        //note the use of abstract base class references
        
        //File f = new File("D:\\IDM_data\\DownloadList.ser");
        
        File f = new File("D:\\IDM_data\\DownloadList.ser");
        f.getParentFile().mkdir();
        try {
            f.createNewFile();
        } catch (IOException ex) {
            //Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            f.createNewFile();
        } catch (IOException ex) {
            //Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        if (f.length() > 0) {
            try {
                //use buffering
                InputStream file = new FileInputStream("D:\\IDM_data\\DownloadList.ser");
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);
                try {
                    //deserialize the List
                    //List<String> recoveredQuarks = (List<String>) input.readObject();
                    dArray = (ArrayList<NewDownloader>) input.readObject();
                } finally {
                    input.close();
                }
            } catch (ClassNotFoundException ex) {
                //fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
            } catch (IOException ex) {
                //fLogger.log(Level.SEVERE, "Cannot perform input.", ex);
            }

            //testing if recovered 
//            for (NewDownloader nd : dArray) {
//                nd.resume();
//                synchronized (nd) {
//                    try {
//                        System.out.println("Waiting for b to complete...");
//                        nd.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("Wait is over");
//                }
//
//                DetailDownload detailDownload = new DetailDownload(nd);
//            }
        }
        launch(args);
    }
    //private static final Logger fLogger
         //   = Logger.getLogger(MainView.class.getPackage().getName());
}
