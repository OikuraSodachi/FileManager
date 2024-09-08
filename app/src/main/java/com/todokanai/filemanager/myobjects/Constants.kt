package com.todokanai.filemanager.myobjects

object Constants {
    const val CHANNEL_ID = "Todokanai_FileManager"

    /** notification channel for "onComplete" **/
    const val NOTIFICATION_CHANNEL_ID_COMPLETED : String = "Completed"

    const val DEFAULT_MODE : Int = 10
    const val MULTI_SELECT_MODE : Int = 11
    const val CONFIRM_MODE_COPY : Int = 12
    const val CONFIRM_MODE_MOVE : Int = 13
    const val CONFIRM_MODE_UNZIP : Int = 14
    const val CONFIRM_MODE_UNZIP_HERE : Int = 15

    const val ACTION_KEY_COPY : Int = 20
    const val ACTION_KEY_MOVE : Int = 21
    const val ACTION_KEY_DELETE : Int = 22
    const val ACTION_KEY_ZIP : Int = 23
    const val ACTION_KEY_UNZIP : Int = 24

    const val BY_DEFAULT : String = "BY_DEFAULT"
    const val BY_NAME_ASCENDING : String = "BY_NAME_ASCENDING"
    const val BY_NAME_DESCENDING : String = "BY_NAME_DESCENDING"
    const val BY_SIZE_ASCENDING : String = "BY_SIZE_ASCENDING"
    const val BY_SIZE_DESCENDING : String = "BY_SIZE_DESCENDING"
    const val BY_TYPE_ASCENDING : String = "BY_TYPE_ASCENDING"
    const val BY_TYPE_DESCENDING : String = "BY_TYPE_DESCENDING"
    const val BY_DATE_ASCENDING : String = "BY_DATE_ASCENDING"
    const val BY_DATE_DESCENDING : String = "BY_DATE_DESCENDING"

    /** TestWorker seed의 key 값 **/
    const val WORKER_TEST_SEED : String = "seed"

    /** array of selected Files **/
    const val WORKER_KEY_SELECTED_FILES = "selected"

    /** targeted Directory **/
    const val WORKER_KEY_TARGET_DIRECTORY = "targetDirectory"

    /** notification message when work is completed **/
    const val WORKER_KEY_NOTIFICATION_COMPLETE_MESSAGE = "notification_complete_message"

    /** notification title when work is completed **/
    const val WORKER_KEY_NOTIFICATION_COMPLETE_TITLE = "notification_complete_title"

    /** whether notification should be silent **/
    const val WORKER_KEY_IS_SILENT = "notification_is_silent"

    const val NOTIFICATION_CHANNEL_ID_WORK : Int = 1
}