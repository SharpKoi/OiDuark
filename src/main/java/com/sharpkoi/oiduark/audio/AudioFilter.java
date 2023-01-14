package com.sharpkoi.oiduark.audio;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Predicate;

import javafx.collections.transformation.FilteredList;

public class AudioFilter {
	
	private boolean onlyStared;
	private String titleToSearch;
	private LinkedList<Integer> tagsToSearch;
	
	public AudioFilter() {
		this("");
	}
	
	public AudioFilter(String titleToSearch) {
		this(titleToSearch, new Integer[] {});
	}
	
	public AudioFilter(String titleToSearch, Integer... tagID) {
		this.titleToSearch = titleToSearch;
		tagsToSearch = new LinkedList<>();
		tagsToSearch.addAll(Arrays.asList(tagID));
	}
	
	public void setOnlyStared(boolean b) {
		this.onlyStared = b;
	}
	
	public boolean onlyStared() {
		return onlyStared;
	}
	
	public void searchTitle(String title) {
		this.titleToSearch = title.toLowerCase();
	}
	
	public void selectTag(Integer tagID) {
		this.tagsToSearch.add(tagID);
	}
	
	public void unselectTag(Integer tagID) {
		this.tagsToSearch.remove(tagID);
	}
	
	public Predicate<Audio> getPredicate() {
		Predicate<Audio> starFilter = Audio::isFavorite;
		Predicate<Audio> titleFilter = audio -> audio.getTitle().toLowerCase().contains(titleToSearch);
		Predicate<Audio> tagFilter = audio -> {
			if(tagsToSearch.contains(-1))
				return audio.getMetadata().getTags().size() == 0;
			else
				return audio.getMetadata().getTags().containsAll(tagsToSearch);
		};
		
		Predicate<Audio> filter = titleFilter.and(tagFilter);
		return onlyStared? filter.and(starFilter) : filter;
	}
	
	public void clear() {
		onlyStared = false;
		titleToSearch = "";
		tagsToSearch.clear();
	}
	
	public void search(FilteredList<Audio> audioList) {
		audioList.setPredicate(getPredicate());
	}
}
