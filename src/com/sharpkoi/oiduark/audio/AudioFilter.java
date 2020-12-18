package com.sharpkoi.oiduark.audio;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;

import javafx.collections.transformation.FilteredList;

public class AudioFilter {
	
	private boolean onlyStared;
	private String searchedTitle;
	private LinkedList<Integer> selectedTags;
	
	public AudioFilter() {
		this("");
	}
	
	public AudioFilter(String searchedTitle) {
		this(searchedTitle, new Integer[] {});
	}
	
	public AudioFilter(String searchedTitle, Integer... tagID) {
		this.searchedTitle = searchedTitle;
		selectedTags = new LinkedList<>();
		selectedTags.addAll(Arrays.asList(tagID));
	}
	
	public void setOnlyStared(boolean b) {
		this.onlyStared = b;
	}
	
	public boolean onlyStared() {
		return onlyStared;
	}
	
	public void searchTitle(String title) {
		this.searchedTitle = title;
	}
	
	public void selectTag(Integer tagID) {
		this.selectedTags.add(tagID);
	}
	
	public void unselectTag(Integer tagID) {
		this.selectedTags.remove(tagID);
	}
	
	public Predicate<Audio> getPredicate() {
		Predicate<Audio> starFilter = audio -> audio.isFavorite();
		Predicate<Audio> titleFilter = audio -> audio.getTitle().contains(searchedTitle);
		Predicate<Audio> tagFilter = audio -> audio.getTagIDList().containsAll(selectedTags);
		
		Predicate<Audio> filter = titleFilter.and(tagFilter);
		return onlyStared? filter.and(starFilter) : filter;
	}
	
	public void clear() {
		onlyStared = false;
		searchedTitle = "";
		selectedTags.clear();
	}
	
	public void search(FilteredList<Audio> audioList) {
		audioList.setPredicate(getPredicate());
	}
}
