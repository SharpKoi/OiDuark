package com.sharpkoi.oiduark.listview;

import java.util.ArrayList;
import java.util.List;

import com.sharpkoi.oiduark.OiDuarkApp;
import com.sharpkoi.oiduark.component.AudioTagBox;
import com.sharpkoi.oiduark.dialog.AudioSettingDialog;
import com.sharpkoi.oiduark.dialog.TagSettingDialog;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.audio.AudioTag;
import com.sharpkoi.oiduark.manager.AudioTagManager;
import com.sharpkoi.oiduark.utils.SimpleAnimation;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

public class AudioListCell extends ListCell<Audio> {

	public static final int DEFAULT_HEIGHT = 56;

	private GridPane container;

	private Button b_add;
	
	private HBox titleContainer;
	
	private FlowPane tagsContainer;

	// updateItem is called frequently even if it does not update item.
	// thus we use itemProperty listener to update item
	public void onItemUpdated(Audio item) {
		if(item == null) {
			setGraphic(null);
			setText("");
			setPrefHeight(DEFAULT_HEIGHT);
			return;
		}

		AudioPlayer player = OiDuarkApp.getInstance().getAudioPlayer();

		// Writing so much if-else is really not my will,
		// but after checking, I found that it's hard to be simplified :'(
		setOnMouseClicked(e -> {
			if(e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 2) {
				if(player.getPlayList().contains(item)) {
					((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
					if(player.getCurrentAudio() != null) {
						if(player.getCurrentAudio().equals(item)) {
							player.pause();
							if(player.getPlayList().size() <= 1) {
								player.stop();
								player.getPlayList().remove(item);
							}else {
								player.getPlayList().remove(item);
								player.play();
							}
						}else {
							player.getPlayList().remove(item);
						}
					}else {
						player.getPlayList().remove(item);
					}
				}else {
					player.addAudio(item);
					((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
				}
			}else if(e.getButton().equals(MouseButton.SECONDARY)) {
				AudioSettingDialog dialog = new AudioSettingDialog(item);
				dialog.show();
				dialog.setOnCancel(onCancel -> dialog.close());
				dialog.setOnConfirm(onConfirm -> {
					Audio.Metadata modified = dialog.getResult();
					item.updateMetadata(modified);
					// save changes
					OiDuarkApp.getInstance().getAudioManager().saveAllAudioData();
					dialog.close();
					getListView().refresh();
				});
			}
		});

		buildOPButton(item);
		buildTitleContainer(item);
		buildTagsContainer(item);

		container = new GridPane();
		container.setAlignment(Pos.TOP_LEFT);
		container.getColumnConstraints().add(new ColumnConstraints(30));
		container.setVgap(5);
		container.add(b_add, 0, 0);
		container.add(titleContainer, 1, 0);
		container.add(tagsContainer, 1, 1);

		setGraphic(container);
	}
	
	protected void buildOPButton(Audio item) {
		AudioPlayer player = OiDuarkApp.getInstance().getAudioPlayer();
		MaterialDesignIconView opIcon = new MaterialDesignIconView();
		
		opIcon.setGlyphSize(16);
		opIcon.setFill(Paint.valueOf("#fff"));
		
		b_add = new Button();
		b_add.setAlignment(Pos.CENTER);
		b_add.setPadding(Insets.EMPTY);
		b_add.setCursor(Cursor.HAND);
		b_add.setPrefSize(24, 24);
		
		b_add.setGraphic(opIcon);
		if(player.getPlayList().contains(item)) {
			((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
		}else {
			((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
		}
		
		b_add.getStyleClass().add("add-button");
		
		b_add.setOnAction(e -> {
			if(player.getPlayList().contains(item)) {
				((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.PLUS);
				if(player.getCurrentAudio() != null) {
					if(player.getCurrentAudio().equals(item)) {
						 player.pause();
						if(player.getPlayList().size() <= 1) {
							player.stop();
							player.getPlayList().remove(item);
						}else {
							player.getPlayList().remove(item);
							player.play();
						}
					}else {
						player.getPlayList().remove(item);
					}
				}else {
					player.getPlayList().remove(item);
				}
			}else {
				player.addAudio(item);
				((MaterialDesignIconView) b_add.getGraphic()).setIcon(MaterialDesignIcon.MINUS);
			}
		});
	}
	
	protected void buildTitleContainer(Audio item) {
		Label l_title = new Label(item.getTitle());
		l_title.setAlignment(Pos.CENTER_LEFT);
		l_title.setFont(Font.font(16));
		l_title.setTextFill(Paint.valueOf("white"));
		l_title.setTextOverrun(OverrunStyle.ELLIPSIS);
		
		SVGPath star = new SVGPath();
		star.setContent("M3.09,0 8.09,-2 13.09,0 12.735,-5.373 16.18,-9.51 10.959,-10.832 8.09,-15.39 5.221,-10.832 0,-9.51 3.445,-5.373");
		star.setFill(Paint.valueOf("gold"));
		Button b_star = new Button(null, star);
		b_star.setFont(Font.font(1));
		b_star.setPadding(Insets.EMPTY);
		b_star.setOpacity(item.isFavorite()? 1 : 0.3);
		b_star.setBackground(new Background(new BackgroundFill(Paint.valueOf("transparent"), null, null)));
		b_star.setCursor(Cursor.HAND);
		b_star.getStyleClass().add("star");
		b_star.setPrefSize(24, 24);
		b_star.setAccessibleHelp("我的最愛");
		b_star.setOnAction(e -> {
			item.getMetadata().setFavorite(!item.isFavorite());
			getListView().refresh();
			OiDuarkApp.getInstance().getStage().getScene().getRoot().requestFocus();
		});

//		JFXButton b_menu = new JFXButton();
//		b_menu.getStyleClass().add("audio-more-button");
//		FontAwesomeIconView b_menu_icon = new FontAwesomeIconView(FontAwesomeIcon.ELLIPSIS_V);
//		b_menu_icon.setFill(Paint.valueOf("white"));
//		b_menu.setGraphic(b_menu_icon);
//		b_menu.setTextFill(Paint.valueOf("white"));
//		b_menu.setText("");
//		b_menu.setMinWidth(28);

//		titleContainer = new HBox(b_star, l_title, b_menu);
		titleContainer = new HBox(b_star, l_title);
		titleContainer.setAlignment(Pos.CENTER_LEFT);
	}
	
	protected void buildTagsContainer(Audio audio) {
		Button addTagButton = new Button();
		addTagButton.getStyleClass().add("add-tag-button");
		addTagButton.setCursor(Cursor.HAND);
		Label l_addTag = new Label("+ tag");
		l_addTag.setTextFill(Paint.valueOf("white"));
		l_addTag.setFont(Font.font(12));
		addTagButton.setGraphic(l_addTag);
		addTagButton.setOnAction(e -> {
			AudioTagManager tagManager = OiDuarkApp.getInstance().getAudioTagManager();
			List<MenuItem> tagItems = new ArrayList<>();
			for(int i = 0; i < tagManager.getTagCount(); i++) {
				if(!audio.getTags().contains(i)) {
					final int id = i;
					
					Label l = new Label(tagManager.getAudioTag(id).getName());
					l.setPrefSize(100, USE_COMPUTED_SIZE);
					l.setAlignment(Pos.CENTER);
					
					MenuItem tagItem = new MenuItem();
					tagItem.setGraphic(l);
					tagItem.setOnAction(e1 -> {
						audio.getTags().add(id);
						getListView().refresh();
					});
					tagItems.add(tagItem);
				}
			}
			
			MenuItem newTagItem = new MenuItem();
			newTagItem.getStyleClass().add("new-tag-item");
			Label newTagLabel = new Label("+ new tag");
			newTagLabel.setPrefSize(100, USE_COMPUTED_SIZE);
			newTagLabel.setAlignment(Pos.CENTER);
			newTagItem.setGraphic(newTagLabel);
			
			newTagItem.setOnAction(e3 -> showNewTagDialog(audio));
			tagItems.add(newTagItem);
			
			ContextMenu menu = new ContextMenu(tagItems.toArray(new MenuItem[] {}));
			Bounds rect = addTagButton.localToScreen(addTagButton.getLayoutBounds());
			menu.show(
					OiDuarkApp.getInstance().getStage(),
					rect.getMaxX(),
					rect.getMaxY()
			);
		});
		
		AudioTagBox[] tagBoxes = new AudioTagBox[audio.getTags().size()];
		for(int i = 0; i < audio.getTags().size(); i++) {
			Integer tagID = audio.getTags().get(i);
			AudioTagBox tagBox = new AudioTagBox(tagID);
			
			tagBox.setOnRemove(e -> {
				audio.getTags().remove(tagID);
				tagsContainer.getChildren().remove(tagBox);
			});
			
			tagBox.setOnMouseClicked(e -> {
				if(e.getButton().equals(MouseButton.PRIMARY)) {
					TagSettingDialog dialog = new TagSettingDialog(tagBox.getTag().getName(), tagBox.getTag().getColor());
					dialog.show();
					SimpleAnimation.popup(dialog.getScene().getRoot());
					dialog.setOnConfirm(confirm -> {
						AudioTag tagRef = dialog.getResult();
						
						String tagName = tagRef.getName();
						String tagColor = tagRef.getColor().toString();
						
						tagBox.getTag().setName(tagName);
						tagBox.getTag().setColor(tagColor);
						tagBox.tagNameProperty().set(tagName);
						tagBox.tagColorProperty().set(Color.valueOf(tagColor));
						getListView().refresh();
					});
				}
			});
			
			tagBoxes[i] = tagBox;
		}
		
		tagsContainer = new FlowPane(5, 5, tagBoxes);
		tagsContainer.getChildren().add(addTagButton);
	}
	
	public void showNewTagDialog(Audio audio) {
		TagSettingDialog dialog = new TagSettingDialog();
		dialog.show();
		SimpleAnimation.popup(dialog.getScene().getRoot());
		dialog.setOnConfirm(e -> {
			AudioTagManager tm = OiDuarkApp.getInstance().getAudioTagManager();
			tm.createNewTag(dialog.getResult());
			audio.getTags().add(tm.getTagCount() -1);
			getListView().refresh();
		});
	}
}
