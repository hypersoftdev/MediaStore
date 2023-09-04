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
import com.hypersoft.mediastore.datamodel.FolderItem
import com.hypersoft.mediastore.datamodel.VideoItem
import com.hypersoft.mediastore.utils.GeneralUtils.readableDuration
import com.hypersoft.mediastore.utils.GeneralUtils.readableFileSize
import com.hypersoft.mediastore.utils.GeneralUtils.readableTimeStamp
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class VideosViewModel : ViewModel() {

    private val VIDEOS_CODE = 77

    /**
     * Indicates whether videos are currently being fetched or not.
     * property 'isVideosFetching' True if videos are being fetched, false otherwise.
     */
    private var isVideosFetching = false

    /**
     * Handles exceptions that occur during background processes such as fetching videos
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        isVideosFetching = false
        setVideoUpdated(false)
        Log.e("VideosViewModelTAG", "${exception.message}")
    }

    /**
     * Handles the variables and functions related to LiveData updates for videos.
     */
    private val _videoUpdate = MutableLiveData<Boolean>()
    var videoUpdate: LiveData<Boolean> = _videoUpdate.map { it }

    /**
     * Sets the LiveData value for videos updates to the specified boolean value.
     * @param update The boolean value to set as the LiveData value.
     */
    fun setVideoUpdated(update: Boolean) {
        _videoUpdate.postValue(update)
    }

    /**
     * The 'videosList' of Videos that cannot be changed outside of the ViewModel.
     * property 'videosList' list of videos to be used within the ViewModel.
     *
     */
    private var videosList = ArrayList<VideoItem>()

    /**
    Represents a mapping of folder names to their respective lists of videos.
    The 'videoFolderMap' property is a HashMap that stores folder names as keys and associated
    video lists as values. Each folder name is mapped to a MutableList of VideoItem objects.
     */
    private var videoFolderMap: HashMap<String, MutableList<VideoItem>> = hashMapOf()


    /**
     * Fetching Videos from system
     */
    fun fetchingVideos(activity: Activity?) {
        val loadedVideosList = ArrayList<VideoItem>()
        val folderMap = HashMap<String, MutableList<VideoItem>>()

        if (!isVideosFetching) {
            isVideosFetching = true
            if (isVideosListEmpty()) {
                val timeFormat = "dd,MMMM,yyyy"
                activity?.let { mActivity ->
                    val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    val projection = arrayOf(
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.HEIGHT,
                        MediaStore.Video.Media.RESOLUTION,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.DATE_ADDED,
                        MediaStore.Video.Media.DATE_MODIFIED,
                        MediaStore.Video.Media.MIME_TYPE,
                        MediaStore.Video.Media.DATA
                    )

                    val selection = MediaStore.Video.Media.MIME_TYPE + " ='video/mp4' OR " +
                            MediaStore.Video.Media.MIME_TYPE + " ='video/WMV' OR " +
                            MediaStore.Video.Media.MIME_TYPE + " ='video/MKV' OR " +
                            MediaStore.Video.Media.MIME_TYPE + " ='video/AVI' OR " +
                            MediaStore.Video.Media.MIME_TYPE + " ='video/MOV'"

                    val orderBy = MediaStore.Video.Media.SIZE + " ASC"

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
                                            val mBucket =
                                                data.getString(data.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                                            val mDataId =
                                                data.getLong(data.getColumnIndex(MediaStore.Video.Media._ID))
                                            val mTitle =
                                                data.getString(data.getColumnIndex(MediaStore.Video.Media.TITLE))
                                                    ?: "<Unknown>"
                                            val mSize =
                                                data.getLong(data.getColumnIndex(MediaStore.Video.Media.SIZE))
                                            val mHeight =
                                                data.getLong(data.getColumnIndex(MediaStore.Video.Media.HEIGHT))
                                            val mResolution =
                                                data.getString(data.getColumnIndex(MediaStore.Video.Media.RESOLUTION))?:"0x0"
                                            val mDuration =
                                                data.getLong(data.getColumnIndex(MediaStore.Video.Media.DURATION))
                                            val mDateAdded =
                                                data.getLong(data.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
                                            val mDateModified =
                                                data.getLong(data.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
                                            val vdMimeType =
                                                data.getString(data.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))?:"Video"
                                            val vdPath =
                                                data.getString(data.getColumnIndex(MediaStore.Video.Media.DATA))


                                            vdPath?.let { path ->
                                                if (File(path).exists()) {
                                                    mBucket?.let {
                                                        if (folderMap.containsKey(it)) {
                                                            val listOfValues = folderMap[it]
                                                            listOfValues!!.add(
                                                                VideoItem(
                                                                    id = mDataId,
                                                                    videoName = mTitle,
                                                                    videoSizeTitle = readableFileSize(mSize),
                                                                    videoSize = mSize,
                                                                    videoHeightTitle =  "${mHeight}p",
                                                                    videoHeight = mHeight,
                                                                    videoResolution = mResolution,
                                                                    videoDurationTitle = readableDuration(mDuration),
                                                                    videoDuration = mDuration,
                                                                    videoDateAddedTitle = readableTimeStamp(mDateAdded, timeFormat),
                                                                    videoDateAdded = mDateAdded,
                                                                    videoDateModifiedTitle = readableTimeStamp(mDateModified, timeFormat),
                                                                    videoDateModified = mDateModified,
                                                                    videoMimeType = vdMimeType,
                                                                    videoPath = path
                                                                )
                                                            )
                                                            folderMap.put(it, listOfValues)

                                                        } else {
                                                            val listOfValues = ArrayList<VideoItem>()
                                                            listOfValues.add(
                                                                VideoItem(
                                                                    id = mDataId,
                                                                    videoName = mTitle,
                                                                    videoSizeTitle = readableFileSize(mSize),
                                                                    videoSize = mSize,
                                                                    videoHeightTitle =  "${mHeight}p",
                                                                    videoHeight = mHeight,
                                                                    videoResolution = mResolution,
                                                                    videoDurationTitle = readableDuration(mDuration),
                                                                    videoDuration = mDuration,
                                                                    videoDateAddedTitle = readableTimeStamp(mDateAdded, timeFormat),
                                                                    videoDateAdded = mDateAdded,
                                                                    videoDateModifiedTitle = readableTimeStamp(mDateModified, timeFormat),
                                                                    videoDateModified = mDateModified,
                                                                    videoMimeType = vdMimeType,
                                                                    videoPath = path
                                                                )
                                                            )
                                                            folderMap.put(it, listOfValues)

                                                        }
                                                    }
                                                    loadedVideosList.add(
                                                        VideoItem(
                                                            id = mDataId,
                                                            videoName = mTitle,
                                                            videoSizeTitle = readableFileSize(mSize),
                                                            videoSize = mSize,
                                                            videoHeightTitle =  "${mHeight}p",
                                                            videoHeight = mHeight,
                                                            videoResolution = mResolution,
                                                            videoDurationTitle = readableDuration(mDuration),
                                                            videoDuration = mDuration,
                                                            videoDateAddedTitle = readableTimeStamp(mDateAdded, timeFormat),
                                                            videoDateAdded = mDateAdded,
                                                            videoDateModifiedTitle = readableTimeStamp(mDateModified, timeFormat),
                                                            videoDateModified = mDateModified,
                                                            videoMimeType = vdMimeType,
                                                            videoPath = path
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        data.close()
                                    }
                                    clearVideoListData()
                                    clearFolderMap()

                                }.await()
                                isVideosFetching = false
                                videosList.addAll(loadedVideosList)
                                videoFolderMap.putAll(folderMap)

                                setVideoUpdated(true)
                                LoaderManager.getInstance(mActivity as AppCompatActivity)
                                    .destroyLoader(VIDEOS_CODE)
                            }
                        }

                        override fun onLoaderReset(loader: Loader<Cursor>) {}
                    }
                    LoaderManager.getInstance(mActivity as AppCompatActivity)
                        .initLoader(VIDEOS_CODE, null, loaderCallbacks)

                } ?: kotlin.run {
                    isVideosFetching = false
                    setVideoUpdated(true)
                }
            } else {
                isVideosFetching = false
                setVideoUpdated(true)
            }
        }
    }

    /**
     * Retrieve data for all Videos
     */
    //    val allVideos: List<VideoItem> get() = videosList.toList()
    fun getAllVideos(): MutableList<VideoItem> {
        val list = mutableListOf<VideoItem>()
        list.addAll(videosList)
        return list
    }

    fun isVideosListEmpty() = videosList.isEmpty()

    private fun clearVideoListData() = videosList.clear()

    fun videoSize() = videosList.size

    /**
     * Retrieve all data for folders
     */

    fun foldersSize() = videoFolderMap.size
    fun folderVideoListSize(folderName: String) = videoFolderMap[folderName]?.size ?: 0
    fun isFolderMapEmpty() = videoFolderMap.isEmpty()
    fun isFolderVideosListEmpty(folderName: String) = videoFolderMap[folderName]?.isEmpty() ?: true
    fun getFoldersList(): MutableList<FolderItem> {
        val list = mutableListOf<FolderItem>()
        list.addAll(videoFolderMap.entries
            .asSequence()
            .filter {
                it.value.isNotEmpty()
            }
            .map {
                it.key to it.value[0]
            }
            .map {
                var folderSize = 0
                val mutableList1 = videoFolderMap[it.first]?.size
                mutableList1?.let { fSize ->
                    folderSize = fSize
                }
                FolderItem(it.first, it.second.videoPath, folderSize)
            }
            .sortedBy {
                it.folderName.lowercase()
            }
            .toMutableList())
        return list
    }

    fun getVideosFromFolder(folderName: String) = videoFolderMap[folderName] ?: mutableListOf()
    private fun clearFolderMap() = videoFolderMap.clear()


    /**
     * Do yourself
    Implement operations for deleting, renaming, sorting, and searching within the
    context of videosList, videoFolderMap and others
    Provide comprehensive functionality for these features.
     */

}