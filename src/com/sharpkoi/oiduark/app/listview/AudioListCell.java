package com.sharpkoi.oiduark.app.listview;

import java.util.ArrayList;
import java.util.List;

import com.sharpkoi.oiduark.app.Main;
import com.sharpkoi.oiduark.app.component.AudioTagBox;
import com.sharpkoi.oiduark.app.dialog.AudioSettingDialog;
import com.sharpkoi.oiduark.app.dialog.TagSettingDialog;
import com.sharpkoi.oiduark.audio.Audio;
import com.sharpkoi.oiduark.audio.AudioPlayer;
import com.sharpkoi.oiduark.audio.AudioTag;
import com.sharpkoi.oiduark.audio.AudioTagManager;
import com.sharpkoi.oiduark.utils.SimpleAnimation;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

public class AudioListCell extends ListCell<Audio> {
	
	private GridPane container;
	
	private Button b_add;
	
	private HBox titleContainer;
	
	private FlowPane tagsContainer;
	
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
					}
				}else if(e.getButton().equals(MouseButton.SECONDARY)) {
					AudioSettingDialog dialog = new AudioSettingDialog(item);
					dialog.show();
					dialog.setOnCancel(onCancel -> {
						dialog.close();
					});
					dialog.setOnConfirm(onConfirm -> {
						Audio modified = dialog.getResult();
						item.setTitle(modified.getTitle());
						item.setAuthor(modified.getAuthor());
						item.setCoverPath(modified.getCoverPath());
						item.getTagIDList().clear();
						item.getTagIDList().addAll(modified.getTagIDList());
						
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
	}
	
	protected void buildOPButton(Audio item) {
		AudioPlayer player = Main.getInstance().getAudioPlayer();
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
			item.setFavorite(!item.isFavorite());
			getListView().refresh();
			Main.getInstance().getStage().getScene().getRoot().requestFocus();
		});
		
		titleContainer = new HBox(b_star, l_title);
		titleContainer.setAlignment(Pos.CENTER_LEFT);
	}
	
	protected void buildTagsContainer(Audio audio) {
		Button addTagButton = new Button("新增標籤");
		addTagButton.getStyleClass().add("add-tag-button");
		addTagButton.setCursor(Cursor.HAND);
		addTagButton.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
		addTagButton.setOnAction(e -> {
			AudioTagManager tagManager = Main.getInstance().getAudioTagManager();
			List<MenuItem> tagItems = new ArrayList<>();
			for(int i = 0; i < tagManager.getTagCount(); i++) {
				if(!audio.getTagIDList().contains(i)) {
					final int id = i;
					
					Label l = new Label(tagManager.getAudioTag(id).getName());
					l.setPrefSize(100, USE_COMPUTED_SIZE);
					l.setAlignment(Pos.CENTER);
					
					MenuItem tagItem = new MenuItem();
					tagItem.setGraphic(l);
					tagItem.setOnAction(e1 -> {
						audio.getTagIDList().add(id);
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
			
			newTagItem.setOnAction(e3 -> {
				showNewTagDialog(audio);
			});
			tagItems.add(newTagItem);
			
			ContextMenu menu = new ContextMenu(tagItems.toArray(new MenuItem[] {}));
			Bounds rect = addTagButton.localToScreen(addTagButton.getLayoutBounds());
			menu.show(
					Main.getInstance().getStage(), 
					rect.getMaxX(),
					rect.getMaxY()
			);
		});
		
		AudioTagBox[] tagBoxes = new AudioTagBox[audio.getTagIDList().size()];
		for(int i = 0; i < audio.getTagIDList().size(); i++) {
			final int index = i;
			Integer tagID = Integer.valueOf(audio.getTagIDList().get(index));
			AudioTagBox tagBox = new AudioTagBox(tagID.intValue());
			
			tagBox.setOnRemove(e -> {
				audio.getTagIDList().remove(tagID);
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
			AudioTagManager tm = Main.getInstance().getAudioTagManager();
			tm.createNewTag(dialog.getResult());
			audio.getTagIDList().add(tm.getTagCount() -1);
			getListView().refresh();
		});
	}
}
