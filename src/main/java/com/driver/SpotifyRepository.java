package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    Artist artist;
    Song song;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();

        artist = new Artist();
        song = new Song();
    }

    public User createUser(String name, String mobile) {
        User user = new User();
        user.setName(name);
        user.setMobile(mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {

        artist.setName(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        Album album = new Album();
        album.setTitle(title);

        Artist artist1 = null;
        //check whether artist with artistName is already exists or not
        for(Artist a:artists){
            if(a.getName().equals(artistName)){
                artist1 = a;
                break;
            }
        }

        //if it is still null that means it doesn't exist before
        if(artist1==null){
            //create new artist
            artist1 = new Artist();
            artist1.setName(artistName);
            artists.add(artist1);
        }

        if(artistAlbumMap.containsKey(artist1)){
            //if artist already exist then we will add album into existing artis's album
            artistAlbumMap.get(artist1).add(album);//here list already exist therefore no need to make new list
        }else{
            //else create new album list
            ArrayList<Album> albumList = new ArrayList<>();
            albumList.add(album);
            artistAlbumMap.put(artist1,albumList);
        }

        //add the album into global albums
        albums.add(album);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song1 = new Song();
        song1.setTitle(title);
        song1.setLength(length);
        songs.add(song1);
        Album album1 = null;
//        albumSongMap.put(Album,list<Song>)
        for(Album alb:albums){
            if(alb.getTitle().equals(albumName)){
                album1 = alb;
                break;
            }
        }

        if(album1==null){
            throw new Exception("Album does not exist");
        }

        if(albumSongMap.containsKey(album1)){
            albumSongMap.get(album1).add(song1);
        }else{
            ArrayList<Song> songList = new ArrayList<>();
            songList.add(song1);
            albumSongMap.put(album1,songList);
        }
        songs.add(song1);
        return song1;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        Playlist playlist_ = new Playlist();
        playlist_.setTitle(title);

//        mobile is a part of user & length is a part of song
//        HashMap<Playlist, List<Song>> playlistSongMap;done
//        HashMap<Playlist, List<User>> playlistListenerMap;done
//        HashMap<User, Playlist> creatorPlaylistMap;done
//        HashMap<User, List<Playlist>> userPlaylistMap;done

        User user1 = null;
        ArrayList<Song> songsWithSameLength = new ArrayList<>();

        for(User u:users){
            if(u.getMobile().equals(mobile)){
                user1=u;
                break;
            }
        }
        if(user1==null){
            throw new Exception("User does not exist");
        }
        creatorPlaylistMap.put(user1,playlist_);

        //add list<songs> which have same length with key as playlist
        playlistSongMap.put(playlist_,new ArrayList<>());
        for(Song s:songs){
            if(s.getLength()==length){
                playlistSongMap.get(playlist_).add(s);
            }
        }

        //playlistListenerMap
        ArrayList<User> listeners = new ArrayList<>();
        listeners.add(user1);
        playlistListenerMap.put(playlist_,listeners);

//        userPlaylistMap.get(user1).add(playlist_);

        if(userPlaylistMap.containsKey(user1)){
            userPlaylistMap.get(user1).add(playlist_);
        }else{
            ArrayList<Playlist> playlists1 = new ArrayList<>();
            playlists1.add(playlist_);
            userPlaylistMap.put(user1,playlists1);
        }
        playlists.add(playlist_);//add playlist_ to global variable also
        return playlist_;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {

//        public HashMap<Artist, List<Album>> artistAlbumMap;
//        public HashMap<Album, List<Song>> albumSongMap;
//        public HashMap<Playlist, List<Song>> playlistSongMap;         done
//        public HashMap<Playlist, List<User>> playlistListenerMap;     done
//        public HashMap<User, Playlist> creatorPlaylistMap;            done
//        public HashMap<User, List<Playlist>> userPlaylistMap;         done
//        public HashMap<Song, List<User>> songLikeMap;
        User user1 = null;
        for(User u : users){
            if(u.getMobile().equals(mobile)){
                user1 = u;
                break;
            }
        }

        //If the user does not exist, throw "User does not exist" exception
        if(user1==null){
            throw new Exception("User does not exist");
        }


        Playlist playlist1 = new Playlist();
        playlist1.setTitle(title);

        playlistSongMap.put(playlist1,new ArrayList<>());
        for(Song s:songs){
            for(int i=0;i<songTitles.size();i++) {
                if (s.getTitle().equals(songTitles.get(i))){
                    playlistSongMap.get(playlist1).add(s);
                }
            }
        }

        creatorPlaylistMap.put(user1,playlist1);

        playlistListenerMap.put(playlist1,new ArrayList<>());
        playlistListenerMap.get(playlist1).add(user1);

        userPlaylistMap.put(user1,new ArrayList<>());
        userPlaylistMap.get(user1).add(playlist1);

        return playlist1;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        //If the playlist does not exists, throw "Playlist does not exist" exception

//        public HashMap<Artist, List<Album>> artistAlbumMap;
//        public HashMap<Album, List<Song>> albumSongMap;
//        public HashMap<Playlist, List<Song>> playlistSongMap;
//        public HashMap<Playlist, List<User>> playlistListenerMap;
//        public HashMap<User, Playlist> creatorPlaylistMap;
//        public HashMap<User, List<Playlist>> userPlaylistMap;
//        public HashMap<Song, List<User>> songLikeMap;

        //Find the playlist with given title and /// add user as listener of that playlist
        User listener = null;
        for(User u:users){
            if(u.getMobile().equals(mobile)){
                listener = u;
                break;
            }
        }

        //If the user does not exist, throw "User does not exist" exception
        if(listener==null){
            throw new Exception("User does not exist");
        }

//      Find the playlist with given title
        Playlist playlist1 = null;
        for(Playlist p:playlists){
            if(p.getTitle().equals(playlistTitle)){
                playlist1 = p;
                break;
            }
        }

        if(playlist1==null){
            throw new Exception("Playlist does not exist");
        }



        if(userPlaylistMap.containsKey(listener)) {
            userPlaylistMap.get(listener).add(playlist1);
        }else{
            List<Playlist> listPlaylist = new ArrayList<>();
            listPlaylist.add(playlist1);
            userPlaylistMap.put(listener,listPlaylist);
        }

        //if user is a creator, directly return currPlaylist
        if(creatorPlaylistMap.containsKey(listener)){
            return playlist1;
        }

//        //add playlist against currUser in userPlaylistMap
//        userPlaylistMap.get(listener).add(playlist1);

        //add user to playlistListerMap
        if(playlistListenerMap.containsKey(playlist1)){
            if(!playlistListenerMap.get(playlist1).contains(listener)){
                playlistListenerMap.get(playlist1).add(listener);
            }
        }

//        if(playlistListenerMap.containsKey(playlist1)) {
//            List<User> listeners = playlistListenerMap.get(playlist1);
//            //cond to check if listener is already part of playlistListenerMap & part of creatorPlaylistMap
//            //if not then will have to add if already exists then do nothing
//            //!creatorPlaylistMap.getOrDefault(listener,new Playlist()).equals(playlist1) checks if that listener has mapped that playlist1
//            if (!listeners.contains(listener) && !creatorPlaylistMap.getOrDefault(listener,new Playlist()).equals(playlist1)) {
//                //if already exists then do nothing
//                listeners.add(listener);
//            }
//        }else{
//            List<User> listeners = new ArrayList<>();
//            listeners.add(listener);
//            playlistListenerMap.put(playlist1,listeners);
//        }


        return playlist1;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
//        public HashMap<Artist, List<Album>> artistAlbumMap;
//        public HashMap<Album, List<Song>> albumSongMap;
//        public HashMap<Playlist, List<Song>> playlistSongMap;
//        public HashMap<Playlist, List<User>> playlistListenerMap;
//        public HashMap<User, Playlist> creatorPlaylistMap;
//        public HashMap<User, List<Playlist>> userPlaylistMap;
//        public HashMap<Song, List<User>> songLikeMap;

        //The user likes the given song. The corresponding artist of the song gets auto-liked
        //A song can be liked by a user only once. If a user tried to like a song multiple times, do nothing
        //However, an artist can indirectly have multiple likes from a user, if the user has liked multiple songs of that artist.
        //Return the song after updating
        User user1 = null;
        for(User u:users){
            if(u.getMobile().equals(mobile)){
                user1=u;
                break;
            }
        }
        //      If the user does not exist, throw "User does not exist" exception
        if(user1==null){
            throw new Exception("User does not exist");
        }

        Song song1 = null;
        for(Song s:songs){
            if(s.getTitle().equals(songTitle)){
                song1=s;
                break;
            }
        }
        //If the song does not exist, throw "Song does not exist" exception
        if(song1==null){
            throw new Exception("Song does not exist");
        }

        //check if the user is already associated with that song or not in HashMap<Song, List<User>> songLikeMap;
        if(!songLikeMap.containsKey(song1)){//if song1 is not already liked then it will be liked for the first time
            songLikeMap.put(song1,new ArrayList<>());
        }

        //add user
        if(songLikeMap.get(song1).contains(user1)){
            return song1;//return if user already exist
        }

        songLikeMap.get(song1).add(user1);


        //increase like count
        song1.setLikes(song1.getLikes()+1);

        //we want to go from song to artist so initially we will find song to album then album to artist
        Album albumContainsSong = null;
        for(Map.Entry<Album,List<Song>> e: albumSongMap.entrySet()){
            Album album = e.getKey();
            List<Song> songInAlbum = e.getValue();

            if(songInAlbum.contains(song1)){
                albumContainsSong = album;
                break;
            }
        }

        if (albumContainsSong == null) {
            throw new Exception("Album containing the song not found");
        }

        // now we have that album which has the given(ip) song
        //now will get artist from HashMap<Artist, List<Album>> artistAlbumMap
        Artist artistOfCurrentSong = null;
        for(Map.Entry<Artist,List<Album>> e: artistAlbumMap.entrySet()){
            Artist currArtist = e.getKey();
            List<Album> albumOfArtist = e.getValue();

            if(albumOfArtist.contains(albumContainsSong)){
                artistOfCurrentSong = currArtist;
                break;
            }
        }

        if (artistOfCurrentSong == null) {
            throw new Exception("Artist for the song not found");
        }

        // now we have artist of ip song in artistOfCurrentSong so will increase the like count of artist also
        artistOfCurrentSong.setLikes(artistOfCurrentSong.getLikes()+1);

        return song1;

    }

    public String mostPopularArtist() {
        Artist mostLikedArtist = null;
        int maxLikes=0;

        for(Artist a: artists){
            if(a.getLikes()>maxLikes){
                mostLikedArtist = a;
                maxLikes = a.getLikes();
            }
        }


        if(mostLikedArtist == null) {
            return "";
        }
        return mostLikedArtist.getName();

    }

    public String mostPopularSong() {
        Song mostLikedSong = null;
        int maxlikes=0;

        for(Song s:songs){
            if(s.getLikes()>maxlikes){
                mostLikedSong = s;
                maxlikes = s.getLikes();
            }
        }

        if(mostLikedSong == null) return "";
        return mostLikedSong.getTitle();
    }
}