package com.sharpkoi.oiduark.app.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioTag;
import com.sharpkoi.oiduark.utils.ResourceLoader;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AudioSettingDialog extends Stage implements OiDialog<Audio> {
	
	protected Audio audioCache;
	
	protected ImageView coverView;
	
	protected TextField f_title;
	protected TextField f_author;
	protected FlowPane audioTagsContainer;
	
	protected EventHandler<ActionEvent> onCancel;
	protected EventHandler<ActionEvent> onConfirm;
	
	public AudioSettingDialog(Audio audio) {
		super();
		
		this.audioCache = new Audio(
				audio.getFilePath(), 
				audio.getTitle(), 
				audio.getAuthor(), 
				audio.getCoverPath(), 
				audio.getDuration(), 
				audio.lastModified(), 
				audio.isFavorite());
		this.audioCache.getTagIDList().addAll(audio.getTagIDList());
		
		initOwner(Main.getInstance().getStage());
		initModality(Modality.APPLICATION_MODAL);
		
		getIcons().add(ResourceLoader.loadAppIcon());
		setTitle("OiDuark - " + audio.getTitle());
		setScene(buildScene());
	}
	
	public void setOnCancel(EventHandler<ActionEvent> task) {
		this.onCancel = task;
	}
	
	public void setOnConfirm(EventHandler<ActionEvent> task) {
		this.onConfirm = task;
	}
	
	@Override
	public Audio getResult() {
		audioCache.setTitle(f_title.getText());
		audioCache.setAuthor(f_author.getText());
		
		return audioCache;
	}

	@Override
	public Scene buildScene() {
		HBox infoBanner = buildInfoBanner();
		AnchorPane body = buildDialogBody();
		
		AnchorPane root = new AnchorPane(infoBanner, body);
		root.setBackground(new Background(new BackgroundFill(Paint.valueOf("#31393d"), null, null)));
		root.setPrefSize(720, 540);
		root.getStylesheets().add("com/sharpkoi/oiduark/app/style/audio-setting.css");
		
		AnchorPane.setTopAnchor(infoBanner, 0d);
		AnchorPane.setLeftAnchor(infoBanner, 0d);
		AnchorPane.setRightAnchor(infoBanner, 0d);
		
		AnchorPane.setTopAnchor(body, 200d);
		AnchorPane.setLeftAnchor(body, 0d);
		AnchorPane.setRightAnchor(body, 0d);
		AnchorPane.setBottomAnchor(body, 0d);
		
		return new Scene(root);
	}
	
	protected ScrollPane buildCoverPreview() {
		MaterialDesignIconView imageIcon = new MaterialDesignIconView(MaterialDesignIcon.IMAGE);
		imageIcon.setSize("48px");
		imageIcon.setFill(Paint.valueOf("white"));
		Button b_browseImage = new Button(null, imageIcon);
		b_browseImage.setFont(Font.font(1));
		b_browseImage.setAlignment(Pos.CENTER);
		b_browseImage.getStyleClass().add("image-browse-button");
		b_browseImage.setPadding(Insets.EMPTY);
		b_browseImage.setCursor(Cursor.HAND);
		b_browseImage.setOnAction(e -> {
			FileChooser imageChooser = new FileChooser();
			imageChooser.getExtensionFilters().add(new ExtensionFilter("Image File", "*.png", "*.jpg", "*.jpeg", "*.gif"));
			File f = imageChooser.showOpenDialog(getOwner());
			if(f != null) {
				try {
					coverView.setImage(new Image(new FileInputStream(f)));
					audioCache.setCoverPath(f.getAbsolutePath());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		BorderPane coverShade = new BorderPane(b_browseImage);
		coverShade.setBackground(new Background(new BackgroundFill(Paint.valueOf("#282c2e"), null, null)));
		coverShade.setPrefSize(200, 198);
		coverShade.getStyleClass().add("cover-shade");
		
		coverView = (audioCache.getCoverPath().equals("Unknown"))? new ImageView() : new ImageView("file:///" + audioCache.getCoverPath());
		coverView.setPreserveRatio(true);
		coverView.setFitHeight(198);
		
		StackPane stack = new StackPane(coverView, coverShade);
		stack.setAlignment(Pos.CENTER);
		stack.setPrefHeight(198);
		
		ScrollPane coverContainer = new ScrollPane(stack);
		coverContainer.setFocusTraversable(false);
		coverContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		coverContainer.setVbarPolicy(ScrollBarPolicy.NEVER);
		coverContainer.setHvalue(0.5); coverContainer.setVvalue(0.5);
		coverContainer.setPrefSize(200, 200);
		coverContainer.hvalueProperty().addListener((o, oldVal, newVal) -> {
			coverContainer.setHvalue(0.5);
		});
		
		return coverContainer;
	}
	
	protected VBox buildInfoBox() {
		Region r = new Region();
		r.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f05"), null, null)));
		r.setPrefWidth(10);
		f_title = new TextField(audioCache.getTitle());
		f_title.setFont(Font.font("System", FontWeight.BOLD, 20));
		f_title.setAlignment(Pos.CENTER_LEFT);
		f_title.setPrefWidth(500);
		HBox titleBox = new HBox(r, f_title);
		
		Label l_author = new Label("�@��");
		l_author.setFont(Font.font(16));
		l_author.setTextFill(Paint.valueOf("white"));
		l_author.setPrefWidth(40);
		f_author = new TextField(audioCache.getAuthor());
		f_author.setFont(Font.font(16));
		f_author.setAlignment(Pos.CENTER_LEFT);
		f_author.setPadding(new Insets(-1));
		f_author.setPrefWidth(450);
		HBox authorBox = new HBox(20, l_author, f_author);
		
		Label l_tag = new Label("����");
		l_tag.setFont(Font.font(16));
		l_tag.setTextFill(Paint.valueOf("white"));
		l_tag.setPrefWidth(40);
		audioTagsContainer = new FlowPane(
				Arrays.stream(audioCache.getTags()).map(tag -> tagTextForm(tag, 16)).toArray(Label[]::new));
		audioTagsContainer.setHgap(10); audioTagsContainer.setVgap(10);
		audioTagsContainer.setPrefWidth(450);
		HBox tagsBox = new HBox(20, l_tag, audioTagsContainer);
		tagsBox.setAlignment(Pos.TOP_LEFT);
		
		VBox infoBox = new VBox(20, titleBox, authorBox, tagsBox);
		infoBox.setAlignment(Pos.TOP_LEFT);
		infoBox.setPrefSize(510, 180);
		infoBox.setMaxHeight(180);
		
		return infoBox;
	}
	
	protected HBox buildInfoBanner() {
		HBox infoBanner = new HBox(10, buildCoverPreview(), buildInfoBox());
		infoBanner.setFocusTraversable(true);
		infoBanner.setAlignment(Pos.CENTER_LEFT);
		infoBanner.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2b3135"), null, null)));
		infoBanner.setEffect(new DropShadow(4.5, Color.BLACK));
		
		return infoBanner;
	}
	
	protected ScrollPane buildTagsContainer() {
		List<AudioTag> tags = Main.getInstance().getAudioTagManager().getAllTags();
		TilePane tagsTable = 
			new TilePane(tags.stream().map(tag -> {
					 Integer tagID = Integer.valueOf(tags.indexOf(tag));
					 Label l_tag = tagTextForm(tag, 16);
					 CheckBox tagCheckBox = new CheckBox();
					 tagCheckBox.setGraphic(l_tag);
					 tagCheckBox.setPrefWidth(100);
					 if(audioCache.getTagIDList().contains(tagID)) {
						 tagCheckBox.setSelected(true);
					 }
					 tagCheckBox.selectedProperty().addListener((o, oldVal, newVal) -> {
						 if(newVal) {
							 audioCache.getTagIDList().add(tagID);
						 }else {
							 audioCache.getTagIDList().remove(tagID);
						 }
						 audioTagsContainer.getChildren().setAll(
								 Arrays.stream(audioCache.getTags()).map(t -> tagTextForm(t, 16)).toArray(Label[]::new));
					 });
					 return tagCheckBox;
			}).toArray(CheckBox[]::new));
		
		tagsTable.setHgap(20);
		tagsTable.setVgap(10);
		tagsTable.setPrefWidth(700);
		
		ScrollPane tagsContainer = new ScrollPane(tagsTable);
		tagsContainer.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		return tagsContainer;
	}
	
	protected HBox buildButtonBox() {
		Button b_cancel = new Button("����");
		b_cancel.setFont(Font.font(14));
		b_cancel.getStyleClass().add("cancel-button");
		b_cancel.setPrefWidth(160);
		b_cancel.setOnAction(e -> {
			if(onCancel != null) onCancel.handle(e);
		});
		Button b_confirm = new Button("�M��");
		b_confirm.setFont(Font.font(14));
		b_confirm.getStyleClass().add("confirm-button");
		b_confirm.setPrefWidth(160);
		b_confirm.setOnAction(e -> {
			if(onConfirm != null) onConfirm.handle(e);
		});
		
		HBox buttonBox = new HBox(40, b_cancel, b_confirm);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		
		return buttonBox;
	}
	
	protected AnchorPane buildDialogBody() {
		Label l_allTags = new Label("�Ҧ�����");
		l_allTags.setTextFill(Paint.valueOf("white"));
		
		ScrollPane tagsContainer = buildTagsContainer();
		HBox buttonBox = buildButtonBox();
		
		AnchorPane body = new AnchorPane(l_allTags, tagsContainer, buttonBox);
		body.setFocusTraversable(true);
		body.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
		
		AnchorPane.setTopAnchor(l_allTags, 10d);
		AnchorPane.setLeftAnchor(l_allTags, 10d);
		
		AnchorPane.setTopAnchor(tagsContainer, 35d);
		AnchorPane.setLeftAnchor(tagsContainer, 10d);
		AnchorPane.setRightAnchor(tagsContainer, 10d);
		AnchorPane.setBottomAnchor(tagsContainer, 50d);
		
		AnchorPane.setLeftAnchor(buttonBox, 10d);
		AnchorPane.setRightAnchor(buttonBox, 10d);
		AnchorPane.setBottomAnchor(buttonBox, 10d);
		
		return body;
	}
	
	protected Label tagTextForm(AudioTag tag, double size) {
		Label l_tag = new Label(tag.getName());
		l_tag.setFont(Font.font(size));
		l_tag.setTextFill(tag.getColor());
		
		return l_tag;
	}
}
