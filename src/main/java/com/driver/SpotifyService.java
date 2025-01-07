package com.driver;

import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class SpotifyService {

    //Auto-wire will not work in this case, no need to change this and add autowire

    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public User createUser(String name, String mobile){
        User currentUser = spotifyRepository.createUser(name,mobile);
        return currentUser;
    }

    public Artist createArtist(String name) {
        Artist currentArtist = spotifyRepository.createArtist(name);
        return currentArtist;

    }

    public Album createAlbum(String title, String artistName) {
        Album album = spotifyRepository.createAlbum(title, artistName);
        return album;

    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        Song addedSong = spotifyRepository.createSong(title, albumName, length);
        return addedSong;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist = spotifyRepository.createPlaylistOnLength(mobile,title,length);
        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        Playlist playlist = spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
        return playlist;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        Playlist playlist = spotifyRepository.findPlaylist(mobile, playlistTitle);
        return playlist;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        Song song = spotifyRepository.likeSong(mobile, songTitle);
        return song;

    }

    public String mostPopularArtist() {
        String mostLikedArtist = spotifyRepository.mostPopularArtist();
        return mostLikedArtist;

    }

    public String mostPopularSong() {
        String mostLikedSong = spotifyRepository.mostPopularSong();
        return mostLikedSong;


    }
}
