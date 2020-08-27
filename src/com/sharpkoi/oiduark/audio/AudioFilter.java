package com.sharpkoi.oiduark.audio;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;

import javafx.collections.transformation.FilteredList;

public class AudioFilter {
	
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
		Predicate<Audio> titleFilter = audio -> audio.getTitle().contains(searchedTitle);
		Predicate<Audio> tagFilter = audio -> audio.getTags().containsAll(selectedTags);
		
		return titleFilter.and(tagFilter);
	}
	
	public void search(FilteredList<Audio> audioList) {
		audioList.setPredicate(getPredicate());
	}
}
