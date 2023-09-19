import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

// Simulation of a Simple Text-based Music App (like Apple Music)

public class MyAudioUI
{
	// Static method that takes an AudioContent object as a parameter and returns a string indicating its content type.
	private static String getContentType(AudioContent content) {
        if (content instanceof Song) {
			return "SONG";
		} else {
			return "AUDIOBOOK";
		}
    }
	public static void main(String[] args)
	{
		// Simulation of audio content in an online store
		// The songs, podcasts, audiobooks in the store can be downloaded to your library
		AudioContentStore store = new AudioContentStore();
		
		// Create my music library
		Library library = new Library();

		Scanner scanner = new Scanner(System.in);
		System.out.print(">");

		// Process keyboard actions
		while (scanner.hasNextLine())
		{
			String action = scanner.nextLine();

			if (action == null || action.equals("")) 
			{
				System.out.print("\n>");
				continue;
			}
			else if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT"))
				return;
			
			else if (action.equalsIgnoreCase("STORE"))	// List all songs
			{
				store.listAll(); 
			}
			else if (action.equalsIgnoreCase("SONGS"))	// List all songs
			{
				library.listAllSongs(); 
			}
			else if (action.equalsIgnoreCase("BOOKS"))	// List all songs
			{
				library.listAllAudioBooks(); 
			}
			else if (action.equalsIgnoreCase("PODCASTS"))	// List all songs
			{
				library.listAllPodcasts(); 
			}
			else if (action.equalsIgnoreCase("ARTISTS"))	// List all songs
			{
				library.listAllArtists(); 
			}
			else if (action.equalsIgnoreCase("PLAYLISTS"))	// List all play lists
			{
				library.listAllPlaylists(); 
			}

			
			else if (action.equalsIgnoreCase("DOWNLOAD")) {
				System.out.print("From Store Content #: ");
				int fromIndex = Integer.parseInt(scanner.nextLine()) - 1;
				System.out.print("To Store Content #: ");
				int toIndex = Integer.parseInt(scanner.nextLine()) - 1;
			
				for (int i = fromIndex; i <= toIndex; i++) {
					try {
						AudioContent content = store.getAudioContent(i);
						if (!library.isInLibrary(content.getId())) {
							library.download(content);
							System.out.println(getContentType(content) + " " + content.getTitle() + " Added to Library");
						} else {
							System.out.println(getContentType(content) + " " + content.getTitle() + " already downloaded");
						}
					} catch (AudioContentNotFoundException e) {
						System.out.println("Invalid index: " + (i + 1));
					}
				}
			}
			
			// Downloads all audio content by artist
			else if (action.equalsIgnoreCase("DOWNLOADA")) {
				System.out.print("Artist name: ");
				String artist = scanner.nextLine();
			
				try {
					// Retrieve all audio content by artist
					ArrayList<AudioContent> contents = store.getAudioContentByArtist(artist);
					if (contents.isEmpty()) {
						// No audio content found for artist
						System.out.println("No audio content found for artist/author: " + artist);
					} else {
						// Download all audio content by artist
						for (AudioContent content : contents) {
							if (!library.isInLibrary(content.getId())) {
								// Download content if not already in library
								library.download(content);
								System.out.println(getContentType(content) + " " + content.getTitle());
							} else {
								System.out.println(getContentType(content) + " " + content.getTitle() + " already downloaded");
							}
						}
					}
				} catch (AudioContentNotFoundException e) {
					// No audio content found for artist
					System.out.println("No audio content found for artist/author: " + artist);
				}
			}

			// Downloads all songs in a specified genre
			else if (action.equalsIgnoreCase("DOWNLOADG")) {
				System.out.print("Genre: ");
				String genre = scanner.nextLine();
				// Attempt to retrieve all audio content for the specified genre
				try {
					ArrayList<AudioContent> contents = store.downloadSongsByGenre(genre);
					// If no audio content found, inform the user
					if (contents.isEmpty()) {
						System.out.println("No audio content found for genre: " + genre);
					} else {
						for (AudioContent content : contents) {
							 // If the audio content is not in the library, download it and inform the user
							if (!library.isInLibrary(content.getId())) {
								library.download(content);
								System.out.println(getContentType(content) + " " + content.getTitle());
							} 
							// Otherwise, inform the user that the audio content is already downloaded
							else {
								System.out.println(getContentType(content) + " " + content.getTitle() + " already downloaded");
							}
						}
					}
				// If an AudioContentNotFoundException is caught, inform the user that no audio content was found for the specified genre
				} catch (AudioContentNotFoundException e) {
					System.out.println("No audio content found for genre: " + genre);
				}
			}

			else if(action.equalsIgnoreCase("SEARCH"))
			{
				System.out.print("Title: ");
				String title = scanner.nextLine();
				// Search for the audio content with the specified title
				AudioContent content = store.searchTitle(title);
				// If a matching audio content is found, display its details
				if (content != null) {
					int index = store.getIndexFromTitle(title) + 1;
					System.out.print(index + ". " + "Title: " + content.getTitle());
					System.out.print(" ID: " + content.getId());
					System.out.print(" Year: " + content.getYear());
					System.out.print(" Type: " + content.getType());
					System.out.println(" Length: " + content.getLength());
					System.out.print("Artist: " + content.getArtist());
					// If the audio content is a song, display its composer and genre
					if (content instanceof Song) {
						System.out.print(" Composer: " + ((Song) content).getComposer());
						System.out.println(" Genre: " + ((Song) content).getGenre().toString());
					}
				} 
				// If no matching audio content is found, inform the user
				else {
					System.out.println("No matches for " + title);
				}
			}

			else if (action.equalsIgnoreCase("SEARCHA")) {
				System.out.print("Artist: ");
				String artist = scanner.nextLine();
				ArrayList<AudioContent> contentList = store.searchArtist(artist);

				if (!contentList.isEmpty()) {
					for (AudioContent content : contentList) {
						int index = store.getIndexFromTitle(content.getTitle()) + 1;
						System.out.print(index + ". " + "Title: " + content.getTitle());
						System.out.print(" ID: " + content.getId());
						System.out.print(" Year: " + content.getYear());
						System.out.print(" Type: " + content.getType());
						System.out.println(" Length: " + content.getLength());
						System.out.print("Artist: " + content.getArtist());
						if (content instanceof Song) {
							System.out.print(" Composer: " + ((Song) content).getComposer());
							System.out.println(" Genre: " + ((Song) content).getGenre().toString());
						}
					}
				} else {
					System.out.println("No matches for " + artist);
				}
			}

			// Chooses to search by artist, prompt for the artist's name and display all matching audio content
			else if (action.equalsIgnoreCase("SEARCHG")) {
				System.out.print("Genre [POP, ROCK, JAZZ, HIPHOP, RAP, CLASSICAL]: ");
				String genre = scanner.nextLine();
				// Search for all audio content by the specified artist
				ArrayList<AudioContent> contentList = store.searchByGenre(genre);
			
				if (!contentList.isEmpty()) {
					// If matching audio content is found, display its details
					for (AudioContent content : contentList) {
						int index = store.getIndexOfContent(content) + 1;
						System.out.print(index + ". " + "Title: " + content.getTitle());
						System.out.print(" ID: " + content.getId());
						System.out.print(" Year: " + content.getYear());
						System.out.print(" Type: " + content.getType());
						System.out.println(" Length: " + content.getLength());
						System.out.print("Artist: " + content.getArtist());
						if (content instanceof Song) {
							System.out.print(" Composer: " + ((Song) content).getComposer());
							System.out.println(" Genre: " + ((Song) content).getGenre().toString());
						}
					}
				} 
				// If no matching audio content is found, inform the user
				else {
					System.out.println("No matches for " + genre);
				}
			}

			// Search for audio content by entering a partial string match.
			else if (action.equalsIgnoreCase("SEARCHP")) {
				// Prompt the user to enter a target string
				System.out.print("Enter the target string to search: ");
				String target = scanner.nextLine();
				// Search for audio content that partially matches the target string
				ArrayList<AudioContent> results = library.searchPartialMatch(target);
			    
				// If any matching audio content is found, print their details
				if (results.isEmpty()) {
					System.out.println("No audio content found with the target string: " + target);
				} else {
					for (AudioContent content : results) {
						content.printInfo();
					}
				}
			}
			

			else if (action.equalsIgnoreCase("PLAYSONG")) {
				int index = 0;
			
				System.out.print("Song Number: ");
				if (scanner.hasNextInt()) {
					index = scanner.nextInt();
					// consume the nl character since nextInt() does not
					scanner.nextLine();
				}
				try {
					library.playSong(index);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("BOOKTOC")) {
				int index = 0;
			
				System.out.print("Audio Book Number: ");
				if (scanner.hasNextInt()) {
					index = scanner.nextInt();
					scanner.nextLine();
				}
				try {
					library.printAudioBookTOC(index);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("PLAYBOOK")) {
				int index = 0;
			
				System.out.print("Audio Book Number: ");
				if (scanner.hasNextInt()) {
					index = scanner.nextInt();
				}
				int chapter = 0;
				System.out.print("Chapter: ");
				if (scanner.hasNextInt()) {
					chapter = scanner.nextInt();
					scanner.nextLine();
				}
				try {
					library.playAudioBook(index, chapter);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			
			else if (action.equalsIgnoreCase("PODTOC")) {
				int index = 0;
				int season = 0;
			
				System.out.print("Podcast Number: ");
				if (scanner.hasNextInt()) {
					index = scanner.nextInt();
				}
				System.out.print("Season: ");
				if (scanner.hasNextInt()) {
					season = scanner.nextInt();
					scanner.nextLine();
				}
				try {
					library.printPodcastEpisodes(index, season);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("PLAYPOD")) {
				int index = 0;
			
				System.out.print("Podcast Number: ");
				if (scanner.hasNextInt()) {
					index = scanner.nextInt();
					scanner.nextLine();
				}
				int season = 0;
				System.out.print("Season: ");
				if (scanner.hasNextInt()) {
					season = scanner.nextInt();
					scanner.nextLine();
				}
				int episode = 0;
				System.out.print("Episode: ");
				if (scanner.hasNextInt()) {
					episode = scanner.nextInt();
					scanner.nextLine();
				}
				try {
					library.playPodcast(index, season, episode);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("PLAYALLPL")) {
				String title = "";
			
				System.out.print("Playlist Title: ");
				if (scanner.hasNextLine()) {
					title = scanner.nextLine();
				}
				try {
					library.playPlaylist(title);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("PLAYPL")) {
				String title = "";
				int index = 0;
			
				System.out.print("Playlist Title: ");
				if (scanner.hasNextLine()) {
					title = scanner.nextLine();
				}
				System.out.print("Content Number: ");
				if (scanner.hasNextInt()) {
					index = scanner.nextInt();
					scanner.nextLine();
				}
				try {
					library.playPlaylist(title, index);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			// Delete a song from the library and any play lists it belongs to
			else if (action.equalsIgnoreCase("DELSONG")) {
				int songNum = 0;
			
				System.out.print("Library Song #: ");
				if (scanner.hasNextInt()) {
					songNum = scanner.nextInt();
					scanner.nextLine();
				}
			
				try {
					library.deleteSong(songNum);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("MAKEPL")) {
				String title = "";
				
				System.out.print("Playlist Title: ");
				if (scanner.hasNextLine()) {
					title = scanner.nextLine();
				}
				try {
					library.makePlaylist(title);
				} catch (PlaylistAlreadyExistsException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("PRINTPL")) // print playlist content
			{
				String title = "";

				System.out.print("Playlist Title: ");
				if (scanner.hasNextLine()) {
					title = scanner.nextLine();
				}
				try {
					library.printPlaylist(title);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			// Add content from library (via index) to a playlist
			else if (action.equalsIgnoreCase("ADDTOPL")) {
				int contentIndex = 0;
				String contentType = "";
				String playlist = "";
			
				System.out.print("Playlist Title: ");
				if (scanner.hasNextLine()) {
					playlist = scanner.nextLine();
				}
			
				System.out.print("Content Type [SONG, PODCAST, AUDIOBOOK]: ");
				if (scanner.hasNextLine()) {
					contentType = scanner.nextLine();
				}
			
				System.out.print("Library Content #: ");
				if (scanner.hasNextInt()) {
					contentIndex = scanner.nextInt();
					scanner.nextLine(); // consume nl
				}
			
				try {
					library.addContentToPlaylist(contentType, contentIndex, playlist);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			// Delete content from play list
			else if (action.equalsIgnoreCase("DELFROMPL")) {
				int contentIndex = 0;
				String playlist = "";
			
				System.out.print("Playlist Title: ");
				if (scanner.hasNextLine()) {
					playlist = scanner.nextLine();
				}
			
				System.out.print("Playlist Content #: ");
				if (scanner.hasNextInt()) {
					contentIndex = scanner.nextInt();
					scanner.nextLine(); // consume nl
				}
				try {
					library.delContentFromPlaylist(contentIndex, playlist);
				} catch (AudioContentNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else if (action.equalsIgnoreCase("SORTBYYEAR")) // sort songs by year
			{
				library.sortSongsByYear();
			}
			else if (action.equalsIgnoreCase("SORTBYNAME")) // sort songs by name (alphabetic)
			{
				library.sortSongsByName();
			}
			else if (action.equalsIgnoreCase("SORTBYLENGTH")) // sort songs by length
			{
				library.sortSongsByLength();
			}

			System.out.print("\n>");
		}
	}
}
