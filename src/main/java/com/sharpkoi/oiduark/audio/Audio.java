package com.sharpkoi.oiduark.audio;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.annotations.SerializedName;
import lombok.*;

@Getter
public class Audio {
	private Metadata metadata;
	private LinkedHashMap<Integer, String> timeLyrics = new LinkedHashMap<>();

	private Audio(Metadata metadata) {
		this.metadata = metadata;
	}

	public static Audio fromMetadata(@NonNull Metadata metadata) {
		Audio audio = new Audio(metadata);
		try {
			String lyricsFilePath = metadata.getLyricsFilePath();
			if (!lyricsFilePath.equals("Unknown")) {
				audio.loadLyrics(new File(lyricsFilePath));
			}

			return audio;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			audio.getMetadata().setLyricsFilePath("Unknown");
		}

		return audio;
	}

	public void updateMetadata(Metadata newData) {
		assert metadata.equals(newData): "Updating metadata with a different audio filepath is not allowed.";

		metadata =
				metadata.toBuilder()
						.lastModified(new Date())
						.title(newData.getTitle())
						.author(newData.getAuthor())
						.coverPath(newData.getCoverPath())
						.lyricsFilePath(newData.getLyricsFilePath())
						.tags(newData.getTags())
						.favorite(newData.isFavorite()).build();
	}
	
	public void loadLyrics(File lyricsFile) throws FileNotFoundException {
		if(!this.metadata.getLyricsFilePath().equals(lyricsFile.getAbsolutePath())) {
			this.metadata.setLyricsFilePath(lyricsFile.getAbsolutePath());
		}

		InputStream in = new FileInputStream(lyricsFile);
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
			String line;
			while((line = reader.readLine()) != null) {
				int segIndex = line.indexOf(" ");
				timeLyrics.put(Integer.valueOf(line.substring(0, segIndex)), line.substring(segIndex).trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFilePath() {
		return metadata.getFilePath();
	}

	public Date getLastModified() {
		return metadata.getLastModified();
	}

	public double getDuration() {
		return metadata.getDuration();
	}

	public boolean isFavorite() {
		return metadata.isFavorite();
	}

	public String getTitle() {
		return metadata.getTitle();
	}

	public String getAuthor() {
		return metadata.getAuthor();
	}

	public String getCoverPath() {
		return metadata.getCoverPath();
	}

	public String getLyricsFilePath() {
		return metadata.getLyricsFilePath();
	}

	public List<Integer> getTags() {
		return metadata.getTags();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Audio) {
			return getMetadata().equals(((Audio) obj).getMetadata());
		}
		return false;
	}

	/**
	 * This class presents the metadata of an audio.
	 * Note that the filePath inside is immutable.
	 */
	@Data
	@Builder(toBuilder = true)
	@AllArgsConstructor
	public static class Metadata {
		@SerializedName(value = "source", alternate = {"file", "filepath"})
		private @NonNull String filePath;

		private String title;
		private String author = "Unknown";
		private double duration = 0.0;
		private boolean favorite = false;
		private LinkedList<Integer> tags = new LinkedList<>();

		@SerializedName(value = "cover")
		private String coverPath = "Unknown";
		@SerializedName(value = "lyrics_file")
		private String lyricsFilePath = "Unknown";
		@SerializedName(value = "last_modified")
		private Date lastModified;

		@Builder
		public Metadata(@NonNull String filePath) {
			this.filePath = filePath;
			this.title = Paths.get(filePath).getFileName().toString();
			this.lastModified = new Date();
		}
	}
}
