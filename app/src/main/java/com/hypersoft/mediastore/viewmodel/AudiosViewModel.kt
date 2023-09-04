package com.hypersoft.mediastore.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.hypersoft.mediastore.datamodel.AudioItem
import com.hypersoft.mediastore.datamodel.FolderItem
import com.hypersoft.mediastore.utils.GeneralUtils.readableDuration
import com.hypersoft.mediastore.utils.GeneralUtils.readableFileSize
import com.hypersoft.mediastore.utils.GeneralUtils.readableTimeStamp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class AudiosViewModel : ViewModel() {

    private val AUDIO_CODE = 66

    /**
     * Indicates whether audios files are currently being fetched or not.
     * property 'isAudiosFetching' True if files are being fetched, false otherwise.
     */
    private var isAudiosFetching = false

    /**
     * Handles exceptions that occur during background processes such as fetching audio files
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        isAudiosFetching = false
        setAudiosUpdated(false)
        Log.e("AudiosViewModelTAG", "${exception.message}")
    }

    /**
     * Handles the variables and functions related to LiveData updates for audio files.
     */
    private val _audioUpdate = MutableLiveData<Boolean>()
    var audioUpdate: LiveData<Boolean> = _audioUpdate.map { it }

    /**
     * Sets the LiveData value for audio file updates to the specified boolean value.
     * @param update The boolean value to set as the LiveData value.
     */
    fun setAudiosUpdated(update: Boolean) {
        _audioUpdate.postValue(update)
    }

    /**
     * The 'audioList' of audio file that cannot be changed outside of the view model.
     * property 'audiosList' list of audio file to be used within the view model.
     *
     */
    private var audiosList = ArrayList<AudioItem>()

    /**
    Represents a mapping of folder names to their respective lists of audio files.
    The 'audioFolderMap' property is a HashMap that stores folder names as keys and associated
    audio file lists as values. Each folder name is mapped to a MutableList of AudioItem objects.
     */
    private var audioFolderMap: HashMap<String, MutableList<AudioItem>> = hashMapOf()

    /**
    Represents a mapping of album names to their respective lists of audio file.
    The 'audioAlbumMap' property is a HashMap that stores album names as keys and associated
    audio file lists as values. Each album name is mapped to a MutableList of AudioItem objects.
     */
    private var audioAlbumMap: HashMap<String, MutableList<AudioItem>> = hashMapOf()

    /**
     * Fetching Audio Files from system
     */
    fun fetchingAudios(activity: Activity?,fromMediaObserver:Boolean = false) {
        val loadedAudiosList = ArrayList<AudioItem>()
        val folderMap = HashMap<String, MutableList<AudioItem>>()
        val albumMap = HashMap<String, MutableList<AudioItem>>()

        if (!isAudiosFetching) {
            isAudiosFetching = true
            val check = if (fromMediaObserver) true else isAudiosListEmpty()
            if (check) {
                val timeFormat = "dd,MMMM,yyyy"
                activity?.let { mActivity ->
                    val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    val projection = arrayOf(
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TRACK,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.SIZE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATE_ADDED,
                        MediaStore.Audio.Media.DATE_MODIFIED,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST_ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.COMPOSER,
                        MediaStore.Audio.Media.ALBUM_ARTIST,
                        MediaStore.Audio.Media.DATA
                    )

                    val selection = MediaStore.Audio.Media.MIME_TYPE + " ='audio/mpeg' OR " +
                            MediaStore.Audio.Media.MIME_TYPE + " ='audio/wav' OR " +
                            MediaStore.Audio.Media.MIME_TYPE + " ='audio/ogg' OR " +
                            MediaStore.Audio.Media.MIME_TYPE + " ='audio/aac' OR " +
                            MediaStore.Audio.Media.MIME_TYPE + " ='audio/flac'"

                    val orderBy = MediaStore.Audio.Media.DATE_MODIFIED + " ASC"

                    val loaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
                        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                            return CursorLoader(
                                mActivity,
                                uri,
                                projection,
                                selection,
                                null,
                                orderBy
                            )
                        }

                        @SuppressLint("Range")
                        override fun onLoadFinished(loader: Loader<Cursor>, curserData: Cursor?) {

                            viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
                                async(Dispatchers.IO + exceptionHandler) {
                                    curserData?.let { data ->
                                        while (data.moveToNext()) {
                                            val mDataId =
                                                data.getLong(data.getColumnIndex(MediaStore.Audio.Media._ID))
                                            val mTrackNumber =
                                                data.getInt(data.getColumnIndex(MediaStore.Audio.Media.TRACK))
                                            val mTitle =
                                                data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE))
                                                    ?: "<Unknown>"
                                            val mSize =
                                                data.getLong(data.getColumnIndex(MediaStore.Audio.Media.SIZE))
                                            val mDuration =
                                                data.getLong(data.getColumnIndex(MediaStore.Audio.Media.DURATION))
                                            val mDateAdded =
                                                data.getLong(data.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED))
                                            val mDateModified =
                                                data.getLong(data.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED))
                                            val mAlbumID =
                                                data.getLong(data.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                                            val mAlbumName =
                                                data.getString(data.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                                                    ?: "<Unknown>"
                                            val mArtistID =
                                                data.getLong(data.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
                                            val mArtistName =
                                                data.getString(data.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                                                    ?: "<Unknown>"
                                            val mComposerName =
                                                data.getString(data.getColumnIndex(MediaStore.Audio.Media.COMPOSER))
                                                    ?: "<Unknown>"
                                            val mAlbumArtistName =
                                                data.getString(data.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST))
                                                    ?: "<Unknown>"
                                            val adPath =
                                                data.getString(data.getColumnIndex(MediaStore.Audio.Media.DATA))

                                            adPath?.let { path ->
                                                if (File(path).exists()) {
                                                    val audioFile = AudioItem(
                                                        id = mDataId,
                                                        trackNumber = mTrackNumber,
                                                        audioName = mTitle,
                                                        audioSizeTitle = readableFileSize(mSize),
                                                        audioSize = mSize,
                                                        audioDurationTitle = readableDuration(
                                                            mDuration
                                                        ),
                                                        audioDuration = mDuration,
                                                        audioDateAddedTitle = readableTimeStamp(
                                                            mDateAdded,
                                                            timeFormat
                                                        ),
                                                        audioDateAdded = mDateAdded,
                                                        audioDateModifiedTitle = readableTimeStamp(
                                                            mDateModified,
                                                            timeFormat
                                                        ),
                                                        audioDateModified = mDateModified,
                                                        albumId = mAlbumID,
                                                        albumName = mAlbumName,
                                                        artistId = mArtistID,
                                                        artistName = mArtistName,
                                                        composerName = mComposerName,
                                                        albumArtist = mAlbumArtistName,
                                                        audioPath = path
                                                    )
                                                    loadedAudiosList.add(audioFile)
                                                    /**
                                                     * Folder Map
                                                     */
                                                    val folderPath =
                                                        audioFile.audioPath.substringBeforeLast("/")
                                                    val folderName =
                                                        folderPath.substringAfterLast("/")
                                                    val folderAudioFiles =
                                                        folderMap[folderName]?.toMutableList()
                                                            ?: mutableListOf()
                                                    folderAudioFiles.add(audioFile)
                                                    folderMap[folderName] = folderAudioFiles

                                                    /**
                                                     * Album Map
                                                     */

                                                    val albumName = audioFile.albumName
                                                    val albumAudioFiles =
                                                        albumMap[albumName]?.toMutableList()
                                                            ?: mutableListOf()
                                                    albumAudioFiles.add(audioFile)
                                                    albumMap[albumName] = albumAudioFiles
                                                }
                                            }
                                        }
                                        data.close()
                                    }
                                    clearAudioListData()
                                    clearFolderMap()
                                    clearAlbumMap()
                                }.await()
                                isAudiosFetching = false
                                audiosList.addAll(loadedAudiosList)
                                audioFolderMap.putAll(folderMap)
                                audioAlbumMap.putAll(albumMap)
                                setAudiosUpdated(true)
                                LoaderManager.getInstance(mActivity as AppCompatActivity)
                                    .destroyLoader(AUDIO_CODE)
                            }
                        }

                        override fun onLoaderReset(loader: Loader<Cursor>) {}
                    }
                    LoaderManager.getInstance(mActivity as AppCompatActivity)
                        .initLoader(AUDIO_CODE, null, loaderCallbacks)

                } ?: kotlin.run {
                    isAudiosFetching = false
                    setAudiosUpdated(true)
                }
            } else {
                isAudiosFetching = false
                setAudiosUpdated(true)
            }
        }
    }


    /**
     * Retrieve data for all Audio Files
     */
    //    val allAudios: List<AudioItem> get() = audiosList.toList()
    fun getAllAudios(): MutableList<AudioItem> {
        val list = mutableListOf<AudioItem>()
        list.addAll(audiosList)
        return list
    }

    fun isAudiosListEmpty() = audiosList.isEmpty()

    private fun clearAudioListData() = audiosList.clear()

    fun audioSize() = audiosList.size

    /**
     * Retrieve all data for folders
     */

    fun foldersSize() = audioFolderMap.size
    fun folderAudioListSize(folderName: String) = audioFolderMap[folderName]?.size ?: 0
    fun isFolderMapEmpty() = audioFolderMap.isEmpty()
    fun isFolderAudiosListEmpty(folderName: String) = audioFolderMap[folderName]?.isEmpty() ?: true
    fun getFoldersList(): MutableList<FolderItem> {
        val list = mutableListOf<FolderItem>()
        list.addAll(audioFolderMap.entries
            .asSequence()
            .filter {
                it.value.isNotEmpty()
            }
            .map {
                it.key to it.value[0]
            }
            .map {
                var folderSize = 0
                val mutableList1 = audioFolderMap[it.first]?.size
                mutableList1?.let { fSize ->
                    folderSize = fSize
                }
                FolderItem(it.first, it.second.audioPath, folderSize)
            }
            .sortedBy {
                it.folderName.lowercase()
            }
            .toMutableList())
        return list
    }

    fun getAudiosFromFolder(folderName: String) = audioFolderMap[folderName] ?: mutableListOf()
    private fun clearFolderMap() = audioFolderMap.clear()

    /**
     * Retrieve all data for album
     * Do other functionalities by yourself like folder
     */
    private fun clearAlbumMap() = audioAlbumMap.clear()

    /**
     * When Audio file updated from external storage
     * fetch again files
     */

    fun mediaObserve(activity: Activity?){
        fetchingAudios(activity,true)
    }


    /**
     * Do yourself
    Implement operations for deleting, renaming, sorting, and searching within the
    context of audiosList, audioFolderMap, audioAlbumMap and others
    Provide comprehensive functionality for these features.
     */

}