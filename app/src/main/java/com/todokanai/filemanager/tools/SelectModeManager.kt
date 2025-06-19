package com.todokanai.filemanager.tools

class SelectModeManager(val onDefaultMode:()->Unit):SelectModeManagerLogics() {

    override fun toDefaultMode() {
        super.toDefaultMode()
        onDefaultMode()
    }

    override fun toMultiSelectMode() {
        super.toMultiSelectMode()
    }

    override fun toConfirmCopyMode() {
        super.toConfirmCopyMode()
    }

    override fun toConfirmMoveMode() {
        super.toConfirmMoveMode()
    }

}