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
import com.hypersoft.mediastore.datamodel.ImageItem
import com.hypersoft.mediastore.utils.GeneralUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File

class ImagesViewModel : ViewModel() {

    private val IMAGE_CODE = 77

    /**
     * Indicates whether images are currently being fetched or not.
     * property 'isImagesFetching' True if images are being fetched, false otherwise.
     */
    private var isImagesFetching = false

    /**
     * Handles exceptions that occur during background processes such as fetching images
     */
    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        isImagesFetching = false
        setImagesUpdated(false)
        Log.e("ImagesViewModelTAG", "${exception.message}")
    }

    /**
     * Handles the variables and functions related to LiveData updates for images.
     */
    private val _imagesUpdate = MutableLiveData<Boolean>()
    var imageUpdate: LiveData<Boolean> = _imagesUpdate.map { it }

    /**
     * Sets the LiveData value for images updates to the specified boolean value.
     * @param update The boolean value to set as the LiveData value.
     */
    fun setImagesUpdated(update: Boolean) {
        _imagesUpdate.postValue(update)
    }

    /**
     * The 'imagesList' of images that cannot be changed outside of the ViewModel.
     * property 'imagesList' list of images to be used within the ViewModel.
     *
     */
    private var imagesList = ArrayList<ImageItem>()

    /**
    Represents a mapping of folder names to their respective lists of images.
    The 'imagesFolderMap' property is a HashMap that stores folder names as keys and associated
    images lists as values. Each folder name is mapped to a MutableList of ImageItem objects.
     */
    private var imagesFolderMap: HashMap<String, MutableList<ImageItem>> = hashMapOf()


    /**
     * Fetching Images from system
     */
    fun fetchingImages(activity: Activity?,fromMediaObserver:Boolean = false) {
        val loadedImagesList = ArrayList<ImageItem>()
        val folderMap = HashMap<String, MutableList<ImageItem>>()

        if (!isImagesFetching) {
            isImagesFetching = true
            val check = if(fromMediaObserver) true else isImagesListEmpty()
            if (check) {
                val timeFormat = "dd,MMMM,yyyy"
                activity?.let { mActivity ->
                    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    val projection = arrayOf(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.TITLE,
                        MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.DATE_ADDED,
                        MediaStore.Images.Media.DATE_MODIFIED,
                        MediaStore.Images.Media.MIME_TYPE,
                        MediaStore.Images.Media.DATA
                    )

//                    val selection = MediaStore.Images.Media.MIME_TYPE + " ='image/jpeg' OR " +
//                            MediaStore.Images.Media.MIME_TYPE + " ='image/png' OR " +
//                            MediaStore.Images.Media.MIME_TYPE + " ='image/webp' OR "

                    val orderBy = MediaStore.Images.Media.DATE_MODIFIED + " ASC"

                    val loaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
                        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                            return CursorLoader(
                                mActivity,
                                uri,
                                projection,
                                null,
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
                                                data.getString(data.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                                            val mDataId =
                                                data.getLong(data.getColumnIndex(MediaStore.Images.Media._ID))
                                            val mTitle =
                                                data.getString(data.getColumnIndex(MediaStore.Images.Media.TITLE))
                                                    ?: "<Unknown>"
                                            val mSize =
                                                data.getLong(data.getColumnIndex(MediaStore.Images.Media.SIZE))
                                            val mDateAdded =
                                                data.getLong(data.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
                                            val mDateModified =
                                                data.getLong(data.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED))
                                            val vdMimeType =
                                                data.getString(data.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))?:"image"
                                            val vdPath =
                                                data.getString(data.getColumnIndex(MediaStore.Images.Media.DATA))


                                            vdPath?.let { path ->
                                                if (File(path).exists()) {
                                                    mBucket?.let {
                                                        if (folderMap.containsKey(it)) {
                                                            val listOfValues = folderMap[it]
                                                            listOfValues!!.add(
                                                                ImageItem(
                                                                    id = mDataId,
                                                                    imageName = mTitle,
                                                                    imageSizeTitle = GeneralUtils.readableFileSize(mSize),
                                                                    imageSize = mSize,
                                                                    imageDateAddedTitle = GeneralUtils.readableTimeStamp(mDateAdded, timeFormat),
                                                                    imageDateAdded = mDateAdded,
                                                                    imageDateModifiedTitle = GeneralUtils.readableTimeStamp(mDateModified, timeFormat),
                                                                    imageDateModified = mDateModified,
                                                                    imageMimeType = vdMimeType,
                                                                    imagePath = path
                                                                )
                                                            )
                                                            folderMap.put(it, listOfValues)

                                                        } else {
                                                            val listOfValues = ArrayList<ImageItem>()
                                                            listOfValues.add(
                                                                ImageItem(
                                                                    id = mDataId,
                                                                    imageName = mTitle,
                                                                    imageSizeTitle = GeneralUtils.readableFileSize(mSize),
                                                                    imageSize = mSize,
                                                                    imageDateAddedTitle = GeneralUtils.readableTimeStamp(mDateAdded, timeFormat),
                                                                    imageDateAdded = mDateAdded,
                                                                    imageDateModifiedTitle = GeneralUtils.readableTimeStamp(mDateModified, timeFormat),
                                                                    imageDateModified = mDateModified,
                                                                    imageMimeType = vdMimeType,
                                                                    imagePath = path
                                                                )
                                                            )
                                                            folderMap.put(it, listOfValues)

                                                        }
                                                    }
                                                    loadedImagesList.add(
                                                        ImageItem(
                                                            id = mDataId,
                                                            imageName = mTitle,
                                                            imageSizeTitle = GeneralUtils.readableFileSize(mSize),
                                                            imageSize = mSize,
                                                            imageDateAddedTitle = GeneralUtils.readableTimeStamp(mDateAdded, timeFormat),
                                                            imageDateAdded = mDateAdded,
                                                            imageDateModifiedTitle = GeneralUtils.readableTimeStamp(mDateModified, timeFormat),
                                                            imageDateModified = mDateModified,
                                                            imageMimeType = vdMimeType,
                                                            imagePath = path
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                        data.close()
                                    }
                                    clearImagesListData()
                                    clearFolderMap()

                                }.await()
                                isImagesFetching = false
                                imagesList.addAll(loadedImagesList)
                                imagesFolderMap.putAll(folderMap)

                                setImagesUpdated(true)
                                LoaderManager.getInstance(mActivity as AppCompatActivity)
                                    .destroyLoader(IMAGE_CODE)
                            }
                        }

                        override fun onLoaderReset(loader: Loader<Cursor>) {}
                    }
                    LoaderManager.getInstance(mActivity as AppCompatActivity)
                        .initLoader(IMAGE_CODE, null, loaderCallbacks)

                } ?: kotlin.run {
                    isImagesFetching = false
                    setImagesUpdated(true)
                }
            } else {
                isImagesFetching = false
                setImagesUpdated(true)
            }
        }
    }

    /**
     * Retrieve data for all Images
     */
    //    val allImages: List<ImageItem> get() = imagesList.toList()
    fun getAllImages(): MutableList<ImageItem> {
        val list = mutableListOf<ImageItem>()
        list.addAll(imagesList)
        return list
    }

    fun isImagesListEmpty() = imagesList.isEmpty()

    private fun clearImagesListData() = imagesList.clear()

    fun imageSize() = imagesList.size

    /**
     * Retrieve all data for folders
     */

    fun foldersSize() = imagesFolderMap.size
    fun folderImagesListSize(folderName: String) = imagesFolderMap[folderName]?.size ?: 0
    fun isFolderMapEmpty() = imagesFolderMap.isEmpty()
    fun isFolderImagesListEmpty(folderName: String) = imagesFolderMap[folderName]?.isEmpty() ?: true
    fun getFoldersList(): MutableList<FolderItem> {
        val list = mutableListOf<FolderItem>()
        list.addAll(imagesFolderMap.entries
            .asSequence()
            .filter {
                it.value.isNotEmpty()
            }
            .map {
                it.key to it.value[0]
            }
            .map {
                var folderSize = 0
                val mutableList1 = imagesFolderMap[it.first]?.size
                mutableList1?.let { fSize ->
                    folderSize = fSize
                }
                FolderItem(it.first, it.second.imagePath, folderSize)
            }
            .sortedBy {
                it.folderName.lowercase()
            }
            .toMutableList())
        return list
    }
    fun getImagesFromFolder(folderName: String) = imagesFolderMap[folderName] ?: mutableListOf()
    private fun clearFolderMap() = imagesFolderMap.clear()

    /**
     * When images updated from external storage
     * fetch again files
     */

    fun mediaObserve(activity: Activity?){
        fetchingImages(activity,true)
    }

    /**
     * Do yourself
    Implement operations for deleting, renaming, sorting, and searching within the
    context of imagesList, imagesFolderMap and others
    Provide comprehensive functionality for these features.
     */

}