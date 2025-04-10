package com.todokanai.filemanager.tools.independent

/**
 * 독립적으로 사용 가능하고, Android 의존성 있는 method 모음
 */
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.storage.StorageManager
import android.provider.Settings
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.system.exitProcess

/** Todokanai
 *
 * Application 종료 method
 *
 * Service가 실행중일 경우 서비스를 실행한 Intent도 입력할것
 */
fun exit_td(activity: Activity, serviceIntent: Intent? = null) {
    ActivityCompat.finishAffinity(activity)
    serviceIntent?.let { activity.stopService(it) }     // 서비스 종료
    System.runFinalization()
    exitProcess(0)
}

suspend fun getThumbnail_td(file: File, width: Int = 100, height: Int = 100): Bitmap =
    withContext(Dispatchers.IO) {
        ThumbnailUtils.extractThumbnail(
            BitmapFactory.decodeFile(file.absolutePath), width, height
        )
    }


/**
 *  looper.
 *
 */
fun callHandler_td(
    handler: Handler,
    callback: () -> Unit
) {
    handler.post({ callback() })
}

/*

/** Logcat Tag 매크로 용도 **/
fun setupTimber_td() {
    Timber.plant(
        object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                val className = super.createStackElementTag(element)
                return "TAG $className ${element.methodName}"
            }
        }
    )
}

 */

fun setMediaPlaybackState_td(state: Int, mediaSession: MediaSessionCompat) {
    val playbackState = PlaybackStateCompat.Builder()
        .run {
            val actions = if (state == PlaybackStateCompat.STATE_PLAYING) {
                PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SET_REPEAT_MODE or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
            } else {
                PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SET_REPEAT_MODE or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
            }
            setActions(actions)
        }
        .setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, 0f)
    mediaSession.setPlaybackState(playbackState.build())
}

fun isPermissionGranted_td(activity: Activity, permission: String): Boolean {
    val result = ContextCompat.checkSelfPermission(activity, permission)
    return result == PackageManager.PERMISSION_GRANTED
}


/** requestCode를 111로 냅둬도 무방한게 정말 맞는지 확인 필요
 *
 *  permissionNotice : 권한이 필요함을 사용자에게 알림
 *
 * system에 permission n개를 한번에 요청함
 **/
fun requestPermission_td(
    activity: Activity,
    permissions: Array<String>,
    permissionNotice: () -> Unit,
    requestCode: Int = 111
) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            permissions.first()
        )
    ) {
        permissionNotice()
    } else {
        ActivityCompat.requestPermissions(
            activity,
            permissions,
            requestCode
        )
    }
}

/** Todokanai
 *
 * == Toast.makeText(appContext,text,Toast.LENGTH_SHORT).show() **/
fun ToastShort_td(appContext: Context, text: String) {
    Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()
}

/** Todokanai
 *
 * 기기의 storage 목록 반환 **/
fun getPhysicalStorages_td(context: Context): Array<File> {
    val defaultStorage = Environment.getExternalStorageDirectory()
    val volumes = context.getSystemService(StorageManager::class.java)?.storageVolumes
    val storageList = mutableListOf<File>(defaultStorage)
    volumes?.forEach { volume ->
        if (!volume.isPrimary && volume.isRemovable) {
            val sdCard = volume.directory
            if (sdCard != null) {
                storageList.add(sdCard)
            }
        }
    }
    return storageList.toTypedArray()
}

/** Todokanai
 *
 * 모든 파일 접근 권한 요청 **/
fun requestStorageManageAccess_td(activity: Activity) {
    val intent = Intent()
    intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
    val uri: Uri = Uri.fromParts("package", activity.packageName, null)
    intent.data = uri
    activity.startActivity(intent)
}

/** Todokanai
 *
 *  open the file with a compatible application
 *
 *  requires ContentProvider
 *
 *  onFailure: no application available to open the file, etc...
 *
 *  mimeType: Mime type of the given file
 * **/
fun openFile_td(
    context: Context,
    file: File,
    mimeType: String,
    onFailure: () -> Unit = {}
) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    intent.setDataAndType(
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        ), mimeType
    )
    // println("mimeType: $mimeType")
    try {
        ActivityCompat.startActivity(context, intent, null)
    } catch (t: Throwable) {
        println(t)
        onFailure()
    }
}


/** Todokanai
 *
 *  @param uri uri of the file
 *
 *  open the file with a compatible application
 *
 *  onFailure: no application available to open the file, etc...
 *
 *  mimeType: Mime type of the given file **/
fun openFileFromUri_td(
    context: Context,
    uri: Uri,
    mimeType: String,
    onFailure: () -> Unit = {}
) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    intent.setDataAndType(uri, mimeType)
    // println("mimeType: $mimeType")
    try {
        ActivityCompat.startActivity(context, intent, null)
    } catch (t: Throwable) {
        println(t)
        onFailure()
    }
}


/** Todokanai
 *
 *  itemList: List of Pair<( ItemTitle ),( Callback ) >
 *
 * **/
fun popupMenu_td(
    context: Context,
    anchor: View,
    itemList: List<Pair<String, () -> Unit>>,
    gravity: Int = 0  // == Gravity.NO_GRAVITY
) {
    val popupMenu = PopupMenu(context, anchor, gravity)
    popupMenu.run {
        itemList.forEach {
            menu.add(it.first)
        }
        setOnMenuItemClickListener { item ->
            itemList.forEach {
                if (item.title == it.first) {
                    it.second()
                }
            }
            false
        }
        show()
    }
}