import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Simulation of audio content in an online store
// The songs, podcasts, audiobooks listed here can be "downloaded" to your library

public class AudioContentStore
{
		private ArrayList<AudioContent> contents; 
		private Map<String, Integer> titleMap;
		private Map<String, ArrayList<Integer>> artistMap;
		private Map<String, ArrayList<Integer>> genreMap;

		public AudioContentStore()
		{
			contents = new ArrayList<AudioContent>();

			// Create a new HashMap for titles
			titleMap = new HashMap<>();
			// Create a new HashMap for artists
			artistMap = new HashMap<>();
			// Create a new HashMap for genres
			genreMap = new HashMap<>();
			
			// Read store.txt and create AudioContent objects based on its content
			try {
				BufferedReader reader = new BufferedReader(new FileReader("store.txt"));
				String line;
	
				while ((line = reader.readLine()) != null) {
					String contentType = line.trim();
	
					if (contentType.equals("SONG")) {
					// Read and parse the lines for a Song object
					String id = reader.readLine().trim();
					String title = reader.readLine().trim();
					int year = Integer.parseInt(reader.readLine().trim());
					int rating = Integer.parseInt(reader.readLine().trim());
					String artist = reader.readLine().trim();
					String songwriter = reader.readLine().trim();
					Song.Genre genre = Song.Genre.valueOf(reader.readLine().trim());
					int numLines = Integer.parseInt(reader.readLine().trim());
					StringBuilder lyrics = new StringBuilder();
					for (int i = 0; i < numLines; i++) {
						lyrics.append(reader.readLine()).append("\n");
					}
					// Create a new Song object and add it to the contents list
					Song song = new Song(title, year, id, Song.TYPENAME, lyrics.toString(), rating, artist, songwriter, genre, lyrics.toString());
					contents.add(song);
				} else if (contentType.equals("AUDIOBOOK")) {
					// Read and parse the lines for an AudioBook object
					String id = reader.readLine().trim();
					String title = reader.readLine().trim();
					int year = Integer.parseInt(reader.readLine().trim());
					int length = Integer.parseInt(reader.readLine().trim());
					String author = reader.readLine().trim();
					String narrator = reader.readLine().trim();
					int numberOfChapters = Integer.parseInt(reader.readLine().trim());

					ArrayList<String> chapterTitles = new ArrayList<>();
					ArrayList<String> chapters = new ArrayList<>();

					// Read chapter titles
					for (int i = 0; i < numberOfChapters; i++) {
						String chapterTitle = reader.readLine().trim(); 
						chapterTitles.add(chapterTitle);
					}

					// Read chapter content
					for (int i = 0; i < numberOfChapters; i++) {
						String chapter = reader.readLine().trim();
						chapters.add(chapter);
					}

					// Create a new AudioBook object and add it to the contents list
					AudioBook audioBook = new AudioBook(title, year, id, "AUDIOBOOK", "", length, author, narrator, chapterTitles, chapters);
					contents.add(audioBook);
				}
			}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
			// Build the maps based on the contents ArrayList
			for (int i = 0; i < contents.size(); i++) {
				AudioContent content = contents.get(i);

				// Add content to the title map
				titleMap.put(content.getTitle(), i);

				// Add content to the artist map
				String artistKey = content.getArtist();
				if (!artistMap.containsKey(artistKey)) {
					artistMap.put(artistKey, new ArrayList<>());
				}
				artistMap.get(artistKey).add(i);

				// Add content to the genre map
				if (content instanceof Song) {
					String genreKey = ((Song) content).getGenre().toString();
					if (!genreMap.containsKey(genreKey)) {
						genreMap.put(genreKey, new ArrayList<>());
					}
					genreMap.get(genreKey).add(i);
				}
			}
		}
		// Define a method to search for AudioContent objects that contain a given string in their title, artist, lyrics or chapters
		public ArrayList<AudioContent> searchp(String targetString) {
			ArrayList<AudioContent> results = new ArrayList<>();

			// Iterate over all AudioContent objects in the contents ArrayList and check if they match the search criteria
			for (AudioContent audioContent : contents) {
				if (audioContent.getTitle().toLowerCase().contains(targetString.toLowerCase())
						|| audioContent.getArtist().toLowerCase().contains(targetString.toLowerCase())
						|| (audioContent instanceof Song && ((Song) audioContent).getLyrics().toLowerCase().contains(targetString.toLowerCase()))
						|| (audioContent instanceof AudioBook && ((AudioBook) audioContent).getChapters().toString().toLowerCase().contains(targetString.toLowerCase()))) {
					results.add(audioContent);
				}
			}
			// Return the results ArrayList
			return results;
		}
		
		// This method returns the index of the given AudioContent in the contents list
		public int getIndexOfContent(AudioContent content) {
			return contents.indexOf(content);
		}
		
		// This method returns the AudioContent object at the given index in the contents list
		public AudioContent getAudioContent(int index) {
			return contents.get(index);
		}

		// This method searches the titleMap for the index of the AudioContent with the given title, and returns -1 if not found
		public int getIndexFromTitle(String title) {
			if (titleMap.containsKey(title)) {
				return titleMap.get(title);
			}
			return -1;
		}
		
		// This method searches the titleMap for the AudioContent with the given title, and returns null if not found
		public AudioContent searchTitle(String title) {
			int index = getIndexFromTitle(title);
			if (index != -1) {
				return contents.get(index);
			}
			return null;
		}
		
		// Add the index of an AudioContent object to the artistMap
		public void addIndexToArtistMap(String artist, int index) {
			artistMap.computeIfAbsent(artist, k -> new ArrayList<>()).add(index);
		}

		// Retrieve AudioContent objects by artist name from artistMap
		public ArrayList<AudioContent> getAudioContentByArtist(String artist) {
			ArrayList<Integer> ids = artistMap.getOrDefault(artist, new ArrayList<>());
			ArrayList<AudioContent> contentsByArtist = new ArrayList<>();
			
			for (Integer id : ids) {
				contentsByArtist.add(contents.get(id));
			}
			
			return contentsByArtist;
		}

		// Retrieve all songs of a specific genre
		public ArrayList<AudioContent> downloadSongsByGenre(String genre) {
			ArrayList<Integer> ids = genreMap.getOrDefault(genre, new ArrayList<>());
			ArrayList<AudioContent> songsByGenre = new ArrayList<>();
			
			for (Integer id : ids) {
				songsByGenre.add(contents.get(id));
			}
			
			return songsByGenre;
		}

		
		public void addAudioContent(AudioContent audioContent) {
			// Add the audioContent to the contents list
			contents.add(audioContent);
			// Add the audioContent's title and index to the titleMap
			titleMap.put(audioContent.getTitle(), contents.size() - 1);

			// Adding/updating the artistMap
			String artist = audioContent.getArtist();
			if (artistMap.containsKey(artist)) {
				artistMap.get(artist).add(contents.size() - 1);
			} else {
				ArrayList<Integer> newList = new ArrayList<>();
				newList.add(contents.size() - 1);
				artistMap.put(artist, newList);
			}
			 // Adding/updating the genreMap (only for Song instances)
			 if (audioContent instanceof Song) {
				String genre = ((Song) audioContent).getGenre().toString();
				ArrayList<Integer> indices = genreMap.getOrDefault(genre, new ArrayList<>());
				indices.add(contents.size() - 1);
				genreMap.put(genre, indices);
			}
		}

		// This method searches for audio content based on the artist's name.
		// It returns an ArrayList of all audio content that match the artist.
		public ArrayList<AudioContent> searchArtist(String artist) {
			ArrayList<AudioContent> results = new ArrayList<>();
			if (artistMap.containsKey(artist)) {
				ArrayList<Integer> indices = artistMap.get(artist);
				for (Integer index : indices) {
					results.add(contents.get(index));
				}
			}
			return results;
		}
		
		// This method searches for AudioContent objects by their genre
		// It takes a genre string as input and returns an ArrayList of AudioContent objects with that genre
		public ArrayList<AudioContent> searchByGenre(String genre) {
			ArrayList<AudioContent> results = new ArrayList<>();
			if (genreMap.containsKey(genre)) {
				ArrayList<Integer> indices = genreMap.get(genre);
				for (Integer index : indices) {
					results.add(contents.get(index));
				}
			}
			return results;
		}

		public AudioContent getContent(int index)
		{
			if (index < 1 || index > contents.size())
			{
				return null;
			}
			return contents.get(index-1);
		}
		
		public void listAll()
		{
			for (int i = 0; i < contents.size(); i++)
			{
				int index = i + 1;
				System.out.print(index + ". ");
				contents.get(i).printInfo();
				System.out.println();
			}
		}
		
		private ArrayList<String> makeHPChapterTitles()
		{
			ArrayList<String> titles = new ArrayList<String>();
			titles.add("The Riddle House");
			titles.add("The Scar");
			titles.add("The Invitation");
			titles.add("Back to The Burrow");
			return titles;
		}
		
		private ArrayList<String> makeHPChapters()
		{
			ArrayList<String> chapters = new ArrayList<String>();
			chapters.add("In which we learn of the mysterious murders\n"
					+ "in the Riddle House fifty years ago, \n"
					+ "how Frank Bryce was accused but released for lack of evidence, \n"
					+ "and how the Riddle House fell into disrepair. ");
			chapters.add("In which Harry awakens from a bad dream, \n"
					+ "his scar burning, we recap Harry�s previous adventures, \n"
					+ "and he writes a letter to his godfather.");
			chapters.add("In which Dudley and the rest of the Dursleys are on a diet,\n"
					+ "and the Dursleys get letter from Mrs. Weasley inviting Harry to stay\n"
					+ "with her family and attend the World Quidditch Cup finals.");
			chapters.add("In which Harry awaits the arrival of the Weasleys, \n"
					+ "who come by Floo Powder and get trapped in the blocked-off fireplace,\n"
					+ "blast it open, send Fred and George after Harry�s trunk,\n"
					+ "then Floo back to the Burrow. Just as Harry is about to leave, \n"
					+ "Dudley eats a magical toffee dropped by Fred and grows a huge purple tongue. ");
			return chapters;
		}
		
		private ArrayList<String> makeMDChapterTitles()
		{
			ArrayList<String> titles = new ArrayList<String>();
			titles.add("Loomings.");
			titles.add("The Carpet-Bag.");
			titles.add("The Spouter-Inn.");
			return titles;
		}
		private ArrayList<String> makeMDChapters()
		{
			ArrayList<String> chapters = new ArrayList<String>();
			chapters.add("Call me Ishmael. Some years ago�never mind how long precisely�having little\n"
					+ "or no money in my purse, and nothing particular to interest me on shore,\n"
					+ "I thought I would sail about a little and see the watery part of the world.");
			chapters.add("stuffed a shirt or two into my old carpet-bag, tucked it under my arm, \n"
					+ "and started for Cape Horn and the Pacific. Quitting the good city of old Manhatto, \n"
					+ "I duly arrived in New Bedford. It was a Saturday night in December.");
			chapters.add("Entering that gable-ended Spouter-Inn, you found yourself in a wide, \n"
					+ "low, straggling entry with old-fashioned wainscots, \n"
					+ "reminding one of the bulwarks of some condemned old craft.");
			return chapters;
		}
		
		private ArrayList<String> makeSHChapterTitles()
		{
			ArrayList<String> titles = new ArrayList<String>();
			titles.add("");
			titles.add("");
			titles.add("");
			titles.add("");
			return titles;
		}
		
		private ArrayList<String> makeSHChapters()
		{
			ArrayList<String> chapters = new ArrayList<String>();
			chapters.add("The gale tore at him and he felt its bite deep within\n"
					+ "and he knew that if they did not make landfall in three days they would all be dead");
			chapters.add("Blackthorne was suddenly awake. For a moment he thought he was dreaming\n"
					+ "because he was ashore and the room unbelieveable");
			chapters.add("The daimyo, Kasigi Yabu, Lord of Izu, wants to know who you are,\n"
					+ "where you come from, how ou got here, and what acts of piracy you have committed.");
			chapters.add("Yabu lay in the hot bath, more content, more confident than he had ever been in his life.");
			return chapters;
		}
		
}
