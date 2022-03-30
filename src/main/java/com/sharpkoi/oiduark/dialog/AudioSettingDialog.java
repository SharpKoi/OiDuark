package com.sharpkoi.oiduark.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import com.sharpkoi.oiduark.OiDuarkApp;
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
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AudioSettingDialog extends Stage implements OiDialog<Audio.Metadata> {
	
	protected Audio.Metadata metadataCache;

	protected ImageView coverView;
	
	protected TextField f_title;
	protected TextField f_author;
	protected FlowPane audioTagsContainer;
	
	protected EventHandler<ActionEvent> onCancel;
	protected EventHandler<ActionEvent> onConfirm;
	
	public AudioSettingDialog(Audio audio) {
		super();

		// copy the metadata
		this.metadataCache = audio.getMetadata().toBuilder().build();
		
		initOwner(OiDuarkApp.getInstance().getStage());
		initModality(Modality.APPLICATION_MODAL);
		
		getIcons().add(OiDuarkApp.getInstance().getResourceLoader().loadAppIcon());
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
	public Audio.Metadata getResult() {
		return metadataCache;
	}

	@Override
	public Scene buildScene() {
		HBox infoBanner = buildInfoBanner();
		AnchorPane body = buildDialogBody();
		
		AnchorPane root = new AnchorPane(infoBanner, body);
		root.setBackground(new Background(new BackgroundFill(Paint.valueOf("#31393d"), null, null)));
		root.setPrefSize(720, 540);
		root.getStylesheets().add(OiDuarkApp.getInstance().getResourceLoader().getResourceURL("styles/audio-setting.css").toString());
		
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
					metadataCache.setCoverPath(f.getAbsolutePath());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		BorderPane coverShade = new BorderPane(b_browseImage);
		coverShade.setBackground(new Background(new BackgroundFill(Paint.valueOf("#282c2e"), null, null)));
		coverShade.setPrefSize(200, 198);
		coverShade.getStyleClass().add("cover-shade");
		
		coverView = (metadataCache.getCoverPath().equals("Unknown"))? new ImageView() : new ImageView("file:///" + metadataCache.getCoverPath());
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
		coverContainer.hvalueProperty().addListener((o, oldVal, newVal) -> coverContainer.setHvalue(0.5));
		
		return coverContainer;
	}
	
	protected VBox buildInfoBox() {
		Region r = new Region();
		r.setBackground(new Background(new BackgroundFill(Paint.valueOf("#f05"), null, null)));
		r.setPrefWidth(10);
		f_title = new TextField(this.metadataCache.getTitle());
		f_title.setFont(Font.font("System", FontWeight.BOLD, 20));
		f_title.setAlignment(Pos.CENTER_LEFT);
		f_title.setPrefWidth(500);
		HBox titleBox = new HBox(r, f_title);
		
		Label l_author = new Label("作者");
		l_author.setFont(Font.font(16));
		l_author.setTextFill(Paint.valueOf("white"));
		l_author.setPrefWidth(40);
		f_author = new TextField(this.metadataCache.getAuthor());
		f_author.setFont(Font.font(16));
		f_author.setAlignment(Pos.CENTER_LEFT);
		f_author.setPadding(new Insets(-1));
		f_author.setPrefWidth(450);
		HBox authorBox = new HBox(20, l_author, f_author);
		
		Label l_tag = new Label("標籤");
		l_tag.setFont(Font.font(16));
		l_tag.setTextFill(Paint.valueOf("white"));
		l_tag.setPrefWidth(40);
		audioTagsContainer = new FlowPane(
				metadataCache.getTags().stream().map(tagId -> {
					AudioTag tag = OiDuarkApp.getInstance().getAudioTagManager().getAudioTag(tagId);
					return tagTextForm(tag);
				}).toArray(Label[]::new));
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
		List<AudioTag> allTags = OiDuarkApp.getInstance().getAudioTagManager().getAllTags();
		TilePane tagsTable = 
			new TilePane(allTags.stream().map(tag -> {
					 // tagId is the ID of the iterated tag from all tags.
					 Integer tagId = allTags.indexOf(tag);
					 Label l_tag = tagTextForm(tag);
					 CheckBox tagCheckBox = new CheckBox();
					 tagCheckBox.setGraphic(l_tag);
					 tagCheckBox.setPrefWidth(100);
					 if(metadataCache.getTags().contains(tagId)) {
						 tagCheckBox.setSelected(true);
					 }
					 tagCheckBox.selectedProperty().addListener((o, oldVal, newVal) -> {
						 if(newVal) {
							 metadataCache.getTags().add(tagId);
						 }else {
							 metadataCache.getTags().remove(tagId);
						 }
						 audioTagsContainer.getChildren().setAll(
								 metadataCache.getTags().stream().map(_tagId -> {
									 // `_tagId` is the ID of the tag audio currently have. Which is different from `tagId` outside.
									 AudioTag t = OiDuarkApp.getInstance().getAudioTagManager().getAudioTag(_tagId);
									 return tagTextForm(t);
								 }).toArray(Label[]::new));
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
		Button b_cancel = new Button("取消");
		b_cancel.setFont(Font.font(14));
		b_cancel.getStyleClass().add("cancel-button");
		b_cancel.setPrefWidth(160);
		b_cancel.setOnAction(e -> {
			if(onCancel != null) onCancel.handle(e);
		});
		Button b_confirm = new Button("套用");
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
		Label l_allTags = new Label("所有標籤");
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
	
	protected Label tagTextForm(AudioTag tag) {
		Label l_tag = new Label(tag.getName());
		l_tag.setFont(Font.font(16));
		l_tag.setTextFill(tag.getColor());
		
		return l_tag;
	}
}
