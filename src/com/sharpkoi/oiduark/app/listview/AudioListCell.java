package com.sharpkoi.oiduark.app.listview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.component.AudioTagBox;
import com.sharpkoi.oiduark.app.controller.AudioPageController;
import com.sharpkoi.oiduark.app.controller.AudioSetter;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.utils.ResourceLoader;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AudioListCell extends ListCell<Audio> {
	
	private GridPane container;
	
	private Button b_op;
	
	private Label l_title;
	
	private TilePane tagsContainer;
	
	@Override
	protected void updateItem(Audio item, boolean empty) {
		super.updateItem(item, empty);
		
		if(item == null) {
			setGraphic(null);
			setText("");
		}else {
			AudioPlayer player = Main.getInstance().getAudioPlayer();
			
			// Actually writing so much if-else is really not my will, 
			// but after checking, I found that it cannot be simplified :'(
			setOnMouseClicked(e -> {
				if(e.getButton().equals(MouseButton.PRIMARY)) {
					if(e.getClickCount() == 2) {
						if(player.getPlayList().contains(item)) {
							if(player.getCurrentAudio() != null) {
								if(player.getCurrentAudio().equals(item)) {
									player.pause();
									if(player.getPlayList().size() <= 1) {
										player.stop();
										AudioPageController.getInstance().removeAudioFromPlaylist(item);
									}else {
										AudioPageController.getInstance().removeAudioFromPlaylist(item);
										player.play();
									}
								}else {
									AudioPageController.getInstance().removeAudioFromPlaylist(item);
									((MaterialDesignIconView) b_op.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
								}
							}else {
								AudioPageController.getInstance().removeAudioFromPlaylist(item);
								((MaterialDesignIconView) b_op.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
							}
	 					}else {
							AudioPageController.getInstance().addAudioToPlaylist(item);
							((MaterialDesignIconView) b_op.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
						}
					}
				}else if(e.getButton().equals(MouseButton.SECONDARY)) {
					settingDialog(item);
					getListView().refresh();
				}
			});
			
			buildOPButton(item);
			buildTitleLabel(item.getTitle());
			buildTagsContainer(item);
			
			container = new GridPane();
			container.setAlignment(Pos.TOP_LEFT);
			container.getColumnConstraints().add(new ColumnConstraints(30));
			container.setVgap(5);
			container.add(b_op, 0, 0);
			container.add(l_title, 1, 0);
			container.add(tagsContainer, 1, 1);
			
			setGraphic(container);
			
//			selectedProperty().addListener((observable, o_select, n_select) -> {
//				if(n_select) {
//					this.setEffect(new Glow(0.4));
//				}else {
//					this.setEffect(null);
//				}
//			});
		}
	}
	
	private void buildOPButton(Audio item) {
		AudioPlayer player = Main.getInstance().getAudioPlayer();
		MaterialDesignIconView opIcon = new MaterialDesignIconView();
		
		opIcon.setGlyphSize(16);
		opIcon.setFill(Paint.valueOf("#fff"));
		
		b_op = new Button();
		b_op.setAlignment(Pos.CENTER);
		b_op.setPadding(Insets.EMPTY);
		b_op.setCursor(Cursor.HAND);
		b_op.setPrefSize(24, 24);
		
		b_op.setGraphic(opIcon);
		if(player.getPlayList().contains(item)) {
			((MaterialDesignIconView) b_op.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
		}else {
			((MaterialDesignIconView) b_op.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
		}
		
		b_op.getStyleClass().add("op-button");
		
		b_op.setOnAction(e -> {
			if(player.getPlayList().contains(item)) {
				if(player.getCurrentAudio() != null) {
					if(player.getCurrentAudio().equals(item)) {
						player.pause();
						if(player.getPlayList().size() <= 1) {
							player.stop();
							AudioPageController.getInstance().removeAudioFromPlaylist(item);
						}else {
							AudioPageController.getInstance().removeAudioFromPlaylist(item);
							player.play();
						}
					}else {
						getListView().getItems().remove(this.getIndex());
					}
				}else {
					AudioPageController.getInstance().removeAudioFromPlaylist(item);
					((MaterialDesignIconView) b_op.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
				}
			}else {
				AudioPageController.getInstance().addAudioToPlaylist(item);
				((MaterialDesignIconView) b_op.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
			}
		});
	}
	
	private void buildTitleLabel(String title) {
		l_title = new Label(title);
		l_title.setAlignment(Pos.CENTER_LEFT);
		l_title.setFont(Font.font(16));
		l_title.setTextFill(Paint.valueOf("white"));
	}
	
	private void buildTagsContainer(Audio audio) {
		Button addTagButton = new Button("·s¼W¼ÐÅÒ");
		
		addTagButton.getStyleClass().add("add-tag-button");
		addTagButton.setCursor(Cursor.HAND);
		addTagButton.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		addTagButton.setOnAction(e -> {
			List<String> allTags = Main.getInstance().getTagList();
			List<MenuItem> tags = new ArrayList<>();
			for(int i = 0; i < allTags.size(); i++) {
				if(!audio.getTags().contains(i)) {
					final int id = i;
					ObservableList<Node> ol_tagsContainer = tagsContainer.getChildren();
					
					AudioTagBox tagBox = new AudioTagBox(i);
					tagBox.setOnRemove(e1 -> {
						audio.getTags().remove(Integer.valueOf(id));
						ol_tagsContainer.remove(tagBox);
					});
					
					Label l = new Label(allTags.get(id));
					l.setPrefSize(100, USE_COMPUTED_SIZE);
					l.setAlignment(Pos.CENTER);
					
					MenuItem tagItem = new MenuItem();
					tagItem.setGraphic(l);
					tagItem.setOnAction(e2 -> {
						audio.getTags().add(id);
						ol_tagsContainer.add(ol_tagsContainer.size() - 1, tagBox);
					});
					tags.add(tagItem);
				}
			}
			ContextMenu menu = new ContextMenu(tags.toArray(new MenuItem[] {}));
			Bounds rect = addTagButton.localToScreen(addTagButton.getLayoutBounds());
			menu.show(
					Main.getInstance().getStage(), 
					rect.getMaxX(),
					rect.getMaxY()
			);
		});
		
		AudioTagBox[] tagBoxes = new AudioTagBox[audio.getTags().size()];
		for(int i = 0; i < audio.getTags().size(); i++) {
			tagBoxes[i] = new AudioTagBox(audio.getTags().get(i));
		}
		tagsContainer = new TilePane(5, 5, tagBoxes);
		tagsContainer.getChildren().add(addTagButton);
	}
	
	public void settingDialog(Audio audio) {
		Stage dialog = new Stage();
		dialog.initOwner(Main.getInstance().getStage());
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.setTitle(audio.getTitle());
		dialog.getIcons().add(ResourceLoader.loadAppIcon());
		
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/AudioSetting.fxml"));
		try {
			Parent dialogPane = loader.load();
			AudioSetter setter = loader.getController();
			
			setter.initByAudio(audio);
			
			setter.setOnSettingDone(() -> {
				HashMap<String, String> setting = setter.getResult();
				Audio item = getItem();
				item.setTitle(setting.getOrDefault("title", item.getTitle()));
				item.setAuthor(setting.getOrDefault("author", item.getAuthor()));
				item.setCoverPath(setting.getOrDefault("cover", item.getCoverPath()));
				if(setting.containsKey("lyrics")) {
					try {
						item.loadLyrics(new File(setting.get("lyrics")));
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				dialog.close();
			});
			
			dialog.setScene(new Scene(dialogPane));
			dialog.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
